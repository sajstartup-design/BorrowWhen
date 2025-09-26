package project.borrowhen.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import project.borrowhen.common.constant.MessageConstant;
import project.borrowhen.dto.BorrowRequestDto;
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
	
			ra.addFlashAttribute("successMsg", MessageConstant.REQUEST_CANCELLED_MSG);
			
		}catch(Exception e) {
			
			e.printStackTrace();
			
			ra.addFlashAttribute("errorMsg", MessageConstant.SOMETHING_WENT_WRONG);
		}
		
		return "redirect:/request";
	}
	
	@PostMapping("/item-received")
	public String postItemReceivedScreen(@ModelAttribute BorrowRequestDto borrowRequestWebDto,
			RedirectAttributes ra) {
		
		try {
			
			borrowRequestService.itemReceivedBorrowRequest(borrowRequestWebDto);
	
			ra.addFlashAttribute("successMsg", MessageConstant.REQUEST_ITEM_RECEIVED_MSG);
			
		}catch(Exception e) {
			
			e.printStackTrace();
			
			ra.addFlashAttribute("errorMsg", MessageConstant.SOMETHING_WENT_WRONG);
		}
		
		return "redirect:/request";
	}
	
	@PostMapping("/paid")
	public String postPaidScreen(@ModelAttribute BorrowRequestDto borrowRequestWebDto,
			RedirectAttributes ra) {
		
		try {
			
			borrowRequestService.paidBorrowRequest(borrowRequestWebDto);
	
			ra.addFlashAttribute("successMsg", MessageConstant.REQUEST_ITEM_RECEIVED_MSG);
			
		}catch(Exception e) {
			
			e.printStackTrace();
			
			ra.addFlashAttribute("errorMsg", MessageConstant.SOMETHING_WENT_WRONG);
		}
		
		return "redirect:/request";
	}
}
