package com.cpstudio.zhuojiaren.model;

import java.io.Serializable;

/**
 * ≥…≥§ ”∆µ
 * @author lef
 *
 */
public class GrouthVedio implements Serializable{
	private String id;
	private String imageUrl;
	private String videoUrl;
	private String name;
	private String browerCount;
	private String duration;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getImageUrl() {
		return imageUrl;
	}
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	public String getName() {
		return name;
	}
	public String getVideoUrl() {
		return videoUrl;
	}
	public void setVideoUrl(String videoUrl) {
		this.videoUrl = videoUrl;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getBrowerCount() {
		return browerCount;
	}
	public void setBrowerCount(String browerCount) {
		this.browerCount = browerCount;
	}
	public String getDuration() {
		return duration;
	}
	public void setDuration(String duration) {
		this.duration = duration;
	}
	

}
