package com.cpstudio.zhuojiaren.model;

import java.util.ArrayList;
import java.util.List;

public class ContactVO {
	private String follownum;
	private String fansnum;
	private List<UserVO> users = new ArrayList<UserVO>();
	public String getFollownum() {
		return follownum;
	}
	public void setFollownum(String follownum) {
		this.follownum = follownum;
	}
	public String getFansnum() {
		return fansnum;
	}
	public void setFansnum(String fansnum) {
		this.fansnum = fansnum;
	}
	public List<UserVO> getUsers() {
		return users;
	}
	public void setUsers(List<UserVO> users) {
		this.users = users;
	}
	
}
