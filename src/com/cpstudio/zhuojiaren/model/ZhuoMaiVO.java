package com.cpstudio.zhuojiaren.model;


public class ZhuoMaiVO {

	String msgId;

	private String title;
	String detailContent;
	private String addtime;
	String pubpic;// 列表显示的图片

	String isCollect;
	UserVO owner;

	public String getMsgId() {
		return msgId;
	}

	public void setMsgId(String msgId) {
		this.msgId = msgId;
	}

	public UserVO getOwner() {
		return owner;
	}

	public void setOwner(UserVO owner) {
		this.owner = owner;
	}

	public String getIsCollect() {
		return isCollect;
	}

	public void setIsCollect(String isCollect) {
		this.isCollect = isCollect;
	}

	public String getAddtime() {
		return addtime;
	}

	public void setAddtime(String addtime) {
		this.addtime = addtime;
	}

	public String getDetailContent() {
		return detailContent;
	}

	public void setDetailContent(String detailContent) {
		this.detailContent = detailContent;
	}

	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getPubpic() {
		return pubpic;
	}

	public void setPubpic(String pubpic) {
		this.pubpic = pubpic;
	}

}
