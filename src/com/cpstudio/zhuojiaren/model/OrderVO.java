package com.cpstudio.zhuojiaren.model;

import java.util.List;

/**
 * order goods
 * 
 * @author lef
 * 
 */
public class OrderVO {
	// ��Ʒ
	private List<GoodsVO> buyGoods;
	// ������
	private String billNo;
	// ��Ʊ��Ϣ
	private String invoice;
	// ���̼�����
	private String message;

	private String userid;
	// (پ���ܼ�)
	private int totalZhuobi;
	// <int> (����״̬ 0-δ֧�� 1-�ѳɹ�֧�� 2-֧��ʧ�� 3-����ȡ�� 4-�����ѷ��� 5-������ջ�) ,
	private int status;
	// ��������ʱ��
	private String gentime;
	// ����֧��ʱ��
	private String paytime;
	// ��������ʱ��
	private String sendtime;
	// �����ջ�ʱ��
	private String receipttime;
	// �ջ���
	private String receiver;
	// �ջ���ַ
	private String receiverAddr;
	// �ջ��˵绰
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
