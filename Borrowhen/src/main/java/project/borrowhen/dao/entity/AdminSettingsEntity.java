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
	
	private int userPerPage = 10;
    private int inventoryPerPage = 10;
    private int requestPerPage = 10;
    private int paymentPerPage = 10;

    private boolean showInventoryPage = true;
    private boolean showNotificationPage = true;
    private boolean showHistoryPage = true;
    private boolean showPaymentPage = true;
}
