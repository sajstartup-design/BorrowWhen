package project.borrowhen.dto;

import com.stripe.model.PaymentIntent;

import lombok.Data;

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
}
