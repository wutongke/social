package com.cpstudio.zhuojiaren;

import java.util.ArrayList;
import com.cpstudio.zhuojiaren.adapter.SysMsgListAdapter;
import com.cpstudio.zhuojiaren.facade.SysMsgFacade;
import com.cpstudio.zhuojiaren.helper.ResHelper;
import com.cpstudio.zhuojiaren.model.SysMsgVO;
import com.cpstudio.zhuojiaren.widget.ListViewFooter;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.AdapterView.OnItemClickListener;
import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;

public class MsgSysListActivity extends Activity implements OnItemClickListener {
	private ListView mListView;
	private SysMsgListAdapter mAdapter;
	private ArrayList<SysMsgVO> mList = new ArrayList<SysMsgVO>();
	private RelativeLayout mFooterView;
	private ListViewFooter mListViewFooter = null;
	private SysMsgFacade mFacade = null;
	private boolean toTab = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sys_msg_list);
		Intent intent = getIntent();
		toTab = intent.getBooleanExtra("toTab", false);
		mAdapter = new SysMsgListAdapter(this, mList);
		mListView = (ListView) findViewById(R.id.listView);
		LayoutInflater inflater = LayoutInflater.from(MsgSysListActivity.this);
		mFooterView = (RelativeLayout) inflater.inflate(
				R.layout.listview_footer, null);
		mListView.addFooterView(mFooterView);
		mListViewFooter = new ListViewFooter(mFooterView, onMoreClick);
		mListView.setAdapter(mAdapter);
		mListView.setOnItemClickListener(this);
		mFacade = new SysMsgFacade(MsgSysListActivity.this);
		initClick();
		loadData();
	}

	private void initClick() {
		findViewById(R.id.buttonBack).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				MsgSysListActivity.this.finish();
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
		ArrayList<SysMsgVO> list = mFacade.getAllByCondition(null, null, null,
				"length(id) desc,id desc limit 0,20");
		if (!list.isEmpty()) {
			mList.clear();
			mList.addAll(list);
			mAdapter.notifyDataSetChanged();
			mFacade.updateReadState();
		} else {
			mListViewFooter.noData(false);
		}
	}

	private void loadMore() {
		int offset = mList.size();
		ArrayList<SysMsgVO> list = mFacade.getAllByCondition(null, null, null,
				"length(id) desc,id desc limit 20 offset " + offset);
		if (!list.isEmpty()) {
			mList.addAll(list);
			mAdapter.notifyDataSetChanged();
			mFacade.updateReadState();
		} else {
			mListViewFooter.noData(true);
		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		if (arg3 != -1) {
		}
	}

	@Override
	protected void onResume() {
		NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		notificationManager.cancelAll();
		super.onResume();
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
