package com.example.controller;

import java.io.IOException;
import java.util.List;

import javax.naming.AuthenticationException;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.example.exception.AuthorizationException;
import com.example.exception.UnAuthenticatedRequestException;
import com.example.model.BMCCellDescription;
import com.example.model.ProjectDescription;
import com.example.model.ProjectInfo;
import com.example.util.SecurityContext;

@RestController
public class BMCInfoController {
	
	@Autowired 
	JdbcTemplate jdbcTemplate;

	/*@Autowired
	SecurityContext securityContext;
	*/
	private String role[] = {"user","admin"};
	
	@RequestMapping(value = "/api/updateBMCCellDescription",method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public int updateBMCCellDescription(@RequestBody BMCCellDescription bmcCellDescription,@CookieValue("XSRF-TOKEN") String token)throws UnAuthenticatedRequestException, AuthorizationException {
		
		int countOfRecords = 0;
		
		String activeProject = getActiveProjectId(token);
		
		try{
			countOfRecords = jdbcTemplate.queryForObject(
				"select count(*) from bmccelldescription where projectid  = '" + activeProject + "' and cellname = '"+ bmcCellDescription.getCellName()+"'",
				Integer.class);
		}catch(Exception e){
			
		}
		String str;
		if (countOfRecords == 0) {
			str = "Insert into bmccelldescription (cellname , celldescription ,projectid) values('"
					+ bmcCellDescription.getCellName() + "' , '"+bmcCellDescription.getDescription()+"','" +activeProject + "')";
		} else {
			str = "update bmccelldescription set celldescription = '" + bmcCellDescription.getDescription()
					+ "' where " + "projectid  ='" + activeProject + "' and cellname='"+bmcCellDescription.getCellName()+"'";
		}
		System.out.println("Str----------" + str);
		jdbcTemplate.update(str);
		System.out.println("updateVisionDescription----------" + "updateVisionDescription--success");
		return 200;

	}
	
	//getBMCCellDescription
	@RequestMapping(value = "/api/getBMCCellDescription/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public BMCCellDescription getBMCCellDescription(@PathVariable("id") String id,@CookieValue("XSRF-TOKEN") String token ) throws UnAuthenticatedRequestException, AuthorizationException{

		BMCCellDescription cellDescription = new BMCCellDescription();

		try {
			
			String str = "select celldescription from bmccelldescription where projectid='"	+ getActiveProjectId(token) + "' and cellname='"+id+"'";
			
			System.out.println(str);
			
			String description = jdbcTemplate
					.queryForObject(str, String.class);
			
			cellDescription.setDescription(description);
		} catch (Exception e) {

		}
		System.out.println("ideaDescription---------" + cellDescription);

		
		return cellDescription;
	}
	
	
	private String getActiveProjectId(String token) throws UnAuthenticatedRequestException, AuthorizationException{
		
		if(!isAuthenticated(token)){
			throw new UnAuthenticatedRequestException();
		}else if(!isAuthorized(token, role)){
			throw new AuthorizationException();
		}	
		
		String activeProject = getActiveProjectIdByToken(token);
		
		/*String activeProject = jdbcTemplate.queryForObject("select activeproject from userinfo where userid= '1111'",
				String.class);
*/
		return activeProject;
	}
	
	
	private boolean isAuthenticated(String token) {
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

	private boolean isAuthorized(String token, String[] role) {
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

	private String getUserId(String token) {
		
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

	private String getActiveProjectIdByToken(String token) {
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

	private String getActiveProjectIdByUserId(String userid) {
		
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
	
	@ExceptionHandler({IllegalArgumentException.class, NullPointerException.class})
	void handleBadRequests(HttpServletResponse response) throws IOException {
	    response.sendError(HttpStatus.BAD_REQUEST.value());
	}
	
	@ExceptionHandler({UnAuthenticatedRequestException.class})
	void handleUnAuthenticatedRequest(HttpServletResponse response) throws IOException {
	    response.sendError(HttpStatus.NETWORK_AUTHENTICATION_REQUIRED.value());
	}

	@ExceptionHandler({AuthorizationException.class})
	void handleUnAuthorizeRequest(HttpServletResponse response) throws IOException {
	    response.sendError(HttpStatus.UNAUTHORIZED.value());
	}


}
