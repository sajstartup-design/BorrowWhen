package project.borrowhen.service;

import org.springframework.stereotype.Service;

import project.borrowhen.dao.entity.InventoryEntity;
import project.borrowhen.dto.InventoryDto;

@Service
public interface InventoryService {

	public void saveInventory(InventoryDto inDto) throws Exception;
	
	public InventoryDto getInventory(InventoryDto inDto) throws Exception;
	
	public InventoryDto getAllInventory(InventoryDto inDto) throws Exception;
	
	public InventoryDto getAllOwnedInventory(InventoryDto inDto) throws Exception;
	
	public void editInventory(InventoryDto inDto) throws Exception;
	
	
	/*
	 * To be used from another services
	 */
	public InventoryEntity getInventory(int id);
	
	public void updateInventoryAvailableQty(int id, int qty, String status);
}
