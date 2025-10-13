package project.borrowhen.controller.lender;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import project.borrowhen.common.constant.MessageConstant;
import project.borrowhen.dto.PaymentDto;
import project.borrowhen.service.PaymentService;

@Controller
public class L_PaymentController {
	
	@Autowired
	private PaymentService paymentService;

	@GetMapping("/lender/payment")
	public String showPaymentView(Model model,
			RedirectAttributes ra) {
		
		try {
			
			PaymentDto outDto = paymentService.getLenderPaymentOverview();
			
			model.addAttribute("paymentDto", outDto);
			
		}catch(Exception e) {
			e.printStackTrace();
			ra.addFlashAttribute("isError", true);
	        ra.addFlashAttribute("errorMsg", MessageConstant.SOMETHING_WENT_WRONG);
	        
	        return "redirect:/dashboard";
		}
		
		return "payment/lender/payment-view";
	}
}
