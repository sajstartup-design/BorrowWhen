package project.borrowhen.dao.entity;

import org.springframework.context.annotation.Scope;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Scope("prototype")
public class BorrowRequestOverview {

	private int totalPending;
	
	private int totalApproved;
	
	private int totalOngoing;
	
	private int totalPendingPayment;
	
	private int totalPaid;
	
	private int totalOverdue;
}
