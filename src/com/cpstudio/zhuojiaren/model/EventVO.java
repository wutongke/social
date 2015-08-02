package com.cpstudio.zhuojiaren.model;

import java.util.ArrayList;

public class EventVO {

	private String activityid;
	private String content;

	private String starttime;

	private String endtime;
	private String contacts;

	private String phone;
	private String userid;

	private String name;
	private String uheader;

	private String position;
	private String company;
	private String viewCount;
	private String shareCount;

	private int outdate;//(是否过期 1已过期  0未过期) ,
	private String address;
	private String joinCount;
	private ArrayList<PicNewVO> activityPic;
	private boolean collected;
	// add by lz
	boolean isSelected = false;
    private String title;
    
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public boolean iscollected() {
		return collected;
	}

	public void setIscollected(boolean iscollected) {
		this.collected = iscollected;
	}

	public String getEndtime() {
		return endtime;
	}

	public void setEndtime(String endtime) {
		this.endtime = endtime;
	}

	public String getContacts() {
		return contacts;
	}

	public void setContacts(String contacts) {
		this.contacts = contacts;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUheader() {
		return uheader;
	}

	public void setUheader(String uheader) {
		this.uheader = uheader;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getViewCount() {
		return viewCount;
	}

	public void setViewCount(String viewCount) {
		this.viewCount = viewCount;
	}

	public String getShareCount() {
		return shareCount;
	}

	public void setShareCount(String shareCount) {
		this.shareCount = shareCount;
	}

	public ArrayList<PicNewVO> getActivityPic() {
		return activityPic;
	}

	public void setActivityPic(ArrayList<PicNewVO> activityPic) {
		this.activityPic = activityPic;
	}

	public String getActivityid() {
		return activityid;
	}

	public void setActivityid(String activityid) {
		this.activityid = activityid;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getStarttime() {
		return starttime;
	}

	public void setStarttime(String starttime) {
		this.starttime = starttime;
	}

	
	public int getOutdate() {
		return outdate;
	}

	public void setOutdate(int outdate) {
		this.outdate = outdate;
	}

	
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getJoinCount() {
		return joinCount;
	}

	public void setJoinCount(String joinCount) {
		this.joinCount = joinCount;
	}

	public boolean isSelected() {
		return isSelected;
	}

	public void setSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}

}
