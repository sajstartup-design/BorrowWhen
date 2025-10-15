package project.borrowhen.service.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import project.borrowhen.common.util.TimeAgoUtil;
import project.borrowhen.dao.BorrowRequestDao;
import project.borrowhen.dao.NotificationDao;
import project.borrowhen.dao.entity.BorrowRequestEntity;
import project.borrowhen.dao.entity.BorrowRequestOverview;
import project.borrowhen.dao.entity.NotificationEntity;
import project.borrowhen.dao.entity.UserEntity;
import project.borrowhen.dto.DashboardDto;
import project.borrowhen.object.BorrowRequestObj;
import project.borrowhen.object.NotificationObj;
import project.borrowhen.object.OverdueBorrowRequestObj;
import project.borrowhen.service.DashboardService;
import project.borrowhen.service.UserService;

@Service
public class DashboardServiceImpl implements DashboardService{
	
	@Autowired
	private BorrowRequestDao borrowRequestDao;
	
	@Autowired
	private NotificationDao notificationDao;
	
	@Autowired
	private UserService userService;

	@Override
	public DashboardDto getBorrowerDashboardDetails() {
		
		DashboardDto outDto = new DashboardDto();
		
		UserEntity user = userService.getLoggedInUser();
		
		List<BorrowRequestEntity> allOverdue = borrowRequestDao.getOverdueRequestForBorrower(user.getId());
		
		List<BorrowRequestObj> overdues = new ArrayList<>();
		
		for(BorrowRequestEntity overdue : allOverdue) {
			
			BorrowRequestObj obj = new BorrowRequestObj();
			
			obj.setItemName(overdue.getItemName());
			Timestamp ts = overdue.getDateToReturn() != null 
				    ? new Timestamp(overdue.getDateToReturn().getTime()) 
				    : null;

			obj.setTimeAgo(TimeAgoUtil.toTimeAgo(ts));
				
			overdues.add(obj);
		}
		
		List<BorrowRequestEntity> allPaymentPending = borrowRequestDao.getPaymentPendingRequestForBorrower(user.getId());
		
		List<BorrowRequestObj> paymentPendings = new ArrayList<>();
		
		for(BorrowRequestEntity paymentPending : allPaymentPending) {
			
			BorrowRequestObj obj = new BorrowRequestObj(); 
			
			obj.setItemName(paymentPending.getItemName());
			obj.setQty(paymentPending.getQty());
			obj.setAmount(paymentPending.getPrice() * paymentPending.getQty());
			
			paymentPendings.add(obj);		
		}
		
		List<NotificationEntity> allNotifications = notificationDao.getNotificationsForBorrower(user.getId());
		
		List<NotificationObj> notifications = new ArrayList<>();
		
		for(NotificationEntity notification : allNotifications) {
			
			NotificationObj obj = new NotificationObj(); 
			
//			obj.setEncryptedId(cipherUtil.encrypt(String.valueOf(notification.getId())));
			obj.setMessage(notification.getMessage());		
			obj.setIsRead(notification.getIsRead());			
			obj.setType(notification.getType());
			obj.setDateAndTime(TimeAgoUtil.toTimeAgo(notification.getCreatedDate()));;
			
			notifications.add(obj);
			
		}
		
		BorrowRequestOverview overview = borrowRequestDao.getBorrowRequestOverviewForBorrower(user.getId());
		
		outDto.setNotifications(notifications);
		outDto.setPaymentPendings(paymentPendings);	
		outDto.setOverdues(overdues);
		outDto.setOverview(overview);
		;
		return outDto;
	}

	@Override
	public DashboardDto getLenderDashboardDetails() {
		
		DashboardDto outDto = new DashboardDto();
		
		UserEntity user = userService.getLoggedInUser();
		
		List<NotificationEntity> allNotifications = notificationDao.getNotificationsForBorrower(user.getId());
		
		List<NotificationObj> notifications = new ArrayList<>();
		
		for(NotificationEntity notification : allNotifications) {
			
			NotificationObj obj = new NotificationObj(); 
			
			obj.setMessage(notification.getMessage());		
			obj.setIsRead(notification.getIsRead());			
			obj.setType(notification.getType());
			obj.setDateAndTime(TimeAgoUtil.toTimeAgo(notification.getCreatedDate()));;
			
			notifications.add(obj);
			
		}
		
		BorrowRequestOverview overview = borrowRequestDao.getBorrowRequestOverviewForBorrower(user.getId());
		
		outDto.setOverview(overview);
		
		outDto.setNotifications(notifications);
		
		return outDto;
	}

}
