package project.borrowhen.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import project.borrowhen.service.EmailService;

@Service
public class EmailServiceImpl implements EmailService{
	
	@Autowired
    private JavaMailSender mailSender;

	@Override
	public void sendActivationEmail(String toEmail, String fullName) {
		SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("Your Account Has Been Activated");
        message.setText("Hello " + fullName + ",\n\nYour account has been successfully activated.");
        mailSender.send(message);
		
	}

	@Override
	public void sendDeactivationEmail(String toEmail, String fullName) {
		SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("Your Account Has Been Deactivated");
        message.setText("Hello " + fullName + ",\n\nYour account has been deactivated. "
        				+ "If you believe this was a mistake, please contact support.");
        mailSender.send(message);
		
	}
	
	

}
