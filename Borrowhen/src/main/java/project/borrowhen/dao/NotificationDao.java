package project.borrowhen.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import project.borrowhen.dao.entity.NotificationEntity;

public interface NotificationDao extends JpaRepository<NotificationEntity, Integer>{

	public final String GET_NOTIFICATIONS_COUNT = ""
			+ "SELECT COUNT(e) "
			+ "FROM NotificationEntity e "
			+ "WHERE e.userId = :userId "
			+ "";
	
	@Query(value=GET_NOTIFICATIONS_COUNT)
	public int getNotificationsCountByUser(int userId);
	
	public final String GET_NOTIFICATIONS_FOR_MODAL = ""
			+ "SELECT e "
			+ "FROM NotificationEntity e "
			+ "WHERE e.userId = :userId "
			+ "ORDER BY e.createdDate DESC "
			+ "LIMIT 10 "
			+ "";
	
	@Query(value=GET_NOTIFICATIONS_FOR_MODAL)
	public List<NotificationEntity> getNotificationsByUser(int userId);
}
