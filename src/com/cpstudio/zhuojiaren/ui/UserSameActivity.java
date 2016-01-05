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

import com.cpstudio.zhuojiaren.R;
import com.cpstudio.zhuojiaren.adapter.ZhuoNearByUserListAdatper;
import com.cpstudio.zhuojiaren.adapter.ZhuoUserListAdapter;
import com.cpstudio.zhuojiaren.helper.JsonHandler;
import com.cpstudio.zhuojiaren.helper.ResHelper;
import com.cpstudio.zhuojiaren.helper.ConnHelper;
import com.cpstudio.zhuojiaren.model.City;
import com.cpstudio.zhuojiaren.model.MsgTagVO;
import com.cpstudio.zhuojiaren.model.UserAndCollection;
import com.cpstudio.zhuojiaren.util.CommonAdapter;
import com.cpstudio.zhuojiaren.util.CommonUtil;
import com.cpstudio.zhuojiaren.widget.PullDownView;
import com.cpstudio.zhuojiaren.widget.PullDownView.OnPullDownListener;

public class UserSameActivity extends BaseActivity implements
		OnPullDownListener, OnItemClickListener {
	private ListView mListView;
	private CommonAdapter mAdapter;
	private PullDownView mPullDownView;

	private ArrayList<UserAndCollection> mListAll = new ArrayList<UserAndCollection>();
	private ArrayList<UserAndCollection> mList = new ArrayList<UserAndCollection>();

	// 关键字
	private String mSearchKey = null;
	// 同行、同趣有多个匹配项
	private String itemId = "";
	private String itemName = "";

	private String mLastId = null;
	private String uid = null;
	int type;
	private String mType = "1";
	private ConnHelper mConnHelper = null;
	int mPage = 0, pageSize = 5;
	private int requestCodeCity = 1;
	private int requestCodeIndustry = 2;
	private int requestCodeHoppy = 3;
	private int requestCodeTeacher = 4;
	// add by lz
	boolean isManaging = false;
	TextView tvItem;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_same_city);
		mConnHelper = ConnHelper.getInstance(getApplicationContext());

		Intent intent = getIntent();
		type = intent.getIntExtra("type", 1);
		mType = String.valueOf(type);
		initTitle();

		String sk = intent.getStringExtra("mSearchKey");
		uid = ResHelper.getInstance(getApplicationContext()).getUserid();
		mPullDownView = (PullDownView) findViewById(R.id.pull_down_view);
		mPullDownView.initHeaderViewAndFooterViewAndListView(this,
				R.layout.listview_header5);

		mPullDownView.setOnPullDownListener(this);
		mListView = mPullDownView.getListView();
		mListView.setOnItemClickListener(this);
		if (type != 3)
			mAdapter = new ZhuoUserListAdapter(UserSameActivity.this, mList,
					R.layout.item_zhuouser_list2, mUIHandler);
		else
			mAdapter = new ZhuoNearByUserListAdatper(UserSameActivity.this,
					mList, R.layout.item_zhuouser_near_list);
		mListView.setAdapter(mAdapter);
		mPullDownView.setHideHeader();
		mPullDownView.setShowFooter(false);
		initData();
		initClick();
		if (sk != null && !sk.equals("")) {
			mSearchKey = sk;
			((EditText) findViewById(R.id.editTextSearch)).setText(sk);
		}

		loadData();
		// test
		// UserAndCollection a = new UserAndCollection();
		// a.setIsCollection("1");
		// a.setDistance("1公里");
		// UserVO user = new UserVO();
		// user.setUsername("张来才");
		// user.setPost("php董事");
		// user.setUheader("https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=2222979786,259610352&fm=116&gp=0.jpg");
		// user.setCompany("php902大集团");
		// a.setUser(user);
		// UserAndCollection b = new UserAndCollection();
		// b.setIsCollection("0");
		// b.setDistance("1.5公里");
		// b.setUser(user);
		// mList.add(a);
		// mList.add(b);
		// mList.add(a);
		// mList.add(b);
		// mList.add(a);
		// mList.add(b);
		// mList.add(a);
		// mList.add(b);
		// mList.add(a);
		// mList.add(b);
		// mList.add(a);
		// mList.add(b);
		// mList.add(a);
		// mList.add(b);
		// mAdapter.notifyDataSetChanged();
	}

	private void loadData() {
		// TODO Auto-generated method stub
		mPage = 0;
		if (mPullDownView.startLoadData()) {
			// mList.clear();
			// mAdapter.notifyDataSetChanged();

			if (CommonUtil.getNetworkState(getApplicationContext()) == 2) {
				// ArrayList<Dynamic> list = infoFacade.getByPage(mPage);
				// Message msg = mUIHandler.obtainMessage(MsgTagVO.DATA_LOAD);
				// msg.obj = list;
				// msg.sendToTarget();
			} else {
				
				if(type==8 || type==9 || type==10)
					mConnHelper.getMyStatusCard(mUIHandler,MsgTagVO.DATA_LOAD,type-8);
				else
					mConnHelper.getSameUser(mUIHandler, MsgTagVO.DATA_LOAD, type,
							mPage, pageSize, itemId);
			}
		}
	}

	private void updateItemList(ArrayList<UserAndCollection> list,
			boolean refresh, boolean append) {
		if (!list.isEmpty()) {
			// mPullDownView.hasData();
			if (!append) {
				mListAll.clear();
			}
			mListAll.addAll(list);
			// mAdapter.notifyDataSetChanged();
			if (mListAll.size() > 0) {
				// mLastId = mList.get(mList.size() - 1).getStatusid();
			}
			mPage++;
		} else {
			// mPullDownView.noData(!refresh);
		}
		filterData(refresh, append);
	}

	void filterData(boolean refresh, boolean append) {
		mList.clear();
		for (UserAndCollection item : mListAll) {
			String name = item.getName();
			if (mSearchKey == null || mSearchKey.equals("")
					|| name.indexOf(mSearchKey) != -1) {
				mList.add(item);
			}
		}
		if (!mList.isEmpty()) {
			mAdapter.notifyDataSetChanged();
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
			case MsgTagVO.DATA_MORE: {
				mPullDownView.notifyDidMore();
				ArrayList<UserAndCollection> list = new ArrayList<UserAndCollection>();
				if (msg.obj instanceof ArrayList) {
					list = (ArrayList<UserAndCollection>) msg.obj;
				} else {
					if (msg.obj != null && !msg.obj.equals("")) {
						JsonHandler nljh = new JsonHandler((String) msg.obj,
								getApplicationContext());
						list = nljh.parseUserCollection();
						if (!list.isEmpty()) {
							// infoFacade.update(list);
						}
					}
				}
				updateItemList(list, false, true);
				break;
			}
			case MsgTagVO.MSG_FOWARD: {
				if (JsonHandler.checkResult((String) msg.obj,
						getApplicationContext())) {
					CommonUtil.displayToast(UserSameActivity.this, "操作成功");
				}
				break;
			}
			}
		}
	};

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		if (position >=1) {
			Intent i = new Intent();
			i.setClass(UserSameActivity.this, ZhuoMaiCardActivity.class);
			i.putExtra("userid", mList.get(position-1).getUserid());
			startActivity(i);
		}
	}

	@Override
	public void onRefresh() {
		mPage = 0;
		loadData();
	}

	@Override
	public void onMore() {
		if (CommonUtil.getNetworkState(getApplicationContext()) == 2) {
			// ArrayList<ZhuoInfoVO> list = infoFacade.getByPage(mPage);
			// Message msg = mUIHandler.obtainMessage(MsgTagVO.DATA_MORE);
			// msg.obj = list;
			// msg.sendToTarget();
		} else {
			if(type!=8 && type!=9 && type!=10)
				mConnHelper.getSameUser(mUIHandler, MsgTagVO.DATA_MORE, type,
					mPage, pageSize, itemId);
		}
	}

	private void initData() {

		tvItem = (TextView) findViewById(R.id.lvh5_text);

		switch (type) {
		case 1:// 同城家人
			title.setText(R.string.title_activity_group_type2);
			function.setText(R.string.country);
			function.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					startActivityForResult(new Intent(UserSameActivity.this,
							CityChooseActivity.class), requestCodeCity);
				}
			});
			break;
		case 2:// 同行家人
			title.setText(R.string.title_activity_group_type3);
			function.setText(R.string.label_field);
			// 选择行业
			function.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent intent = new Intent(UserSameActivity.this,
							FieldSelectUserActivity.class);
					intent.putExtra("type", 1);
					startActivityForResult(intent, requestCodeIndustry);
				}
			});
			break;
		case 3:// /附近家人
			title.setText(R.string.title_activity_group_type5);
			break;
		case 4:// 同趣家人
			title.setText(R.string.title_activity_group_type4);
			function.setText(R.string.label_hobby);
			function.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent intent = new Intent(UserSameActivity.this,
							FieldSelectUserActivity.class);
					intent.putExtra("type", 2);
					startActivityForResult(intent, requestCodeHoppy);
				}
			});
			break;
		case 5:// 智慧导师
			title.setText(R.string.title_activity_group_type6);
			function.setText(R.string.label_filter2);
			function.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent intent = new Intent(UserSameActivity.this,
							FieldSelectUserActivity.class);
					intent.putExtra("type", 3);
					startActivityForResult(intent, requestCodeTeacher);
				}
			});
			break;
		case 6:// 所有家人
			title.setText(R.string.title_activity_group_type7);
			break;
		case 7:// 请求交换名片的人
			title.setText(R.string.label_active_reuqest_card); // 还需要显示人数

			break;
		case 8:
			title.setText(R.string.label_active_viewed);
			break;
		case 9:
			title.setText(R.string.label_active_collected_me);
			break;
		case 10:
			title.setText(R.string.label_active_zaned_me);
			break;
		case 12:// 我的人脉
			title.setText(R.string.renmai_my);
			function.setText(R.string.label_manage);
			// 我的人脉不显示“收藏”
			if (findViewById(R.id.ll_collect) != null)
				findViewById(R.id.ll_collect).setVisibility(View.GONE);
			function.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					isManaging = !isManaging;
					if (isManaging)
						function.setText(R.string.EXIT);
					else
						function.setText(R.string.label_manage);
					if (mAdapter != null)
						((ZhuoUserListAdapter) mAdapter)
								.setIsManaging(isManaging);
				}
			});
			break;
		}
	}

	private void initClick() {
		final EditText searchView = (EditText) findViewById(R.id.editTextSearch);
		final View delSearch = findViewById(R.id.imageViewDelSearch);
		searchView.setOnEditorActionListener(new OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId,
					KeyEvent event) {
				if (actionId == EditorInfo.IME_ACTION_SEARCH) {
					mSearchKey = v.getText().toString();
					InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
					imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
					filterData(false, true);
				}
				return false;
			}
		});
		searchView.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {
				mSearchKey = searchView.getText().toString();
				if (mSearchKey.equals("")) {
					delSearch.setVisibility(View.GONE);
				} else {
					delSearch.setVisibility(View.VISIBLE);
				}
				filterData(false, true);
			}
		});

		delSearch.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				searchView.setText("");
				if (mSearchKey != null && !mSearchKey.equals("")) {
					mSearchKey = "";
					loadData();
				}
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			switch (requestCode) {
			// 城市
			case 1:
				String cName = data.getStringExtra("city");
				int code = getCityCodeByName(cName);
				if (itemId == null || itemId.equals(""))
					itemId = String.valueOf(code);
				else
					itemId += "," + String.valueOf(code);
				if (itemName == null || itemName.equals(""))
					itemName = cName;
				else
					itemName += "," + cName;
				// function.setText(itemName);
				break;
			case 2:
			case 3:
			case 4:
				String name = data.getStringExtra("name");
				String id = data.getStringExtra("id");
				if (itemId == null || itemId.equals(""))
					itemId = id;
				else
					itemId += "," + id;
				if (itemName == null || itemName.equals(""))
					itemName = name;
				else
					itemName += "," + name;
				// function.setText(itemName);

				break;
			}
			tvItem.setVisibility(View.VISIBLE);
			tvItem.setText("当前选择:" + itemName);
			loadData();
		}
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
