package com.cpstudio.zhuojiaren;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.ButterKnife;
import butterknife.InjectView;

import com.cpstudio.zhuojiaren.helper.JsonHandler;
import com.cpstudio.zhuojiaren.helper.ZhuoConnHelper;
import com.cpstudio.zhuojiaren.model.MsgTagVO;
import com.cpstudio.zhuojiaren.model.ResourceGXVO;
import com.cpstudio.zhuojiaren.model.ResultVO;
import com.cpstudio.zhuojiaren.util.CommonUtil;
import com.cpstudio.zhuojiaren.widget.ListViewFooter;
import com.cpstudui.zhuojiaren.lz.GongXuDetailActivity;
import com.cpstudui.zhuojiaren.lz.MyResListAdapterListAdapter;

public class CardAddUserResourceActivity extends  BaseActivity implements
		OnItemClickListener {
	@InjectView(R.id.activity_title)
	TextView tvTitle;
	@InjectView(R.id.activity_back)
	TextView tvBack;
	@InjectView(R.id.activity_function)
	TextView tvManage;
	
//	@InjectView(R.id.buttonManage)
//	TextView buttonManage;

	
	@InjectView(R.id.lt_pub_res)
	View lt_pub_res;


	private ListView mListView;
	private MyResListAdapterListAdapter mAdapter;
	private ArrayList<ResourceGXVO> mList = new ArrayList<ResourceGXVO>();
	private ZhuoConnHelper mConnHelper = null;
	private int mType = 0;
	private int mPage = 0;
	private String userid = "";
	private ListViewFooter mListViewFooter = null;
	// 是否处于管理
	boolean isManaging = false;
	String myId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_card_add_user_resource);
		ButterKnife.inject(this);
		mConnHelper = ZhuoConnHelper.getInstance(getApplicationContext());
		Intent i = getIntent();
		mType = i.getIntExtra(CardEditActivity.EDIT_RES_STR1, 0);
		userid = i.getStringExtra(CardEditActivity.EDIT_RES_STR2);
		myId=mConnHelper.getUserid();
		if (mType == 0)
			tvTitle.setText(R.string.mygong);
		else
			tvTitle.setText(R.string.myxu);
		
		mListView = (ListView) findViewById(R.id.listView);
		mAdapter = new MyResListAdapterListAdapter(
				CardAddUserResourceActivity.this, mList);
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
		tvManage.setText(getString(R.string.label_manage));
		// 当打开的不是我自己的名片时需要隐藏管理按钮
		if (!userid.equals(myId) ) {
			tvManage.setVisibility(View.GONE);
			lt_pub_res.setVisibility(View.GONE);
			isManaging = false;
		}
	}

	public void offManager() {
		isManaging = false;
		if (mAdapter != null) {
			mAdapter.setManaging(false);
			mAdapter.getmSelectedList().clear();
			mAdapter.notifyDataSetChanged();
		}
	}

	private void initClick() {
		tvBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				CardAddUserResourceActivity.this.finish();
			}
		});

		tvManage.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (!isManaging) {
					tvManage.setText(getString(R.string.DELETE));
					isManaging = true;
					mAdapter.setManaging(true);
				} else {
					deleteSelected();
				}
			}
		});
		lt_pub_res.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent i = new Intent(CardAddUserResourceActivity.this,
						PublishResourceActivity.class);
				startActivityForResult(i, MsgTagVO.DATA_REFRESH);
			}
		});
		
	}
	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		if (event.getKeyCode() == KeyEvent.KEYCODE_BACK && isManaging) {
			offManager();
			return true;
		}
		return super.dispatchKeyEvent(event);
	}
	protected void deleteSelected() {
		// TODO Auto-generated method stub\
		// 解散选中的我创建的圈子
		ArrayList<ResourceGXVO> deletelist = (ArrayList<ResourceGXVO>) mAdapter
				.getmSelectedList();
		if (deletelist == null || deletelist.size() < 1) {
			CommonUtil.displayToast(CardAddUserResourceActivity.this,
					R.string.selected_null);
			return;
		}

		StringBuilder ids = new StringBuilder();
		for (ResourceGXVO item : deletelist) {
			if(ids.length()>0)
				ids.append(",");
			ids.append(item.getSdid());
		}
		mConnHelper.deleteGongxu(ids.toString(), mUIHandler, MsgTagVO.MSG_DEL,
				CardAddUserResourceActivity.this);
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
				ArrayList<ResourceGXVO> list = (ArrayList<ResourceGXVO>) nljh
						.parseGongxuList();
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
				ResultVO res;
				if (JsonHandler.checkResult((String) msg.obj,
						CardAddUserResourceActivity.this)) {
//					res = JsonHandler.parseResult((String) msg.obj);
					updateItemList((String)msg.obj, true, false);
				} else {
					CommonUtil.displayToast(CardAddUserResourceActivity.this,
							R.string.data_error);
					return;
				}
				
				break;
			}
			case MsgTagVO.DATA_MORE: {
				mListViewFooter.finishLoading();
				ResultVO res;
				if (JsonHandler.checkResult((String) msg.obj,
						CardAddUserResourceActivity.this)) {
					res = JsonHandler.parseResult((String) msg.obj);
				} else {
					CommonUtil.displayToast(CardAddUserResourceActivity.this,
							R.string.data_error);
					return;
				}
				String data = res.getData();
				updateItemList(data, false, true);
				break;
			}
			case MsgTagVO.MSG_DEL:
				if (JsonHandler.checkResult((String) msg.obj,
						CardAddUserResourceActivity.this)) {
					CommonUtil.displayToast(CardAddUserResourceActivity.this,
							"删除成功");
					// 退出管理，重新下载数据
					offManager();
					loadData();
				} else {
					CommonUtil.displayToast(CardAddUserResourceActivity.this,
							R.string.data_error);
					return;
				}
				break;
			}
		}
	};

	private void loadData() {
		if (mListViewFooter.startLoading()) {
			mPage = 0;
			mAdapter.notifyDataSetChanged();
			mConnHelper.getGongXuList(String.valueOf(mType),null, null, mPage, 5,
					mUIHandler, MsgTagVO.DATA_LOAD,
					CardAddUserResourceActivity.this, true, null, null, userid);
		}
	}

	private void loadMore() {
		if (mListViewFooter.startLoading()) {
			mConnHelper.getGongXuList(String.valueOf(mType),null, null, mPage, 5,
					mUIHandler, MsgTagVO.DATA_MORE,
					CardAddUserResourceActivity.this, true, null, null, userid);
		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		if (arg2 != -1) {
			Intent i = new Intent(CardAddUserResourceActivity.this,
					GongXuDetailActivity.class);
			i.putExtra("msgid",mList.get(arg2).getSdid());
			startActivity(i);
		}
	}
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == Activity.RESULT_OK) {
			loadData();
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
}
