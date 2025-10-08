package project.borrowhen.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import project.borrowhen.common.constant.MessageConstant;
import project.borrowhen.dto.UserDto;
import project.borrowhen.service.UserService;

@Controller
public class RegisterController {
	
	@Autowired
	private UserService userService;

	@GetMapping("/register")
	public String showRegisterScreen() {
		
		return "register";
	}
	
	@PostMapping("/register")
	public String postRegisterScreen(@ModelAttribute UserDto userWebDto,
			RedirectAttributes ra) {
		
		try {
			
			userService.registerUser(userWebDto);
			
			ra.addFlashAttribute("isSuccess", true);
			ra.addFlashAttribute("successMsg", MessageConstant.REGISTER_SUCCESS);
			
		} catch(Exception e) {
			
			e.printStackTrace();
			
			ra.addFlashAttribute("isError", true);
			ra.addFlashAttribute("errorMsg", MessageConstant.SOMETHING_WENT_WRONG);
		}

		return "redirect:/login";
	}
}
