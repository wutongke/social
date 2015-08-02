package com.cpstudio.zhuojiaren.model;

import java.util.List;

public class TopicDetailVO {
	private String topicid;

	private String content;

	private String groupid;

	private String userid;

	private String name;

	private String uheader;

	private int position;

	private String addtime;

	private String company;

	private List<PicNewVO> topicPics;

	private List<Praise> praiseLists;

	private List<Comment> commentLists;

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public List<PicNewVO> getTopicPics() {
		return topicPics;
	}

	public void setTopicPics(List<PicNewVO> topicPics) {
		this.topicPics = topicPics;
	}

	public List<Praise> getPraiseLists() {
		return praiseLists;
	}

	public void setPraiseLists(List<Praise> praiseLists) {
		this.praiseLists = praiseLists;
	}

	public List<Comment> getCommentLists() {
		return commentLists;
	}

	public void setCommentLists(List<Comment> commentLists) {
		this.commentLists = commentLists;
	}

	public void setTopicid(String topicid) {
		this.topicid = topicid;
	}

	public String getTopicid() {
		return this.topicid;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getContent() {
		return this.content;
	}

	public void setGroupid(String groupid) {
		this.groupid = groupid;
	}

	public String getGroupid() {
		return this.groupid;
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

	public void setAddtime(String addtime) {
		this.addtime = addtime;
	}

	public String getAddtime() {
		return this.addtime;
	}


}