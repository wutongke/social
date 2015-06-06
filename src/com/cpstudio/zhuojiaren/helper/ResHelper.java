package com.cpstudio.zhuojiaren.helper;

import java.io.File;
import java.util.HashMap;

import com.cpstudio.zhuojiaren.R;
import com.utils.PreferenceUtil;

import android.content.Context;
import android.net.Uri;
import android.os.Environment;

public class ResHelper {
	private static ResHelper instance;
	private String userid = null;
	private String password = null;
	private PreferenceUtil mPu = null;
	private String SDPATH;
	private Context mContext;
	private float times = 1;
	/**
	 * 当前正在聊天的对象
	 */
	private String chatuser = null;
	private boolean isMsgList = false;
	/**
	 * 当前正在圈聊的圈子
	 */
	private String chatgroup = null;
	private boolean isAppShow = false;

	public final static String FILTER_TYPE = "filterType";
	public final static String USER_ID = "uid";
	public final static String PASSWORD = "password";
	public final static String LOGIN_STATE = "loginState";
	public final static String ALERT_INTERVAL = "alertInterval";
	public final static String VIBRATE = "vibrate";
	public final static String SOUND = "sound";
	public final static String LED = "led";
	public final static String COOKIE = "cookie";
	public final static String FIRSTUSER = "firstUse";

	public final static String ROOT_PATH = "rootPath";
	public final static String DEFAULT_ROOT_PATH = "zhuojiaren/";
	public final static String VOICE_PATH = "voicePath";
	public final static String DEFAULT_VOICE_PATH = "zhuojiaren/voice/";
	public final static String IMAGE_PATH = "imagePath";
	public final static String DEFAULT_IMAGE_PATH = "zhuojiaren/image/";
	public final static String CHAT_VOICE_PATH = "chatVoicePath";
	public final static String DEFAULT_CHAT_VOICE_PATH = "zhuojiaren/voice/chat/";
	public final static String CHAT_IMAGE_PATH = "chatImagePath";
	public final static String DEFAULT_CHAT_IMAGE_PATH = "zhuojiaren/image/chat/";
	public final static String HEAD_PATH = "headPath";
	public final static String DEFAULT_HEAD_PATH = "zhuojiaren/userhead/";

	private ResHelper(Context context) {
		mContext = context;
		SDPATH = Environment.getExternalStorageDirectory() + "/";
		mPu = new PreferenceUtil(context, "cpzhuojiaren");
		userid = getLoginName();
		password = getLoginPwd();
	}

	public static ResHelper getInstance(Context context) {
		if (null == instance) {
			instance = new ResHelper(context);
		}
		return instance;
	}

	public boolean isAppShow() {
		return isAppShow;
	}

	public void setAppShow(boolean isAppShow) {
		this.isAppShow = isAppShow;
	}

	public float getTimes() {
		return times;
	}

	public void setTimes(float times) {
		this.times = times;
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

	public boolean isMsgList() {
		return isMsgList;
	}

	public void setMsgList(boolean isMsgList) {
		this.isMsgList = isMsgList;
	}

	public String getChatuser() {
		return chatuser;
	}

	public void setChatuser(String chatuser) {
		this.chatuser = chatuser;
	}

	public String getChatgroup() {
		return chatgroup;
	}

	public void setChatgroup(String chatgroup) {
		this.chatgroup = chatgroup;
	}

	public String getCookie() {
		return mPu.getPreference(COOKIE, "");
	}

	public boolean getFirstUse() {
		return mPu.getPreference(FIRSTUSER, true);
	}

	public String getRootPath() {
		String path = SDPATH + mPu.getPreference(ROOT_PATH, DEFAULT_ROOT_PATH);
		File dir = new File(path);
		if (!dir.exists()) {
			dir.mkdirs();
		}
		return path;
	}

	public String getVoicePath() {
		String path = SDPATH
				+ mPu.getPreference(VOICE_PATH, DEFAULT_VOICE_PATH);
		File dir = new File(path);
		if (!dir.exists()) {
			dir.mkdirs();
		}
		return path;
	}

	public String getChatVoicePath() {
		String path = SDPATH
				+ mPu.getPreference(CHAT_VOICE_PATH, DEFAULT_CHAT_VOICE_PATH);
		File dir = new File(path);
		if (!dir.exists()) {
			dir.mkdirs();
		}
		return path;
	}

	public String getChatImagePath() {
		String path = SDPATH
				+ mPu.getPreference(CHAT_IMAGE_PATH, DEFAULT_CHAT_IMAGE_PATH);
		File dir = new File(path);
		if (!dir.exists()) {
			dir.mkdirs();
		}
		return path;
	}

	public String getImagePath() {
		String path = SDPATH
				+ mPu.getPreference(IMAGE_PATH, DEFAULT_IMAGE_PATH);
		File dir = new File(path);
		if (!dir.exists()) {
			dir.mkdirs();
		}
		return path;
	}

	public Uri getCaptrueUri() {
		return Uri.fromFile(new File(getCaptruePath()));
	}

	public String getCaptruePath() {
		String path = SDPATH
				+ mPu.getPreference(IMAGE_PATH, DEFAULT_IMAGE_PATH);
		File dir = new File(path);
		if (!dir.exists()) {
			dir.mkdirs();
		}
		path += "captrueTemp.jpg";
		return path;
	}

	public String getHeadPath() {
		String path = SDPATH + mPu.getPreference(HEAD_PATH, DEFAULT_HEAD_PATH);
		File dir = new File(path);
		if (!dir.exists()) {
			dir.mkdirs();
		}
		return path;
	}

	public String getFilterType() {
		String[] reses = mContext.getResources().getStringArray(
				R.array.array_res_type);
		return mPu.getPreference(FILTER_TYPE, reses[0]);
	}

	private String getLoginName() {
		return mPu.getPreference(USER_ID, "");
	}

	public String getLoginPwd() {
		return mPu.getPreference(PASSWORD, "");
	}

	public int getLoginState() {
		return mPu.getPreference(LOGIN_STATE, 0);
	}

	public boolean getVibrate() {
		return mPu.getPreference(VIBRATE, true);
	}

	public boolean getSound() {
		return mPu.getPreference(SOUND, true);
	}

	public boolean getLed() {
		return mPu.getPreference(LED, false);
	}

	public int getRuntime() {
		return mPu.getPreference(ALERT_INTERVAL, 86400000);
	}

	public void setPreference(HashMap<String, Object> hashMap) {
		mPu.setPreference(hashMap);
	}
}
