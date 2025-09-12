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
	
	private Timestamp createdDate;
	
	private Timestamp updatedDate;
	
	private Boolean isDeletable;
}
