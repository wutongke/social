package com.cpstudio.zhuojiaren.ui;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;

import com.cpstudio.zhuojiaren.BaseActivity;
import com.cpstudio.zhuojiaren.R;
import com.cpstudio.zhuojiaren.R.layout;
import com.cpstudio.zhuojiaren.R.menu;
import com.cpstudio.zhuojiaren.adapter.CrowdFundingAdapter;
import com.cpstudio.zhuojiaren.adapter.TitleAdapter;
import com.cpstudio.zhuojiaren.model.CrowdFundingVO;
import com.cpstudio.zhuojiaren.widget.PullDownView;
import com.cpstudio.zhuojiaren.widget.PullDownView.OnPullDownListener;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.ListView;

public class CrowdFundingListActivity extends BaseActivity {
	@InjectView(R.id.acfl_pull_down_view)
	PullDownView pullDownView;
	private ListView mListView;
	private CrowdFundingAdapter mAdapter;
	private ArrayList<CrowdFundingVO> mListDatas=new ArrayList<CrowdFundingVO>();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_crowd_funding_list);
		ButterKnife.inject(this);
		String type = getIntent().getStringExtra("type");
		initTitle();
		if(!type.isEmpty()){
			title.setText(type);
		}
		initPullDownView();
	}
	private void initPullDownView() {
		// TODO Auto-generated method stub
		pullDownView.initHeaderViewAndFooterViewAndListView(this, R.layout.head_pull_all);
		pullDownView.setOnPullDownListener(new OnPullDownListener() {
			
			@Override
			public void onRefresh() {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onMore() {
				// TODO Auto-generated method stub
				
			}
		});
		pullDownView.setShowHeader();
		pullDownView.setShowFooter(false);
		mListView = pullDownView.getListView();
		mAdapter = new CrowdFundingAdapter(this, mListDatas, R.layout.item_crowdfunding);
		mListView.setAdapter(mAdapter);
	}
	
}
