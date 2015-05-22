package com.cpstudio.zhuojiaren.model;

public class CardMsgVO {
	private String id;
	private UserVO sender = new UserVO();
	private UserVO receiver = new UserVO();
	private String leavemsg;
	private String isopen;
	private String state;
	private String isread;
	private String addtime;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getLeavemsg() {
		return leavemsg;
	}

	public void setLeavemsg(String leavemsg) {
		this.leavemsg = leavemsg;
	}

	public String getIsopen() {
		return isopen;
	}

	public void setIsopen(String isopen) {
		this.isopen = isopen;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getAddtime() {
		return addtime;
	}

	public void setAddtime(String addtime) {
		this.addtime = addtime;
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

}
