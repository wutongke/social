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

import com.cpstudio.zhuojiaren.R;
import com.cpstudio.zhuojiaren.adapter.GrouthVisitAdapter;
import com.cpstudio.zhuojiaren.helper.ConnHelper;
import com.cpstudio.zhuojiaren.helper.JsonHandler;
import com.cpstudio.zhuojiaren.model.GrouthVisit;
import com.cpstudio.zhuojiaren.model.MsgTagVO;
import com.cpstudio.zhuojiaren.model.ResultVO;
import com.cpstudio.zhuojiaren.widget.PullDownView;
import com.cpstudio.zhuojiaren.widget.PullDownView.OnPullDownListener;

public class GrouthVisitListActivity extends BaseActivity {

	@InjectView(R.id.agv_pulldown)
	PullDownView pullDownView;
	private GrouthVisitAdapter mAdapter;
	private ListView listView;
	private ArrayList<GrouthVisit> mDatas = new ArrayList<GrouthVisit>();
	// ·ÖÒ³
	private int mPage = 0;
	private ConnHelper appClientLef;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_grouth_visit_listctivity);
		ButterKnife.inject(this);
		initTitle();
		appClientLef = ConnHelper.getInstance(this);
		title.setText(R.string.grouth_visite_label);
		initPullDownView();
		loadData();
	}

	private void loadData() {
		if (pullDownView.startLoadData()) {
			mDatas.clear();
			mPage = 0;
			mAdapter.notifyDataSetChanged();
			appClientLef.getVisiteList(mPage, 5, uiHandler,
					MsgTagVO.DATA_LOAD, GrouthVisitListActivity.this, true, null,
					null);
		}

	}

	private void loadMore() {
		appClientLef.getVisiteList(mPage, 5, uiHandler,
				MsgTagVO.DATA_MORE, GrouthVisitListActivity.this, true, null, null);
	}

	private Handler uiHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			ResultVO res;
			if (JsonHandler.checkResult((String) msg.obj,
					GrouthVisitListActivity.this)) {
				res = JsonHandler.parseResult((String) msg.obj);
			} else {
				return;
			}
			String data = res.getData();
			switch (msg.what) {
			case MsgTagVO.DATA_LOAD: {
				updateItemList(data, true, false);
				break;
			}
			case MsgTagVO.DATA_MORE: {
				updateItemList(data, false, true);
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
				ArrayList<GrouthVisit> list = JsonHandler
						.parseVisitList(data);
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

	private void initPullDownView() {
		// TODO Auto-generated method stub
		pullDownView.initHeaderViewAndFooterViewAndListView(this,
				R.layout.head_pull_all_no);
		listView = pullDownView.getListView();
		mAdapter = new GrouthVisitAdapter(this, mDatas, R.layout.item_visite);
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
				loadMore();
			}
		});
		pullDownView.setShowHeader();
		pullDownView.setShowFooter(false);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(GrouthVisitListActivity.this,
						GrouthVisitDetailActivity.class);
				intent.putExtra("visit", mDatas.get(position-1));
				startActivity(intent);
			}
		});
	}

}
