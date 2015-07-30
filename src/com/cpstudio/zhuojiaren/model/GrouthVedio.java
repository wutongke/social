package com.cpstudio.zhuojiaren.model;

import java.io.Serializable;

/**
 * 成长视频
 * @author lef
 *
 */
public class GrouthVedio implements Serializable{
//	 "id": <int> (公告id),
//     "imageAddr": <string> (展示图片url，可以使用七牛缩略api进行缩略),
//     "vedioAddr": <string> (视频url),
//     "tutorName": <string> (讲师名称),
//     "typeName": <string> (成长在线类型名称),
//     "crtDate": <string> (创建日期)
	private String id;
	private String imageAddr;
	private String vedioAddr;
	private String tutorName;
	private String typeName;
	private String crtDate;
	//观看人数
	private String viewCount;
	//时长
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
