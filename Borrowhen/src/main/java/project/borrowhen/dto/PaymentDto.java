package project.borrowhen.dto;

import java.util.List;

import lombok.Data;
import project.borrowhen.object.FilterAndSearchObj;
import project.borrowhen.object.PaginationObj;
import project.borrowhen.object.PaymentObj;

@Data
public class PaymentDto {
	
	private String encryptedId;
	
	private int borrowRequestId;

	private double amount;
	
	private String emailAddress;
	
	 // Stripe info
    private String stripePaymentId; // pi_xxx
    private String clientSecret;    // needed by frontend to confirm payment
    private String status;          // succeeded, requires_payment_method, etc.
    private String receiptUrl;      // charge receipt
    
    private List<PaymentObj> payments;
    
	private PaginationObj pagination;
	
	private FilterAndSearchObj filter;
	
	private boolean paid;
	
}
