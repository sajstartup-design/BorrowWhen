package project.borrowhen.dao.entity;

import java.sql.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name="borrow_Request")
public class BorrowRequestEntity {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	
	private int inventoryId;
	
	private int userId;
	
	private Date dateToBorrow;
	
	private Date dateToReturn;
	
	private int qty;

	private String status;
	
	private Date createdDate;
	
	private Date updatedDate;
	
	private Boolean isDeleted;
}
