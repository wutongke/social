package com.cpstudio.zhuojiaren.model;


public class EventVO {

	private String eventId;
	private String Name;
	private String BrowerCount;
	private String shareCount;
	private String applyCount;
	private String Content;
	private UserVO boss;
	private String timeNow;
	private String time;
	private String people;
	private String locate;
	private String phone;
	private String memberType;
	private String isCollection;
	private String isShared;
	
	public String getEventId() {
		return eventId;
	}
	public void setEventId(String eventId) {
		this.eventId = eventId;
	}
	public String getName() {
		return Name;
	}
	public void setName(String name) {
		Name = name;
	}
	public String getBrowerCount() {
		return BrowerCount;
	}
	public void setBrowerCount(String browerCount) {
		BrowerCount = browerCount;
	}
	public String getShareCount() {
		return shareCount;
	}
	public void setShareCount(String shareCount) {
		this.shareCount = shareCount;
	}
	public String getApplyCount() {
		return applyCount;
	}
	public void setApplyCount(String applyCount) {
		this.applyCount = applyCount;
	}
	public String getContent() {
		return Content;
	}
	public void setContent(String content) {
		Content = content;
	}
	public UserVO getBoss() {
		return boss;
	}
	public void setBoss(UserVO boss) {
		this.boss = boss;
	}
	public String getTimeNow() {
		return timeNow;
	}
	public void setTimeNow(String timeNow) {
		this.timeNow = timeNow;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getPeople() {
		return people;
	}
	public void setPeople(String people) {
		this.people = people;
	}
	public String getLocate() {
		return locate;
	}
	public void setLocate(String locate) {
		this.locate = locate;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getMemberType() {
		return memberType;
	}
	public void setMemberType(String memberType) {
		this.memberType = memberType;
	}
	public String getIsCollection() {
		return isCollection;
	}
	public void setIsCollection(String isCollection) {
		this.isCollection = isCollection;
	}
	public String getIsShared() {
		return isShared;
	}
	public void setIsShared(String isShared) {
		this.isShared = isShared;
	}
	
}
