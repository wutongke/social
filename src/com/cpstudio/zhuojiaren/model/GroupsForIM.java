package com.cpstudio.zhuojiaren.model;

import java.util.List;
/**
 * 群组，其实就是圈子，包括我创建的圈子和我加入的圈子
 * @author lz
 *
 */
public class GroupsForIM {
	List<QuanVO> createGroups;
	List<QuanVO> followGroups;
	public List<QuanVO> getCreateGroups() {
		return createGroups;
	}
	public void setCreateGroups(List<QuanVO> createGroups) {
		this.createGroups = createGroups;
	}
	public List<QuanVO> getFollowGroups() {
		return followGroups;
	}
	public void setFollowGroups(List<QuanVO> followGroups) {
		this.followGroups = followGroups;
	}
	
}
