package com.cpstudio.zhuojiaren.helper;

import io.rong.app.model.ApiResult;
import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient.ErrorCode;
import io.rong.imlib.RongIMClient.SendMessageCallback;
import io.rong.imlib.model.Conversation.ConversationType;
import io.rong.imlib.model.Group;
import io.rong.message.TextMessage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InvalidClassException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Type;
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
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.SurfaceView;
import android.widget.Toast;

import com.cpstudio.zhuojiaren.helper.AsyncConnectHelperLZ.FinishCallback;
import com.cpstudio.zhuojiaren.helper.AsyncUploadHelper.ICompleteCallback;
import com.cpstudio.zhuojiaren.model.BaseCodeData;
import com.cpstudio.zhuojiaren.model.City;
import com.cpstudio.zhuojiaren.model.Dynamic;
import com.cpstudio.zhuojiaren.model.Province;
import com.cpstudio.zhuojiaren.model.UserNewVO;
import com.cpstudui.zhuojiaren.lz.CustomerMessageFactory;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.qiniu.android.storage.UploadManager;

/**
 * �������
 * 
 * @author lef
 * 
 */
public class ZhuoConnHelper {

	public enum EditMODE {
		VIEW, EDIT, ADD, DELETE
	};

	public static final String BASEDATA = "baseCodeDatas";
	public static final String CITYS = "citys";
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
	Context context;

	private BaseCodeData baseDataSet;
	private List<Province> citysOfProvince;
	private List<City> citys;
	private HashMap<String, Group> groupMap;// Ⱥ����Ϣ

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
		this.context = context;
	}

	public static ZhuoConnHelper getInstance(Context mcontext) {
		if (null == instance) {
			instance = new ZhuoConnHelper();
		}
		if (instance.password == null || instance.password.equals("")) {
			instance.init(mcontext);
		}
		return instance;
	}

	public HashMap<String, Group> getGroupMap() {
		return groupMap;
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

	public void setCitysOfPrince(List<Province> citysOfPrince) {
		this.citysOfProvince = citysOfPrince;
		citys = new ArrayList<City>();
		for (Province temp : citysOfProvince) {
			citys.addAll(temp.getCitys());
		}
	}

	public BaseCodeData getBaseDataSet() {
		if (baseDataSet == null) {
			String data = AppClientLef.getInstance(context).readObject(
					"BaseSetData");
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

	// public boolean addProduct(String products, Handler handler, int
	// handlerTag,
	// Activity activity, boolean cancelable, OnCancelListener cancel,
	// String data) {
	// List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
	// nameValuePairs.add(new BasicNameValuePair("products", products));
	// return doPost(nameValuePairs, ZhuoCommHelper.getUrlAddProduct(),
	// handler, handlerTag, activity, "addProduct", cancelable,
	// cancel, data);
	// }

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
		return doPostWithFile(new HashMap<String, ArrayList<String>>(),
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
		return doPostWithFile(new HashMap<String, ArrayList<String>>(),
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
		return doPostWithFile(files, nameValuePairs,
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

		return doPostWithFile(files, nameValuePairs,
				ZhuoCommHelper.getUrlChat(), handler, handlerTag, activity,
				null, cancelable, cancel, data);
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
		if (instance == null) {
			instance = getInstance(activity);
		}
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
			AsyncUploadHelper helper = new AsyncUploadHelper(activity,
					uploadFileToken, null, new ICompleteCallback() {

						@Override
						public void onReturn(Map<String, StringBuilder> map) {
							// TODO Auto-generated method stub
							if (map == null)
								Toast.makeText(activity, "�ϴ�����ţ��ʧ��", 1000)
										.show();
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
								if (map == null)
									Toast.makeText(activity, "�ϴ�����ţ��ʧ��", 1000)
											.show();
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
						});
				helper.execute("test");
			} else {
				nameValuePairs.add(new BasicNameValuePair("file", extra));
				AsyncConnectHelperLZ conn = new AsyncConnectHelperLZ(
						addUserInfoByPost(nameValuePairs), url, true,
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

		return getFromServerByPost(ZhuoCommHelperLz.getMainInfo(), params,
				mUIHandler, tag);
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

		return getFromServerByPost(ZhuoCommHelperLz.deleteActives(), params,
				mUIHandler, tag);
	}

	/**
	 * ��ȡ�����Ϣ����δ����
	 * 
	 * @param mUIHandler
	 * @param tag
	 * @param type ������ 0-��ҳ 1-���� 2-���� 3-�̳� 4-�ڳ�
	 * @return
	 */
	public boolean getAdInfo(Handler mUIHandler, int tag, int type) {
		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("type",String.valueOf(type)));
		return getFromServerByPost(ZhuoCommHelperLz.getAdInfo(), params,
				mUIHandler, tag);
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
		return getFromServerByPost(ZhuoCommHelperLz.getQuanTopicList(),
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
				ZhuoCommHelperLz.pubQuanTopic(), mUIHandler, tag, activity,
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
		return doPostWithFile(fileMap, nameValuePairs,
				ZhuoCommHelperLz.pubAdvice(), mUIHandler, tag, activity,
				"pubAdvice", false, null, null);
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
				ZhuoCommHelperLz.pubQuanTopic(), mUIHandler, tag, activity,
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
		return doPostWithFile(fileMap, nameValuePairs,
				ZhuoCommHelperLz.pubPhoto(), mUIHandler, tag, activity,
				"pubQuanTopic", false, null, null, originFilekeys);
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
		return getFromServerByPost(ZhuoCommHelperLz.getQuanEventList(),
				nameValuePairs, mUIHandler, tag);
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
		return getFromServerByPost(ZhuoCommHelperLz.getUserEvent(),
				nameValuePairs, mUIHandler, tag);
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
		return getFromServerByPost(ZhuoCommHelperLz.getQuanInfo(),
				nameValuePairs, mUIHandler, tag);
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
		return getFromServerByPost(ZhuoCommHelperLz.manageQuanPermit(),
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
		return getFromServerByPost(ZhuoCommHelperLz.modifyGroupInfo(),
				nameValuePairs, mUIHandler, handlerTag);
	}
	/**
	 * ����Ȧ��ͷ��
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
		return getFromServerByPost(ZhuoCommHelperLz.setQuanLogo(),
				nameValuePairs, mUIHandler, handlerTag);
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
		return getFromServerByPost(ZhuoCommHelperLz.getTopicDetail(),
				nameValuePairs, mUIHandler, handlerTag);
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
		return getFromServerByPost(ZhuoCommHelperLz.topicPraise(),
				nameValuePairs, mUIHandler, handlerTag);
	}
	

	public boolean praiseDynamic(Handler mUIHandler, int handlerTag,
			String statusid, int praise) {
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("statusid", statusid));
		nameValuePairs.add(new BasicNameValuePair("praise", praise + ""));
		return getFromServerByPost(ZhuoCommHelperLz.dynamicPraise(),
				nameValuePairs, mUIHandler, handlerTag);
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
		return getFromServerByPost(ZhuoCommHelperLz.zanCard(), nameValuePairs,
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
		return getFromServerByPost(ZhuoCommHelperLz.topicComment(),
				nameValuePairs, mUIHandler, handlerTag);
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
		return getFromServerByPost(ZhuoCommHelperLz.dynamicComment(),
				nameValuePairs, mUIHandler, handlerTag);
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
		return getFromServerByPost(ZhuoCommHelperLz.getUserInfo(),
				nameValuePairs, mUIHandler, handlerTag);
	}

	public boolean getGonggaoDetail(Handler mUIHandler, int handlerTag,
			String id) {
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		if (id != null)
			nameValuePairs.add(new BasicNameValuePair("id", id));
		return getFromServerByPost(ZhuoCommHelperLz.gonggaoDetail(),
				nameValuePairs, mUIHandler, handlerTag);
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
		if(user.getFaith()!=null)
			nameValuePairs
			.add(new BasicNameValuePair("faith", user.getFaith()));
		return getFromServerByPost(ZhuoCommHelperLz.modifyUserInfo(),
				nameValuePairs, mUIHandler, handlerTag);
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
				ZhuoCommHelperLz.setUserHeadImage(), mUIHandler, tag, activity,
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
		return getFromServerByPost(ZhuoCommHelperLz.getDetailDynamic(),
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
		return getFromServerByPost(ZhuoCommHelperLz.getDynamicList(),
				nameValuePairs, mUIHandler, handlerTag);
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
		return getFromServerByPost(ZhuoCommHelperLz.orderList(),
				nameValuePairs, mUIHandler, handlerTag);
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
		return getFromServerByPost(ZhuoCommHelperLz.orderDetail(),
				nameValuePairs, mUIHandler, handlerTag);
	}

	public boolean getCardBg(Handler mUIHandler, int handlerTag, int version) {
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("version", String
				.valueOf(version)));
		return getFromServerByPost(ZhuoCommHelperLz.cardBg(), nameValuePairs,
				mUIHandler, handlerTag);
	}

	public boolean setCardBg(Handler mUIHandler, int handlerTag, int bgid) {
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs
				.add(new BasicNameValuePair("bgid", String.valueOf(bgid)));
		return getFromServerByPost(ZhuoCommHelperLz.setCardBg(),
				nameValuePairs, mUIHandler, handlerTag);
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
		return doPostWithFile(fileMap, nameValuePairs,
				ZhuoCommHelperLz.pubDynamic(), mUIHandler, handlerTag,
				activity, "pubDynamic", false, null, null);
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
		return doPostWithFile(fileMap, nameValuePairs,
				ZhuoCommHelperLz.pubGongxu(), mUIHandler, handlerTag, activity,
				"pubDynamic", false, null, null);
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
		return getFromServerByPost(ZhuoCommHelperLz.getDetailDynamic(),
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
		return getFromServerByPost(ZhuoCommHelperLz.collectStatusFamily(),
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
		return getFromServerByPost(ZhuoCommHelperLz.collectTopic(),
				nameValuePairs, mUIHandler, handlerTag);
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
		return getFromServerByPost(ZhuoCommHelperLz.getGroupMemberList(),
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
		return getFromServerByPost(ZhuoCommHelperLz.getPutList(),
				nameValuePairs, mUIHandler, tag);
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
		return getFromServerByPost(ZhuoCommHelperLz.groupStatus(),
				nameValuePairs, mUIHandler, tag);
	}

	public boolean getFollowReqList(Handler mUIHandler, int tag) {
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		return getFromServerByPost(ZhuoCommHelperLz.getFollowReqList(),
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
		return getFromServerByPost(ZhuoCommHelperLz.getMyStatusCard(),
				nameValuePairs, mUIHandler, tag);
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
		return getFromServerByPost(ZhuoCommHelperLz.myRenmai(), nameValuePairs,
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
		return getFromServerByPost(ZhuoCommHelperLz.followUser(),
				nameValuePairs, mUIHandler, tag);
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
		return getFromServerByPost(ZhuoCommHelperLz.makeFriends(),
				nameValuePairs, mUIHandler, tag);
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
		return getFromServerByPost(ZhuoCommHelperLz.getReqQuanUsers(),
				nameValuePairs, mUIHandler, tag);
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
		return getFromServerByPost(ZhuoCommHelperLz.getUrlMyGroupList(),
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
		return getFromServerByPost(ZhuoCommHelperLz.getUserBusinessInfo(),
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
		String URLS[] = { ZhuoCommHelperLz.getCityJiaren(),
				ZhuoCommHelperLz.getIndustryJiaren(),
				ZhuoCommHelperLz.getNearJiaren(),
				ZhuoCommHelperLz.getHobbyJiaren(),
				ZhuoCommHelperLz.getTeatureJiaren(),
				ZhuoCommHelperLz.getAllJiaren() };
		String keys[] = { "city", "industry", "near(����)", "hobby", "tchtype",
				"xx" };
		if (type < 1 || type > URLS.length)
			return false;
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs = addPageInfo(nameValuePairs, pageNo, pageSize);
		if (extraStr != null && !extraStr.equals(""))
			nameValuePairs
					.add(new BasicNameValuePair(keys[type - 1], extraStr));
		return getFromServerByPost(ZhuoCommHelperLz.SERVER + URLS[type - 1],
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
		return getFromServerByPost(ZhuoCommHelperLz.getFriendReq(),
				nameValuePairs, mUIHandler, tag);
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
		return getFromServerByPost(ZhuoCommHelperLz.getProduct(),
				nameValuePairs, mUIHandler, tag);
	}

	public boolean getZMDTCount(Handler mUIHandler, int tag, String userid) {
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		if (userid != null)
			nameValuePairs.add(new BasicNameValuePair("userid", userid));
		return getFromServerByPost(ZhuoCommHelperLz.getZMDT(), nameValuePairs,
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
		return getFromServerByPost(ZhuoCommHelperLz.getCompany(),
				nameValuePairs, mUIHandler, tag);
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
		return getFromServerByPost(ZhuoCommHelperLz.deleteProduct(),
				nameValuePairs, mUIHandler, tag);
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
		return getFromServerByPost(ZhuoCommHelperLz.deleteCompany(),
				nameValuePairs, mUIHandler, tag);
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
				ZhuoCommHelperLz.updateProduct(), mUIHandler, tag, activity,
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
		return doPostWithFile(fileMap, nameValuePairs,
				ZhuoCommHelperLz.addProduct(), mUIHandler, tag, activity,
				"addProduct", false, null, null, originFilekeys);
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
		return getFromServerByPost(ZhuoCommHelperLz.updateCompany(),
				nameValuePairs, mUIHandler, tag);
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
		return getFromServerByPost(ZhuoCommHelperLz.getHotKey(),
				nameValuePairs, mUIHandler, tag);
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
		return getFromServerByPost(ZhuoCommHelperLz.getPortalSearch(),
				nameValuePairs, mUIHandler, tag);
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

		return getFromServerByPost(ZhuoCommHelperLz.getMyFriends(),
				nameValuePairs, mUIHandler, tag);
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
		return getFromServerByPost(ZhuoCommHelperLz.addCompany(),
				nameValuePairs, mUIHandler, tag);
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
		return getFromServerByPost(ZhuoCommHelperLz.cmtGX(), nameValuePairs,
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
		String res = readObject(BASEDATA);
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
			String url = ZhuoCommHelperLz.getBaseCodeData();
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
			String url = ZhuoCommHelper.getServiceCityList();
			return doPost(nameValuePairs, url, handler, handlerTag, activity,
					url, cancelable, cancel, data);
		}
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
		String url = ZhuoCommHelper.getGongxulist();
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
		String url = ZhuoCommHelper.getDELETEGONGXU();
		// String url = ZhuoCommHelper.getDisolveQuan();
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
		TextMessage reqMsg = null, quitMsg = null;
		String pushReqMs = "", pushQuitMs = "";
		if (type == 1) {// ����Ⱥ
			groupHashMap.put(group.getId(), group);
			// reqMsg = CustomerMessageFactory.getInstance().getReqQuanMsg(
			// getUserid(), "XX", group.getId(), group.getName());
			// pushReqMs = getUserid() + "�������Ȧ�ӣ�" + group.getName() + "(+"
			// + group.getId() + ")";
		} else if (type == 0) {
			groupHashMap.remove(group.getId());
			// quitMsg=
		}
		setGroupMap(groupHashMap);

		// if (RongIM.getInstance().getRongIMClient() == null)
		// return;
		// if (getUserid() == null)
		// return;
		// ios ��ʱû��
		// RongIM.getInstance()
		// .getRongIMClient()
		// .sendMessage(ConversationType.PRIVATE, targetId, reqMsg,
		// pushReqMs, new SendMessageCallback() {
		// @Override
		// public void onSuccess(Integer arg0) {
		// // TODO Auto-generated method stub
		// Toast.makeText(context, "���͵��Է��ɹ�", 1000).show();
		//
		// }
		//
		// @Override
		// public void onError(Integer arg0, ErrorCode arg1) {
		// // TODO Auto-generated method stub
		// Toast.makeText(context,
		// "���뷢��ʧ��ErrorCode��" + arg1, 1000).show();
		// }
		// });
	}

}