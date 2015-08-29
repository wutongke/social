package com.cpstudio.zhuojiaren.model;

public class CompanyNewVO {
	private String comid;

	private String company;

	private int industry;

	private int city;

	private String homepage;

	private int position;

	private int status;

	public void setComid(String comid) {
		this.comid = comid;
	}

	public String getComid() {
		return this.comid;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getCompany() {
		return this.company;
	}

	public void setIndustry(int industry) {
		this.industry = industry;
	}

	public int getIndustry() {
		return this.industry;
	}

	public void setCity(int city) {
		this.city = city;
	}

	public int getCity() {
		return this.city;
	}

	public void setHomepage(String homepage) {
		this.homepage = homepage;
	}

	public String getHomepage() {
		return this.homepage;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	public int getPosition() {
		return this.position;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getStatus() {
		return this.status;
	}
}
