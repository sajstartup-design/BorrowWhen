package project.borrowhen.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import project.borrowhen.dto.InventoryDto;
import project.borrowhen.service.BorrowRequestService;
import project.borrowhen.common.constant.MessageConstant;
import project.borrowhen.dto.BorrowRequestDto;

@RequestMapping("/inventory")
@Controller
public class InventoryController {
	
	@Autowired
	private BorrowRequestService borrowRequestService;

	@GetMapping()
	public String showInventory() {
		
		return "inventory/inventory";
	}
	
	@PostMapping("/request")
	public String postRequest(@ModelAttribute BorrowRequestDto itemRequestWebDto,
			RedirectAttributes ra) {

		try {
			
			borrowRequestService.saveBorrowRequest(itemRequestWebDto);
			
			ra.addFlashAttribute("successMsg", MessageConstant.REQUEST_CREATED_MSG);
			
		}catch(Exception e) {
			
			e.printStackTrace();
			
			ra.addFlashAttribute("errorMsg", MessageConstant.SOMETHING_WENT_WRONG);
		}
		
		return "redirect:/inventory";
	}
}
