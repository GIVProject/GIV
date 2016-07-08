/*package com.example.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;
import org.springframework.jdbc.core.JdbcTemplate;

import com.example.exception.UserNotFountException;
import com.sun.research.ws.wadl.Request;

//import com.leanstacks.ws.util.RequestContext;

*//**
 * The RequestContextInitializationFilter is executed for every web request. The
 * filter initializes the RequestContext for the current thread, preventing
 * leaking of RequestContext attributes from the previous thread's execution.
 * 
 * @author Matt Warman
 *//*
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class RequestContextInitializationFilter extends GenericFilterBean {

	*//**
	 * The Logger for this class.
	 *//*
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Override
	public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
			throws IOException, ServletException {
		logger.info("> doFilter");

		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) resp;
		//request.authenticate(response);
		
	//	request.getParameter(name)
	
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) resp;
		String str = request.getHeader("Authorization");

	
		
		if (request.getRequestURI().contains("api")) {

			System.out.println("Str is ---------------------------" + str);
			if (str == null) {

				throw new ServletException("Not authenticated");

			}
		
			System.out.println("Authentication vaue is ================"+request.authenticate(response));	
		
		}

		// RequestContext.init();

		chain.doFilter(req, resp);
		logger.info("< doFilter");
	}

}*/