package project.borrowhen.controller.lender;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import project.borrowhen.common.constant.MessageConstant;
import project.borrowhen.dto.BorrowRequestDto;
import project.borrowhen.service.BorrowRequestService;

@Controller
public class L_ReviewController {
	
	@Autowired
	private BorrowRequestService borrowRequestService;

	@GetMapping("/lender/reviews")
	public String showLenderReviews(Model model,
			RedirectAttributes ra) {
		
		try {
			BorrowRequestDto outDto = borrowRequestService.getReviewOverviewForLender();
			
			model.addAttribute("borrowRequestDto", outDto);
			
			
		}catch(Exception e) {
			
			e.printStackTrace();
			
			e.printStackTrace();
			ra.addFlashAttribute("isError", true);
	        ra.addFlashAttribute("errorMsg", MessageConstant.SOMETHING_WENT_WRONG);
	        
	        return "redirect:/lender/dashboard";
		}
		
		
		return "review/l-review";
	}
}
