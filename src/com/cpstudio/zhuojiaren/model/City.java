package com.cpstudio.zhuojiaren.model;

public class City {
	public String name;
	public String pinyi;
	public String cityId;
	public String cityName;

	public City(String name, String pinyi) {
		super();
		this.name = name;
		this.pinyi = pinyi;
	}

	public City() {
		super();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPinyi() {
		return pinyi;
	}

	public void setPinyi(String pinyi) {
		this.pinyi = pinyi;
	}

	public String getCityId() {
		return cityId;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityId(String cityId) {
		this.cityId = cityId;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

}
