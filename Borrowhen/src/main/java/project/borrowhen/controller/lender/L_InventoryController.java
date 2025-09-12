package project.borrowhen.controller.lender;

import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.validation.Valid;
import project.borrowhen.common.constant.MessageConstant;
import project.borrowhen.dto.InventoryDto;
import project.borrowhen.service.InventoryService;

@Controller
@RequestMapping("/lender/inventory")
public class L_InventoryController {
	
	@Autowired
	private InventoryService inventoryService;
	
	@GetMapping()
	public String showInventoryScreen() {
		
		return "inventory/lender/inventory-view";
	}
	
	@GetMapping("/create")
	public String showInventoryCreateScreen(@ModelAttribute InventoryDto inventoryWebDto) {
		
		return "inventory/lender/inventory-create";
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
	        
	        return "redirect:/lender/inventory/create";
		}
		
		try {
			
			inventoryService.saveInventory(inventoryWebDto);
			
			ra.addFlashAttribute("successMsg", MessageConstant.INVENTORY_CREATE_MSG);
			
		}catch(Exception e) {
			
			e.printStackTrace();
			
			ra.addFlashAttribute("errorMsg", "Something went wrong!");
		}
		
		return "redirect:/lender/inventory";
	}
	
	@GetMapping("/edit")
	public String showInventoryEditScreen(Model model,
			@RequestParam("encryptedId") String encryptedId,
			@ModelAttribute InventoryDto inventoryWebDto,
			RedirectAttributes ra) {
		
		try {
			
			InventoryDto inDto = new InventoryDto();
			
			inDto.setEncryptedId(encryptedId);
			
			InventoryDto outDto = inventoryService.getInventory(inDto);
			
			outDto.setEncryptedId(encryptedId);
			
			model.addAttribute("inventoryDto", outDto);
			
		} catch (Exception e) {
			
			e.printStackTrace();
			
			ra.addFlashAttribute("errorMsg", "Something went wrong!");
			
			return "redirect:/lender/inventory";
		}
		
		return "inventory/lender/inventory-edit";
	}
	
	@PostMapping("/edit")
	public String postInventoryEditScreen(@ModelAttribute @Valid InventoryDto inventoryWebDto,
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
	        
	        return "redirect:/lender/inventory/edit?encryptedId=" + inventoryWebDto.getEncryptedId();
	        
		}
		
		try {
			
			inventoryService.editInventory(inventoryWebDto);
			
			ra.addFlashAttribute("successMsg", MessageConstant.INVENTORY_EDIT_MSG);
			
		}catch(Exception e) {
			
			e.printStackTrace();
			
			ra.addFlashAttribute("errorMsg", "Something went wrong!");
		}
		
		return "redirect:/lender/inventory";
	}
	
	@GetMapping("/details")
	public String showInventoryDetailsScreen(Model model,
			@RequestParam("encryptedId") String encryptedId,
			RedirectAttributes ra) {
		
		try {
			
			InventoryDto inDto = new InventoryDto();
			
			inDto.setEncryptedId(encryptedId);
			
			InventoryDto outDto = inventoryService.getInventory(inDto);
			
			model.addAttribute("inventoryDto", outDto);
			
		} catch(Exception e) {
			
			e.printStackTrace();
			
			ra.addFlashAttribute("errorMsg", "Something went wrong!");
			
			return "redirect:/lender/user";
		}
		
		return "inventory/lender/inventory-details";
	}

}
