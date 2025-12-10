package project.borrowhen.dao.entity;

import org.springframework.context.annotation.Scope;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Scope("prototype")
public class AdminDashboardOverview {

	private int totalBorrowers;
	
	private int totalLenders;
	
	private int totalItems;
	
	private int totalQty;
	
	private int totalAvailableQty;
	
	private int totalRequests;
	
	private double totalRevenues;
	
	private int totalLost;
	
	private int totalDamaged;

}
