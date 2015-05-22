package com.cpstudio.zhuojiaren.model;

import java.util.ArrayList;
import java.util.List;

public class PagesCmtVO {
	private String pages;
	private List<CmtVO> data = new ArrayList<CmtVO>();
	public String getPages() {
		return pages;
	}
	public void setPages(String pages) {
		this.pages = pages;
	}
	public List<CmtVO> getData() {
		return data;
	}
	public void setData(List<CmtVO> data) {
		this.data = data;
	}
}
