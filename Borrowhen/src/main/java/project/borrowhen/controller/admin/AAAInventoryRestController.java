package project.borrowhen.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import project.borrowhen.dto.InventoryDto;
import project.borrowhen.object.PaginationObj;
import project.borrowhen.service.InventoryService;

@RestController
public class AAAInventoryRestController {
	
	@Autowired
    private InventoryService inventoryService;

    @GetMapping("/api/admin/inventory")
    public InventoryDto getUsers(@RequestParam(defaultValue = "0") int page) {
        try {
            InventoryDto inDto = new InventoryDto();

            PaginationObj pagination = new PaginationObj();
            pagination.setPage(page);

            inDto.setPagination(pagination);

            return inventoryService.getAllInventory(inDto);
        } catch (Exception e) {
            e.printStackTrace();

            return new InventoryDto();
        }
    }
}
