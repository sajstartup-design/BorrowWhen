package project.borrowhen.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import project.borrowhen.common.util.CipherUtil;
import project.borrowhen.common.util.TimeAgoUtil;
import project.borrowhen.dao.NotificationDao;
import project.borrowhen.dao.entity.NotificationEntity;
import project.borrowhen.dao.entity.UserEntity;
import project.borrowhen.dto.NotificationDto;
import project.borrowhen.object.NotificationObj;
import project.borrowhen.service.NotificationService;
import project.borrowhen.service.UserService;

@Service
public class NotificationServiceImpl implements NotificationService{

	@Autowired
	private NotificationDao notificationDao;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private CipherUtil cipherUtil;
	
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
			obj.setDateAndTime(TimeAgoUtil.toTimeAgo(notification.getCreatedDate()));;
			
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

}
