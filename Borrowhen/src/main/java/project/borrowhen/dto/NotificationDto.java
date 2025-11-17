package project.borrowhen.dto;

import java.util.List;

import lombok.Data;
import project.borrowhen.object.FilterAndSearchObj;
import project.borrowhen.object.NotificationObj;
import project.borrowhen.object.PaginationObj;

@Data
public class NotificationDto {

	private List<NotificationObj> notifications;
	
	private PaginationObj pagination;
	
	private FilterAndSearchObj filter;
	
	private int notificationCount;
}
