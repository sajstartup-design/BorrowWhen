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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.validation.Valid;
import project.borrowhen.common.constant.MessageConstant;
import project.borrowhen.dto.UserDto;
import project.borrowhen.service.UserService;

@Controller
public class A_UserController {
	
	@Autowired
	private UserService userService;

	@GetMapping("/admin/borrowers")
	public String showBorrowerScreen() {

		return "user/user-borrower-view";
	}
	
	@GetMapping("/admin/lenders")
	public String showLenderScreen() {

		return "user/user-lender-view";
	}
	
	@GetMapping("/create")
	public String showUserCreateScreen(@ModelAttribute UserDto userWebDto) {
		
		return "user/user-create";
	}
	
	@GetMapping("/admin/lenders/create")
	public String showLendersCreateScreen(@ModelAttribute UserDto userWebDto) {
		
		return "user/user-lender-create";
	}
	
	@PostMapping("/admin/lenders/create")
	public String postUserCreateScreen(Model model,
			@ModelAttribute @Valid UserDto userWebDto,
			BindingResult result,
			RedirectAttributes ra
			) {
		
		System.out.println(userWebDto);
		
		if(result.hasErrors()) {
			
			Map<String, String> fieldErrors = result.getFieldErrors()
	                .stream()
	                .collect(Collectors.toMap(
	                        FieldError::getField, 
	                        DefaultMessageSourceResolvable::getDefaultMessage,
	                        (existing, replacement) -> existing 
	                ));

	        ra.addFlashAttribute("fieldErrors", fieldErrors);
	        
	        ra.addFlashAttribute("userDto", userWebDto);
	        
	        return "redirect:/admin/lenders/create";
		}
		
		try {
			
			userService.saveUser(userWebDto);
			
			ra.addFlashAttribute("isSuccess", true);
			ra.addFlashAttribute("successMsg", MessageConstant.USER_CREATE_MSG);
			
		} catch(Exception e) {
			
			e.printStackTrace();
			
			ra.addFlashAttribute("isError", true);
			ra.addFlashAttribute("errorMsg", MessageConstant.SOMETHING_WENT_WRONG);
		}

		return "redirect:/admin/lenders";
	}
	
	@GetMapping("/edit")
	public String showUserEditScreen(Model model,
			@RequestParam("encryptedId") String encryptedId,
			RedirectAttributes ra) {
		
		try {
			
			UserDto inDto = new UserDto();
			
			inDto.setEncryptedId(encryptedId);
			
			UserDto outDto = userService.getUser(inDto);
			
			outDto.setEncryptedId(encryptedId);
			
			model.addAttribute("userDto", outDto);
			
		} catch (Exception e) {
			
			e.printStackTrace();
			
			ra.addFlashAttribute("errorMsg", "Something went wrong!");
			
			return "redirect:/admin/user";
		}
		
		return "user/user-edit";
	}
	
	@PostMapping("/edit")
	public String postUserEditScreen(Model model,
			@ModelAttribute @Valid UserDto userWebDto, 
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
	        
	        ra.addFlashAttribute("userDto", userWebDto);
	        
	        return "redirect:/admin/user/edit?encryptedId=" + userWebDto.getEncryptedId();
	        
		}
		
		try {
			
			userService.editUser(userWebDto);
			
			ra.addFlashAttribute("successMsg", MessageConstant.USER_EDIT_MSG);
			
		} catch(Exception e) {
			
			e.printStackTrace();
			
			ra.addFlashAttribute("errorMsg", "Something went wrong!");
		}

		return "redirect:/admin/user";
	}
	
	@GetMapping("/admin/lenders/details")
	public String showUserDetailsScreen(Model model,
			@RequestParam("encryptedId") String encryptedId,
			RedirectAttributes ra) {
		
		try {
			
			UserDto inDto = new UserDto();
			
			inDto.setEncryptedId(encryptedId);
			
			UserDto outDto = userService.getLenderDetails(inDto);
			
			outDto.setEncryptedId(encryptedId);
			
			model.addAttribute("userDto", outDto);
			
		} catch (Exception e) {
			
			e.printStackTrace();
			
			ra.addFlashAttribute("isError", true);
			ra.addFlashAttribute("errorMsg", MessageConstant.SOMETHING_WENT_WRONG);
			
			return "redirect:/admin/lenders";
		}
		
		return "user/user-lender-details";
	}
	
}
