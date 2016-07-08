package com.example.controller;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.example.exception.AuthorizationException;
import com.example.exception.UnAuthenticatedRequestException;
import com.example.model.LastLogonInfo;
import com.example.model.MVPData;
import com.example.model.ProductConfiguration;
import com.example.model.ProjectDescription;
import com.example.model.ProjectInfo;
import com.example.model.UpdateRequest;
import com.example.model.UserInfo;
import com.example.model.UserProjectList;
import com.example.util.SecurityContext;

//import com.example.model.ProjectInfo;
//@CrossOrigin
@RestController
public class ProjectInfoController {

	@Autowired
	JdbcTemplate jdbcTemplate;

	private String role[] = { "user", "admin" };

	// @CrossOrigin(origins = "http://localhost:9081", maxAge = 3600)
	@RequestMapping(value = "/api/getBMCData/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	// @RequestMapping(value = "/api/getBMCData")
	public List getBMCData(@PathVariable("id") String id, @CookieValue("XSRF-TOKEN") String token)
			throws UnAuthenticatedRequestException, AuthorizationException {
		List listOfRecords = jdbcTemplate.queryForList("Select * from bmcinfo where cellName='" + id
				+ "' and projectid='" + getActiveProjectId(token) + "' order by IndexOfRecord");

		System.out.println("Got the list =============");
		return listOfRecords;

	}

	@RequestMapping(value = "/api/insertBMC", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public int insertBMC(@RequestBody ProjectInfo projectInfo, @CookieValue("XSRF-TOKEN") String token)
			throws UnAuthenticatedRequestException, AuthorizationException {

		/**
		 * create table BMCInfo (ProjectID varchar(255) , CellName varchar(50),
		 * ColorOfAttribute varchar(12), ValueOfAttribute varchar(500));
		 */

		System.out.println("--getCell------" + projectInfo.getCell());
		System.out.println("------getColorOfAttribute--" + projectInfo.getColorOfAttribute());
		System.out.println("-getValueOfAttribute-------" + projectInfo.getValueOfAttribute());
		System.out.println("-getCount-------" + projectInfo.getCount());
		int countOfRecords = 0;
		try {

			countOfRecords = jdbcTemplate.queryForObject(
					"select count(IndexOfRecord) from bmcinfo where projectid='" + getActiveProjectId(token) + "'",
					Integer.class);

			countOfRecords++;

			jdbcTemplate
					.update("Insert into bmcinfo (ProjectID , CellName , ColorOfAttribute , ValueOfAttribute , IndexOfRecord ) VALUES ('"
							+ getActiveProjectId(token) + "','" + projectInfo.getCell() + "','"
							+ projectInfo.getColorOfAttribute() + "','" + projectInfo.getValueOfAttribute() + "','"
							+ countOfRecords + "')");
			System.out.println("insert  is success--------------");

		} catch (Exception e) {
			e.printStackTrace();
		}
		return countOfRecords;
	}

	@RequestMapping(value = "/api/updateBMC", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public int updateBMC(@RequestBody ProjectInfo projectInfo, @CookieValue("XSRF-TOKEN") String token)
			throws UnAuthenticatedRequestException, AuthorizationException {

		/**
		 * create table BMCInfo (ProjectID varchar(255) , CellName varchar(50),
		 * ColorOfAttribute varchar(12), ValueOfAttribute varchar(500));
		 */

		System.out.println("-getCount-------" + projectInfo.getIndex());
		try {

			String str = "UPDATE bmcinfo set CellName ='" + projectInfo.getCell() + "', ColorOfAttribute='"
					+ projectInfo.getColorOfAttribute() + "', ValueOfAttribute='" + projectInfo.getValueOfAttribute()
					+ "' where " + "CellName  = '" + projectInfo.getCell() + "' and " + "IndexOfRecord  = '"
					+ projectInfo.getIndex() + "' and projectid='" + getActiveProjectId(token) + "'";

			System.out.println("Str----------" + str);
			jdbcTemplate.update(str);

			System.out.println("Update is success--------------");

		} catch (Exception e) {
			e.printStackTrace();
		}
		return 100;
	}

	@RequestMapping(value = "/api/getProblemValidationData", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public List getProblemValidationData(@CookieValue("XSRF-TOKEN") String token)
			throws UnAuthenticatedRequestException, AuthorizationException {

		List listOfRecords = jdbcTemplate
				.queryForList("select * from bmcinfo where projectid='" + getActiveProjectId(token) + "'");
		return listOfRecords;
	}

	@RequestMapping(value = "/api/getTotalRiskValue", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public String getTotalRiskValue(@CookieValue("XSRF-TOKEN") String token)
			throws UnAuthenticatedRequestException, AuthorizationException {

		String str = "select sum(totalimpact) from bmcinfo where projectid='" + getActiveProjectId(token) + "'";

		System.out.println(str);
		Double totalRisk = jdbcTemplate.queryForObject(str, Double.class);

		DecimalFormat myFormatter = new DecimalFormat("###.##");
		String value = "";
		try{
		value = myFormatter.format(totalRisk);
		}catch(Exception e){
			
		}
		return value;
	}

	@RequestMapping(value = "/api/updateValidationExperimentValue", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public int updateValidationExperimentValue(@RequestBody ProjectInfo projectInfo,
			@CookieValue("XSRF-TOKEN") String token) throws UnAuthenticatedRequestException, AuthorizationException {

		System.out.println("-getCount-------" + projectInfo.getIndex());
		try {

			String str = "UPDATE bmcinfo set validationexperiment ='" + projectInfo.getValidationExperient()
					+ "' where " + "CellName  = '" + projectInfo.getCell() + "' and " + "IndexOfRecord  = '"
					+ projectInfo.getIndex() + "' and projectid='" + getActiveProjectId(token) + "'";

			System.out.println("Str----------" + str);
			jdbcTemplate.update(str);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return 100;
	}

	@RequestMapping(value = "/api/updateUncertanityValue", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public int updateUncertanityValue(@RequestBody ProjectInfo projectInfo, @CookieValue("XSRF-TOKEN") String token)
			throws UnAuthenticatedRequestException, AuthorizationException {

		System.out.println("-getCount-------" + projectInfo.getIndex());
		String activeProject = getActiveProjectId(token);
		try {

			String str = "UPDATE bmcinfo set uncertanity ='" + projectInfo.getUncertanity() + "' where "
					+ "CellName  = '" + projectInfo.getCell() + "' and " + "IndexOfRecord  = '" + projectInfo.getIndex()
					+ "' and projectid='" + activeProject + "'";

			System.out.println("Str----------" + str);
			jdbcTemplate.update(str);

			String impactValue = retrieveImpactValue(projectInfo.getIndex(), activeProject, projectInfo.getCell());
			if (impactValue != null) {
				updateTotalImpact(projectInfo.getUncertanity(), impactValue, activeProject, projectInfo.getCell(),
						projectInfo.getIndex());
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return 100;
	}

	@RequestMapping(value = "/api/updateImpactValue", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public int updateImpactValue(@RequestBody ProjectInfo projectInfo, @CookieValue("XSRF-TOKEN") String token)
			throws UnAuthenticatedRequestException, AuthorizationException {

		System.out.println("-getCount-------" + projectInfo.getIndex());
		String activeProject = getActiveProjectId(token);
		try {

			String str = "UPDATE bmcinfo set impact ='" + projectInfo.getImpact() + "' where " + "CellName  = '"
					+ projectInfo.getCell() + "' and " + "IndexOfRecord  = '" + projectInfo.getIndex()
					+ "' and projectid='" + activeProject + "'";

			System.out.println("Str----------" + str);
			jdbcTemplate.update(str);

			String uncertanityValue = retrieveUncertanityValue(projectInfo.getIndex(), activeProject,
					projectInfo.getCell());
			if (uncertanityValue != null) {
				updateTotalImpact(uncertanityValue, projectInfo.getImpact(), activeProject, projectInfo.getCell(),
						projectInfo.getIndex());
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return 100;
	}

	@RequestMapping(value = "/api/updateValidationPlanValue", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public int updateValidationPlanValue(@RequestBody ProjectInfo projectInfo, @CookieValue("XSRF-TOKEN") String token)
			throws UnAuthenticatedRequestException, AuthorizationException {

		System.out.println("-getCount-------" + projectInfo.getIndex());
		try {

			String str = "UPDATE bmcinfo set validationplan ='" + projectInfo.getValidationPlan() + "' where "
					+ "CellName  = '" + projectInfo.getCell() + "' and " + "IndexOfRecord  = '" + projectInfo.getIndex()
					+ "' and projectid='" + getActiveProjectId(token) + "'";

			System.out.println("Str----------" + str);
			jdbcTemplate.update(str);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return 100;
	}

	@RequestMapping(value = "/api/getPendingProblemValidationCount", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public int getPendingProblemValidationCount(@CookieValue("XSRF-TOKEN") String token)
			throws UnAuthenticatedRequestException, AuthorizationException {

		int countOfRecords = 0;
		try {
			countOfRecords = jdbcTemplate.queryForObject(
					"select count(*) from bmcinfo where validationexperiment is null or uncertanity is null or impact is null or validationplan is null and projectid='"
							+ getActiveProjectId(token) + "'",
					Integer.class);
		} catch (Exception e) {

		}
		System.out.println("countOfRecords---------" + countOfRecords);
		return countOfRecords;
	}

	@RequestMapping(value = "/api/getVisionDescription", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ProjectDescription getVisionDescription(@CookieValue("XSRF-TOKEN") String token)
			throws UnAuthenticatedRequestException, AuthorizationException {

		String visionDescription = null;

		System.out.println("token value is -----------------" + token);

		try {
			visionDescription = jdbcTemplate
					.queryForObject("select visionDescription from projectdescription where projectid= '"
							+ getActiveProjectId(token) + "' ", String.class);
		} catch (Exception e) {

		}
		ProjectDescription projectDescription = new ProjectDescription();
		projectDescription.setVisionDescription(visionDescription);
		System.out.println("visionDescription---------" + visionDescription);
		return projectDescription;
	}

	@RequestMapping(value = "/api/updateVisionDescription", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public int updateVisionDescription(@RequestBody ProjectDescription projectDescription,
			@CookieValue("XSRF-TOKEN") String token) throws UnAuthenticatedRequestException, AuthorizationException {

		int countOfRecords = 0;
		String activeProjectId = getActiveProjectId(token);
		try {
			countOfRecords =jdbcTemplate.queryForObject(
					"select count(*) from projectdescription where projectid  = '" + activeProjectId + "' ",
					Integer.class);
		} catch (Exception e) {

			System.out.println(e.getMessage());
		}
		System.out.println("Uodate sttement is executed===========");

		String str;

		if (countOfRecords == 0) {
			str = "Insert into projectdescription (visionDescription,projectid) values('"
					+ projectDescription.getVisionDescription() + "' , '" + activeProjectId + "')";
		} else {
			str = "update projectdescription set visionDescription = '" + projectDescription.getVisionDescription()
					+ "' where " + "projectid  ='" + activeProjectId + "' ";
		}
		System.out.println("Str-updateVisionDescription---------" + str);
		jdbcTemplate.update(str);
		System.out.println("updateVisionDescription----------" + "updateVisionDescription--success");
		return 200;
	}

	@RequestMapping(value = "/api/getIdeaDescription", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ProjectDescription getIdeaDescription(@CookieValue("XSRF-TOKEN") String token)
			throws UnAuthenticatedRequestException, AuthorizationException {

		String ideaDescription = null;

		System.out.println("token value is -----------------" + token);

		try {
			ideaDescription = jdbcTemplate
					.queryForObject("select ideaDescription from projectdescription where projectid='"
							+ getActiveProjectId(token) + "' ", String.class);
		} catch (Exception e) {

		}
		System.out.println("ideaDescription---------" + ideaDescription);

		ProjectDescription projectDescription = new ProjectDescription();
		projectDescription.setIdeaDescription(ideaDescription);
		return projectDescription;
	}

	@RequestMapping(value = "/api/updateIdeaDescription", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public int updateIdeaDescription(@RequestBody ProjectDescription projectDescription,
			@CookieValue("XSRF-TOKEN") String token) throws UnAuthenticatedRequestException, AuthorizationException {

		int countOfRecords = 0;
		String activeProjectId = getActiveProjectId(token);
		try {
			countOfRecords = jdbcTemplate.queryForObject(
					"select count(*) from projectdescription where projectid  ='" + activeProjectId + "' ",
					Integer.class);
		} catch (Exception e) {
			System.out.println("updateIdeaDescription -------------" + e.getMessage());
		}
		System.out.println("Uodate sttement is executed===========");

		String str;

		if (countOfRecords == 0) {
			str = "Insert into projectdescription (ideaDescription,projectid) values('"
					+ projectDescription.getIdeaDescription() + "','" + activeProjectId + "')";
		} else {
			str = "update projectdescription set ideaDescription = '" + projectDescription.getIdeaDescription()
					+ "' where " + "projectid  = '" + activeProjectId + "'";
		}

		System.out.println("Str---updateIdeaDescription---------" + str);
		jdbcTemplate.update(str);

		return 200;
	}

	@RequestMapping(value = "/api/getProductConfiguration", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ProductConfiguration getProductConfiguration(@CookieValue("XSRF-TOKEN") String token)
			throws UnAuthenticatedRequestException, AuthorizationException {

		ProductConfiguration productConfiguration = new ProductConfiguration();

		try {
			productConfiguration = jdbcTemplate.queryForObject(
					"select * from ideaconfiguration where projectid= '" + getActiveProjectId(token) + "'",
					new Object[] {}, new RowMapper<ProductConfiguration>() {

						public ProductConfiguration mapRow(ResultSet rs, int rowNum) throws SQLException {
							ProductConfiguration prdConfig = new ProductConfiguration();

							prdConfig.setCustomerValue(rs.getString("customer"));
							prdConfig.setDeliveryOptionValue(rs.getString("ways"));
							prdConfig.setMarketOptionValue(rs.getString("market"));
							prdConfig.setProductFeature(rs.getString("productfeature"));
							prdConfig.setTechnologyOptionValue(rs.getString("technology"));
							return prdConfig;
						}
					});

		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("Success");

		return productConfiguration;
	}

	@RequestMapping(value = "/api/updateProductConfiguration", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public int updateProductConfiguration(@RequestBody UpdateRequest updateRequest,
			@CookieValue("XSRF-TOKEN") String token) throws UnAuthenticatedRequestException, AuthorizationException {

		int countOfRecords = 0;
		String activeProjectId = getActiveProjectId(token);

		try {
			countOfRecords = jdbcTemplate.queryForObject(
					"select count(*) from ideaconfiguration where projectid  = '" + activeProjectId + "'",
					Integer.class);
		} catch (Exception e) {

		}
		String str;
		if (countOfRecords == 0) {
			str = "Insert into ideaconfiguration (" + updateRequest.getStr() + ",projectid) values('"
					+ updateRequest.getValue() + "','" + activeProjectId + "')";
		} else {
			str = "update ideaconfiguration set " + updateRequest.getStr() + " = '" + updateRequest.getValue()
					+ "' where " + "projectid  = '" + activeProjectId + "'";
		}

		System.out.println("Str----------" + str);
		jdbcTemplate.update(str);

		return 200;
	}

	@RequestMapping(value = "/api/getProjectList", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public List getProjectList(@CookieValue("XSRF-TOKEN") String token)
			throws UnAuthenticatedRequestException, AuthorizationException {

		System.out.println("token value is -----------------" + token);

		// SecurityContext securityContext = new SecurityContext();
		String userid = getUserId(token);

		System.out.println("userid -----------------" + userid);

		UserProjectList userProject = null;
		List userProjectList = new ArrayList();

		try {
			List<String> list = jdbcTemplate.queryForList(
					"select projectid from projectusermaster where userid= '" + userid + "'", String.class);
			System.out.println("List is " + list.size());
			System.out.println("List is " + list);

			String activeProject = jdbcTemplate
					.queryForObject("select activeproject from userinfo where userid= '" + userid + "'", String.class);

			System.out.println("activeProject  " + activeProject);

			if (list.size() > 0) {
				for (int i = 0; i < list.size(); i++) {
					userProject = new UserProjectList();
					String str = "select projectname from projectmaster where projectid='" + list.get(i).toString()
							+ "'";
					System.out.println("String value is " + str);

					String projectName = jdbcTemplate.queryForObject(str, String.class);

					System.out.println("projectName---" + projectName);

					System.out.println("list.get(i).toString()---" + list.get(i).toString());
					System.out.println("activeProject---" + activeProject);
					if (activeProject.equals(list.get(i).toString())) {
						userProject.setActiveProject("active");
					} else {
						userProject.setActiveProject("none");
					}
					userProject.setProjectId(list.get(i).toString());
					userProject.setProjectName(projectName);
					userProjectList.add(userProject);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("Success");

		return userProjectList;
	}

	@RequestMapping(value = "/api/getProfileImage", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public UserProjectList getProfileImage(@CookieValue("XSRF-TOKEN") String token)
			throws UnAuthenticatedRequestException, AuthorizationException {

		String imagePath = "images/";

		String userid = getUserId(token);

		UserProjectList userProjectList = new UserProjectList();
		try {

			imagePath = imagePath + jdbcTemplate
					.queryForObject("select profilepic from userinfo where userid= '" + userid + "'", String.class);

		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("Success");

		userProjectList.setProfileImagePath(imagePath);
		return userProjectList;
	}

	@RequestMapping(value = "/api/getTeamInfoController", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public List<UserInfo> getTeamInfoController(@CookieValue("XSRF-TOKEN") String token)
			throws UnAuthenticatedRequestException, AuthorizationException {

		// String imagePath = "images/";
		// UserInfo userInfo = new UserInfo();
		List<UserInfo> userInfoList = new ArrayList<UserInfo>();
		try {

			String currentUserId = getUserId(token);

			String activeProject = jdbcTemplate.queryForObject(
					"select activeproject from userinfo where userid= '" + currentUserId + "'", String.class);

			List<String> list = jdbcTemplate.queryForList(
					"select userid from  projectusermaster where projectid= '" + activeProject + "'", String.class);

			if (list.size() > 0) {
				// userinfo
				for (int i = 0; i < list.size(); i++) {
					UserInfo userInfo = new UserInfo();
					if (!currentUserId.equals(list.get(i).toString())) {
						userInfo = jdbcTemplate.queryForObject(
								"select * from userinfo where userid= '" + list.get(i).toString() + "'",
								new Object[] {}, new RowMapper<UserInfo>() {

									public UserInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
										UserInfo userInfo = new UserInfo();

										userInfo.setUserName(rs.getString("username"));
										userInfo.setSkill(rs.getString("skills"));
										userInfo.setProfilePic("images/" + rs.getString("profilepic"));
										return userInfo;
									}
								});
						userInfoList.add(userInfo);
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("Success");

		// userProjectList.setProfileImagePath(imagePath);
		return userInfoList;
	}

	@RequestMapping(value = "/api/updateActiveProject", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public int updateActiveProject(@RequestBody UserProjectList userProjectList,
			@CookieValue("XSRF-TOKEN") String token) throws UnAuthenticatedRequestException, AuthorizationException {

		String str;

		String userid = getUserId(token);

		str = "update userinfo  set activeproject = '" + userProjectList.getProjectId() + "' where userID = '" + userid
				+ "'";

		System.out.println("Str----------" + str);
		jdbcTemplate.update(str);

		return 200;
	}

	@RequestMapping(value = "/api/getMVPData", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public MVPData getMVPData(@CookieValue("XSRF-TOKEN") String token)
			throws UnAuthenticatedRequestException, AuthorizationException {

		MVPData mvpData = new MVPData();

		String str = "select * from mvpdata where projectid= '" + getActiveProjectId(token) + "' LIMIT 1";
		System.out.println(str);
		try {
			mvpData = jdbcTemplate.queryForObject(str, new Object[] {}, new RowMapper<MVPData>() {

				public MVPData mapRow(ResultSet rs, int rowNum) throws SQLException {
					MVPData data = new MVPData();

					data.setDocumentDescription(rs.getString("documentDescription"));
					data.setDocumentTitle(rs.getString("documentTitle"));
					data.setLinktoDocument(rs.getString("linktoDocument"));

					System.out.println(" data.getDocumentDescription() " + data.getDocumentDescription());
					System.out.println(" data.getDocumentTitle() " + data.getDocumentTitle());
					System.out.println(" data.getLinktoDocument() " + data.getLinktoDocument());

					return data;
				}
			});

		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println(" mvpData.getDocumentDescription() " + mvpData.getDocumentDescription());
		System.out.println(" mvpData.getDocumentTitle() " + mvpData.getDocumentTitle());
		System.out.println(" mvpData.getLinktoDocument() " + mvpData.getLinktoDocument());
		System.out.println("Success");

		return mvpData;
	}

	@RequestMapping(value = "/api/getMVPTableData", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public List getMVPTableData(@CookieValue("XSRF-TOKEN") String token)
			throws UnAuthenticatedRequestException, AuthorizationException {

		MVPData mvpData = new MVPData();
		// List list<MVPData>;

		// List<MVPData> list = new ArrayList();

		List list = jdbcTemplate
				.queryForList("select * from mvpdata where projectid= '" + getActiveProjectId(token) + "'");

		/*
		 * try { mvpData = jdbcTemplate.queryForList(
		 * "select * from mvpdata where projectid= '" +
		 * getActiveProjectId(token) + "'", new Object[] {}, new
		 * RowMapper<MVPData>() {
		 * 
		 * public MVPData mapRow(ResultSet rs, int rowNum) throws SQLException {
		 * MVPData data = new MVPData();
		 * 
		 * data.setDocumentDescription("documentDescription");
		 * data.setDocumentTitle("documentTitle");
		 * data.setLinktoDocument("linktoDocument");
		 * 
		 * return data; } });
		 * 
		 * } catch (Exception e) { e.printStackTrace(); }
		 */
		System.out.println("Success");

		return list;
	}

	@RequestMapping(value = "/api/updateMVPData", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public int updateMVPData(@RequestBody MVPData mvpData, @CookieValue("XSRF-TOKEN") String token)
			throws UnAuthenticatedRequestException, AuthorizationException {

		// MVPData mvpData = new MVPData();
		String str;
		try {
			str = "insert into mvpdata (projectid, documenttitle, documentdescription, linktodocument) values('"
					+ getActiveProjectId(token) + "','" + mvpData.getDocumentTitle() + "','"
					+ mvpData.getDocumentDescription() + "','" + mvpData.getLinktoDocument() + "')";

			System.out.println("updateMVPData is " + str);

			jdbcTemplate.update(str);

		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("Success");

		return 200;
	}
	
	@RequestMapping(value = "/api/getLastLogonInfo", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public LastLogonInfo getLastLogonInfo(@CookieValue("XSRF-TOKEN") String token)
			throws UnAuthenticatedRequestException, AuthorizationException {

		
		String userId = getUserId(token);
		
		LastLogonInfo lastLogonInfo = new LastLogonInfo();
		
		System.out.println("userId is========="+ userId);
		
		String query1 = "select username from userinfo where userid='"+userId+"'";
		
		
		String username = jdbcTemplate.queryForObject(query1,String.class);
		
		
		System.out.println("username is========="+ username);
		String query2 ="select lastlogon from userlogoninfo where username='"+username+"'";
		
	
		String lastLogonTime =  jdbcTemplate.queryForObject(query2,String.class);
		
		lastLogonInfo.setLastLogonInformation(lastLogonTime);
		
		System.out.println("lastLogonTime is========="+ lastLogonTime);

		return lastLogonInfo;
	}
	

	private String getActiveProjectId(String token) throws UnAuthenticatedRequestException, AuthorizationException {

		if (!isAuthenticated(token)) {
			throw new UnAuthenticatedRequestException();
		}
		if (!isAuthorized(token, role)) {
			throw new AuthorizationException();
		}

		System.out.println("Token value is " + token);

		String activeProject = getActiveProjectIdByToken(token);

		/*
		 * String activeProject = jdbcTemplate.queryForObject(
		 * "select activeproject from userinfo where userid= '1111'",
		 * String.class);
		 */
		return activeProject;
	}

	private String retrieveUncertanityValue(String indexofrecord, String projectId, String cellName) {

		String uncertanityValue = null;
		try {

			String str = "Select uncertanity from bmcinfo where projectid ='" + projectId + "' and IndexOfRecord ='"
					+ indexofrecord + "' and cellName ='" + cellName + "'";

			System.out.println("Str----------" + str);
			uncertanityValue = jdbcTemplate.queryForObject(str, String.class);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return uncertanityValue;
	}

	private String retrieveImpactValue(String indexofrecord, String projectId, String cellName) {

		String impactValue = null;
		try {

			String str = "Select impact from bmcinfo where projectid ='" + projectId + "' and IndexOfRecord ='"
					+ indexofrecord + "' and cellName='" + cellName + "'";

			System.out.println("Str----------" + str);
			impactValue = jdbcTemplate.queryForObject(str, String.class);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return impactValue;
	}

	private void updateTotalImpact(String uncertanityValue, String impactValue, String activeProjectId, String cellName,
			String indexOfRecord) {
		double v1 = 0;
		double v2 = 0;

		if (uncertanityValue.equalsIgnoreCase("High")) {
			v1 = 0.5;
		} else if (uncertanityValue.equalsIgnoreCase("medium")) {
			v1 = 0.3;
		} else if (uncertanityValue.equalsIgnoreCase("low")) {
			v1 = 0.1;
		}

		if (impactValue.equalsIgnoreCase("Very High")) {
			v2 = 0.8;
		} else if (impactValue.equalsIgnoreCase("High")) {
			v2 = 0.5;
		} else if (impactValue.equalsIgnoreCase("medium")) {
			v2 = 0.3;
		} else if (impactValue.equalsIgnoreCase("low")) {
			v2 = 0.1;
		}

		/*
		 * switch(uncertanityValue){ case "High": v1=0.5; break; case "Medium":
		 * v1=0.3; break; case "Low": v1=0.1; break;
		 * 
		 * }
		 * 
		 * switch(impactValue){ case "High": v2=0.5; break; case "Medium":
		 * v2=0.3; break; case "Low": v2=0.1; break;
		 * 
		 * }
		 */

		double v = v1 * v2;

		DecimalFormat myFormatter = new DecimalFormat("###.##");
		String value = myFormatter.format(v);

		System.out.println("value is -------------------------------------------------------" + value);
		String str = "UPDATE bmcinfo set totalimpact ='" + value + "' where " + "CellName  = '" + cellName + "' and "
				+ "IndexOfRecord  = '" + indexOfRecord + "' and projectid='" + activeProjectId + "'";

		System.out.println("Str----------" + str);
		jdbcTemplate.update(str);

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

	@ExceptionHandler({ UnAuthenticatedRequestException.class })
	void handleUnAuthenticatedRequest(HttpServletResponse response) throws IOException {
		response.sendError(HttpStatus.NETWORK_AUTHENTICATION_REQUIRED.value());
	}

	@ExceptionHandler({ AuthorizationException.class })
	void handleUnAuthorizeRequest(HttpServletResponse response) throws IOException {
		response.sendError(HttpStatus.UNAUTHORIZED.value());
	}

}
// updateImpactValue updateUncertanityValue