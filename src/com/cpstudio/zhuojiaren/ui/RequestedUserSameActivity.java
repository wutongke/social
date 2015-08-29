package com.cpstudio.zhuojiaren.ui;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.cpstudio.zhuojiaren.BaseActivity;
import com.cpstudio.zhuojiaren.JiarenActiveActivity;
import com.cpstudio.zhuojiaren.MsgDetailActivity;
import com.cpstudio.zhuojiaren.R;
import com.cpstudio.zhuojiaren.adapter.ZhuoNearByUserListAdatper;
import com.cpstudio.zhuojiaren.adapter.ZhuoUserListAdapter2;
import com.cpstudio.zhuojiaren.helper.JsonHandler;
import com.cpstudio.zhuojiaren.helper.ResHelper;
import com.cpstudio.zhuojiaren.helper.ZhuoConnHelper;
import com.cpstudio.zhuojiaren.model.BaseCodeData;
import com.cpstudio.zhuojiaren.model.City;
import com.cpstudio.zhuojiaren.model.MsgTagVO;
import com.cpstudio.zhuojiaren.model.UserAndCollection;
import com.cpstudio.zhuojiaren.model.position;
import com.cpstudio.zhuojiaren.util.CommonAdapter;
import com.cpstudio.zhuojiaren.util.CommonUtil;
import com.cpstudio.zhuojiaren.util.ViewHolder;
import com.cpstudio.zhuojiaren.widget.PullDownView;
import com.cpstudio.zhuojiaren.widget.PullDownView.OnPullDownListener;
import com.cpstudui.zhuojiaren.lz.DynamicDetailActivity;
/**
 * LZUserSameActivity中已经实现，此类不需要，问题是LZUserSameActivity中的URL：getFollowReq和此类中的URL：getFriendReq是重复了么？
 * @author lz
 *
 */
public class RequestedUserSameActivity extends BaseActivity implements
		OnPullDownListener, OnItemClickListener {
	private ListView mListView;
	private CommonAdapter mAdapter;
	private PullDownView mPullDownView;

	private ArrayList<UserAndCollection> mList = new ArrayList<UserAndCollection>();
	List<position> posList;
	private String uid = null;
	private ZhuoConnHelper mConnHelper = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_same_city);
		mConnHelper = ZhuoConnHelper.getInstance(getApplicationContext());
		posList = mConnHelper.getBaseDataSet().getPosition();
		Intent intent = getIntent();
		initTitle();
		uid = ResHelper.getInstance(getApplicationContext()).getUserid();
		mPullDownView = (PullDownView) findViewById(R.id.pull_down_view);
		mPullDownView.initHeaderViewAndFooterViewAndListView(this,
				R.layout.listview_header5);

		mPullDownView.setOnPullDownListener(this);
		mListView = mPullDownView.getListView();
		mListView.setOnItemClickListener(this);
		mAdapter = new CommonAdapter<UserAndCollection>(
				RequestedUserSameActivity.this, mList,
				R.layout.item_requested_user_list) {

			@Override
			public void convert(ViewHolder helper, UserAndCollection item) {
				// TODO Auto-generated method stub
				helper.setText(R.id.req_name, item.getName());
				int posCode = item.getPosition();
				if (posList != null && posCode >= 1
						&& posCode <= posList.size()) {
					helper.setText(R.id.req_pos, posList.get(posCode - 1)
							.getContent());
				}
				helper.setText(R.id.req_company, item.getCompany());
				helper.setImageResource(R.id.req_accept, R.drawable.cart_choise, new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						
					}
				});
			}
		};
		mListView.setAdapter(mAdapter);
		mPullDownView.setShowHeader();
		mPullDownView.setShowFooter(false);
		initData();
		loadData();
	}

	private void loadData() {
		// TODO Auto-generated method stub
		if (mPullDownView.startLoadData()) {
			// mList.clear();
			// mAdapter.notifyDataSetChanged();
			if (CommonUtil.getNetworkState(getApplicationContext()) == 2) {
				// ArrayList<Dynamic> list = infoFacade.getByPage(mPage);
				// Message msg = mUIHandler.obtainMessage(MsgTagVO.DATA_LOAD);
				// msg.obj = list;
				// msg.sendToTarget();
			} else {
				mConnHelper.getFriendReq(mUIHandler, MsgTagVO.DATA_LOAD);
			}
		}
	}

	private void updateItemList(ArrayList<UserAndCollection> list,
			boolean refresh, boolean append) {
		if (!list.isEmpty()) {
			mPullDownView.hasData();
			if (!append) {
				mList.clear();
			}
			mList.addAll(list);
			mAdapter.notifyDataSetChanged();
			if (mList.size() > 0) {
				// mLastId = mList.get(mList.size() - 1).getStatusid();
			}
		} else {
			mPullDownView.noData(!refresh);
		}
	}

	@SuppressLint("HandlerLeak")
	private Handler mUIHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MsgTagVO.DATA_LOAD: {
				ArrayList<UserAndCollection> list = new ArrayList<UserAndCollection>();
				boolean loadState = false;
				if (msg.obj instanceof ArrayList) {
					list = (ArrayList<UserAndCollection>) msg.obj;
				} else {
					if (msg.obj != null && !msg.obj.equals("")) {
						loadState = true;
						JsonHandler nljh = new JsonHandler((String) msg.obj,
								getApplicationContext());
						list = nljh.parseUserCollection();
						if (!list.isEmpty()) {
							// infoFacade.update(list);
						}
					}
				}
				mPullDownView.finishLoadData(loadState);
				updateItemList(list, true, false);
				break;
			}
			case MsgTagVO.DATA_REFRESH: {
				boolean loadState = false;
				if (msg.obj != null && !msg.obj.equals("")) {
					loadState = true;
					JsonHandler nljh = new JsonHandler((String) msg.obj,
							getApplicationContext());
					ArrayList<UserAndCollection> list = nljh
							.parseUserCollection();
					updateItemList(list, false, false);
				}
				mPullDownView.RefreshComplete(loadState);
				break;
			}
			}
		}
	};

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		if (id != -1) {
			//
			// Intent i = new Intent();
			// i.setClass(RequestedUserSameActivity.this,
			// DynamicDetailActivity.class);
			// i.putExtra("msgid", (String) view.getTag(R.id.tag_id));
			// startActivity(i);
		}
	}

	@Override
	public void onRefresh() {
		loadData();
	}

	@Override
	public void onMore() {
	}

	private void initData() {
		title.setText(R.string.label_active_reuqest_card);
		function.setText(R.string.clear);
		function.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mList.clear();
				mAdapter.notifyDataSetChanged();
			}
		});
	}

	int getCityCodeByName(String cName) {
		int code = 1;
		List<City> cityList = mConnHelper.getCitys();
		for (City item : cityList) {
			if (item.getCityName().equals(cName)) {
				code = Integer.parseInt(item.getCityId());
				return code;
			}
		}
		return code;
	}
}
