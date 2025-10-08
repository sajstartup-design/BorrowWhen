package project.borrowhen.object;

import java.sql.Timestamp;

import lombok.Data;

@Data
public class ChatObj {

	private String encryptedConversationId;
	
	private String fullName;
	
	private String lastMessage;
	
	private Boolean isLastMessage;	
	
	private Timestamp lastMessageDate;
	
	private Boolean isNew;
}
