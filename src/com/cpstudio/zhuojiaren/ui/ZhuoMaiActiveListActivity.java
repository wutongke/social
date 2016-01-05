package com.cpstudio.zhuojiaren.ui;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.cpstudio.zhuojiaren.R;
import com.cpstudio.zhuojiaren.R.id;
import com.cpstudio.zhuojiaren.R.layout;
import com.cpstudio.zhuojiaren.R.string;
import com.cpstudio.zhuojiaren.helper.JsonHandler;
import com.cpstudio.zhuojiaren.helper.ConnHelper;
import com.cpstudio.zhuojiaren.model.MessagePubVO;
import com.cpstudio.zhuojiaren.model.MsgTagVO;
import com.cpstudio.zhuojiaren.util.CommonAdapter;
import com.cpstudio.zhuojiaren.util.CommonUtil;
import com.cpstudio.zhuojiaren.widget.ListViewFooter;
import com.cpstudio.zhuojiaren.widget.ViewHolder;
/**
 * 倬脉动态
 * @author lz
 *
 */
public class ZhuoMaiActiveListActivity extends BaseActivity implements
		OnItemClickListener {
	ImageView ivBanner;
	private ListView mListView;
	private CommonAdapter mAdapter;
	private ArrayList<MessagePubVO> mList = new ArrayList<MessagePubVO>();
	private ConnHelper mConnHelper = null;
	private ListViewFooter mListViewFooter = null;
	int pageNum = 0, pageSize = 5;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_up_level);
		initTitle();
		title.setText(R.string.label_active_zhuomai);

		mConnHelper = ConnHelper.getInstance(getApplicationContext());
		mAdapter = new CommonAdapter<MessagePubVO>(
				ZhuoMaiActiveListActivity.this, mList,
				R.layout.item_zhuomai_list) {

			@Override
			public void convert(ViewHolder helper, final MessagePubVO item) {
				// TODO Auto-generated method stub
				helper.setText(R.id.textViewTitle, item.getPublish());// 职位
				helper.setText(R.id.textViewTime, item.getPubtime());
				helper.setText(R.id.textViewContent, item.getContent());
				helper.setImageByNetUrl(R.id.imageViewRes, item.getPubpic());
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
		ivBanner = (ImageView) findViewById(R.id.main_banner);
		loadAdImage();
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
			case MsgTagVO.DATA_OTHER: {
//				LoadImage imageLoader = LoadImage.getInstance();
				//解析图片并下载ivBanner
				break;
			}
			}
		}
	};

	public void loadMore() {
		if (mListViewFooter.startLoading()) {
			mConnHelper.getZhuomaiList(mUIHandler, MsgTagVO.DATA_MORE, pageNum,
					pageSize);
		}
	}

	private void loadData() {
		if (CommonUtil.getNetworkState(getApplicationContext()) == 0) {
			{
				if (mListViewFooter.startLoading()) {
					mList.clear();
					pageNum = 0;
					mAdapter.notifyDataSetChanged();
					mConnHelper.getZhuomaiList(mUIHandler, MsgTagVO.DATA_LOAD,
							pageNum, pageSize);
				}
			}
		}

	}

	private void loadAdImage() {
		if (CommonUtil.getNetworkState(getApplicationContext()) == 0) {
			mConnHelper.getAdInfo(mUIHandler, MsgTagVO.DATA_OTHER, 2);
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
