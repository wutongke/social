package com.cpstudio.zhuojiaren.model;

import java.io.Serializable;

public class gtype implements Serializable{
	private int id;

	private String content;

	public void setId(int id) {
		this.id = id;
	}

	public int getId() {
		return this.id;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public gtype(int id, String content) {
		super();
		this.id = id;
		this.content = content;
	}

	public String getContent() {
		return this.content;
	}

}