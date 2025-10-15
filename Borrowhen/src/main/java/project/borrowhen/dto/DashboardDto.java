package project.borrowhen.dto;

import java.util.List;

import lombok.Data;
import project.borrowhen.dao.entity.BorrowRequestOverview;
import project.borrowhen.dao.entity.LenderDashboardOverview;
import project.borrowhen.object.BorrowRequestObj;
import project.borrowhen.object.InventoryObj;
import project.borrowhen.object.NotificationObj;

@Data
public class DashboardDto {

	private List<BorrowRequestObj> overdues;
	
	private List<BorrowRequestObj> paymentPendings;
	
	private List<NotificationObj> notifications;
	
	private BorrowRequestOverview overview;
		
	private LenderDashboardOverview lenderDashboardOverview;
	
	private List<InventoryObj> popularItems;
}
