package project.borrowhen.service.impl;

import java.sql.Date;
import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import project.borrowhen.common.util.CipherUtil;
import project.borrowhen.dao.BorrowRequestDao;
import project.borrowhen.dao.entity.BorrowRequestEntity;
import project.borrowhen.dao.entity.UserEntity;
import project.borrowhen.dto.BorrowRequestDto;
import project.borrowhen.service.BorrowRequestService;
import project.borrowhen.service.UserService;

@Service
public class BorrowRequestServiceImpl implements BorrowRequestService{
	
	@Autowired
	private BorrowRequestDao borrowRequestDao;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private CipherUtil cipherUtil;

	@Override
	public void saveBorrowRequest(BorrowRequestDto inDto) throws Exception {
		
		Date dateNow = Date.valueOf(LocalDate.now());
		
		UserEntity user = userService.getLoggedInUser();
		
		int id = Integer.valueOf(cipherUtil.decrypt(inDto.getEncryptedId()));
		
		BorrowRequestEntity request = new BorrowRequestEntity();
		
		request.setInventoryId(id);
		
		request.setUserId(user.getId());
		
		
	
		
	}

}
