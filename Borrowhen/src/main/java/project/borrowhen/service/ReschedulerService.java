package project.borrowhen.service;

import org.springframework.stereotype.Service;

@Service
public interface ReschedulerService {

	public void checkOverdueRequests();
	
	public void voidUnpickedRequests();
}
