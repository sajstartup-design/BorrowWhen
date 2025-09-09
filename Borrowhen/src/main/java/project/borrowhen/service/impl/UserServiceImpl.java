package project.borrowhen.service.impl;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import project.borrowhen.common.util.CipherUtil;
import project.borrowhen.dao.UserDao;
import project.borrowhen.dao.entity.UserEntity;
import project.borrowhen.dto.UserDto;
import project.borrowhen.object.PaginationObj;
import project.borrowhen.object.UserObj;
import project.borrowhen.service.UserService;

@Service
public class UserServiceImpl implements UserService {
	
	@Autowired
	private UserDao userDao;
	
	@Autowired
	private PasswordEncoder encoder;
	
	@Autowired
	private CipherUtil cipherUtil;
	
	@Value("${user.max.display}")
	private String MAX_USER_DISPLAY;

	@Override
	public void saveUser(UserDto inDto) throws Exception{
		
		Date dateNow = Date.valueOf(LocalDate.now());
		
		UserEntity user = new UserEntity();
		
		user.setFirstName(inDto.getFirstName());
		user.setMiddleName(inDto.getMiddleName());
		user.setFamilyName(inDto.getFamilyName());
		user.setAddress(inDto.getAddress());
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
		
		Pageable pageable = PageRequest.of(inDto.getPagination().getPage(), Integer.valueOf(MAX_USER_DISPLAY));
		
		Page<UserEntity> allUsers = userDao.getAllUsers(pageable);
		
		List<UserObj> users = new ArrayList<>();
		
		for (UserEntity user : allUsers) {
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
	    outDto.setFirstName(user.getFirstName());
	    outDto.setMiddleName(user.getMiddleName());
	    outDto.setFamilyName(user.getFamilyName());
	    outDto.setAddress(user.getAddress());
	    outDto.setEmailAddress(user.getEmailAddress());
	    outDto.setPhoneNumber(user.getPhoneNumber());

	    if (user.getBirthDate() != null) {
	    	outDto.setBirthDate(user.getBirthDate().toString());
	    }

	    outDto.setGender(user.getGender());
	    outDto.setUserId(user.getUserId());
	    outDto.setRole(user.getRole());
	    
		return outDto;
	}

	@Override
	public void editUser(UserDto inDto) throws Exception {
		
		int id = Integer.valueOf(cipherUtil.decrypt(inDto.getEncryptedId()));
		
		//Check if the password has been changed
		boolean hasChanged = false; 
		
		if(!inDto.getPassword().contains("0000000000")) {
			hasChanged = true;
		}
		
		userDao.updateUser(id,
				inDto.getFirstName(),
				inDto.getMiddleName(),
				inDto.getFamilyName(),
				inDto.getAddress(),
				inDto.getEmailAddress(),
				inDto.getPhoneNumber(),
				Date.valueOf(inDto.getBirthDate()),
				inDto.getGender(),
				inDto.getUserId(),
				encoder.encode(inDto.getPassword()),
				inDto.getRole(),
				hasChanged);
	}
}