package com.cpstudio.zhuojiaren.model;

import java.io.Serializable;
import java.util.List;

/**
 * 基本编码数据(除了)
 * 
 * @author lz
 * 
 */
public class GXTypeItemVO implements Serializable {
	private String title;

	private List<gtype> sdtype;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public List<gtype> getSdtype() {
		return sdtype;
	}

	public void setSdtype(List<gtype> sdtype) {
		this.sdtype = sdtype;
	}

}