package project.borrowhen.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import project.borrowhen.common.constant.CommonConstant;

@EnableWebSecurity
@Configuration
public class SecurityConfig {

	@Autowired
	private DataSource dataSource;

	@Bean
	protected PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	private static final String USER_ACCOUNT_SQL = "SELECT USER_ID,PASSWORD,TRUE"
			+ " FROM USERS "
			+ " WHERE USER_ID = ?"
			+ " AND USERS.IS_DELETED = FALSE ";

	private static final String USER_ROLE_SQL = "SELECT USER_ID, ROLE FROM USERS WHERE USER_ID = ?";
	
	@Bean
	protected UserDetailsManager userDetailsService() {

		JdbcUserDetailsManager users = new JdbcUserDetailsManager(dataSource);

		users.setUsersByUsernameQuery(USER_ACCOUNT_SQL);
		users.setAuthoritiesByUsernameQuery(USER_ROLE_SQL);

		return users;
	}
	
	@Bean
	protected AuthenticationSuccessHandler authenticationSuccessHandler() {
		return new CustomSuccessHandler();
	}

	
	@Bean
	protected SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
				.authorizeHttpRequests((requests) -> requests
						
						
//						.requestMatchers("/images/**").permitAll()
//						.requestMatchers("/css/**").permitAll()
//						.requestMatchers("/script/**").permitAll()
//						.requestMatchers("/gif/**").permitAll()
//						.requestMatchers("/assets/**").permitAll()
//						.requestMatchers("/admin/assets/**").permitAll()
//
//						
//						 
//						.requestMatchers("/payment/receipt").hasAnyAuthority(CommonConstant.ROLE_BORROWER, CommonConstant.ROLE_LENDER)
//						
//						.requestMatchers("/admin/dashboard/**").hasAuthority(CommonConstant.ROLE_ADMIN)
//						.requestMatchers("/admin/borrower/**").hasAuthority(CommonConstant.ROLE_ADMIN)
//						.requestMatchers("/admin/inventory/**").hasAuthority(CommonConstant.ROLE_ADMIN)
//						.requestMatchers("/admin/request/**").hasAuthority(CommonConstant.ROLE_ADMIN)
//						
//						.requestMatchers("/api/admin/user/**").hasAuthority(CommonConstant.ROLE_ADMIN)
//						.requestMatchers("/api/admin/inventory/**").hasAuthority(CommonConstant.ROLE_ADMIN)
//						.requestMatchers("/api/admin/request/**").hasAuthority(CommonConstant.ROLE_ADMIN)
//						
//						
//						.requestMatchers("/lender/inventory/**").hasAuthority(CommonConstant.ROLE_LENDER)
//						.requestMatchers("/lender/request/**").hasAuthority(CommonConstant.ROLE_LENDER)
//						
//						.requestMatchers("/api/lender/inventory/**").hasAuthority(CommonConstant.ROLE_LENDER)
//						.requestMatchers("/api/lender/request/**").hasAuthority(CommonConstant.ROLE_LENDER)
//						.requestMatchers("/api/lender/payment/**").hasAuthority(CommonConstant.ROLE_LENDER)
//						
//						.requestMatchers("/request/**").hasAuthority(CommonConstant.ROLE_BORROWER)
//						.requestMatchers("/payment/**").hasAuthority(CommonConstant.ROLE_BORROWER)
//						
//						.requestMatchers("/api/request/**").hasAuthority(CommonConstant.ROLE_BORROWER)
//						.requestMatchers("/api/payment/**").hasAuthority(CommonConstant.ROLE_BORROWER)
//						
//						.requestMatchers("/api/inventory").authenticated()
//						
//						.requestMatchers("/api/**").authenticated()
//						
//						.requestMatchers("/chat/**").hasAnyAuthority(CommonConstant.ROLE_BORROWER, CommonConstant.ROLE_LENDER)
//						
//						.requestMatchers("/borrow-when-websocket/**").authenticated()
						
						.anyRequest().permitAll()									
						)
				.formLogin((form) -> form
						.loginPage("/login")
						.permitAll()
						.failureUrl("/login")
						//.failureHandler(authenticationFailureHandler())
						.usernameParameter("userId")
						.passwordParameter("password")
						 .defaultSuccessUrl("/dashboard")
						.successHandler(authenticationSuccessHandler()))
				.logout((logout) -> logout
						.logoutSuccessUrl("/login")
						.invalidateHttpSession(true)
						.permitAll());

		return http.build();
	}
}
