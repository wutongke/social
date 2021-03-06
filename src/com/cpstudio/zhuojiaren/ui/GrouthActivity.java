package com.cpstudio.zhuojiaren.ui;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import butterknife.ButterKnife;
import butterknife.InjectView;

import com.cpstudio.zhuojiaren.R;
import com.cpstudio.zhuojiaren.adapter.GrouthAdapter;
import com.cpstudio.zhuojiaren.helper.ConnHelper;
import com.cpstudio.zhuojiaren.helper.JsonHandler;
import com.cpstudio.zhuojiaren.imageloader.LoadImage;
import com.cpstudio.zhuojiaren.model.GrouthVedio;
import com.cpstudio.zhuojiaren.model.MsgTagVO;
import com.cpstudio.zhuojiaren.model.ResultVO;
import com.cpstudio.zhuojiaren.widget.PullDownView;
import com.cpstudio.zhuojiaren.widget.PullDownView.OnPullDownListener;

public class GrouthActivity extends BaseActivity {
	@InjectView(R.id.as_pulldown)
	PullDownView pullDownView;
	private GrouthAdapter mAdapter;
	private ListView listView;
	private ArrayList<GrouthVedio> mDatas = new ArrayList<GrouthVedio>();
	// ��ҳ
	private int mPage = 0;
	private ConnHelper appClientLef;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_study);
		ButterKnife.inject(this);
		appClientLef = ConnHelper.getInstance(this);
		initTitle();
		title.setText(R.string.title_activity_up_level);
		imageFunction.setBackgroundResource(R.drawable.jjglass);
		imageFunction.setVisibility(View.GONE);
		findViewById(R.id.activity_back).setVisibility(View.GONE);
		initPullDownView();
		loadData();
	}

	private void loadData() {
		if (pullDownView.startLoadData()) {
			mDatas.clear();
			mPage = 0;
			mAdapter.notifyDataSetChanged();
			appClientLef.getVedioList(null, null, mPage, 5, uiHandler,
					MsgTagVO.DATA_LOAD, GrouthActivity.this, true, null, null);
		}

	}

	private void loadMore() {
		appClientLef.getVedioList(null, null, mPage, 5, uiHandler,
				MsgTagVO.DATA_MORE, GrouthActivity.this, true, null, null);
	}

	Handler uiHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case MsgTagVO.DATA_LOAD: {
				ResultVO res;
				if (JsonHandler.checkResult((String) msg.obj,
						GrouthActivity.this)) {
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
				ArrayList<GrouthVedio> list = JsonHandler
						.parseGrouthVedioList(data);
				if (!list.isEmpty()) {
					if (!append) {
						mDatas.clear();
					}
					pullDownView.hasData();
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
				R.layout.head_grouth_main);
		ImageView advertisement = (ImageView) findViewById(R.id.hgm_adv);
		LoadImage.getInstance().beginLoad("http://7xkb2a.com1.z0.glb.clouddn.com/android-gg.png", advertisement);
		listView = pullDownView.getListView();
		mAdapter = new GrouthAdapter(this, mDatas, R.layout.item_growth);
		listView.setAdapter(mAdapter);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(GrouthActivity.this,
						VedioActivity.class);
				intent.putExtra("vedio", mDatas.get(position - 1));
				startActivity(intent);
			}
		});
		pullDownView.setShowHeader();
		pullDownView.setShowFooter(false);
		pullDownView.setHideFooter(true);
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
		findViewById(R.id.hgm_grouth).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivity(new Intent(GrouthActivity.this,
						GrouthChooseActivity.class));
			}
		});
		findViewById(R.id.hgm_visit).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivity(new Intent(GrouthActivity.this,
						GrouthVisitListActivity.class));
			}
		});
		findViewById(R.id.hgm_radio).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivity(new Intent(GrouthActivity.this,
						AudioListActivity.class));
			}
		});
	}

}
