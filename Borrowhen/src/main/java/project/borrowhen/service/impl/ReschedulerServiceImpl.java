package project.borrowhen.service.impl;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import project.borrowhen.common.constant.CommonConstant;
import project.borrowhen.common.util.DateFormatUtil;
import project.borrowhen.dao.BorrowRequestDao;
import project.borrowhen.dao.entity.BorrowRequestEntity;
import project.borrowhen.dao.entity.NotificationEntity;
import project.borrowhen.dao.entity.UserEntity;
import project.borrowhen.service.NotificationService;
import project.borrowhen.service.ReschedulerService;
import project.borrowhen.service.UserService;

@Service
public class ReschedulerServiceImpl implements ReschedulerService{

	 @Autowired
    private BorrowRequestDao borrowRequestDao;
	 
	 @Autowired
	 private NotificationService notificationService;
	 
	    @Autowired
	    private SimpMessagingTemplate messagingTemplate;
	    
	    @Autowired
	    private UserService userService;

	    public void checkOverdueRequests() {
	        Date today = Date.valueOf(LocalDate.now());
	        List<BorrowRequestEntity> overdueRequests =
	                borrowRequestDao.findOngoingAndOverdue(today);

	        overdueRequests.forEach(request -> {
	            // log info
	            System.out.println("⚠️ Overdue: Request ID " + request.getId() +
	                    " | Item: " + request.getItemName() +
	                    " | Due on: " + request.getDateToReturn());

	            UserEntity borrower = userService.getUser(request.getUserId()); 
	            Timestamp dateNow = DateFormatUtil.getCurrentTimestamp(); 
	            
	    	    borrowRequestDao.updateBorrowRequestStatusById(request.getId(), CommonConstant.REQUEST_OVERDUE);
	    	    borrowRequestDao.updateBorrowRequestReturnedLate(request.getId());

	            // Build notification
	            NotificationEntity notification = new NotificationEntity();
	            notification.setUserId(request.getUserId());

	            String message = String.format(
	                "Reminder: Your borrowed item '%s' was due on %s. Please return it as soon as possible.",
	                request.getItemName(),
	                request.getDateToReturn()
	            );

	            notification.setMessage(message);
	            notification.setIsRead(false);
	            notification.setType(CommonConstant.REQUEST_OVERDUE); // optional: create a new constant
	            notification.setCreatedDate(dateNow);
	            notification.setUpdatedDate(dateNow);
	            notification.setIsDeleted(false);

	            // Save to DB
	            notificationService.saveNotification(notification);

	            // Send via WebSocket
	            messagingTemplate.convertAndSendToUser(
	                borrower.getUserId().toString(),
	                "/queue/borrower/notifications",
	                message
	            );
	        });
	    }
	    
	    /**
	     * Void requests that were never picked up (borrow date already passed).
	     */
	    @Override
	    public void voidUnpickedRequests() {
	        Date today = Date.valueOf(LocalDate.now());
	        List<BorrowRequestEntity> voidableRequests = borrowRequestDao.findVoidableRequests(today);

	        voidableRequests.forEach(request -> {
	            System.out.println("🕒 Voiding unpicked request: ID " + request.getId() +
	                    " | Item: " + request.getItemName() +
	                    " | Borrow date: " + request.getDateToBorrow());

	            UserEntity borrower = userService.getUser(request.getUserId());
	            Timestamp now = DateFormatUtil.getCurrentTimestamp();

	            // Update status to VOID
	            borrowRequestDao.updateBorrowRequestStatusById(request.getId(), CommonConstant.REQUEST_VOID);

	            // Build notification
	            NotificationEntity notification = new NotificationEntity();
	            notification.setUserId(request.getUserId());
	            notification.setMessage(String.format(
	                "Your borrow request for '%s' has been voided since the borrow date (%s) has already passed.",
	                request.getItemName(), request.getDateToBorrow()
	            ));
	            notification.setType(CommonConstant.REQUEST_VOID);
	            notification.setIsRead(false);
	            notification.setIsDeleted(false);
	            notification.setCreatedDate(now);
	            notification.setUpdatedDate(now);

	            // Save and send
	            notificationService.saveNotification(notification);
	            messagingTemplate.convertAndSendToUser(
	                borrower.getUserId().toString(),
	                "/queue/borrower/notifications",
	                notification.getMessage()
	            );
	        });
	    }

	    @Override
	    public void notifyRequestsDueIn2Days() {
	        // Calculate the target date (2 days from today)
	        Date targetDate = Date.valueOf(LocalDate.now().plusDays(2));

	        // Fetch all borrow requests whose dateToReturn is exactly 2 days from now
	        List<BorrowRequestEntity> requests = borrowRequestDao.getBorrowRequestDueIn2Days(targetDate);

	        // Current timestamp
	        Timestamp now = DateFormatUtil.getCurrentTimestamp();

	        // Loop through each request and send notifications
	        requests.forEach(request -> {
	            System.out.println("⏰ Reminder: Request ID " + request.getId() +
	                    " | Item: " + request.getItemName() +
	                    " | Due on: " + request.getDateToReturn());

	            UserEntity borrower = userService.getUser(request.getUserId());

	            // Build notification message
	            String message = String.format(
	                    "Reminder: Your borrowed item '%s' is due on %s. Please return it on time.",
	                    request.getItemName(),
	                    request.getDateToReturn()
	            );

	            // Create notification entity
	            NotificationEntity notification = new NotificationEntity();
	            notification.setUserId(request.getUserId());
	            notification.setMessage(message);
	            notification.setType(CommonConstant.REQUEST_DUE_SOON);
	            notification.setIsRead(false);
	            notification.setIsDeleted(false);
	            notification.setCreatedDate(now);
	            notification.setUpdatedDate(now);

	            // Save to database
	            notificationService.saveNotification(notification);

	            // Send via WebSocket
	            messagingTemplate.convertAndSendToUser(
	                    borrower.getUserId().toString(),
	                    "/queue/borrower/notifications",
	                    message
	            );
	        });
	    }



}
