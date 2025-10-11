package project.borrowhen.object;

import lombok.Data;

@Data
public class UserObj {
	
	private String encryptedId;
	
	private String gender;
	
	private String birthDate;
	
	private String fullName;
	
	private String phoneNumber;
	
	private String emailAddress;
	
	private String barangay;
	
	private String street;
	
	private String city;
	
	private String province;
	
	private String postalCode;
	
	private String about;
	
	private String userId;
		
	private String role;
	
	private String createdDate;
	
	private String updatedDate;
	
	private Boolean isDeletable;
	
	private int totalItem;
	
	private int totalRequest;
	
	private double totalRevenue;
	
	private int rating;
	
	private int page;
}
