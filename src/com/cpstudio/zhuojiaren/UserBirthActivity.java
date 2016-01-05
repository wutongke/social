package com.cpstudio.zhuojiaren;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.cpstudio.zhuojiaren.adapter.BirthUsersListAdapter;
import com.cpstudio.zhuojiaren.helper.JsonHandler;
import com.cpstudio.zhuojiaren.helper.ZhuoCommHelper;
import com.cpstudio.zhuojiaren.helper.ConnHelper;
import com.cpstudio.zhuojiaren.model.MsgTagVO;
import com.cpstudio.zhuojiaren.model.UserVO;
import com.cpstudio.zhuojiaren.widget.ListViewFooter;
import com.cpstudui.zhuojiaren.lz.ZhuoMaiCardActivity;

public class UserBirthActivity extends Activity implements OnItemClickListener {
	private ListView mListView;
	private BirthUsersListAdapter mAdapter;
	private ArrayList<UserVO> mList = new ArrayList<UserVO>();
	private ConnHelper mConnHelper = null;
	private int mPage = 1;
	private String groupid = null;
	private ListViewFooter mListViewFooter = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_birth_users);
		mConnHelper = ConnHelper.getInstance(getApplicationContext());
		Intent intent = getIntent();
		groupid = intent.getStringExtra("groupid");
		mAdapter = new BirthUsersListAdapter(this, mList);
		mListView = (ListView) findViewById(R.id.listView);
		LayoutInflater inflater = LayoutInflater.from(UserBirthActivity.this);
		RelativeLayout mFooterView = (RelativeLayout) inflater.inflate(
				R.layout.listview_footer, null);
		mListView.addFooterView(mFooterView);
		mListViewFooter = new ListViewFooter(mFooterView, onMoreClick);
		mListView.setAdapter(mAdapter);
		mListView.setOnItemClickListener(this);
		initClick();
		loadData();
	}

	private void updateItemList(String data, boolean refresh, boolean append) {
		try {
			if (data != null && !data.equals("")) {
				JsonHandler nljh = new JsonHandler(data,
						getApplicationContext());
				ArrayList<UserVO> list = nljh.parseUserList();
				if (!list.isEmpty()) {
					mListViewFooter.hasData();
					if (!append) {
						mList.clear();
					}
					mList.addAll(list);
					mAdapter.notifyDataSetChanged();
					mPage++;
				} else {
					mListViewFooter.noData(!refresh);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@SuppressLint("HandlerLeak")
	private Handler mUIHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MsgTagVO.DATA_LOAD: {
				mListViewFooter.finishLoading();
				updateItemList((String) msg.obj, true, false);
				break;
			}
			case MsgTagVO.DATA_MORE: {
				mListViewFooter.finishLoading();
				updateItemList((String) msg.obj, false, true);
				break;
			}
			}
		}
	};

	private void loadData() {
		if (mListViewFooter.startLoading()) {
			mList.clear();
			mAdapter.notifyDataSetChanged();
			mPage = 1;
			String params = ZhuoCommHelper.getUrlBirthdayUsers() + "?page="
					+ mPage;
			if (groupid != null && !groupid.equals("")) {
				params += "&groupid=" + groupid;
			}
			mConnHelper.getFromServer(params, mUIHandler, MsgTagVO.DATA_LOAD);
		}
	}

	private void loadMore() {
		if (mListViewFooter.startLoading()) {
			String params = ZhuoCommHelper.getUrlBirthdayUsers() + "?page="
					+ mPage;
			if (groupid != null && !groupid.equals("")) {
				params += "&groupid=" + groupid;
			}
			mConnHelper.getFromServer(params, mUIHandler, MsgTagVO.DATA_MORE);
		}
	}

	private void initClick() {
		findViewById(R.id.buttonBack).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				UserBirthActivity.this.finish();
			}
		});
	}

	private OnClickListener onMoreClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			loadMore();
		}
	};

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		if (arg3 != -1) {
			String userid = (String) arg1.getTag(R.id.tag_id);
			Intent i = new Intent(UserBirthActivity.this,
					ZhuoMaiCardActivity.class);
			i.putExtra("userid", userid);
			startActivity(i);
		}
	}

}
