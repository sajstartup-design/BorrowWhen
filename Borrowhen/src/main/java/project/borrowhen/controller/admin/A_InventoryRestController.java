package project.borrowhen.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import project.borrowhen.common.constant.CommonConstant;
import project.borrowhen.dto.InventoryDto;
import project.borrowhen.object.FilterAndSearchObj;
import project.borrowhen.object.PaginationObj;
import project.borrowhen.service.InventoryService;

@RestController
public class A_InventoryRestController {
	
	@Autowired
    private InventoryService inventoryService;

    @GetMapping("/api/admin/inventory")
    public InventoryDto getUsers(@RequestParam(defaultValue = "0") int page) {
        try {
            InventoryDto inDto = new InventoryDto();

            PaginationObj pagination = new PaginationObj();
            pagination.setPage(page);
            
            FilterAndSearchObj filter = new FilterAndSearchObj();
            filter.setSearch(CommonConstant.BLANK);

            inDto.setPagination(pagination);
            inDto.setFilter(filter);

            return inventoryService.getAllInventory(inDto);
        } catch (Exception e) {
            e.printStackTrace();

            return new InventoryDto();
        }
    }
}
