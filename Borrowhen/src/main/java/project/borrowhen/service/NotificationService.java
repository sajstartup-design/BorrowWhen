package project.borrowhen.service;

import org.springframework.stereotype.Service;

import project.borrowhen.dao.entity.NotificationEntity;

@Service
public interface NotificationService {

	public void saveNotification(NotificationEntity notification);
}
