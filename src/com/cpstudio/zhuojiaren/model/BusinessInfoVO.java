package com.cpstudio.zhuojiaren.model;

public class BusinessInfoVO {
	private ResourceGXVO supply;
	private ResourceGXVO demand;
	private String wxShop;//(企业微店链接)
	private String comVedio;//(视频介绍)
	public ResourceGXVO getSupply() {
		return supply;
	}
	public void setSupply(ResourceGXVO supply) {
		this.supply = supply;
	}
	public ResourceGXVO getDemand() {
		return demand;
	}
	public void setDemand(ResourceGXVO demand) {
		this.demand = demand;
	}
	public String getWxShop() {
		return wxShop;
	}
	public void setWxShop(String wxShop) {
		this.wxShop = wxShop;
	}
	public String getComVedio() {
		return comVedio;
	}
	public void setComVedio(String comVedio) {
		this.comVedio = comVedio;
	}
	
}
