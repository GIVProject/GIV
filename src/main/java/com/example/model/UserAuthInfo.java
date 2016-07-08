package com.example.model;

import java.util.Date;

public class UserAuthInfo {

	private String userName;
	
	private String password;
	
	private String role;
	
	private Date lastLogonDate;

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public Date getLastLogonDate() {
		return lastLogonDate;
	}

	public void setLastLogonDate(Date lastLogonDate) {
		this.lastLogonDate = lastLogonDate;
	}
	
	
	
	
}
