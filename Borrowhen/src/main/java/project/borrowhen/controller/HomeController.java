package project.borrowhen.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

	@GetMapping("")
	public String showHome() {
		
		return "home";
	}
	
	@GetMapping("/about")
	public String showAbout(){
		
		return "home/about";
	}
}
