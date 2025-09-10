package project.borrowhen.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/inventory")
@Controller
public class InventoryController {

	@GetMapping()
	public String showInventory() {
		
		return "inventory/inventory";
	}
}
