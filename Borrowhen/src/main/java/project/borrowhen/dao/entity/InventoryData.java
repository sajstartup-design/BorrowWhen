package project.borrowhen.dao.entity;

import java.sql.Date;

import org.springframework.context.annotation.Scope;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Scope("prototype")
public class InventoryData {

	private int inventoryId;
	
	private String firstName;
	
	private String familyName;
	
	private String itemName;
	
	private Double price;
	
	private int totalQty;
	
	private Date createdDate;
	
	private Date updatedDate;
}
