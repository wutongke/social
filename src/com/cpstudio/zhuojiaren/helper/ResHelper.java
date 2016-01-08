package com.cpstudio.zhuojiaren.helper;

import java.io.File;
import java.util.HashMap;
import java.util.UUID;

import com.cpstudio.zhuojiaren.R;
import com.utils.PreferenceUtil;

import android.content.Context;
import android.net.Uri;
import android.os.Environment;

/**
 * 工具类，preferenceUtil相关操作， 单例模式
 */
public class ResHelper {
	private static ResHelper instance;
	private String userid = null;
	private String password = null;
	private PreferenceUtil mPu = null;
	private String SDPATH;
	private Context mContext;
	private float times = 1;

	private String upLoadTokenForQiniu;// 上传文件token,七牛
	private String sessionForAPP;
	private String imTokenForRongyun;// 聊天与融云服务器的token

	private boolean isMsgList = false;
	/**
	 * 是否前台运行
	 */
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

	// add by lz
	public final static String SESSION = "zhuojiaren/session/";
	public final static String UPLIOAD_TOKEN = "zhuojiaren/uploadtoken/";
	public final static String IM_TOKEN = "zhuojiaren/imtoken/";
	// 本地背景图片版本
	public final static String BG_VERSION = "card_bg_new_version";
	// 本地背景,以";"隔开
	public final static String BG_PICS = "card_bg_pics";

	private ResHelper(Context context) {
		mContext = context;
		SDPATH = Environment.getExternalStorageDirectory() + "/";
		mPu = new PreferenceUtil(context, "cpzhuojiaren");
		userid = getLoginName();
		password = getLoginPwd();
		sessionForAPP = getSession();
		upLoadTokenForQiniu = getUploadToken();
		imTokenForRongyun = getImToken();
	}

	public String getUpLoadTokenForQiniu() {
		return upLoadTokenForQiniu;
	}

	public String getSessionForAPP() {
		return sessionForAPP;
	}

	public String getImTokenForRongyun() {
		return imTokenForRongyun;
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

	public String getCookie() {
		return mPu.getPreference(COOKIE, "");
	}

	public boolean getFirstUse() {
		return mPu.getPreference(FIRSTUSER, true);
	}

	public int getBgVersion() {
		return mPu.getPreference(BG_VERSION, 0);
	}

	public void setBgVersion(int newVersion) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put(BG_VERSION, newVersion);
		setPreference(map);
	}

	public String getBgPics() {
		return mPu.getPreference(BG_PICS, null);
	}

	public void setBgPics(String pics) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put(BG_PICS, pics);
		setPreference(map);
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
		// lz 此处若不加唯一标示，则每次生成的缩率图地址一样，从而会覆盖之前的内容，只能加一张图片
		UUID uuid = UUID.randomUUID();
		String uniqueId = uuid.toString();
		path += uniqueId + "captrueTemp.jpg";
		return path;
	}

	public Uri getSureCaptrueUri() {
		return Uri.fromFile(new File(getSureCaptruePath()));
	}

	public String getSureCaptruePath() {
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

	/**
	 * 登录状态 0未登录1已登录
	 * 
	 * @return
	 */
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

	// add by lz 20150725
	private String getSession() {
		return mPu.getPreference(SESSION, "");
	}

	private String getUploadToken() {
		return mPu.getPreference(UPLIOAD_TOKEN, "");
	}

	private String getImToken() {
		return mPu.getPreference(IM_TOKEN, "");
	}

	public void setUpLoadTokenForQiniu(String upLoadTokenForQiniu) {
		this.upLoadTokenForQiniu = upLoadTokenForQiniu;
	}

	public void setSessionForAPP(String sessionForAPP) {
		this.sessionForAPP = sessionForAPP;
	}

	public void setImTokenForRongyun(String imTokenForRongyun) {
		this.imTokenForRongyun = imTokenForRongyun;
	}

}
