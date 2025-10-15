package project.borrowhen.dto;

import java.util.List;

import lombok.Data;
import project.borrowhen.object.BorrowRequestObj;
import project.borrowhen.object.NotificationObj;

@Data
public class DashboardDto {

	private List<BorrowRequestObj> overdues;
	
	private List<BorrowRequestObj> paymentPendings;
	
	private List<NotificationObj> recentNotifications;
	
	private int totalPending;
	
	private int totalApproved;
	
	private int totalPickUpReady;
	
	private int totalOngoing;
	
	private int totalComplete;
	
	private int totalPaymentPending;
	
	private int totalPaid;
	
	private int rejected;
}
