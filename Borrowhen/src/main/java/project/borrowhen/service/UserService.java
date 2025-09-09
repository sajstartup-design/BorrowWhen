package project.borrowhen.service;

import org.springframework.stereotype.Service;

import project.borrowhen.dto.UserDto;

@Service
public interface UserService {
	
	public void saveUser(UserDto inDto) throws Exception;
	
	public UserDto getAllUsers(UserDto inDto) throws Exception;
	
	public UserDto getUser(UserDto inDto) throws Exception;
	
	public void editUser(UserDto inDto) throws Exception;
}
