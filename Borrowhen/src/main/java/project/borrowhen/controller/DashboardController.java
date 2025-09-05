package project.borrowhen.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {

	@GetMapping("/dashboard")
    public String showDashboard(Model model) {
        
		model.addAttribute("content", "content/dashboard");
		return "base";
    }
}
