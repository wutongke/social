package com.cpstudio.zhuojiaren.model;

import java.util.List;

public class Dynamic {
	
	public static final int DYNATIC_TYPE_MY_JIAREN=0;
	public static final int DYNATIC_TYPE_SB_JIAREN=1;
	public static final int DYNATIC_TYPE_ALL_JIAREN=2;
	
	private String statusid;

	private String content;

	private int type;

	private String shareid;

	private String userid;

	private String name;

	private String uheader;

	private int position;

	private String addtime;

	private List<PicNewVO> statusPic;

	public void setStatusid(String statusid) {
		this.statusid = statusid;
	}

	public String getStatusid() {
		return this.statusid;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getContent() {
		return this.content;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getType() {
		return this.type;
	}

	public void setShareid(String shareid) {
		this.shareid = shareid;
	}

	public String getShareid() {
		return this.shareid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getUserid() {
		return this.userid;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return this.name;
	}

	public void setUheader(String uheader) {
		this.uheader = uheader;
	}

	public String getUheader() {
		return this.uheader;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	public int getPosition() {
		return this.position;
	}

	public void setAddtime(String addtime) {
		this.addtime = addtime;
	}

	public String getAddtime() {
		return this.addtime;
	}

	public void setStatusPic(List<PicNewVO> statusPic) {
		this.statusPic = statusPic;
	}

	public List<PicNewVO> getStatusPic() {
		return this.statusPic;
	}
}
