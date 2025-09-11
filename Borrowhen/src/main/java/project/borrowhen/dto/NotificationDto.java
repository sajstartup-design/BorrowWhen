package project.borrowhen.dto;

import java.util.List;

import lombok.Data;
import project.borrowhen.object.NotificationObj;

@Data
public class NotificationDto {

	private List<NotificationObj> notifications;
	
	private int notificationCount;
}
