package com.cpstudio.zhuojiaren.ui;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
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
import com.cpstudio.zhuojiaren.R;
import com.cpstudio.zhuojiaren.adapter.AudioAdapter;
import com.cpstudio.zhuojiaren.adapter.CrowdFundingAdapter;
import com.cpstudio.zhuojiaren.model.CrowdFundingVO;
import com.cpstudio.zhuojiaren.model.RecordVO;
import com.cpstudio.zhuojiaren.util.DeviceInfoUtil;
import com.cpstudio.zhuojiaren.widget.PullDownView;
import com.cpstudio.zhuojiaren.widget.PullDownView.OnPullDownListener;

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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_collection);
		ButterKnife.inject(this);
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
				loadData();
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
		//收藏列表，初始tablelayout
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
					initData((Integer)(v.getTag()));
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
		// TODO Auto-generated method stub
		// test
		RecordVO g = new RecordVO();
		g.setName("张来才");
		g.setId("21");
		g.setLength("12:12:13");
		g.setUsers("张来才");
		g.setDate("2015.6.23");
		g.setPath("http://yinyueshiting.baidu.com/data2/music/122878621/648618151200320.mp3?xcode=f8e09d23004f8a09b123be2e4a685e68");
		pullDownView.finishLoadData(true);
		pullDownView.hasData();
		mDatas.add(g);
		mAdapter.notifyDataSetChanged();
	}
	private void initData(Integer param){
		switch(param){
		case 7:
			mAdapter = new AudioAdapter(mContext,
					mDatas, R.layout.item_radio);
			listView.setAdapter(mAdapter);
			listView.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(
						AdapterView<?> parent,
						View view, int position, long id) {
					// TODO Auto-generated method stub
					if (mAdapter != null)
						((AudioAdapter) mAdapter)
								.stop();
					Intent intent = new Intent(
							MyCollectionActivity.this,
							AudioDetailActivity.class);
					intent.putExtra("id",
							mDatas.get(position - 1)
									.getId());
					startActivity(intent);
				}
			});
			break;
		case 6:
			final ArrayList<CrowdFundingVO> mListDatas=new ArrayList<CrowdFundingVO>();
			CrowdFundingVO test = new CrowdFundingVO();
//			test.setFundingId("123");
//			test.setName("asdf");
//			test.setMinPrice("5");
//			test.setMoneyGet("8000");
			pullDownView.finishLoadData(true);
			pullDownView.hasData();
			mListDatas.add(test);
			mAdapter = new CrowdFundingAdapter(mContext, mListDatas, R.layout.item_crowdfunding);
			listView.setAdapter(mAdapter);
			listView.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					// TODO Auto-generated method stub
					Intent intent = new Intent(MyCollectionActivity.this,CrowdFundingDetailActivity.class);
					intent.putExtra(CrowdFundingVO.CROWDFUNDINGID,mListDatas.get(position-1).getId());
					startActivity(intent);
				}
			});
			break;
			default:
				mAdapter = new AudioAdapter(mContext,
						mDatas, R.layout.item_radio);
				listView.setAdapter(mAdapter);
				listView.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(
							AdapterView<?> parent,
							View view, int position, long id) {
						// TODO Auto-generated method stub
						if (mAdapter != null)
							((AudioAdapter) mAdapter)
									.stop();
						Intent intent = new Intent(
								MyCollectionActivity.this,
								AudioDetailActivity.class);
						intent.putExtra("id",
								mDatas.get(position - 1)
										.getId());
						startActivity(intent);
					}
				});
				break;
		}
	}
}
