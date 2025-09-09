package project.borrowhen.dao.entity;

import java.sql.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name="users")
public class UserEntity {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;
	
	private String firstName;
	
	private String middleName;
	
	private String familyName;
	
	private String address;
	
	private String emailAddress;
	
	private String phoneNumber;
	
	private Date birthDate;
	
	private String gender;
	
	private String userId;
	
	private String password;
	
	private String role;
	
	private Date createdDate;
	
	private Date updatedDate;
	
	private Boolean isDeleted;
	
}
