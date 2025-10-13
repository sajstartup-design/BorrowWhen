package project.borrowhen.dao.entity;

import org.springframework.context.annotation.Scope;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Scope("prototype")
public class PaymentOverview {

	private int totalPaid;
	
	private int totalPending;
	
	private double totalRevenue;
	
	private double expectedRevenue;
}
