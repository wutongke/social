package com.cpstudio.zhuojiaren.model;

import java.io.Serializable;

/**
 * �ɳ���Ƶ
 * @author lef
 *
 */
public class GrouthVedio implements Serializable{
//	 "id": <int> (����id),
//     "imageAddr": <string> (չʾͼƬurl������ʹ����ţ����api��������),
//     "vedioAddr": <string> (��Ƶurl),
//     "tutorName": <string> (��ʦ����),
//     "typeName": <string> (�ɳ�������������),
//     "crtDate": <string> (��������)
	private String id;
	private String imageAddr;
	private String vedioAddr;
	private String tutorName;
	private String typeName;
	private String crtDate;
	//�ۿ�����
	private String viewCount;
	//ʱ��
	private String duration;
	public String getImageAddr() {
		return imageAddr;
	}
	public String getVedioAddr() {
		return vedioAddr;
	}
	public String getTutorName() {
		return tutorName;
	}
	public String getTypeName() {
		return typeName;
	}
	public String getCrtDate() {
		return crtDate;
	}
	public String getDuration() {
		return duration;
	}
	public void setImageAddr(String imageAddr) {
		this.imageAddr = imageAddr;
	}
	public void setVedioAddr(String vedioAddr) {
		this.vedioAddr = vedioAddr;
	}
	public void setTutorName(String tutorName) {
		this.tutorName = tutorName;
	}
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
	public void setCrtDate(String crtDate) {
		this.crtDate = crtDate;
	}
	public void setDuration(String duration) {
		this.duration = duration;
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getViewCount() {
		return viewCount;
	}
	public void setViewCount(String viewCount) {
		this.viewCount = viewCount;
	}

}
