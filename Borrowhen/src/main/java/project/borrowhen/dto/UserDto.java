package project.borrowhen.dto;

import java.util.List;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import project.borrowhen.common.constant.MessageConstant;
import project.borrowhen.object.BorrowRequestObj;
import project.borrowhen.object.FilterAndSearchObj;
import project.borrowhen.object.InventoryObj;
import project.borrowhen.object.PaginationObj;
import project.borrowhen.object.UserObj;

@Data
public class UserDto {
	
	private String encryptedId;

	@NotBlank(message = MessageConstant.NOT_BLANK, groups = {ValidationGroup.Create.class, ValidationGroup.Update.class})
	private String fullName;
	
	@NotBlank(message = MessageConstant.NOT_BLANK, groups = {ValidationGroup.Create.class})
	private String gender;
	
	@NotBlank(message = MessageConstant.NOT_BLANK, groups = {ValidationGroup.Create.class})
	private String birthDate;
	
	@NotBlank(message = MessageConstant.NOT_BLANK, groups = {ValidationGroup.Create.class})
	private String phoneNumber;
	
	@NotBlank(message = MessageConstant.NOT_BLANK, groups = {ValidationGroup.Create.class, ValidationGroup.Update.class})
	private String emailAddress;
	
	@NotBlank(message = MessageConstant.NOT_BLANK, groups = {ValidationGroup.Create.class})
	private String barangay;
	
	@NotBlank(message = MessageConstant.NOT_BLANK, groups = {ValidationGroup.Create.class})
	private String street;
	
	@NotBlank(message = MessageConstant.NOT_BLANK, groups = {ValidationGroup.Create.class})
	private String city;
	
	@NotBlank(message = MessageConstant.NOT_BLANK, groups = {ValidationGroup.Create.class})
	private String province;
	
	@NotBlank(message = MessageConstant.NOT_BLANK, groups = {ValidationGroup.Create.class})
	private String postalCode;
	
	private String about;
	
	@NotBlank(message = MessageConstant.NOT_BLANK, groups = {ValidationGroup.Create.class, ValidationGroup.Update.class})
	private String userId;
	
	@NotBlank(message = MessageConstant.NOT_BLANK, groups = {ValidationGroup.Create.class})
	private String password;
	
	private String role;
	
	private String createdDate;
	
	private String updatedDate;
	
	private List<UserObj> users;
	
	private List<InventoryObj> recentInventory;
	
	private List<BorrowRequestObj> recentBorrow;
	
	private UserObj user;
	
	private PaginationObj pagination;
	
	private FilterAndSearchObj filter;
}
