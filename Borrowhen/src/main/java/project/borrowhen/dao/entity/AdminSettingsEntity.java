package project.borrowhen.dao.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name="admin_settings")
public class AdminSettingsEntity {

	@Id
	private int id;
	
	private int userPerPage;

	private int inventoryPerPage;
	
	private int requestPerPage;
	
	private boolean showInventoryPage;
	
	private boolean showNotificationPage;
	
	private boolean showHistoryPage;
	
	private boolean showPaymentPage;
}
