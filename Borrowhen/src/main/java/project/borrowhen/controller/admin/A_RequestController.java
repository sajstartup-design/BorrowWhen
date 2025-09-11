package project.borrowhen.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/request")
public class A_RequestController {

	@GetMapping()
	public String showRequestViewScreen() {
		
		return "request/admin/request";
	}
}
