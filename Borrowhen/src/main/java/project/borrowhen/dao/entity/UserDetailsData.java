package project.borrowhen.dao.entity;

import org.springframework.context.annotation.Scope;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Scope("prototype")
public class UserDetailsData {

	private int id;
	
	private String fullName;
	
	private String userId;
	
	private String emailAddress;
	
	private String phoneNumber;
	
	private String about;
	
	private String barangay;
	
	private String street;
	
	private String city;
	
	private String province;
	
	private String postalCode;
	
	private int totalItem;
	
	private int totalRequest;
	
	private double totalRevenue;
	
	private int rating;
}
