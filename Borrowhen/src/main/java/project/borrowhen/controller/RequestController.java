package project.borrowhen.controller;

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
import project.borrowhen.dto.InventoryDto;
import project.borrowhen.service.BorrowRequestService;

@Controller
@RequestMapping("/request")
public class RequestController {
	
	@Autowired
	private BorrowRequestService borrowRequestService;

	@GetMapping()
	public String showRequestViewScreen() {
		
		return "request/request";
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
		
		return "redirect:/request";
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
		
		return "redirect:/request";
	}
	
	@PostMapping("/paid")
	public String postPaidScreen(@ModelAttribute BorrowRequestDto borrowRequestWebDto,
			RedirectAttributes ra) {
		
		try {
			
			borrowRequestService.paidBorrowRequest(borrowRequestWebDto);
	
			ra.addFlashAttribute("isSuccess", true);
			ra.addFlashAttribute("successMsg", MessageConstant.REQUEST_ITEM_RECEIVED_MSG);
			
		}catch(Exception e) {
			
			e.printStackTrace();
			
			ra.addFlashAttribute("isError", true);
			ra.addFlashAttribute("errorMsg", MessageConstant.SOMETHING_WENT_WRONG);
		}
		
		return "redirect:/request";
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
		
		return "/request/request-details";
	}
}
