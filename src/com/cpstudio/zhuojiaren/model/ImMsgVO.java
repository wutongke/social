package com.cpstudio.zhuojiaren.model;

public class ImMsgVO {
	private String id;
	private UserVO sender = new UserVO();
	private UserVO receiver = new UserVO();
	private String type;
	private String content;
	private String file;
	private String isread;
	private String groupid;
	private String addtime;
	private String secs;
	private String savepath;
	private boolean showtime = false;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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

	public String getGroupid() {
		return groupid;
	}

	public void setGroupid(String groupid) {
		this.groupid = groupid;
	}

	public String getSavepath() {
		return savepath;
	}

	public void setSavepath(String savepath) {
		this.savepath = savepath;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getIsread() {
		return isread;
	}

	public void setIsread(String isread) {
		this.isread = isread; // 0 未读 1已读 2失败 3接受中 4发送中
	}

	public String getFile() {
		return file;
	}

	public void setFile(String file) {
		this.file = file;
	}

	public String getSecs() {
		return secs;
	}

	public void setSecs(String secs) {
		this.secs = secs;
	}

	public boolean isShowtime() {
		return showtime;
	}

	public void setShowtime(boolean showtime) {
		this.showtime = showtime;
	}

}
