package com.cpstudio.zhuojiaren.model;

public class Comment {
	private String id;
	private String toId;
	private String toUserid;
	private String toName;
	private String userid;

	private String name;

	private String uheader;

	private int position;

	private String comment;

	private String addtime;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getToId() {
		return toId;
	}

	public void setToId(String toId) {
		this.toId = toId;
	}

	public String getToUserid() {
		return toUserid;
	}

	public void setToUserid(String toUserid) {
		this.toUserid = toUserid;
	}

	public String getToName() {
		return toName;
	}

	public void setToName(String toName) {
		this.toName = toName;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getUserid() {
		return this.userid;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return this.name;
	}

	public void setUheader(String uheader) {
		this.uheader = uheader;
	}

	public String getUheader() {
		return this.uheader;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	public int getPosition() {
		return this.position;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getComment() {
		return this.comment;
	}

	public void setAddtime(String addtime) {
		this.addtime = addtime;
	}

	public String getAddtime() {
		return this.addtime;
	}

}