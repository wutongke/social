package com.cpstudio.zhuojiaren.model;

public class PicAdVO {

	public static int AD_MAIN = 0, AD_FIND = 1, AD_JINJIN = 2, AD_STORE = 3,
			AD_ZHONGCHOU = 4;

	private int adno;

	private String adpic;

	private String adlink;

	private String goodsid;

	public String getAdlink() {
		return adlink;
	}

	public void setAdlink(String adlink) {
		this.adlink = adlink;
	}

	public int getAdno() {
		return adno;
	}

	public void setAdno(int adno) {
		this.adno = adno;
	}

	public String getAdpic() {
		return adpic;
	}

	public void setAdpic(String adpic) {
		this.adpic = adpic;
	}

	public String getGoodsid() {
		return goodsid;
	}

	public void setGoodsid(String goodsid) {
		this.goodsid = goodsid;
	}

}
