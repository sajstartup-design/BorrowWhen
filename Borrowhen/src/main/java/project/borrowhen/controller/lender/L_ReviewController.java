package project.borrowhen.controller.lender;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class L_ReviewController {

	@GetMapping("/lender/reviews")
	public String showLenderReviews() {
		
		return "review/l-review";
	}
}
