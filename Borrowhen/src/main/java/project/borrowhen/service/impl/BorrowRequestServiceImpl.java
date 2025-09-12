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

import jakarta.servlet.http.HttpSession;
import project.borrowhen.common.constant.CommonConstant;
import project.borrowhen.common.util.CipherUtil;
import project.borrowhen.common.util.DateFormatUtil;
import project.borrowhen.dao.BorrowRequestDao;
import project.borrowhen.dao.entity.BorrowRequestData;
import project.borrowhen.dao.entity.BorrowRequestEntity;
import project.borrowhen.dao.entity.InventoryEntity;
import project.borrowhen.dao.entity.NotificationEntity;
import project.borrowhen.dao.entity.UserEntity;
import project.borrowhen.dto.BorrowRequestDto;
import project.borrowhen.object.BorrowRequestObj;
import project.borrowhen.object.PaginationObj;
import project.borrowhen.service.AdminSettingsService;
import project.borrowhen.service.BorrowRequestService;
import project.borrowhen.service.InventoryService;
import project.borrowhen.service.NotificationService;
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
	    
	    Page<BorrowRequestData> allRequests = borrowRequestDao.getAllOwnedBorrowRequestsForLender(pageable, user.getId());
	    
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
	    
	    Page<BorrowRequestData> allRequests = borrowRequestDao.getAllOwnedBorrowRequestsForBorrower(pageable, user.getId());
	    
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


}
