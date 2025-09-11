package project.borrowhen.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import project.borrowhen.dao.NotificationDao;
import project.borrowhen.dao.entity.NotificationEntity;
import project.borrowhen.service.NotificationService;

@Service
public class NotificationServiceImpl implements NotificationService{

	@Autowired
	private NotificationDao notificationDao;
	
	@Override
	public void saveNotification(NotificationEntity notification) {
		
		notificationDao.save(notification);
		
	}

}
