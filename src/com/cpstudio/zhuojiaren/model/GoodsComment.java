package com.cpstudio.zhuojiaren.model;

public class GoodsComment {

	String id;
	String userId;
	String content;
	String goodsId;
	String img;
	public String getImg() {
		return img;
	}
	public void setImg(String img) {
		this.img = img;
	}
	public String getId() {
		return id;
	}
	public String getUserId() {
		return userId;
	}
	public String getContent() {
		return content;
	}
	public String getGoodsId() {
		return goodsId;
	}
	public void setId(String id) {
		this.id = id;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public void setGoodsId(String goodsId) {
		this.goodsId = goodsId;
	}
}
