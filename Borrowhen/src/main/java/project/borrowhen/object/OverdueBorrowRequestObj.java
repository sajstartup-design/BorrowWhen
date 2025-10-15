package project.borrowhen.object;

import lombok.Data;

@Data
public class OverdueBorrowRequestObj {

    private String itemName;
    private double price;
    private String borrower;
    private String dateToReturn;
    private String dateTimeAgo;
    
}
