package com.cpstudio.zhuojiaren.model;

import java.util.List;

public class UserEvent {

	List<EventVO> createActs;
	List<EventVO> joinActs;
	public List<EventVO> getCreateActs() {
		return createActs;
	}
	public void setCreateActs(List<EventVO> createActs) {
		this.createActs = createActs;
	}
	public List<EventVO> getJoinActs() {
		return joinActs;
	}
	public void setJoinActs(List<EventVO> joinActs) {
		this.joinActs = joinActs;
	}
	
}
