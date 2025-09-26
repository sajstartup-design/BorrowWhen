package project.borrowhen.service;

import org.springframework.stereotype.Service;

import project.borrowhen.dto.BorrowRequestDto;

@Service
public interface BorrowRequestService {

	public void saveBorrowRequest(BorrowRequestDto inDto) throws Exception;
	
	public BorrowRequestDto getAllBorrowRequest(BorrowRequestDto inDto) throws Exception;
	
	public BorrowRequestDto getAllOwnedBorrowRequestForLender(BorrowRequestDto inDto) throws Exception;
	
	public BorrowRequestDto getAllOwnedBorrowRequestForBorrower(BorrowRequestDto inDto) throws Exception;
	
	public BorrowRequestDto getBorrowRequestDetailsForLender(BorrowRequestDto inDto) throws Exception;
	
	public BorrowRequestDto getBorrowRequestDetailsForBorrower(BorrowRequestDto inDto) throws Exception;
	
	public void approveBorrowRequest(BorrowRequestDto inDto) throws Exception;
	
	public void cancelBorrowRequest(BorrowRequestDto inDto) throws Exception;
	
	public void rejectBorrowRequest(BorrowRequestDto inDto) throws Exception;
	
	public void itemReceivedBorrowRequest(BorrowRequestDto inDto) throws Exception;
	
	public void itemReturnedBorrowRequest(BorrowRequestDto inDto) throws Exception;
	
	public void itemPickUpBorrowRequest(BorrowRequestDto inDto) throws Exception;
	
	public void issuePaymentBorrowRequest(BorrowRequestDto inDto) throws Exception;
	
	public void paidBorrowRequest(BorrowRequestDto inDto) throws Exception;
	
	public BorrowRequestDto getBorrowRequest(BorrowRequestDto inDto) throws Exception;
}
