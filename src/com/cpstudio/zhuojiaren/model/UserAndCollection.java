package com.cpstudio.zhuojiaren.model;

public class UserAndCollection {

	public static String TYPE="type";
	
	public static int CITY=1;
	public static int INDUSTRY=2;
	public static int  INTERST=3;
	public static int ALL=4;
	public static int NEAYBY=5;
	public static int TEACHER=6;
		
	
	
	//“— ’≤ÿ
	public static String collection = "1";
	
	
	private String id;
	private String isCollection;
	private String distance;
	private UserVO user;
	
	
	//add by lz
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

}
