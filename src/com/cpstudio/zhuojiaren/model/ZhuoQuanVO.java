package com.cpstudio.zhuojiaren.model;

import java.util.ArrayList;
import java.util.List;

public class ZhuoQuanVO {
	private String type;
	private String typeid;
	private List<QuanVO> groups = new ArrayList<QuanVO>();
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public List<QuanVO> getGroups() {
		return groups;
	}
	public void setGroups(List<QuanVO> groups) {
		this.groups = groups;
	}
	public String getTypeid() {
		return typeid;
	}
	public void setTypeid(String typeid) {
		this.typeid = typeid;
	}
}
