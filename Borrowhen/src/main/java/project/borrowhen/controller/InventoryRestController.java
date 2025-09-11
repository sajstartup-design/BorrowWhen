package project.borrowhen.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import project.borrowhen.dto.InventoryDto;
import project.borrowhen.object.FilterAndSearchObj;
import project.borrowhen.object.PaginationObj;
import project.borrowhen.service.InventoryService;

@RestController
@RequestMapping("/api/")
public class InventoryRestController {

	@Autowired
    private InventoryService inventoryService;

    @GetMapping("inventory")
    public InventoryDto getUsers(@RequestParam(defaultValue = "0") int page,
    		@RequestParam(required = false) String search) {
    	
        try {																					
            InventoryDto inDto = new InventoryDto();

            PaginationObj pagination = new PaginationObj();
            pagination.setPage(page);
            
            FilterAndSearchObj filter = new FilterAndSearchObj();
            filter.setSearch(search);

            inDto.setPagination(pagination);
            inDto.setFilter(filter);

            return inventoryService.getAllInventory(inDto);
        } catch (Exception e) {
            e.printStackTrace();

            return new InventoryDto();
        }
    }
}
