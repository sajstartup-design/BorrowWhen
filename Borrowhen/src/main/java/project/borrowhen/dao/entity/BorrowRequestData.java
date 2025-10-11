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
}
