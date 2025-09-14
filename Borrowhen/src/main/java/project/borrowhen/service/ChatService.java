package project.borrowhen.service;

import org.springframework.stereotype.Service;

import project.borrowhen.dto.ChatDto;

@Service
public interface ChatService {

	public ChatDto searchUsers(ChatDto inDto) throws Exception;
	
	public ChatDto saveConversation(ChatDto inDto) throws Exception;
	
	public void sendMessage(ChatDto inDto) throws Exception;
	
	public ChatDto getAllChats() throws Exception;
}
