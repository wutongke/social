package com.cpstudio.zhuojiaren.ui;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.RelativeLayout;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import butterknife.ButterKnife;
import butterknife.InjectView;

import com.cpstudio.zhuojiaren.BaseActivity;
import com.cpstudio.zhuojiaren.R;
import com.cpstudio.zhuojiaren.adapter.GrouthAdapter;
import com.cpstudio.zhuojiaren.helper.AppClientLef;
import com.cpstudio.zhuojiaren.helper.JsonHandler;
import com.cpstudio.zhuojiaren.helper.JsonHandler_Lef;
import com.cpstudio.zhuojiaren.model.GrouthVedio;
import com.cpstudio.zhuojiaren.model.MsgTagVO;
import com.cpstudio.zhuojiaren.model.ResultVO;
import com.cpstudio.zhuojiaren.widget.ListViewFooter;
import com.cpstudio.zhuojiaren.widget.PullDownView;
import com.cpstudio.zhuojiaren.widget.PullDownView.OnPullDownListener;

public class GrouthListActivity extends BaseActivity {
	@InjectView(R.id.agq_pulldown)
	PullDownView pullDownView;
	private GrouthAdapter mAdapter;
	private ListView listView;
	private ArrayList<GrouthVedio> mDatas = new ArrayList<GrouthVedio>();
	// ·ÖÒ³
	private int mPage = 0;
	private AppClientLef appClientLef;
	private String tutorId;
	private String typeId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_grouth_query);
		ButterKnife.inject(this);
		initTitle();
		function.setBackgroundResource(R.drawable.ibutton);
		function.setText(R.string.label_filter2);
		tutorId = getIntent().getStringExtra("tutorId");
		typeId = getIntent().getStringExtra("typeId");
		title.setText(R.string.title_activity_up_level);
		initPullDownView();
		appClientLef = AppClientLef.getInstance(this);
		loadData();
	}

	private void initPullDownView() {
		// TODO Auto-generated method stub
		function.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivity(new Intent(GrouthListActivity.this,GrouthChooseActivity.class));
				GrouthListActivity.this.finish();
			}
		});
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
				Intent intent = new Intent(GrouthListActivity.this,
						VedioActivity.class);
				intent.putExtra("vedio", mDatas.get(position - 1));
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
				loadMore();
			}
		});
		pullDownView.setShowHeader();
		pullDownView.setShowFooter(false);
//		pullDownView.setHideFooter(true);
	}

	private void loadData() {
		if (pullDownView.startLoadData()) {
			mDatas.clear();
			mPage = 0;
			mAdapter.notifyDataSetChanged();
			appClientLef.getVedioList(tutorId, typeId, mPage, 5, uiHandler,
					MsgTagVO.DATA_LOAD, GrouthListActivity.this, true, null,
					null);
		}

	}

	private void loadMore() {
		appClientLef.getVedioList(tutorId, typeId, mPage, 5, uiHandler,
				MsgTagVO.DATA_MORE, GrouthListActivity.this, true, null, null);
	}

	Handler uiHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			ResultVO res;
			if (JsonHandler.checkResult((String) msg.obj,
					GrouthListActivity.this)) {
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
				ArrayList<GrouthVedio> list = JsonHandler_Lef
						.parseGrouthVedioList(data);
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
