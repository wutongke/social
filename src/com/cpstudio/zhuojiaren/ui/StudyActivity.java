package com.cpstudio.zhuojiaren.ui;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;
import butterknife.ButterKnife;
import butterknife.InjectView;

import com.cpstudio.zhuojiaren.BaseActivity;
import com.cpstudio.zhuojiaren.R;
import com.cpstudio.zhuojiaren.adapter.GrouthAdapter;
import com.cpstudio.zhuojiaren.model.GrouthVideo;
import com.cpstudio.zhuojiaren.widget.PullDownView;
import com.cpstudio.zhuojiaren.widget.PullDownView.OnPullDownListener;

public class StudyActivity extends BaseActivity {
	@InjectView(R.id.as_pulldown)
	PullDownView pullDownView;
	private GrouthAdapter mAdapter;
	private ListView listView;
	private ArrayList<GrouthVideo> mDatas = new ArrayList<GrouthVideo>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_study);
		ButterKnife.inject(this);
		initTitle();
		findViewById(R.id.activity_back).setVisibility(View.GONE);
		title.setText(R.string.title_activity_up_level);
		initPullDownView();
		loadData();
	}

	private void loadData() {
		// TODO Auto-generated method stub
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
				R.layout.head_grouth_main);
		listView = pullDownView.getListView();
		mAdapter = new GrouthAdapter(this, mDatas, R.layout.item_growth);
		listView.setAdapter(mAdapter);
		pullDownView.setShowHeader();
		pullDownView.setShowFooter(false);
		pullDownView.setOnPullDownListener(new OnPullDownListener() {

			@Override
			public void onRefresh() {
				// TODO Auto-generated method stub
//				loadData();
			}

			@Override
			public void onMore() {
				// TODO Auto-generated method stub
//				loadData();
			}
		});

		findViewById(R.id.hgm_grouth).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivity(new Intent(StudyActivity.this,
						GrouthChooseActivity.class));
			}
		});
		findViewById(R.id.hgm_visit).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivity(new Intent(StudyActivity.this,
						GrouthVisitListctivity.class));
			}
		});
		findViewById(R.id.hgm_radio).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivity(new Intent(StudyActivity.this,
						AudioListActivity.class));
			}
		});
	}

}
