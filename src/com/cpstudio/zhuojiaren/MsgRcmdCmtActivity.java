package com.cpstudio.zhuojiaren;

import java.util.ArrayList;

import com.cpstudio.zhuojiaren.adapter.CmtRcmdListAdapter;
import com.cpstudio.zhuojiaren.facade.CmtRcmdFacade;
import com.cpstudio.zhuojiaren.helper.ResHelper;
import com.cpstudio.zhuojiaren.model.CmtRcmdVO;
import com.cpstudio.zhuojiaren.model.MsgTagVO;
import com.cpstudio.zhuojiaren.widget.ListViewFooter;

import android.os.Bundle;
import android.app.Activity;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.RelativeLayout;

public class MsgRcmdCmtActivity extends Activity implements OnItemClickListener {
	private ListView mListView;
	private CmtRcmdListAdapter mAdapter;
	private ArrayList<CmtRcmdVO> mList = new ArrayList<CmtRcmdVO>();
	private ListViewFooter mListViewFooter = null;
	private CmtRcmdFacade mFacade = null;
	private MsgReceiver msgReceiver = null;
	private boolean toTab = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent intent = getIntent();
		toTab = intent.getBooleanExtra("toTab", false);
		setContentView(R.layout.activity_recommand_cmt);
		mAdapter = new CmtRcmdListAdapter(this, mList);
		mListView = (ListView) findViewById(R.id.listView);
		LayoutInflater inflater = LayoutInflater.from(MsgRcmdCmtActivity.this);
		RelativeLayout mFooterView = (RelativeLayout) inflater.inflate(
				R.layout.listview_footer, null);
		mListView.addFooterView(mFooterView);
		mListViewFooter = new ListViewFooter(mFooterView, onMoreClick);
		mListView.setAdapter(mAdapter);
		mListView.setOnItemClickListener(this);
		mFacade = new CmtRcmdFacade(MsgRcmdCmtActivity.this);
		initClick();
		loadData();
	}

	private void initClick() {

		findViewById(R.id.buttonBack).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				MsgRcmdCmtActivity.this.finish();
			}
		});
	}

	private OnClickListener onMoreClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			loadMore();
		}
	};

	private void loadData() {
		ArrayList<CmtRcmdVO> list = mFacade.getAllByCondition(null, null, null,
				"addtime desc limit 0,20");
		if (!list.isEmpty()) {
			mList.clear();
			mList.addAll(list);
			mAdapter.notifyDataSetChanged();
			mFacade.updateReadState();
		} else {
			mListViewFooter.noData(false);
		}
	}

	public void loadMore() {
		int offset = mList.size();
		ArrayList<CmtRcmdVO> list = mFacade.getAllByCondition(null, null, null,
				"addtime desc limit 20 offset " + offset);
		if (!list.isEmpty()) {
			mList.addAll(list);
			mAdapter.notifyDataSetChanged();
			mFacade.updateReadState();
		} else {
			mListViewFooter.noData(true);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == Activity.RESULT_OK) {
			if (requestCode == MsgTagVO.MSG_CMT) {
				String forward = data.getStringExtra("forward");
				String msgid = data.getStringExtra("msgid");
				String outterid = data.getStringExtra("outterid");
				for (int i = 0; i < mList.size(); i++) {
					CmtRcmdVO item = mList.get(i);
					if (outterid != null && msgid != null
							&& item.getOrgin() != null
							&& item.getOrgin().getMsgid().equals(msgid)) {
						if (forward != null && forward.equals("1")) {
							item.getOrgin().setForwardnum(
									String.valueOf(Integer.valueOf(item
											.getOrgin().getForwardnum()) + 1));
						}
						item.getOrgin().setCmtnum(
								String.valueOf(Integer.valueOf(item.getOrgin()
										.getCmtnum()) + 1));
						mList.set(i, item);
					}
				}
				mAdapter.notifyDataSetChanged();
				String userid = data.getStringExtra("userid");
				String myid = ResHelper.getInstance(getApplicationContext())
						.getUserid();
				if (userid.equals(myid)) {
					loadData();
				}
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void onItemClick(AdapterView<?> paramAdapterView, View arg1,
			int arg2, long arg3) {
		if (arg3 != -1) {
			Intent i = new Intent();
			i.setClass(MsgRcmdCmtActivity.this, MsgDetailActivity.class);
			i.putExtra("msgid", (String) arg1.getTag(R.id.tag_id));
			startActivity(i);
		}
	}

	@Override
	protected void onResume() {
		NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		notificationManager.cancelAll();
		msgReceiver = new MsgReceiver();
		IntentFilter filter = new IntentFilter("com.cpstudio.rcmdcmt");
		registerReceiver(msgReceiver, filter);
//		mListView.invalidateViews();
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
