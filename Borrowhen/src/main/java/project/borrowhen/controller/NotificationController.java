package project.borrowhen.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class NotificationController {

	@GetMapping("/notifications")
	public String showNotificationsBorrower() {
		return "notification/notifications";
	}
	
	@GetMapping("/lender/notifications")
	public String showNotificationsLender() {
		return "notification/l-notifications";
	}
}
