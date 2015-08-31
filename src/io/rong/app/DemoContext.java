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

import com.cpstudio.zhuojiaren.facade.GroupFacade;
import com.cpstudio.zhuojiaren.facade.UserFacade;
import com.cpstudio.zhuojiaren.model.QuanVO;
import com.cpstudio.zhuojiaren.model.UserNewVO;

/**
 * Created by Bob on 2015/1/30.
 */
public class DemoContext {

	private static DemoContext mDemoContext;
	public Context mContext;
	private ArrayList<UserInfo> mUserInfos;
	private ArrayList<UserInfo> mFriendInfos;
	private SharedPreferences mPreferences;
	private RongIM.LocationProvider.LocationCallback mLastLocationCallback;
	// private UserInfosDao mUserInfosDao;
	private UserFacade mUserInfosDao;
	private GroupFacade mGroupInfoDao;

	public UserFacade getmUserInfosDao() {
		return mUserInfosDao;
	}

	public void setmUserInfosDao(UserFacade mUserInfosDao) {
		this.mUserInfosDao = mUserInfosDao;
	}

	public synchronized static DemoContext getInstance(Context context) {

		if (mDemoContext == null) {
			mDemoContext = new DemoContext(context);
		}
		return mDemoContext;
	}

	private DemoContext(Context context) {
		mContext = context;
		mDemoContext = this;
		// http��ʼ�� ���ڵ�¼��ע��ʹ��
		mPreferences = PreferenceManager.getDefaultSharedPreferences(context);

		RongIM.setLocationProvider(new LocationProvider());

		mUserInfosDao = new UserFacade(mContext.getApplicationContext());
		mGroupInfoDao = new GroupFacade(mContext.getApplicationContext());
	}

	public GroupFacade getmGroupInfoDao() {
		return mGroupInfoDao;
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

	public ArrayList<UserInfo> getUserInfos() {
		return mUserInfos;
	}

	public void setUserInfos(ArrayList<UserInfo> userInfos) {
		mUserInfos = userInfos;
	}

	// /**
	// * ��ʱ����û�����
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
	 * ɾ�� userinfos ��
	 */
	public void deleteUserInfos() {

		mUserInfosDao.deleteAll();
	}

	/**
	 * ���� ������Ϣ,��ɾ�����ѹ�ϵ
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
		// UserNewVO ��Ҫ����һ���ֶΣ���ʾ���ҵĹ�ϵ
		// user.setu
		mUserInfosDao.update(user);

	}

	/**
	 * �����ݿ��������
	 * 
	 * @param info
	 *            �û���Ϣ
	 * @param status
	 *            ״̬
	 */
	// public void insertOrReplaceUserInfo(UserInfo info, String status) {
	//
	// UserNewVO user = new UserNewVO();
	// // user.setBirthday(birthday)
	// // UserInfos userInfos = new UserInfos();
	// // userInfos.setStatus(status);
	// // userInfos.setUsername(info.getName());
	// // userInfos.setPortrait(String.valueOf(info.getPortraitUri()));
	// // userInfos.setUserid(info.getUserId());
	// mUserInfosDao.saveOrUpdate(user);
	// }

	public void insertOrReplaceUserInfoList(ArrayList<UserInfo> list,
			String status) {

		List<UserInfos> userInfos = new ArrayList();

	}

	/**
	 * ͨ��userid ���� UserInfo�����ҵ��Ǳ��ص����ݿ�
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
	 * ��ú����б�
	 * 
	 * @return
	 */
	public ArrayList<UserInfo> getFriendList() {
		List<UserInfo> userInfoList = new ArrayList();

		// ��Ӧ����ȫ���ģ�Ӧ�������еĺ���
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
	 * ����userids��ú����б�
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
	 * ͨ��groupid ���groupname
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
		 * λ����Ϣ�ṩ��:LocationProvider �Ļص��������򿪵�������ͼҳ�档
		 * 
		 * @param context
		 *            ������
		 * @param callback
		 *            �ص�
		 */
		@Override
		public void onStartLocation(Context context,
				RongIM.LocationProvider.LocationCallback callback) {
			/**
			 * demo ���� ���������滻���Լ��Ĵ��롣
			 */
			// DemoContext.getInstance().setLastLocationCallback(callback);
			// Intent intent = new Intent(context, SOSOLocationActivity.class);
			// intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			// context.startActivity(intent);// SOSO��ͼ
		}
	}

}
