package project.borrowhen.dao;

import java.sql.Date;
import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import jakarta.transaction.Transactional;
import project.borrowhen.dao.entity.AdminDashboardOverview;
import project.borrowhen.dao.entity.UserData;
import project.borrowhen.dao.entity.UserDetailsData;
import project.borrowhen.dao.entity.UserEntity;

public interface UserDao extends JpaRepository<UserEntity, Integer> {

	public final String GET_ALL_BORROWERS =
		    "SELECT new project.borrowhen.dao.entity.UserData(" +
		    "   u.id, u.fullName, u.gender, u.birthDate, u.phoneNumber, u.emailAddress, " +
		    "   u.barangay, u.street, u.city, u.province, u.postalCode, u.about, u.userId, u.password, u.role, " +
		    "   u.createdDate, u.updatedDate, " +
		    "   CASE WHEN (" +
		    "       SELECT COUNT(br) " +
		    "       FROM BorrowRequestEntity br " +
		    "       WHERE br.userId = u.id " +
		    "       AND br.status IN ('PENDING', 'APPROVED', 'PICK-UP READY', 'ON GOING', 'PAYMENT PENDING', 'OVERDUE')" +
		    "   ) > 0 THEN false ELSE true END AS isDeletable " +
		    ") " +
		    "FROM UserEntity u " +
		    "WHERE u.isDeleted = false " +
		    "AND u.role = 'BORROWER' " +
		    "AND ( " +
		    "   (:search IS NOT NULL AND :search <> '' AND (" +
		    "       LOWER(u.fullName) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
		    "       LOWER(u.emailAddress) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
		    "       LOWER(u.phoneNumber) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
		    "       LOWER(u.userId) LIKE LOWER(CONCAT('%', :search, '%'))" +
		    "   )) " +
		    "   OR (:search IS NULL OR :search = '')" +
		    ")";


	@Query(GET_ALL_BORROWERS)
	Page<UserData> getAllBorrowers(Pageable pageable, 
			@Param("search") String search) throws DataAccessException;
	
	public final String GET_ALL_LENDERS =
		    "SELECT new project.borrowhen.dao.entity.UserData(" +
		    "   u.id, u.fullName, u.gender, u.birthDate, u.phoneNumber, u.emailAddress, " +
		    "   u.barangay, u.street, u.city, u.province, u.postalCode, u.about, u.userId, u.password, u.role, " +
		    "   u.createdDate, u.updatedDate, " +
		    "   CASE WHEN (" +
		    "       SELECT COUNT(br2) " +
		    "       FROM BorrowRequestEntity br2 " +
		    "       JOIN InventoryEntity ii ON ii.id = br2.inventoryId " +
		    "       WHERE ii.isDeleted = false AND ii.userId = u.id " +
		    "       AND br2.status NOT IN ('PAID', 'CANCELLED', 'REJECTED', 'VOID')" +
		    "   ) > 0 THEN false ELSE true END AS isDeletable " +
		    ") " +
		    "FROM UserEntity u " +
		    "WHERE u.isDeleted = false " +
		    "AND u.role = 'LENDER' " +
		    "AND ( " +
		    "   (:search IS NOT NULL AND :search <> '' AND (" +
		    "       LOWER(u.fullName) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
		    "       LOWER(u.emailAddress) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
		    "       LOWER(u.phoneNumber) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
		    "       LOWER(u.userId) LIKE LOWER(CONCAT('%', :search, '%'))" +
		    "   )) " +
		    "   OR (:search IS NULL OR :search = '')" +
		    ")";


	@Query(GET_ALL_LENDERS)
	Page<UserData> getAllLenders(Pageable pageable, 
			@Param("search") String search) throws DataAccessException;

	
	public final String GET_USER_BY_ID = "SELECT e "
			+ "FROM UserEntity e "
			+ "WHERE e.id = :id "
			+ "AND e.isDeleted = false ";
	
	@Query(value=GET_USER_BY_ID)
	public UserEntity getUser(@Param("id") int id)  throws DataAccessException;
	
	public final String GET_USER_BY_USER_ID = "SELECT e "
			+ "FROM UserEntity e "
			+ "WHERE e.userId = :userId "
			+ "AND e.isDeleted = false ";
	
	@Query(value=GET_USER_BY_USER_ID)
	public UserEntity getUserByUserId(@Param("userId") String userId) throws DataAccessException;
	
	public final String UPDATE_USER = 
		    "UPDATE users " +
		    "SET full_name = :fullName, " +
		    "gender = :gender, " +
		    "birth_date = :birthDate, " +
		    "phone_number = :phoneNumber, " +
		    "email_address = :emailAddress, " +
		    "barangay = :barangay, " +
		    "street = :street, " +
		    "city = :city, " +
		    "province = :province, " +
		    "postal_code = :postalCode, " +
		    "about = :about, " +
		    "user_id = :userId, " +
		    "password = CASE WHEN :hasChanged = true THEN :password ELSE password END, " +
		    "updated_date = :updatedDate " +
		    "WHERE id = :id";

    @Modifying
    @Transactional
    @Query(value = UPDATE_USER, nativeQuery = true)
    public void updateUser(
            @Param("id") int id,
            @Param("fullName") String fullName,
            @Param("gender") String gender,
            @Param("birthDate") Date birthDate,
            @Param("phoneNumber") String phoneNumber,
            @Param("emailAddress") String emailAddress,
            @Param("barangay") String barangay,
            @Param("street") String street,
            @Param("city") String city,
            @Param("province") String province,
            @Param("postalCode") String postalCode,
            @Param("about") String about,
            @Param("userId") String userId,
            @Param("password") String password,
            @Param("hasChanged") boolean hasChanged,
            @Param("updatedDate") Date updatedDate
    )  throws DataAccessException;
    
    public final String GET_ALL_USER_ID = "SELECT e.userId "
			+ "FROM UserEntity e "
			+ "WHERE e.isDeleted = false "
			+ "AND e.role NOT IN ('ADMIN', 'BORROWER') ";
    
    @Query(value=GET_ALL_USER_ID)
    public List<String> getAllUserId() throws DataAccessException;
    
    public final String GET_ALL_USERS_BY_ROLE =
    	    "SELECT u " +
    	    "FROM UserEntity u " +
    	    "WHERE u.role = :role " +
    	    "AND u.isDeleted = false " +
    	    "AND ( " +
    	    "   (:search IS NULL OR :search = '') " +
    	    "   OR LOWER(u.fullName) LIKE LOWER(CONCAT('%', :search, '%')) " +
    	    ")";



	@Query(value = GET_ALL_USERS_BY_ROLE)
	public List<UserEntity> getAllUsersByRole(
	        @Param("role") String role,
	        @Param("search") String search
	) throws DataAccessException;
	
	public final String GET_LENDER_DETAILS_BY_ID = """
				SELECT new project.borrowhen.dao.entity.UserDetailsData(
				    u.id,
				    u.fullName,
				    u.userId,
				    u.emailAddress,
				    u.phoneNumber,
				    u.about,
				    u.barangay,
				    u.street,
				    u.city,
				    u.province,
				    u.postalCode,
				    CAST((
				        SELECT COUNT(i.id)
				        FROM InventoryEntity i
				        WHERE i.userId = u.id AND i.isDeleted = false
				    ) AS INTEGER),
								        CAST((
				        SELECT count(br.id)
				        FROM BorrowRequestEntity br
				        JOIN InventoryEntity i ON i.id = br.inventoryId
				        WHERE i.userId = u.id
				          AND br.status IN ('PAID')
				    ) AS INTEGER),
								     CAST((
				        SELECT COALESCE(SUM(br.price * br.qty), 0)
				        FROM BorrowRequestEntity br
				        JOIN InventoryEntity i ON i.id = br.inventoryId
				        WHERE i.userId = u.id
				          AND br.status IN ('PAID')
				    ) AS DOUBLE),
								        CAST(ROUND((
				        SELECT COALESCE(AVG(br.rating), 0)
				        FROM BorrowRequestEntity br
				        JOIN InventoryEntity i ON i.id = br.inventoryId
				        WHERE i.userId = u.id
				          AND br.status IN ('PAID')
				          AND br.rating IS NOT NULL
				    ), 0) AS INTEGER)
				)
				FROM UserEntity u
				WHERE u.isDeleted = false	
				AND u.id = :id	
			""";
	
	@Query(GET_LENDER_DETAILS_BY_ID)
	public UserDetailsData getLenderDetails(@Param("id") int id) throws DataAccessException;
	
	public final String GET_BORROWER_DETAILS_BY_ID = """
			SELECT new project.borrowhen.dao.entity.UserDetailsData(
			    u.id,
			    u.fullName,
			    u.userId,
			    u.emailAddress,
			    u.phoneNumber,
			    u.about,
			    u.barangay,
			    u.street,
			    u.city,
			    u.province,
			    u.postalCode,
			    CAST((
				    SELECT COALESCE(SUM(br.qty), 0)
				    FROM BorrowRequestEntity br
				    WHERE br.userId = u.id
				      AND br.status IN ('PAID')
				) AS INTEGER),
				CAST((
				    SELECT COALESCE(COUNT(br.id), 0)
				    FROM BorrowRequestEntity br
				    WHERE br.userId = u.id
				      AND br.status IN ('PENDING')
				) AS INTEGER),
				CAST((
				    SELECT COALESCE(SUM(br.price * br.qty), 0)
				    FROM BorrowRequestEntity br
				    WHERE br.userId = u.id
				      AND br.status IN ('PAYMENT PENDING')
				) AS DOUBLE),
				CAST((
				    SELECT COALESCE(SUM(br.qty), 0)
				    FROM BorrowRequestEntity br
				    WHERE br.userId = u.id
				      AND br.status IN ('COMPLETED', 'PAYMENT PENDING', 'PAID')
				) AS INTEGER),
				false

			)
			FROM UserEntity u  
			WHERE u.isDeleted = false	
			AND u.id = :id	
		""";

	@Query(GET_BORROWER_DETAILS_BY_ID)
	public UserDetailsData getBorrowerDetails(@Param("id") int id) throws DataAccessException;
	
    public final String DELETE_USER = 
    	    """
    			UPDATE users
    			SET is_deleted = true,
    			updated_date = :updatedDate
    			WHERE id = :id
    		""";

	@Modifying
	@Transactional
	@Query(value=DELETE_USER, nativeQuery=true)
	public void deleteUser(
	    @Param("id") int userId,
	    @Param("updatedDate") Date updatedDate
	) throws DataAccessException;
	
	public final String GET_ADMIN_OVERVIEW = """
		    WITH total_borrowers AS (
		        SELECT CAST(COALESCE(COUNT(*), 0) AS INT) AS value
		        FROM users
		        WHERE role = 'BORROWER' AND is_deleted = false
		    ),
		    total_lenders AS (
		        SELECT CAST(COALESCE(COUNT(*), 0) AS INT) AS value
		        FROM users
		        WHERE role = 'LENDER' AND is_deleted = false
		    ),
		    total_items AS (
		        SELECT CAST(COALESCE(COUNT(*), 0) AS INT) AS value
		        FROM inventory
		        WHERE is_deleted = false
		    ),
		    total_qty AS (
		        SELECT CAST(COALESCE(SUM(total_qty), 0) AS INT) AS value
		        FROM inventory
		        WHERE is_deleted = false
		    ),
		    total_available_qty AS (
		        SELECT CAST(COALESCE(SUM(available_qty), 0) AS INT) AS value
		        FROM inventory
		        WHERE is_deleted = false
		    ),
		    total_requests AS (
		        SELECT CAST(COALESCE(COUNT(*), 0) AS INT) AS value
		        FROM borrow_request
		        WHERE is_deleted = false
		    ),
		    total_revenues AS (
		        SELECT CAST(COALESCE(SUM(price * qty), 0) AS DOUBLE PRECISION) AS value
		        FROM borrow_request
		        WHERE is_deleted = false AND status = 'PAID'
		    )
		    SELECT 
		        (SELECT value FROM total_borrowers) AS total_borrowers,
		        (SELECT value FROM total_lenders) AS total_lenders,
		        (SELECT value FROM total_items) AS total_items,
		        (SELECT value FROM total_qty) AS total_qty,
		        (SELECT value FROM total_available_qty) AS total_available_qty,
		        (SELECT value FROM total_requests) AS total_requests,
		        (SELECT value FROM total_revenues) AS total_revenues
		""";

	
	@Query(value=GET_ADMIN_OVERVIEW, nativeQuery=true)
	public AdminDashboardOverview getAdminOverview() throws DataAccessException;
}
