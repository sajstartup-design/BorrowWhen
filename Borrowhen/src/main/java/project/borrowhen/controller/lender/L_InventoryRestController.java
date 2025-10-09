package project.borrowhen.controller.lender;

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
@RequestMapping("/api/lender/")
public class L_InventoryRestController {
	
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

            return inventoryService.getAllOwnedInventory(inDto);
        } catch (Exception e) {
            e.printStackTrace();

            return new InventoryDto();
        }
    }
}
