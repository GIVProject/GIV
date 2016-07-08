package com.example.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.example.exception.AuthorizationException;
import com.example.exception.UnAuthenticatedRequestException;
import com.example.model.BMCCellDescription;
import com.example.model.CostNRevenue;
import com.example.model.CumulativeCostModel;
import com.example.util.SecurityContext;

@RestController
public class FinancialDataController {

	@Autowired
	JdbcTemplate jdbcTemplate;
	
	
	private String role[] = {"user","admin"};

	@RequestMapping(value = "/api/updateCostNRevenue", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public int updateCostNRevenue(@RequestHeader(value="User-Agent")  String userAgent, @RequestBody CostNRevenue costNRevenue,@CookieValue("XSRF-TOKEN") String token )throws UnAuthenticatedRequestException, AuthorizationException {

		String activeProject = getActiveProjectId(token);
		
		int countOfRecords = 0;
		
		try{
			countOfRecords = jdbcTemplate.queryForObject("select count(*) from costNRevenue where projectid  = '"
				+ activeProject + "' and year = '" + costNRevenue.getYear() + "'", Integer.class);
		}catch(Exception e){
			
		}
		String str;
		if (countOfRecords == 0) {
			str = "Insert into costNRevenue (projectid, year, cost, revenue) values('" + activeProject
					+ "' , '" + costNRevenue.getYear() + "','" + costNRevenue.getCost() + "','"
					+ costNRevenue.getRevenue() + "')";
		} else {
			str = "update costNRevenue set cost = '" + costNRevenue.getCost() + "', revenue='"
					+ costNRevenue.getRevenue() + "' where " + "projectid  ='" + activeProject
					+ "' and year='" + costNRevenue.getYear() + "'";
		}
		System.out.println("Str----------" + str);
		jdbcTemplate.update(str);
		calculateNPV(getActiveProjectId(token));
		System.out.println("updateVisionDescription----------" + "updateVisionDescription--success");
		return 200;

	}

	// getBMCCellDescription
	@RequestMapping(value = "/api/getCostNRevenue", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public List getBMCCellDescription(@CookieValue("XSRF-TOKEN") String token ) throws UnAuthenticatedRequestException, AuthorizationException {

		List list = new ArrayList();

		try {

			String str = "select year,cost,revenue from costNRevenue where projectid='" + getActiveProjectId(token)
					+ "'";

			System.out.println(str);

			list = jdbcTemplate.queryForList(str);

		} catch (Exception e) {

		}

		return list;
	}
	// updateCumulativeCostValues //getCumulativeCostValues

	@RequestMapping(value = "/api/updateCumulativeCostValues", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public int updateCumulativeCostValues(@RequestBody CumulativeCostModel cumulativeCostValue,@CookieValue("XSRF-TOKEN") String token )  throws UnAuthenticatedRequestException, AuthorizationException {

		String activeProject = getActiveProjectId(token);
		
		int countOfRecords = 0;
		try{
		countOfRecords = jdbcTemplate.queryForObject(
				"select count(*) from cumulativecost where projectid  = '" +activeProject + "' and year = '"
						+ cumulativeCostValue.getYear() + "' and month='" + cumulativeCostValue.getMonth() + "'",
				Integer.class);
		}catch(Exception e){
			
		}
		String str;
		if (countOfRecords == 0) {
			str = "Insert into cumulativecost (projectid, year, month, cost) values('" + activeProject
					+ "' , '" + cumulativeCostValue.getYear() + "','" + cumulativeCostValue.getMonth() + "','"
					+ cumulativeCostValue.getCost() + "')";
		} else {
			str = "update cumulativecost set cost = '" + cumulativeCostValue.getCost() + "' where " + "projectid  ='"
					+ activeProject + "' and year='" + cumulativeCostValue.getYear() + "' and month='"
					+ cumulativeCostValue.getMonth() + "'";
		}
		System.out.println("Str----------" + str);
		jdbcTemplate.update(str);
		System.out.println("updateVisionDescription----------" + "updateVisionDescription--success");
		return 200;

	}

	@RequestMapping(value = "/api/getCumulativeCostValues", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public List getCumulativeCostValues(@CookieValue("XSRF-TOKEN") String token ) throws UnAuthenticatedRequestException, AuthorizationException{

		List list = new ArrayList();

		try {

			String str = "select year,month,cost from cumulativecost where projectid='" + getActiveProjectId(token)
					+ "'";

			System.out.println(str);

			list = jdbcTemplate.queryForList(str);

		} catch (Exception e) {

		}

		return list;
	}

	@RequestMapping(value = "/api/getrNPVData", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public List getrNPVData(@CookieValue("XSRF-TOKEN") String token ) throws UnAuthenticatedRequestException, AuthorizationException{

		List list = new ArrayList();

		try {

			String str = "select phasename,rnvp from projectphasedata where projectid='" + getActiveProjectId(token)
					+ "'";

			System.out.println(str);

			list = jdbcTemplate.queryForList(str);

		} catch (Exception e) {

		}

		return list;
	}
	
	
	private String getActiveProjectId(String token) throws UnAuthenticatedRequestException, AuthorizationException {
		
	
		

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

	private String calculateNPV(String projectId){

		
		try{
		String str = "Select * from costNRevenue where projectid='"+projectId+"' order by year";

		List<CostNRevenue> list = jdbcTemplate.query(str,new BeanPropertyRowMapper<CostNRevenue>(CostNRevenue.class));

		System.out.println(" Got the list -------------------------------------" + list.size());
		
	/*	List<Customer> customers  = getJdbcTemplate().query(sql,
				new BeanPropertyRowMapper(Customer.class));*/
		
		
	
		CostNRevenue costNRevenueModel = new CostNRevenue();
		double sum = 0;
		for(int i=0;i<list.size();i++){
			costNRevenueModel = (CostNRevenue)list.get(i);
			
			System.out.println(" costNRevenueModel.year------------------------------------" + costNRevenueModel.getYear());
			System.out.println(" costNRevenueModel.cost------------------------------------" + costNRevenueModel.getCost());
			System.out.println(" costNRevenueModel.revenue------------------------------------" + costNRevenueModel.getRevenue());
			
			double revenue = Double.valueOf(costNRevenueModel.getRevenue());
			double expenses = Double.valueOf(costNRevenueModel.getCost());

			double multiplyer = Math.pow(1.3, i+1);
			
			System.out.println(" multiplyer------------------------------------" + multiplyer);			
			
			double diff = revenue-expenses;
			
			System.out.println(" revenue-expenses------------------------------------" + diff);
			
			double npv = diff/multiplyer;
			sum = sum+npv;
		}

		System.out.println(" Sum is calculated ----------------------------------" + sum);
		
		if(list.size()>0){

		int countOfRecords = jdbcTemplate.queryForObject("select count(*) from costNRevenue where projectid  = '"
				+ projectId + "'", Integer.class);

		String queryString;
		if(countOfRecords==0){
			queryString = "Insert into projectriskdata(projectid,npvvalue) values('"+projectId +"','"+ sum +"')";
			
			System.out.println(" queryString is  ----------------------------------" + queryString);
			
		}else{
			queryString ="Update projectriskdata set npvvalue ='"+sum+"'";
			System.out.println(" queryString is  ----------------------------------" + queryString);
		}
		jdbcTemplate.update(queryString);
}
		}catch(Exception e){
			e.printStackTrace();
		}
		return "success";
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
	
	@ExceptionHandler({UnAuthenticatedRequestException.class})
	void handleUnAuthenticatedRequest(HttpServletResponse response) throws IOException {
	    response.sendError(HttpStatus.NETWORK_AUTHENTICATION_REQUIRED.value());
	}

	@ExceptionHandler({AuthorizationException.class})
	void handleUnAuthorizeRequest(HttpServletResponse response) throws IOException {
	    response.sendError(HttpStatus.UNAUTHORIZED.value());
	}


}
