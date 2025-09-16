package project.borrowhen.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import project.borrowhen.common.util.StripeUtil;

@Controller
public class PaymentController {

	@GetMapping("/payment")
	public String showPaymentScreen(@RequestParam(value = "encryptedId", required = false) String encryptedId,
	                                Model model) {
		
		model.addAttribute("publicKey", StripeUtil.STRIPE_API_PUBLISHABLE_KEY);
		
		return "payment/payment";
	}
	
	@GetMapping("/payment/testing")
	public String showPaymentTesting(Model model) {
		
		model.addAttribute("publicKey", StripeUtil.STRIPE_API_PUBLISHABLE_KEY);
		
		return "payment";
	}
}
