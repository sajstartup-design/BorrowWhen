package project.borrowhen.dao.entity;

import org.springframework.context.annotation.Scope;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Scope("prototype")
public class UserDetailsData {
	
	// Constructor 1: Only general info + summary stats
    public UserDetailsData(int id, String fullName, String userId, String emailAddress, String phoneNumber,
                           String about, String barangay, String street, String city, String province,
                           String postalCode, int totalItem, int totalRequest, double totalRevenue, int rating) {
        this.id = id;
        this.fullName = fullName;
        this.userId = userId;
        this.emailAddress = emailAddress;
        this.phoneNumber = phoneNumber;
        this.about = about;
        this.barangay = barangay;
        this.street = street;
        this.city = city;
        this.province = province;
        this.postalCode = postalCode;
        this.totalItem = totalItem;
        this.totalRequest = totalRequest;
        this.totalRevenue = totalRevenue;
        this.rating = rating;
    }

    // Constructor 2: Only general info + borrowed/pending/returned/active loans stats
    public UserDetailsData(int id, String fullName, String userId, String emailAddress, String phoneNumber,
                           String about, String barangay, String street, String city, String province,
                           String postalCode, int borrowedItems, int pendingRequests, double activeLoans, int returnedItems, boolean n) {
        this.id = id;
        this.fullName = fullName;
        this.userId = userId;
        this.emailAddress = emailAddress;
        this.phoneNumber = phoneNumber;
        this.about = about;
        this.barangay = barangay;
        this.street = street;
        this.city = city;
        this.province = province;
        this.postalCode = postalCode;
        this.borrowedItems = borrowedItems;
        this.pendingRequests = pendingRequests;        
        this.activeLoans = activeLoans;
        this.returnedItems = returnedItems;
        this.n = n;
    }
    
 

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
    
    private int borrowedItems;
    
    private int pendingRequests;
    
    private double activeLoans;
    
    private int returnedItems;
    
    private boolean n;
    
    

    
}
