package project.borrowhen.dto;

import lombok.Data;

@Data
public class AdminSettingsDto {
	
	private int userPerPage;

	private int inventoryPerPage;
	
	private int requestPerPage;
	
	private int paymentPerPage;
	
	private boolean showInventoryPage;
	
	private boolean showNotificationPage;
	
	private boolean showHistoryPage;
	
	private boolean showPaymentPage;
}
