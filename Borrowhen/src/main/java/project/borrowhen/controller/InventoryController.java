package project.borrowhen.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class InventoryController {
	@GetMapping("/inventory")
	public String homeInventory() {
		return "inventory/inventory-view";
	}
}
