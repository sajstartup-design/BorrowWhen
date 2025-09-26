package project.borrowhen.controller.lender;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class L_PaymentController {

	@GetMapping("/lender/payment")
	public String showPaymentView() {
		
		return "payment/lender/payment-view";
	}
}
