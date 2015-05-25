package com.cpstudio.zhuojiaren.model;

public class CmtRcmdVO {
	private String id;
	private UserVO sender = new UserVO();
	private UserVO receiver = new UserVO();
	private String content;
	private String isread;
	private String addtime;
	private ZhuoInfoVO orgin = new ZhuoInfoVO();

	public ZhuoInfoVO getOrgin() {
		return orgin;
	}

	public void setOrgin(ZhuoInfoVO orgin) {
		this.orgin = orgin;
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

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
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

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

}
