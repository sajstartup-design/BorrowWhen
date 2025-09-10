package project.borrowhen.service;

import org.springframework.stereotype.Service;

import project.borrowhen.dto.InventoryDto;

@Service
public interface InventoryService {

	public void saveInventory(InventoryDto inDto) throws Exception;
	
	public InventoryDto getAlInventory(InventoryDto inDto) throws Exception;
}
