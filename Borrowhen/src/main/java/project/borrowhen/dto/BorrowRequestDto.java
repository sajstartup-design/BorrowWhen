package project.borrowhen.dto;

import lombok.Data;

@Data
public class BorrowRequestDto {
	
	private String encryptedId;
	
	private Integer qty;
	
	private String dateToBorrow;

	private String dateToReturn;
}
