package project.borrowhen.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import project.borrowhen.common.util.CipherUtil;
import project.borrowhen.common.util.TimeAgoUtil;
import project.borrowhen.dao.NotificationDao;
import project.borrowhen.dao.entity.NotificationEntity;
import project.borrowhen.dao.entity.UserEntity;
import project.borrowhen.dto.NotificationDto;
import project.borrowhen.object.FilterAndSearchObj;
import project.borrowhen.object.NotificationObj;
import project.borrowhen.object.PaginationObj;
import project.borrowhen.service.AdminSettingsService;
import project.borrowhen.service.NotificationService;
import project.borrowhen.service.UserService;

@Service
public class NotificationServiceImpl implements NotificationService{

	@Autowired
	private NotificationDao notificationDao;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private SimpMessagingTemplate messagingTemplate;
	
	@Autowired
	private CipherUtil cipherUtil;
	
	@Autowired
	private AdminSettingsService adminSettingsService;
    
    private int getMaxNotifcationsDisplay() {
        return adminSettingsService.getSettings().getNotificationPerPage();
    }
	
	@Override
	public void saveNotification(NotificationEntity notification) {
		
		notificationDao.save(notification);
		
	}
	
	

	@Override
	public NotificationDto getNotificationsByUser() throws Exception{
		
		NotificationDto outDto = new NotificationDto();
		
		UserEntity user = userService.getLoggedInUser();

		List<NotificationEntity> retrievedNotifications = notificationDao.getNotificationsByUser(user.getId());
		
		System.out.println(retrievedNotifications);
		
		List<NotificationObj> notifications = new ArrayList<>();
		
		for(NotificationEntity notification : retrievedNotifications) {
			
			NotificationObj obj = new NotificationObj();
			
			obj.setEncryptedId(cipherUtil.encrypt(String.valueOf(notification.getId())));
			obj.setMessage(notification.getMessage());		
			obj.setIsRead(notification.getIsRead());			
			obj.setType(notification.getType());
			obj.setDateAndTime(TimeAgoUtil.toTimeAgo(notification.getCreatedDate()));
			
			notifications.add(obj);
		}
		
		outDto.setNotifications(notifications);
		
		return outDto;
	}



	@Override
	public NotificationDto getNotificationsCountByUser() throws Exception {
		
		NotificationDto outDto = new NotificationDto();
		
		UserEntity user = userService.getLoggedInUser();
		
		int count = notificationDao.getNotificationsCountByUser(user.getId());
		
		outDto.setNotificationCount(count);
		
		return outDto;
	}



	@Async
	@Override
	public void sendToBorrowers(List<UserEntity> borrowers, String message) throws Exception {
		
		for (UserEntity borrower : borrowers) {
            messagingTemplate.convertAndSendToUser(
                borrower.getUserId(),
                "/queue/new-item/notifications",
                message
            );
        }
		
	}



	@Override
	public NotificationDto getAllNotificationsByUser(NotificationDto inDto) throws Exception {
		
		NotificationDto outDto = new NotificationDto();
		
		UserEntity user = userService.getLoggedInUser();

	    Pageable pageable = PageRequest.of(
	        inDto.getPagination().getPage(),
	        Integer.valueOf(getMaxNotifcationsDisplay())
	    );
	    
	    FilterAndSearchObj filter = inDto.getFilter();
	
		 // Call DAO
		 Page<NotificationEntity> allNotifications = notificationDao.getAllNotifications(
		         pageable,
		         user.getId(),
		         filter.getStartDate(),
		         filter.getEndDate(),
		         filter.getStatus()
		);
	    
	    List<NotificationObj> notifications = new ArrayList<>();
	    
	    for (NotificationEntity notification : allNotifications) {
	    	NotificationObj obj = new NotificationObj();
	        
			obj.setEncryptedId(cipherUtil.encrypt(String.valueOf(notification.getId())));
			obj.setMessage(notification.getMessage());		
			obj.setIsRead(notification.getIsRead());			
			obj.setType(notification.getType());
			obj.setDateAndTime(TimeAgoUtil.toTimeAgo(notification.getCreatedDate()));
	        
	    	notifications.add(obj);
	    }
	    
	    PaginationObj pagination = new PaginationObj();
		
		pagination.setPage(allNotifications.getNumber());
		pagination.setTotalPages(allNotifications.getTotalPages());
		pagination.setTotalElements(allNotifications.getTotalElements());
		pagination.setHasNext(allNotifications.hasNext());
		pagination.setHasPrevious(allNotifications.hasPrevious());
		pagination.setPageSize(getMaxNotifcationsDisplay());
		
		outDto.setNotifications(notifications);	
		outDto.setPagination(pagination);
		
	    return outDto;
	}

}
