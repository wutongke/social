package com.cpstudio.zhuojiaren.model;

import java.util.List;

public class GroupStatus {
	public final static int GROUP_STATUS_TYPE_ALL=0; 
	public final static int GROUP_STATUS_TYPE_CREATED=1; 
	public final static int GROUP_STATUS_TYPE_JOINED=2; 
	
	private int type;
	private EventVO groupActivity;
	private QuanTopicVO groupTopic;
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public EventVO getGroupActivity() {
		return groupActivity;
	}
	public void setGroupActivity(EventVO groupActivity) {
		this.groupActivity = groupActivity;
	}
	public QuanTopicVO getGroupTopic() {
		return groupTopic;
	}
	public void setGroupTopic(QuanTopicVO groupTopic) {
		this.groupTopic = groupTopic;
	}
	
}
