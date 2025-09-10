package project.borrowhen.object;

import java.sql.Date;

import lombok.Data;

@Data
public class InventoryObj {
	
	private String owner;
	
	private String encryptedId;
	
	private String itemName;
	
	private Double price;

	private Integer totalQty;
	
	private Date createdDate;
	
	private Date updatedDate;
}
