package com.cpstudio.zhuojiaren.model;

import java.util.List;

/**
 * order goods
 * 
 * @author lef
 * 
 */
public class OrderVO {
	// 商品
	private List<GoodsVO> buyGoods;
	// 订单号
	private String billNo;
	// 发票信息
	private String invoice;
	// 给商家留言
	private String message;

	private String userid;
	// (倬币总价)
	private int totalZhuobi;
	// <int> (订单状态 0-未支付 1-已成功支付 2-支付失败 3-订单取消 4-卖家已发货 5-买家已收货) ,
	private int status;
	// 订单生成时间
	private String gentime;
	// 订单支付时间
	private String paytime;
	// 订单发货时间
	private String sendtime;
	// 订单收货时间
	private String receipttime;
	// 收货人
	private String receiver;
	// 收货地址
	private String receiverAddr;
	// 收货人电话
	private String phone;

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getReceiver() {
		return receiver;
	}

	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}

	public String getReceiverAddr() {
		return receiverAddr;
	}

	public void setReceiverAddr(String receiverAddr) {
		this.receiverAddr = receiverAddr;
	}

	public List<GoodsVO> getBuyGoods() {
		return buyGoods;
	}

	public void setBuyGoods(List<GoodsVO> buyGoods) {
		this.buyGoods = buyGoods;
	}

	public String getBillNo() {
		return billNo;
	}

	public void setBillNo(String billNo) {
		this.billNo = billNo;
	}

	public String getInvoice() {
		return invoice;
	}

	public void setInvoice(String invoice) {
		this.invoice = invoice;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public int getTotalZhuobi() {
		return totalZhuobi;
	}

	public void setTotalZhuobi(int totalZhuobi) {
		this.totalZhuobi = totalZhuobi;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getGentime() {
		return gentime;
	}

	public void setGentime(String gentime) {
		this.gentime = gentime;
	}

	public String getPaytime() {
		return paytime;
	}

	public void setPaytime(String paytime) {
		this.paytime = paytime;
	}

	public String getSendtime() {
		return sendtime;
	}

	public void setSendtime(String sendtime) {
		this.sendtime = sendtime;
	}

	public String getReceipttime() {
		return receipttime;
	}

	public void setReceipttime(String receipttime) {
		this.receipttime = receipttime;
	}
}
