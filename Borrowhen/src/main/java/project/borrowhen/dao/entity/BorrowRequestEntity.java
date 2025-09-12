package project.borrowhen.dao.entity;

import java.sql.Date;
import java.sql.Timestamp;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name="borrow_request")
public class BorrowRequestEntity {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	 
	private int inventoryId;
	
	private int userId;
	
	private String itemName;
	
	private Date dateToBorrow;
	
	private Date dateToReturn;
	
	private int qty;
	
	private double price;

	private String status;
	
	private Timestamp createdDate;
	
	private Timestamp updatedDate;
	
	private Boolean isDeleted;
}
