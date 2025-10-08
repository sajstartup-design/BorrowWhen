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
import project.borrowhen.dao.entity.UserData;
import project.borrowhen.dao.entity.UserEntity;

public interface UserDao extends JpaRepository<UserEntity, Integer> {

	public final String GET_ALL_USERS_NO_GROUP =
		    "SELECT new project.borrowhen.dao.entity.UserData(" +
		    "   u.id, u.fullName, u.gender, u.birthDate, u.phoneNumber, u.emailAddress, " +
		    "   u.barangay, u.street, u.city, u.province, u.postalCode, u.about, u.userId, u.password, u.role, " +
		    "   u.createdDate, u.updatedDate, " +
		    "   (CASE WHEN (SELECT COUNT(br2) FROM BorrowRequestEntity br2 WHERE br2.userId = u.id AND br2.status <> 'COMPLETED') > 0 THEN false ELSE true END) " +
		    ") " +
		    "FROM UserEntity u " +
		    "WHERE u.isDeleted = false " +
		    "AND ( " +
		    "   (:search IS NOT NULL AND :search <> '' AND ( " +
		    "       LOWER(u.fullName) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
		    "       LOWER(u.emailAddress) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
		    "       LOWER(u.phoneNumber) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
		    "       LOWER(u.userId) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
		    "       LOWER(u.role) LIKE LOWER(CONCAT('%', :search, '%'))" +
		    "   )) " +
		    "   OR (:search IS NULL OR :search = '') " +
		    ")";

	@Query(GET_ALL_USERS_NO_GROUP)
	Page<UserData> getAllUsers(Pageable pageable, @Param("search") String search) throws DataAccessException;

	
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
		    "postalCode = :postalCode, " +
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

}
