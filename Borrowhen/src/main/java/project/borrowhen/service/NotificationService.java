package project.borrowhen.service;

import org.springframework.stereotype.Service;

import project.borrowhen.dao.entity.NotificationEntity;
import project.borrowhen.dto.NotificationDto;

@Service
public interface NotificationService {

	public void saveNotification(NotificationEntity notification);
	
	/*
	 * This is for MODAL Notification
	 */
	public NotificationDto getNotificationsByUser() throws Exception;
	
	public NotificationDto getNotificationsCountByUser() throws Exception;
}
