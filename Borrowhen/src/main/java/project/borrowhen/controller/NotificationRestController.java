package project.borrowhen.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import project.borrowhen.dto.NotificationDto;
import project.borrowhen.service.NotificationService;

@RestController
@RequestMapping("/api/notifications")
public class NotificationRestController {

	@Autowired
    private NotificationService notificationService;

    @GetMapping()
    public NotificationDto getNotifications() {
    	
        try {																					

            return notificationService.getNotificationsByUser();
            
        } catch (Exception e) {
            e.printStackTrace();

            return new NotificationDto();
        }
    }
    
    @GetMapping("/count")
    public NotificationDto getNotificationsCount() {
    	
        try {																					

            return notificationService.getNotificationsCountByUser();
            
        } catch (Exception e) {
            e.printStackTrace();

            return new NotificationDto();
        }
    }
}
