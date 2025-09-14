package project.borrowhen.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ChatController {
	
	@GetMapping("/chat")
	public String showChatScreen() {
		
		return "chat";
	}
}
