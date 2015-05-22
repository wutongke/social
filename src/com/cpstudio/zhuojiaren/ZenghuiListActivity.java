package com.cpstudio.zhuojiaren;

import java.util.ArrayList;

import com.cpstudio.zhuojiaren.R;
import com.cpstudio.zhuojiaren.adapter.ZenghuiListAdapter;
import com.cpstudio.zhuojiaren.helper.JsonHandler;
import com.cpstudio.zhuojiaren.helper.ZhuoCommHelper;
import com.cpstudio.zhuojiaren.helper.ZhuoConnHelper;
import com.cpstudio.zhuojiaren.model.MsgTagVO;
import com.cpstudio.zhuojiaren.model.ZhuoInfoVO;
import com.cpstudio.zhuojiaren.widget.ListViewFooter;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.RelativeLayout;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;

public class ZenghuiListActivity extends Activity implements
		OnItemClickListener {
	private ListView mListView;
	private ZenghuiListAdapter mAdapter;
	private ArrayList<ZhuoInfoVO> mList = new ArrayList<ZhuoInfoVO>();
	private String mType = "";
	// private String mLastId = null;
	private ZhuoConnHelper mConnHelper = null;
	private ListViewFooter mListViewFooter = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_zenghui_list);
		mConnHelper = ZhuoConnHelper.getInstance(getApplicationContext());
		Intent i = getIntent();
		mType = i.getStringExtra("type");
		TextView title = (TextView) findViewById(R.id.userNameShow);
		title.setText(ZhuoCommHelper.getZenghuiName(ZenghuiListActivity.this,
				mType));
		mAdapter = new ZenghuiListAdapter(this, mList);
		mListView = (ListView) findViewById(R.id.listViewZenghui);
		LayoutInflater inflater = LayoutInflater.from(ZenghuiListActivity.this);
		RelativeLayout mFooterView = (RelativeLayout) inflater.inflate(
				R.layout.listview_footer, null);
		mFooterView.setVisibility(View.GONE);
		mListView.addFooterView(mFooterView);
		mListViewFooter = new ListViewFooter(mFooterView, onMoreClick);
		mListView.setAdapter(mAdapter);
		mListView.setOnItemClickListener(this);
		initClick();
		loadData();
	}

	private void updateItemList(String data, boolean refresh, boolean append) {
		try {
			if (refresh || (data != null && !data.equals(""))) {
				JsonHandler nljh = new JsonHandler(data,
						getApplicationContext());
				ArrayList<ZhuoInfoVO> list = nljh.parseZhuoInfoList();
				if (!list.isEmpty()) {
					mListViewFooter.hasData();
					if (!append) {
						mList.clear();
					}
					mList.addAll(list);
					mAdapter.notifyDataSetChanged();
					// if (mList.size() > 0) {
					// mLastId = mList.get(mList.size() - 1).getMsgid();
					// }
				} else {
					mListViewFooter.noData(!refresh);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private OnClickListener onMoreClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			loadMore();
		}
	};
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

	public void loadMore() {
		if (mListViewFooter.startLoading()) {
		}
	}

	private void loadData() {
		if (mListViewFooter.startLoading()) {
			mList.clear();
			mAdapter.notifyDataSetChanged();
			String params = ZhuoCommHelper.getUrlZenghuiTitleList();
			params += "?type=" + mType;
			mConnHelper.getFromServer(params, mUIHandler, MsgTagVO.DATA_LOAD);
		}
	}

	private void initClick() {

		findViewById(R.id.buttonBack).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				ZenghuiListActivity.this.finish();
			}
		});
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		if (arg3 != -1) {
			Intent i = new Intent(ZenghuiListActivity.this,
					ZenghuiActivity.class);
			i.putExtra("msgid", (String) arg1.getTag(R.id.tag_id));
			startActivity(i);
			// overridePendingTransition(R.anim.activity_right_in,
			// R.anim.activity_left_out);
		}
	}

}
