package project.borrowhen.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import project.borrowhen.common.MessageConstant;
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
	public String postUserCreateScreen(@ModelAttribute UserDto userWebDto, RedirectAttributes ra) {
		
		try {
			
			userService.saveUser(userWebDto);
			
			ra.addFlashAttribute("successMsg", MessageConstant.USER_CREATE_MSG);
			
		} catch(Exception e) {
			
			e.printStackTrace();
			
			ra.addFlashAttribute("errorMsg", "Something went wrong!");
		}

		return "redirect:/user";
	}
	
}
