package com.cpstudio.zhuojiaren;

import java.util.ArrayList;
import java.util.List;

import com.cpstudio.zhuojiaren.adapter.UserListAdapter;
import com.cpstudio.zhuojiaren.helper.JsonHandler;
import com.cpstudio.zhuojiaren.helper.ZhuoCommHelper;
import com.cpstudio.zhuojiaren.helper.ZhuoConnHelper;
import com.cpstudio.zhuojiaren.model.MsgTagVO;
import com.cpstudio.zhuojiaren.model.UserVO;
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
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.AdapterView.OnItemClickListener;

public class UserListActivity extends Activity implements OnItemClickListener {
	private ListView mListView;
	private UserListAdapter mAdapter;
	private ArrayList<UserVO> mList = new ArrayList<UserVO>();
	private ZhuoConnHelper mConnHelper = null;
	private String userid;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_list);
		mConnHelper = ZhuoConnHelper.getInstance(getApplicationContext());
		mAdapter = new UserListAdapter(this, mList);
		mListView = (ListView) findViewById(R.id.listView);
		Intent i = getIntent();
		userid = i.getStringExtra("userid");
		LayoutInflater inflater = LayoutInflater.from(UserListActivity.this);
		RelativeLayout mFooterView = (RelativeLayout) inflater.inflate(
				R.layout.listview_footer2, null);
		mListView.addFooterView(mFooterView);
		mListView.setAdapter(mAdapter);
		mListView.setOnItemClickListener(this);
		initClick();
		loadData();
	}

	@SuppressLint("HandlerLeak")
	private Handler mUIHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MsgTagVO.DATA_LOAD: {
				findViewById(R.id.progressLoading).setVisibility(View.GONE);
				findViewById(R.id.textViewLoading).setVisibility(View.GONE);
				findViewById(R.id.textViewFail).setVisibility(View.GONE);
				if (msg.obj != null && !msg.obj.equals("")) {
					JsonHandler nljh = new JsonHandler((String) msg.obj,
							getApplicationContext());
					List<UserVO> list = nljh.parseUserList();
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
		String params = ZhuoCommHelper.getUrlFamily() + "?uid=" + userid;
		mConnHelper.getFromServer(params, mUIHandler, MsgTagVO.DATA_LOAD);
	}

	private void initClick() {

		findViewById(R.id.buttonBack).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				UserListActivity.this.finish();
			}
		});
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		if (arg3 != -1) {
			Intent i = new Intent(UserListActivity.this, UserCardActivity.class);
			i.putExtra("userid", (String) arg1.getTag(R.id.tag_id));
			startActivity(i);
		}
	}

}
