package com.cpstudio.zhuojiaren.model;

public class ImQuanVO {
	private String id;
	private UserVO sender = new UserVO();
	private String type;
	private String content;
	private String file;
	private String isread;
	private QuanVO group = new QuanVO();
	private String addtime;
	private String savepath;
	private String secs;
	private boolean showtime = false;

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

	public String getFile() {
		return file;
	}

	public void setFile(String file) {
		this.file = file;
	}

	public String getIsread() {
		return isread;
	}

	public void setIsread(String isread) {
		this.isread = isread;
	}

	public QuanVO getGroup() {
		return group;
	}

	public void setGroup(QuanVO group) {
		this.group = group;
	}

	public String getAddtime() {
		return addtime;
	}

	public void setAddtime(String addtime) {
		this.addtime = addtime;
	}

	public String getSavepath() {
		return savepath;
	}

	public void setSavepath(String savepath) {
		this.savepath = savepath;
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
