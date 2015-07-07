package com.cpstudio.zhuojiaren.model;

import java.util.ArrayList;
import java.util.List;

public class GoodsVO {
	private String gid;
	// 商品名字
	private String name;
	// 商品价格
	private String price;
	private String money;
	// 商品图片
	private List<PicVO> pic = new ArrayList<PicVO>();
	// 商品描述
	private String detail;
	// 添加时间
	private String addtime;

	private String zhuobi;// 倬币价格(多少个)
	private String goodCmtRate;// 好评百分比
	private int payerNum;// 付款人数
	// 商家名字
	private String companyName;
	//商家图片
	private PicVO companyPic;
	// 商家描述
	private String companyDes;
	// 是否收藏
	private String isCollection;

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

}
