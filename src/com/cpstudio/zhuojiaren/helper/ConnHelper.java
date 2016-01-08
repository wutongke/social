package com.cpstudio.zhuojiaren.helper;

import io.rong.imlib.model.Group;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InvalidClassException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import com.cpstudio.zhuojiaren.R;
import com.cpstudio.zhuojiaren.helper.AsyncConnectHelper.FinishCallback;
import com.cpstudio.zhuojiaren.helper.AsyncUploadHelper.ICompleteCallback;
import com.cpstudio.zhuojiaren.model.BaseCodeData;
import com.cpstudio.zhuojiaren.model.City;
import com.cpstudio.zhuojiaren.model.Dynamic;
import com.cpstudio.zhuojiaren.model.GXTypeCodeData;
import com.cpstudio.zhuojiaren.model.LoginRes;
import com.cpstudio.zhuojiaren.model.Province;
import com.cpstudio.zhuojiaren.model.UserNewVO;
import com.cpstudio.zhuojiaren.model.ZhuoShareContent;
import com.cpstudio.zhuojiaren.util.CommonUtil;

/**
 * 网络请求工具类，单例
 * 
 * @author lz
 * 
 */
public class ConnHelper {

	public enum EditMODE {
		VIEW, EDIT, ADD, DELETE
	};

	// SharedPreferences缓存数据key
	public static final String BASEDATA = "baseCodeDatas";// 基本编码数据
	public static final String GXTYPES = "gongxuTypes";// 供需类型
	public static final String CITYS = "citys";// 城市编号等

	private static ConnHelper instance;
	private String userid = null;
	private String password = null;

	String session;// 应用服务器token
	String uploadFileToken;// 七牛云上传图片token
	String imToken; // 融云聊天token
	Context context;

	// 标识每一次请求，请求开始后不重复请求
	private Set<String> mStartedTag = new HashSet<String>();

	private BaseCodeData baseDataSet;
	private GXTypeCodeData gxTypeCodeDataSet;
	private List<Province> citysOfProvince;
	private List<City> citys;
	// 群组信息
	private HashMap<String, Group> groupMap;

	public void init(Context context) {
		ResHelper resHelper = ResHelper.getInstance(context);
		this.userid = resHelper.getUserid();
		this.password = resHelper.getPassword();
		this.imToken = resHelper.getImTokenForRongyun();
		this.session = resHelper.getSessionForAPP();
		this.uploadFileToken = resHelper.getUpLoadTokenForQiniu();
		this.context = context;
	}

	public static ConnHelper getInstance(Context mcontext) {
		if (null == instance) {
			instance = new ConnHelper();
		}
		if (instance.password == null || instance.password.equals("")) {
			instance.init(mcontext);
		}
		return instance;
	}

	/**
	 * http post
	 * 
	 * @param nameValuePairs
	 * @param url
	 * @param handler
	 * @param handlerTag
	 * @param activity
	 * @param tag
	 * @param cancelable
	 * @param cancel
	 * @param data
	 * @return
	 */
	private boolean doPost(List<NameValuePair> nameValuePairs, String url,
			Handler handler, int handlerTag, Activity activity, String tag,
			boolean cancelable, OnCancelListener cancel, String data) {
		if (instance == null) {
			instance = getInstance(activity);
		}
		if (!mStartedTag.contains(tag) || tag == null) {
			if (tag != null) {
				mStartedTag.add(tag);
			}
			// 添加的是用户名和密码，其他地方直接用addUserInfoByPost
			AsyncConnectHelper conn = new AsyncConnectHelper(
					addUserInfoByPost(nameValuePairs), url, getFinishCallback(
							handler, handlerTag, tag, data), activity);
			conn.setCancelable(cancelable);
			if (cancelable) {
				conn.setCancel(getCancelListener(cancel, tag, conn));
			}
			conn.execute();
			return true;
		}
		return false;
	}

	/**
	 * 需要上传文件时调用此接口
	 * 
	 * @param files
	 *            欲上传文件地址列表
	 * @param nameValuePairs
	 *            参数
	 * @param url
	 *            url
	 * @param handler
	 * @param handlerTag
	 * @param activity
	 * @param tag
	 * @param cancelable
	 * @param cancel
	 * @param data
	 * @return
	 */
	public boolean doPostWithFile(
			final Map<String, ArrayList<String>> filesMap,
			final List<NameValuePair> nameValuePairs, final String url,
			final Handler handler, final int handlerTag,
			final Activity activity, final String tag,
			final boolean cancelable, final OnCancelListener cancel,
			final String data) {
		return doPostWithFile(filesMap, nameValuePairs, url, handler,
				handlerTag, activity, tag, cancelable, cancel, data, null);
	}

	/**
	 * 
	 * @param filesMap
	 * @param nameValuePairs
	 * @param url
	 * @param handler
	 * @param handlerTag
	 * @param activity
	 * @param tag
	 * @param cancelable
	 * @param cancel
	 * @param data
	 * @param extra
	 *            当extra不为null时，将extra的内容添加到filesMap中
	 * @return
	 */
	public boolean doPostWithFile(
			final Map<String, ArrayList<String>> filesMap,
			final List<NameValuePair> nameValuePairs, final String url,
			final Handler handler, final int handlerTag,
			final Activity activity, final String tag,
			final boolean cancelable, final OnCancelListener cancel,
			final String data, final String extra) {

		if (!mStartedTag.contains(tag) || tag == null) {
			if (tag != null) {
				mStartedTag.add(tag);
			}
			if (filesMap != null && filesMap.size() > 0) {
				AsyncUploadHelper helper = new AsyncUploadHelper(activity,
						uploadFileToken, filesMap, new ICompleteCallback() {

							@Override
							public void onReturn(Map<String, StringBuilder> map) {
								// TODO Auto-generated method stub
								if (map == null) {
									Toast.makeText(activity, "上传到七牛云失败", 1000)
											.show();
									return;
								}
								if (map.size() > 0) {
									for (Map.Entry<String, StringBuilder> entry : map
											.entrySet()) {
										String key = entry.getKey();

										String value = entry.getValue()
												.toString();
										if (extra != null)
											value = extra + "," + value;
										nameValuePairs
												.add(new BasicNameValuePair(
														key, value));
									}
								} else
									nameValuePairs.add(new BasicNameValuePair(
											"file", extra));
								AsyncConnectHelper conn = new AsyncConnectHelper(
										addUserInfoByPost(nameValuePairs), url,
										getFinishCallback(handler, handlerTag,
												tag, data), activity);
								conn.setCancelable(cancelable);
								if (cancelable) {
									conn.setCancel(getCancelListener(cancel,
											tag, conn));
								}
								conn.execute();
							}
						});
				helper.execute("test");
			} else {
				nameValuePairs.add(new BasicNameValuePair("file", extra));
				AsyncConnectHelper conn = new AsyncConnectHelper(
						addUserInfoByPost(nameValuePairs), url,
						getFinishCallback(handler, handlerTag, tag, data),
						activity);
				conn.setCancelable(cancelable);
				if (cancelable) {
					conn.setCancel(getCancelListener(cancel, tag, conn));
				}
				conn.execute();
			}

			return true;
		}
		return false;
	}

	private boolean doGet(String url, Handler handler, int handlerTag,
			String tag, Activity activity, boolean cancelable,
			OnCancelListener cancel, String data) {
		if (!mStartedTag.contains(tag) || tag == null) {
			if (tag != null) {
				mStartedTag.add(tag);
			}
			AsyncConnectHelper conn = new AsyncConnectHelper(addUserInfo(url),
					getFinishCallback(handler, handlerTag, tag, data), activity);
			conn.setCancelable(cancelable);
			if (cancelable) {
				conn.setCancel(getCancelListener(cancel, tag, conn));
			}
			conn.execute();
			return true;
		}
		return false;
	}

	private boolean doGet(String url, FinishCallback callback, String tag,
			Activity activity, boolean cancelable, OnCancelListener cancel) {
		if (!mStartedTag.contains(tag) || tag == null) {
			if (tag != null) {
				mStartedTag.add(tag);
			}
			AsyncConnectHelper conn = new AsyncConnectHelper(addUserInfo(url),
					getFinishCallback(callback, tag), activity);
			conn.setCancelable(cancelable);
			if (cancelable) {
				conn.setCancel(getCancelListener(cancel, tag, conn));
			}
			conn.execute();
			return true;
		}
		return false;
	}

	private FinishCallback getFinishCallback(final FinishCallback callback,
			final String tag) {
		return new FinishCallback() {

			@Override
			public boolean onReturn(String rs, int responseCode) {
				if (tag != null) {
					mStartedTag.remove(tag);
				}
				callback.onReturn(rs, responseCode);
				return false;
			}
		};
	}

	private FinishCallback getFinishCallback(final Handler handler,
			final int handlerTag, final String tag, final String data) {

		return new FinishCallback() {
			@Override
			public boolean onReturn(String rs, int responseCode) {
				if (tag != null) {
					mStartedTag.remove(tag);
				}
				if (handler != null) {
					Message msg = handler.obtainMessage(handlerTag);
					Bundle bundle = new Bundle();
					bundle.putString("data", data);
					msg.setData(bundle);
					msg.obj = rs;
					msg.sendToTarget();
				}
				return false;
			}
		};
	}

	private OnCancelListener getCancelListener(final OnCancelListener cancel,
			final String tag, final AsyncConnectHelper conn) {
		return new OnCancelListener() {
			@Override
			public void onCancel(DialogInterface dialog) {
				if (tag != null) {
					mStartedTag.remove(tag);
				}
				conn.cancel(true);
				if (cancel != null) {
					cancel.onCancel(dialog);
				}
			}
		};
	}

	private String addUserInfo(String params) {
		if (params.contains("?")) {
			return params + "&username=" + userid + "&password=" + password
					+ "&session=" + session;
		} else {
			return params + "?username=" + userid + "&password=" + password
					+ "&session=" + session;
		}
	}

	private List<NameValuePair> addUserInfo(List<NameValuePair> nameValuePairs) {
		nameValuePairs.add(new BasicNameValuePair("username", userid));
		nameValuePairs.add(new BasicNameValuePair("password", password));
		return nameValuePairs;
	}

	private List<NameValuePair> addUserInfoByPost(
			List<NameValuePair> nameValuePairs) {
		nameValuePairs.add(new BasicNameValuePair("session", session));
		nameValuePairs.add(new BasicNameValuePair("apptype", "0"));
		return nameValuePairs;
	}
	/**
	 * 增加页信息
	 * 
	 * @param nameValuePairs
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	private List<NameValuePair> addPageInfo(List<NameValuePair> nameValuePairs,
			int pageNo, int pageSize) {
		nameValuePairs.add(new BasicNameValuePair("pageNo", String
				.valueOf(pageNo)));
		nameValuePairs.add(new BasicNameValuePair("pageSize", String
				.valueOf(pageSize)));
		return nameValuePairs;
	}
	public HashMap<String, Group> getGroupMap() {
		return groupMap;
	}

	public GXTypeCodeData getGxTypeCodeDataSet() {
		return gxTypeCodeDataSet;
	}

	public void setGxTypeCodeDataSet(GXTypeCodeData gxTypeCodeDataSet) {
		this.gxTypeCodeDataSet = gxTypeCodeDataSet;
	}

	public void setGroupMap(HashMap<String, Group> groupMap) {
		this.groupMap = groupMap;
	}

	public List<City> getCitys() {
		if (citys == null) {

		}
		return citys;
	}

	public void setCitys(List<City> citys) {
		this.citys = citys;
	}

	public List<Province> getCitysOfPrince() {
		return citysOfProvince;
	}

	public String getSession() {
		return session;
	}

	public String getUploadFileToken() {
		return uploadFileToken;
	}

	public String getImToken() {
		return imToken;
	}

	public void setSession(String session) {
		this.session = session;
	}

	public void setUploadFileToken(String uploadFileToken) {
		this.uploadFileToken = uploadFileToken;
	}

	public void setImToken(String imToken) {
		this.imToken = imToken;
	}

	public void setCitysOfPrince(List<Province> citysOfPrince) {
		this.citysOfProvince = citysOfPrince;
		citys = new ArrayList<City>();
		for (Province temp : citysOfProvince) {
			citys.addAll(temp.getCitys());
		}
	}

	public BaseCodeData getBaseDataSet() {
		if (baseDataSet == null) {
			String data = readObject("BaseSetData");
			baseDataSet = JsonHandler.parseBaseCodeData(data);
		}
		return baseDataSet;
	}

	public void setBaseDataSet(BaseCodeData baseDataSet) {
		this.baseDataSet = baseDataSet;
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

	public boolean getFromServer(String url, Handler handler, int tag) {
		return getFromServer(url, handler, tag, null, false, null, null);
	}

	private boolean getFromServerByPost(String url,
			List<NameValuePair> pamNameValuePairs, Handler handler, int tag) {
		if (pamNameValuePairs == null)
			pamNameValuePairs = new ArrayList<NameValuePair>();
		return getFromServerByPost(url, pamNameValuePairs, handler, tag, null,
				false, null, null);
	}

	public boolean getFromServer(String url, Handler handler, int tag,
			String data) {
		return getFromServer(url, handler, tag, null, false, null, data);
	}

	public boolean getFromServer(String url, Handler handler, int tag,
			Activity activity, boolean cancelable, OnCancelListener cancel) {
		return getFromServer(url, handler, tag, activity, cancelable, cancel,
				null);
	}

	/**
	 * <b><i>public boolean getDataFromServer(String url, Handler handler, int
	 * tag,Activity activity, boolean cancelable, OnCancelListener cancel,String
	 * data)</i></b> <br>
	 * Added in <a href="#">v 1.0.8</a> <br>
	 * <br>
	 * Get Data From server by HTTP_GET.
	 * 
	 * @author sofia
	 * @param url
	 *            request url+param
	 * @param handler
	 *            do HTTP request complete
	 * @param tag
	 *            handlertag
	 * @param activity
	 *            request processbar
	 * @param cancelable
	 *            request cancelable
	 * @param cancel
	 *            on request cancel
	 * @param data
	 *            ex String data
	 * @return true if start HTTP request success otherwise return false
	 * */
	public boolean getFromServer(String url, Handler handler, int tag,
			Activity activity, boolean cancelable, OnCancelListener cancel,
			String data) {
		return doGet(url, handler, tag, url, activity, cancelable, cancel, data);
	}

	// lz0713
	public boolean getFromServerByPost(String url,
			List<NameValuePair> pamNameValuePairs, Handler handler, int tag,
			Activity activity, boolean cancelable, OnCancelListener cancel,
			String data) {
		pamNameValuePairs.add(new BasicNameValuePair("pubnum", "5"));
		return doPost(pamNameValuePairs, url, handler, tag, activity, url,
				cancelable, cancel, data);
	}

	public boolean getFromServer(String url, FinishCallback callback) {
		return getFromServer(url, callback, null, false, null);
	}

	/**
	 * heep Get方法与服务器交互
	 * 
	 * @param url
	 * @param callback
	 * @param activity
	 * @param cancelable
	 * @param cancel
	 * @return
	 */
	public boolean getFromServer(String url, FinishCallback callback,
			Activity activity, boolean cancelable, OnCancelListener cancel) {
		return doGet(url, callback, url, activity, cancelable, cancel);
	}

	public boolean pubCmt(String msgid, String parentid, String content,
			String forward, Handler handler, int handlerTag, Activity activity,
			boolean cancelable, OnCancelListener cancel, String data) {
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("msgid", msgid));
		nameValuePairs.add(new BasicNameValuePair("parentid", parentid));
		nameValuePairs.add(new BasicNameValuePair("content", content));
		nameValuePairs.add(new BasicNameValuePair("forward", forward));
		return doPost(nameValuePairs, UrlHelper.getUrlCmt(), handler,
				handlerTag, activity, "pubCmt", cancelable, cancel, data);
	}

	public boolean goodMsg(String msgid, Handler handler, int handlerTag,
			Activity activity, boolean cancelable, OnCancelListener cancel,
			String data) {
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("msgid", msgid));
		return doPost(nameValuePairs, UrlHelper.getUrlGood(), handler,
				handlerTag, activity, "goodMsg", cancelable, cancel, data);
	}

	public boolean followGroup(String groupid, String type, Handler handler,
			int handlerTag, Activity activity, boolean cancelable,
			OnCancelListener cancel, String data) {
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("groupid", groupid));
		nameValuePairs.add(new BasicNameValuePair("type", type));
		return doPost(nameValuePairs, UrlHelper.getUrlFollowGroup(), handler,
				handlerTag, activity, "followGroup", cancelable, cancel, data);
	}

	public boolean androidName(Handler handler, int handlerTag,
			Activity activity, String info, boolean cancelable,
			OnCancelListener cancel, String data) {
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("info", info));
		if (info.equals("")) {
			instance = null;
		}
		return doPost(nameValuePairs, UrlHelper.getUrlAndroidName(), handler,
				handlerTag, activity, "androidName", cancelable, cancel, data);
	}

	// 登陆
	public boolean login(String userid, String password, Handler handler,
			int handlerTag, Activity activity, boolean cancelable,
			OnCancelListener cancel, String data) {
		this.userid = userid;
		this.password = password;
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("from", "android"));
		// 登陆时需要添加额外的账号和密码信息
		return doPost(addUserInfo(nameValuePairs), UrlHelper.getUrlLogin(),
				handler, handlerTag, activity, "login", cancelable, cancel,
				data);
	}

	/**
	 * 获取验证码
	 * 
	 * @param code
	 *            电话号
	 * @param code
	 *            电话号
	 * @param handler
	 * @param handlerTag
	 *            回调时候的msg.what
	 * @param handlerTag
	 *            回调时候的msg.what
	 * @param activity
	 * @param cancelalbe
	 * @return
	 */
	public boolean getVerificationcode(String code, Handler handler,
			int handlerTag, Activity activity, boolean cancelalbe) {
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("verificationcode", code));
		return doPost(nameValuePairs, "", handler, handlerTag, activity, "",
				cancelalbe, null, null);
	}

	public boolean modifyPwd(String password, String newpassword,
			Handler handler, int handlerTag, Activity activity,
			boolean cancelable, OnCancelListener cancel, String data) {
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("newpassword", newpassword));
		return doPost(nameValuePairs, UrlHelper.getUrlChangPwd(), handler,
				handlerTag, activity, "modifyPwd", cancelable, cancel, data);
	}

	/**
	 * 获取首页广告信息
	 * 
	 * @param mUIHandler
	 * @param tag
	 * @param type
	 * @return
	 */
	public boolean getMainInfo(Handler mUIHandler, int tag, int pubnum,
			int statusnum) {

		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
		if (pubnum > 5)
			params.add(new BasicNameValuePair("pubnum", pubnum + ""));
		if (statusnum > 5)
			params.add(new BasicNameValuePair("statusnum", statusnum + ""));

		return getFromServerByPost(UrlHelper.getMainInfo(), params, mUIHandler,
				tag);
	}

	/**
	 * 删除活动
	 * 
	 * @param mUIHandler
	 * @param tag
	 * @param activityid
	 *            圈活动ID （如果为多个，以逗号分隔）
	 * @return
	 */
	public boolean deleteEvents(Handler mUIHandler, int tag, String activityid) {

		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
		if (activityid != null)
			params.add(new BasicNameValuePair("activityid", activityid));

		return getFromServerByPost(UrlHelper.deleteActives(), params,
				mUIHandler, tag);
	}

	/**
	 * 获取广告信息，暂未调用
	 * 
	 * @param mUIHandler
	 * @param tag
	 * @param type
	 *            广告类别 0-首页 1-发现 2-精进 3-商城 4-众筹
	 * @return
	 */
	public boolean getAdInfo(Handler mUIHandler, int tag, int type) {
		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("type", String.valueOf(type)));
		return getFromServerByPost(UrlHelper.getAdInfo(), params, mUIHandler,
				tag);
	}

	/**
	 * 获取圈话题列表
	 * 
	 * @param context
	 * @param groupid
	 * @param uid
	 *            uid为null则获取本圈子的圈话题；否则获取此用户在此圈子的圈话
	 * @param pageNo
	 * @param pageSize
	 */
	public boolean getQuanTopicList(Handler mUIHandler, int tag,
			String groupid, String uid, int pageNo, int pageSize) {
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("groupid", groupid));
		if (uid != null)
			nameValuePairs.add(new BasicNameValuePair("userid", uid));
		nameValuePairs.add(new BasicNameValuePair("pageNo", "" + pageNo));
		nameValuePairs.add(new BasicNameValuePair("pageSize", "" + pageSize));
		return getFromServerByPost(UrlHelper.getQuanTopicList(),
				nameValuePairs, mUIHandler, tag);
	}

	/**
	 * 发布圈话题
	 * 
	 * @param activity
	 * @param mUIHandler
	 * @param tag
	 * @param groupid
	 * @param content
	 * @param files
	 * @return
	 */
	public boolean pubQuanTopic(Activity activity, Handler mUIHandler, int tag,
			String groupid, String content, ArrayList<String> files) {
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("groupid", groupid));
		nameValuePairs.add(new BasicNameValuePair("content", content));
		Map<String, ArrayList<String>> fileMap = new HashMap<String, ArrayList<String>>();
		fileMap.put("file", files);
		return doPostWithFile(fileMap, nameValuePairs,
				UrlHelper.pubQuanTopic(), mUIHandler, tag, activity,
				"pubQuanTopic", false, null, null);
	}

	/**
	 * 发布反馈意见
	 * 
	 * @param activity
	 * @param mUIHandler
	 * @param tag
	 * @param groupid
	 * @param content
	 * @param files
	 * @return
	 */
	public boolean pubAdvice(Activity activity, Handler mUIHandler, int tag,
			String groupid, String content, ArrayList<String> files) {
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("groupid", groupid));
		nameValuePairs.add(new BasicNameValuePair("content", content));
		Map<String, ArrayList<String>> fileMap = new HashMap<String, ArrayList<String>>();
		fileMap.put("file", files);
		return doPostWithFile(fileMap, nameValuePairs, UrlHelper.pubAdvice(),
				mUIHandler, tag, activity, "pubAdvice", false, null, null);
	}

	/**
	 * 发布反馈意见
	 * 
	 * @param activity
	 * @param mUIHandler
	 * @param tag
	 * @param groupid
	 * @param content
	 * @param files
	 * @return
	 */
	public boolean pubFeedBack(Activity activity, Handler mUIHandler, int tag,
			String groupid, String content, ArrayList<String> files) {
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("content", content));
		Map<String, ArrayList<String>> fileMap = new HashMap<String, ArrayList<String>>();
		fileMap.put("file", files);
		return doPostWithFile(fileMap, nameValuePairs,
				UrlHelper.pubQuanTopic(), mUIHandler, tag, activity,
				"pubQuanTopic", false, null, null);
	}

	/**
	 * 更新个人照片
	 * 
	 * @param activity
	 * @param mUIHandler
	 * @param tag
	 * @param groupid
	 * @param content
	 * @param files
	 * @return
	 */
	public boolean pubPhoto(Activity activity, Handler mUIHandler, int tag,
			String originFilekeys, ArrayList<String> files) {
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		Map<String, ArrayList<String>> fileMap = null;
		if (files != null && files.size() > 0) {
			fileMap = new HashMap<String, ArrayList<String>>();
			fileMap.put("file", files);
		}
		return doPostWithFile(fileMap, nameValuePairs, UrlHelper.pubPhoto(),
				mUIHandler, tag, activity, "pubinfo", false, null, null,
				originFilekeys);
	}

	/**
	 * 获得圈子活动列表
	 * 
	 * @param mUIHandler
	 * @param tag
	 * @param groupid
	 * @param uid
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	public boolean getQuanEventList(Handler mUIHandler, int tag,
			String groupid, String uid, int pageNo, int pageSize) {
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("groupid", groupid));
		if (uid != null)
			nameValuePairs.add(new BasicNameValuePair("userid", uid));
		nameValuePairs.add(new BasicNameValuePair("pageNo", "" + pageNo));
		nameValuePairs.add(new BasicNameValuePair("pageSize", "" + pageSize));
		return getFromServerByPost(UrlHelper.getQuanEventList(),
				nameValuePairs, mUIHandler, tag);
	}

	public boolean getQuanEventListCollection(Handler mUIHandler, int tag,
			String groupid, String uid, int pageNo, int pageSize) {
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("groupid", groupid));
		if (uid != null)
			nameValuePairs.add(new BasicNameValuePair("userid", uid));
		nameValuePairs.add(new BasicNameValuePair("pageNo", "" + pageNo));
		nameValuePairs.add(new BasicNameValuePair("pageSize", "" + pageSize));
		return getFromServerByPost(UrlHelper.getQuaneventcollection(),
				nameValuePairs, mUIHandler, tag);
	}

	public boolean getGongListCollection(Handler mUIHandler, int tag,
			int pageNo, int pageSize) {
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("pageNo", "" + pageNo));
		nameValuePairs.add(new BasicNameValuePair("pageSize", "" + pageSize));
		nameValuePairs.add(new BasicNameValuePair("sdflag", "0"));
		return getFromServerByPost(UrlHelper.getGonglist(), nameValuePairs,
				mUIHandler, tag);
	}

	public boolean getPeopleListCollection(Handler mUIHandler, int tag,
			int pageNo, int pageSize) {
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("pageNo", "" + pageNo));
		nameValuePairs.add(new BasicNameValuePair("pageSize", "" + pageSize));
		return getFromServerByPost(UrlHelper.getPeoplelist(), nameValuePairs,
				mUIHandler, tag);
	}

	public boolean getXuListCollection(Handler mUIHandler, int tag, int pageNo,
			int pageSize) {
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("pageNo", "" + pageNo));
		nameValuePairs.add(new BasicNameValuePair("pageSize", "" + pageSize));
		nameValuePairs.add(new BasicNameValuePair("sdflag", "1"));
		return getFromServerByPost(UrlHelper.getGonglist(), nameValuePairs,
				mUIHandler, tag);
	}

	public boolean getTopicListCollection(Handler mUIHandler, int tag,
			int pageNo, int pageSize) {
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("pageNo", "" + pageNo));
		nameValuePairs.add(new BasicNameValuePair("pageSize", "" + pageSize));
		return getFromServerByPost(UrlHelper.getTopiclist(), nameValuePairs,
				mUIHandler, tag);
	}

	/**
	 * 获取用户加入和创建的活动
	 * 
	 * @param mUIHandler
	 * @param tag
	 * @param userid
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	public boolean getUserEventList(Handler mUIHandler, int tag, String userid,
			int pageNo, int pageSize) {
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		if (userid != null)
			nameValuePairs.add(new BasicNameValuePair("userid", userid));
		nameValuePairs.add(new BasicNameValuePair("pageNo", "" + pageNo));
		nameValuePairs.add(new BasicNameValuePair("pageSize", "" + pageSize));
		return getFromServerByPost(UrlHelper.getUserEvent(), nameValuePairs,
				mUIHandler, tag);
	}

	/**
	 * 获得圈子信息(圈子主页)
	 * 
	 * @param mUIHandler
	 * @param tag
	 * @param groupid
	 * @param uid
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	public boolean getQuanInfo(Handler mUIHandler, int tag, String groupid) {
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("groupid", groupid));
		return getFromServerByPost(UrlHelper.getQuanInfo(), nameValuePairs,
				mUIHandler, tag);
	}

	/**
	 * 加入或退出圈子
	 * 
	 * @param mUIHandler
	 * @param tag
	 * @param groupid
	 * @param type
	 *            0取消关注 1申请关注 2接受关注
	 * @param content
	 * @return
	 */
	public boolean followGroup(Handler mUIHandler, int tag, String groupid,
			int type, String userid, String content) {
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("groupid", groupid));
		nameValuePairs.add(new BasicNameValuePair("type", type + ""));
		nameValuePairs.add(new BasicNameValuePair("content", content));
		if (userid != null)
			nameValuePairs.add(new BasicNameValuePair("userid", userid));
		return getFromServerByPost(UrlHelper.manageQuanPermit(),
				nameValuePairs, mUIHandler, tag);
	}

	/**
	 * 修改圈子信息
	 * 
	 * @param mUIHandler
	 * @param handlerTag
	 * @param groupid
	 * @param gname
	 * @param gintro
	 * @param gtype
	 * @param city
	 * @param followpms
	 * @param accesspms
	 * @param pub
	 * @param files
	 * @return
	 */
	public boolean modifyGroupInfo(Handler mUIHandler, int handlerTag,
			String groupid, String gname, String gintro, String gtype,
			String city, String followpms, String accesspms, String pub,
			ArrayList<String> files) {
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		// nameValuePairs = addUserInfoByPost(nameValuePairs);
		nameValuePairs.add(new BasicNameValuePair("groupid", groupid));
		nameValuePairs.add(new BasicNameValuePair("gname", gname));
		nameValuePairs.add(new BasicNameValuePair("gintro", gintro));
		nameValuePairs.add(new BasicNameValuePair("gtype", gtype));
		nameValuePairs.add(new BasicNameValuePair("city", city));
		nameValuePairs.add(new BasicNameValuePair("followpms", followpms));
		nameValuePairs.add(new BasicNameValuePair("accesspms", accesspms));
		nameValuePairs.add(new BasicNameValuePair("gpub", pub));
		Map<String, ArrayList<String>> filesMap = new HashMap<String, ArrayList<String>>();
		filesMap.put("gheader", files);
		return getFromServerByPost(UrlHelper.modifyGroupInfo(), nameValuePairs,
				mUIHandler, handlerTag);
	}

	/**
	 * 设置圈子头像
	 * 
	 * @param mUIHandler
	 * @param handlerTag
	 * @param groupid
	 * @param files
	 * @return
	 */
	public boolean setQuanLogo(Handler mUIHandler, int handlerTag,
			String groupid, ArrayList<String> files) {
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		Map<String, ArrayList<String>> filesMap = new HashMap<String, ArrayList<String>>();
		filesMap.put("file", files);
		nameValuePairs.add(new BasicNameValuePair("groupid", groupid));
		return getFromServerByPost(UrlHelper.setQuanLogo(), nameValuePairs,
				mUIHandler, handlerTag);
	}

	/**
	 * 获取圈话题详情
	 * 
	 * @return
	 */
	public boolean getTopicDetail(Handler mUIHandler, int handlerTag,
			String topicid) {
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("topicid", topicid));
		return getFromServerByPost(UrlHelper.getTopicDetail(), nameValuePairs,
				mUIHandler, handlerTag);
	}

	/**
	 * 
	 * @param mUIHandler
	 * @param handlerTag
	 * @param topicid
	 * @param praise
	 *            0:取消赞 1:赞
	 * @return
	 */
	public boolean praiseTopic(Handler mUIHandler, int handlerTag,
			String topicid, int praise) {
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("topicid", topicid));
		nameValuePairs.add(new BasicNameValuePair("praise", praise + ""));
		return getFromServerByPost(UrlHelper.topicPraise(), nameValuePairs,
				mUIHandler, handlerTag);
	}

	public boolean praiseDynamic(Handler mUIHandler, int handlerTag,
			String statusid, int praise) {
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("statusid", statusid));
		nameValuePairs.add(new BasicNameValuePair("praise", praise + ""));
		return getFromServerByPost(UrlHelper.dynamicPraise(), nameValuePairs,
				mUIHandler, handlerTag);
	}

	/**
	 * 赞名片
	 * 
	 * @param mUIHandler
	 * @param handlerTag
	 * @param statusid
	 * @param praise
	 *            //0:取消赞 1:赞
	 * @return
	 */
	public boolean praiseCard(Handler mUIHandler, int handlerTag,
			String userid, int praise) {
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("userid", userid));
		nameValuePairs.add(new BasicNameValuePair("praise", praise + ""));
		return getFromServerByPost(UrlHelper.zanCard(), nameValuePairs,
				mUIHandler, handlerTag);
	}

	/**
	 * 圈话题评论
	 * 
	 * @param mUIHandler
	 * @param handlerTag
	 * @param topicid
	 * @param comment
	 * @param toId
	 *            可选 要回复的评论ID (不填则是普通评论)
	 * @param toUserid
	 *            可选 要回复的用户ID (不填则是普通评论)
	 * @return
	 */
	public boolean CommentTopic(Handler mUIHandler, int handlerTag,
			String topicid, String comment, String toId, String toUserid) {
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("topicid", topicid));
		nameValuePairs.add(new BasicNameValuePair("comment", comment));
		if (toId != null)
			nameValuePairs.add(new BasicNameValuePair("toId", toId));
		if (toUserid != null)
			nameValuePairs.add(new BasicNameValuePair("toUserid", toUserid));
		return getFromServerByPost(UrlHelper.topicComment(), nameValuePairs,
				mUIHandler, handlerTag);
	}

	public boolean CommentDynamic(Handler mUIHandler, int handlerTag,
			String statusid, String comment, String toId, String toUserid) {
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("statusid", statusid));
		nameValuePairs.add(new BasicNameValuePair("comment", comment));
		if (toId != null)
			nameValuePairs.add(new BasicNameValuePair("toId", toId));
		if (toUserid != null)
			nameValuePairs.add(new BasicNameValuePair("toUserid", toUserid));
		return getFromServerByPost(UrlHelper.dynamicComment(), nameValuePairs,
				mUIHandler, handlerTag);
	}

	/**
	 * 获得个人信息
	 * 
	 * @param mUIHandler
	 * @param handlerTag
	 * @param userid
	 *            (可选) 要查看哪个用户的信息。不填则查看自己的信息
	 * @return
	 */
	public boolean getUserInfo(Handler mUIHandler, int handlerTag, String userid) {
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		if (userid != null)
			nameValuePairs.add(new BasicNameValuePair("userid", userid));
		return getFromServerByPost(UrlHelper.getUserInfo(), nameValuePairs,
				mUIHandler, handlerTag);
	}

	public boolean getGonggaoDetail(Handler mUIHandler, int handlerTag,
			String id) {
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		if (id != null)
			nameValuePairs.add(new BasicNameValuePair("id", id));
		return getFromServerByPost(UrlHelper.gonggaoDetail(), nameValuePairs,
				mUIHandler, handlerTag);
	}

	/**
	 * 修改用户信息
	 * 
	 * @param mUIHandler
	 * @param handlerTag
	 * @param userid
	 * @return
	 */
	public boolean modifyUserInfo(Handler mUIHandler, int handlerTag,
			UserNewVO user) {
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		if (user.getName() != null)
			nameValuePairs.add(new BasicNameValuePair("name", user.getName()));
		if (user.getEmail() != null)
			nameValuePairs
					.add(new BasicNameValuePair("email", user.getEmail()));
		if (user.getGender() != -1)
			nameValuePairs.add(new BasicNameValuePair("gender", user
					.getGender() + ""));
		if (user.getMarried() != -1)
			nameValuePairs.add(new BasicNameValuePair("married", user
					.getMarried() + ""));
		if (user.getCity() != -1)
			nameValuePairs.add(new BasicNameValuePair("city", user.getCity()
					+ ""));
		if (user.getHometown() != -1)
			nameValuePairs.add(new BasicNameValuePair("hometown", user
					.getHometown() + ""));
		if (user.getTravelCity() != null)
			nameValuePairs.add(new BasicNameValuePair("travelCity", user
					.getTravelCity()));
		if (user.getBirthday() != null)
			nameValuePairs.add(new BasicNameValuePair("birthday", user
					.getBirthday()));
		if (user.getIsBirthdayOpen() != -1)
			nameValuePairs.add(new BasicNameValuePair("isBirthdayOpen", user
					.getIsBirthdayOpen() + ""));
		if (user.getBirthdayLunar() != null)
			nameValuePairs.add(new BasicNameValuePair("birthdayLunar", user
					.getBirthdayLunar()));
		if (user.getConstellation() != -1)
			nameValuePairs.add(new BasicNameValuePair("constellation", user
					.getConstellation() + ""));
		if (user.getZodiac() != -1)
			nameValuePairs.add(new BasicNameValuePair("zodiac", user
					.getZodiac() + ""));
		if (user.getHobby() != null)
			nameValuePairs
					.add(new BasicNameValuePair("hobby", user.getHobby()));
		if (user.getIndustry() != -1)
			nameValuePairs.add(new BasicNameValuePair("industry", user
					.getIndustry() + ""));
		if (user.getPosition() != -1)
			nameValuePairs.add(new BasicNameValuePair("position", user
					.getPosition() + ""));
		if (user.getIsEmailOpen() != -1)
			nameValuePairs.add(new BasicNameValuePair("isEmailOpen", user
					.getIsEmailOpen() + ""));
		if (user.getIsPhoneOpen() != -1)
			nameValuePairs.add(new BasicNameValuePair("isPhoneOpen", user
					.getIsPhoneOpen() + ""));
		if (user.getPhone() != null)
			nameValuePairs
					.add(new BasicNameValuePair("phone", user.getPhone()));
		if (user.getQq() != null)
			nameValuePairs.add(new BasicNameValuePair("qq", user.getQq()));
		if (user.getIsQqOpen() != -1)
			nameValuePairs.add(new BasicNameValuePair("isQqOpen", user
					.getIsQqOpen() + ""));
		if (user.getWeixin() != null)
			nameValuePairs.add(new BasicNameValuePair("weixin", user
					.getWeixin()));
		if (user.getIsWeixinOpen() != -1)
			nameValuePairs.add(new BasicNameValuePair("isWeixinOpen", user
					.getIsWeixinOpen() + ""));
		if (user.getSignature() != null)
			nameValuePairs.add(new BasicNameValuePair("signature", user
					.getSignature()));
		if (user.getDream() != null)
			nameValuePairs
					.add(new BasicNameValuePair("dream", user.getDream()));
		if (user.getFaith() != null)
			nameValuePairs
					.add(new BasicNameValuePair("faith", user.getFaith()));
		return getFromServerByPost(UrlHelper.modifyUserInfo(), nameValuePairs,
				mUIHandler, handlerTag);
	}

	/**
	 * 设置用户头像
	 * 
	 * @param activity
	 * @param mUIHandler
	 * @param tag
	 * @param filePath
	 * @return
	 */
	public boolean setUserHeadImage(Activity activity, Handler mUIHandler,
			int tag, String filePath) {
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		ArrayList<String> fileList = new ArrayList<String>();
		fileList.add(filePath);
		Map<String, ArrayList<String>> fileMap = new HashMap<String, ArrayList<String>>();
		fileMap.put("file", fileList);
		return doPostWithFile(fileMap, nameValuePairs,
				UrlHelper.setUserHeadImage(), mUIHandler, tag, activity,
				"setHeadImage", false, null, null);
	}

	/**
	 * 获得动态详情
	 * 
	 * @param mUIHandler
	 * @param handlerTag
	 * @param statusid
	 * @return
	 */
	public boolean getDetailDynamic(Handler mUIHandler, int handlerTag,
			String statusid) {
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("statusid", statusid));
		return getFromServerByPost(UrlHelper.getDetailDynamic(),
				nameValuePairs, mUIHandler, handlerTag);
	}

	public boolean getDynamicList(Handler mUIHandler, int handlerTag, int type,
			String userid, int pageNo, int pageSize) {
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs
				.add(new BasicNameValuePair("type", String.valueOf(type)));
		if (type == Dynamic.DYNATIC_TYPE_SB_JIAREN)
			nameValuePairs.add(new BasicNameValuePair("userid", userid));
		nameValuePairs.add(new BasicNameValuePair("pageNo", String
				.valueOf(pageNo)));
		nameValuePairs.add(new BasicNameValuePair("pageSize", String
				.valueOf(pageSize)));
		return getFromServerByPost(UrlHelper.getDynamicList(), nameValuePairs,
				mUIHandler, handlerTag);
	}

	/**
	 * 获得订单列表
	 * 
	 * @param mUIHandler
	 * @param handlerTag
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	public boolean getOrdersList(Handler mUIHandler, int handlerTag,
			int pageNo, int pageSize) {
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

		nameValuePairs.add(new BasicNameValuePair("pageNo", String
				.valueOf(pageNo)));
		nameValuePairs.add(new BasicNameValuePair("pageSize", String
				.valueOf(pageSize)));
		return getFromServerByPost(UrlHelper.orderList(), nameValuePairs,
				mUIHandler, handlerTag);
	}

	/**
	 * 获得订单详情
	 * 
	 * @param mUIHandler
	 * @param handlerTag
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	public boolean getOrderDetail(Handler mUIHandler, int handlerTag,
			String billNo) {
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("billNo", billNo));
		return getFromServerByPost(UrlHelper.orderDetail(), nameValuePairs,
				mUIHandler, handlerTag);
	}

	public boolean getCardBg(Handler mUIHandler, int handlerTag, int version) {
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("version", String
				.valueOf(version)));
		return getFromServerByPost(UrlHelper.cardBg(), nameValuePairs,
				mUIHandler, handlerTag);
	}

	public boolean setCardBg(Handler mUIHandler, int handlerTag, int bgid) {
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs
				.add(new BasicNameValuePair("bgid", String.valueOf(bgid)));
		return getFromServerByPost(UrlHelper.setCardBg(), nameValuePairs,
				mUIHandler, handlerTag);
	}

	/**
	 * 发布动态
	 * 
	 * @param activity
	 * @param mUIHandler
	 * @param handlerTag
	 * @param content
	 * @param files
	 * @return
	 */
	public boolean pubDynamic(Activity activity, Handler mUIHandler,
			int handlerTag, String content, ArrayList<String> files) {
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

		nameValuePairs.add(new BasicNameValuePair("content", content));
		Map<String, ArrayList<String>> fileMap = new HashMap<String, ArrayList<String>>();
		fileMap.put("file", files);
		return doPostWithFile(fileMap, nameValuePairs, UrlHelper.pubDynamic(),
				mUIHandler, handlerTag, activity, "pubDynamic", false, null,
				null);
	}

	/**
	 * 供需发布
	 * 
	 * @param activity
	 * @param mUIHandler
	 * @param handlerTag
	 * @param type
	 * @param title
	 * @param content
	 * @param files
	 * @param label
	 * @param contacts
	 * @param phone
	 * @return
	 */
	public boolean pubGongxu(Activity activity, Handler mUIHandler,
			int handlerTag, int type, String title, String content,
			ArrayList<String> files, String label, String contacts, String phone) {
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs
				.add(new BasicNameValuePair("type", String.valueOf(type)));
		nameValuePairs.add(new BasicNameValuePair("title", title));
		nameValuePairs.add(new BasicNameValuePair("content", content));
		nameValuePairs.add(new BasicNameValuePair("label", label));
		nameValuePairs.add(new BasicNameValuePair("contacts", contacts));
		nameValuePairs.add(new BasicNameValuePair("phone", phone));
		Map<String, ArrayList<String>> fileMap = new HashMap<String, ArrayList<String>>();
		fileMap.put("file", files);
		return doPostWithFile(fileMap, nameValuePairs, UrlHelper.pubGongxu(),
				mUIHandler, handlerTag, activity, "pubDynamic", false, null,
				null);
	}

	/**
	 * 删除动态
	 * 
	 * @param mUIHandler
	 * @param handlerTag
	 * @param statusid
	 * @return
	 */
	public boolean deleteDynamic(Handler mUIHandler, int handlerTag,
			String statusid) {
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("statusid", statusid));
		return getFromServerByPost(UrlHelper.getDetailDynamic(),
				nameValuePairs, mUIHandler, handlerTag);
	}

	/**
	 * 收藏动态
	 * 
	 * @param mUIHandler
	 * @param handlerTag
	 * @param statusid
	 * @param type
	 *            1-加入收藏 0-取消收藏
	 * @return
	 */
	public boolean collectDynamic(Handler mUIHandler, int handlerTag,
			String statusid, int type) {
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("statusid", statusid));
		nameValuePairs.add(new BasicNameValuePair("type", type + ""));
		return getFromServerByPost(UrlHelper.collectStatusFamily(),
				nameValuePairs, mUIHandler, handlerTag);
	}

	/**
	 * 收藏圈话题
	 * 
	 * @param mUIHandler
	 * @param handlerTag
	 * @param topicid
	 * @param type
	 * @return
	 */
	public boolean collectTopic(Handler mUIHandler, int handlerTag,
			String topicid, int type) {
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("topicid", topicid));
		nameValuePairs.add(new BasicNameValuePair("type", type + ""));
		return getFromServerByPost(UrlHelper.collectTopic(), nameValuePairs,
				mUIHandler, handlerTag);
	}

	/**
	 * 获得圈子成员列表
	 * 
	 * @param mUIHandler
	 * @param tag
	 * @param groupid
	 * @return
	 */
	public boolean getQuanMemberList(Handler mUIHandler, int tag, String groupid) {
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("groupid", groupid));
		return getFromServerByPost(UrlHelper.getGroupMemberList(),
				nameValuePairs, mUIHandler, tag);
	}

	/**
	 * 获得倬脉动态
	 * 
	 * @param mUIHandler
	 * @param tag
	 * @param groupid
	 * @return
	 */
	public boolean getZhuomaiList(Handler mUIHandler, int tag, int pageNo,
			int pageSize) {
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("pageNo", String
				.valueOf(pageNo)));
		nameValuePairs.add(new BasicNameValuePair("pageSize", String
				.valueOf(pageSize)));
		return getFromServerByPost(UrlHelper.getPutList(), nameValuePairs,
				mUIHandler, tag);
	}

	/**
	 * 与我相关的圈子动态：包括全话题和圈活动
	 * 
	 * @param mUIHandler
	 * @param tag
	 * @param type
	 *            类型 0-全部圈子 1-我创建的圈子 2-我加入的圈子
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	public boolean getQuanStatusList(Handler mUIHandler, int tag, int type,
			int pageNo, int pageSize) {
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs
				.add(new BasicNameValuePair("type", String.valueOf(type)));
		nameValuePairs.add(new BasicNameValuePair("pageNo", String
				.valueOf(pageNo)));
		nameValuePairs.add(new BasicNameValuePair("pageSize", String
				.valueOf(pageSize)));
		return getFromServerByPost(UrlHelper.groupStatus(), nameValuePairs,
				mUIHandler, tag);
	}

	public boolean getFollowReqList(Handler mUIHandler, int tag) {
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		return getFromServerByPost(UrlHelper.getFollowReqList(),
				nameValuePairs, mUIHandler, tag);
	}

	/**
	 * 获取赞、浏览、收藏过我名片的用户列表。
	 * 
	 * @param mUIHandler
	 * @param tag
	 *            0-浏览过我名片的人 1-收藏过我名片的人 2-赞过我名片的人
	 * @return
	 */
	public boolean getMyStatusCard(Handler mUIHandler, int tag, int type) {
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs
				.add(new BasicNameValuePair("type", String.valueOf(type)));
		return getFromServerByPost(UrlHelper.getMyStatusCard(), nameValuePairs,
				mUIHandler, tag);
	}

	/**
	 * 获得我的人脉
	 * 
	 * @param mUIHandler
	 * @param tag
	 * @return
	 */
	public boolean getMyRenmai(Handler mUIHandler, int tag) {
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		return getFromServerByPost(UrlHelper.myRenmai(), nameValuePairs,
				mUIHandler, tag);
	}

	/**
	 * 
	 * @param mUIHandler
	 * @param tag
	 * @param userid
	 *            对方用户ID （要关注的、要取消关注的、要接受的）
	 * @param type
	 *            0取消关注(取消收藏) 1关注(收藏)
	 * @return
	 */
	public boolean followUser(Handler mUIHandler, int tag, String userid,
			int type) {
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("userid", userid));
		nameValuePairs.add(new BasicNameValuePair("type", type + ""));
		return getFromServerByPost(UrlHelper.followUser(), nameValuePairs,
				mUIHandler, tag);
	}

	/**
	 * 申请添加好友=递送名片；接受好友请求=交换名片。 如果对方接受名片交换请求（接受好友添加请求）则成为好友关系。
	 * 注意：好友关系由应用服务器来维护。使用融云发送ContactNotificationMessage联系人好友通知消息后
	 * ，调用此接口通知应用服务器进行相应的好友关系处理。 流程： 1.调用申请添加好友接口(type=1)，在应用服务器建立相应的关联。
	 * 2.通过融云的好友添加通知向对方发送一个通知。 3.对方点接受，调用接受好友添加接口(type=2)。
	 * 
	 * @param mUIHandler
	 * @param tag
	 * @param userid
	 * @param type
	 *            0删除好友 1申请添加好友(递送名片) 2接受添加请求(交换名片)
	 * @return
	 */
	public boolean makeFriends(Handler mUIHandler, int tag, String userid,
			int type) {
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("userid", userid));
		nameValuePairs.add(new BasicNameValuePair("type", type + ""));
		return getFromServerByPost(UrlHelper.makeFriends(), nameValuePairs,
				mUIHandler, tag);
	}

	/**
	 * 获取请求加入圈子的人
	 * 
	 * @param mUIHandler
	 * @param tag
	 * @param groupid
	 *            为null则表示所有请求加入圈子的人
	 * @return
	 */
	public boolean getReqQuanUsers(Handler mUIHandler, int tag, String groupid) {
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		if (groupid != null)
			nameValuePairs.add(new BasicNameValuePair("groupid", groupid));
		return getFromServerByPost(UrlHelper.getReqQuanUsers(), nameValuePairs,
				mUIHandler, tag);
	}

	/**
	 * 获取圈子信息，为融云提供
	 * 
	 * @param mUIHandler
	 * @param tag
	 * @param userid
	 * @param type
	 * @return
	 */
	public boolean getMyGroupList(Handler mUIHandler, int tag) {
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		return getFromServerByPost(UrlHelper.getUrlMyGroupList(),
				nameValuePairs, mUIHandler, tag);
	}

	/**
	 * 用户商务信息：供需
	 * 
	 * @param mUIHandler
	 * @param tag
	 * @param userid
	 * @return
	 */
	public boolean getBusinessInfo(Handler mUIHandler, int tag, String userid) {
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		if (userid != null)
			nameValuePairs.add(new BasicNameValuePair("userid", userid));
		return getFromServerByPost(UrlHelper.getUserBusinessInfo(),
				nameValuePairs, mUIHandler, tag);
	}

	/**
	 * 获得相同条件的用户，同城，同趣等
	 * 
	 * @param mUIHandler
	 * @param tag
	 * @param userid
	 * @return
	 */
	public boolean getSameUser(Handler mUIHandler, int tag, int type,
			int pageNo, int pageSize, String extraStr) {
		String URLS[] = { UrlHelper.getCityJiaren(),
				UrlHelper.getIndustryJiaren(), UrlHelper.getNearJiaren(),
				UrlHelper.getHobbyJiaren(), UrlHelper.getTeatureJiaren(),
				UrlHelper.getAllJiaren() };
		String keys[] = { "city", "industry", "near(暂无)", "hobby", "tchtype",
				"xx" };
		if (type < 1 || type > URLS.length)
			return false;
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs = addPageInfo(nameValuePairs, pageNo, pageSize);
		if (extraStr != null && !extraStr.equals(""))
			nameValuePairs
					.add(new BasicNameValuePair(keys[type - 1], extraStr));
		return getFromServerByPost(UrlHelper.SERVER + URLS[type - 1],
				nameValuePairs, mUIHandler, tag);
	}

	/**
	 * 获得请求交换名片的家人
	 * 
	 * @param mUIHandler
	 * @param tag
	 * @return
	 */
	public boolean getFriendReq(Handler mUIHandler, int tag) {
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		return getFromServerByPost(UrlHelper.getFriendReq(), nameValuePairs,
				mUIHandler, tag);
	}

	/**
	 * 获得公司产品
	 * 
	 * @param mUIHandler
	 * @param tag
	 * @return
	 */
	public boolean getCompanyProduct(Handler mUIHandler, int tag, String comid) {
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("comid", comid));
		return getFromServerByPost(UrlHelper.getProduct(), nameValuePairs,
				mUIHandler, tag);
	}

	public boolean getZMDTCount(Handler mUIHandler, int tag, String userid) {
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		if (userid != null)
			nameValuePairs.add(new BasicNameValuePair("userid", userid));
		return getFromServerByPost(UrlHelper.getZMDT(), nameValuePairs,
				mUIHandler, tag);
	}

	/**
	 * 获取公司信息
	 * 
	 * @param mUIHandler
	 * @param tag
	 * @param userid
	 *            (可选)不填则获取我的公司信息，填则获取指定用户的公司信息。
	 * @return
	 */
	public boolean getCompanyInfo(Handler mUIHandler, int tag, String userid) {
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		if (userid != null)
			nameValuePairs.add(new BasicNameValuePair("userid", userid));
		return getFromServerByPost(UrlHelper.getCompany(), nameValuePairs,
				mUIHandler, tag);
	}

	/**
	 * 删除产品信息
	 * 
	 * @param mUIHandler
	 * @param tag
	 * @param productid
	 * @return
	 */
	public boolean deleteProduct(Handler mUIHandler, int tag, String productid) {
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("productid", productid));
		return getFromServerByPost(UrlHelper.deleteProduct(), nameValuePairs,
				mUIHandler, tag);
	}

	/**
	 * 删除公司信息
	 * 
	 * @param mUIHandler
	 * @param tag
	 * @param comid
	 * @return
	 */
	public boolean deleteCompanyInfo(Handler mUIHandler, int tag, String comid) {
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("comid", comid));
		return getFromServerByPost(UrlHelper.deleteCompany(), nameValuePairs,
				mUIHandler, tag);
	}

	public boolean updateProduct(Activity activity, Handler mUIHandler,
			int tag, String productid, String product, String description,
			String customer, String value, ArrayList<String> files,
			String originFilekeys) {
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("productid", productid));
		nameValuePairs.add(new BasicNameValuePair("product", product));
		nameValuePairs.add(new BasicNameValuePair("description", description));
		nameValuePairs.add(new BasicNameValuePair("customer", customer));
		nameValuePairs.add(new BasicNameValuePair("value", value));
		Map<String, ArrayList<String>> fileMap = new HashMap<String, ArrayList<String>>();
		fileMap.put("file", files);
		return doPostWithFile(fileMap, nameValuePairs,
				UrlHelper.updateProduct(), mUIHandler, tag, activity,
				"updateProduct", false, null, null, originFilekeys);
	}

	public boolean addProduct(Activity activity, Handler mUIHandler, int tag,
			String comid, String product, String description, String customer,
			String value, ArrayList<String> files, String originFilekeys) {
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("comid", comid));
		nameValuePairs.add(new BasicNameValuePair("product", product));
		nameValuePairs.add(new BasicNameValuePair("description", description));
		nameValuePairs.add(new BasicNameValuePair("customer", customer));
		nameValuePairs.add(new BasicNameValuePair("value", value));
		Map<String, ArrayList<String>> fileMap = new HashMap<String, ArrayList<String>>();
		fileMap.put("file", files);
		return doPostWithFile(fileMap, nameValuePairs, UrlHelper.addProduct(),
				mUIHandler, tag, activity, "addProduct", false, null, null,
				originFilekeys);
	}

	/**
	 * 
	 * @param activity
	 * @param mUIHandler
	 * @param tag
	 * @param comid
	 *            公司ID ,其他参数可选
	 * @param company
	 * @param industry
	 * @param city
	 * @param position
	 * @param homepage
	 * @param status
	 *            (可选)是否是主公司 1-主公司 0-普通公司
	 * @return
	 */
	public boolean updateCompany(Handler mUIHandler, int tag, String comid,
			String company, int industry, int city, int position,
			String homepage, int status) {
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("comid", comid));
		nameValuePairs.add(new BasicNameValuePair("company", company));
		nameValuePairs.add(new BasicNameValuePair("industry", String
				.valueOf(industry)));
		nameValuePairs
				.add(new BasicNameValuePair("city", String.valueOf(city)));
		nameValuePairs.add(new BasicNameValuePair("position", String
				.valueOf(position)));
		nameValuePairs.add(new BasicNameValuePair("homepage", homepage));
		nameValuePairs.add(new BasicNameValuePair("status", String
				.valueOf(status)));
		return getFromServerByPost(UrlHelper.updateCompany(), nameValuePairs,
				mUIHandler, tag);
	}

	/**
	 * 获取搜索热词
	 * 
	 * @param mUIHandler
	 * @param tag
	 * @return
	 */
	public boolean getHotKey(Handler mUIHandler, int tag) {
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		return getFromServerByPost(UrlHelper.getHotKey(), nameValuePairs,
				mUIHandler, tag);
	}

	/**
	 * 根据关键词检索用户。 关键词可以为名字，公司名，职位。
	 * 
	 * @param mUIHandler
	 * @param tag
	 * @param keyword
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	public boolean getSearchContent(Handler mUIHandler, int tag,
			String keyword, int pageNo, int pageSize) {
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("keyword", keyword));
		nameValuePairs.add(new BasicNameValuePair("pageNo", String
				.valueOf(pageNo)));
		nameValuePairs.add(new BasicNameValuePair("pageSize", String
				.valueOf(pageSize)));
		return getFromServerByPost(UrlHelper.getPortalSearch(), nameValuePairs,
				mUIHandler, tag);
	}

	/**
	 * 获取我的好友列表
	 * 
	 * @param mUIHandler
	 * @param tag
	 * @return
	 */
	public boolean getMyFriends(Handler mUIHandler, int tag) {
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

		return getFromServerByPost(UrlHelper.getMyFriends(), nameValuePairs,
				mUIHandler, tag);
	}

	public boolean addCompany(Handler mUIHandler, int tag, String company,
			int industry, int city, int position, String homepage, int status) {
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("company", company));
		nameValuePairs.add(new BasicNameValuePair("industry", String
				.valueOf(industry)));
		nameValuePairs
				.add(new BasicNameValuePair("city", String.valueOf(city)));
		nameValuePairs.add(new BasicNameValuePair("position", String
				.valueOf(position)));
		nameValuePairs.add(new BasicNameValuePair("homepage", homepage));
		nameValuePairs.add(new BasicNameValuePair("status", String
				.valueOf(status)));
		return getFromServerByPost(UrlHelper.addCompany(), nameValuePairs,
				mUIHandler, tag);
	}

	/**
	 * 评论和回复供需
	 * 
	 * @param mUIHandler
	 * @param tag
	 * @param sdid
	 *            供需ID
	 * @param comment
	 * @param toId
	 *            可选 要回复的评论ID (不填则是普通评论)
	 * @param toUserid
	 *            可选 要回复的用户ID (不填则是普通评论)
	 * @return
	 */
	public boolean cmtGongxu(Handler mUIHandler, int tag, String sdid,
			String comment, String toId, String toUserid) {
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		if (sdid == null || comment == null)
			return false;
		nameValuePairs.add(new BasicNameValuePair("sdid", sdid));
		nameValuePairs.add(new BasicNameValuePair("comment", comment));
		if (toId != null)
			nameValuePairs.add(new BasicNameValuePair("toId", toId));
		if (toId != null)
			nameValuePairs.add(new BasicNameValuePair("toUserid", toUserid));
		return getFromServerByPost(UrlHelper.cmtGX(), nameValuePairs,
				mUIHandler, tag);
	}

	/**
	 * 获得基本编码数据：id和值的对应
	 * 
	 * @param handler
	 * @param handlerTag
	 * @param activity
	 * @param cancelable
	 * @param cancel
	 * @param data
	 * @return
	 */
	public boolean getBaseCodeData(Handler handler, int handlerTag,
			Activity activity, boolean cancelable, OnCancelListener cancel,
			String data) {
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs = addUserInfoByPost(nameValuePairs);
		nameValuePairs.add(new BasicNameValuePair("version", "0"));
		String url = UrlHelper.getBaseCodeData();
		return doPost(nameValuePairs, url, handler, handlerTag, activity, url,
				cancelable, cancel, data);
	}
/**
 * 获得供需类型列表
 * @param handler
 * @param handlerTag
 * @param activity
 * @param cancelable
 * @param cancel
 * @param data
 * @return
 */
	public boolean getGXTypes(Handler handler, int handlerTag,
			Activity activity, boolean cancelable, OnCancelListener cancel,
			String data) {
		String res = readObject(GXTYPES);
		if (res != null) {
			Message msg = handler.obtainMessage(handlerTag);
			Bundle bundle = new Bundle();
			bundle.putString("data", data);
			msg.setData(bundle);
			msg.obj = res;
			msg.sendToTarget();
			return true;
		} else {
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs = addUserInfoByPost(nameValuePairs);
			nameValuePairs.add(new BasicNameValuePair("version", "0"));
			String url = UrlHelper.getGXTypes();
			return doPost(nameValuePairs, url, handler, handlerTag, activity,
					url, cancelable, cancel, data);
		}
	}

	/**
	 * 获取城市列表,lef
	 */
	public boolean getCitys(Handler handler, int handlerTag, Activity activity,
			boolean cancelable, OnCancelListener cancel, String data) {
		String res = readObject(CITYS);
		if (res != null) {
			Message msg = handler.obtainMessage(handlerTag);
			Bundle bundle = new Bundle();
			bundle.putString("data", data);
			msg.setData(bundle);
			msg.obj = res;
			msg.sendToTarget();
			return true;
		} else {
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs = addUserInfoByPost(nameValuePairs);
			nameValuePairs.add(new BasicNameValuePair("version", "0"));
			String url = UrlHelper.getServiceCityList();
			return doPost(nameValuePairs, url, handler, handlerTag, activity,
					url, cancelable, cancel, data);
		}
	}

	/**
	 * 
	 * @param sdflag
	 *            (可选) 供需标识 0-资源 1-需求
	 * @param type
	 *            (可选) 供需类型 如果填则根据指定类型筛选信息
	 * @param title
	 * @param pageNo
	 * @param pageSize
	 * @param handler
	 * @param handlerTag
	 * @param activity
	 * @param cancelable
	 * @param cancel
	 * @param data
	 * @param userid
	 * @return
	 */
	public boolean getGongXuList(String sdflag, String type, String title,
			int pageNo, int pageSize, Handler handler, int handlerTag,
			Activity activity, boolean cancelable, OnCancelListener cancel,
			String data, String userid) {
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs = addPageInfo(nameValuePairs, pageNo, pageSize);
		nameValuePairs = addUserInfoByPost(nameValuePairs);
		if (sdflag != null)
			nameValuePairs.add(new BasicNameValuePair("sdflag", sdflag));
		if (type != null)
			nameValuePairs.add(new BasicNameValuePair("type", type));
		if (title != null)
			nameValuePairs.add(new BasicNameValuePair("title", title));
		if (userid != null)
			nameValuePairs.add(new BasicNameValuePair("userid", userid));
		String url = UrlHelper.getGongxulist();
		return doPost(nameValuePairs, url, handler, handlerTag, activity, url,
				cancelable, cancel, data);
	}

	/***
	 * 删除需求
	 */
	public boolean deleteGongxu(String sdid, Handler handler, int handlerTag,
			Activity activity) {
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs = addUserInfoByPost(nameValuePairs);
		nameValuePairs.add(new BasicNameValuePair("sdid", sdid));
		String url = UrlHelper.getDELETEGONGXU();
		return doPost(nameValuePairs, url, handler, handlerTag, activity, url,
				true, null, null);
	}

	/**
	 * 保存对象
	 * 
	 * @param ser
	 * @param file
	 * @throws IOException
	 */
	public boolean saveObject(String ser, String file) {
		FileOutputStream fos = null;
		ObjectOutputStream oos = null;
		try {
			fos = context.openFileOutput(file, context.MODE_PRIVATE);
			oos = new ObjectOutputStream(fos);
			oos.writeObject(ser);
			oos.flush();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			try {
				oos.close();
			} catch (Exception e) {
			}
			try {
				fos.close();
			} catch (Exception e) {
			}
		}
	}

	/**
	 * 读取对象,lef
	 * 
	 * @param file
	 * @return
	 * @throws IOException
	 */
	public String readObject(String file) {
		if (!isExistDataCache(file))
			return null;
		FileInputStream fis = null;
		ObjectInputStream ois = null;
		try {
			fis = context.openFileInput(file);
			ois = new ObjectInputStream(fis);
			return (String) ois.readObject();
		} catch (FileNotFoundException e) {
		} catch (Exception e) {
			e.printStackTrace();
			// 反序列化失败 - 删除缓存文件
			if (e instanceof InvalidClassException) {
				File data = context.getFileStreamPath(file);
				data.delete();
			}
		} finally {
			try {
				ois.close();
			} catch (Exception e) {
			}
			try {
				fis.close();
			} catch (Exception e) {
			}
		}
		return null;
	}

	/**
	 * 判断缓存是否存在
	 * 
	 * @param cachefile
	 * @return
	 */
	private boolean isExistDataCache(String cachefile) {
		boolean exist = false;
		if (context == null)
			return exist;
		File data = context.getFileStreamPath(cachefile);
		if (data.exists())
			exist = true;
		return exist;
	}

	/**
	 * 设置群组信息提供者，并发送消息到群主
	 * 
	 * @param context
	 * @param targetId
	 *            对方ID
	 * @param group
	 * @param type
	 *            0,退出；1 加入
	 */
	public void followQuan(final Context context, String targetId, Group group,
			int type) {
		HashMap<String, Group> groupHashMap = getGroupMap();
		// reqMsg已实现， quitMsg需要重新自定义
		if (type == 1) {// 加入群
			groupHashMap.put(group.getId(), group);
		} else if (type == 0) {
			groupHashMap.remove(group.getId());
		}
		setGroupMap(groupHashMap);
	}

	// appclient
	/**
	 * 刷新session
	 * 
	 * @param context
	 */
	public void refreshSession(final Context context) {
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		AsyncConnectHelper conn = new AsyncConnectHelper(
				addUserInfo(nameValuePairs), UrlHelper.getUrlLogin(),
				new AsyncConnectHelper.FinishCallback() {

					@Override
					public boolean onReturn(String rs, int responseCode) {
						// TODO Auto-generated method stub
						if (JsonHandler.checkResult(rs,
								context.getApplicationContext())) {
							// 获取session
							LoginRes res = JsonHandler.parseLoginRes(context,
									JsonHandler.parseResult(rs).getData());
							refreshUserInfo(res);
						}
						return false;
					}
				}, null);
		conn.execute();
	}

	public void refreshUserInfo(LoginRes res) {
		SharedPreferences sp = context.getSharedPreferences("cpzhuojiaren",
				Activity.MODE_PRIVATE);
		SharedPreferences.Editor editor = sp.edit();
		editor.putString(ResHelper.SESSION, res.getSession());
		editor.putString(ResHelper.UPLIOAD_TOKEN, res.getQiniuToken());
		editor.putString(ResHelper.IM_TOKEN, res.getRongyunToken());
		editor.commit();

		// 保存到两个单例中
		ResHelper mResHelper = ResHelper.getInstance(context);
		mResHelper.setSessionForAPP(res.getSession());
		mResHelper.setUpLoadTokenForQiniu(res.getQiniuToken());
		mResHelper.setImTokenForRongyun(res.getRongyunToken());
		setSession(res.getSession());
		setUploadFileToken(res.getQiniuToken());
		setImToken(res.getRongyunToken());
		if (context != null)
			init(context);
	}

	/**
	 * 获取圈子,根据url区分类别
	 */
	public boolean getQuanzi(String url, String gtype, String city, int pageNo,
			int pageSize, Handler handler, int handlerTag, Activity activity,
			boolean cancelable, OnCancelListener cancel, String data,
			String userid) {
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs = addPageInfo(nameValuePairs, pageNo, pageSize);
		nameValuePairs = addUserInfoByPost(nameValuePairs);
		if (gtype != null)
			nameValuePairs.add(new BasicNameValuePair("gtype", gtype));
		if (city != null)
			nameValuePairs.add(new BasicNameValuePair("city", city));
		if (userid != null)
			nameValuePairs.add(new BasicNameValuePair("userid", userid));
		return doPost(nameValuePairs, url, handler, handlerTag, activity, url,
				cancelable, cancel, data);
	}

	/**
	 * 获取商品大类
	 * 
	 * @param url
	 * @param handler
	 * @param handlerTag
	 * @param activity
	 * @param cancelable
	 * @param cancel
	 * @param data
	 * @return
	 */
	public boolean getGoodsCategory(Handler handler, int handlerTag,
			Activity activity, boolean cancelable, OnCancelListener cancel,
			String data) {
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs = addUserInfoByPost(nameValuePairs);
		return doPost(nameValuePairs, UrlHelper.getGOODSCATEGORY(), handler,
				handlerTag, activity, UrlHelper.getGOODSCATEGORY(), cancelable,
				cancel, data);
	}

	/**
	 * 送倬币给朋友
	 * 
	 * @param handler
	 * @param handlerTag
	 * @param activity
	 * @param cancelable
	 * @param cancel
	 * @param data
	 * @return
	 */
	public boolean giveZhuobiToFriend(Handler handler, int handlerTag,
			Activity activity, boolean cancelable, OnCancelListener cancel,
			String userid, String zhuobi) {
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs = addUserInfoByPost(nameValuePairs);
		nameValuePairs.add(new BasicNameValuePair("userid", userid));
		nameValuePairs.add(new BasicNameValuePair("zhuobi", zhuobi));
		return doPost(nameValuePairs, UrlHelper.getGIVEMONEYTOFRIEND(),
				handler, handlerTag, activity,
				UrlHelper.getGIVEMONEYTOFRIEND(), cancelable, cancel, null);
	}



	/***
	 * 解散圈子
	 */
	public boolean disolveQuan(String groupId, String content, Handler handler,
			int handlerTag, Activity activity, boolean cancelable,
			OnCancelListener cancel, String data) {
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs = addUserInfoByPost(nameValuePairs);
		nameValuePairs.add(new BasicNameValuePair("groupid", groupId));
		nameValuePairs.add(new BasicNameValuePair("content", content));
		String url = UrlHelper.getDisolveQuan();
		return doPost(nameValuePairs, url, handler, handlerTag, activity, url,
				cancelable, cancel, data);
	}

	/**
	 * 退出圈子
	 */
	public boolean quitQuan(String groupId, String type, String content,
			Handler handler, int handlerTag, Activity activity,
			boolean cancelable, OnCancelListener cancel, String data) {
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs = addUserInfoByPost(nameValuePairs);
		// type int 0取消关注 1申请关注 2接受关注
		nameValuePairs.add(new BasicNameValuePair("type", type));
		nameValuePairs.add(new BasicNameValuePair("groupid", groupId));
		nameValuePairs.add(new BasicNameValuePair("content", content));
		String url = UrlHelper.getQuitQuan();
		return doPost(nameValuePairs, url, handler, handlerTag, activity, url,
				cancelable, cancel, data);
	}

	/**
	 * 获取视频在线
	 */
	public boolean getVedioList(String tutorId, String typeId, int pageNo,
			int pageSize, Handler handler, int handlerTag, Activity activity,
			boolean cancelable, OnCancelListener cancel, String data) {
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs = addUserInfoByPost(nameValuePairs);
		nameValuePairs = addPageInfo(nameValuePairs, pageNo, pageSize);
		if (tutorId != null)
			nameValuePairs.add(new BasicNameValuePair("tutorId", tutorId));
		if (typeId != null)
			nameValuePairs.add(new BasicNameValuePair("typeId", typeId));
		String url = UrlHelper.getServiceVedioList();
		return doPost(nameValuePairs, url, handler, handlerTag, activity, url,
				cancelable, cancel, data);
	}

	/**
	 * 获取视频在线收藏
	 */
	public boolean getVedioCollectionList(String tutorId, String typeId,
			int pageNo, int pageSize, Handler handler, int handlerTag,
			Activity activity, boolean cancelable, OnCancelListener cancel,
			String data) {
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs = addUserInfoByPost(nameValuePairs);
		nameValuePairs = addPageInfo(nameValuePairs, pageNo, pageSize);
		if (tutorId != null)
			nameValuePairs.add(new BasicNameValuePair("tutorId", tutorId));
		if (typeId != null)
			nameValuePairs.add(new BasicNameValuePair("typeId", typeId));
		String url = UrlHelper.getServiceVedioListCollection();
		return doPost(nameValuePairs, url, handler, handlerTag, activity, url,
				cancelable, cancel, data);
	}

	/**
	 * 提交视频统计
	 */
	public boolean submitVedio(Activity activity, String Id) {
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("onlineid", Id));
		nameValuePairs = addUserInfoByPost(nameValuePairs);
		String url = UrlHelper.getSubmitVisit();
		return doPost(nameValuePairs, url, null, 0, activity, url, false, null,
				null);
	}

	/**
	 * 获取音频在线
	 */
	public boolean getAudioList(int pageNo, int pageSize, Handler handler,
			int handlerTag, Activity activity, boolean cancelable,
			OnCancelListener cancel, String data) {
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs = addUserInfoByPost(nameValuePairs);
		nameValuePairs = addPageInfo(nameValuePairs, pageNo, pageSize);
		String url = UrlHelper.getServiceAudioList();
		return doPost(nameValuePairs, url, handler, handlerTag, activity, url,
				cancelable, cancel, data);
	}

	/**
	 * 获取音频在线
	 */
	public boolean getAudioListCollection(int pageNo, int pageSize,
			Handler handler, int handlerTag, Activity activity,
			boolean cancelable, OnCancelListener cancel, String data) {
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs = addUserInfoByPost(nameValuePairs);
		nameValuePairs = addPageInfo(nameValuePairs, pageNo, pageSize);
		String url = UrlHelper.getAudioCollection();
		return doPost(nameValuePairs, url, handler, handlerTag, activity, url,
				cancelable, cancel, data);
	}

	/**
	 * 获取商品列表 参数 类型 说明 categoryid ： 商品类别id（可选） keyword ： 搜索商品关键字（可选） providerid
	 * ： 供应商id（可选） （以上参数均不传，获取商城首页默认商品） pageNo ： 页码 pageSize ： 显示数
	 */
	public boolean getGoodsList(int pageNo, int pageSize, Handler handler,
			int handlerTag, Activity activity, String categoryid,
			String keyword, String providerid) {
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs = addUserInfoByPost(nameValuePairs);
		nameValuePairs = addPageInfo(nameValuePairs, pageNo, pageSize);
		if (categoryid != null)
			nameValuePairs
					.add(new BasicNameValuePair("categoryid", categoryid));
		if (keyword != null)
			nameValuePairs.add(new BasicNameValuePair("keyword", keyword));
		if (providerid != null)
			nameValuePairs
					.add(new BasicNameValuePair("providerid", providerid));
		String url = UrlHelper.getGOODSLIST();
		return doPost(nameValuePairs, url, handler, handlerTag, activity, url,
				false, null, null);
	}

	/**
	 * 获取众筹
	 */
	public boolean getFundingList(int catid, int ismy, int pageNo,
			int pageSize, Handler handler, int handlerTag, Activity activity,
			boolean cancelable, OnCancelListener cancel, String data) {
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs = addUserInfoByPost(nameValuePairs);
		nameValuePairs = addPageInfo(nameValuePairs, pageNo, pageSize);
		if (ismy >= 0)
			nameValuePairs.add(new BasicNameValuePair("ismy", ismy + ""));
		nameValuePairs.add(new BasicNameValuePair("catid", catid + ""));
		String url = UrlHelper.getServiceFundingList();
		return doPost(nameValuePairs, url, handler, handlerTag, activity, url,
				cancelable, cancel, data);
	}

	/**
	 * 获取众筹投资
	 */
	public boolean getFundingListInvest(int pageNo, int pageSize,
			Handler handler, int handlerTag, Activity activity,
			boolean cancelable, OnCancelListener cancel, String data) {
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs = addUserInfoByPost(nameValuePairs);
		nameValuePairs = addPageInfo(nameValuePairs, pageNo, pageSize);
		String url = UrlHelper.getServiceFundingListInvest();
		return doPost(nameValuePairs, url, handler, handlerTag, activity, url,
				cancelable, cancel, data);
	}

	/**
	 * 获取明细
	 */
	public boolean getIncomeList(int pageNo, int pageSize, Handler handler,
			int handlerTag, Activity activity) {
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs = addUserInfoByPost(nameValuePairs);
		nameValuePairs = addPageInfo(nameValuePairs, pageNo, pageSize);
		String url = UrlHelper.getINCOME();
		return doPost(nameValuePairs, url, handler, handlerTag, activity, url,
				false, null, null);
	}

	/**
	 * 移除购物车goods
	 */
	public boolean removeGoods(String goodsid, Handler handler, int handlerTag,
			Activity activity) {
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs = addUserInfoByPost(nameValuePairs);
		if (goodsid != null && goodsid.length() > 0) {
			goodsid = goodsid.substring(0, goodsid.length() - 1);
		}
		nameValuePairs.add(new BasicNameValuePair("goodsid", goodsid));
		String url = UrlHelper.getREMOVEGOODS();
		return doPost(nameValuePairs, url, handler, handlerTag, activity, url,
				false, null, null);
	}

	/**
	 * 获取采访
	 * 
	 * @param pageNo
	 * @param pageSize
	 * @param handler
	 * @param handlerTag
	 * @param activity
	 * @param cancelable
	 * @param cancel
	 * @param data
	 * @return
	 */
	public boolean getVisiteList(int pageNo, int pageSize, Handler handler,
			int handlerTag, Activity activity, boolean cancelable,
			OnCancelListener cancel, String data) {
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs = addUserInfoByPost(nameValuePairs);
		nameValuePairs = addPageInfo(nameValuePairs, pageNo, pageSize);
		String url = UrlHelper.getServiceVisitList();
		return doPost(nameValuePairs, url, handler, handlerTag, activity, url,
				cancelable, cancel, data);
	}

	/**
	 * 获取成长在线类型
	 * 
	 * @param handler
	 * @param handlerTag
	 * @param activity
	 * @param cancelable
	 * @param cancel
	 * @param data
	 * @return
	 */
	public boolean getGrowthOnlineType(Handler handler, int handlerTag,
			Activity activity) {
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs = addUserInfoByPost(nameValuePairs);
		String url = UrlHelper.getGrowthonlinetype();
		return doPost(nameValuePairs, url, handler, handlerTag, activity, url,
				false, null, null);
	}

	/**
	 * 
	 * @param handler
	 * @param handlerTag
	 * @param activity
	 * @return
	 */
	public boolean getTeacherList(Handler handler, int handlerTag,
			Activity activity) {
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs = addUserInfoByPost(nameValuePairs);
		String url = UrlHelper.getServiceTeacherList();
		return doPost(nameValuePairs, url, handler, handlerTag, activity, url,
				false, null, null);
	}

	/**
	 * 收藏
	 */
	public boolean collection(Activity acitivity, String url, String idKey,
			String id, String stateKey, String state) {
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs = addUserInfoByPost(nameValuePairs);
		nameValuePairs.add(new BasicNameValuePair(idKey, id));
		if (stateKey != null && !stateKey.equals(""))
			nameValuePairs.add(new BasicNameValuePair(stateKey, state));
		return doPost(nameValuePairs, url, null, 0, acitivity, url, false,
				null, null);
	}

	public boolean collection(Activity acitivity, Handler handler,
			int handlerTag, String url, String idKey, String id,
			String stateKey, String state) {
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs = addUserInfoByPost(nameValuePairs);
		nameValuePairs.add(new BasicNameValuePair(idKey, id));
		nameValuePairs.add(new BasicNameValuePair(stateKey, state));
		return doPost(nameValuePairs, url, handler, handlerTag, acitivity, url,
				false, null, null);
	}

	/**
	 * 获取我倬币
	 */
	public boolean getMyZhuoBi(Activity acitivity, Handler handler,
			int handlerTag) {
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs = addUserInfoByPost(nameValuePairs);
		return doPost(nameValuePairs, UrlHelper.getMYZHUOBI(), handler,
				handlerTag, acitivity, UrlHelper.getMYZHUOBI(), false, null,
				null);
	}

	/**
	 * 用倬币支付
	 * 
	 * @param acitivity
	 * @param handler
	 * @param handlerTag
	 * @param url
	 * @param idKey
	 * @param id
	 * @param stateKey
	 * @param state
	 * @return
	 */
	public boolean payWithZhuobi(Activity acitivity, Handler handler,
			int handlerTag, String money, String number) {
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs = addUserInfoByPost(nameValuePairs);
		nameValuePairs.add(new BasicNameValuePair("zhuobi", money));
		nameValuePairs.add(new BasicNameValuePair("billNo", number));
		String url = UrlHelper.getMALLPAY();
		return doPost(nameValuePairs, url, handler, handlerTag, acitivity, url,
				false, null, null);
	}

	/**
	 * 支付状态
	 * 
	 * @param acitivity
	 * @param handler
	 * @param handlerTag
	 * @param url
	 * @param idKey
	 * @param id
	 * @param stateKey
	 * @param state
	 * @return
	 */
	public boolean postPayStatus(Activity acitivity, Handler handler,
			int handlerTag, String billNo, String status) {
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs = addUserInfoByPost(nameValuePairs);
		nameValuePairs.add(new BasicNameValuePair("billNo", billNo));
		nameValuePairs.add(new BasicNameValuePair("status", status));
		String url = UrlHelper.getPOSTPAYSTATUS();
		return doPost(nameValuePairs, url, handler, handlerTag, acitivity, url,
				false, null, null);
	}

	/**
	 * 设置收货地址
	 * 
	 * @param acitivity
	 * @param handler
	 * @param handlerTag
	 * @param url
	 * @param idKey
	 * @param id
	 * @param stateKey
	 * @param state
	 * @return
	 */
	public boolean setShippingAddress(Activity acitivity, Handler handler,
			int handlerTag, String street, String detail, String receiptor,
			String phone, String zip) {
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs = addUserInfoByPost(nameValuePairs);
		nameValuePairs.add(new BasicNameValuePair("street", street));
		nameValuePairs.add(new BasicNameValuePair("detail", detail));
		nameValuePairs.add(new BasicNameValuePair("receiptor", receiptor));
		nameValuePairs.add(new BasicNameValuePair("phone", phone));
		nameValuePairs.add(new BasicNameValuePair("zip", zip));
		String url = UrlHelper.getSHIPPINGADDRESS();
		return doPost(nameValuePairs, url, handler, handlerTag, acitivity, url,
				false, null, null);
	}

	public boolean GoodsAddToCart(Activity acitivity, Handler handler,
			int handlerTag, String id) {
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs = addUserInfoByPost(nameValuePairs);
		nameValuePairs.add(new BasicNameValuePair("goodsid", id));
		String url = UrlHelper.getGoodsAddToCart();
		return doPost(nameValuePairs, url, handler, handlerTag, acitivity, url,
				false, null, null);
	}

	/**
	 * 发布进展
	 */
	public boolean pubProgress(Activity activity, Handler handler,
			int handlerTag, String id, String content) {
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs = addUserInfoByPost(nameValuePairs);
		nameValuePairs.add(new BasicNameValuePair("id", id));
		nameValuePairs.add(new BasicNameValuePair("content", content));
		String url = UrlHelper.getPubcrowdfundingcomment();
		return doPost(nameValuePairs, url, handler, handlerTag, activity, url,
				false, null, null);
	}

	/**
	 * 获取进展列表
	 */
	public boolean getProgress(Activity activity, Handler handler,
			int handlerTag, String id) {
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs = addUserInfoByPost(nameValuePairs);
		nameValuePairs.add(new BasicNameValuePair("id", id));
		String url = UrlHelper.getGetcrowdfundingprogress();
		return doPost(nameValuePairs, url, handler, handlerTag, activity, url,
				false, null, null);
	}

	/**
	 * 回报
	 * 
	 * @param activity
	 * @param handler
	 * @param handlerTag
	 * @param id
	 * @return
	 */
	public boolean getPayback(Activity activity, Handler handler,
			int handlerTag, String id) {
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs = addUserInfoByPost(nameValuePairs);
		nameValuePairs.add(new BasicNameValuePair("id", id));
		String url = UrlHelper.getPaybackList();
		return doPost(nameValuePairs, url, handler, handlerTag, activity, url,
				false, null, null);
	}

	/**
	 * 创建圈子
	 */
	// session string(32) 账号session 登陆返回
	// gname string(32) 圈子名称
	// gheader string(200) 圈子logo图片名(七牛云key)
	// gintro string(150) 圈子简介
	// gpub string(150) 圈子公告
	// gtype int 圈子类别 0:未知，1:资源整合圈，2:金融投资圈，3:兴趣爱好圈，4:精进成长圈，5:同城家人圈，6:公益活动圈
	// city int 地区 城市编码
	// followpms int 加入权限 0:允许任何人加入，1:需要申请才能加入
	// accesspms int 访问权限 0:所有人都可以访问，1:加入圈子才可以访问
	public boolean createQuan(Activity activity, Handler handler,
			int handlerTag, String gname, String gintro, String gtype,
			String city, String followpms, String accesspms, String gpub,
			ArrayList<String> files) {
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs = addUserInfoByPost(nameValuePairs);
		nameValuePairs.add(new BasicNameValuePair("gname", gname));
		nameValuePairs.add(new BasicNameValuePair("gintro", gintro));
		nameValuePairs.add(new BasicNameValuePair("gtype", gtype));
		nameValuePairs.add(new BasicNameValuePair("city", city));
		nameValuePairs.add(new BasicNameValuePair("followpms", followpms));
		nameValuePairs.add(new BasicNameValuePair("accesspms", accesspms));
		nameValuePairs.add(new BasicNameValuePair("gpub", gpub));
		Map<String, ArrayList<String>> filesMap = new HashMap<String, ArrayList<String>>();
		filesMap.put("gheader", files);
		return doPostWithFile(filesMap, nameValuePairs,
				UrlHelper.getCreategroup(), handler, handlerTag, activity, "1",
				false, null, null);
	}

	/**
	 * 发布活动
	 */
	public boolean createEvent(Activity activity, Handler handler,
			int handlerTag, String longitude, String latitude, String groupid,
			String title, String content, String contacts, String starttime,
			String endtime, String address, String phone,
			ArrayList<String> files) {
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs = addUserInfoByPost(nameValuePairs);
		nameValuePairs.add(new BasicNameValuePair("groupid", groupid));
		nameValuePairs.add(new BasicNameValuePair("title", title));
		nameValuePairs.add(new BasicNameValuePair("content", content));
		nameValuePairs.add(new BasicNameValuePair("contacts", contacts));
		nameValuePairs.add(new BasicNameValuePair("starttime", starttime));
		nameValuePairs.add(new BasicNameValuePair("endtime", endtime));
		nameValuePairs.add(new BasicNameValuePair("address", address));
		nameValuePairs.add(new BasicNameValuePair("phone", phone));
		nameValuePairs.add(new BasicNameValuePair("longtitude", longitude));
		nameValuePairs.add(new BasicNameValuePair("latitude", latitude));
		nameValuePairs.add(new BasicNameValuePair("phone", phone));
		Map<String, ArrayList<String>> filesMap = new HashMap<String, ArrayList<String>>();
		filesMap.put("file", files);
		return doPostWithFile(filesMap, nameValuePairs,
				UrlHelper.getAddgroupactivity(), handler, handlerTag, activity,
				"1", false, null, null);
	}

	/**
	 * 退出活动
	 */
	public boolean quitEvent(String url, String idKey, String id,
			String stateKey, String state, Handler handler, int handlerTag,
			Activity activity) {
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs = addUserInfoByPost(nameValuePairs);
		nameValuePairs.add(new BasicNameValuePair(idKey, id));
		nameValuePairs.add(new BasicNameValuePair(stateKey, state));
		return doPost(nameValuePairs, url, handler, handlerTag, activity, url,
				false, null, null);
	}

	public boolean updateFilesForCrowdFunding(
			final Map<String, ArrayList<String>> filesMap,
			final Handler handler, final int handlerTag,
			final Activity activity, final String tag) {
		if (CommonUtil.getNetworkState(activity) == 2) {

			CommonUtil.displayToast(activity, R.string.error0);
			return false;
		}
		if (!mStartedTag.contains(tag) || tag == null) {
			if (tag != null) {
				mStartedTag.add(tag);
			}
			AsyncUploadHelper helper = new AsyncUploadHelper(activity,
					uploadFileToken, filesMap, new ICompleteCallback() {

						@Override
						public void onReturn(Map<String, StringBuilder> map) {
							// TODO Auto-generated method stub
							if (tag != null)
								mStartedTag.remove(tag);
							if (map == null || map.size() == 0)
								Toast.makeText(activity, "图片上传失败，请重新登录提交",
										Toast.LENGTH_LONG).show();
							else if (map.size() > 0) {
								if (handler != null) {
									Message msg = handler
											.obtainMessage(handlerTag);
									msg.obj = map;
									msg.sendToTarget();
								}
							}
						}
					});
			helper.execute("test");
			return true;
		} else {
			CommonUtil.displayToast(activity, "不要重复提交");
		}
		return false;
	}

	/**
	 * 创建众筹
	 */
	public boolean createCrowdFunding(Activity activity, Handler handler,
			int handlerTag, String title, String catid, String targetZb,
			String thumbPic, String support, String description) {
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs = addUserInfoByPost(nameValuePairs);
		nameValuePairs.add(new BasicNameValuePair("title", title));
		nameValuePairs.add(new BasicNameValuePair("catid", catid));
		nameValuePairs.add(new BasicNameValuePair("targetZb", targetZb));
		nameValuePairs.add(new BasicNameValuePair("thumbPic", thumbPic));
		nameValuePairs.add(new BasicNameValuePair("support", support));
		nameValuePairs.add(new BasicNameValuePair("description", description));
		String url = UrlHelper.getCreatecrowdfunding();
		return doPost(nameValuePairs, url, handler, handlerTag, activity, url,
				false, null, null);
	}

	/**
	 * 获取众筹详情
	 */
	public boolean getCrowdFunding(Activity activity, Handler handler,
			int handlerTag, String id) {
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs = addUserInfoByPost(nameValuePairs);
		nameValuePairs.add(new BasicNameValuePair("id", id));
		String url = UrlHelper.getGetcrowdfunding();
		return doPost(nameValuePairs, url, handler, handlerTag, activity, url,
				false, null, null);
	}

	/**
	 * 获取商品流水号
	 */
	public boolean getOrderNumber(Activity activity, Handler handler,
			int handlerTag, String channel, String totalFee) {
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs = addUserInfoByPost(nameValuePairs);
		nameValuePairs.add(new BasicNameValuePair("channel", channel));
		nameValuePairs.add(new BasicNameValuePair("totalFee", totalFee));
		String url = UrlHelper.getGOODSNUMBER();
		return doPost(nameValuePairs, url, handler, handlerTag, activity, url,
				false, null, null);
	}

	/**
	 * 获取众筹评论列表
	 */
	public boolean getCrowdFundingComment(Activity activity, Handler handler,
			int handlerTag, String id) {
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs = addUserInfoByPost(nameValuePairs);
		nameValuePairs.add(new BasicNameValuePair("id", id));
		String url = UrlHelper.getGetcrowdfundingcomment();
		return doPost(nameValuePairs, url, handler, handlerTag, activity, url,
				false, null, null);
	}

	/**
	 * 商品收藏列表
	 * 
	 * @param activity
	 * @param handler
	 * @param handlerTag
	 * @param id
	 * @return
	 */
	public boolean getGoodsCollectionList(int pageNo, int pageSize,
			Handler handler, int handlerTag, Activity activity, String type) {
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs = addUserInfoByPost(nameValuePairs);
		nameValuePairs = addPageInfo(nameValuePairs, pageNo, pageSize);
		if (type != null) {
			nameValuePairs.add(new BasicNameValuePair("type", type));
		}
		String url = UrlHelper.getGOODSCOLLECTION();
		return doPost(nameValuePairs, url, handler, handlerTag, activity, url,
				false, null, null);
	}

	/**
	 * 发布评论
	 * 
	 */
	public boolean pubComment(Activity activity, Handler handler,
			int handlerTag, String id, String content, String toId,
			String toUserid) {
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs = addUserInfoByPost(nameValuePairs);
		nameValuePairs.add(new BasicNameValuePair("id", id));
		nameValuePairs.add(new BasicNameValuePair("content", content));
		if (toId != null && !toId.equals("-1")) {
			nameValuePairs.add(new BasicNameValuePair("toUserid", toUserid));
			nameValuePairs.add(new BasicNameValuePair("toId", toId));
		}
		String url = UrlHelper.getComment();
		return doPost(nameValuePairs, url, handler, handlerTag, activity, url,
				false, null, null);
	}

	/***
	 * 获取活动详情
	 * 
	 * @param activity
	 * @param handler
	 * @param handlerTag
	 * @param activityid
	 * @return
	 */
	public boolean getEventDetail(Activity activity, Handler handler,
			int handlerTag, String activityid) {
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs = addUserInfoByPost(nameValuePairs);
		nameValuePairs.add(new BasicNameValuePair("activityid", activityid));
		String url = UrlHelper.getGeteventdetail();
		return doPost(nameValuePairs, url, handler, handlerTag, activity, url,
				true, null, null);
	}

	/**
	 * 获取购物车信息
	 * 
	 * @param activity
	 * @param handler
	 * @param handlerTag
	 * @return
	 */
	public boolean getCartGoodsList(Activity activity, Handler handler,
			int handlerTag, int pageNo, int pageSize) {
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs = addUserInfoByPost(nameValuePairs);
		nameValuePairs = addPageInfo(nameValuePairs, pageNo, pageSize);
		String url = UrlHelper.getCARTGOODSLIST();
		return doPost(nameValuePairs, url, handler, handlerTag, activity, url,
				true, null, null);
	}

	/***
	 * 供需
	 * 
	 * @param activity
	 * @param handler
	 * @param handlerTag
	 * @param activityid
	 * @return
	 */
	public boolean getGongxuDetail(Activity activity, Handler handler,
			int handlerTag, String sdid) {
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs = addUserInfoByPost(nameValuePairs);
		nameValuePairs.add(new BasicNameValuePair("sdid", sdid));
		String url = UrlHelper.getGongxudetail();
		return doPost(nameValuePairs, url, handler, handlerTag, activity, url,
				true, null, null);
	}

	/**
	 * 获取商品详情
	 */
	public boolean getGoodsDetail(Activity activity, Handler handler,
			int handlerTag, String goodsid) {
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs = addUserInfoByPost(nameValuePairs);
		nameValuePairs.add(new BasicNameValuePair("goodsid", goodsid));
		String url = UrlHelper.getGOODSDETAIL();
		return doPost(nameValuePairs, url, handler, handlerTag, activity, url,
				true, null, null);
	}

	/**
	 * 分享到倬脉
	 * 
	 * @param activity
	 * @param handler
	 * @param handlerTag
	 * @param zhuoShareContent
	 * @return
	 */
	public boolean shareToZhuo(Activity activity, Handler handler,
			int handlerTag, ZhuoShareContent zhuoShareContent) {
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs = addUserInfoByPost(nameValuePairs);
		nameValuePairs.add(new BasicNameValuePair(zhuoShareContent.getIdName(),
				zhuoShareContent.getId()));
		String url = zhuoShareContent.getUrl();
		return doPost(nameValuePairs, url, handler, handlerTag, activity, url,
				false, null, null);
	}

	public boolean shareRESToZhuo(Activity activity, Handler handler,
			int handlerTag, String sdid) {
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs = addUserInfoByPost(nameValuePairs);
		nameValuePairs.add(new BasicNameValuePair("sdid", sdid));
		String url = UrlHelper.getSHARETOZHUO();
		return doPost(nameValuePairs, url, handler, handlerTag, activity, url,
				false, null, null);
	}

	/**
	 * 获取服务器 时间
	 */
	public boolean getTime(Activity activity, Handler handler, int handlerTag) {
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs = addUserInfoByPost(nameValuePairs);
		return doPost(nameValuePairs, UrlHelper.getAddgroupactivity(), handler,
				handlerTag, activity, null, false, null, null);
	}

	/**
	 * 获取商城订单
	 * 
	 * @param msgid
	 * @param iscollect
	 * @param handler
	 * @param handlerTag
	 * @param activity
	 * @param cancelable
	 * @param cancel
	 * @param data
	 * @return
	 */
	public boolean getOrderNumber(String invoice, String message,
			String totalZhuobi, String buyGoods, final Handler handler,
			int handlerTag, Activity activity) {
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs = addUserInfoByPost(nameValuePairs);
		nameValuePairs.add(new BasicNameValuePair("invoice", invoice));
		nameValuePairs.add(new BasicNameValuePair("message", message));
		nameValuePairs.add(new BasicNameValuePair("totalZhuobi", totalZhuobi));
		nameValuePairs.add(new BasicNameValuePair("buyGoods", buyGoods));
		return doPost(nameValuePairs, UrlHelper.getGENERATEORDER(), handler,
				handlerTag, activity, UrlHelper.getGENERATEORDER(), false,
				null, null);
	}

	public boolean shareThought(String url, String idName, String id,
			String text, Handler handler, int handlerTag, Activity activity,
			boolean cancelable, OnCancelListener cancel, String data) {
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair(idName, id));
		nameValuePairs.add(new BasicNameValuePair("comment", text));
		nameValuePairs = addUserInfoByPost(nameValuePairs);
		return doPost(nameValuePairs, url, handler, handlerTag, activity, url,
				cancelable, cancel, data);
	}

	public boolean recommandMsg(String msgid, String useridlist,
			Handler handler, int handlerTag, Activity activity,
			boolean cancelable, OnCancelListener cancel, String data) {
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("msgid", msgid));
		nameValuePairs.add(new BasicNameValuePair("useridlist", useridlist));
		return doPost(nameValuePairs, UrlHelper.getUrlRecommandMsg(), handler,
				handlerTag, activity, "recommandMsg", cancelable, cancel, data);
	}

}