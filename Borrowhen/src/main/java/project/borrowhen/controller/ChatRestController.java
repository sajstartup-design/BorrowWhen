package project.borrowhen.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import project.borrowhen.dto.ChatDto;
import project.borrowhen.service.ChatService;

@RestController
@RequestMapping("/chat")
public class ChatRestController {
	
	@Autowired
	private ChatService chatService;

	@GetMapping("/users")
	public ChatDto searchUser(@RequestParam(required = false) String search) {
		
		try {
			
			ChatDto inDto = new ChatDto();
			
			inDto.setSearch(search);
			
			ChatDto outDto = chatService.searchUsers(inDto);
			
			return outDto;
			
		}catch(Exception e) {
			
			e.printStackTrace();
			
			return new ChatDto();
		}
	}
	
	@PostMapping("/new-conversation")
	public ChatDto saveConversation(@RequestParam("encryptedUserId") String encryptedUserId) {
		
		try {
			
			ChatDto inDto = new ChatDto();
			
			inDto.setEncryptedUserId(encryptedUserId);
			
			ChatDto outDto = chatService.saveConversation(inDto);
			
			return outDto;
			
		}catch(Exception e) {
			e.printStackTrace();
			
			return new ChatDto();
		}
	}
	
	
	
	
	
	@GetMapping("/chats")
	public ChatDto getChats() {
		
		try {
			
			ChatDto outDto = chatService.getAllChats();
			
			return outDto;
			
		}catch(Exception e) {
			
			e.printStackTrace();
			
			return new ChatDto();
		}
	}
	
	@PostMapping("/send-message")
	public void sendMessage(@RequestParam("message") String message,
			@RequestParam("encryptedUserId") String encryptedUserId) {
		
		try {
			ChatDto inDto = new ChatDto();
			
			inDto.setEncryptedUserId(encryptedUserId);
			inDto.setMessage(message);
			
			chatService.sendMessage(inDto);
		}catch(Exception e) {
			e.printStackTrace();
		}

	}
}
