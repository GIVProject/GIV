package com.example.model;

import java.math.BigInteger;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;


public class ProjectInfo {

	private BigInteger count ;
	private String valueOfAttribute;
	private String colorOfAttribute;
	private String cell;
	private String index;
	private String validationExperient;
	private String uncertanity;
	private String impact;
	private String validationPlan;
	
	 
	public String getValidationExperient() {
		return validationExperient;
	}

	public void setValidationExperient(String validationExperient) {
		this.validationExperient = validationExperient;
	}

	public String getUncertanity() {
		return uncertanity;
	}

	public void setUncertanity(String uncertanity) {
		this.uncertanity = uncertanity;
	}

	public String getImpact() {
		return impact;
	}

	public void setImpact(String impact) {
		this.impact = impact;
	}

	public String getValidationPlan() {
		return validationPlan;
	}

	public void setValidationPlan(String validationPlan) {
		this.validationPlan = validationPlan;
	}

	public String getIndex() {
		return index;
	}

	public void setIndex(String index) {
		this.index = index;
	}

	public String getValueOfAttribute() {
		return valueOfAttribute;
	}

	public void setValueOfAttribute(String valueOfAttribute) {
		this.valueOfAttribute = valueOfAttribute;
	}

	public String getColorOfAttribute() {
		return colorOfAttribute;
	}

	public void setColorOfAttribute(String colorOfAttribute) {
		this.colorOfAttribute = colorOfAttribute;
	}

	public String getCell() {
		return cell;
	}

	public void setCell(String cell) {
		this.cell = cell;
	}

	public ProjectInfo(){
		
	}
	
	public Map getCount(){
		this.count = BigInteger.valueOf(100);
		Map<String,BigInteger> hm = new HashMap();
		hm.put("count", this.count);
		
		
		return hm;
	}
	
	public void setCount(BigInteger count){
		this.count = count ; 
	}

	public static Collections getValues() {
		// TODO Auto-generated method stub
		return null;
	}
}
