package project.borrowhen.controller.admin;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import project.borrowhen.common.constant.CommonConstant;
import project.borrowhen.common.constant.MessageConstant;
import project.borrowhen.dto.BorrowRequestDto;
import project.borrowhen.service.BorrowRequestService;

@Controller
@RequestMapping("/admin/request")
public class A_RequestController {
	
	@Autowired
	private BorrowRequestService borrowRequestService;

	@GetMapping()
	public String showRequestViewScreen(Model model,
			RedirectAttributes ra) {
		
		try {
			
			BorrowRequestDto outDto = borrowRequestService.getAdminBorrowRequestOverview();
			
			model.addAttribute("borrowRequestDto", outDto);
			
		}catch(Exception e) {
			e.printStackTrace();
			ra.addFlashAttribute("isError", true);
	        ra.addFlashAttribute("errorMsg", MessageConstant.SOMETHING_WENT_WRONG);
	        
	        return "redirect:/admin/dashboard";
		}
		
		return "request/admin/request";
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
		
		return "redirect:/admin/request";
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
		
		return "redirect:/admin/request";
	}
	
	@PostMapping("/cancel")
	public String postCancelRequestScreen(@ModelAttribute BorrowRequestDto borrowRequestWebDto,
			RedirectAttributes ra) {
		
		try {
			
			borrowRequestService.cancelBorrowRequest(borrowRequestWebDto);
			
			ra.addFlashAttribute("isSuccess", true);
			ra.addFlashAttribute("successMsg", MessageConstant.REQUEST_CANCELLED_MSG);
			
		}catch(Exception e) {
			
			e.printStackTrace();
			
			ra.addFlashAttribute("isError", true);
			ra.addFlashAttribute("errorMsg", MessageConstant.SOMETHING_WENT_WRONG);
		}
		
		return "redirect:/admin/request";
	}
	
	@PostMapping("/item-received")
	public String postItemReceivedScreen(@ModelAttribute BorrowRequestDto borrowRequestWebDto,
			RedirectAttributes ra) {
		
		try {
			
			borrowRequestService.itemReceivedBorrowRequest(borrowRequestWebDto);
	
			ra.addFlashAttribute("isSuccess", true);
			ra.addFlashAttribute("successMsg", MessageConstant.REQUEST_ITEM_RECEIVED_MSG);
			
		}catch(Exception e) {
			
			e.printStackTrace();
			
			ra.addFlashAttribute("isError", true);
			ra.addFlashAttribute("errorMsg", MessageConstant.SOMETHING_WENT_WRONG);
		}
		
		return "redirect:/admin/request";
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
		
		return "redirect:/admin/request";
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
		
		return "redirect:/admin/request";
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
		
		return "redirect:/admin/request";
	}
	
	@GetMapping("/details")
	public String showRequestDetailsScreen(Model model,
			@RequestParam("encryptedId") String encryptedId,
			RedirectAttributes ra) {
		
		try {
			
			BorrowRequestDto inDto = new BorrowRequestDto();
			
			inDto.setEncryptedId(encryptedId);
			
			BorrowRequestDto outDto = borrowRequestService.getBorrowRequestDetailsForLender(inDto);
			
			List<String> steps = List.of(
			    CommonConstant.PENDING,
			    CommonConstant.APPROVED,
			    CommonConstant.REJECTED,
			    CommonConstant.PICK_UP_READY,
			    CommonConstant.ITEM_RECEIVED,
			    CommonConstant.ON_GOING,
			    CommonConstant.COMPLETED,
			    CommonConstant.PAYMENT_PENDING,
			    CommonConstant.PAID
			);

			int currentStepIndex = steps.indexOf(outDto.getRequest().getStatus());

			model.addAttribute("steps", steps);
			model.addAttribute("currentStepIndex", currentStepIndex);
			
			model.addAttribute("borrowRequestDto", outDto);
			
		} catch(Exception e) {
			
			e.printStackTrace();
			
			ra.addFlashAttribute("isError", true);
			ra.addFlashAttribute("errorMsg", MessageConstant.SOMETHING_WENT_WRONG);
			
			return "redirect:/lender/request";
		}
		
		return "/request/admin/request-details";
	}
}
