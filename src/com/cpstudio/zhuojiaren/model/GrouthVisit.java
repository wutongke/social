package com.cpstudio.zhuojiaren.model;

import java.io.Serializable;

/***
 * �ɳ�ר��
 * 
 * @author lef
 * 
 */
public class GrouthVisit implements Serializable {
	// "interviewName": <string> (���ɷ�������),
	// "title": <string> (�ɷñ���),
	// "content": <string> (�ɷ�����),
	// "callname": <string> (�ɷ�������),
	// "calldate": <string> (�ɷ�����)
	private String id;
	private String content;
	private String calldate;
	private String interviewName;
	private String title;
	private String callname;
	private String imageAddr;

	public String getId() {
		return id;
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

	public String getImageAddr() {
		return imageAddr;
	}

	public void setImageAddr(String imageAddr) {
		this.imageAddr = imageAddr;
	}

	public void setCallname(String callname) {
		this.callname = callname;
	}
}
