package com.cpstudio.zhuojiaren.model;

public class GrouthType {

	String id;
	String typeName;
	public String getId() {
		return id;
	}
	public String getTypeName() {
		return typeName;
	}
	public void setId(String id) {
		this.id = id;
	}
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return typeName;
	}
}
