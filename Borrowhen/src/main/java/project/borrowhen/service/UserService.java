package project.borrowhen.service;

import java.util.List;

import org.springframework.stereotype.Service;

import project.borrowhen.dao.entity.UserEntity;
import project.borrowhen.dto.UserDto;

@Service
public interface UserService {
	
	public void saveUser(UserDto inDto) throws Exception;
	
	public UserDto getAllUsers(UserDto inDto) throws Exception;
	
	public UserDto getUser(UserDto inDto) throws Exception;
	
	public void editUser(UserDto inDto) throws Exception;
	
	
	
	/*
	 * Authentication Part
	 * 
	 * This can be used from another services
	 */
	
	public UserEntity getLoggedInUser();
	
	public UserEntity getUser(int id);
	
	public List<String> getAllUserId();
	
	public UserEntity getUserByUserId(String userId);
	
	public List<UserEntity> getAllUsersByRole(String role, String search);
}
