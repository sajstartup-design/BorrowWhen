package project.borrowhen.object;

import java.sql.Timestamp;

import lombok.Data;

@Data
public class PaymentObj {

	private String encryptedId;
	
	private String fullName;
	
	private String emailAddress;
	
	private String itemName;
	
	private double price;
	
	private int qty;
	
	private double totalAmount;
	
	private Timestamp dateCheckout;
	
	private String paymentMethod;
	
	private String status;
}
