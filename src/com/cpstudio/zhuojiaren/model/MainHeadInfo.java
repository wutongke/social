package com.cpstudio.zhuojiaren.model;

import java.util.List;

public class MainHeadInfo {
	private List<PicAdVO> admid;
	private	List<MessagePubVO> pub;
	private GoodsPicAdVO adtop;
	public List<PicAdVO> getAdmid() {
		return admid;
	}
	public void setAdmid(List<PicAdVO> admid) {
		this.admid = admid;
	}
	public List<MessagePubVO> getPub() {
		return pub;
	}
	public void setPub(List<MessagePubVO> pub) {
		this.pub = pub;
	}
	public GoodsPicAdVO getAdtop() {
		return adtop;
	}
	public void setAdtop(GoodsPicAdVO adtop) {
		this.adtop = adtop;
	}
	
	
}