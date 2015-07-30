package com.cpstudio.zhuojiaren.model;

import java.io.Serializable;
import java.util.ArrayList;

public class RecordVO implements Serializable{
	public final static String PRAISED = "1";
	public final static String NOPRAISED = "0";
	private String id;
	private String serverid;
	private String path;
	private String name;
	private String size;
	private String date;
	private String length;
	private String users;
	private String state;
//	 "title": <string> (语音标题),
//     "tutorName": <string> (讲师名称),
//     "audioAddr": <string> (音频地址),
//     "crtDate": <string> (创建日期)
	private String title;
	private String tutorName;
	private String audioAddr;
	private String crtDate;
	private String praise;
	private ArrayList<String> userids = new ArrayList<String>();

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getLength() {
		return length;
	}

	public void setLength(String length) {
		this.length = length;
	}

	public String getUsers() {
		return users;
	}

	public void setUsers(String users) {
		this.users = users;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public ArrayList<String> getUserids() {
		return userids;
	}

	public void setUserids(ArrayList<String> userids) {
		this.userids = userids;
	}

	public String getServerid() {
		return serverid;
	}

	public void setServerid(String serverid) {
		this.serverid = serverid;
	}

	public String getTitle() {
		return title;
	}

	public String getTutorName() {
		return tutorName;
	}

	public String getAudioAddr() {
		return audioAddr;
	}

	public String getCrtDate() {
		return crtDate;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setTutorName(String tutorName) {
		this.tutorName = tutorName;
	}

	public void setAudioAddr(String audioAddr) {
		this.audioAddr = audioAddr;
	}

	public String getPraise() {
		return praise;
	}

	public void setPraise(String praise) {
		this.praise = praise;
	}

	public void setCrtDate(String crtDate) {
		this.crtDate = crtDate;
	}

}
