package com.cpstudio.zhuojiaren.model;

public class CmtVO {
	private String id;
	private String addtime;
	private String content;
	private UserVO user = new UserVO();
	private String parentid;
	private String msgid;
	private String likecnt;
	private boolean like = false;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public UserVO getUser() {
		return user;
	}

	public void setUser(UserVO user) {
		this.user = user;
	}

	public String getMsgid() {
		return msgid;
	}

	public void setMsgid(String msgid) {
		this.msgid = msgid;
	}

	public String getAddtime() {
		return addtime;
	}

	public void setAddtime(String addtime) {
		this.addtime = addtime;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getParentid() {
		return parentid;
	}

	public void setParentid(String parentid) {
		this.parentid = parentid;
	}

	public String getLikecnt() {
		return likecnt;
	}

	public void setLikecnt(String likecnt) {
		this.likecnt = likecnt;
	}

	public boolean isLike() {
		return like;
	}

	public void setLike(boolean like) {
		this.like = like;
	}
	
	
}
