package project.borrowhen.object;

import lombok.Data;

@Data
public class PaginationObj {
	
    private int page;           
    
    private int totalPages;   
    
    private long totalElements;  
    
    private boolean hasNext;
    
    private boolean hasPrevious;
    
    private int pageSize;
}
