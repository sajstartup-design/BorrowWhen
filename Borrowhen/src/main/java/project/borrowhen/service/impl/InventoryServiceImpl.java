package project.borrowhen.service.impl;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import project.borrowhen.common.constant.CommonConstant;
import project.borrowhen.common.util.CipherUtil;
import project.borrowhen.dao.InventoryDao;
import project.borrowhen.dao.entity.InventoryData;
import project.borrowhen.dao.entity.InventoryEntity;
import project.borrowhen.dao.entity.UserEntity;
import project.borrowhen.dto.InventoryDto;
import project.borrowhen.object.FilterAndSearchObj;
import project.borrowhen.object.InventoryObj;
import project.borrowhen.object.PaginationObj;
import project.borrowhen.service.InventoryService;
import project.borrowhen.service.UserService;

@Service
public class InventoryServiceImpl implements InventoryService{
	
	@Autowired
	private InventoryDao inventoryDao;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private CipherUtil cipherUtil;
	
	@Value("${inventory.max.display}")
	private String MAX_USER_DISPLAY;

	@Override
	public void saveInventory(InventoryDto inDto) throws Exception {
		
		Date dateNow = Date.valueOf(LocalDate.now());
		
		UserEntity user = userService.getLoggedInUser();
		
		InventoryEntity inventory = new InventoryEntity();
		
		if (inDto.getUserId() != null && !inDto.getUserId().isBlank()) {
	        user = userService.getUserByUserId(inDto.getUserId());
	    }
		
		inventory.setUserId(user.getId());
		inventory.setItemName(inDto.getItemName());
		inventory.setPrice(inDto.getPrice());
		inventory.setTotalQty(inDto.getTotalQty());
		inventory.setStatus(CommonConstant.AVAILABLE);
		inventory.setCreatedDate(dateNow);
		inventory.setUpdatedDate(dateNow);
		inventory.setIsDeleted(false);
		
		inventoryDao.save(inventory);
		
	}

	@Override
	public InventoryDto getAllInventory(InventoryDto inDto) throws Exception {
			
		InventoryDto outDto = new InventoryDto();
		
		Pageable pageable = PageRequest.of(inDto.getPagination().getPage(), Integer.valueOf(MAX_USER_DISPLAY));
		
		FilterAndSearchObj filter = inDto.getFilter();
		
		Page<InventoryData> allInventories = inventoryDao.getAllInventory(pageable, filter.getSearch()); 
		
		List<InventoryObj> inventories = new ArrayList<>();
		
		for(InventoryData inventory : allInventories) {
			
			InventoryObj obj = new InventoryObj();
			
			obj.setEncryptedId(cipherUtil.encrypt(String.valueOf(inventory.getInventoryId())));
			obj.setItemName(inventory.getItemName());
			obj.setPrice(inventory.getPrice());
			obj.setTotalQty(inventory.getTotalQty());
			obj.setOwner(inventory.getFirstName() + " " + inventory.getFamilyName());
			obj.setCreatedDate(inventory.getCreatedDate());
			obj.setUpdatedDate(inventory.getUpdatedDate());		
			
			inventories.add(obj);
			
		}
		
		PaginationObj pagination = new PaginationObj();
		
		pagination.setPage(allInventories.getNumber());
		pagination.setTotalPages(allInventories.getTotalPages());
		pagination.setTotalElements(allInventories.getTotalElements());
		pagination.setHasNext(allInventories.hasNext());
		pagination.setHasPrevious(allInventories.hasPrevious());
		
		outDto.setInventories(inventories);
		outDto.setPagination(pagination);
		
		return outDto;
	}

	@Override
	public InventoryDto getAllOwnedInventory(InventoryDto inDto) throws Exception {
		
		InventoryDto outDto = new InventoryDto();
		
		UserEntity user = userService.getLoggedInUser();
		
		Pageable pageable = PageRequest.of(inDto.getPagination().getPage(), Integer.valueOf(MAX_USER_DISPLAY));
		
		Page<InventoryData> allInventories = inventoryDao.getAllOwnedInventory(pageable, user.getId()); 
		
		List<InventoryObj> inventories = new ArrayList<>();
		
		for(InventoryData inventory : allInventories) {
			
			InventoryObj obj = new InventoryObj();
			
			obj.setEncryptedId(cipherUtil.encrypt(String.valueOf(inventory.getInventoryId())));
			obj.setItemName(inventory.getItemName());
			obj.setPrice(inventory.getPrice());
			obj.setTotalQty(inventory.getTotalQty());
	
			inventories.add(obj);
			
		}
		
		PaginationObj pagination = new PaginationObj();
		
		pagination.setPage(allInventories.getNumber());
		pagination.setTotalPages(allInventories.getTotalPages());
		pagination.setTotalElements(allInventories.getTotalElements());
		pagination.setHasNext(allInventories.hasNext());
		pagination.setHasPrevious(allInventories.hasPrevious());
		
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
}
