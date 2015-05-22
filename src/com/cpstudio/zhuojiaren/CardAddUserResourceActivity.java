package com.cpstudio.zhuojiaren;

import java.util.ArrayList;

import com.cpstudio.zhuojiaren.adapter.ResListAdapter;
import com.cpstudio.zhuojiaren.helper.JsonHandler;
import com.cpstudio.zhuojiaren.helper.ZhuoCommHelper;
import com.cpstudio.zhuojiaren.helper.ZhuoConnHelper;
import com.cpstudio.zhuojiaren.model.MsgTagVO;
import com.cpstudio.zhuojiaren.model.ZhuoInfoVO;
import com.cpstudio.zhuojiaren.widget.ListViewFooter;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.RelativeLayout;

public class CardAddUserResourceActivity extends Activity implements
		OnItemClickListener {

	private ListView mListView;
	private ResListAdapter mAdapter;
	private ArrayList<ZhuoInfoVO> mList = new ArrayList<ZhuoInfoVO>();
	private ZhuoConnHelper mConnHelper = null;
	private int mType = 0;
	private int mPage = 1;
	private String userid = "";
	private ListViewFooter mListViewFooter = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_card_add_user_resource);
		mConnHelper = ZhuoConnHelper.getInstance(getApplicationContext());
		Intent i = getIntent();
		mType = i.getIntExtra(CardEditActivity.EDIT_RES_STR1, 0);
		userid = i.getStringExtra(CardEditActivity.EDIT_RES_STR2);
		mListView = (ListView) findViewById(R.id.listView);
		mAdapter = new ResListAdapter(CardAddUserResourceActivity.this, mList);
		LayoutInflater inflater = LayoutInflater
				.from(CardAddUserResourceActivity.this);
		RelativeLayout mFooterView = (RelativeLayout) inflater.inflate(
				R.layout.listview_footer, null);
		mListView.addFooterView(mFooterView);
		mListViewFooter = new ListViewFooter(mFooterView, onMoreClick);
		mListView.setAdapter(mAdapter);
		mListView.setOnItemClickListener(this);
		loadData();
		initClick();
	}

	private void initClick() {
		findViewById(R.id.buttonBack).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				CardAddUserResourceActivity.this.finish();
			}
		});
	}

	private OnClickListener onMoreClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			loadMore();
		}
	};

	private void updateItemList(String data, boolean refresh, boolean append) {
		try {
			if (data != null && !data.equals("")) {
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
					mPage++;
				} else {
					mListViewFooter.noData(!refresh);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
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

	private void loadData() {
		if (mListViewFooter.startLoading()) {
			mList.clear();
			mAdapter.notifyDataSetChanged();
			mPage = 1;
			String params = ZhuoCommHelper.getUrlMyResource() + "?page=1";
			params += "&type=" + mType;
			params += "&uid=" + userid;
			mConnHelper.getFromServer(params, mUIHandler, MsgTagVO.DATA_LOAD);
		}
	}

	private void loadMore() {
		if (mListViewFooter.startLoading()) {
			String params = ZhuoCommHelper.getUrlMyResource() + "?page="
					+ mPage;
			params += "&type=" + mType;
			params += "&uid=" + userid;
			mConnHelper.getFromServer(params, mUIHandler, MsgTagVO.DATA_MORE);
		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		if (arg3 != -1) {
			Intent i = new Intent(CardAddUserResourceActivity.this,
					MsgDetailActivity.class);
			i.putExtra("msgid", (String) arg1.getTag(R.id.tag_id));
			startActivity(i);
		}
	}
}
