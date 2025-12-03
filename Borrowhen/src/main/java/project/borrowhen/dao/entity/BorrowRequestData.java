package project.borrowhen.dao.entity;

import java.sql.Date;
import java.sql.Timestamp;

import org.springframework.context.annotation.Scope;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Scope("prototype")
public class BorrowRequestData {
	
	
	
	// Constructor for main borrow request details
    public BorrowRequestData(int borrowRequestId, String borrowerFullName, String borrowerUserId,
                             String lenderFullName, String lenderUserId, String itemName,
                             double price, int qty, Date dateToBorrow, Date dateToReturn,
                             String status, Timestamp createdDate, Timestamp updatedDate,
                             String purpose) {
        this.borrowRequestId = borrowRequestId;
        this.borrowerFullName = borrowerFullName;
        this.borrowerUserId = borrowerUserId;
        this.lenderFullName = lenderFullName;
        this.lenderUserId = lenderUserId;
        this.itemName = itemName;
        this.price = price;
        this.qty = qty;
        this.dateToBorrow = dateToBorrow;
        this.dateToReturn = dateToReturn;
        this.status = status;
        this.createdDate = createdDate;
        this.updatedDate = updatedDate;
        this.purpose = purpose;
    }

    // Constructor for feedback & rating only
    public BorrowRequestData(int borrowRequestId, String borrowerFullName, String borrowerUserId, String itemName,
                             String feedback, double rating) {
        this.borrowRequestId = borrowRequestId;
        this.borrowerFullName = borrowerFullName;
        this.borrowerUserId = borrowerUserId;
        this.itemName = itemName;
        this.feedback = feedback;
        this.rating = rating;
    }

	private int borrowRequestId;
	
	private String borrowerFullName;
	
	private String borrowerUserId;
	
	private String lenderFullName;
	
	private String lenderUserId;

	private String itemName;
	
	private double price;
	
	private int qty;
	
	private Date dateToBorrow;
	
	private Date dateToReturn;
	
	private String status;
		
	private Timestamp createdDate;
	
	private Timestamp updatedDate;
	
	private String feedback;
	
	private double rating;
	
	private String purpose;
}
