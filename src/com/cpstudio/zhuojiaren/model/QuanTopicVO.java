package com.cpstudio.zhuojiaren.model;

import java.util.List;

public class QuanTopicVO {

	private String topicid;

	private String content;

	private String groupid;

	private String userid;

	private String name;

	private String uheader;

	private String position;

	private String addtime;

	private List<PicNewVO> topicPics;

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

	public void setPosition(String position) {
		this.position = position;
	}

	public String getPosition() {
		return this.position;
	}

	public void setAddtime(String addtime) {
		this.addtime = addtime;
	}

	public String getAddtime() {
		return this.addtime;
	}

	public void setTopicPic(List<PicNewVO> topicPic) {
		this.topicPics = topicPic;
	}

	public List<PicNewVO> getTopicPic() {
		return this.topicPics;
	}
}
