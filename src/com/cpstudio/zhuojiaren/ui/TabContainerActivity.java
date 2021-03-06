package com.cpstudio.zhuojiaren.ui;

import io.rong.app.IMChatDataHelper;
import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Group;
import io.rong.message.ContactNotificationMessage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
import android.widget.Toast;

import com.cpstudio.zhuojiaren.R;
import com.cpstudio.zhuojiaren.facade.GroupFacade;
import com.cpstudio.zhuojiaren.helper.ConnHelper;
import com.cpstudio.zhuojiaren.helper.JsonHandler;
import com.cpstudio.zhuojiaren.helper.ResHelper;
import com.cpstudio.zhuojiaren.helper.ActivityManager;
import com.cpstudio.zhuojiaren.model.BaseCodeData;
import com.cpstudio.zhuojiaren.model.CustomerMessageFactory;
import com.cpstudio.zhuojiaren.model.GXTypeCodeData;
import com.cpstudio.zhuojiaren.model.GroupsForIM;
import com.cpstudio.zhuojiaren.model.MsgTagVO;
import com.cpstudio.zhuojiaren.model.Province;
import com.cpstudio.zhuojiaren.model.QuanVO;
import com.cpstudio.zhuojiaren.model.ResultVO;
import com.cpstudio.zhuojiaren.util.CommonUtil;

@SuppressWarnings("deprecation")
public class TabContainerActivity extends TabActivity implements
		OnTabChangeListener {
	public static final String ACTION_DMEO_RECEIVE_MESSAGE = "action_demo_receive_message";

	/**
	 * 同意加入圈子
	 */
	public static final String ACTION_DMEO_GROUP_MESSAGE = "action_demo_group_message";
	/**
	 * 同意添加好友
	 */
	public static final String ACTION_DMEO_AGREE_REQUEST = "action_demo_agree_request";
	public static final String ACTION_SYS_MSG = "action_demo_sys_message";
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
			com.cpstudio.zhuojiaren.ui.MainActivity.class,
			JiarenActiveActivity.class, MsgListActivity.class,
			GrouthActivity.class, MyMainActivity.class };

	private int[] mImageResourceArray = { R.drawable.indicator_tab_ico_zhuo,
			R.drawable.indicator_tab_ico_active,
			R.drawable.indicator_tab_ico_im, R.drawable.indicator_tab_ico_up,
			R.drawable.indicator_tab_ico_my };
	private String[] mTextArray = null;
	public RongIM.OnReceiveUnreadCountChangedListener mCountListener = new RongIM.OnReceiveUnreadCountChangedListener() {
		@Override
		public void onMessageIncreased(int count) {
			if (count == 0) {
				numTV.setVisibility(View.GONE);
			} else if (count > 0 && count < 100) {
				numTV.setVisibility(View.VISIBLE);
			} else {
				numTV.setVisibility(View.VISIBLE);
			}
		}
	};
	private ConnHelper connHelper = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tab_container);

		init();
		msgReceiver = new MsgReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction(ACTION_DMEO_RECEIVE_MESSAGE);
		filter.addAction(ACTION_SYS_MSG);
		filter.addAction(ACTION_DMEO_AGREE_REQUEST);
		filter.addAction(ACTION_DMEO_GROUP_MESSAGE);

		registerReceiver(msgReceiver, filter);
	}

	private void getMyGroupSuccess(GroupsForIM groups) {
		if (groups != null) {
			List<Group> grouplist = new ArrayList<Group>();
			HashMap<String, Group> groupMap = new HashMap<String, Group>();
			final List<QuanVO> quanlist = new ArrayList<QuanVO>();
			if (groups.getCreateGroups() != null) {
				for (int i = 0; i < groups.getCreateGroups().size(); i++) {
					QuanVO quna = groups.getCreateGroups().get(i);
					quanlist.add(quna);
					String id = quna.getGroupid();
					String name = quna.getGname();
					if (id == null || name == null)
						continue;
					Group tmpGroup = null;
					if (groups.getCreateGroups().get(i).getGheader() != null) {
						Uri uri = Uri.parse(groups.getCreateGroups().get(i)
								.getGheader());
						tmpGroup = new Group(id, name, uri);
					} else {
						tmpGroup = new Group(id, name, null);
					}
					if (tmpGroup != null)
						grouplist.add(tmpGroup);
					groupMap.put(tmpGroup.getId(), tmpGroup);
				}
			}
			if (groups.getFollowGroups() != null) {
				for (int i = 0; i < groups.getFollowGroups().size(); i++) {
					QuanVO quna = groups.getFollowGroups().get(i);
					quanlist.add(quna);
					String id = quna.getGroupid();
					String name = quna.getGname();
					if (id == null || name == null)
						continue;
					Group tmpGroup = null;
					if (groups.getFollowGroups().get(i).getGheader() != null) {
						Uri uri = Uri.parse(groups.getFollowGroups().get(i)
								.getGheader());
						tmpGroup = new Group(id, name, uri);
					} else {
						tmpGroup = new Group(id, name, null);
					}
					if (tmpGroup != null)
						grouplist.add(tmpGroup);
					groupMap.put(tmpGroup.getId(), tmpGroup);
				}
			}
			connHelper.setGroupMap(groupMap);
			if (grouplist.size() > 0 && RongIM.getInstance() != null
					&& RongIM.getInstance().getRongIMClient() != null)
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
			// 异步更新圈子信息到数据库
			new Thread(new Runnable() {
				@Override
				public void run() {
					// TODO Auto-genrated method stub
					GroupFacade mgfcade = IMChatDataHelper.getInstance(
							getApplicationContext()).getmGroupInfoDao();
					mgfcade.saveOrUpdateAll(quanlist);
				}
			}).start();

		}
	}

	private void init() {
		ResHelper.getInstance(getApplicationContext()).setAppShow(true);
		ActivityManager.getInstance().addActivity(TabContainerActivity.this);
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
		connHelper = ConnHelper.getInstance(getApplicationContext());
		if (connHelper.getGroupMap() == null) {
			connHelper.getMyGroupList(msgHandler, MsgTagVO.DATA_OTHER);
		}
		connHelper.getBaseCodeData(msgHandler, MsgTagVO.DATA_BASE,
				TabContainerActivity.this, false, null, null);
		connHelper.getGXTypes(msgHandler, MsgTagVO.DATA_GXTYPES,
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
		textView.setTextSize(10);
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
							ConnHelper.CITYS);
				} else {
					return;
				}
				String data = res.getData();
				List<Province> dataset = JsonHandler.parseCodedCitys(data);
				connHelper.setCitysOfPrince(dataset);
				break;
			}
			case MsgTagVO.DATA_BASE: {// 基础编码数据，保存到内存中\
				ResultVO res = null;
				String dataStr = null;
				if (JsonHandler.checkResult((String) msg.obj,
						getApplicationContext())) {
					dataStr = (String) msg.obj;
					connHelper.saveObject(dataStr, ConnHelper.BASEDATA);
				} else {
					dataStr = connHelper.readObject(ConnHelper.BASEDATA);
				}
				if (dataStr != null)
					res = JsonHandler.parseResult(dataStr);
				if (res != null) {
					String data = res.getData();
					BaseCodeData dataset = JsonHandler.parseBaseCodeData(data);
					connHelper.setBaseDataSet(dataset);
				}
				break;
			}
			case MsgTagVO.DATA_GXTYPES: {// 供需类型数据，保存到内存中\
				ResultVO res = null;
				String dataStr = null;
				if (JsonHandler.checkResult((String) msg.obj,
						getApplicationContext())) {
					dataStr = (String) msg.obj;
					connHelper.saveObject(dataStr, ConnHelper.GXTYPES);
				} else {
					dataStr = connHelper.readObject(ConnHelper.GXTYPES);
				}
				if (dataStr != null)
					res = JsonHandler.parseResult(dataStr);
				if (res != null) {
					String data = res.getData();
					GXTypeCodeData gxTypes = JsonHandler
							.parseGXTypeCodeData(data);
					connHelper.setGxTypeCodeDataSet(gxTypes);
				}
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

		super.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		if (msgReceiver != null) {
			unregisterReceiver(msgReceiver);
		}
		super.onDestroy();
	}

	private class MsgReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			numTV.setVisibility(View.VISIBLE);
			String action = intent.getAction();
			// 收到好友添加的邀请
			if (action.equals(ACTION_DMEO_RECEIVE_MESSAGE)) {
				ContactNotificationMessage msg = intent
						.getParcelableExtra("rongCloud");

				if (msg != null) {
					String sourceid = msg.getSourceUserId();
					String text = sourceid + getString(R.string.label_tomy)
							+ getString(R.string.label_sendcard);
					CommonUtil.displayToast(getApplicationContext(), text);
				}
			} else if (action.equals(ACTION_DMEO_AGREE_REQUEST)) {
				String text = intent.getStringExtra("userid");
				if (text != null) {
					text += "同意添加好友！";
					CommonUtil.displayToast(getApplicationContext(), text);
				}
			} else if (action
					.equals(TabContainerActivity.ACTION_DMEO_GROUP_MESSAGE)) {

			} else {
				String data = intent.getStringExtra("json");
				Toast.makeText(getApplicationContext(), data, Toast.LENGTH_SHORT).show();
				int type = intent.getIntExtra("type", -1);
				if (type == -1)
					return;
				switch (type) {
				case CustomerMessageFactory.CMT:

					break;
				case CustomerMessageFactory.ZAN:

					break;
				case CustomerMessageFactory.REQUEST_JOIN_QAUN:

					break;
				case CustomerMessageFactory.SYS:

					break;

				default:
					break;
				}
			}
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
}
