package com.cpstudio.zhuojiaren.model;

import java.util.ArrayList;
import java.util.List;

public class ZhuoInfoVO {
	
	
	
	
	
	private String msgid;
	private UserVO user = new UserVO();
	private String type;//包括，家人动态(发布的个人动态消息)，名片动态()，圈子动态(谁加入了圈子，退出圈子，创建圈子？)，倬脉动态(谁发布了啥？？？)
	private String category;
	private String title;
	private String text;
	private String position;
	private List<String> tags = new ArrayList<String>();
	private List<PicVO> pic = new ArrayList<PicVO>();
	private String goodnum;
	private String cmtnum;
	private String collectnum;
	private String forwardnum;
	private List<UserVO> good = new ArrayList<UserVO>();
	private List<CmtVO> cmt = new ArrayList<CmtVO>();
	private String addtime;
	private String iscollect;
	private String isgood;
	private String iscmt;
	private ZhuoInfoVO origin;

	public String getMsgid() {
		return msgid;
	}

	public void setMsgid(String msgid) {
		this.msgid = msgid;
	}

	public String getIscollect() {
		return iscollect;
	}

	public void setIscollect(String iscollect) {
		this.iscollect = iscollect;
	}

	public String getIsgood() {
		return isgood;
	}

	public void setIsgood(String isgood) {
		this.isgood = isgood;
	}

	public String getIscmt() {
		return iscmt;
	}

	public void setIscmt(String iscmt) {
		this.iscmt = iscmt;
	}

	public UserVO getUser() {
		return user;
	}

	public void setUser(UserVO user) {
		this.user = user;
	}

	public String getCollectnum() {
		return collectnum;
	}

	public void setCollectnum(String collectnum) {
		this.collectnum = collectnum;
	}

	public String getForwardnum() {
		return forwardnum;
	}

	public void setForwardnum(String forwardnum) {
		this.forwardnum = forwardnum;
	}

	public List<String> getTags() {
		return tags;
	}

	public void setTags(List<String> tags) {
		this.tags = tags;
	}

	public List<PicVO> getPic() {
		return pic;
	}

	public void setPic(List<PicVO> pic) {
		this.pic = pic;
	}

	public String getGoodnum() {
		return goodnum;
	}

	public void setGoodnum(String goodnum) {
		this.goodnum = goodnum;
	}

	public String getCmtnum() {
		return cmtnum;
	}

	public void setCmtnum(String cmtnum) {
		this.cmtnum = cmtnum;
	}

	public List<CmtVO> getCmt() {
		return cmt;
	}

	public void setCmt(List<CmtVO> cmt) {
		this.cmt = cmt;
	}

	public List<UserVO> getGood() {
		return good;
	}

	public void setGood(List<UserVO> good) {
		this.good = good;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public String getAddtime() {
		return addtime;
	}

	public void setAddtime(String addtime) {
		this.addtime = addtime;
	}

	public ZhuoInfoVO getOrigin() {
		return origin;
	}

	public void setOrigin(ZhuoInfoVO origin) {
		this.origin = origin;
	}

}
