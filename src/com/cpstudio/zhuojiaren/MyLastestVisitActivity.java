package com.cpstudio.zhuojiaren;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.cpstudio.zhuojiaren.adapter.VisitUsersListAdapter;
import com.cpstudio.zhuojiaren.helper.JsonHandler;
import com.cpstudio.zhuojiaren.helper.ZhuoCommHelper;
import com.cpstudio.zhuojiaren.helper.ZhuoConnHelper;
import com.cpstudio.zhuojiaren.model.MsgTagVO;
import com.cpstudio.zhuojiaren.model.RecentVisitVO;
import com.cpstudui.zhuojiaren.lz.ZhuoMaiCardActivity;

public class MyLastestVisitActivity extends Activity implements
		OnItemClickListener {
	private ListView mListView;
	private VisitUsersListAdapter mAdapter;
	private ArrayList<RecentVisitVO> mList = new ArrayList<RecentVisitVO>();
	private ZhuoConnHelper mConnHelper = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_lastest_visit);
		mConnHelper = ZhuoConnHelper.getInstance(getApplicationContext());
		mAdapter = new VisitUsersListAdapter(this, mList);
		mListView = (ListView) findViewById(R.id.listView);
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
				if (msg.obj != null && !msg.obj.equals("")) {
					JsonHandler nljh = new JsonHandler((String) msg.obj,
							getApplicationContext());
					ArrayList<RecentVisitVO> list = nljh.parseRecentVisitList();
					if (!list.isEmpty()) {
						mList.clear();
						mList.addAll(list);
						mAdapter.notifyDataSetChanged();
					}
				}
				break;
			}
			}
		}
	};

	private void loadData() {
		mConnHelper.getFromServer(ZhuoCommHelper.getUrlLastVisitUserList(),
				mUIHandler, MsgTagVO.DATA_LOAD);
	}

	private void initClick() {

		findViewById(R.id.buttonBack).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				MyLastestVisitActivity.this.finish();
			}
		});
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		if (arg3 != -1) {
			Intent i = new Intent(MyLastestVisitActivity.this,
					ZhuoMaiCardActivity.class);
			i.putExtra("userid", (String) arg1.getTag(R.id.tag_id));
			startActivity(i);
		}
	}

}
