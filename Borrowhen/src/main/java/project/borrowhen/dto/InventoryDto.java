package project.borrowhen.dto;



import java.util.List;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import project.borrowhen.common.constant.MessageConstant;
import project.borrowhen.object.FilterAndSearchObj;
import project.borrowhen.object.InventoryObj;
import project.borrowhen.object.PaginationObj;

@Data
public class InventoryDto {

	private String encryptedId;
	
	private String userId;
	
	@NotBlank(message = MessageConstant.NOT_BLANK)
	private String itemName;
	
	@Positive(message = MessageConstant.NOT_BLANK)
	@NotNull(message = MessageConstant.NOT_BLANK)
	private Double price;

	@Min(value = 1, message = MessageConstant.QUANTITY_NOT_BLANK)
	@NotNull(message = MessageConstant.QUANTITY_NOT_BLANK)
	private Integer totalQty;
	
	private String createdDate;
	
	private String updatedDate;
	
	private String dateToBorrow;

	private String dateToReturn;
	
	private List<String> allUserId;
	
	private List<InventoryObj> inventories;
	
	private PaginationObj pagination;
	
	private FilterAndSearchObj filter;
}
