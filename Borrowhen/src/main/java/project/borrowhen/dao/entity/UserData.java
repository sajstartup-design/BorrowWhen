package project.borrowhen.dao.entity;

import java.sql.Date;
import java.sql.Timestamp;

import org.springframework.context.annotation.Scope;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Scope("prototype")
public class UserData {

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
	
	private Boolean isDeletable;
}
