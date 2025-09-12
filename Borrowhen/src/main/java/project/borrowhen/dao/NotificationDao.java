package project.borrowhen.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import project.borrowhen.dao.entity.NotificationEntity;

public interface NotificationDao extends JpaRepository<NotificationEntity, Integer>{

	public final String GET_NOTIFICATIONS_COUNT = ""
		    + "SELECT COUNT(e) "
		    + "FROM NotificationEntity e "
		    + "INNER JOIN UserEntity u ON u.id = :userId "
		    + "WHERE e.isDeleted = false "
		    + "AND ( "
		    + "    (u.role = 'BORROWER' AND (e.userId = :userId OR e.targetRole = 'BORROWER' OR e.targetRole = 'ALL')) "
		    + " OR (u.role <> 'BORROWER' AND (e.userId = :userId OR e.targetRole = 'ALL')) "
		    + ") ";
	
	@Query(value=GET_NOTIFICATIONS_COUNT)
	public int getNotificationsCountByUser(int userId);
	
	public final String GET_NOTIFICATIONS_FOR_MODAL = ""
		    + "SELECT e "
		    + "FROM NotificationEntity e "
		    + "INNER JOIN UserEntity u ON u.id = :userId "
		    + "WHERE e.isDeleted = false "
		    + "AND ( "
		    + "    (u.role = 'BORROWER' AND (e.userId = :userId OR e.targetRole = 'BORROWER' OR e.targetRole = 'ALL')) "
		    + " OR (u.role <> 'BORROWER' AND (e.userId = :userId OR e.targetRole = 'ALL')) "
		    + ") "
		    + "ORDER BY e.createdDate DESC";

	
	@Query(value=GET_NOTIFICATIONS_FOR_MODAL)
	public List<NotificationEntity> getNotificationsByUser(int userId);
}
