package com.example.exception;

import org.springframework.web.bind.annotation.ControllerAdvice;


public class BadCredentialsException extends Exception {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public BadCredentialsException(){
		super("Bad credentials");
	}

}
