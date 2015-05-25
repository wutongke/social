package com.cpstudio.zhuojiaren.model;

import java.util.ArrayList;
import java.util.List;

public class TeacherVO {
	private String id;
	private String userid;
	private String sex;
	private String name;
	private String level;
	private String header;
	private String intro;
	private String zjkc;
	private String skfg;
	private String jzzz;
	private String dsyl;
	private String dsmx;
	private List<PicVO> dsfc = new ArrayList<PicVO>();

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public String getHeader() {
		return header;
	}

	public void setHeader(String header) {
		this.header = header;
	}

	public String getIntro() {
		return intro;
	}

	public void setIntro(String intro) {
		this.intro = intro;
	}

	public String getZjkc() {
		return zjkc;
	}

	public void setZjkc(String zjkc) {
		this.zjkc = zjkc;
	}

	public String getSkfg() {
		return skfg;
	}

	public void setSkfg(String skfg) {
		this.skfg = skfg;
	}

	public String getJzzz() {
		return jzzz;
	}

	public void setJzzz(String jzzz) {
		this.jzzz = jzzz;
	}

	public String getDsyl() {
		return dsyl;
	}

	public void setDsyl(String dsyl) {
		this.dsyl = dsyl;
	}

	public String getDsmx() {
		return dsmx;
	}

	public void setDsmx(String dsmx) {
		this.dsmx = dsmx;
	}

	public List<PicVO> getDsfc() {
		return dsfc;
	}

	public void setDsfc(List<PicVO> dsfc) {
		this.dsfc = dsfc;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

}
