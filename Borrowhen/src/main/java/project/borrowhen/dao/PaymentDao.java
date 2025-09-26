package project.borrowhen.dao;

import org.springframework.dao.DataAccessException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import jakarta.transaction.Transactional;
import project.borrowhen.dao.entity.PaymentEntity;

public interface PaymentDao extends JpaRepository<PaymentEntity, Integer> {
	
	public static final String GET_PAYMENT_BY_BORROW_REQUEST_ID = "SELECT e "
			+ "FROM PaymentEntity e "
			+ "WHERE e.borrowRequestId = :borrowRequestId ";
	
	public PaymentEntity getPaymentByBorrowRequestId(@Param("borrowRequestId") int borrowRequestId);
	
    public final String UPDATE_PAYMENT_STATUS =
        "UPDATE PaymentEntity p " +
        "SET p.status = :status, p.updatedDate = CURRENT_TIMESTAMP " +
        "WHERE p.borrowRequestId = :borrowRequestId";

    @Transactional
    @Modifying
    @Query(UPDATE_PAYMENT_STATUS)
    public int updatePaymentStatusByBorrowRequestId(@Param("id") int id, @Param("status") String status) throws DataAccessException;
}
