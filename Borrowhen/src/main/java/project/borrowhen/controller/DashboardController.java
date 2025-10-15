package project.borrowhen.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import project.borrowhen.dto.DashboardDto;
import project.borrowhen.service.DashboardService;


@Controller
public class DashboardController {
	
	@Autowired
	private DashboardService dashboardService;

	@GetMapping("/dashboard")
	public String showBorrowerDashboard(Model model) {
		
		try {
			DashboardDto outDto = dashboardService.getBorrowerDashboardDetails();
			
			model.addAttribute("dashboardDto", outDto);
		}catch(Exception e) {
			e.printStackTrace();
			
			
		}
		
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
	
	@ResponseBody
	@GetMapping("/dashboard/retrieve")
	public DashboardDto getBorrowerDashboardDetails() {
		
		try {
			DashboardDto outDto = dashboardService.getBorrowerDashboardDetails();
			
			return outDto;
		}catch(Exception e) {
			e.printStackTrace();
			
			return new DashboardDto();
		}
	}
}
