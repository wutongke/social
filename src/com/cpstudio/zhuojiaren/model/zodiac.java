package com.cpstudio.zhuojiaren.model;

import java.io.Serializable;

/**
 * ��Ф
 * @author lz
 *
 */
public class zodiac implements Serializable{
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

	public String getContent() {
		return this.content;
	}

}