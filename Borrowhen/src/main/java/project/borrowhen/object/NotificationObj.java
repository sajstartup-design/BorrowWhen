package project.borrowhen.object;

import lombok.Data;

@Data
public class NotificationObj {
	
	private String encryptedId;
	
	private String message;
	
	private Boolean isRead;
	
	private String dateAndTime;
	
	private String type;
}
