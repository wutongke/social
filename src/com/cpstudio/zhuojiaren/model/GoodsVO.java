package com.cpstudio.zhuojiaren.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class GoodsVO implements Serializable{
	private String goodsId;
	// 商品名字
	private String goodsName;
	// 商品价格
	private String markeyPrice;
	private String zhuoPrice;// 倬币价格(多少个)
	// 商品图片
	private List<PicVO> pic = new ArrayList<PicVO>();
	// 商品首页图片
	private String img;
	// 商品描述
	private String content;
	// 添加时间
	private String addtime;

	private String commentPoint;// 好评百分比
	private int orderNum;// 付款人数
	// 商家名字
	private String companyName;
	//商家图片
	private PicVO companyPic;
	// 商家描述
	private String companyDes;
	// 是否收藏
	private String isCollection;
	//订单时候用的商品数量
	private String goodsCount;
	public String getGoodsId() {
		return goodsId;
	}
	public String getGoodsName() {
		return goodsName;
	}
	public String getMarkeyPrice() {
		return markeyPrice;
	}
	public String getZhuoPrice() {
		return zhuoPrice;
	}
	public List<PicVO> getPic() {
		return pic;
	}
	public String getImg() {
		return img;
	}
	public String getContent() {
		return content;
	}
	public String getAddtime() {
		return addtime;
	}
	public String getCommentPoint() {
		return commentPoint;
	}
	public int getOrderNum() {
		return orderNum;
	}
	public String getCompanyName() {
		return companyName;
	}
	public PicVO getCompanyPic() {
		return companyPic;
	}
	public String getCompanyDes() {
		return companyDes;
	}
	public String getIsCollection() {
		return isCollection;
	}
	public String getGoodsCount() {
		return goodsCount;
	}
	public void setGoodsId(String goodsId) {
		this.goodsId = goodsId;
	}
	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}
	public void setMarkeyPrice(String markeyPrice) {
		this.markeyPrice = markeyPrice;
	}
	public void setZhuoPrice(String zhuoPrice) {
		this.zhuoPrice = zhuoPrice;
	}
	public void setPic(List<PicVO> pic) {
		this.pic = pic;
	}
	public void setImg(String img) {
		this.img = img;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public void setAddtime(String addtime) {
		this.addtime = addtime;
	}
	public void setCommentPoint(String commentPoint) {
		this.commentPoint = commentPoint;
	}
	public void setOrderNum(int orderNum) {
		this.orderNum = orderNum;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	public void setCompanyPic(PicVO companyPic) {
		this.companyPic = companyPic;
	}
	public void setCompanyDes(String companyDes) {
		this.companyDes = companyDes;
	}
	public void setIsCollection(String isCollection) {
		this.isCollection = isCollection;
	}
	public void setGoodsCount(String goodsCount) {
		this.goodsCount = goodsCount;
	}

	

}
