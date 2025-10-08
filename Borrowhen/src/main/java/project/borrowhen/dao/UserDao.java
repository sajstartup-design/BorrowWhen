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
		    "   u.id, u.firstName, u.middleName, u.familyName, u.address, u.emailAddress, " +
		    "   u.phoneNumber, u.birthDate, u.gender, u.userId, u.password, u.role, " +
		    "   u.createdDate, u.updatedDate, " +
		    "   (CASE WHEN (SELECT COUNT(br2) FROM BorrowRequestEntity br2 WHERE br2.userId = u.id AND br2.status <> 'COMPLETED') > 0 THEN false ELSE true END) " +
		    ") " +
		    "FROM UserEntity u " +
		    "WHERE u.isDeleted = false " +
		    "AND ( " +
		    "   (:search IS NOT NULL AND :search <> '' AND ( " +
		    "       LOWER(u.firstName) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
		    "       LOWER(u.middleName) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
		    "       LOWER(u.familyName) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
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
		    "address = :address, " +
		    "email_address = :emailAddress, " +
		    "phone_number = :phoneNumber, " +
		    "birth_date = :birthDate, " +
		    "gender = :gender, " +
		    "user_id = :userId, " +
		    "password = CASE WHEN :hasChanged = true THEN :password ELSE password END, " +
		    "role = :role, " +
		    "updated_date = :updatedDate " +
		    "WHERE id = :id";

    @Modifying
    @Transactional
    @Query(value = UPDATE_USER, nativeQuery = true)
    public void updateUser(
            @Param("id") int id,
            @Param("fullName") String fullName,
            @Param("address") String address,
            @Param("emailAddress") String emailAddress,
            @Param("phoneNumber") String phoneNumber,
            @Param("birthDate") Date birthDate,
            @Param("gender") String gender,
            @Param("userId") String userId,
            @Param("password") String password,
            @Param("role") String role,
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
    	    "   OR LOWER(u.firstName) LIKE LOWER(CONCAT('%', :search, '%')) " +
    	    "   OR LOWER(CONCAT(u.firstName, ' ', u.familyName)) LIKE LOWER(CONCAT('%', :search, '%')) " +
    	    "   OR LOWER(CONCAT(u.firstName, ' ', u.middleName, ' ', u.familyName)) LIKE LOWER(CONCAT('%', :search, '%')) " +
    	    "   OR LOWER(u.familyName) LIKE LOWER(CONCAT('%', :search, '%')) " +
    	    "   OR LOWER(CONCAT(u.familyName, ' ', u.firstName)) LIKE LOWER(CONCAT('%', :search, '%')) " +
    	    ")";



	@Query(value = GET_ALL_USERS_BY_ROLE)
	public List<UserEntity> getAllUsersByRole(
	        @Param("role") String role,
	        @Param("search") String search
	) throws DataAccessException;

}
