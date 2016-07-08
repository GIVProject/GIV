package com.example.model;

public class UserProjectList {

	private String projectName;
	private String projectId;
	private String activeProject;
	private String profileImagePath;
	
	
	private String userID;

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public String getProjectId() {
		return projectId;
	}

	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}

	public String getUserID() {
		return userID;
	}

	public void setUserID(String userID) {
		this.userID = userID;
	}

	public String getActiveProject() {
		return activeProject;
	}

	public void setActiveProject(String activeProject) {
		this.activeProject = activeProject;
	}

	public String getProfileImagePath() {
		return profileImagePath;
	}

	public void setProfileImagePath(String profileImagePath) {
		this.profileImagePath = profileImagePath;
	}
	
	
		
}
