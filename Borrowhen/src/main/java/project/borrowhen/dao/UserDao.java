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
import project.borrowhen.dao.entity.UserEntity;

public interface UserDao extends JpaRepository<UserEntity, Integer> {

	public final String GET_ALL_USERS = "SELECT e "
			+ "FROM UserEntity e "
			+ "WHERE e.isDeleted = false ";
	
	@Query(value=GET_ALL_USERS)
	public Page<UserEntity> getAllUsers(Pageable pageable)  throws DataAccessException;
	
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
	
	public final String UPDATE_USER = "UPDATE users "
            + "SET first_name = :firstName, "
            + "middle_name = :middleName, "
            + "family_name = :familyName, "
            + "address = :address, "
            + "email_address = :emailAddress, "
            + "phone_number = :phoneNumber, "
            + "birth_date = :birthDate, "
            + "gender = :gender, "
            + "user_id = :userId, "
            + "password = :password, "
            + "role = :role, "
            + "updated_date = :updatedDate "
            + "WHERE id = :id";

    @Modifying
    @Transactional
    @Query(value = UPDATE_USER, nativeQuery = true)
    public void updateUser(
            @Param("id") int id,
            @Param("firstName") String firstName,
            @Param("middleName") String middleName,
            @Param("familyName") String familyName,
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
			+ "WHERE e.isDeleted = false ";
    
    @Query(value=GET_ALL_USER_ID)
    public List<String> getAllUserId() throws DataAccessException;
}
