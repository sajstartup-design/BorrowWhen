package project.borrowhen.service;

import org.springframework.stereotype.Service;

@Service
public interface EmailService {

	public void sendActivationEmail(String toEmail, String fullName);
	
	public void sendDeactivationEmail(String toEmail, String fullName);
}
