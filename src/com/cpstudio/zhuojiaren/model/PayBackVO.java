package com.cpstudio.zhuojiaren.model;

import java.util.ArrayList;
/**
 * @since 2015.6.15
 * @author lef
 *
 */
public class PayBackVO {
	String name;
	String payBackId;
	String price;
	String maxCount;
	String des;
	String supportCount;
	ArrayList<String> imageUrl = new ArrayList<String>();
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPayBackId() {
		return payBackId;
	}
	public void setPayBackId(String payBackId) {
		this.payBackId = payBackId;
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	public String getMaxCount() {
		return maxCount;
	}
	public void setMaxCount(String maxCount) {
		this.maxCount = maxCount;
	}
	public String getSupportCount() {
		return supportCount;
	}
	public void setSupportCount(String supportCount) {
		this.supportCount = supportCount;
	}
	public ArrayList<String> getImageUrl() {
		return imageUrl;
	}
	public String getDes() {
		return des;
	}
	public void setDes(String des) {
		this.des = des;
	}
	public void setImageUrl(ArrayList<String> imageUrl) {
		this.imageUrl = imageUrl;
	}
	
}
