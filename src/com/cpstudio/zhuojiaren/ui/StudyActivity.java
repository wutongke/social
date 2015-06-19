package com.cpstudio.zhuojiaren.ui;

import java.util.ArrayList;

import android.os.Bundle;
import android.os.Handler;
import android.widget.ListView;
import butterknife.ButterKnife;
import butterknife.InjectView;

import com.cpstudio.zhuojiaren.BaseActivity;
import com.cpstudio.zhuojiaren.R;
import com.cpstudio.zhuojiaren.adapter.GrouthAdapter;
import com.cpstudio.zhuojiaren.model.Grouth;
import com.cpstudio.zhuojiaren.widget.PullDownView;

public class StudyActivity extends BaseActivity {
	@InjectView(R.id.as_pulldown)
	PullDownView pullDownView;
	private GrouthAdapter mAdapter;
	private ListView listView;
	private ArrayList<Grouth> mDatas = new ArrayList<Grouth>();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_study);
		ButterKnife.inject(this);
		initTitle();
		title.setText(R.string.title_activity_up_level);
		initPullDownView();
		loadData();
	}
	private void loadData() {
		// TODO Auto-generated method stub
		
	}
	Handler uiHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			
		};
	};
	private void initPullDownView() {
		// TODO Auto-generated method stub
		pullDownView.initHeaderViewAndFooterViewAndListView(this, R.layout.head_grouth_main);
		listView = pullDownView.getListView();
		mAdapter = new GrouthAdapter(this, mDatas, R.layout.item_growth);
		listView.setAdapter(mAdapter);
	}

}
