package com.cpstudio.zhuojiaren;

import java.util.ArrayList;
import com.cpstudio.zhuojiaren.adapter.GoodsListAdapter;
import com.cpstudio.zhuojiaren.helper.JsonHandler;
import com.cpstudio.zhuojiaren.helper.ZhuoCommHelper;
import com.cpstudio.zhuojiaren.helper.ZhuoConnHelper;
import com.cpstudio.zhuojiaren.model.GoodsVO;
import com.cpstudio.zhuojiaren.model.MsgTagVO;
import com.cpstudio.zhuojiaren.widget.ListViewFooter;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.AdapterView.OnItemClickListener;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;

public class GoodsExchangeActivity extends Activity implements
		OnItemClickListener {

	private GoodsListAdapter mAdapter = null;
	private ArrayList<GoodsVO> mList = new ArrayList<GoodsVO>();
	private ZhuoConnHelper mConnHelper = null;
	private int mPage = 1;
	private ListViewFooter mListViewFooter = null;
	private GridView gridview = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_goods_exchange);
		mConnHelper = ZhuoConnHelper.getInstance(getApplicationContext());
		gridview = (GridView) findViewById(R.id.gridView);
		mAdapter = new GoodsListAdapter(this, mList);
		gridview.setAdapter(mAdapter);
		gridview.setOnItemClickListener(this);
		RelativeLayout mFooterView = (RelativeLayout) findViewById(R.id.layoutFooter);
		mListViewFooter = new ListViewFooter(mFooterView, onMoreClick);
		initClick();
		loadData();
	}

	private void initClick() {

		findViewById(R.id.buttonBack).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				GoodsExchangeActivity.this.finish();
			}
		});

		findViewById(R.id.buttonRule).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i = new Intent(GoodsExchangeActivity.this,
						RuleActivity.class);
				startActivity(i);
			}
		});
	}

	@SuppressLint("HandlerLeak")
	private Handler mUIHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MsgTagVO.DATA_LOAD: {
				mListViewFooter.finishLoading();
				updateItemList((String) msg.obj, true, false);
				break;
			}
			case MsgTagVO.DATA_MORE: {
				mListViewFooter.finishLoading();
				updateItemList((String) msg.obj, false, true);
				break;
			}
			}
		}
	};

	private void updateItemList(String data, boolean refresh, boolean append) {
		if (data != null && !data.equals("")) {
			JsonHandler nljh = new JsonHandler(data, getApplicationContext());
			ArrayList<GoodsVO> list = nljh.parseGoodsList();
			if (!list.isEmpty()) {
				mListViewFooter.hasData();
				if (!append) {
					mList.clear();
				}
				mPage++;
				mList.addAll(list);
				mAdapter.notifyDataSetChanged();
			} else {
				mListViewFooter.noData(!refresh);
			}
		}

	}

	public void loadMore() {
		if (mListViewFooter.startLoading()) {
			String params = ZhuoCommHelper.getUrlGetGoodsList();
			params += "?page=" + mPage;
			mConnHelper.getFromServer(params, mUIHandler, MsgTagVO.DATA_MORE);
		}
	}

	public void loadData() {
		if (mListViewFooter.startLoading()) {
			mList.clear();
			mAdapter.notifyDataSetChanged();
			mPage = 1;
			String params = ZhuoCommHelper.getUrlGetGoodsList();
			params += "?page=" + mPage;
			mConnHelper.getFromServer(params, mUIHandler, MsgTagVO.DATA_LOAD);
		}
	}

	private OnClickListener onMoreClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			loadMore();
		}
	};

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		if (arg3 != -1) {
			Intent i = new Intent(GoodsExchangeActivity.this,
					GoodsDetailActivity.class);
			i.putExtra("gid", (String) arg1.getTag(R.id.tag_id));
			startActivity(i);
		}
	}
}
