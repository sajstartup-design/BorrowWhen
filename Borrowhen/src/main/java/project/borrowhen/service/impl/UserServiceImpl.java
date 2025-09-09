package project.borrowhen.service.impl;

import java.sql.Date;
import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import project.borrowhen.dao.UserDao;
import project.borrowhen.dao.entity.UserEntity;
import project.borrowhen.dto.UserDto;
import project.borrowhen.service.UserService;

@Service
public class UserServiceImpl implements UserService {
	
	@Autowired
	private UserDao userDao;
	
	@Autowired
	private PasswordEncoder encoder;

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

}
