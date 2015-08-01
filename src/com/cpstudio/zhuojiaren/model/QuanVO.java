package com.cpstudio.zhuojiaren.model;

import java.util.ArrayList;
import java.util.List;

public class QuanVO {
	// lz 0801
	/**
	 * 退出圈子
	 */
	public final static int QUAN_QUIT = 0;
	/**
	 * 加入圈子
	 */
	public final static int QUAN_JOIN = 1;
	/*
	 * 接受某人加入圈子
	 */
	public final static int QUAN_PERMIT = 2;

	/*
	 * 接受某人加入圈子 角色 0-未加入 1-普通成员 2-管理员 3-圈主
	 */
	public final static int QUAN_ROLE_YOUKE = 0;
	public final static int QUAN_ROLE_MEMBER = 1;
	public final static int QUAN_ROLE_MANAGER = 2;
	public final static int QUAN_ROLE_OWNER = 3;

	// 圈子类型
	public final static String QUANZITYPE = "quanzitype";
	public final static int QUANZIMYCTEATE = 6;
	public final static int QUANZIMYADD = 7;
	public final static int QUANZIRECOMMEND = 2;
	public final static int QUANZIQUERY = 3;

	// 圈子主页列表类型
	public final static String QUANZIMAINTYPE = "quanzitype";
	public final static int QUANZITOPIC = 7;
	public final static int QUANZIEVENT = 8;
	public final static int QUANZIMEMBER = 9;

	private String groupid;
	private String gname;
	private String gheader;
	private String gintro;

	private String gpub;

	private int gtype;

	private String addtime;
	private int memberCount;
	private int topicCount;
	private String lastmsgtime;
	private String alert;

	int city;// (地区 城市编码)
	int followpms;// 加入权限 0:允许任何人加入，1:需要申请才能加入) ,
	int accesspms;// (访问权限 0:所有人都可以访问，1:加入圈子才可以访问) ,

	int role;// 角色 0-未加入 1-普通成员 2-管理员 3-圈主

	public int getRole() {
		return role;
	}

	public void setRole(int role) {
		this.role = role;
	}

	public String getGroupid() {
		return groupid;
	}

	public void setGroupid(String groupid) {
		this.groupid = groupid;
	}

	public String getGname() {
		return gname;
	}

	public void setGname(String gname) {
		this.gname = gname;
	}

	public String getGheader() {
		return gheader;
	}

	public void setGheader(String gheader) {
		this.gheader = gheader;
	}

	public String getGintro() {
		return gintro;
	}

	public void setGintro(String gintro) {
		this.gintro = gintro;
	}

	public String getGpub() {
		return gpub;
	}

	public void setGpub(String gpub) {
		this.gpub = gpub;
	}

	public int getGtype() {
		return gtype;
	}

	public void setGtype(int gtype) {
		this.gtype = gtype;
	}

	public String getAddtime() {
		return addtime;
	}

	public void setAddtime(String addtime) {
		this.addtime = addtime;
	}

	public int getMemberCount() {
		return memberCount;
	}

	public void setMemberCount(int memberCount) {
		this.memberCount = memberCount;
	}

	public int getTopicCount() {
		return topicCount;
	}

	public void setTopicCount(int topicCount) {
		this.topicCount = topicCount;
	}

	public String getLastmsgtime() {
		return lastmsgtime;
	}

	public void setLastmsgtime(String lastmsgtime) {
		this.lastmsgtime = lastmsgtime;
	}

	public String getAlert() {
		return alert;
	}

	public void setAlert(String alert) {
		this.alert = alert;
	}

	public int getCity() {
		return city;
	}

	public void setCity(int city) {
		this.city = city;
	}

	public int getFollowpms() {
		return followpms;
	}

	public void setFollowpms(int followpms) {
		this.followpms = followpms;
	}

	public int getAccesspms() {
		return accesspms;
	}

	public void setAccesspms(int accesspms) {
		this.accesspms = accesspms;
	}

}
