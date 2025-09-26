package project.borrowhen.controller;

import com.stripe.model.PaymentIntent;
import com.stripe.net.RequestOptions;
import com.stripe.param.PaymentIntentCreateParams;

import project.borrowhen.common.util.StripeUtil;
import project.borrowhen.dto.PaymentDto;
import project.borrowhen.object.FilterAndSearchObj;
import project.borrowhen.object.PaginationObj;
import project.borrowhen.service.PaymentService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

import com.stripe.Stripe;
import com.stripe.model.PaymentIntent;
import com.stripe.model.Charge;
import com.stripe.param.PaymentIntentCreateParams;

import java.util.*;

@RestController
@RequestMapping("/api")
public class PaymentRestController {
	
	@Autowired
    private PaymentService paymentService;

//	@GetMapping("/create-intent")
//	public Map<String, Object> createPaymentIntent(@RequestParam Long amount) throws Exception {
//	    Stripe.apiKey = StripeUtil.STRIPE_API_SECRET_KEY;
//
//	    // ✅ Create PaymentIntent without hardcoding payment methods
//	    PaymentIntentCreateParams params =
//	            PaymentIntentCreateParams.builder()
//	                    .setAmount(amount) // in centavos
//	                    .setCurrency("php")
//	                    .setReceiptEmail("julius.basas0123@gmail.com")
//	                    .build();
//
//	    PaymentIntent intent = PaymentIntent.create(params);
//
//	    // ✅ Build response
//	    Map<String, Object> response = new HashMap<>();
//	    response.put("clientSecret", intent.getClientSecret());
//	    response.put("paymentMethods", intent.getPaymentMethodTypes()); // 👈 Stripe will return enabled methods
//
//	    // ✅ Grab receipt URL if charge exists
//	    Charge latestCharge = intent.getLatestChargeObject();
//	    if (latestCharge != null) {
//	        response.put("receiptUrl", latestCharge.getReceiptUrl());
//	    }
//
//	    return response;
//	}

    
//    @GetMapping("/receipt")
//    public Map<String, Object> getReceipt(@RequestParam String paymentIntentId) throws Exception {
//        Map<String, Object> params = new HashMap<>();
//        params.put("expand", java.util.Arrays.asList("latest_charge"));
//
//        PaymentIntent intent = PaymentIntent.retrieve(paymentIntentId, params, null);
//
//        Map<String, Object> response = new HashMap<>();
//        if (intent.getLatestChargeObject() != null) {
//            response.put("receiptUrl", intent.getLatestChargeObject().getReceiptUrl());
//        }
//        return response;
//    }

    @GetMapping("/payment")
    public PaymentDto getPaymentsForBorrower(@RequestParam(defaultValue = "0") int page,
    		@RequestParam(required = false) String search) {
        try {
            PaymentDto inDto = new PaymentDto();

            PaginationObj pagination = new PaginationObj();
            pagination.setPage(page);

            FilterAndSearchObj filter = new FilterAndSearchObj();
            filter.setSearch(search);

            inDto.setPagination(pagination);
            inDto.setFilter(filter);

            return paymentService.getAllPaymentForBorrower(inDto);
            
        } catch (Exception e) {
            e.printStackTrace();

            return new PaymentDto();
        }
    }
}

