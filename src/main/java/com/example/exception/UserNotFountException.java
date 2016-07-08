package com.example.exception;

import org.springframework.web.bind.annotation.ControllerAdvice;

@ControllerAdvice
public class UserNotFountException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public UserNotFountException(){
		super.getCause();
	}
	
}
