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
	String cfId;
	String amount;
	String limits="0";
	String intro;
	String num;
	public String getLimits() {
		return limits;
	}
	public void setLimits(String limits) {
		this.limits = limits;
	}
	String pics ;
	public String getName() {
		return name;
	}
	public String getCfId() {
		return cfId;
	}
	public String getAmount() {
		return amount;
	}
	public String getIntro() {
		return intro;
	}
	public String getNum() {
		return num;
	}
	public String getPics() {
		return pics;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setCfId(String cfId) {
		this.cfId = cfId;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	public void setIntro(String intro) {
		this.intro = intro;
	}
	public void setNum(String num) {
		this.num = num;
	}
	public void setPics(String pics) {
		this.pics = pics;
	}
	
	
	
}
