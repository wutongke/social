package com.cpstudio.zhuojiaren.model;

public class ResourceGXVO {

	//lz
	public static String RESOURCEGXTYPE ="resource_gongxu_type";// 寻找资源
	public static String RESOURCEGXFILTER_LOCATION ="resource_filter_location";// 
	public static String RESOURCEGXFILTER_TYPE ="resource_filter_type";// 
	// 供需 的各种类型
	public static int RESOURCE_FIND = 0;// 寻找资源
	public static int NEED_FIND = 1;// 发现需求
	public static int PUB_NEED_RESOURCE = 2;// 供需发布
	
	
	private String title;
	String industry;//行业
	String fund;//资金
	int tag;
	String imgUrl;
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getIndustry() {
		return industry;
	}
	public void setIndustry(String industry) {
		this.industry = industry;
	}
	public String getFund() {
		return fund;
	}
	public void setFund(String fund) {
		this.fund = fund;
	}
	public int getTag() {
		return tag;
	}
	public void setTag(int tag) {
		this.tag = tag;
	}
	public String getImgUrl() {
		return imgUrl;
	}
	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}
	
}
