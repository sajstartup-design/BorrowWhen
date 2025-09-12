package project.borrowhen.object;


import lombok.Data;

@Data
public class InventoryObj {
	
	private String owner;
	
	private String encryptedId;
	
	private String itemName;
	
	private Double price;

	private Integer totalQty;
	
	private String createdDate;
	
	private String updatedDate;
}
