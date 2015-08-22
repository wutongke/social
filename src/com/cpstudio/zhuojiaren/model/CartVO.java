package com.cpstudio.zhuojiaren.model;

import java.util.ArrayList;

public class CartVO {

	private String amount;
	private String cashBack = "0";
	private ArrayList<GoodsVO>goodsCartList;
	public String getAmount() {
		return amount;
	}
	public String getCashBack() {
		return cashBack;
	}
	public ArrayList<GoodsVO> getGoodsCartList() {
		return goodsCartList;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	public void setCashBack(String cashBack) {
		this.cashBack = cashBack;
	}
	public void setGoodsCartList(ArrayList<GoodsVO> goodsCartList) {
		this.goodsCartList = goodsCartList;
	}
	
}
