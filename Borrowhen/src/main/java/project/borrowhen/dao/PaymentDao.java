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
import project.borrowhen.dao.entity.PaymentOverview;

public interface PaymentDao extends JpaRepository<PaymentEntity, Integer> {
	
	public static final String GET_PAYMENT_BY_BORROW_REQUEST_ID = "SELECT e "
			+ "FROM PaymentEntity e "
			+ "WHERE e.borrowRequestId = :borrowRequestId ";
	
	public PaymentEntity getPaymentByBorrowRequestId(@Param("borrowRequestId") int borrowRequestId);
	
	public static final String GET_PAYMENT_BY_ID = "SELECT e "
			+ "FROM PaymentEntity e "
			+ "WHERE e.id = :id ";
	
	public PaymentEntity getPaymentById(@Param("id") int id);
	
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
    	    "'' AS fullName, " +
    	    "'' AS userId, " +
    	    "p.emailAddress, " +
    	    "br.itemName, " +
    	    "br.price, " +
    	    "br.qty, " +
    	    "(br.price * br.qty), " +
    	    "br.updatedDate, " +
    	    "p.paymentMethod, " +
    	    "p.status, " +
    	    "br.id )" +
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
		    "u.fullName, " +
		    "u.userId, " +
		    "COALESCE(p.emailAddress, '-'), " +
		    "br.itemName, " +
		    "br.price, " +
		    "br.qty, " +
		    "(br.price * br.qty), " +
		    "br.updatedDate, " +
		    "COALESCE(p.paymentMethod, '-'), " +
		    "p.status, " +
		    "br.id ) " +
		    "FROM BorrowRequestEntity br " +
		    "INNER JOIN PaymentEntity p ON p.borrowRequestId = br.id " +
		    "INNER JOIN UserEntity u ON u.id = br.userId " +
		    "INNER JOIN InventoryEntity i ON i.id = br.inventoryId " +
		    "WHERE br.status IN ('PAYMENT PENDING', 'PAID') " +
		    "AND i.userId = :userId " +
		    "AND ( :search IS NULL OR :search = '' OR " +
		    "      LOWER(p.emailAddress) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
		    "      LOWER(br.itemName) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
		    "      LOWER(u.fullName) LIKE LOWER(CONCAT('%', :search, '%')) OR " +             
		    "      LOWER(u.fullName) LIKE LOWER(CONCAT('%', :search, '%')) OR " + 
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
	
	public static final String GET_LENDER_PAYMENT_OVERVIEW = """
		    SELECT 
		        COALESCE(CAST(SUM(CASE WHEN br.status = 'PAID' THEN 1 ELSE 0 END) AS INTEGER), 0) AS totalPaid,
		        COALESCE(CAST(SUM(CASE WHEN br.status = 'PAYMENT PENDING' THEN 1 ELSE 0 END) AS INTEGER), 0) AS totalPending,
		        COALESCE(CAST(SUM(CASE WHEN br.status = 'PAID' THEN (br.price * br.qty) ELSE 0 END) AS DOUBLE), 0) AS totalRevenue,
		        COALESCE(CAST(SUM(CASE WHEN br.status = 'PAYMENT PENDING' THEN (br.price * br.qty) ELSE 0 END) AS DOUBLE), 0) AS expectedRevenue
		    FROM BorrowRequestEntity br
		    INNER JOIN InventoryEntity i ON i.id = br.inventoryId AND i.userId = :userId
		    WHERE br.status IN ('PAID', 'PAYMENT PENDING')
		""";


	@Query(GET_LENDER_PAYMENT_OVERVIEW)
	public PaymentOverview getLenderPaymentOverview(@Param("userId") int userId) throws DataAccessException;
  
}
