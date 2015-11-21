package com.cpstudio.zhuojiaren;

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
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.cpstudio.zhuojiaren.adapter.MsgUserListAdapter;
import com.cpstudio.zhuojiaren.facade.ImChatFacade;
import com.cpstudio.zhuojiaren.facade.ImQuanFacade;
import com.cpstudio.zhuojiaren.facade.SysMsgFacade;
import com.cpstudio.zhuojiaren.helper.ResHelper;
import com.cpstudio.zhuojiaren.model.ImMsgVO;
import com.cpstudio.zhuojiaren.model.ImQuanVO;
import com.cpstudio.zhuojiaren.model.ReqQuanVO;
import com.cpstudio.zhuojiaren.model.SysMsgVO;
import com.cpstudio.zhuojiaren.model.UserVO;
import com.cpstudio.zhuojiaren.ui.LZUserSameActivity;
import com.cpstudio.zhuojiaren.ui.MyFriendActivity;
import com.cpstudio.zhuojiaren.util.CommonUtil;
import com.cpstudui.zhuojiaren.lz.CustomerMessageFactory;
import com.cpstudui.zhuojiaren.lz.QuanChatListActivity;
import com.umeng.socialize.utils.Log;

public class MsgListActivity extends FragmentActivity implements
		OnItemClickListener {
	private ListView mListView;
	private MsgUserListAdapter mAdapter;
	private ArrayList<ImMsgVO> mList = new ArrayList<ImMsgVO>();
	private String myid = null;
	private ImChatFacade mFacade = null;
	private MsgReceiver msgReceiver = null;
//	private String mSearchKey = "";
	ConversationListFragment listFragment;
	Handler uiHandler;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_msg_list);
		uiHandler = new Handler();
		myid = ResHelper.getInstance(getApplicationContext()).getUserid();
		LayoutInflater inflater = LayoutInflater.from(MsgListActivity.this);
		View headView = inflater.inflate(R.layout.listview_header7, null);
		initHeadViewEvent(headView);
		mAdapter = new MsgUserListAdapter(this, mList);
		mListView = (ListView) findViewById(R.id.listView);
		mListView.addHeaderView(headView);
		mListView.setAdapter(mAdapter);
		mListView.setOnItemClickListener(this);
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
		ViewGroup root = (ViewGroup) findViewById(R.id.ryConversationListContainer);
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
		loadData();
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

	private void startSysActivity() {
		findViewById(R.id.textViewMsgSysAll).setVisibility(View.GONE);
		Intent i = new Intent(MsgListActivity.this, MsgSysListActivity.class);
		startActivity(i);
	}

	private void startCmtActivity() {
		findViewById(R.id.textViewMsgCmtAll).setVisibility(View.GONE);
		Intent i = new Intent(MsgListActivity.this, MsgRcmdCmtActivity.class);
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
								LZUserSameActivity.class);
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
								LZUserSameActivity.class);
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
		v.findViewById(R.id.linearLayoutSys).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						startSysActivity();
					}
				});
		v.findViewById(R.id.linearLayoutCmt).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						startCmtActivity();
					}
				});

		// // 通讯录
		findViewById(R.id.buttonLinkList).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						// Intent i = new Intent(MsgListActivity.this,
						// LinkListActivity.class);
						// startActivity(i);
						Intent i = new Intent(MsgListActivity.this,
								MyFriendActivity.class);
						startActivity(i);
					}
				});

//		final EditText searchView = (EditText) findViewById(R.id.editTextSearch);
//		searchView.setOnEditorActionListener(new OnEditorActionListener() {
//
//			@Override
//			public boolean onEditorAction(TextView v, int actionId,
//					KeyEvent event) {
//				if (actionId == EditorInfo.IME_ACTION_SEARCH) {
//					mSearchKey = v.getText().toString();
//					InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//					imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
//					loadData();
//				}
//				return false;
//			}
//		});
	}

	private void loadData() {
	}

	private void loadQuanData() {
		ImQuanFacade facade = new ImQuanFacade(MsgListActivity.this);
		ArrayList<ImQuanVO> list = facade.getAllByCondition(null, null, null,
				"addtime desc limit 0,1");
		if (!list.isEmpty()) {
			int unread = facade.getAllByCondition("isread = ?",
					new String[] { "0" }, null, null).size();
			TextView textViewMsgAll = ((TextView) findViewById(R.id.textViewMsgQuanAll));
			textViewMsgAll.setText(unread + "");
			if (unread > 0) {
				textViewMsgAll.setVisibility(View.VISIBLE);
			} else {
				textViewMsgAll.setVisibility(View.GONE);
			}
			ImQuanVO msg = list.get(0);
			String username = getString(R.string.label_me);
			UserVO sender = msg.getSender();
			if (!sender.getUserid().equals(myid)) {
				username = sender.getUsername();
			}
			String content = "";
			String type = msg.getType();
			if (type.equals("voice")) {
				content = getString(R.string.voice);
			} else if (type.equals("image")) {
				content = getString(R.string.image);
			} else if (type.equals("card")) {
				content = getString(R.string.label_sended)
						+ msg.getContent().split("____")[1]
						+ getString(R.string.label_sendedcardtoall);
			} else if (type.equals("emotion")) {
				content = getString(R.string.label_emotion);
			} else {
				content = msg.getContent();
			}
			TextView msgTV = (TextView) findViewById(R.id.textViewQuanMsg);
			if (msg.getIsread().equals("2")) {
				Drawable drawable = getResources().getDrawable(
						R.drawable.ico_sending_failed);
				drawable.setBounds(0, 0, drawable.getMinimumWidth(),
						drawable.getMinimumHeight());
				msgTV.setCompoundDrawables(drawable, null, null, null);
			} else if (msg.getIsread().equals("4")) {
				Drawable drawable = getResources().getDrawable(
						R.drawable.ico_sending);
				drawable.setBounds(0, 0, drawable.getMinimumWidth(),
						drawable.getMinimumHeight());
				msgTV.setCompoundDrawables(drawable, null, null, null);
			} else {
				msgTV.setCompoundDrawables(null, null, null, null);
			}
			(msgTV).setText(username + ":" + content);
		} else {
			((TextView) findViewById(R.id.textViewQuanMsg)).setText("");
		}
	}

	private void loadSysMsgData() {
		SysMsgFacade facade = new SysMsgFacade(MsgListActivity.this);
		ArrayList<SysMsgVO> list = facade.getAllByCondition(null, null, null,
				"addtime desc limit 0,1");
		if (!list.isEmpty()) {
			int unread = facade.getAllByCondition("isread = ?",
					new String[] { "0" }, null, null).size();
			TextView textViewMsgAll = ((TextView) findViewById(R.id.textViewMsgSysAll));
			textViewMsgAll.setText(unread + "");
			if (unread > 0) {
				textViewMsgAll.setVisibility(View.VISIBLE);
			} else {
				textViewMsgAll.setVisibility(View.GONE);
			}
			SysMsgVO msg = list.get(0);
			((TextView) findViewById(R.id.textViewSysMsg)).setText(CommonUtil
					.removeHTMLTag(msg.getContent()));
		} else {
			((TextView) findViewById(R.id.textViewSysMsg)).setText("");
		}
	}

	// private void loadCardData() {
	// CardMsgFacade facade = new CardMsgFacade(MsgListActivity.this);
	// ArrayList<CardMsgVO> list = facade.getAllByCondition(null, null, null,
	// "addtime desc limit 0,1");
	// if (!list.isEmpty()) {
	// int unread = facade.getAllByCondition("isread = ?",
	// new String[] { "0" }, null, null).size();
	// TextView textViewMsgAll = ((TextView)
	// findViewById(R.id.textViewMsgCardAll));
	// textViewMsgAll.setText(unread + "");
	// if (unread > 0) {
	// textViewMsgAll.setVisibility(View.VISIBLE);
	// } else {
	// textViewMsgAll.setVisibility(View.GONE);
	// }
	// ((TextView) findViewById(R.id.textViewCardMsg))
	// .setText(gentCardMsg(list.get(0)));
	// } else {
	// ((TextView) findViewById(R.id.textViewCardMsg)).setText("");
	// }
	// }

	// private void loadCmtRcmdData() {
	// CmtRcmdFacade facade = new CmtRcmdFacade(MsgListActivity.this);
	// ArrayList<CmtRcmdVO> list = facade.getAllByCondition(null, null, null,
	// "addtime desc limit 0,1");
	// if (!list.isEmpty()) {
	// int unread = facade.getAllByCondition("isread = ?",
	// new String[] { "0" }, null, null).size();
	// TextView textViewMsgAll = ((TextView)
	// findViewById(R.id.textViewMsgCmtAll));
	// textViewMsgAll.setText(unread + "");
	// if (unread > 0) {
	// textViewMsgAll.setVisibility(View.VISIBLE);
	// } else {
	// textViewMsgAll.setVisibility(View.GONE);
	// }
	// CmtRcmdVO msg = list.get(0);
	// ((TextView) findViewById(R.id.textViewGoodMsg)).setText(msg
	// .getSender().getUsername() + ":" + msg.getContent());
	// } else {
	// ((TextView) findViewById(R.id.textViewGoodMsg)).setText("");
	// }
	// }

	// private void loadChatData() {
	// try {
	// Map<String, ImMsgVO> map = new HashMap<String, ImMsgVO>();
	// // 未读消息数目
	// Map<String, Integer> map2 = new HashMap<String, Integer>();
	// // 可能得猜测。group by 返回的是该组中最后插入的一条元素，默认按时间顺序插入的，所以此处能选出最新的消息，但感觉不太严谨
	// // 一下的两个查询能保证保存的是接收到的每个人的最新消息么？ “addtime desc”
	//
	// // 别人发送的消息,group by senderid
	// //
	// ArrayList<ImMsgVO> list1 = mFacade.getAllByCondition(
	// "senderid <> ?", new String[] { myid }, "senderid",
	// "addtime desc", mSearchKey.trim());
	// // 我发送的消息
	// ArrayList<ImMsgVO> list2 = mFacade.getAllByCondition(
	// "receiverid <> ?", new String[] { myid }, "receiverid",
	// "addtime desc", mSearchKey.trim());
	//
	// // list1中保存的是接收到的每个人的最新消息么？
	// for (ImMsgVO msg : list1) {
	// map.put(msg.getSender().getUserid(), msg);
	// }
	// for (ImMsgVO msg : list2) {
	// if (map.get(msg.getReceiver().getUserid()) == null) {
	// map.put(msg.getReceiver().getUserid(), msg);
	// } else {// 既有发送的也有接收的，选择最新的一条
	// String time1 = map.get(msg.getReceiver().getUserid())
	// .getAddtime();
	// String time2 = msg.getAddtime();
	// if (time1.compareTo(time2) < 0) {
	// map.put(msg.getReceiver().getUserid(), msg);
	// }
	// }
	// }
	// list1.clear();
	// list2.clear();
	// for (String userid : map.keySet()) {
	// int unreadCount = mFacade.getUnreadById(userid).size();
	// if (unreadCount > 0) {// list1保存接收到的每个人的未读消息的最后一条
	// list1.add(map.get(userid));
	// map2.put(userid, unreadCount);
	// } else {
	// list2.add(map.get(userid));// list2保存已读消息的最后一条
	// map2.put(userid, 0);
	// }
	// }
	// // 按接收到的时间降序排序
	// Collections.sort(list1, new ImMsgComparator());
	// Collections.sort(list2, new ImMsgComparator());
	// mList.clear();
	// mAdapter.setUnreadCount(map2);
	// mList.addAll(list1);
	// mList.addAll(list2);
	// mAdapter.notifyDataSetChanged();
	// if (mSearchKey != null && !mSearchKey.equals("") && list1.isEmpty()
	// && list2.isEmpty()) {
	// CommonUtil.displayToast(getApplicationContext(),
	// R.string.error18);
	// }
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// }

	// private String gentCardMsg(CardMsgVO msg) {
	// String state = msg.getState();
	// String receiverName = msg.getReceiver().getUsername();
	// String authorName = msg.getSender().getUsername();
	// String senderid = msg.getSender().getUserid();
	// String rs = "";
	// String cardState = "0";
	// if (senderid.equals(myid)) {
	// if (state.equals("send")) {
	// cardState = "0";
	// } else {
	// cardState = "2";
	// }
	// } else {
	// if (state.equals("send")) {
	// cardState = "1";
	// } else {
	// cardState = "3";
	// }
	// }
	// if (cardState.equals("0")) {
	// rs = getString(R.string.label_tohe) + receiverName
	// + getString(R.string.label_sendcard);
	// } else if (cardState.equals("1")) {
	// rs = authorName + getString(R.string.label_tomy)
	// + getString(R.string.label_sendcard);
	// } else if (cardState.equals("2")) {
	// rs = authorName + getString(R.string.label_acceptmycard);
	// } else if (cardState.equals("3")) {
	// rs = getString(R.string.label_accepthecard) + authorName
	// + getString(R.string.label_acceptcard);
	// }
	// return rs;
	// }

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// if (arg3 != -1) {
		// arg1.findViewById(R.id.textViewMsgAll).setVisibility(View.GONE);
		// Intent i = new Intent(MsgListActivity.this, ChatActivity.class);
		// i.putExtra("userid", (String) arg1.getTag(R.id.tag_id));
		// startActivity(i);
		// }
	}

	private class MsgReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {

			String action = intent.getAction();
			// 收到好友添加的邀请
			if (action.equals(TabContainerActivity.ACTION_DMEO_RECEIVE_MESSAGE)) {
				boolean hasNewFriends = intent.getBooleanExtra("has_message",
						false);
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
			} else {
				// ((TextView) findViewById(R.id.textViewCardMsg)).setText("");
			}
			uiHandler.post(calHeight);
		}
	}
}
