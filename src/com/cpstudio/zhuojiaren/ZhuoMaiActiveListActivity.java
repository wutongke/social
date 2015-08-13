package com.cpstudio.zhuojiaren;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.cpstudio.zhuojiaren.helper.JsonHandler;
import com.cpstudio.zhuojiaren.helper.ZhuoConnHelper;
import com.cpstudio.zhuojiaren.model.MessagePubVO;
import com.cpstudio.zhuojiaren.model.MsgTagVO;
import com.cpstudio.zhuojiaren.ui.PubDetailActivity;
import com.cpstudio.zhuojiaren.util.CommonAdapter;
import com.cpstudio.zhuojiaren.util.ViewHolder;
import com.cpstudio.zhuojiaren.widget.ListViewFooter;

public class ZhuoMaiActiveListActivity extends Activity implements
		OnItemClickListener {

	private ListView mListView;
	private CommonAdapter mAdapter;
	private ArrayList<MessagePubVO> mList = new ArrayList<MessagePubVO>();
	private ZhuoConnHelper mConnHelper = null;
	private ListViewFooter mListViewFooter = null;
	int pageNum = 0, pageSize = 5;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_up_level);
		mConnHelper = ZhuoConnHelper.getInstance(getApplicationContext());
		mAdapter = new CommonAdapter<MessagePubVO>(
				ZhuoMaiActiveListActivity.this, mList,
				R.layout.item_zhuomai_list) {

			@Override
			public void convert(ViewHolder helper, final MessagePubVO item) {
				// TODO Auto-generated method stub
				helper.setText(R.id.textViewTitle, item.getPublish());// ְλ
				helper.setText(R.id.textViewTime, item.getPubtime());
				helper.setText(R.id.textViewContent, item.getContent());

			}
		};
		mListView = (ListView) findViewById(R.id.listView);
		LayoutInflater inflater = LayoutInflater
				.from(ZhuoMaiActiveListActivity.this);
		RelativeLayout footerView = (RelativeLayout) inflater.inflate(
				R.layout.listview_footer, null);
		mListView.addFooterView(footerView);
		mListViewFooter = new ListViewFooter(footerView, onMoreClick);
		mListView.setAdapter(mAdapter);
		mListView.setOnItemClickListener(this);
		loadData();
	}

	private void updateItemList(String data, boolean refresh, boolean append) {
		try {
			if (refresh || (data != null && !data.equals(""))) {
				JsonHandler nljh = new JsonHandler(data,
						getApplicationContext());
				List<MessagePubVO> list = nljh.parsePubMessageList();
				if (!list.isEmpty()) {
					mListViewFooter.hasData();
					if (!append) {
						mList.clear();
					}
					mList.addAll(list);
					mAdapter.notifyDataSetChanged();
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
			mConnHelper.getZhuomaiList(mUIHandler, MsgTagVO.DATA_LOAD, pageNum,
					pageSize);
		}
	}

	private void loadData() {
		if (mListViewFooter.startLoading()) {
			mList.clear();
			pageNum = 0;
			mAdapter.notifyDataSetChanged();
			mConnHelper.getZhuomaiList(mUIHandler, MsgTagVO.DATA_LOAD, pageNum,
					pageSize);
		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		if (arg3 != -1) {
			Intent i = new Intent(ZhuoMaiActiveListActivity.this,
					PubDetailActivity.class);
			i.putExtra("id", mList.get(arg2).getId());
			startActivity(i);
		}
	}

}
