package com.example.model;

import java.sql.Date;
import java.util.List;
import java.util.Map;

public class ReportModel {
	//,,,,startdate,
	private String projectId;
	
	private String projectName;
	
	private String innovator;
	
	private String projectCurrentphase;
	
	private String projectStatus;
	
	private Date startdate;
	
	private String userName;
	
	private String npvValue;
	
	private ProductFeatures productFeatures;
	
	private List<ProjectRiskData> projectRiskDataList;
	
	private List<RiskData> riskDataList;
	
	private Map departmentInfoMap;
	
	private Map projectPhaseInfoMap;
	
	/*private String businessModel;
	
	private String technology;
	
	private String productFeature;

	private String customerSegment;
	
	private String market;*/

	public String getInnovator() {
		return innovator;
	}

	public void setInnovator(String innovator) {
		this.innovator = innovator;
	}

	public String getProjectCurrentPhase() {
		return projectCurrentphase;
	}

	public void setProjectCurrentPhase(String projectCurrentphase) {
		this.projectCurrentphase = projectCurrentphase;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public String getNpvValue() {
		return npvValue;
	}

	public void setNpvValue(String npvValue) {
		this.npvValue = npvValue;
	}
/*
	public String getBusinessModel() {
		return businessModel;
	}

	public void setBusinessModel(String businessModel) {
		this.businessModel = businessModel;
	}

	public String getTechnology() {
		return technology;
	}

	public void setTechnology(String technology) {
		this.technology = technology;
	}

	public String getProductFeature() {
		return productFeature;
	}

	public void setProductFeature(String productFeature) {
		this.productFeature = productFeature;
	}

	public String getCustomerSegment() {
		return customerSegment;
	}

	public void setCustomerSegment(String customerSegment) {
		this.customerSegment = customerSegment;
	}

	public String getMarket() {
		return market;
	}

	public void setMarket(String market) {
		this.market = market;
	}*/
	public String getProjectId() {
		return projectId;
	}

	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}

	public String getProjectCurrentphase() {
		return projectCurrentphase;
	}

	public void setProjectCurrentphase(String projectCurrentphase) {
		this.projectCurrentphase = projectCurrentphase;
	}

	public String getProjectStatus() {
		return projectStatus;
	}

	public void setProjectStatus(String projectStatus) {
		this.projectStatus = projectStatus;
	}

	public Date getStartdate() {
		return startdate;
	}

	public void setStartdate(Date startdate) {
		this.startdate = startdate;
	}

	public ProductFeatures getProductFeatures() {
		return productFeatures;
	}

	public void setProductFeatures(ProductFeatures productFeatures) {
		this.productFeatures = productFeatures;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public List<ProjectRiskData> getProjectRiskDataList() {
		return projectRiskDataList;
	}

	public void setProjectRiskDataList(List<ProjectRiskData> projectRiskDataList) {
		this.projectRiskDataList = projectRiskDataList;
	}

	public List<RiskData> getRiskDataList() {
		return riskDataList;
	}

	public void setRiskDataList(List<RiskData> riskDataList) {
		this.riskDataList = riskDataList;
	}

	public Map getDepartmentInfoMap() {
		return departmentInfoMap;
	}

	public void setDepartmentInfoMap(Map departmentInfoMap) {
		this.departmentInfoMap = departmentInfoMap;
	}

	public Map getProjectPhaseInfoMap() {
		return projectPhaseInfoMap;
	}

	public void setProjectPhaseInfoMap(Map projectPhaseInfoMap) {
		this.projectPhaseInfoMap = projectPhaseInfoMap;
	}

	
	
}
