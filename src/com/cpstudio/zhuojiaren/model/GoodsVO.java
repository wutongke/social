package com.cpstudio.zhuojiaren.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class GoodsVO implements Serializable{
	private String gid;
	// ��Ʒ����
	private String name;
	// ��Ʒ�۸�
	private String price;
	private String money;
	// ��ƷͼƬ
	private List<PicVO> pic = new ArrayList<PicVO>();
	// ��Ʒ��ҳͼƬ
	private PicVO firstPic;
	// ��Ʒ����
	private String detail;
	// ���ʱ��
	private String addtime;

	private String zhuobi;// پ�Ҽ۸�(���ٸ�)
	private String goodCmtRate;// �����ٷֱ�
	private int payerNum;// ��������
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

	public String getGoodsCount() {
		return goodsCount;
	}

	public void setGoodsCount(String goodsCount) {
		this.goodsCount = goodsCount;
	}

	public String getGoodCmtRate() {
		return goodCmtRate;
	}

	public void setGoodCmtRate(String goodCmtRate) {
		this.goodCmtRate = goodCmtRate;
	}

	public int getPayerNum() {
		return payerNum;
	}

	public void setPayerNum(int payerNum) {
		this.payerNum = payerNum;
	}

	public String getZhuobi() {
		return zhuobi;
	}

	public void setZhuobi(String zhuobi) {
		this.zhuobi = zhuobi;
	}

	public String getGid() {
		return gid;
	}

	public void setGid(String gid) {
		this.gid = gid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getMoney() {
		return money;
	}

	public void setMoney(String money) {
		this.money = money;
	}

	public List<PicVO> getPic() {
		return pic;
	}

	public void setPic(List<PicVO> pic) {
		this.pic = pic;
		this.firstPic=pic.get(0);
	}

	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}

	public String getAddtime() {
		return addtime;
	}

	public String getCompanyName() {
		return companyName;
	}

	public String getCompanyDes() {
		return companyDes;
	}

	public String getIsCollection() {
		return isCollection;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public void setCompanyDes(String companyDes) {
		this.companyDes = companyDes;
	}

	public void setIsCollection(String isCollection) {
		this.isCollection = isCollection;
	}

	public void setAddtime(String addtime) {
		this.addtime = addtime;
	}

	public PicVO getCompanyPic() {
		return companyPic;
	}

	public void setCompanyPic(PicVO companyPic) {
		this.companyPic = companyPic;
	}

	public PicVO getFirstPic() {
		return firstPic;
	}

	public void setFirstPic(PicVO firstPic) {
		this.firstPic = firstPic;
	}

}
