package project.borrowhen.config;

import java.io.IOException;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import project.borrowhen.dao.entity.UserEntity;
import project.borrowhen.service.UserService;

@Component
public class CustomSuccessHandler implements AuthenticationSuccessHandler {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private HttpSession httpSession;

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
	
		// Retrieve user information
		UserEntity user = userService.getLoggedInUser();

//		if (user.getBlockFlg()) {
//			// If the user is blocked, redirect to login with an error message
//			SecurityContextHolder.clearContext();
//			request.getSession().invalidate();
//			request.getSession().setAttribute("errorMessageLogin", "Your account has been blocked");
//			response.sendRedirect("/login");
//			return;
//		}
		
		httpSession.setAttribute("user", user);

		Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

		if (authorities.stream().anyMatch(role -> role.getAuthority().equals("ADMIN"))) {

			response.sendRedirect("/admin/dashboard");
		} else if (authorities.stream().anyMatch(role -> role.getAuthority().equals("LENDER"))) { 
																									
			response.sendRedirect("/lender/dashboard");
			
		} else if (authorities.stream().anyMatch(role -> role.getAuthority().equals("BORROWER"))) { 
																									
			response.sendRedirect("/dashboard");
			
		} else {
			response.sendRedirect("/login");
		}

		if (authentication != null) {
			String userId = authentication.getName();
			
			request.getSession().setAttribute("role", user.getRole());

			request.getSession().setAttribute("fullname", user.getFirstName() + " " + user.getFamilyName());

			request.getSession().setAttribute("userId", userId);

			request.getSession().setAttribute("id", user.getId());

		}
		
	}

}
