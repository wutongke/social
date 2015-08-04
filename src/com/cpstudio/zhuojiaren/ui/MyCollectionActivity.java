package com.cpstudio.zhuojiaren.ui;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.ToggleButton;
import butterknife.ButterKnife;
import butterknife.InjectView;

import com.cpstudio.zhuojiaren.BaseActivity;
import com.cpstudio.zhuojiaren.QuanDetailActivity;
import com.cpstudio.zhuojiaren.R;
import com.cpstudio.zhuojiaren.adapter.AudioAdapter;
import com.cpstudio.zhuojiaren.adapter.GrouthAdapter;
import com.cpstudio.zhuojiaren.adapter.QuanListAdapter;
import com.cpstudio.zhuojiaren.helper.AppClientLef;
import com.cpstudio.zhuojiaren.helper.JsonHandler;
import com.cpstudio.zhuojiaren.helper.JsonHandler_Lef;
import com.cpstudio.zhuojiaren.helper.ZhuoCommHelper;
import com.cpstudio.zhuojiaren.model.GrouthVedio;
import com.cpstudio.zhuojiaren.model.MsgTagVO;
import com.cpstudio.zhuojiaren.model.QuanVO;
import com.cpstudio.zhuojiaren.model.RecordVO;
import com.cpstudio.zhuojiaren.model.ResultVO;
import com.cpstudio.zhuojiaren.util.DeviceInfoUtil;
import com.cpstudio.zhuojiaren.widget.PullDownView;
import com.cpstudio.zhuojiaren.widget.PullDownView.OnPullDownListener;

/***
 * 思路
 * 
 * 装载不同的数据，可能需要注意回收 设置一个共有url，用于点选不同的选项时加载不同的数据 点选选项时，需要设置url、分页数据、handler等
 * 
 * 返回的数据处理，
 * 
 * @author lef
 * 
 */
public class MyCollectionActivity extends BaseActivity {
	@InjectView(R.id.amc_pulldownview)
	PullDownView pullDownView;
	ListView listView;
	// 收藏录音
	private ArrayList<RecordVO> mDatas = new ArrayList<RecordVO>();
	BaseAdapter mAdapter;
	TableLayout table;
	private ArrayList<ToggleButton> buttonList;
	private int width = 720;
	private float times = 2;
	private int padding = 5;
	private int baseMargin = 19;
	private Context mContext;

	private String url;
	private int handlerTag;
	// 分页
	private int mPage = 0;
	private AppClientLef appClientLef;

	private final int vedio = 1;
	private final int radio = 2;
	private final int article = 3;
	private final int event = 4;
	// 名片
	private final int businessCard = 5;
	private final int product = 6;
	private final int quan = 7;
	private final int topic = 8;
	// 动态
	private final int dynamic = 9;
	// 人脉
	private final int peopleBASE = 10;
	private final int gong = 11;
	private final int xu = 12;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_collection);
		ButterKnife.inject(this);
		appClientLef = AppClientLef.getInstance(this);
		mContext = this;
		initTitle();
		title.setText(R.string.my_collect);
		initTableLayout();
	}

	private void initTableLayout() {
		// TODO Auto-generated method stub
		pullDownView.initHeaderViewAndFooterViewAndListView(mContext,
				R.layout.head_collection);
		listView = pullDownView.getListView();
		table = (TableLayout) findViewById(R.id.amc_tableLayout);
		pullDownView.setShowHeader(false);
		pullDownView.setShowFooter(false);
		mAdapter = new AudioAdapter(mContext, mDatas, R.layout.item_radio);
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
				// loadData();
			}
		});
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				if (mAdapter != null)
					((AudioAdapter) mAdapter).stop();
				Intent intent = new Intent(MyCollectionActivity.this,
						AudioDetailActivity.class);
				intent.putExtra("id", mDatas.get(position - 1).getId());
				startActivity(intent);
			}
		});
		loadData();
		// 收藏列表，初始tablelayout
		buttonList = new ArrayList<ToggleButton>();
		LayoutInflater inflater = LayoutInflater
				.from(MyCollectionActivity.this);

		this.width = DeviceInfoUtil.getDeviceCsw(getApplicationContext());
		this.times = DeviceInfoUtil.getDeviceCsd(getApplicationContext());
		padding = (int) (padding * times);
		baseMargin = (int) (baseMargin * times);
		int restWidth = width - 2 * padding - 3 * baseMargin;
		int widthOne = restWidth / 4;
		baseMargin = (int) ((restWidth - widthOne * 4) / 3) + baseMargin;
		table.setPadding(padding, 0, padding, 0);
		String[] items = getResources().getStringArray(R.array.my_collection);
		TableRow tr = null;
		for (int i = 0; i < items.length; i++) {
			if (i % 4 == 0) {
				tr = new TableRow(MyCollectionActivity.this);
				table.addView(tr);
			}
			final ToggleButton tb = (ToggleButton) inflater.inflate(
					R.layout.item_toggle, null);
			buttonList.add(tb);
			tb.setTag(i);
			tb.setText(items[i]);
			tb.setTextOn(items[i]);
			tb.setTextOff(items[i]);
			/**
			 * 直接设置check不太好用，设置onclick测试可用
			 */
			tb.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					tb.setChecked(true);
					loadData();
					handlerTag = (Integer) (v.getTag()) + 1;
					url = ZhuoCommHelper.collectionUrls[handlerTag - 1];
				}
			});
			tb.setOnCheckedChangeListener(new OnCheckedChangeListener() {

				@Override
				public void onCheckedChanged(CompoundButton buttonView,
						boolean isChecked) {
					if (isChecked) {
						for (ToggleButton temp : buttonList) {
							if (temp.getTag().equals(buttonView.getTag())) {
								buttonView.setTextColor(Color.rgb(0, 72, 255));
							} else {
								temp.setChecked(false);
							}
						}
					} else {
						buttonView.setTextColor(Color.rgb(52, 52, 52));
					}
				}
			});
			tr.addView(tb);
			TableRow.LayoutParams trlp = new TableRow.LayoutParams(widthOne,
					TableRow.LayoutParams.WRAP_CONTENT);
			trlp.rightMargin = baseMargin;
			trlp.bottomMargin = baseMargin * 3 / 8;
			trlp.topMargin = baseMargin * 3 / 8;
			tb.setLayoutParams(trlp);
		}
	}

	private void loadData() {
		if (pullDownView.startLoadData()) {
			mDatas.clear();
			mPage = 0;
			mAdapter.notifyDataSetChanged();
//			appClientLef.getVedioList(tutorId, typeId, mPage, 5, uiHandler,
//					MsgTagVO.DATA_LOAD, GrouthListActivity.this, true, null,
//					null);
		}

	}

	private void loadMore() {
//		appClientLef.getVedioList(tutorId, typeId, mPage, 5, uiHandler,
//				MsgTagVO.DATA_MORE, GrouthListActivity.this, true, null, null);
	}

	Handler uiHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			ResultVO res;
			if (JsonHandler.checkResult((String) msg.obj,
					MyCollectionActivity.this)) {
				res = JsonHandler.parseResult((String) msg.obj);
			} else {
				return;
			}
			String data = res.getData();
			switch (msg.what) {
			case radio:
				mAdapter = new AudioAdapter(mContext, mDatas,
						R.layout.item_radio);
				listView.setAdapter(mAdapter);
				listView.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						// TODO Auto-generated method stub
						if (mAdapter != null)
							((AudioAdapter) mAdapter).stop();
						Intent intent = new Intent(MyCollectionActivity.this,
								AudioDetailActivity.class);
						intent.putExtra("id", mDatas.get(position - 1).getId());
						startActivity(intent);
					}
				});
				break;
			case vedio:
				final ArrayList<GrouthVedio> list = JsonHandler_Lef
						.parseGrouthVedioList(data);
				mAdapter = new GrouthAdapter(mContext, list,
						R.layout.item_growth);
				listView.setAdapter(mAdapter);
				listView.setOnItemClickListener(new OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						// TODO Auto-generated method stub
						Intent intent = new Intent(MyCollectionActivity.this,
								VedioActivity.class);
						intent.putExtra("id", list.get(position - 1).getId());
						startActivity(intent);
					}
				});
				break;
			case quan:
				final ArrayList<QuanVO> list2 = JsonHandler_Lef
						.parseQuanList(data);
				mAdapter = new QuanListAdapter(mContext, list2);
				listView.setAdapter(mAdapter);
				listView.setOnItemClickListener(new OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						// TODO Auto-generated method stub
						Intent intent = new Intent(MyCollectionActivity.this,
								QuanDetailActivity.class);
						intent.putExtra("id", list2.get(position - 1).getGroupid());
						startActivity(intent);
					}
				});
				break;
			default:
				mAdapter = new AudioAdapter(mContext, mDatas,
						R.layout.item_radio);
				listView.setAdapter(mAdapter);
				listView.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						// TODO Auto-generated method stub
						if (mAdapter != null)
							((AudioAdapter) mAdapter).stop();
						Intent intent = new Intent(MyCollectionActivity.this,
								AudioDetailActivity.class);
						intent.putExtra("id", mDatas.get(position - 1).getId());
						startActivity(intent);
					}
				});
				break;
			}
		};
	};
}
