package com.cpstudio.zhuojiaren;

import java.util.ArrayList;

import org.androidpn.client.Notifier;

import android.annotation.SuppressLint;
import android.app.TabActivity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
import com.cpstudio.zhuojiaren.helper.ResHelper;
import com.cpstudio.zhuojiaren.helper.SysApplication;
import com.cpstudio.zhuojiaren.model.MsgTagVO;
import com.cpstudio.zhuojiaren.ui.GrouthActivity;
import com.cpstudio.zhuojiaren.util.CommonUtil;
import com.cpstudui.zhuojiaren.lz.LZMyHomeActivity;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.media.UMImage;

@SuppressWarnings("deprecation")
public class TabContainerActivity extends TabActivity implements
		OnTabChangeListener {
//融云接收广播消息类型
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
