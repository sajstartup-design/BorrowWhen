package project.borrowhen.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import project.borrowhen.dto.NotificationDto;
import project.borrowhen.object.FilterAndSearchObj;
import project.borrowhen.object.PaginationObj;
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
    
    @GetMapping("/borrower")
    public NotificationDto getNotificationsForBorrower(@RequestParam(defaultValue = "0") int page,
    		@RequestParam(required = false) String startDate,
    		@RequestParam(required=false) String endDate,
    		@RequestParam(required=false) String status) {
        try {
        	NotificationDto inDto = new NotificationDto();
        	
        	System.out.println(startDate);
        	System.out.println(endDate);
        	
        	
            PaginationObj pagination = new PaginationObj();
            pagination.setPage(page);

            FilterAndSearchObj filter = new FilterAndSearchObj();
            filter.setStartDate(startDate);
            filter.setEndDate(endDate);
            filter.setStatus(status);

            inDto.setPagination(pagination);
            inDto.setFilter(filter);

            return notificationService.getAllNotificationsByUser(inDto);
            
        } catch (Exception e) {
            e.printStackTrace();

            return new NotificationDto();
        }
    }
    
    @GetMapping("/read")
    public void readNotification(@RequestParam(required=false) String encryptedId) {
    	
    	try {
    		
    		System.out.println("YAWA");
    		
    		notificationService.readNotification(encryptedId);
    	}catch(Exception e) {
    		
    		e.printStackTrace();
    		
    		
    	}
    }
}
