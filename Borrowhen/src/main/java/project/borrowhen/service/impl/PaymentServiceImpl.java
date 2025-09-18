package project.borrowhen.service.impl;

import java.sql.Timestamp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.stripe.Stripe;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;

import project.borrowhen.common.constant.CommonConstant;
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

}
