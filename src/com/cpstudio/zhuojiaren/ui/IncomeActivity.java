package com.cpstudio.zhuojiaren.ui;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;

import com.cpstudio.zhuojiaren.BaseActivity;
import com.cpstudio.zhuojiaren.R;
import com.cpstudio.zhuojiaren.R.layout;
import com.cpstudio.zhuojiaren.R.menu;
import com.cpstudio.zhuojiaren.adapter.CrowdFundingAdapter;
import com.cpstudio.zhuojiaren.adapter.IncomeAdapter;
import com.cpstudio.zhuojiaren.model.CrowdFundingVO;
import com.cpstudio.zhuojiaren.model.IncomeVO;
import com.cpstudio.zhuojiaren.widget.PullDownView;
import com.cpstudio.zhuojiaren.widget.PullDownView.OnPullDownListener;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class IncomeActivity extends BaseActivity {
	@InjectView(R.id.ai_pulldown)
	PullDownView pullDownView;
	private ListView mListView;
	private IncomeAdapter mAdapter;
	private ArrayList<IncomeVO> mListDatas=new ArrayList<IncomeVO>();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_income);
		ButterKnife.inject(this);
		initTitle();
		title.setText(R.string.income_expenditure_details);
		initPullDownView();
		loadData();
	}
	private void initPullDownView() {
		// TODO Auto-generated method stub
		pullDownView.initHeaderViewAndFooterViewAndListView(this, R.layout.head_pull_all_no);
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
		mAdapter = new IncomeAdapter(this, mListDatas, R.layout.item_income);
		mListView.setAdapter(mAdapter);
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(IncomeActivity.this,IncomeDetailsActivity.class);
				intent.putExtra(getResources().getString(R.id.tag_id),mListDatas.get(position-1).getId());
				startActivity(intent);
			}
		});
	}
	private void loadData() {
		// TODO Auto-generated method stub
		//test
		IncomeVO test = new IncomeVO();
		test.setId("123");
		test.setName("π∫÷√Õº È");
		test.setTime(System.currentTimeMillis()-System.currentTimeMillis()/99+"");
		test.setMoney("45");
		pullDownView.finishLoadData(true);
		pullDownView.hasData();
		mListDatas.add(test);
		
		mAdapter.notifyDataSetChanged();
	}
}
