package project.borrowhen.dao;

import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import jakarta.transaction.Transactional;
import project.borrowhen.dao.entity.NotificationEntity;

public interface NotificationDao extends JpaRepository<NotificationEntity, Integer>{

	public final String GET_NOTIFICATIONS_COUNT = ""
		    + "SELECT COUNT(e) "
		    + "FROM NotificationEntity e "
		    + "INNER JOIN UserEntity u ON u.id = :userId "
		    + "WHERE e.isDeleted = false AND e.isRead = false "
		    + "AND ( "
		    + "    (u.role = 'BORROWER' AND (e.userId = :userId OR e.targetRole = 'BORROWER' OR e.targetRole = 'ALL')) "
		    + " OR (u.role <> 'BORROWER' AND (e.userId = :userId OR e.targetRole = 'ALL')) "
		    + ") ";
	
	@Query(value=GET_NOTIFICATIONS_COUNT)
	public int getNotificationsCountByUser(int userId) throws DataAccessException;
	
	public final String GET_NOTIFICATIONS_FOR_MODAL = ""
		    + "SELECT e "
		    + "FROM NotificationEntity e "
		    + "INNER JOIN UserEntity u ON u.id = :userId "
		    + "WHERE e.isDeleted = false "
		    + "AND ( "
		    + "    (u.role = 'BORROWER' AND (e.userId = :userId OR e.targetRole = 'BORROWER' OR e.targetRole = 'ALL')) "
		    + " OR (u.role <> 'BORROWER' AND (e.userId = :userId OR e.targetRole = 'ALL')) "
		    + ") "
		    + "ORDER BY e.createdDate DESC "
		    + "LIMIT 10 ";

	
	@Query(value=GET_NOTIFICATIONS_FOR_MODAL)
	public List<NotificationEntity> getNotificationsByUser(int userId) throws DataAccessException;
	
	public final String GET_NOTIFICATIONS_FOR_BORROWER = ""
		    + "SELECT e "
		    + "FROM NotificationEntity e "
		    + "INNER JOIN UserEntity u ON u.id = :userId "
		    + "WHERE e.isDeleted = false "
		    + "AND ( "
		    + "    (u.role = 'BORROWER' AND (e.userId = :userId OR e.targetRole = 'BORROWER' OR e.targetRole = 'ALL')) "
		    + " OR (u.role <> 'BORROWER' AND (e.userId = :userId OR e.targetRole = 'ALL')) "
		    + ") "
		    + "ORDER BY e.createdDate DESC "
		    + "LIMIT 5 ";
	
	@Query(value=GET_NOTIFICATIONS_FOR_BORROWER)
	public List<NotificationEntity> getNotificationsForBorrower(int userId) throws DataAccessException;
	
	public final String GET_ALL_NOTIFICATIONS = """
		    SELECT 
		        n.id,
		        n.created_date,
		        n.is_deleted,
		        n.is_read,
		        n.message,
		        n.target_role,
		        n.type,
		        n.updated_date,
		        n.user_id,
		        n.borrow_request_id
		    FROM notifications n
		    INNER JOIN users u ON u.id = :userId
		    WHERE n.is_deleted = false
		      AND (
		          (u.role = 'BORROWER' AND (n.user_id = :userId OR n.target_role='BORROWER' OR n.target_role='ALL'))
		       OR (u.role <> 'BORROWER' AND (n.user_id = :userId OR n.target_role='ALL'))
		      )
		      AND (:startDate IS NULL OR n.created_date >= CAST(:startDate AS DATE))
		      AND (:endDate IS NULL OR n.created_date < CAST(:endDate AS DATE) + INTERVAL '1 day')
		      AND (
		          :status = 'ALL' 
		          OR (:status = 'TRUE' AND n.is_read = true) 
		          OR (:status = 'FALSE' AND n.is_read = false)
		      )
		    ORDER BY n.created_date DESC
		""";

		@Query(value = GET_ALL_NOTIFICATIONS, nativeQuery=true)
		public Page<NotificationEntity> getAllNotifications(
		        Pageable pageable,
		        @Param("userId") int userId,
		        @Param("startDate") String startDate,
		        @Param("endDate") String endDate,
		        @Param("status") String status
		) throws DataAccessException;
		
	
	public final String READ_NOTIFICATION = """
				UPDATE notifications
				SET is_read = true
				WHERE id = :notificationId
			""";
	
    @Modifying
    @Transactional
    @Query(value = READ_NOTIFICATION, nativeQuery = true)
	public void readNotification(@Param("notificationId") int notificationId) throws DataAccessException;

}
