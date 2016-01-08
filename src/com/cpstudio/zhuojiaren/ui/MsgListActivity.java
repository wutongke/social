package com.cpstudio.zhuojiaren.ui;

import io.rong.imkit.fragment.ConversationListFragment;
import io.rong.imlib.model.Conversation;
import io.rong.message.ContactNotificationMessage;

import java.lang.reflect.Field;
import java.util.ArrayList;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cpstudio.zhuojiaren.R;
import com.cpstudio.zhuojiaren.R.id;
import com.cpstudio.zhuojiaren.R.layout;
import com.cpstudio.zhuojiaren.R.string;
import com.cpstudio.zhuojiaren.adapter.MsgUserListAdapter;
import com.cpstudio.zhuojiaren.helper.ResHelper;
import com.cpstudio.zhuojiaren.model.CustomerMessageFactory;
import com.cpstudio.zhuojiaren.model.ImMsgVO;
import com.cpstudio.zhuojiaren.model.ReqQuanVO;
import com.umeng.socialize.utils.Log;
/**
 * 倬信界面
 * @author lz
 *
 */
public class MsgListActivity extends FragmentActivity  {
	private ListView mListView;
	private MsgUserListAdapter mAdapter;
	private ArrayList<ImMsgVO> mList = new ArrayList<ImMsgVO>();
	private MsgReceiver msgReceiver = null;
	ConversationListFragment listFragment;
	Handler uiHandler;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_msg_list);
		uiHandler = new Handler();
		LayoutInflater inflater = LayoutInflater.from(MsgListActivity.this);
		View headView = inflater.inflate(R.layout.listview_header7, null);
		initHeadViewEvent(headView);
		mAdapter = new MsgUserListAdapter(this, mList);
		mListView = (ListView) findViewById(R.id.listView);
		mListView.addHeaderView(headView);
		mListView.setAdapter(mAdapter);
		loadMessageSession();

		msgReceiver = new MsgReceiver();
		IntentFilter filter = new IntentFilter(
				TabContainerActivity.ACTION_DMEO_RECEIVE_MESSAGE);
		filter.addAction(TabContainerActivity.ACTION_SYS_MSG);
		filter.addAction(TabContainerActivity.ACTION_DMEO_AGREE_REQUEST);
		filter.addAction(TabContainerActivity.ACTION_DMEO_GROUP_MESSAGE);
		registerReceiver(msgReceiver, filter);

	}

	void loadMessageSession() {
		listFragment = ConversationListFragment.getInstance();
		Uri uri = Uri
				.parse("rong://" + getApplicationInfo().packageName)
				.buildUpon()
				.appendPath("conversationlist")
				.appendQueryParameter(
						Conversation.ConversationType.PRIVATE.getName(),
						"false") // 设置私聊会话是否聚合显示
				// .appendQueryParameter(
				// Conversation.ConversationType.GROUP.getName(), "false")// 群组
				// .appendQueryParameter(
				// Conversation.ConversationType.DISCUSSION.getName(),
				// "false")// 讨论组
				// .appendQueryParameter(
				// Conversation.ConversationType.APP_PUBLIC_SERVICE
				// .getName(),
				// "false")// 应用公众服务。
				// .appendQueryParameter(
				// Conversation.ConversationType.PUBLIC_SERVICE.getName(),
				// "false")// 公共服务号
				.appendQueryParameter(
						Conversation.ConversationType.SYSTEM.getName(), "false")// 系统
				.build();
		listFragment.setUri(uri);
		FragmentManager fragmentManager = getSupportFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager
				.beginTransaction();
		fragmentTransaction.add(R.id.ryConversationListContainer, listFragment);
		fragmentTransaction.commit();
	}

	@Override
	protected void onResume() {
		super.onResume();
		ResHelper.getInstance(getApplicationContext()).setMsgList(true);
		NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		notificationManager.cancelAll();
		uiHandler.postDelayed(calHeight, 500);
	}

	Runnable calHeight = new Runnable() {
		@Override
		public void run() {
			// TODO Auto-generated method stub
			calHeightOfFragment();
		}
	};

	private void calHeightOfFragment() {
		// 获取其高度
		ViewGroup root = (ViewGroup) findViewById(R.id.ryConversationListContainer);
		ListView listFromFragment = null;
		BaseAdapter adapterFromFragment = null;
		Field fields[] = listFragment.getClass().getDeclaredFields();
		Field.setAccessible(fields, true);
		for (Field field : fields) {
			field.setAccessible(true);
			if (field.getName().equals("mList")) {
				try {
					listFromFragment = (ListView) field.get(listFragment);
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					Log.d("Debug", "");
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					Log.d("Debug", "");
				}

			} else if (field.getName().equals("mAdapter")) {
				try {
					adapterFromFragment = (BaseAdapter) field.get(listFragment);
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					Log.d("Debug", "");
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					Log.d("Debug", "");
				}

			}
		}
		if (listFromFragment != null) {
			LinearLayout.LayoutParams lp = (LayoutParams) root
					.getLayoutParams();
			int totalHeight = 0;
			for (int i = 0; i < adapterFromFragment.getCount(); i++) {
				View listItem = adapterFromFragment.getView(i, null,
						listFromFragment);
				if (listItem instanceof RelativeLayout) {
					listItem.setLayoutParams(new RelativeLayout.LayoutParams(
							LayoutParams.WRAP_CONTENT,
							LayoutParams.WRAP_CONTENT));
				} else if (listItem instanceof LinearLayout) {
					listItem.setLayoutParams(new LinearLayout.LayoutParams(
							LayoutParams.WRAP_CONTENT,
							LayoutParams.WRAP_CONTENT));
				}
				try {
					listItem.measure(0, 0);
					totalHeight += listItem.getMeasuredHeight();
				} catch (Exception e) {
					// TODO: handle exception
					totalHeight += listItem.getHeight();
				}
			}
			lp.height = totalHeight;
			if (totalHeight > 0)
				root.setLayoutParams(lp);
		}
	}

	@Override
	protected void onPause() {

		super.onPause();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		ResHelper.getInstance(getApplicationContext()).setMsgList(false);
		if (msgReceiver != null) {
			unregisterReceiver(msgReceiver);
		}
		super.onDestroy();
	}

	private void startQuanListActivity() {
		// RongIM.getInstance().startPrivateChat(MsgListActivity.this, "9237",
		// "标题");
		// RongIM.getInstance().startGroupChat(context, targetGroupId, title);
		// findViewById(R.id.textViewMsgQuanAll).setVisibility(View.GONE);
		Intent i = new Intent(MsgListActivity.this, QuanChatListActivity.class);
		startActivity(i);
	}

	private void initHeadViewEvent(View v) {
		v.findViewById(R.id.linearLayoutCard).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						findViewById(R.id.textViewMsgCardAll).setVisibility(
								View.GONE);
						Intent i = new Intent(MsgListActivity.this,
								UsersListActivity.class);
						startActivity(i);
					}
				});
		// 请求加入圈子的人的列表
		v.findViewById(R.id.linearLayoutReqQuan).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						findViewById(R.id.textViewMsgReqQuanAll).setVisibility(
								View.GONE);
						Intent i = new Intent(MsgListActivity.this,
								UsersListActivity.class);
						i.putExtra("type", 1);
						startActivity(i);
					}
				});
		v.findViewById(R.id.linearLayoutGroup).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						startQuanListActivity();
					}
				});

		// // 通讯录
		findViewById(R.id.buttonLinkList).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						Intent i = new Intent(MsgListActivity.this,
								MyFriendActivity.class);
						startActivity(i);
					}
				});

	}


	private class MsgReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {

			String action = intent.getAction();
			// 收到好友添加的邀请
			if (action.equals(TabContainerActivity.ACTION_DMEO_RECEIVE_MESSAGE)) {
				ContactNotificationMessage msg = intent
						.getParcelableExtra("rongCloud");
				if (msg != null) {
					// 处理
					findViewById(R.id.textViewMsgCardAll).setVisibility(
							View.VISIBLE);
					String sourceid = msg.getSourceUserId();
					String text = sourceid
							+ getString(R.string.label_tomy)
							+ getString(R.string.label_sendcard);
					((TextView) findViewById(R.id.textViewCardMsg))
							.setText(text);
				}
			} else if (action
					.equals(TabContainerActivity.ACTION_DMEO_AGREE_REQUEST)) {

			} else if (action.equals(TabContainerActivity.ACTION_SYS_MSG)) {
				int type = intent.getIntExtra("type", -1);
				String jsonStr = intent.getStringExtra("json");
				if (type == -1 && jsonStr != null)
					return;
				switch (type) {
				case CustomerMessageFactory.CMT:

					break;
				case CustomerMessageFactory.ZAN:

					break;
				case CustomerMessageFactory.REQUEST_JOIN_QAUN:
					ReqQuanVO vo = CustomerMessageFactory.getInstance()
							.parseReqQuan(jsonStr);
					if (vo != null) {
						findViewById(R.id.textViewMsgReqQuanAll).setVisibility(
								View.VISIBLE);
						String text = vo.getUsername()
								+ getString(R.string.label_applyjoin)
								+ vo.getGroupname();
						((TextView) findViewById(R.id.textViewReqQuan))
								.setText(text);
					}
					break;
				case CustomerMessageFactory.SYS:

					break;
				default:
					break;
				}
			} 
			uiHandler.post(calHeight);
		}
	}
}
