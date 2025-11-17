package project.borrowhen.dao.entity;

import java.sql.Timestamp;

import org.springframework.context.annotation.Scope;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Scope("prototype")
public class InventoryData {
		
	public InventoryData(Integer inventoryId, String itemName, Double price, Integer totalQty, Integer availableQty) {
		this.inventoryId = inventoryId;
		this.itemName = itemName;
		this.price = price;
		this.totalQty = totalQty;
		this.availableQty = availableQty;
	}
	
	public InventoryData(Integer inventoryId, String itemName, Double price, Integer totalQty, Integer availableQty, Integer totalBorrows, Double totalRevenue) {
		this.inventoryId = inventoryId;
		this.itemName = itemName;
		this.price = price;
		this.totalQty = totalQty;
		this.availableQty = availableQty;
		this.totalBorrows = totalBorrows;
		this.totalRevenue = totalRevenue;
	}

	private Integer inventoryId;
	
	private String fullName;
	
	private String userId;
	
	private String itemName; 
	
	private Double price;
	
	private Integer totalQty;
	
	private Integer availableQty;
	
	private Timestamp createdDate;
	
	private Timestamp updatedDate;
	
	private Boolean isEditable;
	
	private Boolean isDeletable;
	
	private String barangay;
	
	private int totalBorrows;
	
	private Double totalRevenue;
}
