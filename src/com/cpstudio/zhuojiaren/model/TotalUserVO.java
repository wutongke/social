package com.cpstudio.zhuojiaren.model;

import java.util.ArrayList;

public class TotalUserVO {
	private String total;
	private ArrayList<UserVO> data = new ArrayList<UserVO>();

	public String getTotal() {
		return total;
	}

	public void setTotal(String total) {
		this.total = total;
	}

	public ArrayList<UserVO> getData() {
		return data;
	}

	public void setData(ArrayList<UserVO> data) {
		this.data = data;
	}

}
