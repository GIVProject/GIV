package com.example.exception;

public class AuthorizationException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public AuthorizationException(){
		super("Authorization exception");
	}
	
}
