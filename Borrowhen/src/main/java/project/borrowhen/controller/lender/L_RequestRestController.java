package project.borrowhen.controller.lender;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import project.borrowhen.dto.BorrowRequestDto;
import project.borrowhen.object.PaginationObj;
import project.borrowhen.service.BorrowRequestService;

@RestController
public class L_RequestRestController {

	@Autowired
    private BorrowRequestService borrowRequestService;

    @GetMapping("/api/lender/request")
    public BorrowRequestDto getRequests(@RequestParam(defaultValue = "0") int page) {
        try {
        	BorrowRequestDto inDto = new BorrowRequestDto();

            PaginationObj pagination = new PaginationObj();
            pagination.setPage(page);

            inDto.setPagination(pagination);

            return borrowRequestService.getAllOwnedBorrowRequestForLender(inDto);
            
        } catch (Exception e) {
            e.printStackTrace();

            return new BorrowRequestDto();
        }
    }
}
