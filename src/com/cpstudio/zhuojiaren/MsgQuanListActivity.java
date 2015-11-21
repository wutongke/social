package com.cpstudio.zhuojiaren;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cpstudio.zhuojiaren.adapter.MsgQuanListAdapter;
import com.cpstudio.zhuojiaren.facade.ImQuanFacade;
import com.cpstudio.zhuojiaren.helper.ResHelper;
import com.cpstudio.zhuojiaren.model.ImQuanVO;
import com.cpstudio.zhuojiaren.util.ImQuanMsgComparator;

public class MsgQuanListActivity extends Activity implements
		OnItemClickListener {
	private ListView mListView;
	private MsgQuanListAdapter mAdapter;
	private ArrayList<ImQuanVO> mList = new ArrayList<ImQuanVO>();
	private ImQuanFacade mFacade = null;
	private Map<String, Integer> unreadNum = new HashMap<String, Integer>();
	private MsgReceiver msgReceiver = null;
	private boolean toTab = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_quan_msg_list);
		Intent intent = getIntent();
		toTab = intent.getBooleanExtra("toTab", false);
		mFacade = new ImQuanFacade(MsgQuanListActivity.this);
		mAdapter = new MsgQuanListAdapter(this, mList);
		mListView = (ListView) findViewById(R.id.listView);
		LayoutInflater inflater = LayoutInflater.from(MsgQuanListActivity.this);
		View mFooterView = (RelativeLayout) inflater.inflate(
				R.layout.listview_footer2, null);
		mListView.addFooterView(mFooterView);
		mListView.setAdapter(mAdapter);
		mListView.setOnItemClickListener(this);
		initClick();
	}

	@Override
	protected void onResume() {
		NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		notificationManager.cancelAll();
		if (null == msgReceiver) {
			msgReceiver = new MsgReceiver();
		}
		loadData();
		IntentFilter filter = new IntentFilter("com.cpstudio.groupchat");
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

	private void initClick() {
		findViewById(R.id.buttonBack).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View paramView) {
				MsgQuanListActivity.this.finish();
			}
		});
	}

	private void loadData() {
		ArrayList<ImQuanVO> templist = mFacade.getAllByCondition(null, null,
				"groupid", "addtime desc");
		ArrayList<ImQuanVO> list1 = new ArrayList<ImQuanVO>();
		ArrayList<ImQuanVO> list2 = new ArrayList<ImQuanVO>();
		for (ImQuanVO quanMsg : templist) {
			String groupid = quanMsg.getGroup().getGroupid();
			int unreadCount = getMsgCount(groupid);
			if (unreadCount > 0) {
				list1.add(quanMsg);
				unreadNum.put(groupid, unreadCount);
			} else {
				list2.add(quanMsg);
				unreadNum.put(groupid, 0);
			}
		}
		if (!templist.isEmpty()) {
			Collections.sort(list1, new ImQuanMsgComparator());
			Collections.sort(list2, new ImQuanMsgComparator());
			mList.clear();
			mAdapter.setUnreadCount(unreadNum);
			mList.addAll(list1);
			mList.addAll(list2);
			mAdapter.notifyDataSetChanged();
		} else {
			TextView nodata = (TextView) findViewById(R.id.textViewNoData);
			nodata.setText(R.string.label_no_data2);
			nodata.setVisibility(View.VISIBLE);
		}
		findViewById(R.id.progressLoading).setVisibility(View.GONE);
		findViewById(R.id.textViewLoading).setVisibility(View.GONE);
	}

	private int getMsgCount(String groupid) {
		return mFacade.getAllByCondition("isread = ? and groupid = ?",
				new String[] { "0", groupid }, null, null).size();
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		if (arg3 != -1) {
			arg1.findViewById(R.id.textViewMsgAll).setVisibility(View.GONE);
			Intent i = new Intent(MsgQuanListActivity.this,
					QuanBoardChatActivity.class);
			i.putExtra("groupid", (String) arg1.getTag(R.id.tag_id));
			i.putExtra("type", "chat");
			startActivity(i);
		}
	}

	private class MsgReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			loadData();
		}

	}
	
	@Override
	public void finish() {
		if(toTab && !ResHelper.getInstance(getApplicationContext()).isAppShow()){
			Intent intent = new Intent(getApplicationContext(),
					TabContainerActivity.class);
			intent.putExtra(TabContainerActivity.SHOW_PAGE,
					TabContainerActivity.MSG_PAGE);
			startActivity(intent);
		}
		super.finish();
	}

}
