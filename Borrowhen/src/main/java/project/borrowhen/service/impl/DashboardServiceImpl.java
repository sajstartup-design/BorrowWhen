package project.borrowhen.service.impl;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import project.borrowhen.common.util.TimeAgoUtil;
import project.borrowhen.dao.BorrowRequestDao;
import project.borrowhen.dao.entity.BorrowRequestEntity;
import project.borrowhen.dao.entity.UserEntity;
import project.borrowhen.dto.DashboardDto;
import project.borrowhen.object.BorrowRequestObj;
import project.borrowhen.object.OverdueBorrowRequestObj;
import project.borrowhen.service.DashboardService;
import project.borrowhen.service.UserService;

@Service
public class DashboardServiceImpl implements DashboardService{
	
	@Autowired
	private BorrowRequestDao borrowRequestDao;
	
	@Autowired
	private UserService userService;

	@Override
	public DashboardDto getBorrowerDashboardDetails() {
		
		DashboardDto outDto = new DashboardDto();
		
		UserEntity user = userService.getLoggedInUser();
		
		List<BorrowRequestEntity> allOverdue = borrowRequestDao.getOverdueRequestForBorrower(user.getId());
		
		for(BorrowRequestEntity overdue : allOverdue) {
			
			BorrowRequestObj objOne = new BorrowRequestObj();
			
			objOne.setItemName(overdue.getItemName());
			Timestamp ts = overdue.getDateToReturn() != null 
				    ? new Timestamp(overdue.getDateToReturn().getTime()) 
				    : null;

				objOne.setTimeAgo(TimeAgoUtil.toTimeAgo(ts));
				
				outDto.getOverdues().add(objOne);
		}
		
		List<BorrowRequestEntity> allPaymentPending = borrowRequestDao.getPaymentPendingRequestForBorrower(user.getId());
		
		for(BorrowRequestEntity paymentPending : allPaymentPending) {
			
			BorrowRequestObj objOne = new BorrowRequestObj(); 
			
			objOne.setItemName(paymentPending.getItemName());

			objOne.setAmount(paymentPending.getPrice() * paymentPending.getQty());
			
			outDto.getPaymentPendings().add(objOne);		
		}
		
		return outDto;
	}

}
