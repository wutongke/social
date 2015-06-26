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
import com.cpstudio.zhuojiaren.adapter.AudioAdapter;
import com.cpstudio.zhuojiaren.model.RecordVO;
import com.cpstudio.zhuojiaren.widget.PullDownView;
import com.cpstudio.zhuojiaren.widget.PullDownView.OnPullDownListener;

public class AudioListActivity extends BaseActivity {
	@InjectView(R.id.aal_pulldown)
	PullDownView pullDownView;
	private AudioAdapter mAdapter;
	private ListView listView;
	private ArrayList<RecordVO> mDatas = new ArrayList<RecordVO>();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_audio_list);
		ButterKnife.inject(this);
		initTitle();
		title.setText(R.string.zhuo_audio);
		initPullDownView();
		loadData();
	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if(mAdapter!=null){
			mAdapter.stop();
		}
	}
	private void loadData() {
		// TODO Auto-generated method stub
		// test
		RecordVO g = new RecordVO();
		g.setName("张来才");
		g.setId("21");
		g.setLength("12:12:13");
		g.setUsers("张来才");
		g.setDate("2015.6.23");
		g.setPath("http://yinyueshiting.baidu.com/data2/music/122878621/648618151200320.mp3?xcode=f8e09d23004f8a09b123be2e4a685e68");
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
		mAdapter = new AudioAdapter(this, mDatas, R.layout.item_radio);
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
		listView.setOnItemClickListener(new OnItemClickListener() {
			
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				if(mAdapter!=null)
					mAdapter.stop();
				Intent intent = new Intent(AudioListActivity.this,AudioDetailActivity.class);
				intent.putExtra("id", mDatas.get(position-1).getId());
				startActivity(intent);
			}
		});
	}
}
