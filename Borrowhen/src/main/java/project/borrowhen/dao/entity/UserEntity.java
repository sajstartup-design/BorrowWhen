package project.borrowhen.dao.entity;

import java.sql.Date;
import java.sql.Timestamp;

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
	
	private String fullName;
	
	private String gender;
	
	private Date birthDate;
	
	private String phoneNumber;
	
	private String emailAddress;
	
	private String barangay;
	
	private String street;
	
	private String city;
	
	private String province;
	
	private String postalCode;
	
	private String about;
	
	private String userId;
	
	private String password;
	
	private String role;
	
	private Timestamp createdDate;
	
	private Timestamp updatedDate;
	
	private Boolean isDeleted;
	
}
