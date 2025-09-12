package project.borrowhen.service;

import org.springframework.stereotype.Service;

import project.borrowhen.dto.BorrowRequestDto;

@Service
public interface BorrowRequestService {

	public void saveBorrowRequest(BorrowRequestDto inDto) throws Exception;
	
	public BorrowRequestDto getAllBorrowRequest(BorrowRequestDto inDto) throws Exception;
	
	public BorrowRequestDto getAllOwnedBorrowRequestForLender(BorrowRequestDto inDto) throws Exception;
	
	public void approveBorrowRequest(BorrowRequestDto inDto) throws Exception;
	
	public void rejectBorrowRequest(BorrowRequestDto inDto) throws Exception;
}
