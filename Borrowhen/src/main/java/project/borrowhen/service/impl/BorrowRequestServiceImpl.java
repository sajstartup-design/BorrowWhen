package project.borrowhen.service.impl;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import project.borrowhen.common.constant.CommonConstant;
import project.borrowhen.common.util.CalculationUtil;
import project.borrowhen.common.util.CipherUtil;
import project.borrowhen.common.util.DateFormatUtil;
import project.borrowhen.dao.BorrowRequestDao;
import project.borrowhen.dao.PaymentDao;
import project.borrowhen.dao.entity.BorrowRequestData;
import project.borrowhen.dao.entity.BorrowRequestEntity;
import project.borrowhen.dao.entity.InventoryEntity;
import project.borrowhen.dao.entity.NotificationEntity;
import project.borrowhen.dao.entity.UserEntity;
import project.borrowhen.dto.BorrowRequestDto;
import project.borrowhen.dto.PaymentDto;
import project.borrowhen.object.BorrowRequestObj;
import project.borrowhen.object.FilterAndSearchObj;
import project.borrowhen.object.PaginationObj;
import project.borrowhen.object.UserObj;
import project.borrowhen.service.AdminSettingsService;
import project.borrowhen.service.BorrowRequestService;
import project.borrowhen.service.InventoryService;
import project.borrowhen.service.NotificationService;
import project.borrowhen.service.PaymentService;
import project.borrowhen.service.UserService;

@Service
public class BorrowRequestServiceImpl implements BorrowRequestService{
	
	@Autowired
	private BorrowRequestDao borrowRequestDao;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private NotificationService notificationService;
	
	@Autowired
	private InventoryService inventoryService;
	
	@Autowired
	private PaymentService paymentService;
	
    @Autowired
    private SimpMessagingTemplate messagingTemplate;
	
	@Autowired
	private CipherUtil cipherUtil;
	
	@Autowired
	private AdminSettingsService adminSettingsService;
    
    private int getMaxRequestDisplay() {
        return adminSettingsService.getSettings().getRequestPerPage();
    }

	@Override
	public void saveBorrowRequest(BorrowRequestDto inDto) throws Exception {
		
		Timestamp dateNow = DateFormatUtil.getCurrentTimestamp();
		
		UserEntity user = userService.getLoggedInUser();
		
		int id = Integer.valueOf(cipherUtil.decrypt(inDto.getEncryptedId()));
		
		InventoryEntity inventory = inventoryService.getInventory(id);
		
		UserEntity lender = userService.getUser(inventory.getUserId());
		
		BorrowRequestEntity request = new BorrowRequestEntity();
		
		request.setInventoryId(id);
		request.setUserId(user.getId());
		request.setItemName(inventory.getItemName());		
		request.setDateToBorrow(Date.valueOf(inDto.getDateToBorrow()));
		request.setDateToReturn(Date.valueOf(inDto.getDateToReturn()));
		request.setQty(inDto.getQty());
		request.setPrice(inventory.getPrice());
		request.setStatus(CommonConstant.PENDING);;
		request.setCreatedDate(dateNow);
		request.setUpdatedDate(dateNow);
		request.setIsDeleted(false);
	
		borrowRequestDao.save(request);
		
		inventoryService.updateInventoryAvailableQty(id, inDto.getQty(), CommonConstant.DECREASE);
		
		NotificationEntity notification = new NotificationEntity();
		notification.setUserId(inventory.getUserId());
		
		String message = String.format(
		    "%s %s has requested to borrow '%s' from %s to %s.",
		    user.getFirstName(),
		    user.getFamilyName(),
		    inventory.getItemName(),
		    inDto.getDateToBorrow(),
		    inDto.getDateToReturn()
		);
		notification.setMessage(message);
		notification.setIsRead(false);
		notification.setType(CommonConstant.REQUEST_PENDING);
		notification.setCreatedDate(dateNow);
		notification.setUpdatedDate(dateNow);
		notification.setIsDeleted(false);
		
		notificationService.saveNotification(notification);	
		
		messagingTemplate.convertAndSendToUser(
			lender.getUserId().toString(),   
		    "/queue/lender/notifications",           
		    message                           
		);
		
	}

	@Override
	public BorrowRequestDto getAllBorrowRequest(BorrowRequestDto inDto) throws Exception {
	    
	    BorrowRequestDto outDto = new BorrowRequestDto();
	    
	    Pageable pageable = PageRequest.of(
	        inDto.getPagination().getPage(),
	        Integer.valueOf(getMaxRequestDisplay())
	    );
	    
	    Page<BorrowRequestData> allRequests = borrowRequestDao.getAllBorrowRequests(pageable);
	    
	    List<BorrowRequestObj> requests = new ArrayList<>();
	    
	    for (BorrowRequestData request : allRequests) {
	        BorrowRequestObj obj = new BorrowRequestObj();
	        
	        obj.setEncryptedId(cipherUtil.encrypt(String.valueOf(request.getBorrowRequestId())));
	        
	        String borrowerFullName = request.getBorrowerFirstName() + " " + request.getBorrowerFamilyName();
	        obj.setBorrower(borrowerFullName.trim());
	        
	        String lenderFullName = request.getLenderFirstName() + " " + request.getLenderFamilyName();
	        obj.setLender(lenderFullName.trim());
	        
	        obj.setItemName(request.getItemName());
	        obj.setPrice(request.getPrice());
	        obj.setQty(request.getQty());
	        obj.setDateToBorrow(request.getDateToBorrow());
	        obj.setDateToReturn(request.getDateToReturn());
	        obj.setStatus(request.getStatus());
			obj.setCreatedDate(DateFormatUtil.formatTimestampToString(request.getCreatedDate()));
			obj.setUpdatedDate(DateFormatUtil.formatTimestampToString(request.getUpdatedDate()));		
	        
	        requests.add(obj);
	    }
	    
	    PaginationObj pagination = new PaginationObj();
		
		pagination.setPage(allRequests.getNumber());
		pagination.setTotalPages(allRequests.getTotalPages());
		pagination.setTotalElements(allRequests.getTotalElements());
		pagination.setHasNext(allRequests.hasNext());
		pagination.setHasPrevious(allRequests.hasPrevious());
		
		outDto.setRequests(requests);
		outDto.setPagination(pagination);
		
	    return outDto;
	}

	@Override
	public void approveBorrowRequest(BorrowRequestDto inDto) throws Exception {
		
		Timestamp dateNow = DateFormatUtil.getCurrentTimestamp();
	    
	    int id = Integer.valueOf(cipherUtil.decrypt(inDto.getEncryptedId()));
	    
	    UserEntity user = userService.getLoggedInUser();
	    
	    BorrowRequestEntity request = borrowRequestDao.getBorrowRequest(id);

	    UserEntity borrower = userService.getUser(request.getUserId()); 

	    // Update status → APPROVED
	    borrowRequestDao.updateBorrowRequestStatusById(id, CommonConstant.APPROVED);

	    NotificationEntity notification = new NotificationEntity();
	    notification.setUserId(request.getUserId());
	    
	    String approvedBy = CommonConstant.ROLE_ADMIN.equals(user.getRole())
	            ? "the admin" 
	            : "the lender";

	    String message = String.format(
	            "Your borrow request for '%s' from %s to %s has been approved by %s.",
	            request.getItemName(),
	            request.getDateToBorrow(),
	            request.getDateToReturn(),
	            approvedBy
	        );

	    notification.setMessage(message);
	    notification.setIsRead(false);
	    notification.setType(CommonConstant.REQUEST_APPROVED);
	    notification.setCreatedDate(dateNow);
	    notification.setUpdatedDate(dateNow);
	    notification.setIsDeleted(false);

	    notificationService.saveNotification(notification);	

	    messagingTemplate.convertAndSendToUser(
    		borrower.getUserId().toString(),
	        "/queue/borrower/notifications",
	        message
	    );
	}


	@Override
	public void rejectBorrowRequest(BorrowRequestDto inDto) throws Exception {
		
		Timestamp dateNow = DateFormatUtil.getCurrentTimestamp();
	    
	    int id = Integer.valueOf(cipherUtil.decrypt(inDto.getEncryptedId()));
	    
	    BorrowRequestEntity request = borrowRequestDao.getBorrowRequest(id);

	    UserEntity borrower = userService.getUser(request.getUserId()); 

	    // Update status → APPROVED
	    borrowRequestDao.updateBorrowRequestStatusById(id, CommonConstant.REJECTED);
	    
	    inventoryService.updateInventoryAvailableQty(request.getInventoryId(), request.getQty(), CommonConstant.INCREASE);

	    NotificationEntity notification = new NotificationEntity();
	    notification.setUserId(request.getUserId());

	    String message = String.format(
	        "Your borrow request for '%s' from %s to %s has been rejected by the lender.",
	        request.getItemName(),
	        request.getDateToBorrow(),
	        request.getDateToReturn()
	    );

	    notification.setMessage(message);
	    notification.setIsRead(false);
	    notification.setType(CommonConstant.REQUEST_REJECTED);
	    notification.setCreatedDate(dateNow);
	    notification.setUpdatedDate(dateNow);
	    notification.setIsDeleted(false);

	    notificationService.saveNotification(notification);	

	    messagingTemplate.convertAndSendToUser(
    		borrower.getUserId().toString(),
	        "/queue/borrower/notifications",
	        message
	    );
		
	
	}

	@Override
	public BorrowRequestDto getAllOwnedBorrowRequestForLender(BorrowRequestDto inDto) throws Exception {
		
		BorrowRequestDto outDto = new BorrowRequestDto();
	    
	    Pageable pageable = PageRequest.of(
	        inDto.getPagination().getPage(),
	        Integer.valueOf(getMaxRequestDisplay())
	    );
	    
	    UserEntity user = userService.getLoggedInUser();
	    
	    FilterAndSearchObj filter = inDto.getFilter();
	    
	    Page<BorrowRequestData> allRequests = borrowRequestDao.getAllOwnedBorrowRequestsForLender(pageable, user.getId(), filter.getSearch());
	    
	    List<BorrowRequestObj> requests = new ArrayList<>();
	    
	    for (BorrowRequestData request : allRequests) {
	        BorrowRequestObj obj = new BorrowRequestObj();
	        
	        obj.setEncryptedId(cipherUtil.encrypt(String.valueOf(request.getBorrowRequestId())));
	        
	        String borrowerFullName = request.getBorrowerFirstName() + " " + request.getBorrowerFamilyName();
	        obj.setBorrower(borrowerFullName.trim());
	        	        
	        obj.setItemName(request.getItemName());
	        obj.setPrice(request.getPrice());
	        obj.setQty(request.getQty());
	        obj.setDateToBorrow(request.getDateToBorrow());
	        obj.setDateToReturn(request.getDateToReturn());
	        obj.setStatus(request.getStatus());	
	        
	        requests.add(obj);
	    }
	    
	    PaginationObj pagination = new PaginationObj();
		
		pagination.setPage(allRequests.getNumber());
		pagination.setTotalPages(allRequests.getTotalPages());
		pagination.setTotalElements(allRequests.getTotalElements());
		pagination.setHasNext(allRequests.hasNext());
		pagination.setHasPrevious(allRequests.hasPrevious());
		
		outDto.setRequests(requests);
		outDto.setPagination(pagination);
		
	    return outDto;
	}

	@Override
	public BorrowRequestDto getAllOwnedBorrowRequestForBorrower(BorrowRequestDto inDto) throws Exception {
			
		BorrowRequestDto outDto = new BorrowRequestDto();
	    
	    Pageable pageable = PageRequest.of(
	        inDto.getPagination().getPage(),
	        Integer.valueOf(getMaxRequestDisplay())
	    );
	    
	    UserEntity user = userService.getLoggedInUser();
	    
	    FilterAndSearchObj filter = inDto.getFilter();
	    
	    Page<BorrowRequestData> allRequests = borrowRequestDao.getAllOwnedBorrowRequestsForBorrower(pageable, user.getId(), filter.getSearch());
	    
	    List<BorrowRequestObj> requests = new ArrayList<>();
	    
	    for (BorrowRequestData request : allRequests) {
	        BorrowRequestObj obj = new BorrowRequestObj();
	        
	        obj.setEncryptedId(cipherUtil.encrypt(String.valueOf(request.getBorrowRequestId())));
	        	        
	        obj.setItemName(request.getItemName());
	        obj.setPrice(request.getPrice());
	        obj.setQty(request.getQty());
	        obj.setDateToBorrow(request.getDateToBorrow());
	        obj.setDateToReturn(request.getDateToReturn());
	        obj.setStatus(request.getStatus());	
	        
	        requests.add(obj);
	    }
	    
	    PaginationObj pagination = new PaginationObj();
		
		pagination.setPage(allRequests.getNumber());
		pagination.setTotalPages(allRequests.getTotalPages());
		pagination.setTotalElements(allRequests.getTotalElements());
		pagination.setHasNext(allRequests.hasNext());
		pagination.setHasPrevious(allRequests.hasPrevious());
		
		outDto.setRequests(requests);
		outDto.setPagination(pagination);
		
	    return outDto;
	}

	@Override
	public void itemReceivedBorrowRequest(BorrowRequestDto inDto) throws Exception {

	    Timestamp dateNow = DateFormatUtil.getCurrentTimestamp();

	    int id = Integer.valueOf(cipherUtil.decrypt(inDto.getEncryptedId()));

	    BorrowRequestEntity request = borrowRequestDao.getBorrowRequest(id);

	    UserEntity borrower = userService.getUser(request.getUserId()); 
	    
	    InventoryEntity inventory = inventoryService.getInventory(request.getInventoryId());
	    
	    UserEntity lender = userService.getUser(inventory.getUserId());
	    
	    borrowRequestDao.updateBorrowRequestStatusById(id, CommonConstant.ON_GOING);
	    
	    NotificationEntity notification = new NotificationEntity();
	    notification.setUserId(lender.getId());

	    String message = String.format(
	        "%s %s has marked the borrow request for '%s' from %s to %s as RECEIVED.",
	        borrower.getFirstName(),
	        borrower.getFamilyName(),
	        request.getItemName(),
	        request.getDateToBorrow(),
	        request.getDateToReturn()
	    );

	    notification.setMessage(message);
	    notification.setIsRead(false);
	    notification.setType(CommonConstant.REQUEST_ITEM_RECEIVED);
	    notification.setCreatedDate(dateNow);
	    notification.setUpdatedDate(dateNow);
	    notification.setIsDeleted(false);

	    notificationService.saveNotification(notification);

	    messagingTemplate.convertAndSendToUser(
	        lender.getUserId().toString(),
	        "/queue/lender/notifications",
	        message
	    );
	}

	@Override
	public BorrowRequestDto getBorrowRequest(BorrowRequestDto inDto) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BorrowRequestDto getBorrowRequestDetailsForLender(BorrowRequestDto inDto) throws Exception {
		
		BorrowRequestDto outDto = new BorrowRequestDto();
    
	    int id = Integer.valueOf(cipherUtil.decrypt(inDto.getEncryptedId()));
	    
	    BorrowRequestEntity request = borrowRequestDao.getBorrowRequest(id);
	    
	    UserEntity borrower = userService.getUser(request.getUserId());
	    
	    BorrowRequestObj obj = new BorrowRequestObj();
        
        obj.setEncryptedId(cipherUtil.encrypt(String.valueOf(request.getId())));
        obj.setItemName(request.getItemName());
        obj.setPrice(request.getPrice());
        obj.setQty(request.getQty());
        obj.setDateToBorrow(request.getDateToBorrow());
        obj.setDateToReturn(request.getDateToReturn());
        obj.setStatus(request.getStatus());	     
        
        UserObj borrowerObj = new UserObj();
        
        borrowerObj.setFirstName(borrower.getFirstName());
        borrowerObj.setMiddleName(borrower.getMiddleName());
        borrowerObj.setFamilyName(borrower.getFamilyName());
        borrowerObj.setEmailAddress(borrower.getEmailAddress());
        borrowerObj.setPhoneNumber(borrower.getPhoneNumber());
        borrowerObj.setGender(borrower.getGender());       
	           
        outDto.setRequest(obj);
        outDto.setBorrower(borrowerObj);       
	
	    return outDto;
	    
	}
	

	@Override
	public BorrowRequestDto getBorrowRequestDetailsForBorrower(BorrowRequestDto inDto) throws Exception {
		
		BorrowRequestDto outDto = new BorrowRequestDto();
	    
	    int id = Integer.valueOf(cipherUtil.decrypt(inDto.getEncryptedId()));
	    
	    BorrowRequestEntity request = borrowRequestDao.getBorrowRequest(id);
	    
	    BorrowRequestObj obj = new BorrowRequestObj();
        
        obj.setEncryptedId(cipherUtil.encrypt(String.valueOf(request.getId())));
        obj.setItemName(request.getItemName());
        obj.setPrice(request.getPrice());
        obj.setQty(request.getQty());
        obj.setDateToBorrow(request.getDateToBorrow());
        obj.setDateToReturn(request.getDateToReturn());
        obj.setStatus(request.getStatus());	     
                   
        outDto.setRequest(obj);    
	
	    return outDto;
	}

	@Override
	public void itemReturnedBorrowRequest(BorrowRequestDto inDto) throws Exception {
		
		Timestamp dateNow = DateFormatUtil.getCurrentTimestamp();

	    int id = Integer.valueOf(cipherUtil.decrypt(inDto.getEncryptedId()));

	    BorrowRequestEntity request = borrowRequestDao.getBorrowRequest(id);

	    UserEntity borrower = userService.getUser(request.getUserId()); 

	    borrowRequestDao.updateBorrowRequestStatusById(id, CommonConstant.COMPLETED);
	    
	    NotificationEntity notification = new NotificationEntity();
	    notification.setUserId(borrower.getId());

	    String message = String.format(
    	    "The item '%s' has been returned. Please wait while the lender processes your payment. You will be notified once the payment has been issued.",
    	    request.getItemName()
    	);
	    
	    inventoryService.updateInventoryAvailableQty(request.getInventoryId(), request.getQty(), CommonConstant.INCREASE);
	    
	    notification.setMessage(message);
	    notification.setIsRead(false);
	    notification.setType(CommonConstant.REQUEST_COMPLETED);
	    notification.setCreatedDate(dateNow);
	    notification.setUpdatedDate(dateNow);
	    notification.setIsDeleted(false);

	    notificationService.saveNotification(notification);

	    messagingTemplate.convertAndSendToUser(
    		borrower.getUserId().toString(),
	        "/queue/borrower/notifications",
	        message
	    );
		
	}

	@Override
	public void itemPickUpBorrowRequest(BorrowRequestDto inDto) throws Exception {
		
		Timestamp dateNow = DateFormatUtil.getCurrentTimestamp();

	    int id = Integer.valueOf(cipherUtil.decrypt(inDto.getEncryptedId()));

	    BorrowRequestEntity request = borrowRequestDao.getBorrowRequest(id);

	    UserEntity borrower = userService.getUser(request.getUserId()); 
	      
	    borrowRequestDao.updateBorrowRequestStatusById(id, CommonConstant.PICK_UP_READY);
	    
	    NotificationEntity notification = new NotificationEntity();
	    notification.setUserId(borrower.getId());

	    String message = String.format(
    	    "Your borrow request for '%s' from %s to %s is now ready for pick-up.",
    	    request.getItemName(),
    	    request.getDateToBorrow(),
    	    request.getDateToReturn()
    	);


	    notification.setMessage(message);
	    notification.setIsRead(false);
	    notification.setType(CommonConstant.REQUEST_ITEM_PICK_UP_READY);
	    notification.setCreatedDate(dateNow);
	    notification.setUpdatedDate(dateNow);
	    notification.setIsDeleted(false);

	    notificationService.saveNotification(notification);

	    messagingTemplate.convertAndSendToUser(
    		borrower.getUserId().toString(),
	        "/queue/borrower/notifications",
	        message
	    );
	}

	@Override
	public void issuePaymentBorrowRequest(BorrowRequestDto inDto) throws Exception {
		
		Timestamp dateNow = DateFormatUtil.getCurrentTimestamp();

	    int id = Integer.valueOf(cipherUtil.decrypt(inDto.getEncryptedId()));

	    BorrowRequestEntity request = borrowRequestDao.getBorrowRequest(id);

	    UserEntity borrower = userService.getUser(request.getUserId()); 

	    borrowRequestDao.updateBorrowRequestStatusById(id, CommonConstant.PENDING_PAYMENT);
	    
	    PaymentDto paymentDto = new PaymentDto();
	    
	    paymentDto.setAmount(CalculationUtil.getTotalPrice(request.getQty(), request.getPrice()));
	    paymentDto.setEmailAddress(borrower.getEmailAddress());
	    paymentDto.setBorrowRequestId(id);
	    
	    paymentService.createPaymentIntent(paymentDto);
	    
	    NotificationEntity notification = new NotificationEntity();
	    notification.setUserId(borrower.getId());

	    String message = String.format(
    	    "Please complete the payment for the item '%s'. <a href=\"/payment?encryptedId=%s\">Click here to continue</a>.",
    	    request.getItemName(),
    	    inDto.getEncryptedId()
    	);


	    notification.setMessage(message);
	    notification.setIsRead(false);
	    notification.setType(CommonConstant.REQUEST_PAYMENT_PENDING);
	    notification.setCreatedDate(dateNow);
	    notification.setUpdatedDate(dateNow);
	    notification.setIsDeleted(false);

	    notificationService.saveNotification(notification);

	    messagingTemplate.convertAndSendToUser(
    		borrower.getUserId().toString(),
	        "/queue/borrower/notifications",
	        message
	    );
		
	}

	@Override
	public void paidBorrowRequest(BorrowRequestDto inDto) throws Exception {
		
		Timestamp dateNow = DateFormatUtil.getCurrentTimestamp();

	    int id = Integer.valueOf(cipherUtil.decrypt(inDto.getEncryptedId()));

	    BorrowRequestEntity request = borrowRequestDao.getBorrowRequest(id);

	    UserEntity borrower = userService.getUser(request.getUserId()); 
	    
	    InventoryEntity inventory = inventoryService.getInventory(request.getInventoryId());
	    
	    UserEntity lender = userService.getUser(inventory.getUserId());
	    
	    borrowRequestDao.updateBorrowRequestStatusById(id, CommonConstant.PAID);
	    
	    PaymentDto paymentInDto = new PaymentDto();
	    
	    paymentInDto.setBorrowRequestId(id);
	    paymentInDto.setStatus(CommonConstant.PAID);
	    paymentInDto.setEmailAddress(borrower.getEmailAddress());
	    
	    paymentService.updatePaymentStatus(paymentInDto);
	    
	    NotificationEntity notification = new NotificationEntity();
	    notification.setUserId(lender.getId());

	    String message = String.format(
    	    "%s %s has paid for the borrow request of '%s' from %s to %s.",
    	    borrower.getFirstName(),
    	    borrower.getFamilyName(),
    	    request.getItemName(),
    	    request.getDateToBorrow(),
    	    request.getDateToReturn()
    	);


	    notification.setMessage(message);
	    notification.setIsRead(false);
	    notification.setType(CommonConstant.REQUEST_PAID);
	    notification.setCreatedDate(dateNow);
	    notification.setUpdatedDate(dateNow);
	    notification.setIsDeleted(false);

	    notificationService.saveNotification(notification);

	    messagingTemplate.convertAndSendToUser(
	        lender.getUserId().toString(),
	        "/queue/lender/notifications",
	        message
	    );
	}

}
