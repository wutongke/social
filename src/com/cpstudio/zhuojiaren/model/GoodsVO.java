package com.cpstudio.zhuojiaren.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class GoodsVO implements Serializable{
	public static String collected = "0";
	private String goodsId;
	// ��Ʒ����
	private String goodsName;
	// ��Ʒ�۸�
	private String markeyPrice;
	private int buyNum;
	public int getBuyNum() {
		return buyNum;
	}
	public void setBuyNum(int buyNum) {
		this.buyNum = buyNum;
	}
	public String getSubAmount() {
		return subAmount;
	}
	public void setSubAmount(String subAmount) {
		this.subAmount = subAmount;
	}
	private String subAmount;
	private String zhuoPrice;// پ�Ҽ۸�(���ٸ�)
	// ��ƷͼƬ
	private List<String> imgList = new ArrayList<String>();
	// ��Ʒ��ҳͼƬ
	private String goodsImg;
	private String img;
	// ��Ʒ����
	private String content;
	// ���ʱ��
	private String addtime;

	private String commentPoint;// �����ٷֱ�
	private int orderNum;// ��������
	private ProviderVO provider;
	private ArrayList<GoodsComment> comments;
	// �Ƿ��ղ�
	private String isCollection;
	//����ʱ���õ���Ʒ����
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
	public ProviderVO getProvider() {
		return provider;
	}
	public ArrayList<GoodsComment> getComments() {
		return comments;
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
	public List<String> getImgList() {
		return imgList;
	}
	public void setImgList(List<String> imgList) {
		this.imgList = imgList;
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
	public void setProvider(ProviderVO provider) {
		this.provider = provider;
	}
	public String getImg() {
		return img;
	}
	public void setImg(String img) {
		this.img = img;
	}
	public void setComments(ArrayList<GoodsComment> comments) {
		this.comments = comments;
	}
	public void setIsCollection(String isCollection) {
		this.isCollection = isCollection;
	}
	public void setGoodsCount(String goodsCount) {
		this.goodsCount = goodsCount;
	}
	public String getGoodsImg() {
		return goodsImg;
	}
	public void setGoodsImg(String goodsImg) {
		this.goodsImg = goodsImg;
	}

	

}
