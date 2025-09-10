package project.borrowhen.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import project.borrowhen.dto.InventoryDto;
import project.borrowhen.object.PaginationObj;
import project.borrowhen.service.InventoryService;

@RestController
public class InventoryRestController {
	
	@Autowired
    private InventoryService inventoryService;

    @GetMapping("/api/inventories")
    public InventoryDto getUsers(@RequestParam(defaultValue = "0") int page) {
        try {
            InventoryDto inDto = new InventoryDto();

            PaginationObj pagination = new PaginationObj();
            pagination.setPage(page);

            inDto.setPagination(pagination);

            return inventoryService.getAlInventory(inDto);
        } catch (Exception e) {
            e.printStackTrace();

            return new InventoryDto();
        }
    }
}
