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
	
	private String senderFirstName;
	
	private String senderFamilyName;
	
	private int receiverId;
	
	private String receiverFirstName;
	
	private String receiverFamilyName;
	
	private int lastSenderId;
	
	private String lastMessage;
	
	private Timestamp lastMessageDate;
}
