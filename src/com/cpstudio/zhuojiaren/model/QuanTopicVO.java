package com.cpstudio.zhuojiaren.model;

import java.io.Serializable;
import java.util.List;

public class QuanTopicVO implements Serializable{

	private String topicid;

	private String content;

	private String groupid;
//	private String groupName;
	private String gname;
	private String uheader;

	private int position;

	private String addtime;

	private List<PicNewVO> topicPic;

	private String userid;//·¢²¼Õß
	
	private String name;
	
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

	public void setTopicPic(List<PicNewVO> topicPic) {
		this.topicPic = topicPic;
	}

	public List<PicNewVO> getTopicPic() {
		return this.topicPic;
	}

	public String getGname() {
		return gname;
	}

	public void setGname(String gname) {
		this.gname = gname;
	}

//	public String getGroupName() {
//		return groupName;
//	}
//
//	public void setGroupName(String groupName) {
//		this.groupName = groupName;
//	}
	
}
