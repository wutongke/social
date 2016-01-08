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
 * �������󹤾��࣬����
 * 
 * @author lz
 * 
 */
public class ConnHelper {

	public enum EditMODE {
		VIEW, EDIT, ADD, DELETE
	};

	// SharedPreferences��������key
	public static final String BASEDATA = "baseCodeDatas";// ������������
	public static final String GXTYPES = "gongxuTypes";// ��������
	public static final String CITYS = "citys";// ���б�ŵ�

	private static ConnHelper instance;
	private String userid = null;
	private String password = null;

	String session;// Ӧ�÷�����token
	String uploadFileToken;// ��ţ���ϴ�ͼƬtoken
	String imToken; // ��������token
	Context context;

	// ��ʶÿһ����������ʼ���ظ�����
	private Set<String> mStartedTag = new HashSet<String>();

	private BaseCodeData baseDataSet;
	private GXTypeCodeData gxTypeCodeDataSet;
	private List<Province> citysOfProvince;
	private List<City> citys;
	// Ⱥ����Ϣ
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
			// ��ӵ����û��������룬�����ط�ֱ����addUserInfoByPost
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
	 * ��Ҫ�ϴ��ļ�ʱ���ô˽ӿ�
	 * 
	 * @param files
	 *            ���ϴ��ļ���ַ�б�
	 * @param nameValuePairs
	 *            ����
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
	 *            ��extra��Ϊnullʱ����extra��������ӵ�filesMap��
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
									Toast.makeText(activity, "�ϴ�����ţ��ʧ��", 1000)
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
	 * ����ҳ��Ϣ
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
	 * heep Get���������������
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

	// ��½
	public boolean login(String userid, String password, Handler handler,
			int handlerTag, Activity activity, boolean cancelable,
			OnCancelListener cancel, String data) {
		this.userid = userid;
		this.password = password;
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("from", "android"));
		// ��½ʱ��Ҫ��Ӷ�����˺ź�������Ϣ
		return doPost(addUserInfo(nameValuePairs), UrlHelper.getUrlLogin(),
				handler, handlerTag, activity, "login", cancelable, cancel,
				data);
	}

	/**
	 * ��ȡ��֤��
	 * 
	 * @param code
	 *            �绰��
	 * @param code
	 *            �绰��
	 * @param handler
	 * @param handlerTag
	 *            �ص�ʱ���msg.what
	 * @param handlerTag
	 *            �ص�ʱ���msg.what
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
	 * ��ȡ��ҳ�����Ϣ
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
	 * ɾ���
	 * 
	 * @param mUIHandler
	 * @param tag
	 * @param activityid
	 *            Ȧ�ID �����Ϊ������Զ��ŷָ���
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
	 * ��ȡ�����Ϣ����δ����
	 * 
	 * @param mUIHandler
	 * @param tag
	 * @param type
	 *            ������ 0-��ҳ 1-���� 2-���� 3-�̳� 4-�ڳ�
	 * @return
	 */
	public boolean getAdInfo(Handler mUIHandler, int tag, int type) {
		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("type", String.valueOf(type)));
		return getFromServerByPost(UrlHelper.getAdInfo(), params, mUIHandler,
				tag);
	}

	/**
	 * ��ȡȦ�����б�
	 * 
	 * @param context
	 * @param groupid
	 * @param uid
	 *            uidΪnull���ȡ��Ȧ�ӵ�Ȧ���⣻�����ȡ���û��ڴ�Ȧ�ӵ�Ȧ��
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
	 * ����Ȧ����
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
	 * �����������
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
	 * �����������
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
	 * ���¸�����Ƭ
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
	 * ���Ȧ�ӻ�б�
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
	 * ��ȡ�û�����ʹ����Ļ
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
	 * ���Ȧ����Ϣ(Ȧ����ҳ)
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
	 * ������˳�Ȧ��
	 * 
	 * @param mUIHandler
	 * @param tag
	 * @param groupid
	 * @param type
	 *            0ȡ����ע 1�����ע 2���ܹ�ע
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
	 * �޸�Ȧ����Ϣ
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
	 * ����Ȧ��ͷ��
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
	 * ��ȡȦ��������
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
	 *            0:ȡ���� 1:��
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
	 * ����Ƭ
	 * 
	 * @param mUIHandler
	 * @param handlerTag
	 * @param statusid
	 * @param praise
	 *            //0:ȡ���� 1:��
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
	 * Ȧ��������
	 * 
	 * @param mUIHandler
	 * @param handlerTag
	 * @param topicid
	 * @param comment
	 * @param toId
	 *            ��ѡ Ҫ�ظ�������ID (����������ͨ����)
	 * @param toUserid
	 *            ��ѡ Ҫ�ظ����û�ID (����������ͨ����)
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
	 * ��ø�����Ϣ
	 * 
	 * @param mUIHandler
	 * @param handlerTag
	 * @param userid
	 *            (��ѡ) Ҫ�鿴�ĸ��û�����Ϣ��������鿴�Լ�����Ϣ
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
	 * �޸��û���Ϣ
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
	 * �����û�ͷ��
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
	 * ��ö�̬����
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
	 * ��ö����б�
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
	 * ��ö�������
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
	 * ������̬
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
	 * ���跢��
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
	 * ɾ����̬
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
	 * �ղض�̬
	 * 
	 * @param mUIHandler
	 * @param handlerTag
	 * @param statusid
	 * @param type
	 *            1-�����ղ� 0-ȡ���ղ�
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
	 * �ղ�Ȧ����
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
	 * ���Ȧ�ӳ�Ա�б�
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
	 * ���پ����̬
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
	 * ������ص�Ȧ�Ӷ�̬������ȫ�����Ȧ�
	 * 
	 * @param mUIHandler
	 * @param tag
	 * @param type
	 *            ���� 0-ȫ��Ȧ�� 1-�Ҵ�����Ȧ�� 2-�Ҽ����Ȧ��
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
	 * ��ȡ�ޡ�������ղع�����Ƭ���û��б�
	 * 
	 * @param mUIHandler
	 * @param tag
	 *            0-���������Ƭ���� 1-�ղع�����Ƭ���� 2-�޹�����Ƭ����
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
	 * ����ҵ�����
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
	 *            �Է��û�ID ��Ҫ��ע�ġ�Ҫȡ����ע�ġ�Ҫ���ܵģ�
	 * @param type
	 *            0ȡ����ע(ȡ���ղ�) 1��ע(�ղ�)
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
	 * ������Ӻ���=������Ƭ�����ܺ�������=������Ƭ�� ����Է�������Ƭ�������󣨽��ܺ�������������Ϊ���ѹ�ϵ��
	 * ע�⣺���ѹ�ϵ��Ӧ�÷�������ά����ʹ�����Ʒ���ContactNotificationMessage��ϵ�˺���֪ͨ��Ϣ��
	 * �����ô˽ӿ�֪ͨӦ�÷�����������Ӧ�ĺ��ѹ�ϵ���� ���̣� 1.����������Ӻ��ѽӿ�(type=1)����Ӧ�÷�����������Ӧ�Ĺ�����
	 * 2.ͨ�����Ƶĺ������֪ͨ��Է�����һ��֪ͨ�� 3.�Է�����ܣ����ý��ܺ�����ӽӿ�(type=2)��
	 * 
	 * @param mUIHandler
	 * @param tag
	 * @param userid
	 * @param type
	 *            0ɾ������ 1������Ӻ���(������Ƭ) 2�����������(������Ƭ)
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
	 * ��ȡ�������Ȧ�ӵ���
	 * 
	 * @param mUIHandler
	 * @param tag
	 * @param groupid
	 *            Ϊnull���ʾ�����������Ȧ�ӵ���
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
	 * ��ȡȦ����Ϣ��Ϊ�����ṩ
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
	 * �û�������Ϣ������
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
	 * �����ͬ�������û���ͬ�ǣ�ͬȤ��
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
		String keys[] = { "city", "industry", "near(����)", "hobby", "tchtype",
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
	 * ������󽻻���Ƭ�ļ���
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
	 * ��ù�˾��Ʒ
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
	 * ��ȡ��˾��Ϣ
	 * 
	 * @param mUIHandler
	 * @param tag
	 * @param userid
	 *            (��ѡ)�������ȡ�ҵĹ�˾��Ϣ�������ȡָ���û��Ĺ�˾��Ϣ��
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
	 * ɾ����Ʒ��Ϣ
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
	 * ɾ����˾��Ϣ
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
	 *            ��˾ID ,����������ѡ
	 * @param company
	 * @param industry
	 * @param city
	 * @param position
	 * @param homepage
	 * @param status
	 *            (��ѡ)�Ƿ�������˾ 1-����˾ 0-��ͨ��˾
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
	 * ��ȡ�����ȴ�
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
	 * ���ݹؼ��ʼ����û��� �ؼ��ʿ���Ϊ���֣���˾����ְλ��
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
	 * ��ȡ�ҵĺ����б�
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
	 * ���ۺͻظ�����
	 * 
	 * @param mUIHandler
	 * @param tag
	 * @param sdid
	 *            ����ID
	 * @param comment
	 * @param toId
	 *            ��ѡ Ҫ�ظ�������ID (����������ͨ����)
	 * @param toUserid
	 *            ��ѡ Ҫ�ظ����û�ID (����������ͨ����)
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
	 * ��û����������ݣ�id��ֵ�Ķ�Ӧ
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
 * ��ù��������б�
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
	 * ��ȡ�����б�,lef
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
	 *            (��ѡ) �����ʶ 0-��Դ 1-����
	 * @param type
	 *            (��ѡ) �������� ����������ָ������ɸѡ��Ϣ
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
	 * ɾ������
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
	 * �������
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
	 * ��ȡ����,lef
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
			// �����л�ʧ�� - ɾ�������ļ�
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
	 * �жϻ����Ƿ����
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
	 * ����Ⱥ����Ϣ�ṩ�ߣ���������Ϣ��Ⱥ��
	 * 
	 * @param context
	 * @param targetId
	 *            �Է�ID
	 * @param group
	 * @param type
	 *            0,�˳���1 ����
	 */
	public void followQuan(final Context context, String targetId, Group group,
			int type) {
		HashMap<String, Group> groupHashMap = getGroupMap();
		// reqMsg��ʵ�֣� quitMsg��Ҫ�����Զ���
		if (type == 1) {// ����Ⱥ
			groupHashMap.put(group.getId(), group);
		} else if (type == 0) {
			groupHashMap.remove(group.getId());
		}
		setGroupMap(groupHashMap);
	}

	// appclient
	/**
	 * ˢ��session
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
							// ��ȡsession
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

		// ���浽����������
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
	 * ��ȡȦ��,����url�������
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
	 * ��ȡ��Ʒ����
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
	 * ��پ�Ҹ�����
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
	 * ��ɢȦ��
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
	 * �˳�Ȧ��
	 */
	public boolean quitQuan(String groupId, String type, String content,
			Handler handler, int handlerTag, Activity activity,
			boolean cancelable, OnCancelListener cancel, String data) {
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs = addUserInfoByPost(nameValuePairs);
		// type int 0ȡ����ע 1�����ע 2���ܹ�ע
		nameValuePairs.add(new BasicNameValuePair("type", type));
		nameValuePairs.add(new BasicNameValuePair("groupid", groupId));
		nameValuePairs.add(new BasicNameValuePair("content", content));
		String url = UrlHelper.getQuitQuan();
		return doPost(nameValuePairs, url, handler, handlerTag, activity, url,
				cancelable, cancel, data);
	}

	/**
	 * ��ȡ��Ƶ����
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
	 * ��ȡ��Ƶ�����ղ�
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
	 * �ύ��Ƶͳ��
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
	 * ��ȡ��Ƶ����
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
	 * ��ȡ��Ƶ����
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
	 * ��ȡ��Ʒ�б� ���� ���� ˵�� categoryid �� ��Ʒ���id����ѡ�� keyword �� ������Ʒ�ؼ��֣���ѡ�� providerid
	 * �� ��Ӧ��id����ѡ�� �����ϲ�������������ȡ�̳���ҳĬ����Ʒ�� pageNo �� ҳ�� pageSize �� ��ʾ��
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
	 * ��ȡ�ڳ�
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
	 * ��ȡ�ڳ�Ͷ��
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
	 * ��ȡ��ϸ
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
	 * �Ƴ����ﳵgoods
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
	 * ��ȡ�ɷ�
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
	 * ��ȡ�ɳ���������
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
	 * �ղ�
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
	 * ��ȡ��پ��
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
	 * ��پ��֧��
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
	 * ֧��״̬
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
	 * �����ջ���ַ
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
	 * ������չ
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
	 * ��ȡ��չ�б�
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
	 * �ر�
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
	 * ����Ȧ��
	 */
	// session string(32) �˺�session ��½����
	// gname string(32) Ȧ������
	// gheader string(200) Ȧ��logoͼƬ��(��ţ��key)
	// gintro string(150) Ȧ�Ӽ��
	// gpub string(150) Ȧ�ӹ���
	// gtype int Ȧ����� 0:δ֪��1:��Դ����Ȧ��2:����Ͷ��Ȧ��3:��Ȥ����Ȧ��4:�����ɳ�Ȧ��5:ͬ�Ǽ���Ȧ��6:����Ȧ
	// city int ���� ���б���
	// followpms int ����Ȩ�� 0:�����κ��˼��룬1:��Ҫ������ܼ���
	// accesspms int ����Ȩ�� 0:�����˶����Է��ʣ�1:����Ȧ�Ӳſ��Է���
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
	 * �����
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
	 * �˳��
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
								Toast.makeText(activity, "ͼƬ�ϴ�ʧ�ܣ������µ�¼�ύ",
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
			CommonUtil.displayToast(activity, "��Ҫ�ظ��ύ");
		}
		return false;
	}

	/**
	 * �����ڳ�
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
	 * ��ȡ�ڳ�����
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
	 * ��ȡ��Ʒ��ˮ��
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
	 * ��ȡ�ڳ������б�
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
	 * ��Ʒ�ղ��б�
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
	 * ��������
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
	 * ��ȡ�����
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
	 * ��ȡ���ﳵ��Ϣ
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
	 * ����
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
	 * ��ȡ��Ʒ����
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
	 * ����پ��
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
	 * ��ȡ������ ʱ��
	 */
	public boolean getTime(Activity activity, Handler handler, int handlerTag) {
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs = addUserInfoByPost(nameValuePairs);
		return doPost(nameValuePairs, UrlHelper.getAddgroupactivity(), handler,
				handlerTag, activity, null, false, null, null);
	}

	/**
	 * ��ȡ�̳Ƕ���
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