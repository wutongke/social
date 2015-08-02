package com.cpstudio.zhuojiaren.model;

import java.util.ArrayList;
import java.util.List;
/**
 * @since 2015.6.15
 * @author lef
 *
 */
public class PayBackVO {
	String name;
	String payBackId;
	String amount;
	String limit;
	String intro;
	String supportCount;
	String pics ;
	
	public String getPics() {
		return pics;
	}
	public void setPics(String pics) {
		this.pics = pics;
	}
	public String getName() {
		return name;
	}
	public String getPayBackId() {
		return payBackId;
	}
	public String getAmount() {
		return amount;
	}
	public String getLimit() {
		return limit;
	}
	public String getIntro() {
		return intro;
	}
	public String getSupportCount() {
		return supportCount;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setPayBackId(String payBackId) {
		this.payBackId = payBackId;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	public void setLimit(String limit) {
		this.limit = limit;
	}
	public void setIntro(String intro) {
		this.intro = intro;
	}
	public void setSupportCount(String supportCount) {
		this.supportCount = supportCount;
	}
	
}
