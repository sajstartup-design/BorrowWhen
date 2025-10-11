package project.borrowhen.controller.admin;

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
import project.borrowhen.common.constant.CommonConstant;
import project.borrowhen.common.constant.MessageConstant;
import project.borrowhen.dto.InventoryDto;
import project.borrowhen.object.FilterAndSearchObj;
import project.borrowhen.object.PaginationObj;
import project.borrowhen.service.InventoryService;
import project.borrowhen.service.UserService;

@Controller
@RequestMapping("/admin/inventory")
public class A_InventoryController {
	
	@Autowired
	private InventoryService inventoryService;
	
	@Autowired
	private UserService userService;
	
	@GetMapping()
	public String showInventoryScreen(Model model) throws Exception {
		
		InventoryDto inDto = new InventoryDto();

        PaginationObj pagination = new PaginationObj();
        pagination.setPage(1);
        
        FilterAndSearchObj filter = new FilterAndSearchObj();
        filter.setSearch(CommonConstant.BLANK);

        inDto.setPagination(pagination);
        inDto.setFilter(filter);
        

        InventoryDto outDto = inventoryService.getAllInventory(inDto);

        model.addAttribute("inventoryDto", outDto);
		
		return "inventory/admin/inventory-view";
	}
	
	@GetMapping("/create")
	public String showInventoryCreateScreen(Model model, 
			@ModelAttribute InventoryDto inventoryWebDto) {
		
		model.addAttribute("allUserId", userService.getAllUserId());
		
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
	        
	        return "redirect:/admin/inventory/create";
		}
		
		if(CommonConstant.BLANK.equals(inventoryWebDto.getUserId())) {
			
			ra.addFlashAttribute("ownerError", MessageConstant.OWNER_BLANK);
			
			ra.addFlashAttribute("inventoryDto", inventoryWebDto);
	        
	        return "redirect:/admin/inventory/create";
		}
		
		try {
			
			inventoryService.saveInventory(inventoryWebDto);
			
			ra.addFlashAttribute("isSuccess", true);
			ra.addFlashAttribute("successMsg", MessageConstant.INVENTORY_CREATE_MSG);
			
		}catch(Exception e) {
			
			e.printStackTrace();
			
			ra.addFlashAttribute("isSuccess", false);
			ra.addFlashAttribute("errorMsg", "Something went wrong!");
		}
		
		return "redirect:/admin/inventory";
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
			
			model.addAttribute("allUserId", userService.getAllUserId());
			
		} catch (Exception e) {
			
			e.printStackTrace();
			
			ra.addFlashAttribute("errorMsg", "Something went wrong!");
			
			return "redirect:/admin/inventory";
		}
		
		return "inventory/admin/inventory-edit";
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
	        
	        return "redirect:/admin/inventory/edit?encryptedId=" + inventoryWebDto.getEncryptedId();
	        
		}
		
		try {
			
			inventoryService.editInventory(inventoryWebDto);
			
			ra.addFlashAttribute("successMsg", MessageConstant.INVENTORY_CREATE_MSG);
			
		}catch(Exception e) {
			
			e.printStackTrace();
			
			ra.addFlashAttribute("errorMsg", "Something went wrong!");
		}
		
		return "redirect:/admin/inventory";
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
			
			return "redirect:/admin/user";
		}
		
		return "inventory/admin/inventory-details";
	}
	
}
