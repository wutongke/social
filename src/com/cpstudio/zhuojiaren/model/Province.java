package com.cpstudio.zhuojiaren.model;

import java.util.ArrayList;

public class Province {

	private String provinceId;
	private String provinceName;
	private ArrayList<City> citys;
	public String getProvinceId() {
		return provinceId;
	}
	public String getProvinceName() {
		return provinceName;
	}
	public ArrayList<City> getCitys() {
		return citys;
	}
	public void setProvinceId(String provinceId) {
		this.provinceId = provinceId;
	}
	public void setProvinceName(String provinceName) {
		this.provinceName = provinceName;
	}
	public void setCitys(ArrayList<City> citys) {
		this.citys = citys;
	}
	
}
