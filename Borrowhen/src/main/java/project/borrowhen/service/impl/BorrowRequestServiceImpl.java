package project.borrowhen.service.impl;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import project.borrowhen.common.constant.CommonConstant;
import project.borrowhen.common.util.CipherUtil;
import project.borrowhen.common.util.DateFormatUtil;
import project.borrowhen.dao.BorrowRequestDao;
import project.borrowhen.dao.entity.BorrowRequestEntity;
import project.borrowhen.dao.entity.InventoryEntity;
import project.borrowhen.dao.entity.NotificationEntity;
import project.borrowhen.dao.entity.UserEntity;
import project.borrowhen.dto.BorrowRequestDto;
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
		request.setDateToBorrow(Date.valueOf(inDto.getDateToBorrow()));
		request.setDateToReturn(Date.valueOf(inDto.getDateToReturn()));
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

}
