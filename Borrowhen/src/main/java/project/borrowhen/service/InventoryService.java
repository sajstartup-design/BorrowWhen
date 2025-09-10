package project.borrowhen.service;

import org.springframework.stereotype.Service;

import project.borrowhen.dto.InventoryDto;

@Service
public interface InventoryService {

	public void saveInventory(InventoryDto inDto) throws Exception;
	
	public InventoryDto getInventory(InventoryDto inDto) throws Exception;
	
	public InventoryDto getAllInventory(InventoryDto inDto) throws Exception;
	
	public InventoryDto getAllOwnedInventory(InventoryDto inDto) throws Exception;
	
	public void editInventory(InventoryDto inDto) throws Exception;
}
