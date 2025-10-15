package project.borrowhen.dao;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import jakarta.transaction.Transactional;
import project.borrowhen.dao.entity.BorrowRequestData;
import project.borrowhen.dao.entity.BorrowRequestEntity;
import project.borrowhen.dao.entity.BorrowRequestOverview;
import project.borrowhen.dao.entity.LenderDashboardOverview;

public interface BorrowRequestDao extends JpaRepository<BorrowRequestEntity, Integer> {
	
	public final String GET_ALL_BORROW_REQUESTS =
		    "SELECT new project.borrowhen.dao.entity.BorrowRequestData(" +
		    " br.id, " +                         
		    " borrower.fullName, " +  
		    " borrower.userId, " + 
		    " lender.fullName, " +       
		    " lender.userId, " +     
		    " br.itemName, " +                    
		    " br.price, " +                     
		    " br.qty, " +                        
		    " br.dateToBorrow, " +                
		    " br.dateToReturn, " +                
		    " br.status, " +                    
		    " br.createdDate, " +                 
		    " br.updatedDate) " +              
		    "FROM BorrowRequestEntity br " +     
		    "LEFT JOIN InventoryEntity i ON i.id = br.inventoryId " +     
		    "LEFT JOIN UserEntity borrower ON borrower.id = br.userId " + 
		    "LEFT JOIN UserEntity lender ON lender.id = i.userId " +
		    "WHERE br.isDeleted = false " + 
		    "AND ( :search IS NULL OR :search = '' OR " +
		    "      LOWER(br.itemName) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
		    "      LOWER(borrower.fullName) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
		    "      LOWER(borrower.userId) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
		    "      LOWER(lender.fullName) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
		    "      LOWER(lender.userId) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
		    "      CAST(br.price AS string) LIKE CONCAT('%', :search, '%') OR " +
		    "      CAST(br.qty AS string) LIKE CONCAT('%', :search, '%') OR " +
		    "      CAST(br.dateToBorrow AS string) LIKE CONCAT('%', :search, '%') OR " +
		    "      CAST(br.dateToReturn AS string) LIKE CONCAT('%', :search, '%') OR " +
		    "      LOWER(br.status) LIKE LOWER(CONCAT('%', :search, '%')) " +
		    "    )";
	
	@Query(value=GET_ALL_BORROW_REQUESTS)
	public Page<BorrowRequestData> getAllBorrowRequests(Pageable pageable,
			@Param("search") String search) throws DataAccessException; 
	
	public final String GET_ALL_OWNED_BORROW_REQUESTS_FOR_LENDER =
		    "SELECT new project.borrowhen.dao.entity.BorrowRequestData(" +
		    " br.id, " +                         
		    " borrower.fullName, " +  
		    " borrower.userId, " +     
		    " lender.fullName, " +    
		    " lender.userId, " +  
		    " br.itemName, " +                    
		    " br.price, " +                      
		    " br.qty, " +                        
		    " br.dateToBorrow, " +                
		    " br.dateToReturn, " +                
		    " br.status, " +                    
		    " br.createdDate, " +                 
		    " br.updatedDate) " +              
		    "FROM BorrowRequestEntity br " +     
		    "INNER JOIN InventoryEntity i ON i.id = br.inventoryId " +     
		    "LEFT JOIN UserEntity borrower ON borrower.id = br.userId " + 
		    "LEFT JOIN UserEntity lender ON lender.id = i.userId " +
		    "WHERE br.isDeleted = false " +
		    "AND i.userId = :userId " +
		    "AND ( :search IS NULL OR :search = '' OR " +
		    "      LOWER(br.itemName) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
		    "      LOWER(borrower.fullName) LIKE LOWER(CONCAT('%', :search, '%')) OR " + // ✅ search borrower full name
		    "      CAST(br.price AS string) LIKE CONCAT('%', :search, '%') OR " +
		    "      CAST(br.qty AS string) LIKE CONCAT('%', :search, '%') OR " +
		    "      CAST(br.dateToBorrow AS string) LIKE CONCAT('%', :search, '%') OR " +
		    "      CAST(br.dateToReturn AS string) LIKE CONCAT('%', :search, '%') OR " +
		    "      LOWER(br.status) LIKE LOWER(CONCAT('%', :search, '%')) " +
		    "    )";
	
	@Query(value=GET_ALL_OWNED_BORROW_REQUESTS_FOR_LENDER)
	public Page<BorrowRequestData> getAllOwnedBorrowRequestsForLender(Pageable pageable, 
			@Param("userId") int userId,
			@Param("search") String search) throws DataAccessException; 
	
	public final String GET_ALL_OWNED_BORROW_REQUESTS_FOR_BORROWER =
		    "SELECT new project.borrowhen.dao.entity.BorrowRequestData(" +
		    " br.id, " +                         
		    " borrower.fullName, " +  
		    " borrower.userId, " +     
		    " lender.fullName, " +    
		    " lender.userId, " +                      
		    " br.itemName, " +                    
		    " br.price, " +                     
		    " br.qty, " +                        
		    " br.dateToBorrow, " +                
		    " br.dateToReturn, " +                
		    " br.status, " +                    
		    " br.createdDate, " +                 
		    " br.updatedDate) " +              
		    "FROM BorrowRequestEntity br " +     
		    "INNER JOIN InventoryEntity i ON i.id = br.inventoryId " +     
		    "LEFT JOIN UserEntity borrower ON borrower.id = br.userId " + 
		    "LEFT JOIN UserEntity lender ON lender.id = i.userId " +
		    "WHERE br.isDeleted = false " +
		    "AND br.userId = :userId " +
		    "AND ( :search IS NULL OR :search = '' OR " +
		    "      LOWER(br.itemName) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
		    "      CAST(br.price AS string) LIKE CONCAT('%', :search, '%') OR " +
		    "      CAST(br.qty AS string) LIKE CONCAT('%', :search, '%') OR " +
		    "      CAST(br.dateToBorrow AS string) LIKE CONCAT('%', :search, '%') OR " +
		    "      CAST(br.dateToReturn AS string) LIKE CONCAT('%', :search, '%') OR " +
		    "      LOWER(br.status) LIKE LOWER(CONCAT('%', :search, '%')) " +
		    "    )";

	
	@Query(value=GET_ALL_OWNED_BORROW_REQUESTS_FOR_BORROWER)
	public Page<BorrowRequestData> getAllOwnedBorrowRequestsForBorrower(Pageable pageable, 
			@Param("userId") int userId, 
			@Param("search") String search) throws DataAccessException; 
	
    public final String UPDATE_BORROW_REQUEST_STATUS =
        "UPDATE BorrowRequestEntity br " +
        "SET br.status = :status, br.updatedDate = CURRENT_TIMESTAMP " +
        "WHERE br.id = :id";

    @Transactional
    @Modifying
    @Query(UPDATE_BORROW_REQUEST_STATUS)
    public int updateBorrowRequestStatusById(@Param("id") int id, @Param("status") String status) throws DataAccessException; 
    
    public final String GET_BORROW_REQUEST = "SELECT e "
    		+ "FROM BorrowRequestEntity e "
    		+ "WHERE e.id = :id ";
    
    @Query(value=GET_BORROW_REQUEST)
    public BorrowRequestEntity getBorrowRequest(@Param("id") int id);
    
    public final String GET_BORROW_REQUEST_DETAILS_FOR_LENDER =
		    "SELECT new project.borrowhen.dao.entity.BorrowRequestData(" +
		    " br.id, " +                         
		    " borrower.fullName, " +  
		    " borrower.userId, " +     
		    " lender.fullName, " +    
		    " lender.userId, " +                       
		    " br.itemName, " +                    
		    " br.price, " +                     
		    " br.qty, " +                        
		    " br.dateToBorrow, " +                
		    " br.dateToReturn, " +                
		    " br.status, " +                    
		    " br.createdDate, " +                 
		    " br.updatedDate) " +              
		    "FROM BorrowRequestEntity br " +     
		    "INNER JOIN InventoryEntity i ON i.id = br.inventoryId " +     
		    "LEFT JOIN UserEntity borrower ON borrower.id = br.userId " + 
		    "LEFT JOIN UserEntity lender ON lender.id = i.userId " +
		    "WHERE br.isDeleted = false " +
			"AND br.id = :borrowRequestId ";
	
	@Query(value=GET_BORROW_REQUEST_DETAILS_FOR_LENDER)
	public BorrowRequestData getBorrowRequestDetailsForLender(@Param("borrowRequestId") int borrowRequestId) throws DataAccessException; 
    
	public final String FIND_ONGOING_AND_OVERDUE_REQUESTS = """
	    SELECT b 
	    FROM BorrowRequestEntity b
	    WHERE b.status IN ('ON GOING', 'OVERDUE')
	    AND b.dateToReturn <= :today
	""";

	@Query(value = FIND_ONGOING_AND_OVERDUE_REQUESTS)
	public List<BorrowRequestEntity> findOngoingAndOverdue(@Param("today") Date today) throws DataAccessException;
	
	public final String FIND_VOIDABLE_REQUESTS = """
	    SELECT b 
	    FROM BorrowRequestEntity b
	    WHERE b.status IN ('PENDING', 'APPROVED', 'PICK-UP READY')
	    AND b.dateToBorrow <= :today
	""";

	@Query(value = FIND_VOIDABLE_REQUESTS)
	public List<BorrowRequestEntity> findVoidableRequests(@Param("today") Date today) throws DataAccessException;
	
	public final String GET_LENDER_BORROWER_REQUEST_OVERVIEW = """
		    SELECT 
		        COALESCE(CAST(SUM(CASE WHEN br.status = 'PENDING' THEN 1 ELSE 0 END) AS integer), 0) AS totalPending,
		        COALESCE(CAST(SUM(CASE WHEN br.status = 'APPROVED' THEN 1 ELSE 0 END) AS integer), 0) AS totalApproved,
		        COALESCE(CAST(SUM(CASE WHEN br.status = 'PICK-UP READY' THEN 1 ELSE 0 END) AS integer), 0) AS totalPickupReady,
		        COALESCE(CAST(SUM(CASE WHEN br.status = 'ON GOING' THEN 1 ELSE 0 END) AS integer), 0) AS totalOngoing,
		        COALESCE(CAST(SUM(CASE WHEN br.status = 'COMPLETED' THEN 1 ELSE 0 END) AS integer), 0) AS totalComplete,
		        COALESCE(CAST(SUM(CASE WHEN br.status = 'PAYMENT PENDING' THEN 1 ELSE 0 END) AS integer), 0) AS totalPaymentPending,
		        COALESCE(CAST(SUM(CASE WHEN br.status = 'PAID' THEN 1 ELSE 0 END) AS integer), 0) AS totalPaid,
		        COALESCE(CAST(SUM(CASE WHEN br.status = 'OVERDUE' THEN 1 ELSE 0 END) AS integer), 0) AS totalOverdue,
		        COALESCE(CAST(SUM(CASE WHEN br.status = 'REJECTED' THEN 1 ELSE 0 END) AS integer), 0) AS totalRejected
		    FROM InventoryEntity i
		    LEFT JOIN BorrowRequestEntity br ON br.inventoryId = i.id
		    WHERE i.userId = :userId
		""";

	@Query(value = GET_LENDER_BORROWER_REQUEST_OVERVIEW)
	public BorrowRequestOverview getLenderBorrowerRequestOverview(@Param("userId") int userId) throws DataAccessException;
	
	public final String UPDATE_FEEDBACK_BORROW_REQUEST = 
			"""
				UPDATE borrow_request
				SET rating = :rating,
				feedback = :feedback,
				updated_date = :updatedDate
				WHERE id = :id
				
			""";

    @Modifying
    @Transactional
    @Query(value = UPDATE_FEEDBACK_BORROW_REQUEST, nativeQuery = true)
    public void updateFeedbackBorrowRequest(
            @Param("id") int id,
            @Param("rating") double rating,
            @Param("feedback") String feedback,
            @Param("updatedDate") Timestamp updatedDate
    )  throws DataAccessException;
    
    
    public final String GET_OVERDUE_REQUEST_FOR_BORROWER = """
    			SELECT br.*
    			FROM borrow_request br
    			WHERE br.status = 'OVERDUE'
    			AND br.user_id = :userId
    			LIMIT 3
    		""";
    
    @Query(value=GET_OVERDUE_REQUEST_FOR_BORROWER, nativeQuery=true)
    public List<BorrowRequestEntity> getOverdueRequestForBorrower(@Param("userId") int userId) throws DataAccessException;
    
    public final String GET_PAYMENT_PENDING_REQUEST_FOR_BORROWER = """
			SELECT br.*
			FROM borrow_request br 
			WHERE br.status = 'PAYMENT PENDING' 
			AND br.user_id = :userId 
			LIMIT 3
		""";

	@Query(value=GET_PAYMENT_PENDING_REQUEST_FOR_BORROWER, nativeQuery=true)
	public List<BorrowRequestEntity> getPaymentPendingRequestForBorrower(@Param("userId") int userId) throws DataAccessException;
	
	public final String GET_BORROW_REQUEST_OVERVIEW_FOR_BORROWER = """
		    SELECT 
		        COALESCE(CAST(SUM(CASE WHEN br.status = 'PENDING' THEN 1 ELSE 0 END) AS integer), 0) AS totalPending,
		        COALESCE(CAST(SUM(CASE WHEN br.status = 'APPROVED' THEN 1 ELSE 0 END) AS integer), 0) AS totalApproved,
		        COALESCE(CAST(SUM(CASE WHEN br.status = 'PICK-UP READY' THEN 1 ELSE 0 END) AS integer), 0) AS totalPickupReady,
		        COALESCE(CAST(SUM(CASE WHEN br.status = 'ON GOING' THEN 1 ELSE 0 END) AS integer), 0) AS totalOngoing,
		        COALESCE(CAST(SUM(CASE WHEN br.status = 'COMPLETED' THEN 1 ELSE 0 END) AS integer), 0) AS totalComplete,
		        COALESCE(CAST(SUM(CASE WHEN br.status = 'PAYMENT PENDING' THEN 1 ELSE 0 END) AS integer), 0) AS totalPaymentPending,
		        COALESCE(CAST(SUM(CASE WHEN br.status = 'PAID' THEN 1 ELSE 0 END) AS integer), 0) AS totalPaid,
		        COALESCE(CAST(SUM(CASE WHEN br.status = 'OVERDUE' THEN 1 ELSE 0 END) AS integer), 0) AS totalOverdue,
		        COALESCE(CAST(SUM(CASE WHEN br.status = 'REJECTED' THEN 1 ELSE 0 END) AS integer), 0) AS totalRejected
		    FROM BorrowRequestEntity br
		    WHERE br.userId = :userId
		""";

	@Query(value = GET_BORROW_REQUEST_OVERVIEW_FOR_BORROWER)
	public BorrowRequestOverview getBorrowRequestOverviewForBorrower(@Param("userId") int userId) throws DataAccessException;
	
	public final String GET_LENDER_DASHBOARD_OVERVIEW = """
		    SELECT 
		        COALESCE(CAST(COUNT(DISTINCT i.id) AS integer), 0) AS totalItem,
		        COALESCE(CAST(SUM(i.totalQty) AS integer), 0) AS totalQty,
		        COALESCE(CAST(SUM(CASE WHEN br.status NOT IN ('COMPLETE', 'CANCELLED', 'PAYMENT PENDING', 'PAID') THEN br.qty ELSE 0 END) AS integer), 0) AS totalOngoingQty,
		        COALESCE(CAST(SUM(CASE WHEN br.status IN ('PENDING', 'APPROVED', 'PICK-UP READY', 'ON GOING', 'COMPLETE') THEN 1 ELSE 0 END) AS integer), 0) AS totalOngoing,
		        COALESCE(SUM(CASE WHEN br.status = 'PAID' THEN (br.price * br.qty) ELSE 0 END), 0) AS totalRevenue,
		        COALESCE(CAST(SUM(i.availableQty) AS integer), 0) AS totalItemsAvailableQty
		    FROM InventoryEntity i
		    LEFT JOIN BorrowRequestEntity br ON br.inventoryId = i.id
		    WHERE i.userId = :userId
		""";

	
	@Query(GET_LENDER_DASHBOARD_OVERVIEW)
	public LenderDashboardOverview getLenderDashboardOverview(@Param("userId") int userId) throws DataAccessException;

}
