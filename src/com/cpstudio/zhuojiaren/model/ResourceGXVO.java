package com.cpstudio.zhuojiaren.model;

import java.util.ArrayList;
import java.util.List;

public class ResourceGXVO {

	// lz
	public static String RESOURCEGXTYPE = "resource_gongxu_type";// 寻找资源
	public static String RESOURCEGXFILTER_LOCATION = "resource_filter_location";//
	public static String RESOURCEGXFILTER_TYPE = "resource_filter_type";//
	// 供需 的各种类型
	public static int RESOURCE_FIND = 0;// 寻找资源
	public static int NEED_FIND = 1;// 发现需求
	public static int PUB_NEED_RESOURCE = 2;// 供需发布

	String msgId;
	
	private String title;
	String detailContent;
	String industry;// 行业
	String fund;// 资金
	int tag;
	private String addtime;
	String imgUrl;// 列表显示的图片
	private List<PicVO> pic = new ArrayList<PicVO>();// 图片详情
	private List<CmtVO> cmt = new ArrayList<CmtVO>();// 评论
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

	public List<PicVO> getPic() {
		return pic;
	}

	public void setPic(List<PicVO> pic) {
		this.pic = pic;
	}

	public List<CmtVO> getCmt() {
		return cmt;
	}

	public void setCmt(List<CmtVO> cmt) {
		this.cmt = cmt;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getIndustry() {
		return industry;
	}

	public void setIndustry(String industry) {
		this.industry = industry;
	}

	public String getFund() {
		return fund;
	}

	public void setFund(String fund) {
		this.fund = fund;
	}

	public int getTag() {
		return tag;
	}

	public void setTag(int tag) {
		this.tag = tag;
	}

	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

}
