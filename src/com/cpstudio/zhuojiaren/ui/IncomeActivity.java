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
import com.cpstudio.zhuojiaren.helper.AppClientLef;
import com.cpstudio.zhuojiaren.helper.JsonHandler;
import com.cpstudio.zhuojiaren.helper.JsonHandler_Lef;
import com.cpstudio.zhuojiaren.model.CrowdFundingVO;
import com.cpstudio.zhuojiaren.model.IncomeVO;
import com.cpstudio.zhuojiaren.model.MsgTagVO;
import com.cpstudio.zhuojiaren.model.ResultVO;
import com.cpstudio.zhuojiaren.widget.PullDownView;
import com.cpstudio.zhuojiaren.widget.PullDownView.OnPullDownListener;

import android.os.Bundle;
import android.os.Handler;
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
	private ArrayList<IncomeVO> mDatas = new ArrayList<IncomeVO>();

	// ·ÖÒ³
	private int mPage = 0;
	private AppClientLef appClientLef;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_income);
		ButterKnife.inject(this);
		appClientLef = AppClientLef.getInstance(this);
		initTitle();
		title.setText(R.string.income_expenditure_details);
		initPullDownView();
		loadData();
	}

	private void initPullDownView() {
		// TODO Auto-generated method stub
		pullDownView.initHeaderViewAndFooterViewAndListView(this,
				R.layout.head_pull_all_no);
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
		mAdapter = new IncomeAdapter(this, mDatas, R.layout.item_income);
		mListView.setAdapter(mAdapter);
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(IncomeActivity.this,
						IncomeDetailsActivity.class);
				intent.putExtra(getResources().getString(R.id.tag_id),
						mDatas.get(position - 1).getId());
				startActivity(intent);
			}
		});
	}

	private void loadData() {
		if (pullDownView.startLoadData()) {
			mDatas.clear();
			mPage = 0;
			mAdapter.notifyDataSetChanged();
//			appClientLef.getFundingList(typeCrowd, typePubOrInv, mPage, 5,
//					uiHandler, MsgTagVO.DATA_LOAD,
//					CrowdFundingListActivity.this, true, null, null);
		}

	}

	private void loadMore() {
//		appClientLef.getAudioList(mPage, 5, uiHandler, MsgTagVO.DATA_MORE,
//				CrowdFundingListActivity.this, true, null, null);
	}

	Handler uiHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case MsgTagVO.DATA_LOAD: {
				ResultVO res;
				if (JsonHandler.checkResult((String) msg.obj,
						IncomeActivity.this)) {
					res = JsonHandler.parseResult((String) msg.obj);
				} else {
					return;
				}
				String data = res.getData();
				updateItemList(data, true, false);
				break;
			}
			case MsgTagVO.DATA_MORE: {
				updateItemList((String) msg.obj, false, true);
				break;
			}
			}
			;
		}

	};

	private void updateItemList(String data, boolean refresh, boolean append) {
		// TODO Auto-generated method stub
		try {
			pullDownView.finishLoadData(true);
			if (data != null && !data.equals("")) {
				ArrayList<IncomeVO> list = JsonHandler_Lef
						.parseIncomeVOList(data);
				if (!list.isEmpty()) {
					if (!append) {
						mDatas.clear();
						pullDownView.hasData();
					}
					mDatas.addAll(list);
					mAdapter.notifyDataSetChanged();
					mPage++;
				} else {
					pullDownView.noData(true);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
