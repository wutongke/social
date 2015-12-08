package com.cpstudio.zhuojiaren.model;

import java.util.ArrayList;

public class EventVO {
   private boolean sellected;//管理时用于标志是否被选中了
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

	private int outdate;// (是否过期 1已过期 0未过期) ,
	private String address;
	private String joinCount;
	private ArrayList<PicNewVO> activityPic;
	private String iscollected;
	private String isjoined;
	private String isowner;
	private String lefttime;
	private String groupId,groupName;//该活动所在圈子的ID和名称
	
	
	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public String getIsjoined() {
		return isjoined;
	}

	public boolean isSellected() {
		return sellected;
	}

	public void setSellected(boolean sellected) {
		this.sellected = sellected;
	}

	public String getIsowner() {
		return isowner;
	}

	public void setIsjoined(String isjoined) {
		this.isjoined = isjoined;
	}

	public void setIsowner(String isowner) {
		this.isowner = isowner;
	}

	public String getIscollected() {
		return iscollected;
	}

	public void setIscollected(String iscollected) {
		this.iscollected = iscollected;
	}
	// add by lz
	boolean isSelected = false;
	private String title;
	// 经纬度
	private String longitude;
	private String latitude;

	public String getActivityid() {
		return activityid;
	}

	public String getContent() {
		return content;
	}

	public String getStarttime() {
		return starttime;
	}

	public String getEndtime() {
		return endtime;
	}

	public String getContacts() {
		return contacts;
	}

	public String getPhone() {
		return phone;
	}

	public String getUserid() {
		return userid;
	}

	public String getName() {
		return name;
	}

	public String getUheader() {
		return uheader;
	}

	public String getPosition() {
		return position;
	}

	public String getCompany() {
		return company;
	}

	public String getViewCount() {
		return viewCount;
	}

	public String getShareCount() {
		return shareCount;
	}

	public int getOutdate() {
		return outdate;
	}

	public String getAddress() {
		return address;
	}

	public String getJoinCount() {
		return joinCount;
	}

	public ArrayList<PicNewVO> getActivityPic() {
		return activityPic;
	}


	public boolean isSelected() {
		return isSelected;
	}

	public String getTitle() {
		return title;
	}

	public String getLefttime() {
		return lefttime;
	}

	public void setLefttime(String lefttime) {
		this.lefttime = lefttime;
	}

	public String getLongitude() {
		return longitude;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setActivityid(String activityid) {
		this.activityid = activityid;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public void setStarttime(String starttime) {
		this.starttime = starttime;
	}

	public void setEndtime(String endtime) {
		this.endtime = endtime;
	}

	public void setContacts(String contacts) {
		this.contacts = contacts;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setUheader(String uheader) {
		this.uheader = uheader;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public void setViewCount(String viewCount) {
		this.viewCount = viewCount;
	}

	public void setShareCount(String shareCount) {
		this.shareCount = shareCount;
	}

	public void setOutdate(int outdate) {
		this.outdate = outdate;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public void setJoinCount(String joinCount) {
		this.joinCount = joinCount;
	}

	public void setActivityPic(ArrayList<PicNewVO> activityPic) {
		this.activityPic = activityPic;
	}


	public void setSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}
	public boolean  isCollect(){
		return Integer.parseInt(iscollected)==1; 
	}
}
