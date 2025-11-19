package project.borrowhen.service;

import java.util.List;

import org.springframework.stereotype.Service;

import project.borrowhen.dao.entity.NotificationEntity;
import project.borrowhen.dao.entity.UserEntity;
import project.borrowhen.dto.NotificationDto;

@Service
public interface NotificationService {

	public void saveNotification(NotificationEntity notification);
	
	public void readNotification(String encryptedId)  throws Exception; 
	
	/*
	 * This is for MODAL Notification
	 */
	public NotificationDto getNotificationsByUser() throws Exception;
	
	public NotificationDto getNotificationsCountByUser() throws Exception;
	
	public void sendToBorrowers(List<UserEntity> borrowers, String message) throws Exception;
	
	public NotificationDto getAllNotificationsByUser(NotificationDto inDto) throws Exception;
}
