package project.borrowhen.object;


import lombok.Data;

@Data
public class InventoryObj {
	
	private String owner;
	
	private String userId;
	
	private String encryptedId;
	
	private String itemName;
	
	private Double price;

	private Integer totalQty;
	
	private Integer availableQty;
	
	private String createdDate;
	
	private String updatedDate;
	
	private Boolean isDeletable;
	
	private Boolean isEditable;
	
	private String barangay;
	
	private int totalLent;
}
