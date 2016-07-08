package com.example.controller;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.auditing.CurrentDateTimeProvider;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.example.exception.UnAuthenticatedRequestException;
import com.example.model.CostNRevenue;
import com.example.model.DepartmentDataList;
import com.example.model.ProductConfiguration;
import com.example.model.ProductFeatures;
import com.example.model.ProjectIdModel;
import com.example.model.ProjectPhaseInfo;
import com.example.model.ProjectRiskData;
import com.example.model.ReportModel;
import com.example.model.RiskData;
import com.example.util.SecurityContext;

@RestController
public class InnovationManagerReportController {

	@Autowired
	JdbcTemplate jdbcTemplate;

	private String role[] = { "admin" };

	@RequestMapping(value = "/api/getReportData", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public List getReportData(@CookieValue("XSRF-TOKEN") String token) throws UnAuthenticatedRequestException {

		if (!isAuthenticated(token) || !isAuthorized(token, role)) {
			throw new UnAuthenticatedRequestException();
		}

		String userId = getUserId(token);

		List list = new ArrayList();
		List projectList = new ArrayList<ReportModel>();
		List<ProductFeatures> productFeaturesList = new ArrayList<ProductFeatures>();

		String portfolioId = "";
		String innovatorId = "";
		String projectId = "";
		ReportModel reportModel = new ReportModel();

		try {

			// String str = "select portfolioid from portfoliousermaster where
			// userid='" + userID + "'";

			portfolioId = getPortfolioId(userId);
			projectList = getProjectList(portfolioId);
			// productConfigurationList = getProductConfigurationList()
			try {
				for (int i = 0; i < projectList.size(); i++) {
					Map departmentInfoMap = new HashMap();
					ProductFeatures productFeatures = new ProductFeatures();
					// productConfigurationList.add(i,(ProductConfiguration)
					// getProductConfiguration(((ReportModel)projectList.get(i)).getProjectId()));
					String projectid = ((ReportModel) projectList.get(i)).getProjectId();
					System.out.println("---------------------------projectid " + projectid);

					try {
						productFeaturesList = getProductFeaturesList(projectid);
						((ReportModel) projectList.get(i)).setProductFeatures(productFeaturesList.get(0));
					} catch (Exception e) {
						e.printStackTrace();
					}
					// System.out.println(productFeaturesList.get(0).getCustomer());

					// ((ReportModel)projectList.get(i)).setProductConfiguration((ProductConfiguration)productConfigurationList.get(i));

					String innovatorName = getInnovatorName(((ReportModel) projectList.get(i)).getInnovator());
					((ReportModel) projectList.get(i)).setUserName(innovatorName);

					String currentProjectPhase = getCurrentProjectPhase(projectid);
					((ReportModel) projectList.get(i)).setProjectCurrentPhase(currentProjectPhase);

					((ReportModel) projectList.get(i)).setRiskDataList(getRiskData(projectid));

					((ReportModel) projectList.get(i)).setProjectRiskDataList(getProjectRiskData(projectid));

					((ReportModel) projectList.get(i))
							.setDepartmentInfoMap(getDepartmentData(projectid, departmentInfoMap));

					((ReportModel) projectList.get(i)).setProjectPhaseInfoMap(getProjectPhaseData(projectid));
					// getProjectPhaseData(projectid);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

			/*
			 * String str1 =
			 * "select innovator from projectmaster where porfolioid='" +
			 * portfolioId + "'";
			 * 
			 * String str2 = "Select username from userinfo where userid ='" +
			 * innovatorId + "'";
			 * 
			 * String str3 =
			 * "select projectid from projectmaster where userid='" +
			 * innovatorId + "' and portfolioid='" + portfolioId + "'";
			 * 
			 * String str4 = "Select * from ideaconfiguration where userid ='" +
			 * projectId + "'";
			 */

			// System.out.println(str);

			// list = jdbcTemplate.queryForList(str);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return projectList;
	}

	@RequestMapping(value = "/api/progressProject", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public int progressProject(@CookieValue("XSRF-TOKEN") String token, @RequestBody ProjectIdModel projectIdModel)
			throws UnAuthenticatedRequestException {

		if (!isAuthenticated(token) || !isAuthorized(token, role)) {
			throw new UnAuthenticatedRequestException();
		}
		calculateNPV(projectIdModel.getProjectId());
		System.out.println("projectId-------------------" + projectIdModel.getProjectId());

		return 200;
	}

	@RequestMapping(value = "/api/killProject", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public int killProject(@CookieValue("XSRF-TOKEN") String token, @RequestBody ProjectIdModel projectIdModel)
			throws UnAuthenticatedRequestException {
		if (!isAuthenticated(token) || !isAuthorized(token, role)) {
			throw new UnAuthenticatedRequestException();
		}

		String query = "update projectmaster set projectstatus='kill' where projectid='" + projectIdModel.getProjectId()
				+ "'";

		jdbcTemplate.update(query);
		System.out.println("projectId-------------------" + projectIdModel.getProjectId());

		return 200;
	}

	@RequestMapping(value = "/api/flagProject", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public int flagProject(@CookieValue("XSRF-TOKEN") String token, @RequestBody ProjectIdModel projectIdModel)
			throws UnAuthenticatedRequestException {
		if (!isAuthenticated(token) || !isAuthorized(token, role)) {
			throw new UnAuthenticatedRequestException();
		}

		String query = "update projectmaster set projectstatus='flag' where projectid='" + projectIdModel.getProjectId()
				+ "'";

		jdbcTemplate.update(query);

		System.out.println("projectId-------------------" + projectIdModel.getProjectId());

		return 200;
	}

	@RequestMapping(value = "/api/setActiveProject", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public int setActiveProject(@CookieValue("XSRF-TOKEN") String token, @RequestBody ProjectIdModel projectIdModel)
			throws UnAuthenticatedRequestException {
		if (!isAuthenticated(token) || !isAuthorized(token, role)) {
			throw new UnAuthenticatedRequestException();
		}

		String userid = getUserId(token);

		String query = "update userinfo set activeproject='" + projectIdModel.getProjectId() + "' where userid='"
				+ userid + "'";

		jdbcTemplate.update(query);

		System.out.println("projectId-------------------" + projectIdModel.getProjectId());

		return 200;
	}

	private String getPortfolioId(String userId) {

		String str = "select portfolioid from portfoliousermaster where userid='" + userId + "'";

		System.out.println(str);

		String portfoilioId = jdbcTemplate.queryForObject(str, String.class);

		return portfoilioId;
	}

	private List getProjectList(String portfolioId) {

		String str = "select projectid,projectname,projectstatus,innovator,startdate,currentprojectphase from projectmaster where portfolioId='"
				+ portfolioId + "'";

		System.out.println(str);
		// List projectList = jdbcTemplate.queryForList(str);
		List<ReportModel> projectList = jdbcTemplate.query(str,
				new BeanPropertyRowMapper<ReportModel>(ReportModel.class));

		return projectList;
	}

	private List<ProductFeatures> getProductFeaturesList(String projectId) {

		String str = "select productfeature,customer,market,ways,technology from ideaconfiguration where projectId='"
				+ projectId + "'";

		System.out.println(str);
		// List projectList = jdbcTemplate.queryForList(str);
		List<ProductFeatures> productConfiguration = jdbcTemplate.query(str,
				new BeanPropertyRowMapper<ProductFeatures>(ProductFeatures.class));

		return productConfiguration;
	}

	private String getInnovatorName(String userid) {

		String str = "select username from userinfo where userid='" + userid + "'";

		System.out.println(str);
		// List projectList = jdbcTemplate.queryForList(str);
		String userName = jdbcTemplate.queryForObject(str, String.class);

		return userName;
	}

	private String getCurrentProjectPhase(String projectid) {
		String query = "select currentprojectphase from projectmaster where projectid='" + projectid + "'";

		String currentProjectPhase = jdbcTemplate.queryForObject(query, String.class);

		return currentProjectPhase;
	}

	private List<ProjectRiskData> getProjectRiskData(String projectid) {

		String str = "select * from projectriskdata where projectid='" + projectid + "'";

		System.out.println(str);
		List<ProjectRiskData> projectRiskdataList = jdbcTemplate.query(str,
				new BeanPropertyRowMapper<ProjectRiskData>(ProjectRiskData.class));

		return projectRiskdataList;
	}

	private List<RiskData> getRiskData(String projectid) {

		List<RiskData> riskDataList = new ArrayList<RiskData>();

		RiskData riskData = new RiskData();

		String query1 = "select count(impact) from bmcinfo where projectid='" + projectid + "' and impact='high'";

		String query2 = "select count(impact) from bmcinfo where projectid='" + projectid + "' and impact='medium'";

		System.out.println("query2===============" + query2);

		String query3 = "select count(impact) from bmcinfo where projectid='" + projectid + "' and impact='low'";

		System.out.println("query3===============" + query3);

		String query4 = "select count(impact) from bmcinfo where projectid='" + projectid + "' and impact='very high'";

		System.out.println("query4===============" + query4);

		String query5 = "select count(colorofattribute) from bmcinfo where projectid='" + projectid + "'";

		System.out.println("query5===============" + query5);

		int highRisk = jdbcTemplate.queryForObject(query1, Integer.class);

		System.out.println("query1===============" + query1 + "result " + highRisk);

		int mediumRisk = jdbcTemplate.queryForObject(query2, Integer.class);

		System.out.println("query2===============" + query2 + "result " + mediumRisk);

		int lowRisk = jdbcTemplate.queryForObject(query3, Integer.class);

		System.out.println("query3===============" + query3 + "result " + lowRisk);

		int veryHighRisk = jdbcTemplate.queryForObject(query4, Integer.class);

		System.out.println("query4===============" + query4 + "result " + veryHighRisk);

		int totalRecords = jdbcTemplate.queryForObject(query5, Integer.class);

		System.out.println("query4===============" + query5 + "result " + totalRecords);

		int undefinedRisks = totalRecords - (highRisk + mediumRisk + lowRisk + veryHighRisk);

		riskData.setHighRisk(highRisk);
		riskData.setMediumRisk(mediumRisk);
		riskData.setLowRisk(lowRisk);
		riskData.setVeryHighRisk(veryHighRisk);
		riskData.setUndefinedRisks(undefinedRisks);

		riskDataList.add(riskData);

		return riskDataList;
	}

	private Map getDepartmentData(String projectid, Map departmentInfo) {

		DepartmentDataList departmentDataList = new DepartmentDataList();

		String query = "select userid from projectusermaster where projectid='" + projectid + "'";

		List<String> listOfUserId = jdbcTemplate.queryForList(query, String.class);

		System.out.println(query + "  listOfUserId " + listOfUserId.size());

		for (int i = 0; i < listOfUserId.size(); i++) {
			String userID = listOfUserId.get(i).toString();
			String query2 = "select departmentid from userinfo where userid='" + userID + "'";
			System.out.println("query2 ==========" + query2);
			try {
				String departmentId = jdbcTemplate.queryForObject(query2, String.class);

				System.out.println(query2 + "  departmentId " + departmentId);
				if (departmentInfo.containsKey(departmentId)) {
					int count = Integer.parseInt(departmentInfo.get(departmentId).toString());
					count++;
					departmentInfo.put(departmentId, count);
				} else {
					departmentInfo.put(departmentId, 1);
				}
			} catch (Exception e) {
				System.out.println("Sollow the empty result set exception");
			}

		}

		return departmentInfo;
	}

	private Map getProjectPhaseData(String projectid) {

		ProjectPhaseInfo projectPhaseInfo = new ProjectPhaseInfo();

		Map dataMap = new HashMap();

		String query = "select * from projectphasedata where projectid='" + projectid + "'";

		List<ProjectPhaseInfo> list = jdbcTemplate.query(query,
				new BeanPropertyRowMapper<ProjectPhaseInfo>(ProjectPhaseInfo.class));

		System.out.println("size is========================= " + list.size());

		Calendar calendar1 = Calendar.getInstance();
		Calendar calendar2 = Calendar.getInstance();

		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).getEndDate() == null) {
				list.get(i).setEndDate(new Date());
			}
			System.out.println(" getPhaseid is =============" + list.get(i).getPhaseid());
			System.out.println(" getPhaseName is =============" + list.get(i).getPhaseName());
			System.out.println(" getStartDate is =============" + list.get(i).getStartDate().toString());
			System.out.println(" getEndDate is =============" + list.get(i).getEndDate().toString());

			calendar1.set(list.get(i).getStartDate().getYear(), list.get(i).getStartDate().getMonth(),
					list.get(i).getStartDate().getDay());
			calendar2.set(list.get(i).getEndDate().getYear(), list.get(i).getEndDate().getMonth(),
					list.get(i).getEndDate().getDay());

			long miliSecondForDate1 = calendar1.getTimeInMillis();
			long miliSecondForDate2 = calendar2.getTimeInMillis();

			long diffInMilis = miliSecondForDate2 - miliSecondForDate1;

			double diffDays = diffInMilis / (24 * 3600 * 1000);

			list.get(i).setDuration(diffDays);

			System.out.println(" miliSecondForDate1 is --------------" + miliSecondForDate1);
			System.out.println(" miliSecondForDate2 is --------------" + miliSecondForDate2);
			System.out.println(" Difference is --------------" + diffInMilis);
			System.out.println(" Difference diffDays --------------" + diffDays);

			dataMap.put(list.get(i).getPhaseid(), diffDays);
			dataMap.put("projectCurrentPhase", list.get(i).getPhaseName());
			/*
			 * else{ dataMap.put("Idea description", 0); }
			 */
		}

		return dataMap;

	}

	private boolean isAuthenticated(String token) {
		boolean result = false;

		if (jdbcTemplate == null) {
			System.out.println("JDBC template is null inside SecurityContext===============");
		}

		String query = "select token from currentlogonstatus where token='" + token + "'";

		System.out.println("query is ------------" + query);

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
		if (jdbcTemplate == null) {
			System.out.println("JDBC template is null inside SecurityContext===============");
		}
		String query = "Select roleid from currentlogonstatus where token='" + token + "'";

		System.out.println("query is ------------" + query);

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

		if (jdbcTemplate == null) {
			System.out.println("JDBC template is null inside SecurityContext===============");
		}
		String userid = null;
		String username = "";
		String query = "Select userid from currentlogonstatus where token='" + token + "'";

		System.out.println("query is ------------" + query);

		try {
			username = jdbcTemplate.queryForObject(query, String.class);

			String query1 = "select userid from userinfo where username='" + username + "'";

			System.out.println("query1 is ------------" + query1);
			System.out.println("username is ------------" + username);

			userid = jdbcTemplate.queryForObject(query1, String.class);

			System.out.println("userid is ------------" + userid);
		} catch (EmptyResultDataAccessException e) {

		} catch (Exception e) {
			e.printStackTrace();
		}

		return userid;

	}

	private String getActiveProjectIdByToken(String token) {
		if (jdbcTemplate == null) {
			System.out.println("JDBC template is null inside SecurityContext===============");
		}

		String projectid = null;
		String username = "";
		String query = "Select userid from currentlogonstatus where token='" + token + "'";

		System.out.println("query is ------------" + query);

		try {
			username = jdbcTemplate.queryForObject(query, String.class);

			String query1 = "select activeproject from userinfo where username='" + username + "'";

			projectid = jdbcTemplate.queryForObject(query1, String.class);

		} catch (EmptyResultDataAccessException e) {

		}

		return projectid;

	}

	private String getActiveProjectIdByUserId(String userid) {

		if (jdbcTemplate == null) {
			System.out.println("JDBC template is null inside SecurityContext===============");
		}
		String projectid = null;

		String query = "select activeproject from userinfo where username='" + userid + "'";

		System.out.println("query is ------------" + query);

		try {
			projectid = jdbcTemplate.queryForObject(query, String.class);

		} catch (EmptyResultDataAccessException e) {

		}
		return projectid;
	}

	private String calculateNPV(String projectId) {

		try {
			String str = "Select * from costNRevenue where projectid='" + projectId + "' order by year";

			List<CostNRevenue> list = jdbcTemplate.query(str,
					new BeanPropertyRowMapper<CostNRevenue>(CostNRevenue.class));

			System.out.println(" Got the list -------------------------------------" + list.size());

			/*
			 * List<Customer> customers = getJdbcTemplate().query(sql, new
			 * BeanPropertyRowMapper(Customer.class));
			 */

			CostNRevenue costNRevenueModel = new CostNRevenue();
			double sum = 0;
			for (int i = 0; i < list.size(); i++) {
				costNRevenueModel = (CostNRevenue) list.get(i);

				System.out.println(
						" costNRevenueModel.year------------------------------------" + costNRevenueModel.getYear());
				System.out.println(
						" costNRevenueModel.cost------------------------------------" + costNRevenueModel.getCost());
				System.out.println(" costNRevenueModel.revenue------------------------------------"
						+ costNRevenueModel.getRevenue());

				double revenue = Double.valueOf(costNRevenueModel.getRevenue());
				double expenses = Double.valueOf(costNRevenueModel.getCost());

				double multiplyer = Math.pow(1.3, i + 1);

				System.out.println(" multiplyer------------------------------------" + multiplyer);

				double diff = revenue - expenses;

				System.out.println(" revenue-expenses------------------------------------" + diff);

				double npv = diff / multiplyer;
				sum = sum + npv;
			}

			System.out.println(" Sum is calculated ----------------------------------" + sum);

			DecimalFormat df = new DecimalFormat("#.##");
			String formatted = df.format(sum);
			System.out.println(formatted);

			if (list.size() > 0) {

				int countOfRecords = jdbcTemplate.queryForObject(
						"select count(*) from costNRevenue where projectid  = '" + projectId + "'", Integer.class);

				String queryString;
				if (countOfRecords == 0) {
					queryString = "Insert into projectriskdata(projectid,npvvalue) values('" + projectId + "','"
							+ formatted + "')";

					System.out.println(" queryString is  ----------------------------------" + queryString);

				} else {
					queryString = "Update projectriskdata set npvvalue ='" + formatted + "'";
					System.out.println(" queryString is  ----------------------------------" + queryString);
				}
				jdbcTemplate.update(queryString);
			}

			String query = "select currentprojectphase from projectmaster where projectid='" + projectId + "'";

			String currentPhase = jdbcTemplate.queryForObject(query, String.class);

			String updateProjectPhase;
			String updatePhaseData1;
			String updatePhaseData2;
			String insetProjectPhaseData;
			String updateRisk;
			String retrieveRisk;
			String updateRNVP;
			String rnvp;
			double riskValue = 0;

			Date currDate = new Date();
			String currentDate = (currDate.getYear() - 100 + 2000) + "-" + (currDate.getMonth() + 1) + "-"
					+ currDate.getDate();

			switch (currentPhase) {
			case "Idea description":
				updateProjectPhase = "Update projectmaster set currentprojectphase ='Problem validation' where projectid='"
						+ projectId + "'";

				updatePhaseData1 = "update projectphasedata set enddate='" + currentDate + "' where projectid='"
						+ projectId + "' and phasename='" + currentPhase + "'";
				updatePhaseData2 = "update projectphasedata set npvvalue='" + formatted + "' where projectid='"
						+ projectId + "' and phasename='" + currentPhase + "'";

				updateRisk = "update projectphasedata set totalrisk='0' where projectid='" + projectId
						+ "' and phasename='" + currentPhase + "'";

				updateRNVP = "update projectphasedata set rnvp='0' where projectid='" + projectId
						+ "' and phasename='" + currentPhase + "'";
				
				insetProjectPhaseData = "insert into projectphasedata(projectid, phasename, startdate,phaseid) values('"
						+ projectId + "','Problem validation','" + currentDate + "','2')";

				jdbcTemplate.execute(updateProjectPhase);
				jdbcTemplate.execute(updatePhaseData1);
				jdbcTemplate.execute(updatePhaseData2);
				jdbcTemplate.execute(updateRisk);
				jdbcTemplate.execute(updateRNVP);				
				jdbcTemplate.execute(insetProjectPhaseData);

				break;
			case "Problem validation":
				
				retrieveRisk = "select sum(totalimpact) from bmcinfo where projectid='" + projectId + "'";

				riskValue = jdbcTemplate.queryForObject(retrieveRisk, Double.class);

				riskValueFormatter(riskValue);
				rnvp = String.valueOf(calculateRNVP(riskValue, formatted));
				
				updateRisk = "update projectphasedata set totalrisk='"+riskValueFormatter(riskValue)+"' where projectid='" + projectId
						+ "' and phasename='" + currentPhase + "'";

				
				updateProjectPhase = "Update projectmaster set currentprojectphase ='Solution validation' where projectid='"
						+ projectId + "'";
				updatePhaseData1 = "update projectphasedata set enddate='" + currentDate + "' where projectid='"
						+ projectId + "' and phasename='" + currentPhase + "'";

				updatePhaseData2 = "update projectphasedata set npvvalue='" + formatted + "' where projectid='"
						+ projectId + "' and phasename='" + currentPhase + "'";
				
				updateRNVP = "update projectphasedata set rnvp='" + rnvp + "' where projectid='"
						+ projectId + "' and phasename='" + currentPhase + "'";

				
				insetProjectPhaseData = "insert into projectphasedata(projectid, phasename, startdate,phaseid) values('"
						+ projectId + "','Solution validation','" + currentDate + "','3')";

				jdbcTemplate.execute(updateProjectPhase);
				jdbcTemplate.execute(updatePhaseData1);
				jdbcTemplate.execute(updatePhaseData2);
				jdbcTemplate.execute(updateRisk);
				jdbcTemplate.execute(updateRNVP);
				jdbcTemplate.execute(insetProjectPhaseData);

				break;
			case "Solution validation":
				
				retrieveRisk = "select sum(totalimpact) from bmcinfo where projectid='" + projectId + "'";

				riskValue = jdbcTemplate.queryForObject(retrieveRisk, Double.class);

				riskValueFormatter(riskValue);
				rnvp = String.valueOf(calculateRNVP(riskValue, formatted));
				
				updateRisk = "update projectphasedata set totalrisk='"+riskValueFormatter(riskValue)+"' where projectid='" + projectId
						+ "' and phasename='" + currentPhase + "'";
				
				updateProjectPhase = "Update projectmaster set currentprojectphase ='Scaling the demand' where projectid='"
						+ projectId + "'";
				updatePhaseData1 = "update projectphasedata set enddate='" + currentDate + "' where projectid='"
						+ projectId + "' and phasename='" + currentPhase + "'";

				updatePhaseData2 = "update projectphasedata set npvvalue='" + formatted + "' where projectid='"
						+ projectId + "' and phasename='" + currentPhase + "'";

				updateRNVP = "update projectphasedata set rnvp='" + rnvp + "' where projectid='"
						+ projectId + "' and phasename='" + currentPhase + "'";

				
				insetProjectPhaseData = "insert into projectphasedata(projectid, phasename, startdate,phaseid) values('"
						+ projectId + "','Scaling the demand','" + currentDate + "','4')";

				jdbcTemplate.execute(updateProjectPhase);
				jdbcTemplate.execute(updatePhaseData1);
				jdbcTemplate.execute(updatePhaseData2);
				jdbcTemplate.execute(updateRisk);
				jdbcTemplate.execute(updateRNVP);
				jdbcTemplate.execute(insetProjectPhaseData);

				break;
			case "Scaling the demand":
				
				retrieveRisk = "select sum(totalimpact) from bmcinfo where projectid='" + projectId + "'";

				riskValue = jdbcTemplate.queryForObject(retrieveRisk, Double.class);

				riskValueFormatter(riskValue);
				rnvp = String.valueOf(calculateRNVP(riskValue, formatted));
				
				updateRisk = "update projectphasedata set totalrisk='"+riskValueFormatter(riskValue)+"' where projectid='" + projectId
						+ "' and phasename='" + currentPhase + "'";
				
				updateProjectPhase = "Update projectmaster set currentprojectphase ='Scaling the solution' where projectid='"
						+ projectId + "'";
				updatePhaseData1 = "update projectphasedata set enddate='" + currentDate + "' where projectid='"
						+ projectId + "' and phasename='" + currentPhase + "'";

				updatePhaseData2 = "update projectphasedata set npvvalue='" + formatted + "' where projectid='"
						+ projectId + "' and phasename='" + currentPhase + "'";

				updateRNVP = "update projectphasedata set rnvp='" + rnvp + "' where projectid='"
						+ projectId + "' and phasename='" + currentPhase + "'";

				
				insetProjectPhaseData = "insert into projectphasedata(projectid, phasename, startdate,phaseid) values('"
						+ projectId + "','Scaling the solution','" + currentDate + "','5')";

				jdbcTemplate.execute(updateProjectPhase);
				jdbcTemplate.execute(updatePhaseData1);
				jdbcTemplate.execute(updatePhaseData2);
				jdbcTemplate.execute(updateRisk);
				jdbcTemplate.execute(updateRNVP);
				jdbcTemplate.execute(insetProjectPhaseData);

				break;
			case "Scaling the solution":
				
				retrieveRisk = "select sum(totalimpact) from bmcinfo where projectid='" + projectId + "'";

				riskValue = jdbcTemplate.queryForObject(retrieveRisk, Double.class);

				riskValueFormatter(riskValue);
				rnvp = String.valueOf(calculateRNVP(riskValue, formatted));
				
				updateRisk = "update projectphasedata set totalrisk='"+riskValueFormatter(riskValue)+"' where projectid='" + projectId
						+ "' and phasename='" + currentPhase + "'";
				
				updateProjectPhase = "Update projectmaster set currentprojectphase ='Revenue optimization' where projectid='"
						+ projectId + "'";
				updatePhaseData1 = "update projectphasedata set enddate='" + currentDate + "' where projectid='"
						+ projectId + "' and phasename='" + currentPhase + "'";

				updatePhaseData2 = "update projectphasedata set npvvalue='" + formatted + "' where projectid='"
						+ projectId + "' and phasename='" + currentPhase + "'";
				
				updateRNVP = "update projectphasedata set rnvp='" + rnvp + "' where projectid='"
						+ projectId + "' and phasename='" + currentPhase + "'";


				insetProjectPhaseData = "insert into projectphasedata(projectid, phasename, startdate,phaseid) values('"
						+ projectId + "','Revenue optimization','" + currentDate + "','6')";

				jdbcTemplate.execute(updateProjectPhase);
				jdbcTemplate.execute(updatePhaseData1);
				jdbcTemplate.execute(updatePhaseData2);
				jdbcTemplate.execute(updateRisk);
				jdbcTemplate.execute(updateRNVP);
				jdbcTemplate.execute(insetProjectPhaseData);

				break;
			case "Revenue optimization":

			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return "success";
	}

	private String riskValueFormatter(double riskValue) {

		DecimalFormat df = new DecimalFormat("#.##");
		String formatted = df.format(riskValue);
		System.out.println(formatted);

		return formatted;

	}
	
	private double calculateRNVP(double riskValue, String npvValue){
		
		double rnvp = (1-riskValue)*Double.valueOf(npvValue);
		System.out.println("RNVP is ========"+ rnvp);
		return rnvp;
	}

	@ExceptionHandler({ UnAuthenticatedRequestException.class })
	void handleUnAuthenticatedRequest(HttpServletResponse response) throws IOException {
		response.sendError(HttpStatus.NETWORK_AUTHENTICATION_REQUIRED.value());
	}

}
