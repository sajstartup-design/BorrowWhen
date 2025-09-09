package project.borrowhen.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/inventory")
public class InventoryController {
	
	@GetMapping()
	public String showInventoryScreen() {
		
		return "inventory/inventory-view";
	}
	
	@GetMapping("/create")
	public String showInventoryCreateScreen() {
		
		return "inventory/inventory-create";
	}
}
