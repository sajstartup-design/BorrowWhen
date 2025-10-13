package project.borrowhen.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import project.borrowhen.common.constant.MessageConstant;
import project.borrowhen.dto.BorrowRequestDto;
import project.borrowhen.service.BorrowRequestService;

@Controller
public class FeedbackController {
	
	@Autowired
	private BorrowRequestService borrowRequestService;
	
	@PostMapping("/feedback")
	public String postFeedback(@ModelAttribute BorrowRequestDto borrowRequestWebDto,
			RedirectAttributes ra) {
		
		try {
			
			borrowRequestService.feedbackBorrowRequest(borrowRequestWebDto);
			
			ra.addFlashAttribute("isSuccess", true);
			ra.addFlashAttribute("successMsg", MessageConstant.FEEDBACK_SUCCESS_MSG);
			
		}catch(Exception e) {
			e.printStackTrace();
			
			ra.addFlashAttribute("isError", true);
			ra.addFlashAttribute("errorMsg", MessageConstant.SOMETHING_WENT_WRONG);
		}
		
		return "redirect:/dashboard";
	}
}
