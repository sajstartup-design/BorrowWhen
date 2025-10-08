package project.borrowhen.dao.entity;

import java.sql.Timestamp;

import org.springframework.context.annotation.Scope;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Scope("prototype")
public class ConversationData {

	private int conversationId; 
	
	private int senderId;
	
	private String senderFullName;
		
	private int receiverId;
	
	private String receiverFullName;
	
	private int lastSenderId;
	
	private String lastMessage;
	
	private Timestamp lastMessageDate;
}
