package com.cpstudio.zhuojiaren.model;

import java.util.ArrayList;
import java.util.List;

public class GoodsVO {
	private String gid;
	private String name;
	private String price;
	private String money;
	private List<PicVO> pic = new ArrayList<PicVO>();
	private String detail;
	private String addtime;
	
//	add by lz
	private String zhuobi;//倬币价格(多少个)
	private String goodCmtRate;//好评百分比
	private int payerNum;//付款人数
	
	public String getGoodCmtRate() {
		return goodCmtRate;
	}

	public void setGoodCmtRate(String goodCmtRate) {
		this.goodCmtRate = goodCmtRate;
	}

	public int  getPayerNum() {
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

	public void setAddtime(String addtime) {
		this.addtime = addtime;
	}

}
