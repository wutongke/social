package com.cpstudio.zhuojiaren.helper;

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
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import com.cpstudio.zhuojiaren.helper.AsyncConnectHelperLZ.FinishCallback;
import com.cpstudio.zhuojiaren.helper.AsyncUploadHelper.ICompleteCallback;
import com.qiniu.android.storage.UploadManager;

/**
 * �������
 * 
 * @author lef
 * 
 */
public class ZhuoConnHelper {
	private static ZhuoConnHelper instance;
	private String userid = null;
	private String password = null;
	private Handler uiHandler;
	// ��ʶÿһ����������ʼ���ظ�����
	private Set<String> mStartedTag = new HashSet<String>();

	UploadManager uploadManager;

	String session;
	String uploadFileToken;
	String imToken;

	private void init(Context context) {
		ResHelper resHelper = ResHelper.getInstance(context);
		this.userid = resHelper.getUserid();
		this.password = resHelper.getPassword();
		this.session = resHelper.getSessionForAPP();
		this.imToken = resHelper.getImTokenForRongyun();
		this.uploadFileToken = resHelper.getUpLoadTokenForQiniu();
		this.session = resHelper.getSessionForAPP();
		this.imToken = resHelper.getImTokenForRongyun();
		this.uploadFileToken = resHelper.getUpLoadTokenForQiniu();
	}

	public static ZhuoConnHelper getInstance(Context context) {
		if (null == instance) {
			instance = new ZhuoConnHelper();
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

	public boolean getFromServer(String url, Handler handler, int tag) {
		return getFromServer(url, handler, tag, null, false, null, null);
	}

	// lz0713
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
		return doPostByPost(pamNameValuePairs, url, handler, tag, activity,
				url, cancelable, cancel, data);
	}

	public boolean getFromServer(String url, FinishCallback callback) {
		return getFromServer(url, callback, null, false, null);
	}

	public boolean getFromServer(String url, FinishCallback callback,
			Activity activity, boolean cancelable, OnCancelListener cancel) {
		return doGet(url, callback, url, activity, cancelable, cancel);
	}

	// /////lz new 0729
	public boolean getMainAdInfo(Handler mUIHandler, int tag) {
		return getFromServerByPost(ZhuoCommHelperLz.getMainAdInfo(), null,
				mUIHandler, tag);
	}

	/**
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
			nameValuePairs.add(new BasicNameValuePair("uid", uid));
		nameValuePairs.add(new BasicNameValuePair("pageNo", "" + pageNo));
		nameValuePairs.add(new BasicNameValuePair("pageSize", "" + pageSize));
		return getFromServerByPost(ZhuoCommHelperLz.getQuanTopicList(),
				nameValuePairs, mUIHandler, tag);
	}

	public boolean pubQuanTopic(Activity activity, Handler mUIHandler, int tag,
			String groupid, String content, ArrayList<String> files) {
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("groupid", groupid));
		nameValuePairs.add(new BasicNameValuePair("content", content));
		Map<String, ArrayList<String>> fileMap = new HashMap<String, ArrayList<String>>();
		fileMap.put("file", files);
		return doFormPost(fileMap, nameValuePairs,
				ZhuoCommHelperLz.pubQuanTopic(), mUIHandler, tag, activity,
				"pubQuanTopic", false, null, null);
	}

	public boolean pubCmt(String msgid, String parentid, String content,
			String forward, Handler handler, int handlerTag, Activity activity,
			boolean cancelable, OnCancelListener cancel, String data) {
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("msgid", msgid));
		nameValuePairs.add(new BasicNameValuePair("parentid", parentid));
		nameValuePairs.add(new BasicNameValuePair("content", content));
		nameValuePairs.add(new BasicNameValuePair("forward", forward));
		return doPost(nameValuePairs, ZhuoCommHelper.getUrlCmt(), handler,
				handlerTag, activity, "pubCmt", cancelable, cancel, data);
	}

	public boolean goodMsg(String msgid, Handler handler, int handlerTag,
			Activity activity, boolean cancelable, OnCancelListener cancel,
			String data) {
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("msgid", msgid));
		return doPost(nameValuePairs, ZhuoCommHelper.getUrlGood(), handler,
				handlerTag, activity, "goodMsg", cancelable, cancel, data);
	}

	public boolean shareCloud(String msgid, String receivers,
			FinishCallback callback, Activity activity, boolean cancelable,
			OnCancelListener cancel) {
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("msgid", msgid));
		nameValuePairs.add(new BasicNameValuePair("receivers", receivers));
		return doPost(nameValuePairs, ZhuoCommHelper.getUrlShareCloud(),
				callback, activity, "shareCloud", cancelable, cancel);
	}

	public boolean applyForExch(String gid, String type, String myphone,
			String friphone, String addr, Handler handler, int handlerTag,
			Activity activity, boolean cancelable, OnCancelListener cancel,
			String data) {
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("gid", gid));
		nameValuePairs.add(new BasicNameValuePair("type", type));
		nameValuePairs.add(new BasicNameValuePair("myphone", myphone));
		nameValuePairs.add(new BasicNameValuePair("friphone", friphone));
		nameValuePairs.add(new BasicNameValuePair("addr", addr));
		return doPost(nameValuePairs, ZhuoCommHelper.getUrlApplyForExch(),
				handler, handlerTag, activity, "applyForExch", cancelable,
				cancel, data);
	}

	public boolean collectMsg(String msgid, String iscollect,
			final Handler handler, int handlerTag, Activity activity,
			boolean cancelable, OnCancelListener cancel, String data) {
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("msgid", msgid));
		nameValuePairs.add(new BasicNameValuePair("iscollect", iscollect));
		return doPost(nameValuePairs, ZhuoCommHelper.getUrlCollect(), handler,
				handlerTag, activity, "collectMsg", cancelable, cancel, data);
	}

	public boolean followUser(String uid, String type, Handler handler,
			int handlerTag, Activity activity, boolean cancelable,
			OnCancelListener cancel, String data) {
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("uid", uid));
		nameValuePairs.add(new BasicNameValuePair("type", type));
		return doPost(nameValuePairs, ZhuoCommHelper.getUrlFollow(), handler,
				handlerTag, activity, "followUser", cancelable, cancel, data);
	}

	public boolean followGroup(String groupid, String type, Handler handler,
			int handlerTag, Activity activity, boolean cancelable,
			OnCancelListener cancel, String data) {
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("groupid", groupid));
		nameValuePairs.add(new BasicNameValuePair("type", type));
		return doPost(nameValuePairs, ZhuoCommHelper.getUrlFollowGroup(),
				handler, handlerTag, activity, "followGroup", cancelable,
				cancel, data);
	}

	public boolean groupAlert(String groupid, String isalert, Handler handler,
			int handlerTag, Activity activity, boolean cancelable,
			OnCancelListener cancel, String data) {
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("groupid", groupid));
		nameValuePairs.add(new BasicNameValuePair("isalert", isalert));
		return doPost(nameValuePairs, ZhuoCommHelper.getUrlGroupAlert(),
				handler, handlerTag, activity, "groupAlert", cancelable,
				cancel, data);
	}

	public boolean groupRelease(String groupid, Handler handler,
			int handlerTag, Activity activity, boolean cancelable,
			OnCancelListener cancel, String data) {
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("groupid", groupid));
		return doPost(nameValuePairs, ZhuoCommHelper.getUrlGroupRelease(),
				handler, handlerTag, activity, "groupRelease", cancelable,
				cancel, data);
	}

	public boolean advice(String text, Handler handler, int handlerTag,
			Activity activity, boolean cancelable, OnCancelListener cancel,
			String data) {
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("content", text));
		return doPost(nameValuePairs, ZhuoCommHelper.getUrlAdvice(), handler,
				handlerTag, activity, "advice", cancelable, cancel, data);
	}

	public boolean black(String uid, String type, Handler handler,
			int handlerTag, Activity activity, boolean cancelable,
			OnCancelListener cancel, String data) {
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("uid", uid));
		nameValuePairs.add(new BasicNameValuePair("type", type));
		return doPost(nameValuePairs, ZhuoCommHelper.getUrlBlack(), handler,
				handlerTag, activity, "black", cancelable, cancel, data);
	}

	public boolean recommandMsg(String msgid, String useridlist,
			Handler handler, int handlerTag, Activity activity,
			boolean cancelable, OnCancelListener cancel, String data) {
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("msgid", msgid));
		nameValuePairs.add(new BasicNameValuePair("useridlist", useridlist));
		return doPost(nameValuePairs, ZhuoCommHelper.getUrlRecommandMsg(),
				handler, handlerTag, activity, "recommandMsg", cancelable,
				cancel, data);
	}

	public boolean recommandGroup(String groupid, String useridlist,
			Handler handler, int handlerTag, Activity activity,
			boolean cancelable, OnCancelListener cancel, String data) {
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("groupid", groupid));
		nameValuePairs.add(new BasicNameValuePair("useridlist", useridlist));
		return doPost(nameValuePairs, ZhuoCommHelper.getUrlRecommandGroup(),
				handler, handlerTag, activity, "recommandGroup", cancelable,
				cancel, data);
	}

	public boolean recommandUser(String uid, String useridlist,
			Handler handler, int handlerTag, Activity activity,
			boolean cancelable, OnCancelListener cancel, String data) {
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("uid", uid));
		nameValuePairs.add(new BasicNameValuePair("useridlist", useridlist));
		return doPost(nameValuePairs, ZhuoCommHelper.getUrlRecommandUser(),
				handler, handlerTag, activity, "recommandUser", cancelable,
				cancel, data);
	}

	public boolean prosecute(String uid, Handler handler, int handlerTag,
			Activity activity, boolean cancelable, OnCancelListener cancel,
			String data) {
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("uid", uid));
		return doPost(nameValuePairs, ZhuoCommHelper.getUrlProsecute(),
				handler, handlerTag, activity, "prosecute", cancelable, cancel,
				data);
	}

	public boolean sendCard(String uid, String leavemsg, Handler handler,
			int handlerTag, Activity activity, boolean cancelable,
			OnCancelListener cancel, String data) {
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("uid", uid));
		nameValuePairs.add(new BasicNameValuePair("leavemsg", leavemsg));
		return doPost(nameValuePairs, ZhuoCommHelper.getUrlSendCard(), handler,
				handlerTag, activity, "sendCard", cancelable, cancel, data);
	}

	public boolean plan(String text, Handler handler, int handlerTag,
			Activity activity, boolean cancelable, OnCancelListener cancel,
			String data) {
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("text", text));
		return doPost(nameValuePairs, ZhuoCommHelper.getUrlPlan(), handler,
				handlerTag, activity, "plan", cancelable, cancel, data);
	}

	public boolean delheaderimg(String id, Handler handler, int handlerTag,
			Activity activity, boolean cancelable, OnCancelListener cancel,
			String data) {
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("id", id));
		return doPost(nameValuePairs, ZhuoCommHelper.getUrlDelHeaderImg(),
				handler, handlerTag, activity, "delheaderimg", cancelable,
				cancel, data);
	}

	public boolean addProduct(String products, Handler handler, int handlerTag,
			Activity activity, boolean cancelable, OnCancelListener cancel,
			String data) {
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("products", products));
		return doPost(nameValuePairs, ZhuoCommHelper.getUrlAddProduct(),
				handler, handlerTag, activity, "addProduct", cancelable,
				cancel, data);
	}

	public boolean addDream(String dreams, Handler handler, int handlerTag,
			Activity activity, boolean cancelable, OnCancelListener cancel,
			String data) {
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("dreams", dreams));
		return doPost(nameValuePairs, ZhuoCommHelper.getUrlAddDream(), handler,
				handlerTag, activity, "addDream", cancelable, cancel, data);
	}

	public boolean updateUserDetail(Map<String, String> files, Handler handler,
			int handlerTag, Activity activity, String userid, String username,
			String imgcnt, String sex, String company, String post,
			String industry, String city, String hometown,
			String travel_cities, String birthday, String birthday_type,
			String constellation, String maxim, String hobby, String email,
			String learn_exp, String website, String ismarry, String isworking,
			String isphoneopen, String isisentrepreneurship,
			String isemailopen, String isbirthopen, String mycustomer,
			boolean cancelable, OnCancelListener cancel, String data) {
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("userid", userid));
		nameValuePairs.add(new BasicNameValuePair("username", username));
		nameValuePairs.add(new BasicNameValuePair("imgcnt", imgcnt));
		nameValuePairs.add(new BasicNameValuePair("sex", sex));
		nameValuePairs.add(new BasicNameValuePair("company", company));
		nameValuePairs.add(new BasicNameValuePair("post", post));
		nameValuePairs.add(new BasicNameValuePair("industry", industry));
		nameValuePairs.add(new BasicNameValuePair("city", city));
		nameValuePairs.add(new BasicNameValuePair("hometown", hometown));
		nameValuePairs.add(new BasicNameValuePair("travel_cities",
				travel_cities));
		nameValuePairs.add(new BasicNameValuePair("birthday", birthday));
		nameValuePairs.add(new BasicNameValuePair("birthday_type",
				birthday_type));
		nameValuePairs.add(new BasicNameValuePair("constellation",
				constellation));
		nameValuePairs.add(new BasicNameValuePair("maxim", maxim));
		nameValuePairs.add(new BasicNameValuePair("hobby", hobby));
		nameValuePairs.add(new BasicNameValuePair("email", email));
		nameValuePairs.add(new BasicNameValuePair("learn_exp", ""));
		nameValuePairs.add(new BasicNameValuePair("website", website));
		nameValuePairs.add(new BasicNameValuePair("ismarry", ismarry));
		nameValuePairs.add(new BasicNameValuePair("isworking", isworking));
		nameValuePairs.add(new BasicNameValuePair("isphoneopen", isphoneopen));
		nameValuePairs.add(new BasicNameValuePair("isisentrepreneurship",
				isisentrepreneurship));
		nameValuePairs.add(new BasicNameValuePair("isemailopen", isemailopen));
		nameValuePairs.add(new BasicNameValuePair("isbirthopen", isbirthopen));
		nameValuePairs.add(new BasicNameValuePair("mycustomer", mycustomer));

		// return doFormPost(files, addUserInfo(nameValuePairs),
		// ZhuoCommHelper.getUrlUpdateUserDetail(), handler, handlerTag,
		// activity, "updateUserDetail", cancelable, cancel, data);
		return doFormPost(new HashMap<String, ArrayList<String>>(),
				addUserInfo(nameValuePairs),
				ZhuoCommHelper.getUrlUpdateUserDetail(), handler, handlerTag,
				activity, "updateUserDetail", cancelable, cancel, data);
	}

	public boolean pubZhuoInfo(ArrayList<String> files, Handler handler,
			int handlerTag, Activity activity, String content, String tag,
			String mLocation, String imgCnt, String type, String category,
			String title, boolean cancelable, OnCancelListener cancel,
			String data) {
		return pubZhuoInfo(files, handler, handlerTag, activity, content, tag,
				mLocation, imgCnt, type, category, title, null, cancelable,
				cancel, data);
	}

	public boolean pubZhuoInfo(ArrayList<String> files, Handler handler,
			int handlerTag, Activity activity, String content, String tag,
			String mLocation, String imgCnt, String type, boolean cancelable,
			OnCancelListener cancel, String data) {
		return pubZhuoInfo(files, handler, handlerTag, activity, content, tag,
				mLocation, imgCnt, type, null, null, null, cancelable, cancel,
				data);
	}

	public boolean pubZhuoInfo(ArrayList<String> files, Handler handler,
			int handlerTag, Activity activity, String content, String tag,
			String mLocation, String imgCnt, String type, String category,
			String title, String groupid, boolean cancelable,
			OnCancelListener cancel, String data) {
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("text", content));
		nameValuePairs.add(new BasicNameValuePair("tags", tag.replaceAll(" ",
				";")));
		nameValuePairs.add(new BasicNameValuePair("position", mLocation));
		nameValuePairs.add(new BasicNameValuePair("imgcnt", imgCnt));
		nameValuePairs.add(new BasicNameValuePair("type", type));
		if (null != category) {
			nameValuePairs.add(new BasicNameValuePair("category", category));
		}
		if (null != title) {
			nameValuePairs.add(new BasicNameValuePair("title", title));
		}

		if (null != groupid) {
			nameValuePairs.add(new BasicNameValuePair("groupid", groupid));
		}
		return doFormPost(files, nameValuePairs,
				ZhuoCommHelper.getUrlPubinfo(), handler, handlerTag, activity,
				"pubZhuoInfo", cancelable, cancel, data);
	}

	public boolean createGroup(Map<String, String> files, Handler handler,
			int handlerTag, Activity activity, String gintro, String gproperty,
			String gname, String imgCnt, String groupid, String managers,
			boolean cancelable, OnCancelListener cancel, String data) {
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("gintro", gintro));
		nameValuePairs.add(new BasicNameValuePair("gproperty", gproperty));
		nameValuePairs.add(new BasicNameValuePair("gname", gname));
		nameValuePairs.add(new BasicNameValuePair("imgcnt", imgCnt));
		if (null != groupid) {
			nameValuePairs.add(new BasicNameValuePair("groupid", groupid));
		}
		if (null != managers) {
			nameValuePairs.add(new BasicNameValuePair("managers", managers));
		}
		// return doFormPost(files, nameValuePairs,
		// ZhuoCommHelper.getUrlCreateGroup(), handler, handlerTag,
		// activity, "createGroup", cancelable, cancel, data);
		return doFormPost(new HashMap<String, ArrayList<String>>(),
				nameValuePairs, ZhuoCommHelper.getUrlCreateGroup(), handler,
				handlerTag, activity, "createGroup", cancelable, cancel, data);
	}

	public boolean groupRemoveUser(Handler handler, int handlerTag,
			Activity activity, String groupid, String uid, boolean cancelable,
			OnCancelListener cancel, String data) {
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("groupid", groupid));
		nameValuePairs.add(new BasicNameValuePair("uid", uid));
		return doPost(nameValuePairs, ZhuoCommHelper.getUrlGroupRemoveUser(),
				handler, handlerTag, activity, "groupRemoveUser", cancelable,
				cancel, data);
	}

	public boolean acceptUser(Handler handler, int handlerTag,
			Activity activity, String groupid, String uid, String type,
			boolean cancelable, OnCancelListener cancel, String data) {
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("groupid", groupid));
		nameValuePairs.add(new BasicNameValuePair("uid", uid));
		nameValuePairs.add(new BasicNameValuePair("type", type));
		return doPost(nameValuePairs, ZhuoCommHelper.getUrlAcceptUser(),
				handler, handlerTag, activity, "acceptUser", cancelable,
				cancel, data);
	}

	public boolean frowardInfo(Handler handler, int handlerTag,
			Activity activity, String msgid, boolean cancelable,
			OnCancelListener cancel, String data) {
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("msgid", msgid));
		return doPost(nameValuePairs, ZhuoCommHelper.getUrlForwardInfo(),
				handler, handlerTag, activity, "frowardInfo", cancelable,
				cancel, data);
	}

	public boolean delResource(Handler handler, int handlerTag,
			Activity activity, String msgid, boolean cancelable,
			OnCancelListener cancel, String data) {
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("msgid", msgid));
		return doPost(nameValuePairs, ZhuoCommHelper.getUrlDelResource(),
				handler, handlerTag, activity, "delResource", cancelable,
				cancel, data);
	}

	public boolean updateConfig(Handler handler, int handlerTag,
			Activity activity, String isalert, boolean cancelable,
			OnCancelListener cancel, String data) {
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("isalert", isalert));
		return doPost(nameValuePairs, ZhuoCommHelper.getUrlUpdateConfig(),
				handler, handlerTag, activity, "updateConfig", cancelable,
				cancel, data);
	}

	public boolean msgRead(Handler handler, int handlerTag, Activity activity,
			String msgid, String type, boolean cancelable,
			OnCancelListener cancel, String data) {
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("msgid", msgid));
		nameValuePairs.add(new BasicNameValuePair("type", type));
		return doPost(nameValuePairs, ZhuoCommHelper.getUrlMsgRead(), handler,
				handlerTag, activity, null, cancelable, cancel, data);
	}

	public boolean androidName(Handler handler, int handlerTag,
			Activity activity, String info, boolean cancelable,
			OnCancelListener cancel, String data) {
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("info", info));
		if (info.equals("")) {
			instance = null;
		}
		return doPost(nameValuePairs, ZhuoCommHelper.getUrlAndroidName(),
				handler, handlerTag, activity, "androidName", cancelable,
				cancel, data);
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
		return doPost(addUserInfo(nameValuePairs),
				ZhuoCommHelper.getUrlLogin(), handler, handlerTag, activity,
				"login", cancelable, cancel, data);
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
		return doPost(nameValuePairs, ZhuoCommHelper.getUrlChangPwd(), handler,
				handlerTag, activity, "modifyPwd", cancelable, cancel, data);
	}

	public boolean groupChat(String filePath, final Handler handler,
			final int handlerTag, Activity activity, String content,
			String type, String groupid, String secs, boolean cancelable,
			OnCancelListener cancel, final String data) {
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("content", content));
		nameValuePairs.add(new BasicNameValuePair("groupid", groupid));
		nameValuePairs.add(new BasicNameValuePair("type", type));
		nameValuePairs.add(new BasicNameValuePair("from", "android"));
		nameValuePairs.add(new BasicNameValuePair("secs", secs));
		Map<String, ArrayList<String>> files = new HashMap<String, ArrayList<String>>();
		if (filePath != null && !filePath.equals("")) {
			ArrayList<String> list = new ArrayList<String>();
			list.add(filePath);
			files.put("file", list);
		}
		return doFormPost(files, nameValuePairs,
				ZhuoCommHelper.getUrlGroupChat(), handler, handlerTag,
				activity, null, cancelable, cancel, data);
	}

	public boolean chat(String filePath, final Handler handler,
			final int handlerTag, Activity activity, String content,
			String type, String otheruid, String secs, boolean cancelable,
			OnCancelListener cancel, String data) {
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("content", content));
		nameValuePairs.add(new BasicNameValuePair("otheruid", otheruid));
		nameValuePairs.add(new BasicNameValuePair("type", type));
		nameValuePairs.add(new BasicNameValuePair("from", "android"));
		nameValuePairs.add(new BasicNameValuePair("secs", secs));
		Map<String, ArrayList<String>> files = new HashMap<String, ArrayList<String>>();
		if (filePath != null && !filePath.equals("")) {
			ArrayList<String> list = new ArrayList<String>();
			list.add(filePath);
			files.put("file", list);

			// files.put("file", filePath);
			nameValuePairs.add(new BasicNameValuePair("file", filePath));
		}

		return doFormPost(files, nameValuePairs, ZhuoCommHelper.getUrlChat(),
				handler, handlerTag, activity, null, cancelable, cancel, data);
	}

	/**
	 * 
	 * @param nameValuePairs
	 *            ����
	 * @param url
	 * @param handler
	 * @param handlerTag
	 * @param activity
	 * @param tag
	 * @param cancelable
	 *            �Ƿ��ȡ��
	 * @param cancel
	 * @param data
	 * @return
	 */
	private boolean doPost(List<NameValuePair> nameValuePairs, String url,
			Handler handler, int handlerTag, Activity activity, String tag,
			boolean cancelable, OnCancelListener cancel, String data) {
		if (!mStartedTag.contains(tag) || tag == null) {
			if (tag != null) {
				mStartedTag.add(tag);
			}
			// ��ӵ����û��������룬�����ط�ֱ����addUserInfoByPost
			AsyncConnectHelperLZ conn = new AsyncConnectHelperLZ(
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

	// lz0713
	private boolean doPostByPost(List<NameValuePair> nameValuePairs,
			String url, Handler handler, int handlerTag, Activity activity,
			String tag, boolean cancelable, OnCancelListener cancel, String data) {
		if (!mStartedTag.contains(tag) || tag == null) {
			if (tag != null) {
				mStartedTag.add(tag);
			}
			AsyncConnectHelperLZ conn = new AsyncConnectHelperLZ(
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

	private boolean doPost(List<NameValuePair> nameValuePairs, String url,
			FinishCallback callback, Activity activity, String tag,
			boolean cancelable, OnCancelListener cancel) {
		if (!mStartedTag.contains(tag) || tag == null) {
			if (tag != null) {
				mStartedTag.add(tag);
			}
			AsyncConnectHelperLZ conn = new AsyncConnectHelperLZ(
					addUserInfo(nameValuePairs), url, getFinishCallback(
							callback, tag), activity);
			conn.setCancelable(cancelable);
			if (cancelable) {
				conn.setCancel(getCancelListener(cancel, tag, conn));
			}
			conn.execute();
			return true;
		}
		return false;
	}

	// private boolean doFormPost(Map<String, String> files,
	// List<NameValuePair> nameValuePairs, String url, Handler handler,
	// int handlerTag, Activity activity, String tag, boolean cancelable,
	// OnCancelListener cancel, String data) {
	//
	// ArrayList<String> pathList = new ArrayList<String>();
	// if (files != null)
	// for (Map.Entry<String, String> entry : files.entrySet()) {
	// // System.out.println("key= " + entry.getKey() + " and value= "
	// // + entry.getValue());
	// pathList.add(entry.getValue());
	// }
	//
	// return doFormPost(pathList, nameValuePairs, url, handler, handlerTag,
	// activity, tag, cancelable, cancel, data);
	//
	// }

	/**
	 * ֮�����,��Ҫ��
	 */
	public boolean doFormPost(ArrayList<String> files,
			final List<NameValuePair> nameValuePairs, final String url,
			final Handler handler, final int handlerTag,
			final Activity activity, final String tag,
			final boolean cancelable, final OnCancelListener cancel,
			final String data) {

		if (!mStartedTag.contains(tag) || tag == null) {
			if (tag != null) {
				mStartedTag.add(tag);
			}
			AsyncUploadHelper helper = new AsyncUploadHelper(
					activity, uploadFileToken, null,
					new ICompleteCallback() {

						@Override
						public void onReturn(Map<String, StringBuilder> map) {
							// TODO Auto-generated method stub
							if(map==null)
								Toast.makeText(activity, "�ϴ�����ţ��ʧ��", 1000).show();
							else if (map.size() > 0) {
								for (Map.Entry<String, StringBuilder> entry : map
										.entrySet()) {
									String key = entry.getKey();
									String value = entry.getValue().toString();
									nameValuePairs.add(new BasicNameValuePair(
											key, value));
								}
								AsyncConnectHelperLZ conn = new AsyncConnectHelperLZ(
										addUserInfoByPost(nameValuePairs), url,
										true, getFinishCallback(handler,
												handlerTag, tag, data),
										activity);
								conn.setCancelable(cancelable);
								if (cancelable) {
									conn.setCancel(getCancelListener(cancel,
											tag, conn));
								}
								conn.execute();
							}
						}
					});
			helper.execute("test");
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
	public boolean doFormPost(final Map<String, ArrayList<String>> filesMap,
			final List<NameValuePair> nameValuePairs, final String url,
			final Handler handler, final int handlerTag,
			final Activity activity, final String tag,
			final boolean cancelable, final OnCancelListener cancel,
			final String data) {

		if (!mStartedTag.contains(tag) || tag == null) {
			if (tag != null) {
				mStartedTag.add(tag);
			}

			AsyncUploadHelper helper = new AsyncUploadHelper(
					activity, uploadFileToken, filesMap,
					new ICompleteCallback() {

						@Override
						public void onReturn(Map<String, StringBuilder> map) {
							// TODO Auto-generated method stub
							if(map==null)
								Toast.makeText(activity, "�ϴ�����ţ��ʧ��", 1000).show();
							else if (map.size() > 0) {
								for (Map.Entry<String, StringBuilder> entry : map
										.entrySet()) {
									String key = entry.getKey();
									String value = entry.getValue().toString();
									nameValuePairs.add(new BasicNameValuePair(
											key, value));
								}
								AsyncConnectHelperLZ conn = new AsyncConnectHelperLZ(
										addUserInfoByPost(nameValuePairs), url,
										true, getFinishCallback(handler,
												handlerTag, tag, data),
										activity);
								conn.setCancelable(cancelable);
								if (cancelable) {
									conn.setCancel(getCancelListener(cancel,
											tag, conn));
								}
								conn.execute();
							}
						}
					});
			helper.execute("test");
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
			AsyncConnectHelperLZ conn = new AsyncConnectHelperLZ(
					addUserInfo(url), getFinishCallback(handler, handlerTag,
							tag, data), activity);
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
			AsyncConnectHelperLZ conn = new AsyncConnectHelperLZ(
					addUserInfo(url), getFinishCallback(callback, tag),
					activity);
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
			final String tag, final AsyncConnectHelperLZ conn) {
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
			return params + "&username=" + userid + "&password=" + password;
		} else {
			return params + "?username=" + userid + "&password=" + password;
		}
	}

	private List<NameValuePair> addUserInfo(List<NameValuePair> nameValuePairs) {
		nameValuePairs.add(new BasicNameValuePair("username", userid));
		nameValuePairs.add(new BasicNameValuePair("password", password));
		return nameValuePairs;
	}

	// lz0713
	private List<NameValuePair> addUserInfoByPost(
			List<NameValuePair> nameValuePairs) {
		// nameValuePairs.add(new BasicNameValuePair("session",
		// "e72d664f93de40e7aa08b28f15444f5b"));
		nameValuePairs.add(new BasicNameValuePair("session", session));
		nameValuePairs.add(new BasicNameValuePair("apptype", "0"));
		return nameValuePairs;
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
}