package com.cpstudio.zhuojiaren.model;

public class LoginRes {

	String session;
	String rongyunToken;
	String qiniuToken;
	String userid;
	public String getSession() {
		return session;
	}
	public String getRongyunToken() {
		return rongyunToken;
	}
	public String getQiniuToken() {
		return qiniuToken;
	}
	public String getUserid() {
		return userid;
	}
	public void setSession(String session) {
		this.session = session;
	}
	public void setRongyunToken(String rongyunToken) {
		this.rongyunToken = rongyunToken;
	}
	public void setQiniuToken(String qiniuToken) {
		this.qiniuToken = qiniuToken;
	}
	public void setUserid(String userid) {
		this.userid = userid;
	}
}
