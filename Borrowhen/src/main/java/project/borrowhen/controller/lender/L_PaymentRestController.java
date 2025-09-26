package project.borrowhen.controller.lender;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import project.borrowhen.dto.PaymentDto;
import project.borrowhen.object.FilterAndSearchObj;
import project.borrowhen.object.PaginationObj;
import project.borrowhen.service.PaymentService;

@RestController
@RequestMapping("/api/lender/")
public class L_PaymentRestController {

	@Autowired
    private PaymentService paymentService;

    @GetMapping("payment")
    public PaymentDto getPaymentsForLender(@RequestParam(defaultValue = "0") int page,
    		@RequestParam(required = false) String search) {
        try {
            PaymentDto inDto = new PaymentDto();

            PaginationObj pagination = new PaginationObj();
            pagination.setPage(page);

            FilterAndSearchObj filter = new FilterAndSearchObj();
            filter.setSearch(search);

            inDto.setPagination(pagination);
            inDto.setFilter(filter);

            return paymentService.getAllPaymentForLender(inDto);
            
        } catch (Exception e) {
            e.printStackTrace();

            return new PaymentDto();
        }
    }
}
