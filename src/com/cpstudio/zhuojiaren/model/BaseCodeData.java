package com.cpstudio.zhuojiaren.model;

import java.io.Serializable;
import java.util.List;

/**
 * 基本编码数据(除了)
 * 
 * @author lz
 * 
 */
public class BaseCodeData implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2637852629534005023L;

	private List<zodiac> zodiac;

	private List<constellation> constellation;

	private List<hobby> hobby;

	private List<industry> industry;

	private List<position> position;

	private List<gtype> gtype;

	private List<sdtype> teacherType;

	public List<sdtype> getTeacherType() {
		return teacherType;
	}

	public void setTeacherType(List<sdtype> teacherType) {
		this.teacherType = teacherType;
	}


	public void setZodiac(List<zodiac> zodiac) {
		this.zodiac = zodiac;
	}

	public List<zodiac> getZodiac() {
		return this.zodiac;
	}

	public void setConstellation(List<constellation> constellation) {
		this.constellation = constellation;
	}

	public List<constellation> getConstellation() {
		return this.constellation;
	}

	public void setHobby(List<hobby> hobby) {
		this.hobby = hobby;
	}

	public List<hobby> getHobby() {
		return this.hobby;
	}

	public void setIndustry(List<industry> industry) {
		this.industry = industry;
	}

	public List<industry> getIndustry() {
		return this.industry;
	}

	public void setPosition(List<position> position) {
		this.position = position;
	}

	public List<position> getPosition() {
		return this.position;
	}

	public void setGtype(List<gtype> gtype) {
		this.gtype = gtype;
	}

	public List<gtype> getGtype() {
		return this.gtype;
	}

}