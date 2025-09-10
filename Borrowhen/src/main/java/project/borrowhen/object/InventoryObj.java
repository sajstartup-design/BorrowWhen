package project.borrowhen.object;

import lombok.Data;

@Data
public class InventoryObj {
	
	private String encryptedId;
	
	private String itemName;
	
	private Double price;

	private Integer totalQty;
}
