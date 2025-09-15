package project.borrowhen.controller;

import com.stripe.model.PaymentIntent;
import com.stripe.net.RequestOptions;
import com.stripe.param.PaymentIntentCreateParams;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

import com.stripe.Stripe;
import com.stripe.model.PaymentIntent;
import com.stripe.model.Charge;
import com.stripe.param.PaymentIntentCreateParams;

import java.util.*;

@RestController
@RequestMapping("/api/payment")
public class PaymentRestController {

    @GetMapping("/create-intent")
    public Map<String, Object> createPaymentIntent(@RequestParam Long amount) throws Exception {
        Stripe.apiKey = "312312"; // your secret key

        // ✅ Create PaymentIntent
        PaymentIntentCreateParams params =
                PaymentIntentCreateParams.builder()
                        .setAmount(amount) // in centavos
                        .setCurrency("php")
                        .setReceiptEmail("julius.basas0123@gmail.com")
                        .build();

        // Expand latest_charge so we get the Charge object
        Map<String, Object> expand = new HashMap<>();
        expand.put("expand", Arrays.asList("latest_charge"));

        PaymentIntent intent = PaymentIntent.create(params);

        // ✅ Build response
        Map<String, Object> response = new HashMap<>();
        response.put("clientSecret", intent.getClientSecret());

        // ✅ Grab receipt URL if charge exists
        Charge latestCharge = intent.getLatestChargeObject();
        if (latestCharge != null) {
            response.put("receiptUrl", latestCharge.getReceiptUrl());
        }

        return response;
    }
    
    @GetMapping("/receipt")
    public Map<String, Object> getReceipt(@RequestParam String paymentIntentId) throws Exception {
        Map<String, Object> params = new HashMap<>();
        params.put("expand", java.util.Arrays.asList("latest_charge"));

        PaymentIntent intent = PaymentIntent.retrieve(paymentIntentId, params, null);

        Map<String, Object> response = new HashMap<>();
        if (intent.getLatestChargeObject() != null) {
            response.put("receiptUrl", intent.getLatestChargeObject().getReceiptUrl());
        }
        return response;
    }


}

