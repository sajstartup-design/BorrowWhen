package project.borrowhen.service.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.stripe.Stripe;
import com.stripe.model.Charge;
import com.stripe.model.PaymentIntent;
import com.stripe.model.PaymentMethod;
import com.stripe.param.PaymentIntentCreateParams;

import project.borrowhen.common.constant.CommonConstant;
import project.borrowhen.common.util.CipherUtil;
import project.borrowhen.common.util.DateFormatUtil;
import project.borrowhen.common.util.StripeUtil;
import project.borrowhen.dao.PaymentDao;
import project.borrowhen.dao.entity.PaymentData;
import project.borrowhen.dao.entity.PaymentEntity;
import project.borrowhen.dao.entity.UserEntity;
import project.borrowhen.dto.PaymentDto;
import project.borrowhen.object.FilterAndSearchObj;
import project.borrowhen.object.PaginationObj;
import project.borrowhen.object.PaymentObj;
import project.borrowhen.service.AdminSettingsService;
import project.borrowhen.service.PaymentService;
import project.borrowhen.service.UserService;

@Service
public class PaymentServiceImpl implements PaymentService{
	
	@Autowired
	private PaymentDao paymentDao;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private CipherUtil cipherUtil;
	
	@Autowired
	private AdminSettingsService adminSettingsService;
    
    private int getMaxPaymentDisplay() {
        return adminSettingsService.getSettings().getPaymentPerPage();
   }

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
	    Stripe.apiKey = StripeUtil.STRIPE_API_SECRET_KEY;

	    PaymentEntity payment = paymentDao.getPaymentByBorrowRequestId(inDto.getBorrowRequestId());
	    if (payment == null) {
	        throw new RuntimeException("No payment found for borrowRequestId: " + inDto.getBorrowRequestId());
	    }

	    PaymentIntent intent = PaymentIntent.retrieve(payment.getStripePaymentId());

	    String method = "-";
	    if (intent.getPaymentMethod() != null) {
	        PaymentMethod pm = PaymentMethod.retrieve(intent.getPaymentMethod());
	        if (pm != null) {
	            method = pm.getType();
	        }
	    }

	    paymentDao.updatePaymentStatusByBorrowRequestId(
	        inDto.getBorrowRequestId(),
	        inDto.getStatus(),
	        method,
	        inDto.getEmailAddress()
	    );
	}

	@Override
	public PaymentDto getAllPaymentForLender(PaymentDto inDto) throws Exception {
		
		PaymentDto outDto = new PaymentDto();
		
		Pageable pageable = PageRequest.of(
	        inDto.getPagination().getPage(),
	        Integer.valueOf(getMaxPaymentDisplay())
	    );
	    
	    UserEntity user = userService.getLoggedInUser();
	    
	    FilterAndSearchObj filter = inDto.getFilter();
	    
	    System.out.println(filter.getSearch());
	    
	    Page<PaymentData> allPayments = paymentDao.getAllPaymentForLender(pageable, user.getId(), filter.getSearch());
	    
	    List<PaymentObj> payments = new ArrayList<>();
	    
		for(PaymentData payment : allPayments) {
			
			PaymentObj obj = new PaymentObj();
			
			obj.setEncryptedId(cipherUtil.encrypt(String.valueOf(payment.getPaymentId())));
			obj.setEmailAddress(payment.getEmailAddress());
			obj.setItemName(payment.getItemName());
			obj.setPrice(payment.getPrice());	
			obj.setQty(payment.getQty());
			obj.setTotalAmount(payment.getTotalAmount());
			obj.setDateCheckout(payment.getDateCheckout());
			obj.setPaymentMethod(payment.getPaymentMethod().toUpperCase());
			
			payments.add(obj);
	
		}
		
		PaginationObj pagination = new PaginationObj();
		
		pagination.setPage(allPayments.getNumber());
		pagination.setTotalPages(allPayments.getTotalPages());
		pagination.setTotalElements(allPayments.getTotalElements());
		pagination.setHasNext(allPayments.hasNext());
		pagination.setHasPrevious(allPayments.hasPrevious());
		
		outDto.setPayments(payments);
		outDto.setPagination(pagination);
		
	    return outDto;
	}

	@Override
	public PaymentDto hasPaid(PaymentDto inDto) throws Exception {
		
		PaymentDto outDto = new PaymentDto();
		
		int id = Integer.valueOf(cipherUtil.decrypt(inDto.getEncryptedId()));

	    PaymentEntity payment = paymentDao.getPaymentByBorrowRequestId(id);
	    if (payment == null) {
	        throw new RuntimeException("No payment found for borrowRequestId: " + inDto.getBorrowRequestId());
	    }
	    
	    outDto.setPaid(false);
	    
	    if(CommonConstant.PAID.equals(payment.getStatus())) {
	    	outDto.setPaid(true);
	    }
			
		return outDto;
	}
}
