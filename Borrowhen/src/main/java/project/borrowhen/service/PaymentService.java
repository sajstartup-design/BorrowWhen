package project.borrowhen.service;

import org.springframework.stereotype.Service;

import project.borrowhen.dto.PaymentDto;

@Service
public interface PaymentService {

	public void createPaymentIntent(PaymentDto inDto) throws Exception;
	
	public PaymentDto getPaymentIntent(PaymentDto inDto) throws Exception;
	
	public void updatePaymentStatus(PaymentDto inDto) throws Exception;
	
	public PaymentDto getAllPaymentForLender(PaymentDto inDto) throws Exception;
	
	public PaymentDto hasPaid(PaymentDto inDto) throws Exception;
}
