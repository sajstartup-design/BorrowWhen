package project.borrowhen.object;

import java.sql.Date;

import lombok.Data;

@Data
public class BorrowRequestObj {
	
	private String encryptedId;
	
	private String borrower;
	
	private String borrowerUserId;
	
	private String lender;
	
	private String lenderUserId;
	
	private String itemName;
	
	private double price;
	
	private int qty;
	
	private Date dateToBorrow;
	
	private Date dateToReturn;
	
	private String status;
	
	private String createdDate;
	
	private String updatedDate;
}
