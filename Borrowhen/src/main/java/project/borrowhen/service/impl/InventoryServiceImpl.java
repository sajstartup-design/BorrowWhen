package project.borrowhen.service.impl;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
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
import project.borrowhen.dao.InventoryDao;
import project.borrowhen.dao.entity.InventoryData;
import project.borrowhen.dao.entity.InventoryEntity;
import project.borrowhen.dao.entity.NotificationEntity;
import project.borrowhen.dao.entity.UserEntity;
import project.borrowhen.dto.InventoryDto;
import project.borrowhen.object.FilterAndSearchObj;
import project.borrowhen.object.InventoryObj;
import project.borrowhen.object.PaginationObj;
import project.borrowhen.service.AdminSettingsService;
import project.borrowhen.service.InventoryService;
import project.borrowhen.service.NotificationService;
import project.borrowhen.service.UserService;

@Service
public class InventoryServiceImpl implements InventoryService{
	
	@Autowired
	private InventoryDao inventoryDao;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private NotificationService notificationService;
	
	@Autowired
	private CipherUtil cipherUtil;

	@Autowired
	private AdminSettingsService adminSettingsService;
    
    private int getMaxInventoryDisplay() {
        return adminSettingsService.getSettings().getInventoryPerPage();
    }

	@Override 
	public void saveInventory(InventoryDto inDto) throws Exception {
		
		Timestamp dateNow = DateFormatUtil.getCurrentTimestamp();
		
		UserEntity user = userService.getLoggedInUser();
		
		InventoryEntity inventory = new InventoryEntity();
		
		if (inDto.getUserId() != null && !inDto.getUserId().isBlank()) {
	        user = userService.getUserByUserId(inDto.getUserId());
	    }
		
		inventory.setUserId(user.getId());
		inventory.setItemName(inDto.getItemName());
		inventory.setPrice(inDto.getPrice());
		inventory.setTotalQty(inDto.getTotalQty());
		inventory.setAvailableQty(inDto.getTotalQty());
		inventory.setStatus(CommonConstant.AVAILABLE);
		inventory.setCreatedDate(dateNow);
		inventory.setUpdatedDate(dateNow);
		inventory.setIsDeleted(false);
		
		inventoryDao.save(inventory);
		
		NotificationEntity notification = new NotificationEntity();
		notification.setUserId(-1);
		notification.setTargetRole(CommonConstant.ROLE_BORROWER);
		
		String message = String.format(
		    "A new item %s is now available",
		    inventory.getItemName()
		);
		notification.setMessage(message);
		notification.setIsRead(false);
		notification.setType(CommonConstant.NEW_ITEM);
		notification.setCreatedDate(dateNow);
		notification.setUpdatedDate(dateNow);
		notification.setIsDeleted(false);
		
		notificationService.saveNotification(notification);	
		
		List<UserEntity> borrowers = userService.getAllUsersByRole(CommonConstant.ROLE_BORROWER, CommonConstant.BLANK);

		notificationService.sendToBorrowers(borrowers, message);
		
	}

	@Override
	public InventoryDto getAllInventory(InventoryDto inDto) throws Exception {
			
		InventoryDto outDto = new InventoryDto();
		
		Pageable pageable = PageRequest.of(inDto.getPagination().getPage(), Integer.valueOf(getMaxInventoryDisplay()));
		
		FilterAndSearchObj filter = inDto.getFilter();
		
		Page<InventoryData> allInventories = inventoryDao.getAllInventory(pageable, filter.getSearch()); 
		
		List<InventoryObj> inventories = new ArrayList<>();
		
		for(InventoryData inventory : allInventories) {
			
			InventoryObj obj = new InventoryObj();
			
			obj.setEncryptedId(cipherUtil.encrypt(String.valueOf(inventory.getInventoryId())));
			obj.setItemName(inventory.getItemName());
			obj.setPrice(inventory.getPrice());
			obj.setTotalQty(inventory.getTotalQty());
			obj.setOwner(inventory.getFullName());
			obj.setUserId(inventory.getUserId());
			obj.setCreatedDate(DateFormatUtil.formatTimestampToString(inventory.getCreatedDate()));
			obj.setUpdatedDate(DateFormatUtil.formatTimestampToString(inventory.getUpdatedDate()));		
			obj.setAvailableQty(inventory.getAvailableQty());
			obj.setIsEditable(inventory.getIsEditable());
			obj.setIsDeletable(inventory.getIsDeletable());
			
			inventories.add(obj);
			
		}
		
		PaginationObj pagination = new PaginationObj();
		
		pagination.setPage(allInventories.getNumber());
		pagination.setTotalPages(allInventories.getTotalPages());
		pagination.setTotalElements(allInventories.getTotalElements());
		pagination.setHasNext(allInventories.hasNext());
		pagination.setHasPrevious(allInventories.hasPrevious());
		pagination.setPageSize(getMaxInventoryDisplay());	
		
		outDto.setInventories(inventories);
		outDto.setPagination(pagination);
		
		return outDto;
	}

	@Override
	public InventoryDto getAllOwnedInventory(InventoryDto inDto) throws Exception {
		
		InventoryDto outDto = new InventoryDto();
		
		UserEntity user = userService.getLoggedInUser();
		
		Pageable pageable = PageRequest.of(inDto.getPagination().getPage(), Integer.valueOf(getMaxInventoryDisplay()));
		
		FilterAndSearchObj filter = inDto.getFilter();
		
		Page<InventoryData> allInventories = inventoryDao.getAllOwnedInventory(pageable, filter.getSearch(), user.getId()); 
		
		List<InventoryObj> inventories = new ArrayList<>();
		
		for(InventoryData inventory : allInventories) {
			
			InventoryObj obj = new InventoryObj();
			
			obj.setEncryptedId(cipherUtil.encrypt(String.valueOf(inventory.getInventoryId())));
			obj.setItemName(inventory.getItemName());
			obj.setPrice(inventory.getPrice());
			obj.setTotalQty(inventory.getTotalQty());
			obj.setAvailableQty(inventory.getAvailableQty());
			obj.setIsEditable(inventory.getIsEditable());
			obj.setIsDeletable(inventory.getIsDeletable());
	
			inventories.add(obj);
			
		}
		
		PaginationObj pagination = new PaginationObj();
		
		pagination.setPage(allInventories.getNumber());
		pagination.setTotalPages(allInventories.getTotalPages());
		pagination.setTotalElements(allInventories.getTotalElements());
		pagination.setHasNext(allInventories.hasNext());
		pagination.setHasPrevious(allInventories.hasPrevious());
		pagination.setPageSize(getMaxInventoryDisplay());	
		
		outDto.setInventories(inventories);
		outDto.setPagination(pagination);
		
		return outDto;
	}

	@Override
	public InventoryDto getInventory(InventoryDto inDto) throws Exception {
		
		InventoryDto outDto = new InventoryDto();
		
		int id = Integer.valueOf(cipherUtil.decrypt(inDto.getEncryptedId()));
		
		InventoryEntity inventory = inventoryDao.getInventory(id);
		
		UserEntity user = userService.getUser(inventory.getUserId());
		
		outDto.setUserId(user.getUserId());
		outDto.setItemName(inventory.getItemName());
		outDto.setPrice(inventory.getPrice());
		outDto.setTotalQty(inventory.getTotalQty());
		outDto.setCreatedDate(DateFormatUtil.formatTimestampToString(inventory.getCreatedDate()));
		outDto.setUpdatedDate(DateFormatUtil.formatTimestampToString(inventory.getUpdatedDate()));
		
		return outDto;
	}

	@Override
	public void editInventory(InventoryDto inDto) throws Exception {
			
		int id = Integer.valueOf(cipherUtil.decrypt(inDto.getEncryptedId()));
		
		Date dateNow = Date.valueOf(LocalDate.now());
		
		UserEntity user = userService.getLoggedInUser();

		if (inDto.getUserId() != null && !inDto.getUserId().isBlank()) {
	        user = userService.getUserByUserId(inDto.getUserId());
	    }
		
		inventoryDao.updateInventory(id, 
				user.getId().intValue(), 
				inDto.getItemName(), 
				inDto.getPrice().doubleValue(),
				inDto.getTotalQty().intValue(),
				dateNow);
		
	
	}

	@Override
	public InventoryEntity getInventory(int id) {
		
		return inventoryDao.getInventory(id);
		
	}

	@Override
	public void updateInventoryAvailableQty(int id, int qty, String status) {
		
		Date dateNow = Date.valueOf(LocalDate.now());
		
		if(CommonConstant.DECREASE.equals(status)) {
			inventoryDao.updateInventoryQty(id, -qty, dateNow);
		}else if(CommonConstant.INCREASE.equals(status)){
			inventoryDao.updateInventoryQty(id, +qty, dateNow);
		}else {
			inventoryDao.updateInventoryQty(id, 0, dateNow);
		}
	}

	@Override
	public List<InventoryData> getRecentInventory(int userId) {
		
		Pageable pageable = PageRequest.of(0, 4);
		
		List<InventoryData> inventories = inventoryDao.getRecentInventory(userId, pageable);
		
		return inventories;
	}
}
