package project.borrowhen.service.impl;

import java.sql.Timestamp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.stripe.Stripe;
import com.stripe.model.Charge;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;

import project.borrowhen.common.constant.CommonConstant;
import project.borrowhen.common.util.CipherUtil;
import project.borrowhen.common.util.DateFormatUtil;
import project.borrowhen.common.util.StripeUtil;
import project.borrowhen.dao.PaymentDao;
import project.borrowhen.dao.entity.PaymentEntity;
import project.borrowhen.dto.PaymentDto;
import project.borrowhen.service.PaymentService;

@Service
public class PaymentServiceImpl implements PaymentService{
	
	@Autowired
	private PaymentDao paymentDao;
	
	@Autowired
	private CipherUtil cipherUtil;

	@Override
	public void createPaymentIntent(PaymentDto inDto) throws Exception{
		
		Timestamp dateNow = DateFormatUtil.getCurrentTimestamp();
		
		long amountInCentavos = Math.round(inDto.getAmount() * 100);
		
		Stripe.apiKey = StripeUtil.STRIPE_API_SECRET_KEY;

		PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
	        .setAmount(amountInCentavos)
	        .setCurrency("php")
	        .setReceiptEmail(inDto.getEmailAddress())
	        .build();


		PaymentIntent intent = PaymentIntent.create(params);
		
		PaymentEntity paymentEntity = new PaymentEntity();
		
		paymentEntity.setBorrowRequestId(inDto.getBorrowRequestId());
		paymentEntity.setStripePaymentId(intent.getId());
		paymentEntity.setStatus(CommonConstant.PENDING_PAYMENT);
		paymentEntity.setCreatedDate(dateNow);
		paymentEntity.setUpdatedDate(dateNow);
		paymentEntity.setIsDeleted(false);
		
		paymentDao.save(paymentEntity);
		
	}

	@Override
	public PaymentDto getPaymentIntent(PaymentDto inDto) throws Exception {
		
		int id = Integer.valueOf(cipherUtil.decrypt(inDto.getEncryptedId()));

	    PaymentEntity payment = paymentDao.getPaymentByBorrowRequestId(id);
	    if (payment == null) {
	        throw new RuntimeException("No payment found for borrowRequestId: " + inDto.getBorrowRequestId());
	    }
	    
	    Stripe.apiKey = StripeUtil.STRIPE_API_SECRET_KEY;

	    String stripePaymentId = payment.getStripePaymentId(); 
	    PaymentIntent intent = PaymentIntent.retrieve(stripePaymentId);

	    // 3. Populate DTO
	    PaymentDto dto = new PaymentDto();
	    dto.setBorrowRequestId(inDto.getBorrowRequestId());
	    dto.setEncryptedId(inDto.getEncryptedId());
	    dto.setStripePaymentId(intent.getId());
	    dto.setClientSecret(intent.getClientSecret());
	    dto.setAmount(intent.getAmount() / 100.0);
	    dto.setEmailAddress(intent.getReceiptEmail());
	    dto.setStatus(intent.getStatus());

	    Charge latestCharge = intent.getLatestChargeObject();
	    if (latestCharge != null) {
	        dto.setReceiptUrl(latestCharge.getReceiptUrl());
	    }

	    return dto;
		
		
	}

	@Override
	public void updatePaymentStatus(PaymentDto inDto) throws Exception {
		
		paymentDao.updatePaymentStatusByBorrowRequestId(inDto.getBorrowRequestId(), inDto.getStatus());
	}

}
