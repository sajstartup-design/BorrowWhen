package project.borrowhen.controller.lender;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import project.borrowhen.common.constant.MessageConstant;
import project.borrowhen.dto.BorrowRequestDto;
import project.borrowhen.service.BorrowRequestService;

@Controller
@RequestMapping("/lender/request")
public class L_RequestController {
	
	@Autowired
	private BorrowRequestService borrowRequestService;

	@GetMapping()
	public String showRequestViewScreen() {
		
		return "request/lender/request";
	}
	
	@PostMapping("/approve")
	public String postRequestApproveScreen(@ModelAttribute BorrowRequestDto borrowRequestWebDto,
			RedirectAttributes ra) {
		
		try {
			
			borrowRequestService.approveBorrowRequest(borrowRequestWebDto);
			
			ra.addFlashAttribute("isSuccess", true);
			ra.addFlashAttribute("successMsg", MessageConstant.REQUEST_APPROVED_MSG);
			
		}catch(Exception e) {
			
			e.printStackTrace();
			
			ra.addFlashAttribute("isError", true);
			ra.addFlashAttribute("errorMsg", MessageConstant.SOMETHING_WENT_WRONG);
		}
		
		return "redirect:/lender/request";
	}
	
	@PostMapping("/reject")
	public String postRequestRejectScreen(@ModelAttribute BorrowRequestDto borrowRequestWebDto,
			RedirectAttributes ra) {
		
		try {
			
			ra.addFlashAttribute("isSuccess", true);
			borrowRequestService.rejectBorrowRequest(borrowRequestWebDto);
			
			ra.addFlashAttribute("successMsg", MessageConstant.REQUEST_REJECTED_MSG);
			
		}catch(Exception e) {
			
			e.printStackTrace();
			
			ra.addFlashAttribute("isError", true);
			ra.addFlashAttribute("errorMsg", MessageConstant.SOMETHING_WENT_WRONG);
		}
		
		return "redirect:/lender/request";
	}
	
	@GetMapping("/details")
	public String showRequestDetailsScreen(Model model,
			@RequestParam("encryptedId") String encryptedId,
			RedirectAttributes ra) {
		
		try {
			
			BorrowRequestDto inDto = new BorrowRequestDto();
			
			inDto.setEncryptedId(encryptedId);
			
			BorrowRequestDto outDto = borrowRequestService.getBorrowRequestDetailsForLender(inDto);
			
			model.addAttribute("borrowRequestDto", outDto);
			
		} catch(Exception e) {
			
			e.printStackTrace();
			
			ra.addFlashAttribute("isError", true);
			ra.addFlashAttribute("errorMsg", MessageConstant.SOMETHING_WENT_WRONG);
			
			return "redirect:/lender/request";
		}
		
		return "/request/lender/request-details";
	}
	
	@PostMapping("/pick-up")
	public String postPickUpScreen(@ModelAttribute BorrowRequestDto borrowRequestWebDto,
			RedirectAttributes ra) {
		
		try {
			
			borrowRequestService.itemPickUpBorrowRequest(borrowRequestWebDto);
	
			ra.addFlashAttribute("isSuccess", true);
			ra.addFlashAttribute("successMsg", MessageConstant.REQUEST_ITEM_PICKUP_READY);
			
		}catch(Exception e) {
			
			e.printStackTrace();
			
			ra.addFlashAttribute("isError", true);
			ra.addFlashAttribute("errorMsg", MessageConstant.SOMETHING_WENT_WRONG);
		}
		
		return "redirect:/lender/request";
	}
	
	@PostMapping("/item-returned")
	public String postItemReturnedScreen(@ModelAttribute BorrowRequestDto borrowRequestWebDto,
			RedirectAttributes ra) {
		
		try {
			
			borrowRequestService.itemReturnedBorrowRequest(borrowRequestWebDto);
	
			ra.addFlashAttribute("isSuccess", true);
			ra.addFlashAttribute("successMsg", MessageConstant.REQUEST_ITEM_RECEIVED_MSG);
			
		}catch(Exception e) {
			
			e.printStackTrace();
			
			ra.addFlashAttribute("isError", true);
			ra.addFlashAttribute("errorMsg", MessageConstant.SOMETHING_WENT_WRONG);
		}
		
		return "redirect:/lender/request";
	}
	
	@PostMapping("/issue-payment")
	public String postIssuePaymentScreen(@ModelAttribute BorrowRequestDto borrowRequestWebDto,
			RedirectAttributes ra) {
		
		try {
			
			borrowRequestService.issuePaymentBorrowRequest(borrowRequestWebDto);
	
			ra.addFlashAttribute("isSuccess", true);
			ra.addFlashAttribute("successMsg", MessageConstant.REQUEST_PAYMENT_PENDING);
			
		}catch(Exception e) {
			
			e.printStackTrace();
			
			ra.addFlashAttribute("isError", true);
			ra.addFlashAttribute("errorMsg", MessageConstant.SOMETHING_WENT_WRONG);
		}
		
		return "redirect:/lender/request";
	}
}
