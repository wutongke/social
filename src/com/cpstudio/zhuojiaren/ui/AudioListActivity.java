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
import com.cpstudio.zhuojiaren.adapter.AudioAdapter;
import com.cpstudio.zhuojiaren.helper.ConnHelper;
import com.cpstudio.zhuojiaren.helper.JsonHandler;
import com.cpstudio.zhuojiaren.model.MsgTagVO;
import com.cpstudio.zhuojiaren.model.RecordVO;
import com.cpstudio.zhuojiaren.model.ResultVO;
import com.cpstudio.zhuojiaren.widget.PullDownView;
import com.cpstudio.zhuojiaren.widget.PullDownView.OnPullDownListener;

public class AudioListActivity extends BaseActivity {
	@InjectView(R.id.aal_pulldown)
	PullDownView pullDownView;
	private AudioAdapter mAdapter;
	private ListView listView;
	private ArrayList<RecordVO> mDatas = new ArrayList<RecordVO>();
	private int mPage = 0;
	private ConnHelper appClientLef;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_audio_list);
		ButterKnife.inject(this);
		initTitle();
		title.setText(R.string.zhuo_audio);
		appClientLef = ConnHelper.getInstance(this);
		initPullDownView();
		loadData();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if (mAdapter != null) {
			mAdapter.stop();
		}
	}

	private void loadData() {
		if (pullDownView.startLoadData()) {
			mDatas.clear();
			mPage = 0;
			mAdapter.notifyDataSetChanged();
			appClientLef.getAudioList(mPage, 5, uiHandler,
					MsgTagVO.DATA_LOAD, AudioListActivity.this, true, null,
					null);
		}

	}

	private void loadMore() {
		appClientLef.getAudioList(mPage, 5, uiHandler,
				MsgTagVO.DATA_MORE, AudioListActivity.this, true, null, null);
	}

	Handler uiHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case MsgTagVO.DATA_LOAD: {
				updateItemList((String) msg.obj, true, false);
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
			ResultVO res;
			if (JsonHandler.checkResult(data,
					AudioListActivity.this)) {
				res = JsonHandler.parseResult(data);
			} else {
				return;
			}
			data = res.getData();
			pullDownView.finishLoadData(true);
			if (data != null && !data.equals("")) {
				ArrayList<RecordVO> list = JsonHandler
						.parseAudioList(data);
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
				if (mAdapter != null)
					mAdapter.stop();
				Intent intent = new Intent(AudioListActivity.this,
						AudioDetailActivity.class);
				intent.putExtra("audio", mDatas.get(position-1));
				intent.putExtra("id", mDatas.get(position - 1).getId());
				startActivity(intent);
			}
		});
	}
}
