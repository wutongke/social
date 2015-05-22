package com.cpstudio.zhuojiaren;

import java.util.ArrayList;
import java.util.Collections;

import com.cpstudio.zhuojiaren.adapter.LinkListAdapter;
import com.cpstudio.zhuojiaren.facade.RelateFacade;
import com.cpstudio.zhuojiaren.helper.JsonHandler;
import com.cpstudio.zhuojiaren.helper.ZhuoCommHelper;
import com.cpstudio.zhuojiaren.helper.ZhuoConnHelper;
import com.cpstudio.zhuojiaren.model.MsgTagVO;
import com.cpstudio.zhuojiaren.model.UserVO;
import com.cpstudio.zhuojiaren.util.AlphabetComparator;
import com.cpstudio.zhuojiaren.util.CommonUtil;
import com.cpstudio.zhuojiaren.widget.IndexableListView;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.RelativeLayout;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

public class FanFollowActivity extends Activity implements OnItemClickListener {
	private IndexableListView mListView;
	private LinkListAdapter mAdapter;
	private ArrayList<UserVO> mList = new ArrayList<UserVO>();
	private ZhuoConnHelper mConnHelper = null;
	private String mType = null;
	private RelateFacade mFacade = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_fan_follow);
		mConnHelper = ZhuoConnHelper.getInstance(getApplicationContext());
		mAdapter = new LinkListAdapter(this, mList);
		Intent i = getIntent();
		mType = i.getStringExtra("type");
		if (mType.equals("0")) {
			((TextView) findViewById(R.id.userNameShow))
					.setText(R.string.label_myfollow);
			mFacade = new RelateFacade(getApplicationContext(),
					RelateFacade.FOLLOWLIST);
		} else {
			((TextView) findViewById(R.id.userNameShow))
					.setText(R.string.label_myfan);
			mFacade = new RelateFacade(getApplicationContext(),
					RelateFacade.FANLIST);
		}
		mListView = (IndexableListView) findViewById(R.id.listview);
		LayoutInflater inflater = LayoutInflater.from(FanFollowActivity.this);
		RelativeLayout mFooterView = (RelativeLayout) inflater.inflate(
				R.layout.listview_footer2, null);
		mListView.addFooterView(mFooterView);
		mListView.setAdapter(mAdapter);
		mListView.setDividerHeight(0);
		mListView.setFastScrollEnabled(true);
		mListView.setOnItemClickListener(this);
		loadData();
		initClick();
	}

	private void initClick() {

		findViewById(R.id.buttonBack).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				FanFollowActivity.this.finish();
			}
		});
	}

	@SuppressLint("HandlerLeak")
	private Handler mUIHandler = new Handler() {
		@SuppressWarnings("unchecked")
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MsgTagVO.DATA_LOAD: {
				findViewById(R.id.progressLoading).setVisibility(View.GONE);
				findViewById(R.id.textViewLoading).setVisibility(View.GONE);
				findViewById(R.id.textViewFail).setVisibility(View.GONE);
				if (msg.obj != null && !msg.obj.equals("")) {
					ArrayList<UserVO> list = new ArrayList<UserVO>();
					if (msg.obj instanceof ArrayList) {
						list = (ArrayList<UserVO>) msg.obj;
					} else {
						JsonHandler nljh = new JsonHandler((String) msg.obj,
								getApplicationContext());
						list = nljh.parseUserList();
						if (list != null) {
							mFacade.update(list);
						}
					}
					Collections.sort(list, new AlphabetComparator());
					if (!list.isEmpty()) {
						findViewById(R.id.textViewNoData).setVisibility(
								View.GONE);
						mList.clear();
						mList.addAll(list);
						mAdapter.notifyDataSetChanged();
					} else {
						findViewById(R.id.textViewNoData).setVisibility(
								View.VISIBLE);
					}
				} else {
					findViewById(R.id.textViewFail).setVisibility(View.VISIBLE);
				}
				break;
			}
			}
		}
	};

	private void loadData() {
		findViewById(R.id.textViewFail).setVisibility(View.GONE);
		findViewById(R.id.progressLoading).setVisibility(View.VISIBLE);
		findViewById(R.id.textViewLoading).setVisibility(View.VISIBLE);
		findViewById(R.id.textViewNoData).setVisibility(View.GONE);
		if (CommonUtil.getNetworkState(getApplicationContext()) == 2) {
			ArrayList<UserVO> list = mFacade.getAll();
			Message msg = mUIHandler.obtainMessage(MsgTagVO.DATA_LOAD);
			msg.obj = list;
			msg.sendToTarget();
		} else {
			String params = ZhuoCommHelper.getUrlGetFollows() + "?type="
					+ mType;
			mConnHelper.getFromServer(params, mUIHandler, MsgTagVO.DATA_LOAD);
		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		if (arg3 != -1) {
			Intent i = new Intent(FanFollowActivity.this, ChatActivity.class);
			i.putExtra("userid", (String) arg1.getTag(R.id.tag_id));
			startActivity(i);
		}
	}

}
