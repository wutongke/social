package io.rong.app;

import io.rong.app.database.UserInfos;
import io.rong.app.database.UserInfosDao;
import io.rong.imkit.RongIM;
import io.rong.imlib.model.Group;
import io.rong.imlib.model.UserInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.text.TextUtils;

import com.cpstudio.zhuojiaren.facade.UserFacade;
import com.cpstudio.zhuojiaren.model.UserNewVO;

/**
 * Created by Bob on 2015/1/30.
 */
public class DemoContext {

	private static DemoContext mDemoContext;
	public Context mContext;
	// private DemoApi mDemoApi;
	private HashMap<String, Group> groupMap;
	private ArrayList<UserInfo> mUserInfos;
	private ArrayList<UserInfo> mFriendInfos;
	private SharedPreferences mPreferences;
	private RongIM.LocationProvider.LocationCallback mLastLocationCallback;
	// private UserInfosDao mUserInfosDao;
	private UserFacade mUserInfosDao;

	public UserFacade getmUserInfosDao() {
		return mUserInfosDao;
	}

	public void setmUserInfosDao(UserFacade mUserInfosDao) {
		this.mUserInfosDao = mUserInfosDao;
	}

	public static DemoContext getInstance(Context context) {

		if (mDemoContext == null) {
			mDemoContext = new DemoContext(context);
		}
		return mDemoContext;
	}

	private DemoContext(Context context) {
		mContext = context;
		mDemoContext = this;
		// http初始化 用于登录、注册使用
		mPreferences = PreferenceManager.getDefaultSharedPreferences(context);

		RongIM.setLocationProvider(new LocationProvider());

		// mDemoApi = new DemoApi(context);

		// mUserInfosDao =
		// DBManager.getInstance(mContext).getDaoSession().getUserInfosDao();
		mUserInfosDao = new UserFacade(mContext.getApplicationContext());
	}

	public static void init(Context context) {
		mDemoContext = new DemoContext(context);
	}

	public SharedPreferences getSharedPreferences() {
		return mPreferences;
	}

	public void setSharedPreferences(SharedPreferences sharedPreferences) {
		this.mPreferences = sharedPreferences;
	}

	/**
	 * 登陆成功后应该返回好友列表，圈子列表
	 * 
	 * @param groupMap
	 */
	public void setGroupMap(HashMap<String, Group> groupMap) {
		this.groupMap = groupMap;
	}

	public HashMap<String, Group> getGroupMap() {
		return groupMap;
	}

	public ArrayList<UserInfo> getUserInfos() {
		return mUserInfos;
	}

	public void setUserInfos(ArrayList<UserInfo> userInfos) {
		mUserInfos = userInfos;
	}

	// /**
	// * 临时存放用户数据
	// *
	// * @param userInfos
	// */
	// public void setFriends(ArrayList<UserInfo> userInfos) {
	//
	// this.mFriendInfos = userInfos;
	// }
	//
	// public ArrayList<UserInfo> getFriends() {
	// return mFriendInfos;
	// }

	// public DemoApi getDemoApi() {
	// return mDemoApi;
	// }

	/**
	 * 删除 userinfos 表
	 */
	public void deleteUserInfos() {

		mUserInfosDao.deleteAll();
	}

	/**
	 * 更新 好友信息,如删除好友关系
	 * 
	 * @param targetid
	 * @param status
	 */
	public void updateUserInfos(String targetid, String status) {

		// UserInfos userInfos = mUserInfosDao.queryBuilder()
		// .where(UserInfosDao.Properties.Userid.eq(targetid)).unique();
		// userInfos.setStatus(status);
		// userInfos.setUsername(userInfos.getUsername());
		// userInfos.setPortrait(userInfos.getPortrait());
		// userInfos.setUserid(userInfos.getUserid());
		UserNewVO user = mUserInfosDao.getById(targetid);
		// UserNewVO 需要增加一个字段，表示与我的关系
		// user.setu
		mUserInfosDao.update(user);

	}

	/**
	 * 向数据库插入数据
	 * 
	 * @param info
	 *            用户信息
	 * @param status
	 *            状态
	 */
	public void insertOrReplaceUserInfo(UserInfo info, String status) {

		UserNewVO user = new UserNewVO();
		// user.setBirthday(birthday)
		// UserInfos userInfos = new UserInfos();
		// userInfos.setStatus(status);
		// userInfos.setUsername(info.getName());
		// userInfos.setPortrait(String.valueOf(info.getPortraitUri()));
		// userInfos.setUserid(info.getUserId());
		mUserInfosDao.saveOrUpdate(user);
	}

	public void insertOrReplaceUserInfoList(ArrayList<UserInfo> list,
			String status) {

		List<UserInfos> userInfos = new ArrayList();

	}

	/**
	 * 通过userid 查找 UserInfo，查找的是本地的数据库
	 * 
	 * @param userId
	 * @return
	 */
	public UserInfo getUserInfoById(String userId) {

		if(mUserInfosDao==null)
			return null;
		UserNewVO user = mUserInfosDao.getSimpleInfoById(userId);
		if (user == null)
			return null;

		return new UserInfo(user.getUserid(), user.getName(), Uri.parse(user
				.getUheader()));
	}

	/**
	 * 获得好友列表
	 * 
	 * @return
	 */
	public ArrayList<UserInfo> getFriendList() {
		List<UserInfo> userInfoList = new ArrayList();

		// 不应该是全部的，应该是所有的好友
		List<UserNewVO> userInfos = mUserInfosDao.getAll();

		if (userInfos == null)
			return null;

		for (int i = 0; i < userInfos.size(); i++) {
			UserInfo userInfo = new UserInfo(userInfos.get(i).getUserid(),
					userInfos.get(i).getName(), Uri.parse(userInfos.get(i)
							.getUheader()));

			userInfoList.add(userInfo);
		}
		return (ArrayList) userInfoList;
	}

	/**
	 * 根据userids获得好友列表
	 * 
	 * @return
	 */
	public ArrayList<UserInfo> getUserInfoList(String[] userIds) {

		List<UserInfo> userInfoList = new ArrayList();
		List<UserNewVO> userInfosList = new ArrayList();

		for (int i = 0; i < userIds.length; i++) {
			UserNewVO userInfos = mUserInfosDao.getSimpleInfoById(userIds[i]);
			userInfosList.add(userInfos);
			UserInfo userInfo = new UserInfo(userInfosList.get(i).getUserid(),
					userInfosList.get(i).getName(), Uri.parse(userInfosList
							.get(i).getUheader()));
			userInfoList.add(userInfo);
		}

		if (userInfosList == null)
			return null;

		return (ArrayList) userInfoList;
	}

	/**
	 * 通过groupid 获得groupname
	 * 
	 * @param groupid
	 * @return
	 */
	public String getGroupNameById(String groupid) {
		Group groupReturn = null;
		if (!TextUtils.isEmpty(groupid) && groupMap != null) {

			if (groupMap.containsKey(groupid)) {
				groupReturn = groupMap.get(groupid);
			} else
				return null;

		}
		if (groupReturn != null)
			return groupReturn.getName();
		else
			return null;
	}

	public RongIM.LocationProvider.LocationCallback getLastLocationCallback() {
		return mLastLocationCallback;
	}

	public void setLastLocationCallback(
			RongIM.LocationProvider.LocationCallback lastLocationCallback) {
		this.mLastLocationCallback = lastLocationCallback;
	}

	class LocationProvider implements RongIM.LocationProvider {

		/**
		 * 位置信息提供者:LocationProvider 的回调方法，打开第三方地图页面。
		 * 
		 * @param context
		 *            上下文
		 * @param callback
		 *            回调
		 */
		@Override
		public void onStartLocation(Context context,
				RongIM.LocationProvider.LocationCallback callback) {
			/**
			 * demo 代码 开发者需替换成自己的代码。
			 */
			// DemoContext.getInstance().setLastLocationCallback(callback);
			// Intent intent = new Intent(context, SOSOLocationActivity.class);
			// intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			// context.startActivity(intent);// SOSO地图
		}
	}

}
