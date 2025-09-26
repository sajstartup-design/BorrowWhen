package project.borrowhen.dao;

import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import jakarta.transaction.Transactional;
import project.borrowhen.dao.entity.PaymentData;
import project.borrowhen.dao.entity.PaymentEntity;

public interface PaymentDao extends JpaRepository<PaymentEntity, Integer> {
	
	public static final String GET_PAYMENT_BY_BORROW_REQUEST_ID = "SELECT e "
			+ "FROM PaymentEntity e "
			+ "WHERE e.borrowRequestId = :borrowRequestId ";
	
	public PaymentEntity getPaymentByBorrowRequestId(@Param("borrowRequestId") int borrowRequestId);
	
    public final String UPDATE_PAYMENT_STATUS =
        "UPDATE PaymentEntity p " +
        "SET p.status = :status, p.paymentMethod = :paymentMethod , p.updatedDate = CURRENT_TIMESTAMP, p.emailAddress = :emailAddress  " +
        "WHERE p.borrowRequestId = :borrowRequestId";

    @Transactional
    @Modifying
    @Query(UPDATE_PAYMENT_STATUS)
    public int updatePaymentStatusByBorrowRequestId(@Param("borrowRequestId") int borrowRequestId, 
    		@Param("status") String status, 
    		@Param("paymentMethod") String paymentMethod,
    		@Param("emailAddress") String emailAddress) throws DataAccessException;
    
    public static final String GET_ALL_PAYMENT_FOR_BORROWER =
    	    "SELECT new project.borrowhen.dao.entity.PaymentData(" +
    	    "p.id, " +
    	    "'' AS firstName, " +
    	    "'' AS familyName, " +
    	    "p.emailAddress, " +
    	    "br.itemName, " +
    	    "br.price, " +
    	    "br.qty, " +
    	    "(br.price * br.qty), " +
    	    "br.updatedDate, " +
    	    "p.paymentMethod, " +
    	    "p.status ) " +
    	    "FROM BorrowRequestEntity br " +
    	    "INNER JOIN PaymentEntity p ON p.borrowRequestId = br.id " +
    	    "WHERE br.status IN ('PAYMENT PENDING', 'PAID') " +
    	    "AND br.userId = :userId " +
    	    "AND ( :search IS NULL OR :search = '' OR " +
    	    "      LOWER(p.emailAddress) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
    	    "      LOWER(br.itemName) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
    	    "      CAST(br.price AS string) LIKE CONCAT('%', :search, '%') OR " +
    	    "      CAST(br.qty AS string) LIKE CONCAT('%', :search, '%') OR " +
    	    "      CAST((br.price * br.qty) AS string) LIKE CONCAT('%', :search, '%') OR " +
    	    "      CAST(br.updatedDate AS string) LIKE CONCAT('%', :search, '%') OR " +
    	    "      LOWER(p.paymentMethod) LIKE LOWER(CONCAT('%', :search, '%')) " +
    	    ")";


	@Query(value = GET_ALL_PAYMENT_FOR_BORROWER)
	Page<PaymentData> getAllPaymentForBorrower(Pageable pageable,
	                                         @Param("userId") int userId,
	                                         @Param("search") String search) throws DataAccessException;

	public static final String GET_ALL_PAYMENT_FOR_LENDER =
		    "SELECT new project.borrowhen.dao.entity.PaymentData(" +
		    "p.id, " +
		    "u.firstName, " +
		    "u.familyName, " +
		    "COALESCE(p.emailAddress, '-'), " +
		    "br.itemName, " +
		    "br.price, " +
		    "br.qty, " +
		    "(br.price * br.qty), " +
		    "br.updatedDate, " +
		    "COALESCE(p.paymentMethod, '-'), " +
		    "p.status ) " +
		    "FROM BorrowRequestEntity br " +
		    "INNER JOIN PaymentEntity p ON p.borrowRequestId = br.id " +
		    "LEFT JOIN UserEntity u ON u.id = br.userId " +
		    "LEFT JOIN InventoryEntity i ON i.id = br.inventoryId " +
		    "WHERE br.status IN ('PAYMENT PENDING', 'PAID') " +
		    "AND i.userId = :userId " +
		    "AND ( :search IS NULL OR :search = '' OR " +
		    "      LOWER(p.emailAddress) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
		    "      LOWER(br.itemName) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
		    "      LOWER(u.firstName) LIKE LOWER(CONCAT('%', :search, '%')) OR " +              // ✅ first name
		    "      LOWER(u.familyName) LIKE LOWER(CONCAT('%', :search, '%')) OR " +             // ✅ last name
		    "      LOWER(CONCAT(u.firstName, ' ', u.familyName)) LIKE LOWER(CONCAT('%', :search, '%')) OR " + // ✅ full name
		    "      CAST(br.price AS string) LIKE CONCAT('%', :search, '%') OR " +
		    "      CAST(br.qty AS string) LIKE CONCAT('%', :search, '%') OR " +
		    "      CAST((br.price * br.qty) AS string) LIKE CONCAT('%', :search, '%') OR " +
		    "      CAST(br.updatedDate AS string) LIKE CONCAT('%', :search, '%') OR " +
		    "      LOWER(p.paymentMethod) LIKE LOWER(CONCAT('%', :search, '%')) " +
		    ")";




	@Query(value = GET_ALL_PAYMENT_FOR_LENDER)
	Page<PaymentData> getAllPaymentForLender(Pageable pageable,
	                                         @Param("userId") int userId,
	                                         @Param("search") String search) throws DataAccessException;
  
}
