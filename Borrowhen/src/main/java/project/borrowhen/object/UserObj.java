package project.borrowhen.object;

import java.sql.Date;

import lombok.Data;

@Data
public class UserObj {
	
	private String encryptedId;
	
	private String firstName;
	
	private String middleName;
	
	private String familyName;
	
	private String address;
	
	private String emailAddress;
	
	private String phoneNumber;
	
	private String birthDate;
	
	private String gender;
	
	private String userId;
		
	private String role;
	
	private String createdDate;
	
	private String updatedDate;
	
	private int page;
}
