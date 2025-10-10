package project.borrowhen.dao.entity;

import java.sql.Timestamp;

import org.springframework.context.annotation.Scope;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Scope("prototype")
public class PaymentData {

	private int paymentId;
	
	private String fullName;
	
	private String emailAddress;
	
	private String itemName;
	
	private double price;
	
	private int qty;
	
	private double totalAmount;
	
	private Timestamp dateCheckout;
	
	private String paymentMethod;
	
	private String status;
	
	private int borrowRequestId;
}
