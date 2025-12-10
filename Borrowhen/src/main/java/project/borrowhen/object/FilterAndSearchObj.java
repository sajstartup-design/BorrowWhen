package project.borrowhen.object;

import lombok.Data;

@Data
public class FilterAndSearchObj {

	private String search;
	
	private String role;
	
	private String availability;
	
	private String startDate;
	
	private String endDate;

	private String status;
	
	private String category;
}
