package com.cpstudio.zhuojiaren.model;

public class BankCard {

	private String bankName;
	private String bankImage;
	private String bankNumber;
	private String id;
	private String cardType;
	public String getBankName() {
		return bankName;
	}
	public String getBankImage() {
		return bankImage;
	}
	public String getBankNumber() {
		return bankNumber;
	}
	public String getId() {
		return id;
	}
	public String getCardType() {
		return cardType;
	}
	public void setBankName(String bankName) {
		this.bankName = bankName;
	}
	public void setBankImage(String bankImage) {
		this.bankImage = bankImage;
	}
	public void setBankNumber(String bankNumber) {
		this.bankNumber = bankNumber;
	}
	public void setId(String id) {
		this.id = id;
	}
	public void setCardType(String cardType) {
		this.cardType = cardType;
	}
}
