package com.cpstudio.zhuojiaren.model;

import java.util.ArrayList;
import java.util.List;

public class QuanVO {
	//圈子类型
	public final static  String QUANZITYPE = "quanzitype";
	public final static  int QUANZIMYCTEATE = 6;
	public final static  int QUANZIMYADD = 7;
	public final static  int QUANZIRECOMMEND = 2;
	public final static  int QUANZIQUERY = 3;
	
	//圈子主页列表类型
	public final static  String QUANZIMAINTYPE = "quanzitype";
	public final static  int QUANZITOPIC = 7;
	public final static  int QUANZIEVENT = 8;
	public final static  int QUANZIMEMBER = 9;

	private String groupid;
	private String gname;
	private String gheader;
	private String gproperty;
	private String gintro;
	private String createtime;
	private String membersnum;
	private String membersmax;
	private String lastbroadcast;
	private String lastmsgtime;
	private String alert;
	private String memberCount;
	private UserVO founder = new UserVO();
	private List<UserVO> managers = new ArrayList<UserVO>();
	private List<UserVO> members = new ArrayList<UserVO>();
	private String membertype;

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

	public String getCreatetime() {
		return createtime;
	}

	public void setCreatetime(String createtime) {
		this.createtime = createtime;
	}

	public List<UserVO> getManagers() {
		return managers;
	}

	public void setManagers(List<UserVO> managers) {
		this.managers = managers;
	}

	public List<UserVO> getMembers() {
		return members;
	}

	public void setMembers(List<UserVO> members) {
		this.members = members;
	}

	public String getMembersnum() {
		return membersnum;
	}

	public void setMembersnum(String membersnum) {
		this.membersnum = membersnum;
	}

	public String getMembersmax() {
		return membersmax;
	}

	public void setMembersmax(String membersmax) {
		this.membersmax = membersmax;
	}

	public String getGproperty() {
		return gproperty;
	}

	public void setGproperty(String gproperty) {
		this.gproperty = gproperty;
	}

	public String getLastbroadcast() {
		return lastbroadcast;
	}

	public void setLastbroadcast(String lastbroadcast) {
		this.lastbroadcast = lastbroadcast;
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

	public String getMemberCount() {
		return memberCount;
	}

	public void setMemberCount(String memberCount) {
		this.memberCount = memberCount;
	}

	public void setAlert(String alert) {
		this.alert = alert;
	}

	public UserVO getFounder() {
		return founder;
	}

	public void setFounder(UserVO founder) {
		this.founder = founder;
	}

	public String getMembertype() {
		return membertype;
	}

	public void setMembertype(String membertype) {
		this.membertype = membertype;
	}

}
