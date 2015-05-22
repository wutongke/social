package com.cpstudio.zhuojiaren.model;

public class AboutUsVO {
	private String id;
	private String title;
	private String brief;
	private String content;
	private PicVO pic = new PicVO();
	private String addtime;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getBrief() {
		return brief;
	}

	public void setBrief(String brief) {
		this.brief = brief;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public PicVO getPic() {
		return pic;
	}

	public void setPic(PicVO pic) {
		this.pic = pic;
	}

	public String getAddtime() {
		return addtime;
	}

	public void setAddtime(String addtime) {
		this.addtime = addtime;
	}

}
