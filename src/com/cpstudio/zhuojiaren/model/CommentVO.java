package com.cpstudio.zhuojiaren.model;
/**
 * ÆÀÂÛ
 * @author lef
 *
 */
public class CommentVO {

	private String id;
	private UserVO user;
	private UserVO replyUser;
	private String time;
	private String content;
	private String crowdFundingId;
	private String isPraise;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public UserVO getUser() {
		return user;
	}
	public void setUser(UserVO user) {
		this.user = user;
	}
	public UserVO getReplyUser() {
		return replyUser;
	}
	public void setReplyUser(UserVO replyUser) {
		this.replyUser = replyUser;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getCrowdFundingId() {
		return crowdFundingId;
	}
	public void setCrowdFundingId(String crowdFundingId) {
		this.crowdFundingId = crowdFundingId;
	}
	public String getIsPraise() {
		return isPraise;
	}
	public void setIsPraise(String isPraise) {
		this.isPraise = isPraise;
	}
}
