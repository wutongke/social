package com.cpstudui.zhuojiaren.lz;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.RadioGroup;
import butterknife.ButterKnife;
import butterknife.InjectView;

import com.cpstudio.zhuojiaren.BaseActivity;
import com.cpstudio.zhuojiaren.R;
import com.cpstudio.zhuojiaren.adapter.TypedStoreGoodsListAdapter;
import com.cpstudio.zhuojiaren.helper.AppClientLef;
import com.cpstudio.zhuojiaren.helper.JsonHandler;
import com.cpstudio.zhuojiaren.model.GoodsVO;
import com.cpstudio.zhuojiaren.model.MsgTagVO;
import com.cpstudio.zhuojiaren.ui.GoodsDetailLActivity;
import com.cpstudio.zhuojiaren.widget.PullDownView;
import com.cpstudio.zhuojiaren.widget.PullDownView.OnPullDownListener;

/**
 * 某一类型的商品列表
 * 
 * @author lz
 * 
 */
public class GoodsTypedListActivity extends BaseActivity implements
		OnItemClickListener, OnPullDownListener {
	@InjectView(R.id.radioGroupGender)
	RadioGroup rgSex;
	PullDownView mPullDownView;
	ListView mListView;
	private AppClientLef mConnHelper = null;
	private int mPage = 1;
	//商品类别
	private int type = 1;
	private String typeName = null;

	private TypedStoreGoodsListAdapter mAdapter = null;
	private ArrayList<GoodsVO> mList = new ArrayList<GoodsVO>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_goods_type_listed);
		initTitle();
		ButterKnife.inject(this);
		
		Intent intent = getIntent();
		type = intent.getIntExtra("type", 1);
		typeName = intent.getStringExtra("typeName");
		
		function.setVisibility(View.GONE);
		function.setText(R.string.label_filter2);
		// 此处需要根据传过来的参数修改
		if(typeName!=null)
		title.setText(typeName);
		initView();
		mConnHelper = AppClientLef.getInstance(getApplicationContext());

		mPullDownView = (PullDownView) findViewById(R.id.pull_down_view);
		mPullDownView.initHeaderViewAndFooterViewAndListView(this,
				R.layout.listview_header2);
		mPullDownView.setOnPullDownListener(this);
		mListView = mPullDownView.getListView();
		mListView.setOnItemClickListener(this);
		mAdapter = new TypedStoreGoodsListAdapter(this, mList);
		mListView.setAdapter(mAdapter);
		mPullDownView.setShowHeader();
		mPullDownView.setShowFooter(false);
		loadData();
	}

	private void initView() {
		// TODO Auto-generated method stub

		rgSex.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				switch (checkedId) {
				case R.id.radioFemale:
					//
					break;
				case R.id.radioMale:

					break;
				}
			}
		});

	}

	@SuppressLint("HandlerLeak")
	private Handler mUIHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			mPullDownView.finishLoadData(true);
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
		}
	};

	private void updateItemList(String data, boolean refresh, boolean append) {
		if (data != null && !data.equals("")) {
			JsonHandler nljh = new JsonHandler(data, getApplicationContext());
			ArrayList<GoodsVO> list = nljh.parseGoodsList();
			if (!list.isEmpty()) {
				mPullDownView.hasData();
				if (!append) {
					mList.clear();
				}
				mPage++;
				mList.addAll(list);
				mAdapter.notifyDataSetChanged();
			} else {
				mPullDownView.noData(!refresh);
			}
		}

	}

	private void loadData() {
		mList.clear();
		mPage = 0;
		mAdapter.notifyDataSetChanged();
		mConnHelper.getGoodsList(mPage, 5, mUIHandler, MsgTagVO.DATA_LOAD,
				this, type+"",null,null);

	}

	private void loadMore() {
		mConnHelper.getGoodsList(mPage, 5, mUIHandler, MsgTagVO.DATA_MORE,
				this, type+"",null,null);
	}


	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		if (arg3 != -1) {
			Intent i = new Intent(GoodsTypedListActivity.this,
					GoodsDetailLActivity.class);
			i.putExtra("goodsId", (String) arg1.getTag(R.id.tag_id));
			startActivity(i);
		}
	}

	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub
		loadData();
	}

	@Override
	public void onMore() {
		// TODO Auto-generated method stub
		
//		if (CommonUtil.getNetworkState(getApplicationContext()) == 2) {
//			ArrayList<ZhuoInfoVO> list = infoFacade.getByPage(mPage);
//			Message msg = mUIHandler.obtainMessage(MsgTagVO.DATA_MORE);
//			msg.obj = list;
//			msg.sendToTarget();
//		} else {
//			String params = ZhuoCommHelper.getUrlMsgList();
//			params += "?pageflag=" + "1";
//			params += "&reqnum=" + "10";
//			params += "&lastid=" + mLastId;
//			params += "&type=" + mType;
//			params += "&gongxutype=" + "0";
//			params += "&from=" + "6";
//			params += "&uid=" + mUid;
//			mConnHelper.getFromServer(params, mUIHandler, MsgTagVO.DATA_MORE);
//		}
//		
		loadMore();
	}
}
