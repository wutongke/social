package com.cpstudio.zhuojiaren.helper;

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
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.cpstudio.zhuojiaren.helper.AsyncConnectHelper.FinishCallback;
import com.cpstudio.zhuojiaren.model.LoginRes;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UpProgressHandler;
import com.qiniu.android.storage.UploadManager;
import com.qiniu.android.storage.UploadOptions;

public class AppClientLef {
	private String userId;
	private String password;
	String session;
	String uploadFileToken;
	String imToken;
	private static AppClientLef instance;
	private Context context;

	public static AppClientLef getInstance(Context context) {
		if (null == instance) {
			instance = new AppClientLef();
		}
		if (instance.password == null || instance.password.equals("")) {
			instance.init(context);
		}
		return instance;
	}

	private void init(Context context) {
		ResHelper resHelper = ResHelper.getInstance(context);
		this.userId = resHelper.getUserid();
		this.password = resHelper.getPassword();
		this.session = resHelper.getSessionForAPP();
		this.imToken = resHelper.getImTokenForRongyun();
		this.uploadFileToken = resHelper.getUpLoadTokenForQiniu();
		this.context = context;
	}

	/**
	 * 刷新session
	 * 
	 * @param context
	 */
	public void refreshSession(final Context context) {
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		AsyncConnectHelperLZ conn = new AsyncConnectHelperLZ(
				addUserInfo(nameValuePairs), ZhuoCommHelper.getUrlLogin(),
				new AsyncConnectHelperLZ.FinishCallback() {

					@Override
					public boolean onReturn(String rs, int responseCode) {
						// TODO Auto-generated method stub
						if (JsonHandler.checkResult(rs,
								context.getApplicationContext())) {
							// 获取session
							LoginRes res = JsonHandler_Lef.parseLoginRes(
									context, JsonHandler.parseResult(rs)
											.getData());
							SharedPreferences sp = context
									.getSharedPreferences("cpzhuojiaren",
											Activity.MODE_PRIVATE);
							SharedPreferences.Editor editor = sp.edit();
							editor.putString(ResHelper.SESSION,
									res.getSession());
							editor.putString(ResHelper.UPLIOAD_TOKEN,
									res.getQiniuToken());
							editor.putString(ResHelper.IM_TOKEN,
									res.getRongyunToken());
							editor.commit();

							// 保存到两个单例中
							ResHelper mResHelper = ResHelper
									.getInstance(context);
							mResHelper.setSessionForAPP(res.getSession());
							mResHelper.setUpLoadTokenForQiniu(res
									.getQiniuToken());
							mResHelper.setImTokenForRongyun(res
									.getRongyunToken());
							ZhuoConnHelper connHelper = ZhuoConnHelper
									.getInstance(context);
							connHelper.setSession(res.getSession());
							connHelper.setUploadFileToken(res.getQiniuToken());
							connHelper.setImToken(res.getRongyunToken());
						}
						return false;
					}
				}, null);
		conn.execute();
	}
	
	/**
	 * 获取圈子,根据url区分类别
	 */
	public boolean getQuanzi(String url,String gtype,String city,int pageNo,int pageSize, Handler handler,
			int handlerTag, Activity activity, boolean cancelable,
			OnCancelListener cancel, String data) {
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs = addPageInfo(nameValuePairs, pageNo, pageSize);
		nameValuePairs = addUserInfoByPost(nameValuePairs);
		if(gtype!=null)
			nameValuePairs.add(new BasicNameValuePair("gtype",gtype));
		if(city!=null)
			nameValuePairs.add(new BasicNameValuePair("city", city));
		return doPost(nameValuePairs, url,
				handler, handlerTag, activity, url, cancelable,
				cancel, data);
	}
	/**
	 * 增加页信息
	 * @param nameValuePairs
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	private List<NameValuePair> addPageInfo(List<NameValuePair> nameValuePairs,int pageNo,int pageSize){
		nameValuePairs.add(new BasicNameValuePair("pageNo",String.valueOf(pageNo)));
		nameValuePairs.add(new BasicNameValuePair("pageSize",String.valueOf(pageSize)));
		return nameValuePairs;
	}
	/***
	 * 解散圈子
	 */
	public boolean disolveQuan(String groupId,String content,Handler handler,
			int handlerTag, Activity activity, boolean cancelable,
			OnCancelListener cancel, String data) {
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs = addUserInfoByPost(nameValuePairs);
		nameValuePairs.add(new BasicNameValuePair("groupid", groupId));
		nameValuePairs.add(new BasicNameValuePair("content", content));
		String url = ZhuoCommHelper.getDisolveQuan();
		return doPost(nameValuePairs, url,
				handler, handlerTag, activity, url, cancelable,
				cancel, data);
	}
	
	/**
	 * 退出圈子
	 */
	public boolean quitQuan(String groupId,String type,String content,Handler handler,
			int handlerTag, Activity activity, boolean cancelable,
			OnCancelListener cancel, String data) {
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs = addUserInfoByPost(nameValuePairs);
		//type		int				0取消关注  1申请关注  2接受关注
		nameValuePairs.add(new BasicNameValuePair("type", type));
		nameValuePairs.add(new BasicNameValuePair("groupid", groupId));
		nameValuePairs.add(new BasicNameValuePair("content", content));
		String url = ZhuoCommHelper.getQuitQuan();
		return doPost(nameValuePairs, url,
				handler, handlerTag, activity, url, cancelable,
				cancel, data);
	}
	/**
	 * 获取城市列表
	 */
	public boolean getCitys(Handler handler,
			int handlerTag, Activity activity, boolean cancelable,
			OnCancelListener cancel, String data) {
		String res = readObject("citys");
		if(res!=null){
			Message msg = handler.obtainMessage(handlerTag);
			Bundle bundle = new Bundle();
			bundle.putString("data", data);
			msg.setData(bundle);
			msg.obj = res;
			msg.sendToTarget();
			return true;
		}else{
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs = addUserInfoByPost(nameValuePairs);
			nameValuePairs.add(new BasicNameValuePair("version", "0"));
			String url = ZhuoCommHelper.getServiceCityList();
			return doPost(nameValuePairs, url,
					handler, handlerTag, activity, url, cancelable,
					cancel, data);
		}
	}
	/**
	 * 获取视频在线
	 */
	public boolean getVedioList(String tutorId,String typeId ,int pageNo,int pageSize, Handler handler,
			int handlerTag, Activity activity, boolean cancelable,
			OnCancelListener cancel, String data) {
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs = addUserInfoByPost(nameValuePairs);
		nameValuePairs = addPageInfo(nameValuePairs, pageNo, pageSize);
		if(tutorId!=null)
		nameValuePairs.add(new BasicNameValuePair("tutorId", tutorId));
		if(typeId!=null)
		nameValuePairs.add(new BasicNameValuePair("typeId", typeId));
		String url = ZhuoCommHelper.getServiceVedioList();
		return doPost(nameValuePairs, url,
				handler, handlerTag, activity, url, cancelable,
				cancel, data);
	}
	/**
	 * 提交视频统计
	 */
	public boolean submitVedio(String Id) {
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("onlineid", Id));
		nameValuePairs = addUserInfoByPost(nameValuePairs);
		String url = ZhuoCommHelper.getSubmitVisit();
		return doPost(nameValuePairs, url,
				null, 0, null, url, false,
				null, null);
	}
	/**
	 * 获取音频在线
	 */
	public boolean getAudioList(int pageNo,int pageSize, Handler handler,
			int handlerTag, Activity activity, boolean cancelable,
			OnCancelListener cancel, String data) {
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs = addUserInfoByPost(nameValuePairs);
		nameValuePairs = addPageInfo(nameValuePairs, pageNo, pageSize);
		String url = ZhuoCommHelper.getServiceAudioList();
		return doPost(nameValuePairs, url,
				handler, handlerTag, activity, url, cancelable,
				cancel, data);
	}
	/**
	 * 获取众筹
	 */
	public boolean getFundingList(int pageNo,int pageSize, Handler handler,
			int handlerTag, Activity activity, boolean cancelable,
			OnCancelListener cancel, String data) {
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs = addUserInfoByPost(nameValuePairs);
		nameValuePairs = addPageInfo(nameValuePairs, pageNo, pageSize);
		String url = ZhuoCommHelper.getServiceFundingList();
		return doPost(nameValuePairs, url,
				handler, handlerTag, activity, url, cancelable,
				cancel, data);
	}
	/**
	 * 获取采访
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
	public boolean getVisiteList(int pageNo,int pageSize, Handler handler,
			int handlerTag, Activity activity, boolean cancelable,
			OnCancelListener cancel, String data) {
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs = addUserInfoByPost(nameValuePairs);
		nameValuePairs = addPageInfo(nameValuePairs, pageNo, pageSize);
		String url = ZhuoCommHelper.getServiceVisitList();
		return doPost(nameValuePairs, url,
				handler, handlerTag, activity, url, cancelable,
				cancel, data);
	}
	/**
	 * @param handler
	 * @param handlerTag
	 * @param activity
	 * @param cancelable
	 * @param cancel
	 * @param data
	 * @return
	 */
	public boolean getGrowthOnlineType(Handler handler,
			int handlerTag, Activity activity) {
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs = addUserInfoByPost(nameValuePairs);
		String url = ZhuoCommHelper.getGrowthonlinetype();
		return doPost(nameValuePairs, url,
				handler, handlerTag, activity, url, false,
				null, null);
	}
	/**
	 * 
	 * @param handler
	 * @param handlerTag
	 * @param activity
	 * @return
	 */
	public boolean getTeacherList(Handler handler,
			int handlerTag, Activity activity) {
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs = addUserInfoByPost(nameValuePairs);
		String url = ZhuoCommHelper.getServiceTeacherList();
		return doPost(nameValuePairs, url,
				handler, handlerTag, activity, url, false,
				null, null);
	}
	/**
	 * 收藏
	 */
	public boolean collection(String url,String idKey,String id,String stateKey,String state){
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs = addUserInfoByPost(nameValuePairs);
		nameValuePairs.add(new BasicNameValuePair(idKey, id));
		nameValuePairs.add(new BasicNameValuePair(stateKey, state));
		return doPost(nameValuePairs, url,
				null, 0, null, url, false,
				null, null);
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
	 * 读取对象
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
			return  (String) ois.readObject();
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
		File data = context.getFileStreamPath(cachefile);
		if (data.exists())
			exist = true;
		return exist;
	}
	/**
	 * 
	 */
	// 原接口
	// 标识每一次请求，请求开始后不重复请求
	UploadManager uploadManager;
	private Set<String> mStartedTag = new HashSet<String>();

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
		return doFormPost(files, addUserInfo(nameValuePairs),
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
		return doFormPost(files, nameValuePairs,
				ZhuoCommHelper.getUrlCreateGroup(), handler, handlerTag,
				activity, "createGroup", cancelable, cancel, data);
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

	/**
	 * 获取验证码
	 * 
	 * @param code
	 *            电话号
	 * @param handler
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
		Map<String, String> files = new HashMap<String, String>();
		if (filePath != null && !filePath.equals("")) {
			files.put("file", filePath);
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
		Map<String, String> files = new HashMap<String, String>();
		if (filePath != null && !filePath.equals("")) {
			files.put("file", filePath);
		}
		return doFormPost(files, nameValuePairs, ZhuoCommHelper.getUrlChat(),
				handler, handlerTag, activity, null, cancelable, cancel, data);
	}

	/**
	 * 
	 * @param nameValuePairs
	 *            参数
	 * @param url
	 * @param handler
	 * @param handlerTag
	 * @param activity
	 * @param tag
	 * @param cancelable
	 *            是否可取消
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
			AsyncConnectHelper conn = new AsyncConnectHelper(
					nameValuePairs, url, getFinishCallback(
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
			AsyncConnectHelper conn = new AsyncConnectHelper(
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

	private boolean doFormPost(Map<String, String> files,
			List<NameValuePair> nameValuePairs, String url, Handler handler,
			int handlerTag, Activity activity, String tag, boolean cancelable,
			OnCancelListener cancel, String data) {
		if (!mStartedTag.contains(tag) || tag == null) {
			if (tag != null) {
				mStartedTag.add(tag);
			}
			AsyncConnectHelper conn = new AsyncConnectHelper(files,
					nameValuePairs, addUserInfo(url), true, getFinishCallback(
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

	private boolean doFormPost(ArrayList<String> files,
			List<NameValuePair> nameValuePairs, String url, Handler handler,
			int handlerTag, Activity activity, String tag, boolean cancelable,
			OnCancelListener cancel, String data) {
		if (!mStartedTag.contains(tag) || tag == null) {
			if (tag != null) {
				mStartedTag.add(tag);
			}
			// 文件上传以七牛云的方式上传
			// AsyncConnectHelper conn = new AsyncConnectHelper(files,
			// nameValuePairs, addUserInfo(url), true, getFinishCallback(
			// handler, handlerTag, tag, data), activity);
			// conn.setCancelable(cancelable);
			// if (cancelable) {
			// conn.setCancel(getCancelListener(cancel, tag, conn));
			// }
			// conn.execute();

			// AsyncConnectHelper conn = new AsyncConnectHelper("",
			// nameValuePairs, addUserInfo(url), true, getFinishCallback(
			// handler, handlerTag, tag, data), activity);
			// conn.setCancelable(cancelable);
			// if (cancelable) {
			// conn.setCancel(getCancelListener(cancel, tag, conn));
			// }
			// conn.execute();

			qiniuUpLoadFiles(files);

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
			return params + "&username=" + userId + "&password=" + password;
		} else {
			return params + "?username=" + userId + "&password=" + password;
		}
	}

	private List<NameValuePair> addUserInfo(List<NameValuePair> nameValuePairs) {
		nameValuePairs.add(new BasicNameValuePair("username", userId));
		nameValuePairs.add(new BasicNameValuePair("password", password));
		return nameValuePairs;
	}

	// lz0713
	private List<NameValuePair> addUserInfoByPost(
			List<NameValuePair> nameValuePairs) {
		nameValuePairs.add(new BasicNameValuePair("session", session));
		nameValuePairs.add(new BasicNameValuePair("apptype", "0"));
		return nameValuePairs;
	}

	// 七牛文件上传
	private void qiniuUpLoadFiles(final ArrayList<String> files) {
		final String token = "gqyn9mD9OEVHoayK16ivmeCMcUgLNxVnxIjcrGCm:xFUsFZVKJLh7YnZXI5fTLf1-rNU=:eyJzY29wZSI6InpodW90ZXN0IiwiZGVhZGxpbmUiOjE0Mzc4Mjg3NjN9";

		new Thread(new Runnable() {
			@Override
			public void run() {
				if (uploadManager == null)
					uploadManager = new UploadManager();
				for (String path : files) {
					uploadManager.put(path, null, token,
							new UpCompletionHandler() {

								@Override
								public void complete(String key,
										ResponseInfo arg1, JSONObject arg2) {
									// TODO Auto-generated method stub
									Log.i("qiniu", key + ": " + arg2);
								}

							}, new UploadOptions(null, null, false,
									new UpProgressHandler() {
										public void progress(String key,
												double percent) {
										}
									}, null));
				}
			}
		}).start();

	}
	// 元接口

}
