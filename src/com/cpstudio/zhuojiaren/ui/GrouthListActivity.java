package com.cpstudio.zhuojiaren.ui;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import butterknife.ButterKnife;
import butterknife.InjectView;

import com.cpstudio.zhuojiaren.BaseActivity;
import com.cpstudio.zhuojiaren.R;
import com.cpstudio.zhuojiaren.adapter.GrouthAdapter;
import com.cpstudio.zhuojiaren.model.GrouthVedio;
import com.cpstudio.zhuojiaren.widget.PullDownView;
import com.cpstudio.zhuojiaren.widget.PullDownView.OnPullDownListener;

public class GrouthListActivity extends BaseActivity {
	@InjectView(R.id.agq_pulldown)
	PullDownView pullDownView;
	private GrouthAdapter mAdapter;
	private ListView listView;
	private ArrayList<GrouthVedio> mDatas = new ArrayList<GrouthVedio>();

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
		GrouthVedio g = new GrouthVedio();
		g.setBrowerCount("145");
		g.setImageUrl("http://img0.imgtn.bdimg.com/it/u=3317101867,3739965699&fm=11&gp=0.jpg");
		g.setVideoUrl("http://183.131.2.75/sohu/s26h23eab6/v1/Tmx3Tmwd0KI7PLEmWMX48eW1e2eLhFytht8N0KoF5m47fFoGRMNiNw.mp4?k=Tl6RyY&p=jWlvzSPComvdopwWXZhuOp3Wtf5m4r&r=TildoRrnyLbUZDWS0pviyF2XvmEAoO24WFvSqm8VR5&q=OpCmhW7IWhodRD6sfhvSotE7ZD6sWDXOfO6HfJ6X5GvsfBo2ZDvORhbOWJ6tvm4cRhASqF2sY&cip=115.231.218.207");
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
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(GrouthListActivity.this,VedioActivity.class);
				intent.putExtra("vedio", mDatas.get(position-1));
				startActivity(intent);
			}
		});
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
