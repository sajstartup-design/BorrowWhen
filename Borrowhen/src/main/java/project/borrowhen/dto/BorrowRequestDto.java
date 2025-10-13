package project.borrowhen.dto;

import java.util.List;

import lombok.Data;
import project.borrowhen.dao.entity.BorrowRequestOverview;
import project.borrowhen.object.BorrowRequestObj;
import project.borrowhen.object.FilterAndSearchObj;
import project.borrowhen.object.PaginationObj;
import project.borrowhen.object.UserObj;

@Data
public class BorrowRequestDto {
	
	private String encryptedId;
	
	private Integer qty;
	
	private String dateToBorrow;

	private String dateToReturn;
	
	private BorrowRequestObj request;
	
	private UserObj borrower;
	
	private List<BorrowRequestObj> requests;
	
	private PaginationObj pagination;
	
	private FilterAndSearchObj filter;
	
	private BorrowRequestOverview overview;
	
	private Double rating;
	
	private String feedback;

	
}
