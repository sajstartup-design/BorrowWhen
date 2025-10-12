package project.borrowhen.dao.entity;

import org.springframework.context.annotation.Scope;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Scope("prototype")
public class LenderInventoryOverview {
	
	private int totalItems;
	
	private int totalQty;
	
	private int totalAvailableQty;
	
	private double totalRevenue;
}
