package project.borrowhen.service.impl;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpSession;
import project.borrowhen.common.constant.CommonConstant;
import project.borrowhen.common.util.CipherUtil;
import project.borrowhen.common.util.DateFormatUtil;
import project.borrowhen.dao.UserDao;
import project.borrowhen.dao.entity.UserData;
import project.borrowhen.dao.entity.UserEntity;
import project.borrowhen.dto.UserDto;
import project.borrowhen.object.FilterAndSearchObj;
import project.borrowhen.object.PaginationObj;
import project.borrowhen.object.UserObj;
import project.borrowhen.service.AdminSettingsService;
import project.borrowhen.service.UserService;

@Service
public class UserServiceImpl implements UserService {
	
	@Autowired
	private UserDao userDao;
	
	@Autowired
	private PasswordEncoder encoder;
	
	@Autowired
	private HttpSession httpSession;
	
	@Autowired
	private CipherUtil cipherUtil;
	
	@Autowired
	private AdminSettingsService adminSettingsService;
    
    private int getMaxUserDisplay() {
        return adminSettingsService.getSettings().getUserPerPage();
    }

	@Override
	public void saveUser(UserDto inDto) throws Exception{
		
		Timestamp dateNow = DateFormatUtil.getCurrentTimestamp();
		
		UserEntity user = new UserEntity();
		
		user.setFullName(inDto.getFullName());
	
		user.setEmailAddress(inDto.getEmailAddress());
		user.setPhoneNumber(inDto.getPhoneNumber());
		user.setBirthDate(Date.valueOf(inDto.getBirthDate()));
		user.setGender(inDto.getGender());
		user.setUserId(inDto.getUserId());
		user.setRole(inDto.getRole());
		user.setPassword(encoder.encode(inDto.getPassword()));
		user.setCreatedDate(dateNow);
		user.setUpdatedDate(dateNow);
		user.setIsDeleted(false);
		
		userDao.save(user);
		
	}

	@Override
	public UserDto getAllUsers(UserDto inDto) throws Exception {
		
		UserDto outDto = new UserDto();
		
		Pageable pageable = PageRequest.of(inDto.getPagination().getPage(), Integer.valueOf(getMaxUserDisplay()));
		
		FilterAndSearchObj filter = inDto.getFilter();
		
		Page<UserData> allUsers = userDao.getAllUsers(pageable, filter.getSearch());
		
		List<UserObj> users = new ArrayList<>();
		
		for (UserData user : allUsers) {
		    UserObj obj = new UserObj();

		    obj.setEncryptedId(cipherUtil.encrypt(String.valueOf(user.getId())));
		    obj.setFirstName(user.getFirstName());
		    obj.setMiddleName(user.getMiddleName());
		    obj.setFamilyName(user.getFamilyName());
		    obj.setAddress(user.getAddress());
		    obj.setEmailAddress(user.getEmailAddress());
		    obj.setPhoneNumber(user.getPhoneNumber());

		    if (user.getBirthDate() != null) {
		        obj.setBirthDate(user.getBirthDate().toString());
		    }

		    obj.setGender(user.getGender());
		    obj.setUserId(user.getUserId());
		    obj.setRole(user.getRole());
			obj.setCreatedDate(DateFormatUtil.formatTimestampToString(user.getCreatedDate()));
			obj.setUpdatedDate(DateFormatUtil.formatTimestampToString(user.getUpdatedDate()));	
			obj.setIsDeletable(user.getIsDeletable());
			
		    users.add(obj);
		}
		
		PaginationObj pagination = new PaginationObj();
		
		pagination.setPage(allUsers.getNumber());
		pagination.setTotalPages(allUsers.getTotalPages());
		pagination.setTotalElements(allUsers.getTotalElements());
		pagination.setHasNext(allUsers.hasNext());
		pagination.setHasPrevious(allUsers.hasPrevious());
		
		outDto.setUsers(users);
		outDto.setPagination(pagination);

		return outDto;
	}

	@Override
	public UserDto getUser(UserDto inDto) throws Exception {
		
		UserDto outDto = new UserDto();
		
		int id = Integer.valueOf(cipherUtil.decrypt(inDto.getEncryptedId()));
		
		UserEntity user = userDao.getUser(id);
		
	    outDto.setEncryptedId(inDto.getEncryptedId());
	    outDto.setFullName(user.getFullName());
	    outDto.setEmailAddress(user.getEmailAddress());
	    outDto.setPhoneNumber(user.getPhoneNumber());

	    if (user.getBirthDate() != null) {
	    	outDto.setBirthDate(user.getBirthDate().toString());
	    }

	    outDto.setGender(user.getGender());
	    outDto.setUserId(user.getUserId());
	    outDto.setRole(user.getRole());
	    outDto.setCreatedDate(DateFormatUtil.formatTimestampToString(user.getCreatedDate()));
	    outDto.setUpdatedDate(DateFormatUtil.formatTimestampToString(user.getUpdatedDate()));
	    
	    return outDto;
	}

	@Override
	public void editUser(UserDto inDto) throws Exception {
		
		Date dateNow = Date.valueOf(LocalDate.now());
		
		int id = Integer.valueOf(cipherUtil.decrypt(inDto.getEncryptedId()));
		
		//Check if the password has been changed
		boolean hasChanged = false; 
		
		if(!inDto.getPassword().contains("0000000000")) {
			hasChanged = true;
		}
		
		userDao.updateUser(id,
				inDto.getFullName(),
				inDto.getAddress(),
				inDto.getEmailAddress(),
				inDto.getPhoneNumber(),
				Date.valueOf(inDto.getBirthDate()),
				inDto.getGender(),
				inDto.getUserId(),
				encoder.encode(inDto.getPassword()),
				inDto.getRole(),
				hasChanged,
				dateNow);
		
		UserEntity loggedInUser = getLoggedInUser();
		
		if(loggedInUser.getId() == id) {
			UserEntity user = userDao.getUser(id);
			
			httpSession.setAttribute("user", user);
		}
	}

	@Override
	public UserEntity getLoggedInUser() {

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	  
		String userId = CommonConstant.BLANK;
	
        if(httpSession.getAttribute("userId") == null || CommonConstant.BLANK.equals(httpSession.getAttribute("userId"))) {
		
        	userId = authentication.getName();

		} else {

			userId = (String) httpSession.getAttribute("userId");
		}

		httpSession.setAttribute("userId", userId);

        UserEntity user = userDao.getUserByUserId(userId);

		return user;
		
//		// Try to retrieve cached user from session
//	    UserEntity user = (UserEntity) httpSession.getAttribute("user");
//
//	    // If not yet stored, return null (or you can throw an exception if you want strict behavior)
//	    return user;
	}

	@Override
	public List<String> getAllUserId() {
		
		return userDao.getAllUserId();
	}

	@Override
	public UserEntity getUserByUserId(String userId) {
		
		return userDao.getUserByUserId(userId);
	}

	@Override
	public UserEntity getUser(int id) {
		
		return userDao.getUser(id);
	}

	@Override
	public List<UserEntity> getAllUsersByRole(String role, String search) {
		
		return userDao.getAllUsersByRole(role, search);
	}
}