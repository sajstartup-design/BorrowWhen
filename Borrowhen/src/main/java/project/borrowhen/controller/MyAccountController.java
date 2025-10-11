package project.borrowhen.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import project.borrowhen.common.constant.CommonConstant;
import project.borrowhen.common.constant.MessageConstant;
import project.borrowhen.dto.UserDto;
import project.borrowhen.service.UserService;

@Controller
public class MyAccountController {
	
	@Autowired
	private UserService userService;

	@GetMapping("/my-account")
	public String showMyAccount(Model model,
			RedirectAttributes ra) {
		
		try {
			
			UserDto inDto = new UserDto();
			
			inDto.setEncryptedId(CommonConstant.ROLE_BORROWER);
			
			UserDto outDto = userService.getUser(inDto);
			
			model.addAttribute("userDto", outDto);
			
		} catch (Exception e) {
			
			e.printStackTrace();
			
			ra.addFlashAttribute("isError", true);
			ra.addFlashAttribute("errorMsg", MessageConstant.SOMETHING_WENT_WRONG);
			
			return "redirect:/my-account";
		}

		return "user/borrower-profile";
	}
}
