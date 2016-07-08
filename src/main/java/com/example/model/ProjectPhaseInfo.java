package com.example.model;

import java.util.Date;

public class ProjectPhaseInfo {
	
	private String phaseid="1";
	
	private String phaseName="Idea description";
	
	private double duration;

	private Date startDate;
	
	private Date endDate;
	
	public String getPhaseid() {
		return phaseid;
	}

	public void setPhaseid(String phaseid) {
		this.phaseid = phaseid;
	}

	public String getPhaseName() {
		return phaseName;
	}

	public void setPhaseName(String phaseName) {
		this.phaseName = phaseName;
	}

	public double getDuration() {
		return duration;
	}

	public void setDuration(double diffDays) {
		this.duration = diffDays;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	
}
