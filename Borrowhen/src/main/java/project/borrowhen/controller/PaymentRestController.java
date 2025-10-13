package project.borrowhen.controller;

import project.borrowhen.common.constant.MessageConstant;
import project.borrowhen.dto.BorrowRequestDto;
import project.borrowhen.dto.PaymentDto;
import project.borrowhen.object.FilterAndSearchObj;
import project.borrowhen.object.PaginationObj;
import project.borrowhen.service.BorrowRequestService;
import project.borrowhen.service.PaymentService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@RestController
@RequestMapping("/api")
public class PaymentRestController {
	
	@Autowired
    private PaymentService paymentService;
	
	@Autowired
	private BorrowRequestService borrowRequestService;

    @GetMapping("/payment")
    public PaymentDto getPaymentsForBorrower(@RequestParam(defaultValue = "0") int page,
    		@RequestParam(required = false) String search) {
        try {
            PaymentDto inDto = new PaymentDto();

            PaginationObj pagination = new PaginationObj();
            pagination.setPage(page);

            FilterAndSearchObj filter = new FilterAndSearchObj();
            filter.setSearch(search);

            inDto.setPagination(pagination);
            inDto.setFilter(filter);

            return paymentService.getAllPaymentForBorrower(inDto);
            
        } catch (Exception e) {
            e.printStackTrace();

            return new PaymentDto();
        }
    }
    
    @PostMapping("/payment/confirmed")
	public void confirmPayment(@ModelAttribute BorrowRequestDto webDto,
			RedirectAttributes ra) {
		
		try {

			BorrowRequestDto inDto = new BorrowRequestDto();
			
			inDto.setEncryptedId(webDto.getEncryptedId());
			
			borrowRequestService.paidBorrowRequest(inDto);
			
		}catch(Exception e) {
			
			e.printStackTrace();
			
			ra.addFlashAttribute("errorMsg", MessageConstant.SOMETHING_WENT_WRONG);

		}
	}
}

