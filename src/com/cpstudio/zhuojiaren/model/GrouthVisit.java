package com.cpstudio.zhuojiaren.model;

import java.io.Serializable;

/***
 * 成长专访
 * @author lef
 *
 */
public class GrouthVisit implements Serializable{
//	 "interviewName": <string> (被采访者名称),
//     "title": <string> (采访标题),
//     "content": <string> (采访内容),
//     "callname": <string> (采访者名称),
//     "calldate": <string> (采访日期)
	private String id;
	private String imageUrl;
	/**
	 *  第几期
	 */
	private String order;
	
	private String content;
	private String calldate;
	private String interviewName;
	private String title;
	private String callname;
	public String getId() {
		return id;
	}
	public String getImageUrl() {
		return imageUrl;
	}
	public String getOrder() {
		return order;
	}
	public String getContent() {
		return content;
	}
	public String getCalldate() {
		return calldate;
	}
	public String getInterviewName() {
		return interviewName;
	}
	public String getTitle() {
		return title;
	}
	public String getCallname() {
		return callname;
	}
	public void setId(String id) {
		this.id = id;
	}
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	public void setOrder(String order) {
		this.order = order;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public void setCalldate(String calldate) {
		this.calldate = calldate;
	}
	public void setInterviewName(String interviewName) {
		this.interviewName = interviewName;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public void setCallname(String callname) {
		this.callname = callname;
	}
}
