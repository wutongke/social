package com.cpstudio.zhuojiaren.model;

import java.io.Serializable;

public class PicVO implements Serializable{
	private String thumburl;
	private String orgurl;
	private String desc;
	private String id;

	public String getUrl() {
		return thumburl;
	}

	public void setUrl(String url) {
		this.thumburl = url;
	}

	public String getOrgurl() {
		return orgurl;
	}

	public void setOrgurl(String bigurl) {
		this.orgurl = bigurl;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
}
