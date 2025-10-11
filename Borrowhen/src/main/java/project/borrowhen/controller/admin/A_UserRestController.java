package project.borrowhen.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import project.borrowhen.common.constant.CommonConstant;
import project.borrowhen.dto.UserDto;
import project.borrowhen.object.FilterAndSearchObj;
import project.borrowhen.object.PaginationObj;
import project.borrowhen.service.UserService;

@RestController
public class A_UserRestController {

	@Autowired
    private UserService userService;

    @GetMapping("/api/admin/borrowers")
    public UserDto getUsers(@RequestParam(defaultValue = "0") int page,
    		@RequestParam(required = false) String search) {
        try {
            UserDto inDto = new UserDto();

            PaginationObj pagination = new PaginationObj();
            pagination.setPage(page);

            FilterAndSearchObj filter = new FilterAndSearchObj();
            filter.setSearch(search);
            filter.setRole(CommonConstant.ROLE_BORROWER);

            inDto.setPagination(pagination);
            inDto.setFilter(filter);

            return userService.getAllUsers(inDto);
        } catch (Exception e) {
            e.printStackTrace();

            // Return empty UserDto on error
            return new UserDto();
        }
    }
    
    @GetMapping("/api/admin/lenders")
    public UserDto getLenders(@RequestParam(defaultValue = "0") int page,
    		@RequestParam(required = false) String search) {
        try {
            UserDto inDto = new UserDto();

            PaginationObj pagination = new PaginationObj();
            pagination.setPage(page);

            FilterAndSearchObj filter = new FilterAndSearchObj();
            filter.setSearch(search);
            filter.setRole(CommonConstant.ROLE_LENDER);

            inDto.setPagination(pagination);
            inDto.setFilter(filter);

            return userService.getAllUsers(inDto);
        } catch (Exception e) {
            e.printStackTrace();

            // Return empty UserDto on error
            return new UserDto();
        }
    }
	
}
