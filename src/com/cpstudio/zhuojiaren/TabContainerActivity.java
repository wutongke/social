package com.cpstudio.zhuojiaren;

import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.RongIMClient.ErrorCode;
import io.rong.imlib.RongIMClient.OperationCallback;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Group;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.androidpn.client.Notifier;

import android.annotation.SuppressLint;
import android.app.TabActivity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;

import com.cpstudio.zhuojiaren.facade.CardMsgFacade;
import com.cpstudio.zhuojiaren.facade.CmtRcmdFacade;
import com.cpstudio.zhuojiaren.facade.ImChatFacade;
import com.cpstudio.zhuojiaren.facade.ImQuanFacade;
import com.cpstudio.zhuojiaren.facade.SysMsgFacade;
import com.cpstudio.zhuojiaren.helper.JsonHandler;
import com.cpstudio.zhuojiaren.helper.ResHelper;
import com.cpstudio.zhuojiaren.helper.SysApplication;
import com.cpstudio.zhuojiaren.helper.ZhuoConnHelper;
import com.cpstudio.zhuojiaren.model.BaseCodeData;
import com.cpstudio.zhuojiaren.model.GroupsForIM;
import com.cpstudio.zhuojiaren.model.MsgTagVO;
import com.cpstudio.zhuojiaren.model.Province;
import com.cpstudio.zhuojiaren.model.ResultVO;
import com.cpstudio.zhuojiaren.ui.GrouthActivity;
import com.cpstudio.zhuojiaren.util.CommonUtil;
import com.cpstudui.zhuojiaren.lz.LZMyHomeActivity;
import com.cpstudui.zhuojiaren.lz.MainActivity;

@SuppressWarnings("deprecation")
public class TabContainerActivity extends TabActivity implements
		OnTabChangeListener {
	// 融云接收广播消息类型
	public static final String ACTION_DMEO_RECEIVE_MESSAGE = "action_demo_receive_message";
	public static final String ACTION_DMEO_GROUP_MESSAGE = "action_demo_group_message";
	public static final String ACTION_DMEO_AGREE_REQUEST = "action_demo_agree_request";

	private TextView numTV = null;
	public final static int MAIN_PAGE = 0;
	public final static int ACTIVE_PAGE = 1;
	public final static int UP_PAGE = 2;
	public final static int MSG_PAGE = 3;
	public final static int HOME_PAGE = 4;
	public final static String SHOW_PAGE = "current";
	public final static int MSG_LIST = 0;
	public final static int MSG_CARD = 1;
	public final static int MSG_IM = 2;
	public final static int MSG_QUAN = 3;
	public final static int MSG_LIST_QUAN = 4;
	public final static int MSG_SYS = 5;
	public final static int MSG_CMT = 6;
	public final static int MSG_CLOUD = 7;
	private int mBackClickTimes = 0;
	private MsgReceiver msgReceiver = null;
	ArrayList<TextView> tvs = new ArrayList<TextView>();
	@SuppressWarnings("rawtypes")
	private Class[] mTabClassArray = {
			com.cpstudui.zhuojiaren.lz.MainActivity.class,
			JiarenActiveActivity.class, MsgListActivity.class,
			GrouthActivity.class, LZMyHomeActivity.class };// MyHomeActivity.class

	private int[] mImageResourceArray = { R.drawable.indicator_tab_ico_zhuo,
			R.drawable.indicator_tab_ico_active,
			R.drawable.indicator_tab_ico_im, R.drawable.indicator_tab_ico_up,
			R.drawable.indicator_tab_ico_my };
	private String[] mTextArray = null;
	// 有用，在主activity中来监听显示未读消息
	public RongIM.OnReceiveUnreadCountChangedListener mCountListener = new RongIM.OnReceiveUnreadCountChangedListener() {
		@Override
		public void onMessageIncreased(int count) {
			if (count == 0) {
				numTV.setVisibility(View.GONE);
			} else if (count > 0 && count < 100) {
				numTV.setVisibility(View.VISIBLE);
				numTV.setText(count + "");
			} else {
				numTV.setVisibility(View.VISIBLE);
				numTV.setText(R.string.no_read_message);
			}
		}
	};
	private ZhuoConnHelper connHelper = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tab_container);

		init();
		//
		//
		//
		// // 首先在您的Activity中添加如下成员变量
		// final UMSocialService mController =
		// UMServiceFactory.getUMSocialService("com.umeng.share");
		// // 设置分享内容
		// mController.setShareContent("友盟社会化组件（SDK）让移动应用快速整合社交分享功能，http://www.umeng.com/social");
		// // 设置分享图片, 参数2为图片的url地址
		// mController.setShareMedia(new UMImage(TabContainerActivity.this,
		// "http://www.umeng.com/images/pic/banner_module_social.png"));
		//
		// // 是否只有已登录用户才能打开分享选择页
		// mController.openShare(TabContainerActivity.this, false);
		//
		// // 设置分享图片，参数2为本地图片的资源引用
		// //mController.setShareMedia(new UMImage(getActivity(),
		// R.drawable.icon));
		// // 设置分享图片，参数2为本地图片的路径(绝对路径)
		// //mController.setShareMedia(new UMImage(getActivity(),
		// // BitmapFactory.decodeFile("/mnt/sdcard/icon.png")));
		//
		// // 设置分享音乐
		// //UMusic uMusic = new
		// UMusic("http://sns.whalecloud.com/test_music.mp3");
		// //uMusic.setAuthor("GuGu");
		// //uMusic.setTitle("天籁之音");
		// // 设置音乐缩略图
		// //uMusic.setThumb("http://www.umeng.com/images/pic/banner_module_social.png");
		// //mController.setShareMedia(uMusic);
		//
		// // 设置分享视频
		// //UMVideo umVideo = new UMVideo(
		// // "http://v.youku.com/v_show/id_XNTE5ODAwMDM2.html?f=19001023");
		// // 设置视频缩略图
		// //umVideo.setThumb("http://www.umeng.com/images/pic/banner_module_social.png");
		// //umVideo.setTitle("友盟社会化分享!");
		// //mController.setShareMedia(umVideo);
	}

	private void getMyGroupSuccess(GroupsForIM groups) {
		if (groups != null) {
			List<Group> grouplist = new ArrayList<Group>();
			if (groups.getCreateGroups() != null) {
				for (int i = 0; i < groups.getCreateGroups().size(); i++) {
					String id = groups.getCreateGroups().get(i).getGroupid();
					String name = groups.getCreateGroups().get(i).getGname();
					if (id == null || name == null)
						continue;
					if (groups.getCreateGroups().get(i).getGheader() != null) {
						Uri uri = Uri.parse(groups.getCreateGroups().get(i)
								.getGheader());
						grouplist.add(new Group(id, name, uri));
					} else {
						grouplist.add(new Group(id, name, null));
					}
				}
			}
			if (groups.getFollowGroups() != null) {
				for (int i = 0; i < groups.getFollowGroups().size(); i++) {
					String id = groups.getFollowGroups().get(i).getGroupid();
					String name = groups.getFollowGroups().get(i).getGname();
					if (id == null || name == null)
						continue;
					if (groups.getFollowGroups().get(i).getGheader() != null) {
						Uri uri = Uri.parse(groups.getFollowGroups().get(i)
								.getGheader());
						grouplist.add(new Group(id, name, uri));
					} else {
						grouplist.add(new Group(id, name, null));
					}
				}
			}
			HashMap<String, Group> groupM = new HashMap<String, Group>();
			for (int i = 0; i < grouplist.size(); i++) {
				groupM.put(grouplist.get(i).getId(), grouplist.get(i));
				// 测试，因为之前的圈子都还未加入，先硬编码加入(TabConTainerActivity),等定义好推送的允许加入圈子后就可以加入圈子了
				Group g = grouplist.get(i);
				RongIM.getInstance()
						.getRongIMClient()
						.joinGroup(g.getId(), g.getName(),
								new OperationCallback() {

									@Override
									public void onSuccess() {

									}

									@Override
									public void onError(ErrorCode errorCode) {

									}
								});
			}

			if (ZhuoConnHelper.getInstance(getApplicationContext()) != null)
				ZhuoConnHelper.getInstance(getApplicationContext())
						.setGroupMap(groupM);

			if (grouplist.size() > 0)
				RongIM.getInstance()
						.getRongIMClient()
						.syncGroup(grouplist,
								new RongIMClient.OperationCallback() {
									@Override
									public void onSuccess() {
										Log.e("login",
												"---syncGroup-onSuccess---");
									}

									@Override
									public void onError(
											RongIMClient.ErrorCode errorCode) {
										Log.e("login",
												"---syncGroup-onError---");
									}
								});
		} else {
			// WinToast.toast(this, groups.getCode());
		}
	}

	private void init() {
		ResHelper.getInstance(getApplicationContext()).setAppShow(true);
		SysApplication.getInstance().addActivity(TabContainerActivity.this);
		mTextArray = new String[] { getString(R.string.tab_item1),
				getString(R.string.tab_item2), getString(R.string.tab_item3),
				getString(R.string.tab_item4), getString(R.string.tab_item5) };

		TabHost tab = getTabHost();
		int count = mTabClassArray.length;
		Intent intent = getIntent();
		int curr = intent.getIntExtra(SHOW_PAGE, MAIN_PAGE);
		for (int i = 0; i < count; i++) {
			TabSpec tabSpec = tab.newTabSpec(mTextArray[i])
					.setIndicator(getTabItemView(i))
					.setContent(new Intent(this, mTabClassArray[i]));
			tab.addTab(tabSpec);
		}
		tab.setCurrentTab(curr);
		tvs.get(0).setTextColor(Color.GREEN);
		tab.setOnTabChangedListener(this);

		// 有用，监听哪些未读消息
		final Conversation.ConversationType[] conversationTypes = {
				Conversation.ConversationType.PRIVATE,
				Conversation.ConversationType.DISCUSSION,
				Conversation.ConversationType.GROUP,
				Conversation.ConversationType.SYSTEM,
				Conversation.ConversationType.APP_PUBLIC_SERVICE,
				Conversation.ConversationType.PUBLIC_SERVICE };

		Handler handler = new Handler();
		handler.postDelayed(new Runnable() {
			@Override
			public void run() {
				RongIM.getInstance().setOnReceiveUnreadCountChangedListener(
						mCountListener, conversationTypes);
			}
		}, 500);
		connHelper = ZhuoConnHelper.getInstance(getApplicationContext());
		if (connHelper.getGroupMap() == null) {
			connHelper.getMyGroupList(msgHandler, MsgTagVO.DATA_OTHER);

		}
		connHelper.getBaseCodeData(msgHandler, MsgTagVO.DATA_BASE,
				TabContainerActivity.this, false, null, null);
		connHelper.getCitys(msgHandler, MsgTagVO.DATA_CITYS,
				TabContainerActivity.this, true, null, null);
	}

	/**
	 * 底部显示的布局：倬家人、活动、精进、消息等，还包括隐藏的用于显示未读消息数目的tv
	 * 
	 * @param index
	 * @return
	 */
	private View getTabItemView(int index) {
		LayoutInflater layoutInflater = LayoutInflater.from(this);
		View view = layoutInflater.inflate(R.layout.item_view_tab, null);
		ImageView imageView = (ImageView) view.findViewById(R.id.tabicon);
		if (imageView != null) {
			imageView.setImageResource(mImageResourceArray[index]);
		}

		TextView textView = (TextView) view.findViewById(R.id.tabtitle);
		textView.setText(mTextArray[index]);
		if (index == 2) {
			numTV = (TextView) view.findViewById(R.id.textViewNum);
		}
		tvs.add(textView);
		return view;
	}

	/**
	 * 消息处理
	 */
	@SuppressLint("HandlerLeak")
	private Handler msgHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case MsgTagVO.START_SEND:
				ImChatFacade imChatFacade = new ImChatFacade(
						getApplicationContext());
				ImQuanFacade imQuanFacade = new ImQuanFacade(
						getApplicationContext());
				SysMsgFacade sysMsgFacade = new SysMsgFacade(
						getApplicationContext());
				CardMsgFacade cardMsgFacade = new CardMsgFacade(
						getApplicationContext());
				CmtRcmdFacade cmtRcmdFacade = new CmtRcmdFacade(
						getApplicationContext());
				int all = imChatFacade.getUnread().size();
				all += imQuanFacade.getUnread().size();
				all += sysMsgFacade.getUnread().size();
				all += cardMsgFacade.getUnread().size();
				all += cmtRcmdFacade.getUnread().size();
				if (all > 0) {
					numTV.setText(String.valueOf(all));
					numTV.setVisibility(View.VISIBLE);
				} else {
					numTV.setText(String.valueOf("0"));
					numTV.setVisibility(View.GONE);
					// 清楚通知栏消息
					Notifier notifier = new Notifier(getApplicationContext());
					notifier.clearNotify();
				}
				break;
			case MsgTagVO.DATA_OTHER:
				if (JsonHandler.checkResult((String) msg.obj,
						getApplicationContext())) {
					JsonHandler nljh = new JsonHandler((String) msg.obj,
							getApplicationContext());
					getMyGroupSuccess(nljh.parseGroupsForIM());
				}
				break;
			case MsgTagVO.DATA_CITYS: {
				ResultVO res;
				if (JsonHandler.checkResult((String) msg.obj,
						getApplicationContext())) {
					res = JsonHandler.parseResult((String) msg.obj);
					connHelper.saveObject((String) msg.obj,
							ZhuoConnHelper.CITYS);
				} else {
					return;
				}
				String data = res.getData();
				List<Province> dataset = JsonHandler.parseCodedCitys(data);
				connHelper.setCitysOfPrince(dataset);
				break;
			}
			case MsgTagVO.DATA_BASE: {// 基础编码数据，保存到内存中\
				ResultVO res;
				if (JsonHandler.checkResult((String) msg.obj,
						getApplicationContext())) {
					res = JsonHandler.parseResult((String) msg.obj);
					connHelper.saveObject((String) msg.obj,
							ZhuoConnHelper.BASEDATA);
				} else {
					return;
				}
				String data = res.getData();
				BaseCodeData dataset = JsonHandler.parseBaseCodeData(data);
				connHelper.setBaseDataSet(dataset);
				break;
			}
			default:
				break;
			}
		}
	};

	@Override
	public void onContentChanged() {
		super.onContentChanged();
	}

	@Override
	public void setDefaultTab(int index) {
		super.setDefaultTab(index);
	}

	@Override
	public void setDefaultTab(String tag) {
		super.setDefaultTab(tag);
	}

	/**
	 * 注册广播接收器
	 */
	@Override
	protected void onResume() {
		updateMsg();
		msgReceiver = new MsgReceiver();
		IntentFilter filter = new IntentFilter("com.cpstudio.chatlist");
		registerReceiver(msgReceiver, filter);
		super.onResume();
	}

	@Override
	protected void onPause() {
		if (msgReceiver != null) {
			unregisterReceiver(msgReceiver);
		}
		super.onPause();
	}

	private class MsgReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			updateMsg();
		}
	}

	private void updateMsg() {
		if (!(getCurrentActivity() instanceof MsgListActivity)) {
			Message msg = msgHandler.obtainMessage(MsgTagVO.START_SEND);
			msg.sendToTarget();
		}
	}

	@Override
	public void onTabChanged(String tabId) {
		if (tabId.equals(getString(R.string.tab_item4))) {
			numTV.setVisibility(View.GONE);
		}
		int count = mTabClassArray.length;
		for (int i = 0; i < count; i++) {

			if (mTextArray[i].equals(tabId))
				tvs.get(i).setTextColor(Color.GREEN);
			else
				tvs.get(i).setTextColor(Color.WHITE);
		}
	}

	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		if (event.getKeyCode() == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN
				&& event.getRepeatCount() == 0) {

			if (mBackClickTimes > 0) {
				ResHelper.getInstance(getApplicationContext())
						.setAppShow(false);
				TabContainerActivity.this.finish();
			} else {
				CommonUtil.displayToast(getApplicationContext(),
						R.string.info68);
				mBackClickTimes++;
				new Thread(new Runnable() {

					@Override
					public void run() {
						try {
							Thread.sleep(3000);
							mBackClickTimes = 0;
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}).start();
			}
			return true;
		}
		return super.dispatchKeyEvent(event);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	/***
	 * 退出应用
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		super.onOptionsItemSelected(item);
		switch (item.getItemId()) {
		case R.id.exit:
			SysApplication.getInstance().exit(true, getApplicationContext());
			break;
		}
		return true;
	}
}
