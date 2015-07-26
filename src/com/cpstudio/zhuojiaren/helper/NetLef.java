package com.cpstudio.zhuojiaren.helper;

import java.util.HashSet;
import java.util.Set;

import android.content.Context;

public class NetLef {

	private static NetLef instance;
	private String userid = null;
	private String password = null;
	// 标识每一次请求，请求开始后不重复请求
	private Set<String> mStartedTag = new HashSet<String>();

	private void init(Context context) {
		ResHelper resHelper = ResHelper.getInstance(context);
		this.userid = resHelper.getUserid();
		this.password = resHelper.getPassword();
	}

	public static NetLef getInstance(Context context) {
		if (null == instance) {
			instance = new NetLef();
		}
		if (instance.password == null || instance.password.equals("")) {
			instance.init(context);
		}
		return instance;
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
