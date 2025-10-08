package project.borrowhen.dto;

import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import project.borrowhen.common.constant.MessageConstant;
import project.borrowhen.object.FilterAndSearchObj;
import project.borrowhen.object.PaginationObj;
import project.borrowhen.object.UserObj;

@Data
public class UserDto {
	
	private String encryptedId;

	@NotBlank(message = MessageConstant.NOT_BLANK)
	private String fullName;
	
	@NotBlank(message = MessageConstant.NOT_BLANK)
	private String gender;
	
	@NotBlank(message = MessageConstant.NOT_BLANK)
	private String birthDate;
	
	@NotBlank(message = MessageConstant.NOT_BLANK)
	private String address;
	
	@NotBlank(message = MessageConstant.NOT_BLANK)
	@Pattern(regexp = "\\d{10,11}", message = "Phone number must be 10-11 digits")
	private String phoneNumber;
	
	@NotBlank(message = MessageConstant.NOT_BLANK)
	private String emailAddress;
	
	@NotBlank(message = MessageConstant.NOT_BLANK)
	private String barangay;
	
	@NotBlank(message = MessageConstant.NOT_BLANK)
	private String street;
	
	@NotBlank(message = MessageConstant.NOT_BLANK)
	private String city;
	
	@NotBlank(message = MessageConstant.NOT_BLANK)
	private String province;
	
	@NotBlank(message = MessageConstant.NOT_BLANK)
	private String postalCode;
	
	@NotBlank(message = MessageConstant.NOT_BLANK)
	private String userId;
	
	@NotBlank(message = MessageConstant.NOT_BLANK)
	private String password;
	
	private String role;
	
	private String createdDate;
	
	private String updatedDate;
	
	private List<UserObj> users;
	
	private PaginationObj pagination;
	
	private FilterAndSearchObj filter;
}
