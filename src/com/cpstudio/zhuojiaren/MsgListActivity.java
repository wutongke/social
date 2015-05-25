package com.cpstudio.zhuojiaren;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import com.cpstudio.zhuojiaren.R;
import com.cpstudio.zhuojiaren.adapter.MsgUserListAdapter;
import com.cpstudio.zhuojiaren.facade.CardMsgFacade;
import com.cpstudio.zhuojiaren.facade.CmtRcmdFacade;
import com.cpstudio.zhuojiaren.facade.ImChatFacade;
import com.cpstudio.zhuojiaren.facade.ImQuanFacade;
import com.cpstudio.zhuojiaren.facade.SysMsgFacade;
import com.cpstudio.zhuojiaren.helper.ResHelper;
import com.cpstudio.zhuojiaren.model.CardMsgVO;
import com.cpstudio.zhuojiaren.model.CmtRcmdVO;
import com.cpstudio.zhuojiaren.model.ImMsgVO;
import com.cpstudio.zhuojiaren.model.ImQuanVO;
import com.cpstudio.zhuojiaren.model.SysMsgVO;
import com.cpstudio.zhuojiaren.model.UserVO;
import com.cpstudio.zhuojiaren.util.CommonUtil;
import com.cpstudio.zhuojiaren.util.ImMsgComparator;

import android.os.Bundle;
import android.app.Activity;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView.OnEditorActionListener;
import android.widget.TextView;

public class MsgListActivity extends Activity implements OnItemClickListener {
	private ListView mListView;
	private MsgUserListAdapter mAdapter;
	private ArrayList<ImMsgVO> mList = new ArrayList<ImMsgVO>();
	private String myid = null;
	private ImChatFacade mFacade = null;
	private MsgReceiver msgReceiver = null;
	private String mSearchKey = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_msg_list);
		mFacade = new ImChatFacade(MsgListActivity.this);
		myid = ResHelper.getInstance(getApplicationContext()).getUserid();
		LayoutInflater inflater = LayoutInflater.from(MsgListActivity.this);
		View headView = inflater.inflate(R.layout.listview_header7, null);
		initHeadViewEvent(headView);
		mAdapter = new MsgUserListAdapter(this, mList);
		mListView = (ListView) findViewById(R.id.listView);
		mListView.addHeaderView(headView);
		mListView.setAdapter(mAdapter);
		mListView.setOnItemClickListener(this);
	}

	@Override
	protected void onResume() {
		ResHelper.getInstance(getApplicationContext()).setMsgList(true);
		NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		notificationManager.cancelAll();
		loadData();
		msgReceiver = new MsgReceiver();
		IntentFilter filter = new IntentFilter("com.cpstudio.chatlist");
		registerReceiver(msgReceiver, filter);
		super.onResume();
	}

	@Override
	protected void onPause() {
		ResHelper.getInstance(getApplicationContext()).setMsgList(false);
		if (msgReceiver != null) {
			unregisterReceiver(msgReceiver);
		}
		super.onPause();
	}

	private void startCardActivity() {
		findViewById(R.id.textViewMsgCardAll).setVisibility(View.GONE);
		Intent i = new Intent(MsgListActivity.this, MsgCardListActivity.class);
		startActivity(i);
	}

	private void startQuanListActivity() {
		findViewById(R.id.textViewMsgQuanAll).setVisibility(View.GONE);
		Intent i = new Intent(MsgListActivity.this, MsgQuanListActivity.class);
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
						startCardActivity();
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
		findViewById(R.id.buttonLinkList).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						Intent i = new Intent(MsgListActivity.this,
								LinkListActivity.class);
						startActivity(i);
					}
				});

		final EditText searchView = (EditText) findViewById(R.id.editTextSearch);
		final View delSearch = findViewById(R.id.imageViewDelSearch);
		searchView.setOnEditorActionListener(new OnEditorActionListener() {

			@Override
			public boolean onEditorAction(TextView v, int actionId,
					KeyEvent event) {
				if (actionId == EditorInfo.IME_ACTION_SEARCH) {
					mSearchKey = v.getText().toString();
					InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
					imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
					loadData();
				}
				return false;
			}
		});
		searchView.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {
				if (searchView.getText().toString().equals("")) {
					delSearch.setVisibility(View.GONE);
				} else {
					delSearch.setVisibility(View.VISIBLE);
				}
			}
		});

		delSearch.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				searchView.setText("");
				if (mSearchKey != null && !mSearchKey.equals("")) {
					mSearchKey = "";
					loadData();
				}
			}
		});
	}

	private void loadData() {
		if (mSearchKey == null || mSearchKey.equals("")) {
			loadQuanData();
			loadSysMsgData();
			loadCardData();
			loadCmtRcmdData();
		}
		loadChatData();
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

	private void loadCardData() {
		CardMsgFacade facade = new CardMsgFacade(MsgListActivity.this);
		ArrayList<CardMsgVO> list = facade.getAllByCondition(null, null, null,
				"addtime desc limit 0,1");
		if (!list.isEmpty()) {
			int unread = facade.getAllByCondition("isread = ?",
					new String[] { "0" }, null, null).size();
			TextView textViewMsgAll = ((TextView) findViewById(R.id.textViewMsgCardAll));
			textViewMsgAll.setText(unread + "");
			if (unread > 0) {
				textViewMsgAll.setVisibility(View.VISIBLE);
			} else {
				textViewMsgAll.setVisibility(View.GONE);
			}
			((TextView) findViewById(R.id.textViewCardMsg))
					.setText(gentCardMsg(list.get(0)));
		} else {
			((TextView) findViewById(R.id.textViewCardMsg)).setText("");
		}
	}

	private void loadCmtRcmdData() {
		CmtRcmdFacade facade = new CmtRcmdFacade(MsgListActivity.this);
		ArrayList<CmtRcmdVO> list = facade.getAllByCondition(null, null, null,
				"addtime desc limit 0,1");
		if (!list.isEmpty()) {
			int unread = facade.getAllByCondition("isread = ?",
					new String[] { "0" }, null, null).size();
			TextView textViewMsgAll = ((TextView) findViewById(R.id.textViewMsgCmtAll));
			textViewMsgAll.setText(unread + "");
			if (unread > 0) {
				textViewMsgAll.setVisibility(View.VISIBLE);
			} else {
				textViewMsgAll.setVisibility(View.GONE);
			}
			CmtRcmdVO msg = list.get(0);
			((TextView) findViewById(R.id.textViewGoodMsg)).setText(msg
					.getSender().getUsername() + ":" + msg.getContent());
		} else {
			((TextView) findViewById(R.id.textViewGoodMsg)).setText("");
		}
	}

	private void loadChatData() {
		try {
			Map<String, ImMsgVO> map = new HashMap<String, ImMsgVO>();
			Map<String, Integer> map2 = new HashMap<String, Integer>();
			ArrayList<ImMsgVO> list1 = mFacade.getAllByCondition(
					"senderid <> ?", new String[] { myid }, "senderid",
					"addtime desc", mSearchKey.trim());
			ArrayList<ImMsgVO> list2 = mFacade.getAllByCondition(
					"receiverid <> ?", new String[] { myid }, "receiverid",
					"addtime desc", mSearchKey.trim());
			for (ImMsgVO msg : list1) {
				map.put(msg.getSender().getUserid(), msg);
			}
			for (ImMsgVO msg : list2) {
				if (map.get(msg.getReceiver().getUserid()) == null) {
					map.put(msg.getReceiver().getUserid(), msg);
				} else {
					String time1 = map.get(msg.getReceiver().getUserid())
							.getAddtime();
					String time2 = msg.getAddtime();
					if (time1.compareTo(time2) < 0) {
						map.put(msg.getReceiver().getUserid(), msg);
					}
				}
			}
			list1.clear();
			list2.clear();
			for (String userid : map.keySet()) {
				int unreadCount = mFacade.getUnreadById(userid).size();
				if (unreadCount > 0) {
					list1.add(map.get(userid));
					map2.put(userid, unreadCount);
				} else {
					list2.add(map.get(userid));
					map2.put(userid, 0);
				}
			}
			Collections.sort(list1, new ImMsgComparator());
			Collections.sort(list2, new ImMsgComparator());
			mList.clear();
			mAdapter.setUnreadCount(map2);
			mList.addAll(list1);
			mList.addAll(list2);
			mAdapter.notifyDataSetChanged();
			if (mSearchKey != null && !mSearchKey.equals("") && list1.isEmpty()
					&& list2.isEmpty()) {
				CommonUtil.displayToast(getApplicationContext(),
						R.string.error18);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private String gentCardMsg(CardMsgVO msg) {
		String state = msg.getState();
		String receiverName = msg.getReceiver().getUsername();
		String authorName = msg.getSender().getUsername();
		String senderid = msg.getSender().getUserid();
		String rs = "";
		String cardState = "0";
		if (senderid.equals(myid)) {
			if (state.equals("send")) {
				cardState = "0";
			} else {
				cardState = "2";
			}
		} else {
			if (state.equals("send")) {
				cardState = "1";
			} else {
				cardState = "3";
			}
		}
		if (cardState.equals("0")) {
			rs = getString(R.string.label_tohe) + receiverName
					+ getString(R.string.label_sendcard);
		} else if (cardState.equals("1")) {
			rs = authorName + getString(R.string.label_tomy)
					+ getString(R.string.label_sendcard);
		} else if (cardState.equals("2")) {
			rs = authorName + getString(R.string.label_acceptmycard);
		} else if (cardState.equals("3")) {
			rs = getString(R.string.label_accepthecard) + authorName
					+ getString(R.string.label_acceptcard);
		}
		return rs;
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		if (arg3 != -1) {
			arg1.findViewById(R.id.textViewMsgAll).setVisibility(View.GONE);
			Intent i = new Intent(MsgListActivity.this, ChatActivity.class);
			i.putExtra("userid", (String) arg1.getTag(R.id.tag_id));
			startActivity(i);
		}
	}

	private class MsgReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			loadData();
		}
	}

}
