package com.cpstudio.zhuojiaren;

import java.util.ArrayList;

import com.cpstudio.zhuojiaren.adapter.AboutUsListAdapter;
import com.cpstudio.zhuojiaren.helper.JsonHandler;
import com.cpstudio.zhuojiaren.helper.ZhuoCommHelper;
import com.cpstudio.zhuojiaren.helper.ZhuoConnHelper;
import com.cpstudio.zhuojiaren.model.AboutUsVO;
import com.cpstudio.zhuojiaren.model.MsgTagVO;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.AdapterView.OnItemClickListener;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;

public class MyAboutUsListActivity extends Activity implements
		OnItemClickListener {
	private ListView mListView;
	private AboutUsListAdapter mAdapter;
	private ArrayList<AboutUsVO> mList = new ArrayList<AboutUsVO>();
	private ZhuoConnHelper mConnHelper = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_about_us_list);
		mConnHelper = ZhuoConnHelper.getInstance(getApplicationContext());
		mAdapter = new AboutUsListAdapter(this, mList);
		mListView = (ListView) findViewById(R.id.listView);
		LayoutInflater inflater = LayoutInflater
				.from(MyAboutUsListActivity.this);
		RelativeLayout mFooterView = (RelativeLayout) inflater.inflate(
				R.layout.listview_footer2, null);
		mFooterView.setVisibility(View.GONE);
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
					ArrayList<AboutUsVO> list = nljh.parseAboutUsList();
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
		String params = ZhuoCommHelper.getUrlGetAboutList();
		mConnHelper.getFromServer(params, mUIHandler, MsgTagVO.DATA_LOAD);
	}

	private void initClick() {

		findViewById(R.id.buttonBack).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				MyAboutUsListActivity.this.finish();
			}
		});
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		if (arg3 != -1) {
			Intent i = new Intent(MyAboutUsListActivity.this,
					MyAboutUsActivity.class);
			i.putExtra("id", (String) arg1.getTag(R.id.tag_id));
			startActivity(i);
		}
	}

}
