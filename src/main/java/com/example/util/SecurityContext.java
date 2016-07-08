package com.example.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;


public class SecurityContext {

	@Autowired
	JdbcTemplate jdbcTemplate;

	public boolean isAuthenticated(String token) {
		boolean result = false;
		
		if(jdbcTemplate==null){
			System.out.println("JDBC template is null inside SecurityContext===============");
		}

		String query = "select token from currentlogonstatus where token='" + token + "'";

		System.out.println("query is ------------"+query);
		
		try {
			jdbcTemplate.queryForObject(query, String.class);
			result = true;
		} catch (EmptyResultDataAccessException e) {
			result = false;
		}

		return result;
	}

	public boolean isAuthorized(String token, String[] role) {
		boolean result = false;
		if(jdbcTemplate==null){
			System.out.println("JDBC template is null inside SecurityContext===============");
		}
		String query = "Select roleid from currentlogonstatus where token='" + token + "'";

		System.out.println("query is ------------"+query);
		
		String roleid = "";

		try {
			roleid = jdbcTemplate.queryForObject(query, String.class);

		} catch (EmptyResultDataAccessException e) {
			result = false;
		}

		for (int i = 0; i < role.length; i++) {
			if (roleid.equalsIgnoreCase(role[i])) {
				result = true;
			}
		}

		return result;
	}

	public String getUserId(String token) {
		
		if(jdbcTemplate==null){
			System.out.println("JDBC template is null inside SecurityContext===============");
		}
		String userid = null;
		String username = "";
		String query = "Select userid from currentlogonstatus where token='" + token + "'";

		System.out.println("query is ------------"+query);
		
		try {
			username = jdbcTemplate.queryForObject(query, String.class);

			String query1 = "select userid from userinfo where username='" + username + "'";

			System.out.println("query1 is ------------"+query1);
			System.out.println("username is ------------"+username);
			
			userid = jdbcTemplate.queryForObject(query1, String.class);

			System.out.println("userid is ------------"+userid);
		} catch (EmptyResultDataAccessException e) {

		}catch(Exception e){
			e.printStackTrace();
		}

		return userid;

	}

	public String getActiveProjectIdByToken(String token) {
		if(jdbcTemplate==null){
			System.out.println("JDBC template is null inside SecurityContext===============");
		}

		String projectid = null;
		String username = "";
		String query = "Select userid from currentlogonstatus where token='" + token + "'";
		
		System.out.println("query is ------------"+query);
		
		try {
			username = jdbcTemplate.queryForObject(query, String.class);

			String query1 = "select activeproject from userinfo where username='" + username + "'";

			projectid = jdbcTemplate.queryForObject(query1, String.class);

		} catch (EmptyResultDataAccessException e) {

		}

		return projectid;

	}

	public String getActiveProjectIdByUserId(String userid) {
		
		if(jdbcTemplate==null){
			System.out.println("JDBC template is null inside SecurityContext===============");
		}
		String projectid = null;

		String query = "select activeproject from userinfo where username='" + userid + "'";

		System.out.println("query is ------------"+query);
		
		try {
			projectid = jdbcTemplate.queryForObject(query, String.class);

		} catch (EmptyResultDataAccessException e) {

		}
		return projectid;
	}

}
