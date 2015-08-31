package com.cpstudio.zhuojiaren.model;

public class UserAndCollection {
	public static String TYPE = "type";
	public static int CITY = 1;
	public static int INDUSTRY = 2;
	public static int INTERST = 3;
	public static int ALL = 4;
	public static int NEAYBY = 5;
	public static int TEACHER = 6;

	// “— ’≤ÿ
	public static String collection = "1";

	private String id;
	private String isCollection;
	private String distance;
	private UserVO user;
	// lz
	private String userid;
	private String name;
	private String uheader;
	private int position;
	private String company;
	private String addtime;
	private int isCollect;
	private int tchtype;
	
	// add by lz
	private boolean isChecked;

	public boolean isChecked() {
		return isChecked;
	}

	public void setChecked(boolean isChecked) {
		this.isChecked = isChecked;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public int getIsCollect() {
		return isCollect;
	}

	public void setIsCollect(int isCollect) {
		this.isCollect = isCollect;
	}

	public int getTchtype() {
		return tchtype;
	}

	public void setTchtype(int tchtype) {
		this.tchtype = tchtype;
	}

	public String getIsCollection() {
		return isCollection;
	}

	public void setIsCollection(String isCollection) {
		this.isCollection = isCollection;
	}

	public UserVO getUser() {
		return user;
	}

	public void setUser(UserVO user) {
		this.user = user;
	}

	public String getDistance() {
		return distance;
	}

	public void setDistance(String distance) {
		this.distance = distance;
	}

	public String getAddtime() {
		return addtime;
	}

	public void setAddtime(String addtime) {
		this.addtime = addtime;
	}

}
