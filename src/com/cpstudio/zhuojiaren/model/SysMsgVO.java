package com.cpstudio.zhuojiaren.model;

public class SysMsgVO {
	private String id;
	private UserVO sender = new UserVO();
	private UserVO receiver = new UserVO();
	private String isread;
	private String addtime;
	private String groupid;
	private String gname;
	private String type;
	private String content;
	private UserVO user = new UserVO();

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public UserVO getSender() {
		return sender;
	}

	public void setSender(UserVO sender) {
		this.sender = sender;
	}

	public UserVO getReceiver() {
		return receiver;
	}

	public void setReceiver(UserVO receiver) {
		this.receiver = receiver;
	}

	public String getIsread() {
		return isread;
	}

	public void setIsread(String isread) {
		this.isread = isread;
	}

	public String getAddtime() {
		return addtime;
	}

	public void setAddtime(String addtime) {
		this.addtime = addtime;
	}

	public String getGroupid() {
		return groupid;
	}

	public void setGroupid(String groupid) {
		this.groupid = groupid;
	}

	public String getGroupname() {
		return gname;
	}

	public void setGroupname(String gname) {
		this.gname = gname;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public UserVO getUser() {
		return user;
	}

	public void setUser(UserVO user) {
		this.user = user;
	}

}
