package project.borrowhen.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import project.borrowhen.service.ReschedulerService;

@Component
@EnableScheduling
public class ReschedulerTask {

    @Autowired
    private ReschedulerService reschedulerService;

    // Run every day at 8 AM
//    @Scheduled(cron = "0 0 8 * * ?")
    @Scheduled(cron = "0 */3 * * * ?")
    public void runRescheduler() {
    	System.out.println("YAWA");
        reschedulerService.checkOverdueRequests();
        reschedulerService.voidUnpickedRequests();
    }
}
