package com.cpstudio.zhuojiaren.model;

import java.util.ArrayList;
import java.util.List;

public class PushMsgVO {
	private List<CardMsgVO> exchangecard_msg = new ArrayList<CardMsgVO>();
	private List<ImQuanVO> groupchat = new ArrayList<ImQuanVO>();
	private List<ImMsgVO> userchats = new ArrayList<ImMsgVO>();
	private List<SysMsgVO> sys_msg = new ArrayList<SysMsgVO>();
	private List<CmtRcmdVO> comment_msg = new ArrayList<CmtRcmdVO>();
	private List<ImMsgVO> cloudchat = new ArrayList<ImMsgVO>();

	public List<CardMsgVO> getExchangecard_msg() {
		return exchangecard_msg;
	}

	public void setExchangecard_msg(List<CardMsgVO> exchangecard_msg) {
		this.exchangecard_msg = exchangecard_msg;
	}

	public List<ImQuanVO> getGroupchat() {
		return groupchat;
	}

	public void setGroupchat(List<ImQuanVO> groupchat) {
		this.groupchat = groupchat;
	}

	public List<ImMsgVO> getUserchats() {
		return userchats;
	}

	public void setUserchats(List<ImMsgVO> userchats) {
		this.userchats = userchats;
	}

	public List<SysMsgVO> getSys_msg() {
		return sys_msg;
	}

	public void setSys_msg(List<SysMsgVO> sys_msg) {
		this.sys_msg = sys_msg;
	}

	public List<CmtRcmdVO> getComment_msg() {
		return comment_msg;
	}

	public void setComment_msg(List<CmtRcmdVO> comment_msg) {
		this.comment_msg = comment_msg;
	}

	public List<ImMsgVO> getCloudchat() {
		return cloudchat;
	}

	public void setCloudchat(List<ImMsgVO> cloudchat) {
		this.cloudchat = cloudchat;
	}

}
