package project.borrowhen.service.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import project.borrowhen.common.constant.CommonConstant;
import project.borrowhen.common.util.CipherUtil;
import project.borrowhen.common.util.DateFormatUtil;
import project.borrowhen.common.util.TimeAgoUtil;
import project.borrowhen.dao.BorrowRequestDao;
import project.borrowhen.dao.InventoryDao;
import project.borrowhen.dao.NotificationDao;
import project.borrowhen.dao.UserDao;
import project.borrowhen.dao.entity.AdminDashboardOverview;
import project.borrowhen.dao.entity.BorrowRequestData;
import project.borrowhen.dao.entity.BorrowRequestEntity;
import project.borrowhen.dao.entity.BorrowRequestOverview;
import project.borrowhen.dao.entity.InventoryEntity;
import project.borrowhen.dao.entity.LenderDashboardOverview;
import project.borrowhen.dao.entity.NotificationEntity;
import project.borrowhen.dao.entity.UserEntity;
import project.borrowhen.dto.DashboardDto;
import project.borrowhen.object.BorrowRequestObj;
import project.borrowhen.object.FilterAndSearchObj;
import project.borrowhen.object.InventoryObj;
import project.borrowhen.object.NotificationObj;
import project.borrowhen.object.OverdueBorrowRequestObj;
import project.borrowhen.service.DashboardService;
import project.borrowhen.service.UserService;

@Service
public class DashboardServiceImpl implements DashboardService{
	
	@Autowired
	private BorrowRequestDao borrowRequestDao;
	
	@Autowired
	private NotificationDao notificationDao;
	
	@Autowired
	private InventoryDao inventoryDao;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private UserDao userDao;
	
	@Autowired
	private CipherUtil cipherUtil;

	@Override
	public DashboardDto getBorrowerDashboardDetails() {
		
		DashboardDto outDto = new DashboardDto();
		
		UserEntity user = userService.getLoggedInUser();
		
		List<BorrowRequestEntity> allOverdue = borrowRequestDao.getOverdueRequestForBorrower(user.getId());
		
		List<BorrowRequestObj> overdues = new ArrayList<>();
		
		for(BorrowRequestEntity overdue : allOverdue) {
			
			BorrowRequestObj obj = new BorrowRequestObj();
			
			obj.setItemName(overdue.getItemName());
			Timestamp ts = overdue.getDateToReturn() != null 
				    ? new Timestamp(overdue.getDateToReturn().getTime()) 
				    : null;

			obj.setTimeAgo(TimeAgoUtil.toTimeAgo(ts));
				
			overdues.add(obj);
		}
		
		List<BorrowRequestEntity> allPaymentPending = borrowRequestDao.getPaymentPendingRequestForBorrower(user.getId());
		
		List<BorrowRequestObj> paymentPendings = new ArrayList<>();
		
		for(BorrowRequestEntity paymentPending : allPaymentPending) {
			
			BorrowRequestObj obj = new BorrowRequestObj(); 
			
			obj.setItemName(paymentPending.getItemName());
			obj.setQty(paymentPending.getQty());
			obj.setAmount(paymentPending.getPrice() * paymentPending.getQty());
			
			paymentPendings.add(obj);		
		}
		
		List<NotificationEntity> allNotifications = notificationDao.getNotificationsForBorrower(user.getId());
		
		List<NotificationObj> notifications = new ArrayList<>();
		
		for(NotificationEntity notification : allNotifications) {
			
			NotificationObj obj = new NotificationObj(); 
			
//			obj.setEncryptedId(cipherUtil.encrypt(String.valueOf(notification.getId())));
			obj.setMessage(notification.getMessage());		
			obj.setIsRead(notification.getIsRead());			
			obj.setType(notification.getType());
			obj.setDateAndTime(TimeAgoUtil.toTimeAgo(notification.getCreatedDate()));;
			
			notifications.add(obj);
			
		}
		
		BorrowRequestOverview overview = borrowRequestDao.getBorrowRequestOverviewForBorrower(user.getId());
		
		outDto.setNotifications(notifications);
		outDto.setPaymentPendings(paymentPendings);	
		outDto.setOverdues(overdues);
		outDto.setOverview(overview);
		
		Pageable pageable = PageRequest.of(0, 5);
		List<BorrowRequestData> recentOngoingRequests = borrowRequestDao.getCurrentlyBorrowedRequestOngoingForBorrower(pageable, user.getId()).toList();
		
		List<BorrowRequestObj> ongoingRequests = new ArrayList<>();
		
		for(BorrowRequestData br : recentOngoingRequests) {
			
			BorrowRequestObj obj = new BorrowRequestObj();
			
			obj.setItemName(br.getItemName());
			obj.setLender(br.getLenderFullName());
			obj.setLenderUserId(br.getLenderUserId());
			obj.setStatus(br.getStatus());
			obj.setDateToBorrow(br.getDateToBorrow());
			obj.setDateToReturn(br.getDateToReturn());
			obj.setQty(br.getQty());		
			obj.setPrice(br.getPrice());
			
			ongoingRequests.add(obj);
		}
		
		outDto.setOngoingRequests(ongoingRequests);	
		
		return outDto;
	}

	@Override
	public DashboardDto getLenderDashboardDetails() {
		
		DashboardDto outDto = new DashboardDto();
		
		UserEntity user = userService.getLoggedInUser();
		
		List<NotificationEntity> allNotifications = notificationDao.getNotificationsForBorrower(user.getId());
		
		List<NotificationObj> notifications = new ArrayList<>();
		
		for(NotificationEntity notification : allNotifications) {
			
			NotificationObj obj = new NotificationObj(); 
			
			obj.setMessage(notification.getMessage());		
			obj.setIsRead(notification.getIsRead());			
			obj.setType(notification.getType());
			obj.setDateAndTime(TimeAgoUtil.toTimeAgo(notification.getCreatedDate()));;
			
			notifications.add(obj);
			
		}
		
		List<InventoryEntity> allInventories = inventoryDao.getLenderPopularItems(user.getId());
		
		List<InventoryObj> inventories = new ArrayList<>();
		
		for(InventoryEntity inventory : allInventories) {
			
			InventoryObj obj = new InventoryObj();
			
			obj.setItemName(inventory.getItemName());
			obj.setTotalLent(inventory.getTotalLent());
		
			inventories.add(obj);
		}
		
		LenderDashboardOverview lenderDashboardOverview = borrowRequestDao.getLenderDashboardOverview(user.getId())	;
		
		BorrowRequestOverview overview = borrowRequestDao.getBorrowRequestOverviewForBorrower(user.getId());
		
		outDto.setOverview(overview);
		outDto.setLenderDashboardOverview(lenderDashboardOverview);	
		outDto.setNotifications(notifications);
		outDto.setPopularItems(inventories);
		
		Pageable pageable = PageRequest.of(0, 5);
		List<BorrowRequestData> recentOngoingRequests = borrowRequestDao.getCurrentlyBorrowedRequestOngoing(pageable, user.getId()).toList();
		
		List<BorrowRequestObj> ongoingRequests = new ArrayList<>();
		
		for(BorrowRequestData br : recentOngoingRequests) {
			
			BorrowRequestObj obj = new BorrowRequestObj();
			
			obj.setItemName(br.getItemName());
			obj.setBorrower(br.getBorrowerFullName());
			obj.setBorrowerUserId(br.getBorrowerUserId());
			obj.setStatus(br.getStatus());
			obj.setDateToBorrow(br.getDateToBorrow());
			obj.setDateToReturn(br.getDateToReturn());
			obj.setQty(br.getQty());		
			obj.setPrice(br.getPrice());
			
			ongoingRequests.add(obj);
		}
		
		outDto.setOngoingRequests(ongoingRequests);	
		
		return outDto;
	}

	@Override
	public DashboardDto getAdminDashboardDetails() throws Exception {
		
		DashboardDto outDto = new DashboardDto();
		
		AdminDashboardOverview overview = userDao.getAdminOverview();
		
		outDto.setAdminDashboardOverview(overview);
		
		Pageable pageable = PageRequest.of(0,8);
	     
	    Page<BorrowRequestData> allRequests = borrowRequestDao.getAllBorrowRequests(pageable, CommonConstant.BLANK, "ALL");
	    
	    List<BorrowRequestObj> requests = new ArrayList<>();
	    
	    for (BorrowRequestData request : allRequests) {
	        BorrowRequestObj obj = new BorrowRequestObj();
	        
	        obj.setEncryptedId(cipherUtil.encrypt(String.valueOf(request.getBorrowRequestId())));
	        
	        String borrowerFullName = request.getBorrowerFullName();
	        obj.setBorrower(borrowerFullName.trim());
	        obj.setBorrowerUserId(request.getBorrowerUserId());
	        
	        String lenderFullName = request.getLenderFullName();
	        obj.setLender(lenderFullName.trim());
	        obj.setLenderUserId(request.getLenderUserId());
	        
	        obj.setItemName(request.getItemName());
	        obj.setPrice(request.getPrice());
	        obj.setQty(request.getQty());
	        obj.setDateToBorrow(request.getDateToBorrow());
	        obj.setDateToReturn(request.getDateToReturn());
	        obj.setStatus(request.getStatus());
			obj.setCreatedDate(DateFormatUtil.formatTimestampToString(request.getCreatedDate()));
			obj.setUpdatedDate(DateFormatUtil.formatTimestampToString(request.getUpdatedDate()));		
	        
	        requests.add(obj);
	    }
	    
	    outDto.setRequests(requests);
	    
	    List<InventoryEntity> allInventories = inventoryDao.getAdminPopularItems();
		
		List<InventoryObj> inventories = new ArrayList<>();
		
		for(InventoryEntity inventory : allInventories) {
			
			InventoryObj obj = new InventoryObj();
			
			obj.setItemName(inventory.getItemName());
			obj.setTotalLent(inventory.getTotalLent());
		
			inventories.add(obj);
		}
		
		outDto.setPopularItems(inventories);
		
		List<Double> revenues = borrowRequestDao.getTotalRevenueEachMonth();
		
		outDto.setRevenues(revenues);
		
		List<Integer> borrows = borrowRequestDao.getTotalBorrowEachMonth();
		
		outDto.setBorrows(borrows);
		
		return outDto;
	}

}
