package com.cpstudio.zhuojiaren.model;

import java.util.List;
/**
 * Ⱥ�飬��ʵ����Ȧ�ӣ������Ҵ�����Ȧ�Ӻ��Ҽ����Ȧ��
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
