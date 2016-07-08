package com.example.exception;

public class UserAuthenticationException extends Exception {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public UserAuthenticationException(){
		super("Authentication is failed");
	}

}
