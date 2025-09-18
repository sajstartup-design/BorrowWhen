package project.borrowhen.dto;

import lombok.Data;

@Data
public class PaymentDto {
	
	private int borrowRequestId;

	private double amount;
	
	private String emailAddress;
}
