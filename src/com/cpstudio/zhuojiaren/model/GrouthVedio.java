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
	public String getBrowerCount() {
		return browerCount;
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
	public void setBrowerCount(String browerCount) {
		this.browerCount = browerCount;
	}
	public void setDuration(String duration) {
		this.duration = duration;
	}
	private String imageAddr;
	private String vedioAddr;
	private String tutorName;
	private String typeName;
	private String crtDate;
	//�ۿ�����
	private String browerCount;
	//ʱ��
	private String duration;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}

}
