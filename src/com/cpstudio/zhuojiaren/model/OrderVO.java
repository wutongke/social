package com.cpstudio.zhuojiaren.model;

import java.util.List;
/**
 * order goods
 * @author lef
 *
 */
public class OrderVO {
	//商品
	private List<GoodsVO> goodsList;
	//订单号
	private String orderNum;
	//时间
	private String orderTime;
	
	private UserVO user;
	
	private LocateVO locate;

	public List<GoodsVO> getGoodsList() {
		return goodsList;
	}

	public String getOrderNum() {
		return orderNum;
	}


	public String getOrderTime() {
		return orderTime;
	}

	public UserVO getUser() {
		return user;
	}

	public LocateVO getLocate() {
		return locate;
	}

	public void setGoodsList(List<GoodsVO> goodsList) {
		this.goodsList = goodsList;
	}

	public void setOrderNum(String orderNum) {
		this.orderNum = orderNum;
	}

	public void setOrderTime(String orderTime) {
		this.orderTime = orderTime;
	}

	public void setUser(UserVO user) {
		this.user = user;
	}

	public void setLocate(LocateVO locate) {
		this.locate = locate;
	}
}
