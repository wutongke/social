package com.cpstudui.zhuojiaren.lz;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;

import com.cpstudio.zhuojiaren.R;
import com.cpstudio.zhuojiaren.helper.ConnHelper;
import com.cpstudio.zhuojiaren.helper.JsonHandler;
import com.cpstudio.zhuojiaren.helper.ResHelper;
import com.cpstudio.zhuojiaren.model.EventVO;
import com.cpstudio.zhuojiaren.model.MsgTagVO;
import com.cpstudio.zhuojiaren.model.QuanVO;
import com.cpstudio.zhuojiaren.model.UserEvent;
import com.cpstudio.zhuojiaren.ui.BaseActivity;
import com.cpstudio.zhuojiaren.ui.EventDetailActivity;
import com.cpstudio.zhuojiaren.ui.PublishActiveActivity;
import com.cpstudio.zhuojiaren.util.CommonUtil;
import com.cpstudio.zhuojiaren.widget.PullDownView;
import com.cpstudio.zhuojiaren.widget.PullDownView.OnPullDownListener;

public class MyActiveActivity extends BaseActivity implements
		OnPullDownListener {
	@InjectView(R.id.tvPubActive)
	TextView pubView;
	@InjectView(R.id.tvSearchActive)
	TextView tvSearchActive;

	@InjectView(R.id.pvPubed)
	PullDownView pvPubed;// 发布的活动
	@InjectView(R.id.pv_joined)
	PullDownView pvJoined;// 参加的活动

	boolean isManaging = false;
	private ArrayList<EventVO> mPubList = new ArrayList<EventVO>(),
			mJoinList = new ArrayList<EventVO>();
	private ListView mPubListView, mJoinListView;
	private ConnHelper mConnHelper = null;
	private int mPage = 0;
	final int pageSize = 5;
	private EventListAdapter mPubAdapter, mJoinAdapter;
	private String uid = null;

	// String sk = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_active);
		ButterKnife.inject(this);
		initTitle();
		title.setText(R.string.title_activity_my_active);
		function.setVisibility(View.VISIBLE);
		function.setText(R.string.label_manage);

		mConnHelper = ConnHelper.getInstance(MyActiveActivity.this
				.getApplicationContext());

		uid = ResHelper.getInstance(getApplicationContext()).getUserid();

		pvPubed.initHeaderViewAndFooterViewAndListView(this,
				R.layout.listview_header2);
		pvJoined.initHeaderViewAndFooterViewAndListView(this,
				R.layout.listview_header2);

		pvPubed.setOnPullDownListener(this);
		pvJoined.setOnPullDownListener(this);
		mPubListView = pvPubed.getListView();
		mJoinListView = pvJoined.getListView();

		mPubAdapter = new EventListAdapter(MyActiveActivity.this, mPubList);
		mJoinAdapter = new EventListAdapter(MyActiveActivity.this, mJoinList);

		mPubListView.setAdapter(mPubAdapter);
		mJoinListView.setAdapter(mJoinAdapter);
		mPubListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (position < 1)
					return;
				Intent i = new Intent(MyActiveActivity.this,
						EventDetailActivity.class);
				i.putExtra("eventId", mPubList.get(position - 1)
						.getActivityid());
				startActivity(i);
			}

		});
		mJoinListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (position < 1)
					return;
				Intent i = new Intent(MyActiveActivity.this,
						EventDetailActivity.class);
				i.putExtra("eventId", mJoinList.get(position - 1)
						.getActivityid());
				startActivity(i);
			}

		});

		initClick();
		loadData();
	}

	private void updateItemList(String data, boolean refresh, boolean append) {
		boolean loadState = false;
		try {

			if (data != null && !data.equals("")) {
				JsonHandler nljh = new JsonHandler(data,
						getApplicationContext());
				UserEvent events = nljh.parseUserEvent();
				if (events != null) {
					if (!append) {
						mPubList.clear();
						mJoinList.clear();
					}
					mPubList.addAll(events.getCreateActs());
					mJoinList.addAll(events.getJoinActs());
					mPubAdapter.notifyDataSetChanged();
					mJoinAdapter.notifyDataSetChanged();
					mPage++;
				} else {
					pvPubed.noData(!refresh);
					pvJoined.noData(!refresh);
					return;
				}
				loadState = true;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		pvPubed.finishLoadData(loadState);
		pvJoined.finishLoadData(loadState);
	}

	@SuppressLint("HandlerLeak")
	private Handler mUIHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MsgTagVO.DATA_LOAD: {

				updateItemList((String) msg.obj, true, false);
				// pvPubed.finishLoadData(loadState);
				// pvJoined.finishLoadData(loadState);
				break;
			}
			case MsgTagVO.DATA_MORE: {
				updateItemList((String) msg.obj, false, true);
				break;
			}
			case MsgTagVO.disolve_quan: {
				if (JsonHandler.checkResult((String) msg.obj,
						getApplicationContext())) {
					CommonUtil.displayToast(getApplicationContext(),
							R.string.quit_quan_iCreated_success);
					offManager();
					loadData();
				} else {
					CommonUtil.displayToast(getApplicationContext(),
							R.string.data_error);
					return;
				}

				break;
			}

			case MsgTagVO.out_quan: {
				if (JsonHandler.checkResult((String) msg.obj,
						getApplicationContext())) {
					CommonUtil.displayToast(getApplicationContext(),
							R.string.quit_quan_iJoined_success);
					offManager();
					loadData();
				} else {
					CommonUtil.displayToast(getApplicationContext(),
							R.string.data_error);
					return;
				}

				break;
			}
			}

		}

	};

	public void offManager() {
		isManaging = false;
		function.setText(R.string.label_manage);
		if (mPubAdapter != null) {
			mPubAdapter.setManaging(false);
			mPubAdapter.getmSelectedList().clear();
			mPubAdapter.notifyDataSetChanged();
		}
		if (mJoinAdapter != null) {
			mJoinAdapter.setManaging(false);
			mJoinAdapter.getmSelectedList().clear();
			mJoinAdapter.notifyDataSetChanged();
		}
	}

	public void onRefresh() {
		mPage = 0;
		loadData();
	}

	public void onMore() {
		if (CommonUtil.getNetworkState(getApplicationContext()) == 2) {
		} else {
			mConnHelper.getUserEventList(mUIHandler, MsgTagVO.DATA_MORE, uid,
					mPage, pageSize);
		}
	}

	private void loadData() {
		if (pvPubed.startLoadData()) {
			mConnHelper.getUserEventList(mUIHandler, MsgTagVO.DATA_LOAD, uid,
					mPage, pageSize);
		}
	}

	private void initClick() {
		function.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (!isManaging) {
					function.setText(getString(R.string.DELETE));
					isManaging = true;
					mPubAdapter.setManaging(true);
					mJoinAdapter.setManaging(true);
				} else {
					deleteSelected();
				}

			}
		});

		pubView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent i = new Intent(MyActiveActivity.this,
						PublishActiveActivity.class);
				startActivity(i);
			}
		});

	}

	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		if (event.getKeyCode() == KeyEvent.KEYCODE_BACK && isManaging) {
			offManager();
			return true;
		}
		return super.dispatchKeyEvent(event);
	}

	protected void deleteSelected() {
		// TODO Auto-generated method stub\
		// 解散选中的我创建的圈子
		ArrayList<EventVO> deleteEventlist = (ArrayList<EventVO>) mPubAdapter
				.getmSelectedList();
		// 退出我加入的圈子
		ArrayList<EventVO> quitEventList = (ArrayList<EventVO>) mJoinAdapter
				.getmSelectedList();

		if (deleteEventlist != null && deleteEventlist.size() > 0) {
			StringBuilder eventids = new StringBuilder();
			for (EventVO event : deleteEventlist) {
				eventids.append(event.getActivityid() + ",");
			}
			mConnHelper.deleteEvents(mUIHandler, MsgTagVO.disolve_quan,
					eventids.toString());
		}
		if (quitEventList != null && quitEventList.size() > 0) {
			StringBuilder quitids = new StringBuilder();
			for (EventVO event : quitEventList) {
				quitids.append(event.getActivityid() + ",");
			}
			mConnHelper.followGroup(mUIHandler, MsgTagVO.out_quan,
					quitids.toString(), QuanVO.QUAN_QUIT, null, "none");
		}

	}
}
