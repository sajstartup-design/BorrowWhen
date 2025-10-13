package project.borrowhen.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import project.borrowhen.common.constant.MessageConstant;
import project.borrowhen.common.util.CipherUtil;
import project.borrowhen.common.util.StripeUtil;
import project.borrowhen.dto.BorrowRequestDto;
import project.borrowhen.dto.PaymentDto;
import project.borrowhen.service.BorrowRequestService;
import project.borrowhen.service.PaymentService;

@Controller
public class PaymentController {
	
	@Autowired
	private BorrowRequestService borrowRequestService;
	
	@Autowired
	private PaymentService paymentService;

	@GetMapping("/payment")
	public String showPaymentScreen(@RequestParam(value = "encryptedId", required = false) String encryptedId,
	                                Model model, RedirectAttributes ra) {
		
		try {
			
			PaymentDto paymentInDto = new PaymentDto();
			
			paymentInDto.setEncryptedId(encryptedId);
			
			PaymentDto outDto = paymentService.hasPaid(paymentInDto);
			
			if(outDto.isPaid()) {
				return "redirect:/dashboard";
			}
			
			model.addAttribute("publicKey", StripeUtil.STRIPE_API_PUBLISHABLE_KEY);
			
			BorrowRequestDto borrowRequestInDto = new BorrowRequestDto();
			
			borrowRequestInDto.setEncryptedId(encryptedId);
			
			BorrowRequestDto borrowRequestOutDto = borrowRequestService.getBorrowRequestDetailsForBorrower(borrowRequestInDto);
			
			borrowRequestOutDto.setEncryptedId(encryptedId);
					
			PaymentDto paymentOutDto = paymentService.getPaymentIntent(paymentInDto);
			
			model.addAttribute("borrowRequestDto", borrowRequestOutDto);
			
			model.addAttribute("paymentDto", paymentOutDto);
			
			return "payment/payment";
		}catch(Exception e) {
			
			e.printStackTrace();
			
			ra.addFlashAttribute("errorMsg", MessageConstant.SOMETHING_WENT_WRONG);
			
			return "redirect:/dashboard";
		}
	}

	
	@GetMapping("/payment/view")
	public String showPayment() {
		
		return "payment/payment-view";
	}
	
	@GetMapping("/payment/testing")
	public String showPaymentTesting(Model model) {
		
		model.addAttribute("publicKey", StripeUtil.STRIPE_API_PUBLISHABLE_KEY);
		
		return "payment";
	}
	
    @GetMapping("/payment/receipt")
    public String getReceipt(@RequestParam String encryptedId, RedirectAttributes ra) throws Exception {
    	
    	try {
        	PaymentDto inDto = new PaymentDto();
        	
        	inDto.setEncryptedId(encryptedId);
        	
        	PaymentDto outDto = paymentService.getPaymentReceipt(inDto);
            
        	return "redirect:" + outDto.getReceiptUrl();
            
    	} catch (Exception e) {
    		
    		e.printStackTrace();
    		
    		ra.addFlashAttribute("errorMsg", MessageConstant.SOMETHING_WENT_WRONG);
    		
    		return "redirect:/payment/view";
    	}
    }
}
