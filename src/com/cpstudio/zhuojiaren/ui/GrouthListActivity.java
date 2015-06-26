package com.cpstudio.zhuojiaren.ui;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;

import com.cpstudio.zhuojiaren.BaseActivity;
import com.cpstudio.zhuojiaren.R;
import com.cpstudio.zhuojiaren.R.layout;
import com.cpstudio.zhuojiaren.R.menu;
import com.cpstudio.zhuojiaren.adapter.GrouthAdapter;
import com.cpstudio.zhuojiaren.model.GrouthVideo;
import com.cpstudio.zhuojiaren.widget.PullDownView;
import com.cpstudio.zhuojiaren.widget.PullDownView.OnPullDownListener;

import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.view.Menu;
import android.widget.ListView;

public class GrouthListActivity extends BaseActivity {
	@InjectView(R.id.agq_pulldown)
	PullDownView pullDownView;
	private GrouthAdapter mAdapter;
	private ListView listView;
	private ArrayList<GrouthVideo> mDatas = new ArrayList<GrouthVideo>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_grouth_query);
		ButterKnife.inject(this);
		initTitle();
		title.setText(R.string.title_activity_up_level);
		initPullDownView();
		loadData();
	}

	private void loadData() {
		// TODO Auto-generated method stub
		// test
		GrouthVideo g = new GrouthVideo();
		g.setBrowerCount("145");
		g.setImageUrl("http://img0.imgtn.bdimg.com/it/u=3317101867,3739965699&fm=11&gp=0.jpg");
		g.setName("张博士亲授");
		g.setDuration("4小时");
		pullDownView.finishLoadData(true);
		pullDownView.hasData();
		mDatas.add(g);
		mAdapter.notifyDataSetChanged();
	}

	Handler uiHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {

		};
	};

	private void initPullDownView() {
		// TODO Auto-generated method stub
		pullDownView.initHeaderViewAndFooterViewAndListView(this,
				R.layout.head_pull_all_no);
		listView = pullDownView.getListView();
		mAdapter = new GrouthAdapter(this, mDatas, R.layout.item_growth);
		listView.setAdapter(mAdapter);
		pullDownView.setOnPullDownListener(new OnPullDownListener() {

			@Override
			public void onRefresh() {
				// TODO Auto-generated method stub
				loadData();
			}

			@Override
			public void onMore() {
				// TODO Auto-generated method stub
				loadData();
			}
		});
		pullDownView.setShowHeader();
		pullDownView.setShowFooter(false);
	}
}
