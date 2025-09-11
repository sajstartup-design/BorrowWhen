package project.borrowhen.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import project.borrowhen.dto.InventoryDto;
import project.borrowhen.dto.BorrowRequestDto;

@RequestMapping("/inventory")
@Controller
public class InventoryController {

	@GetMapping()
	public String showInventory() {
		
		return "inventory/inventory";
	}
	
	@PostMapping("/inventory/request")
	public String postRequest(@ModelAttribute BorrowRequestDto itemRequestWebDto) {
		
		
		
		return "redirect:/inventory";
	}
}
