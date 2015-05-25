package com.cpstudio.zhuojiaren;

import java.util.ArrayList;

import com.cpstudio.zhuojiaren.adapter.ZhuoInfoListAdapter;
import com.cpstudio.zhuojiaren.facade.InfoFacade;
import com.cpstudio.zhuojiaren.helper.JsonHandler;
import com.cpstudio.zhuojiaren.helper.ZhuoCommHelper;
import com.cpstudio.zhuojiaren.helper.ZhuoConnHelper;
import com.cpstudio.zhuojiaren.model.MsgTagVO;
import com.cpstudio.zhuojiaren.model.ZhuoInfoVO;
import com.cpstudio.zhuojiaren.util.CommonUtil;
import com.cpstudio.zhuojiaren.widget.ListViewFooter;

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
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;

public class MyCollectListActivity extends Activity implements
		OnItemClickListener {

	private ListView mListView;
	private ZhuoInfoListAdapter mAdapter;
	private ArrayList<ZhuoInfoVO> mList = new ArrayList<ZhuoInfoVO>();
	private ZhuoConnHelper mConnHelper = null;
	private int mPage = 1;
	private ListViewFooter mListViewFooter = null;
	private InfoFacade mFacade = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_collect_list);
		mConnHelper = ZhuoConnHelper.getInstance(getApplicationContext());
		mFacade = new InfoFacade(getApplicationContext(),
				InfoFacade.COLLECTLIST);
		mAdapter = new ZhuoInfoListAdapter(MyCollectListActivity.this, mList);
		mListView = (ListView) findViewById(R.id.listView);
		LayoutInflater inflater = LayoutInflater
				.from(MyCollectListActivity.this);
		RelativeLayout mFooterView = (RelativeLayout) inflater.inflate(
				R.layout.listview_footer, null);
		mListView.addFooterView(mFooterView);
		mListViewFooter = new ListViewFooter(mFooterView, onMoreClick);
		mListView.setAdapter(mAdapter);
		mListView.setOnItemClickListener(this);
		initClick();
		loadData();
	}

	private void updateItemList(ArrayList<ZhuoInfoVO> list, boolean refresh,
			boolean append) {
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

	@SuppressLint("HandlerLeak")
	private Handler mUIHandler = new Handler() {
		@SuppressWarnings("unchecked")
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MsgTagVO.DATA_LOAD: {
				mListViewFooter.finishLoading();
				ArrayList<ZhuoInfoVO> list = new ArrayList<ZhuoInfoVO>();
				if (msg.obj instanceof ArrayList) {
					list = (ArrayList<ZhuoInfoVO>) msg.obj;
				} else {
					if (msg.obj != null && !msg.obj.equals("")) {
						JsonHandler nljh = new JsonHandler((String) msg.obj,
								getApplicationContext());
						list = nljh.parseZhuoInfoList();
						if (!list.isEmpty()) {
							mFacade.update(list);
						}
					}
				}
				updateItemList(list, true, false);
				break;
			}
			case MsgTagVO.DATA_MORE: {
				mListViewFooter.finishLoading();
				ArrayList<ZhuoInfoVO> list = new ArrayList<ZhuoInfoVO>();
				if (msg.obj instanceof ArrayList) {
					list = (ArrayList<ZhuoInfoVO>) msg.obj;
				} else {
					if (msg.obj != null && !msg.obj.equals("")) {
						JsonHandler nljh = new JsonHandler((String) msg.obj,
								getApplicationContext());
						list = nljh.parseZhuoInfoList();
						if (!list.isEmpty()) {
							mFacade.update(list);
						}
					}
				}
				updateItemList(list, false, true);
				break;
			}
			}

		}

	};

	public void loadMore() {
		if (mListViewFooter.startLoading()) {
			if (CommonUtil.getNetworkState(getApplicationContext()) == 2) {
				ArrayList<ZhuoInfoVO> list = mFacade.getByPage(mPage);
				Message msg = mUIHandler.obtainMessage(MsgTagVO.DATA_MORE);
				msg.obj = list;
				msg.sendToTarget();
			} else {
				String params = ZhuoCommHelper.getUrlCollectList();
				params += "?page=" + mPage;
				mConnHelper.getFromServer(params, mUIHandler,
						MsgTagVO.DATA_MORE);
			}
		}
	}

	public void loadData() {
		if (mListViewFooter.startLoading()) {
			mList.clear();
			mAdapter.notifyDataSetChanged();
			mPage = 1;
			if (CommonUtil.getNetworkState(getApplicationContext()) == 2) {
				ArrayList<ZhuoInfoVO> list = mFacade.getByPage(mPage);
				Message msg = mUIHandler.obtainMessage(MsgTagVO.DATA_LOAD);
				msg.obj = list;
				msg.sendToTarget();
			} else {
				String params = ZhuoCommHelper.getUrlCollectList();
				params += "?page=" + mPage;
				mConnHelper.getFromServer(params, mUIHandler,
						MsgTagVO.DATA_LOAD);
			}
		}
	}

	private void initClick() {

		findViewById(R.id.buttonBack).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				MyCollectListActivity.this.finish();
			}
		});
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
			Intent i = new Intent();
			i.setClass(MyCollectListActivity.this, MsgDetailActivity.class);
			i.putExtra("msgid", (String) arg1.getTag(R.id.tag_id));
			startActivity(i);
		}
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == Activity.RESULT_OK) {
			if (requestCode == MsgTagVO.MSG_CMT) {
				String forward = data.getStringExtra("forward");
				String msgid = data.getStringExtra("msgid");
				for (int i = 0; i < mList.size(); i++) {
					ZhuoInfoVO item = mList.get(i);
					if (msgid != null) {
						if (item.getMsgid().equals(msgid)) {
							if (forward != null && forward.equals("1")) {
								item.setForwardnum(String.valueOf(Integer
										.valueOf(item.getForwardnum()) + 1));
							}
							item.setCmtnum(String.valueOf(Integer.valueOf(item
									.getCmtnum()) + 1));
							mList.set(i, item);
							break;
						}
					}
				}
				mAdapter.notifyDataSetChanged();
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

}
