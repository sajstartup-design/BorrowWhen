package project.borrowhen.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class DashboardController {

	@GetMapping("/dashboard")
	public String showBorrowerDashboard(Model model) {
		
		return "dashboard/dashboard";
	}
	
	@GetMapping("/lender/dashboard")
	public String showLenderDashboard(Model model) {
		
		return "dashboard/lender-dashboard";
	}
	
	@GetMapping("/admin/dashboard")
	public String showAdminDashboard(Model model) {
		
		return "dashboard/admin-dashboard";
	}
}
