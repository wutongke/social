package com.cpstudio.zhuojiaren.widget;

public class LoadImageTask {
	String url;
	int sampleHeight;
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public int getSampleHeight() {
		return sampleHeight;
	}
	public void setSampleHeight(int sampleHeight) {
		this.sampleHeight = sampleHeight;
	}
	public LoadImageTask(String url, int sampleHeight) {
		super();
		this.url = url;
		this.sampleHeight = sampleHeight;
	}
	
	
}
