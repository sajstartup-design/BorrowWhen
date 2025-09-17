package project.borrowhen.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import project.borrowhen.common.constant.MessageConstant;
import project.borrowhen.common.util.CipherUtil;
import project.borrowhen.common.util.StripeUtil;
import project.borrowhen.dto.BorrowRequestDto;
import project.borrowhen.service.BorrowRequestService;

@Controller
public class PaymentController {
	
	@Autowired
	private BorrowRequestService borrowRequestService;

	@GetMapping("/payment")
	public String showPaymentScreen(@RequestParam(value = "encryptedId", required = false) String encryptedId,
	                                Model model, RedirectAttributes ra) {
		
		try {
			
			model.addAttribute("publicKey", StripeUtil.STRIPE_API_PUBLISHABLE_KEY);
			
			BorrowRequestDto inDto = new BorrowRequestDto();
			
			inDto.setEncryptedId(encryptedId);
			
			BorrowRequestDto outDto = borrowRequestService.getBorrowRequestDetailsForBorrower(inDto);
			
			model.addAttribute("borrowRequestDto", outDto);
			
			return "payment/payment";
		}catch(Exception e) {
			
			e.printStackTrace();
			
			ra.addFlashAttribute("errorMsg", MessageConstant.SOMETHING_WENT_WRONG);
			
			return "redirect:/dashboard";
		}
	}
	
	@GetMapping("/payment/testing")
	public String showPaymentTesting(Model model) {
		
		model.addAttribute("publicKey", StripeUtil.STRIPE_API_PUBLISHABLE_KEY);
		
		return "payment";
	}
}
