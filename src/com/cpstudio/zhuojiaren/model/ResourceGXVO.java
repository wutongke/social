package com.cpstudio.zhuojiaren.model;

public class ResourceGXVO {

	//lz
	public static String RESOURCEGXTYPE ="resource_gongxu_type";// Ѱ����Դ
	public static String RESOURCEGXFILTER_LOCATION ="resource_filter_location";// 
	public static String RESOURCEGXFILTER_TYPE ="resource_filter_type";// 
	// ���� �ĸ�������
	public static int RESOURCE_FIND = 0;// Ѱ����Դ
	public static int NEED_FIND = 1;// ��������
	public static int PUB_NEED_RESOURCE = 2;// ���跢��
	
	
	private String title;
	String industry;//��ҵ
	String fund;//�ʽ�
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
