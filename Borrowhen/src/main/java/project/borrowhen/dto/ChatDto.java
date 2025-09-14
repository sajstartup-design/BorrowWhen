package project.borrowhen.dto;

import java.util.List;

import lombok.Data;
import project.borrowhen.object.ChatObj;
import project.borrowhen.object.UserObj;

@Data
public class ChatDto {

	private String search;
	
	private String encryptedConversationId;
	
	private String encryptedUserId;

	private String message;
	
	private List<ChatObj> chats;
	
	private ChatObj chat;
	
	private List<UserObj> users;
	
	private Boolean isNew;
}
