package project.borrowhen.dao.entity;

import java.sql.Timestamp;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name="inventory")
public class InventoryEntity {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	
	private int userId;
	
	private String itemName;
	
	private double price;
	
	private int totalQty;
	
	private String status;
	
	private Timestamp createdDate;
	
	private Timestamp updatedDate;
	
	private Boolean isDeleted;
}
