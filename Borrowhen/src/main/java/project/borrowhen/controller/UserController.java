package project.borrowhen.controller;

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
import project.borrowhen.dto.UserDto;
import project.borrowhen.service.UserService;

@Controller
@RequestMapping("/user")
public class UserController {
	
	@Autowired
	private UserService userService;

	@GetMapping()
	public String showUserScreen() {

		return "user/user-view";
	}
	
	@GetMapping("/create")
	public String showUserCreateScreen(@ModelAttribute UserDto userWebDto) {
		
		return "user/user-create";
	}
	
	@PostMapping("/create")
	public String postUserCreateScreen(Model model,
			@ModelAttribute @Valid UserDto userWebDto,
			BindingResult result,
			RedirectAttributes ra
			) {
		
		if(result.hasErrors()) {
			
			Map<String, String> fieldErrors = result.getFieldErrors()
	                .stream()
	                .collect(Collectors.toMap(
	                        FieldError::getField, 
	                        DefaultMessageSourceResolvable::getDefaultMessage,
	                        (existing, replacement) -> existing 
	                ));

	        ra.addFlashAttribute("fieldErrors", fieldErrors);
	        
	        ra.addFlashAttribute("useDto", userWebDto);
	        
	        return "redirect:/user/create";
		}
		
		try {
			
			userService.saveUser(userWebDto);
			
			ra.addFlashAttribute("successMsg", MessageConstant.USER_CREATE_MSG);
			
		} catch(Exception e) {
			
			e.printStackTrace();
			
			ra.addFlashAttribute("errorMsg", "Something went wrong!");
		}

		return "redirect:/user";
	}
	
	@GetMapping("/edit")
	public String showUserEditScreen(Model model,
			@RequestParam("encryptedId") String encryptedId,
			RedirectAttributes ra) {
		
		try {
			
			System.out.println(encryptedId);
			
			UserDto inDto = new UserDto();
			
			inDto.setEncryptedId(encryptedId);
			
			UserDto outDto = userService.getUser(inDto);
			
			outDto.setEncryptedId(encryptedId);
			
			model.addAttribute("userDto", outDto);
			
		} catch (Exception e) {
			
			e.printStackTrace();
			
			ra.addFlashAttribute("errorMsg", "Something went wrong!");
			
			return "redirect:/user";
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
	        
	        ra.addFlashAttribute("useDto", userWebDto);
	        
	        return "redirect:/user/edit?encryptedId=" + userWebDto.getEncryptedId();
	        
		}
		
		try {
			
			
			
			ra.addFlashAttribute("successMsg", MessageConstant.USER_EDIT_MSG);
			
		} catch(Exception e) {
			
			e.printStackTrace();
			
			ra.addFlashAttribute("errorMsg", "Something went wrong!");
		}

		return "redirect:/user";
	}
	
	@GetMapping("/details")
	public String showUserDetailsScreen(Model model,
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
			
			return "redirect:/user";
		}
		
		return "user/user-details";
	}
	
}
