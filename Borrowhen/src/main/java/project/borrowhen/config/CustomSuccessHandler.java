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
	
		// ✅ Get user ID (usually email or username)
	    String userId = authentication.getName();

	    // ✅ Fetch the user directly from the database
	    UserEntity user = userService.getUserByUserId(userId);

	    // ✅ Safety check
	    if (user == null) {
	        // Something went wrong — no user found for the authenticated ID
	        response.sendRedirect("/login?error=userNotFound");
	        return;
	    }

	    // ✅ Set session attributes
	    HttpSession session = request.getSession();
	    session.setAttribute("role", user.getRole());
	    session.setAttribute("fullname", user.getFullName());
	    session.setAttribute("userId", userId);
	    session.setAttribute("id", user.getId());

	    // ✅ Role-based redirect
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

			request.getSession().setAttribute("role", user.getRole());

			request.getSession().setAttribute("fullname", user.getFullName());

			request.getSession().setAttribute("userId", userId);

			request.getSession().setAttribute("id", user.getId());

		}
		
	}

}
