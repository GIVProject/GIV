/*package com.example;


import java.io.IOException; 
import java.security.Principal; 
import java.util.HashMap; 
import java.util.Map; 
import java.util.UUID; 


import javax.servlet.Filter; 
import javax.servlet.FilterChain; 
import javax.servlet.ServletException; 
import javax.servlet.http.Cookie; 
import javax.servlet.http.HttpServletRequest; 
import javax.servlet.http.HttpServletResponse; 

import org.springframework.boot.SpringApplication; 
import org.springframework.boot.autoconfigure.SpringBootApplication; 
import org.springframework.boot.autoconfigure.security.SecurityProperties; 
import org.springframework.context.annotation.Configuration; 
import org.springframework.core.annotation.Order; 
import org.springframework.security.config.annotation.web.builders.HttpSecurity; 
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter; 
import org.springframework.security.web.csrf.CsrfFilter; 
import org.springframework.security.web.csrf.CsrfToken; 
import org.springframework.security.web.csrf.CsrfTokenRepository; 
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository; 
import org.springframework.web.bind.annotation.RequestMapping; 
import org.springframework.web.bind.annotation.RestController; 
import org.springframework.web.filter.OncePerRequestFilter; 
import org.springframework.web.util.WebUtils; 


@SpringBootApplication 
@RestController 
public class UiApplication { 


	@RequestMapping("/user") 
	public Principal user(Principal user) { 
		return user; 
	} 


	@RequestMapping("/resource") 
	public Map<String, Object> home() { 
		Map<String, Object> model = new HashMap<String, Object>(); 
		model.put("id", UUID.randomUUID().toString()); 
		model.put("content", "Hello World"); 
		return model; 
	} 

	public static void main(String[] args) { 
		SpringApplication.run(UiApplication.class, args); 
	} 


	@Configuration 
	@Order(SecurityProperties.ACCESS_OVERRIDE_ORDER) 
	protected static class SecurityConfiguration extends WebSecurityConfigurerAdapter { 
		@Override 
		protected void configure(HttpSecurity http) throws Exception { 
			
			System.out.println("Request intercepted--------------");
			
			 
			 http
             .requestMatchers()
                 .antMatchers("/api/**","/oauth/**").and()
             .authorizeRequests()
                 .antMatchers("/**").hasRole("USER")
                 .and()
                 .formLogin()
                 .loginPage("/layout.html#/login"); // default is /login with an HTTP get
                

			
			http.authorizeRequests() 
					.antMatchers("/index.html", "/layout.html", "/login.html", "/css/**","/js/**","/jslibrary/**","/script/**","/images/**","/partials/**","/template/**","/status/**").permitAll().anyRequest()
					.authenticated().and()
					 .formLogin()
		                .loginPage("/layout.html#/login")
		                .permitAll();		           
					.and().csrf()
					.csrfTokenRepository(csrfTokenRepository()).addFilterAfter(csrfHeaderFilter(), CsrfFilter.class).and().exceptionHandling().accessDeniedPage("/403");
			
			 http
	            .authorizeRequests()
	                .antMatchers("/", "/login","/layout.html#/login.html","/layout.html#/login").permitAll()
	                .antMatchers("/css/**").permitAll()
	                .antMatchers("/js/**").permitAll()
	                .antMatchers("/jslibrary/**").permitAll()
	                .antMatchers("/script/**").permitAll()                
	               // .anyRequest().authenticated() 
	                .and()
	            .formLogin()
	                .loginPage("/layout.html#/login")
	                .permitAll()
	                .and()
	            .logout()
	                .permitAll();

		} 
		
		 @Override
	     protected void configure(AuthenticationManagerBuilder auth)
	             throws Exception {
	         auth
	              .inMemoryAuthentication()
	                   .withUser("user")
	                        .password("password")
	                        .roles("USER");
	     }

		private Filter csrfHeaderFilter() { 
			return new OncePerRequestFilter() { 
				@Override 
				protected void doFilterInternal(HttpServletRequest request, 
						HttpServletResponse response, FilterChain filterChain) 
						throws ServletException, IOException { 
					CsrfToken csrf = (CsrfToken) request.getAttribute(CsrfToken.class 
							.getName()); 
					if (csrf != null) { 
						Cookie cookie = WebUtils.getCookie(request, "XSRF-TOKEN"); 
						String token = csrf.getToken(); 
						if (cookie == null || token != null 
								&& !token.equals(cookie.getValue())) { 
							cookie = new Cookie("XSRF-TOKEN", token); 
							cookie.setPath("/"); 
							response.addCookie(cookie); 
						} 
					} 
					filterChain.doFilter(request, response); 
				} 
			}; 
		} 

		private CsrfTokenRepository csrfTokenRepository() { 
			HttpSessionCsrfTokenRepository repository = new HttpSessionCsrfTokenRepository(); 
			repository.setHeaderName("X-XSRF-TOKEN"); 
			return repository; 
		} 
	} 

} 
*/