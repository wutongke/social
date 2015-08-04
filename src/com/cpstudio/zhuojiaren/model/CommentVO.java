package com.cpstudio.zhuojiaren.model;

import java.io.File;

import org.json.JSONObject;

import android.util.Log;

import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UploadManager;

/**
 * 评论
 * 
 * @author lef
 * 
 */
public class CommentVO {
	// "content": <string>(评论内容),
	// "id": <int> (评论id),
	// "toId":<int>(被回复id)
	// "addtime": <string>,
	// "isLike": <boolean> (是否点赞),
	// "company": <string> (所属公司),
	// "position":<string> (职位),
	// "name": <string> (用户名称),
	// "uheader": <string> (用户头像)
	public static String praise = "1";
	public static String nopraise = "-1";
	private String id;
	private String name;
	private String userid;
	private String toId;
	private String toUserid;

	private String toName;

	private String addtime;
	private String content;
	private String company;
	private String position;
	private String uheader;
	private String crowdFundingId;
	private String isPraise;

	public static String getPraise() {
		return praise;
	}

	public static String getNopraise() {
		return nopraise;
	}

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getToId() {
		return toId;
	}

	public String getAddtime() {
		return addtime;
	}

	public String getContent() {
		return content;
	}

	public String getCompany() {
		return company;
	}

	public String getPosition() {
		return position;
	}

	public String getUheader() {
		return uheader;
	}

	public String getCrowdFundingId() {
		return crowdFundingId;
	}

	public String getIsPraise() {
		return isPraise;
	}

	public static void setPraise(String praise) {
		CommentVO.praise = praise;
	}

	public static void setNopraise(String nopraise) {
		CommentVO.nopraise = nopraise;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setToId(String toId) {
		this.toId = toId;
	}

	public void setAddtime(String addtime) {
		this.addtime = addtime;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public String getToUserid() {
		return toUserid;
	}

	public void setToUserid(String toUserid) {
		this.toUserid = toUserid;
	}

	public String getToName() {
		return toName;
	}

	public void setToName(String toName) {
		this.toName = toName;
	}

	public void setUheader(String uheader) {
		this.uheader = uheader;
	}

	public void setCrowdFundingId(String crowdFundingId) {
		this.crowdFundingId = crowdFundingId;
	}

	public void setIsPraise(String isPraise) {
		this.isPraise = isPraise;
	}

}
