package project.borrowhen.dto;

import java.util.List;

import lombok.Data;
import project.borrowhen.object.BorrowRequestObj;
import project.borrowhen.object.FilterAndSearchObj;
import project.borrowhen.object.PaginationObj;

@Data
public class BorrowRequestDto {
	
	private String encryptedId;
	
	private Integer qty;
	
	private String dateToBorrow;

	private String dateToReturn;
	
	
	private List<BorrowRequestObj> requests;
	
	private PaginationObj pagination;
	
	private FilterAndSearchObj filter;
	
}
