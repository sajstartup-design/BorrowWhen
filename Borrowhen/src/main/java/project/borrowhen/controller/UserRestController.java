package project.borrowhen.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import project.borrowhen.dto.UserDto;
import project.borrowhen.object.PaginationObj;
import project.borrowhen.service.UserService;

@RestController
public class UserRestController {

	@Autowired
    private UserService userService;

    @GetMapping("/api/users")
    public UserDto getUsers(@RequestParam(defaultValue = "0") int page) {
        try {
            UserDto inDto = new UserDto();

            PaginationObj pagination = new PaginationObj();
            pagination.setPage(page);

            inDto.setPagination(pagination);

            return userService.getAllUsers(inDto);
        } catch (Exception e) {
            e.printStackTrace();

            // Return empty UserDto on error
            return new UserDto();
        }
    }
	
}
