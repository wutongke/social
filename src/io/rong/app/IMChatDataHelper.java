package io.rong.app;

import io.rong.app.database.UserInfos;
import io.rong.imkit.RongIM;
import io.rong.imlib.model.Group;
import io.rong.imlib.model.UserInfo;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;

import com.cpstudio.zhuojiaren.facade.GroupFacade;
import com.cpstudio.zhuojiaren.facade.UserFacade;
import com.cpstudio.zhuojiaren.model.QuanVO;
import com.cpstudio.zhuojiaren.model.UserNewVO;

/**
 * 聊天相关的缓存信息帮助类
 * @author lz
 *
 */
public class IMChatDataHelper {

	private static IMChatDataHelper mDemoContext;
	public Context mContext;
	private ArrayList<UserInfo> mUserInfos;
	private SharedPreferences mPreferences;
	private RongIM.LocationProvider.LocationCallback mLastLocationCallback;
	//用户信息缓存类
	private UserFacade mUserInfosDao;
	//群组信息缓存类
	private GroupFacade mGroupInfoDao;

	public UserFacade getmUserInfosDao() {
		return mUserInfosDao;
	}

	public void setmUserInfosDao(UserFacade mUserInfosDao) {
		this.mUserInfosDao = mUserInfosDao;
	}

	public synchronized static IMChatDataHelper getInstance(Context context) {

		if (mDemoContext == null) {
			mDemoContext = new IMChatDataHelper(context);
		}
		return mDemoContext;
	}

	private IMChatDataHelper(Context context) {
		mContext = context;
		mDemoContext = this;
		// http初始化 用于登录、注册使用
		mPreferences = PreferenceManager.getDefaultSharedPreferences(context);

		RongIM.setLocationProvider(new LocationProvider());

		mUserInfosDao = new UserFacade(mContext.getApplicationContext());
		mGroupInfoDao = new GroupFacade(mContext.getApplicationContext());
	}

	public GroupFacade getmGroupInfoDao() {
		return mGroupInfoDao;
	}

	public static void init(Context context) {
		mDemoContext = new IMChatDataHelper(context);
	}

	public SharedPreferences getSharedPreferences() {
		return mPreferences;
	}

	public void setSharedPreferences(SharedPreferences sharedPreferences) {
		this.mPreferences = sharedPreferences;
	}

	public ArrayList<UserInfo> getUserInfos() {
		return mUserInfos;
	}

	public void setUserInfos(ArrayList<UserInfo> userInfos) {
		mUserInfos = userInfos;
	}


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

		UserNewVO user = mUserInfosDao.getById(targetid);
		mUserInfosDao.update(user);

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

		if (mUserInfosDao == null)
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
	public Group getGroupInfoById(String groupid) {

		if (mGroupInfoDao == null)
			return null;
		QuanVO quan = mGroupInfoDao.getSimpleInfoById(groupid);
		if (quan == null)
			return null;

		return new Group(quan.getGroupid(), quan.getGname(), Uri.parse(quan
				.getGheader()));
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
