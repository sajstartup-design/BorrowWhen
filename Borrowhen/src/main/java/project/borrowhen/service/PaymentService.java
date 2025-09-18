package project.borrowhen.service;

import org.springframework.stereotype.Service;

import project.borrowhen.dto.PaymentDto;

@Service
public interface PaymentService {

	public void createPaymentIntent(PaymentDto inDto) throws Exception;
}
