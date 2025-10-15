package project.borrowhen.dao.entity;

import java.sql.Timestamp;

import org.springframework.context.annotation.Scope;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Scope("prototype")
public class LenderDashboardOverview {
	
	private int totalItems;
	
	private int totalItemsQty;
	
	private int totalItemsBorrowedQty;
	
	private int totalOngoingBorrowRequest;
	
	private double totalRevenue;
	
	private int totalAvailableItemsQty;
}
