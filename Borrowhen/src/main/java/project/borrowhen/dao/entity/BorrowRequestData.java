package project.borrowhen.dao.entity;

import java.sql.Date;
import java.sql.Timestamp;

import org.springframework.context.annotation.Scope;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Scope("prototype")
public class BorrowRequestData {


	private int borrowRequestId;
	
	private String borrowerFirstName;
	
	private String borrowerFamilyName;
	
	private String lenderFirstName;
	
	private String lenderFamilyName;
	
	private String itemName;
	
	private double price;
	
	private int qty;
	
	private Date dateToBorrow;
	
	private Date dateToReturn;
	
	private String status;
		
	private Timestamp createdDate;
	
	private Timestamp updatedDate;
}
