package com.example.exception;

public class UnAuthenticatedRequestException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public UnAuthenticatedRequestException(){
		super("Authentication required");
	}
}
