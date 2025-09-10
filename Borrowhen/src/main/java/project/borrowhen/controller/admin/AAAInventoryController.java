package project.borrowhen.controller.admin;

import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.validation.Valid;
import project.borrowhen.common.constant.MessageConstant;
import project.borrowhen.dto.InventoryDto;
import project.borrowhen.service.InventoryService;

@Controller
@RequestMapping("/admin/inventory")
public class AAAInventoryController {
	
	@Autowired
	private InventoryService inventoryService;
	
	@GetMapping()
	public String showInventoryScreen() {
		
		return "inventory/admin/inventory-view";
	}
	
	@GetMapping("/create")
	public String showInventoryCreateScreen(@ModelAttribute InventoryDto inventoryWebDto) {
		
		return "inventory/admin/inventory-create";
	}
	
	@PostMapping("/create")
	public String postInventoryCreateScreen(@ModelAttribute @Valid InventoryDto inventoryWebDto,
			BindingResult result,
			RedirectAttributes ra) {
		
		if(result.hasErrors()) {
			
			Map<String, String> fieldErrors = result.getFieldErrors()
	                .stream()
	                .collect(Collectors.toMap(
	                        FieldError::getField, 
	                        DefaultMessageSourceResolvable::getDefaultMessage,
	                        (existing, replacement) -> existing 
	                ));

	        ra.addFlashAttribute("fieldErrors", fieldErrors);
	        
	        ra.addFlashAttribute("inventoryDto", inventoryWebDto);
	        
	        return "redirect:/inventory/create";
		}
		
		try {
			
			inventoryService.saveInventory(inventoryWebDto);
			
			ra.addFlashAttribute("successMsg", MessageConstant.INVENTORY_CREATE_MSG);
			
		}catch(Exception e) {
			
			e.printStackTrace();
			
			ra.addFlashAttribute("errorMsg", "Something went wrong!");
		}
		
		return "redirect:/admin/inventory";
	}
	
}
