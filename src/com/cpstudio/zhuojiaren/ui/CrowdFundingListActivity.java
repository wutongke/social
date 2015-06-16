package com.cpstudio.zhuojiaren.ui;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;

import com.cpstudio.zhuojiaren.BaseActivity;
import com.cpstudio.zhuojiaren.R;
import com.cpstudio.zhuojiaren.adapter.CrowdFundingAdapter;
import com.cpstudio.zhuojiaren.model.CrowdFundingVO;
import com.cpstudio.zhuojiaren.widget.PullDownView;
import com.cpstudio.zhuojiaren.widget.PullDownView.OnPullDownListener;

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
		loadData();
	}
	private void initPullDownView() {
		// TODO Auto-generated method stub
		pullDownView.initHeaderViewAndFooterViewAndListView(this, R.layout.head_crowdfunding);
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
		mListView = pullDownView.getListView();
		mAdapter = new CrowdFundingAdapter(this, mListDatas, R.layout.item_crowdfunding);
		mListView.setAdapter(mAdapter);
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(CrowdFundingListActivity.this,CrowdFundingDetailActivity.class);
				intent.putExtra(CrowdFundingVO.CROWDFUNDINGID,mListDatas.get(position-1).getFundingId());
				startActivity(intent);
			}
		});
	}
	private void loadData() {
		// TODO Auto-generated method stub
		//test
		CrowdFundingVO test = new CrowdFundingVO();
		test.setFundingId("123");
		test.setName("asdf");
		test.setMinPrice("5");
		test.setMoneyGet("8000");
		pullDownView.finishLoadData(true);
		pullDownView.hasData();
		mListDatas.add(test);
		
		mAdapter.notifyDataSetChanged();
	}
}
