package project.borrowhen.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class GuidelinesController {

	@GetMapping("/guidelines/rules")
	public String showRulesScreen() {
		
		return "guidelines/rules";
	}
}
