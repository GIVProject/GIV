package com.example.controller;

import java.io.IOException;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookieStore;
import java.net.HttpCookie;
import java.net.URL;
import java.util.Calendar;
import java.util.Date;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.httpclient.HttpStatus;
import org.apache.coyote.http11.AbstractNioInputBuffer.HeaderParseData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.exception.AuthorizationException;
import com.example.exception.BadCredentialsException;
import com.example.exception.UnAuthenticatedRequestException;
import com.example.model.UserAuthInfo;
import com.example.util.SessionIdentifierGenerator;

@RestController
public class LogonController {

	@Autowired
	JdbcTemplate jdbcTemplate;

	@RequestMapping(value = "/api/authenticateUser", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public String authenticateUser(@RequestBody UserAuthInfo userAuthInfo, HttpServletRequest request,
			HttpServletResponse response) throws BadCredentialsException, IOException {

		String role = "error";

		String query = "select count(*) from userlogoninfo where username='" + userAuthInfo.getUserName()
				+ "' and password='" + userAuthInfo.getPassword() + "'";

		System.out.println("---------------------" + query);

		String query1 = "select roleid from userlogoninfo where username='" + userAuthInfo.getUserName()
				+ "' and password='" + userAuthInfo.getPassword() + "'";

		System.out.println("---------------------" + query1);
		/*
		 * int i = jdbcTemplate.queryForObject(query, Integer.class);
		 * 
		 * if (i != 1) { role = jdbcTemplate.queryForObject(query1,
		 * String.class); } else { role = "error"; }
		 */
		String returnValue = "1";
		// try{
		authenticate(userAuthInfo.getUserName(), userAuthInfo.getPassword(), response);

		System.out.println("Authenticated");

		role = getRole(userAuthInfo.getUserName());

		System.out.println("Got the role");

		generateToken(userAuthInfo.getUserName(), role, request, response);

		updateLogonInfo(userAuthInfo.getUserName());

		System.out.println("Token is generated");
		// }catch(BadCredentialsException e){
		// returnValue="4";
		// e.printStackTrace();
		// }

		if (role.equalsIgnoreCase("user")) {
			returnValue = "1";
		} else if (role.equalsIgnoreCase("admin")) {
			returnValue = "2";
		} else if (role.equalsIgnoreCase("superadmin")) {
			returnValue = "3";
		} else {
			returnValue = "4";
		}

		// ResponseEntity.status(200);

		// ResponseEntity<T> responsEntity = new ResponseEntity<T>();
		response.setStatus(200);
		// response.encodeRedirectUrl("/layout.html#/admin");
		return returnValue;

	}

	@RequestMapping(value = "/api/logoffUser", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public int userLogoff(@CookieValue("XSRF-TOKEN") String token, HttpServletRequest request,
			HttpServletResponse response) {

		String query = "delete from currentlogonstatus where token='" + token + "'";
		System.out.println("quer is ---------------" + query);

		Cookie cookie = new Cookie("XSRF-TOKEN", null);
		cookie.setMaxAge(0);
		cookie.setPath("/");
		response.addCookie(cookie);

		System.out.println("cookie  is ---------deleted------");

		jdbcTemplate.execute(query);
		return 200;
	}

	private boolean authenticate(String userName, String password, HttpServletResponse response)
			throws BadCredentialsException {

		boolean result;

		String query = "select count(*) from userlogoninfo where username='" + userName + "' and password='" + password
				+ "'";

		int i = 0;
		try {
			i = jdbcTemplate.queryForObject(query, Integer.class);
			System.out.println(query);
		} catch (EmptyResultDataAccessException e) {
			response.setStatus(404);
			System.out.println("Result set in authentication is empty");
		}
		if (i != 1) {

			Cookie cookie = new Cookie("XSRF-TOKEN", null);
			cookie.setMaxAge(0);
			cookie.setPath("/");
			response.addCookie(cookie);

			throw new BadCredentialsException();
		} else {
			result = true;
		}

		return result;
	}

	private String getRole(String userName) {

		boolean result;

		String query = "select roleid from userlogoninfo where username='" + userName + "'";

		String role = null;

		try {
			role = jdbcTemplate.queryForObject(query, String.class);
			System.out.println(query);
		} catch (EmptyResultDataAccessException e) {
			System.out.println("Empty result set");
		}
		return role;

	}

	private HttpServletResponse generateToken(String userName, String role, HttpServletRequest request,
			HttpServletResponse response) {

		String token;

		SessionIdentifierGenerator sessionIdentifierGenerator = new SessionIdentifierGenerator();
		token = sessionIdentifierGenerator.nextSessionId();
		System.out.println("Token is created" + token);
		String query = "insert into currentlogonstatus (userid, roleid, token)values ('" + userName + "','" + role
				+ "','" + token + "')";

		System.out.println("query is created" + query);

		jdbcTemplate.update(query);

		System.out.println("query is created" + query);
		// jdbcTemplate.execute

		/*
		 * Cookie ck[] = request.getCookies();
		 * 
		 * for(int i=0;i<ck.length;i++){
		 * 
		 * System.out.println(ck[0].getName());
		 * 
		 * }
		 */

		// HttpCookie cookie = new HttpCookie("token", token);
		Cookie cookie = new Cookie("XSRF-TOKEN", token);
		cookie.setMaxAge(60 * 60); // (s)
		cookie.setPath("/");
		cookie.setHttpOnly(true);
		response.addCookie(cookie);
		System.out.println("Token is set");
		return response;

	}

	private void updateLogonInfo(String userName) {

		String query1 = "select currentlogon from userlogoninfo where username='" + userName + "'";

		Date currentDate = new Date();

		String currentDateString = (currentDate.getYear() - 100 + 2000) + "-" + (currentDate.getMonth() + 1) + "-"
				+ currentDate.getDate() + " " + currentDate.getHours() + ":" + currentDate.getMinutes() + ":"
				+ currentDate.getSeconds();

		String query2 = "update userlogoninfo set currentlogon ='" + currentDateString + "' where username='" + userName
				+ "'";

		Date lastLogonDate = null; 
		
		try{
		lastLogonDate= jdbcTemplate.queryForObject(query1, Date.class);
		}catch(Exception e){
			
		}
		System.out.println("query 1 is " + query1);

		System.out.println("query 2 is " + query2);

		if (lastLogonDate != null) {
			String dateString = (lastLogonDate.getYear() - 100 + 2000) + "-" + (lastLogonDate.getMonth() + 1) + "-"
					+ lastLogonDate.getDate() + " " + lastLogonDate.getHours() + ":" + lastLogonDate.getMinutes() + ":"
					+ lastLogonDate.getSeconds();

			String query3 = "update userlogoninfo set lastlogon ='" + dateString + "' where username='" + userName
					+ "'";

			System.out.println("query 3 is " + query3);
			jdbcTemplate.update(query3);
		} else {
			String query3 = "update userlogoninfo set lastlogon ='" + currentDateString + "' where username='"
					+ userName + "'";

			System.out.println("query 3 is " + query3);
			jdbcTemplate.update(query3);
		}
		jdbcTemplate.update(query2);

	}

	@ExceptionHandler(value = NumberFormatException.class)
	public String nfeHandler(NumberFormatException e) {
		return e.getMessage();
	}

	/*
	 * @ResponseStatus(org.springframework.http.HttpStatus.UNAUTHORIZED)
	 * 
	 * @ExceptionHandler(value = BadCredentialsException.class) public String
	 * badCredentialsHandler(BadCredentialsException e) {
	 * 
	 * response.sendError(HttpStatus.UNAUTHORIZED.value());
	 * 
	 * return "401"; }
	 */ @ResponseStatus(org.springframework.http.HttpStatus.UNAUTHORIZED)
	@ExceptionHandler({ BadCredentialsException.class })
	void handleUnAuthorizeRequest(HttpServletResponse response) throws IOException {
		response.sendError(org.springframework.http.HttpStatus.UNAUTHORIZED.value());
	}
}
