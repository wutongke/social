package com.cpstudio.zhuojiaren.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class GoodsVO implements Serializable{
	private String goodsId;
	// ��Ʒ����
	private String goodsName;
	// ��Ʒ�۸�
	private String markeyPrice;
	private String zhuoPrice;// پ�Ҽ۸�(���ٸ�)
	// ��ƷͼƬ
	private List<PicVO> pic = new ArrayList<PicVO>();
	// ��Ʒ��ҳͼƬ
	private String img;
	// ��Ʒ����
	private String content;
	// ���ʱ��
	private String addtime;

	private String commentPoint;// �����ٷֱ�
	private int orderNum;// ��������
	// �̼�����
	private String companyName;
	//�̼�ͼƬ
	private PicVO companyPic;
	// �̼�����
	private String companyDes;
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
