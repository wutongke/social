package com.cpstudio.zhuojiaren.model;

public class MessagePubVO {
	private String publish;

	private String id;

	private String content;

	private String pubtime;
	private String pubpic;

	public String getPubpic() {
		return pubpic;
	}

	public void setPubpic(String pubpic) {
		this.pubpic = pubpic;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getPubtime() {
		return pubtime;
	}

	public void setPubtime(String pubtime) {
		this.pubtime = pubtime;
	}

	public void setPublish(String publish) {
		this.publish = publish;
	}

	public String getPublish() {
		return this.publish;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

}