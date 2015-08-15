package com.cpstudio.zhuojiaren.ui;

import java.util.ArrayList;

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
import com.cpstudio.zhuojiaren.MsgDetailActivity;
import com.cpstudio.zhuojiaren.R;
import com.cpstudio.zhuojiaren.adapter.ZhuoNearByUserListAdatper;
import com.cpstudio.zhuojiaren.adapter.ZhuoUserListAdapter2;
import com.cpstudio.zhuojiaren.helper.JsonHandler;
import com.cpstudio.zhuojiaren.helper.ResHelper;
import com.cpstudio.zhuojiaren.helper.ZhuoCommHelper;
import com.cpstudio.zhuojiaren.helper.ZhuoConnHelper;
import com.cpstudio.zhuojiaren.model.MsgTagVO;
import com.cpstudio.zhuojiaren.model.UserAndCollection;
import com.cpstudio.zhuojiaren.model.UserVO;
import com.cpstudio.zhuojiaren.util.CommonAdapter;
import com.cpstudio.zhuojiaren.util.CommonUtil;
import com.cpstudio.zhuojiaren.widget.PullDownView;
import com.cpstudio.zhuojiaren.widget.PullDownView.OnPullDownListener;

public class UserSameActivity extends BaseActivity implements
		OnPullDownListener, OnItemClickListener {
	private ListView mListView;
	private CommonAdapter mAdapter;
	private PullDownView mPullDownView;
	private ArrayList<UserAndCollection> mList = new ArrayList<UserAndCollection>();
	private String mSearchKey = null;
	private String mLastId = null;
	private String uid = null;
	private String mType = "1";
	private ZhuoConnHelper mConnHelper = null;

	private int requestCodeCity = 1;
	private int requestCodeIndustry = 2;
	private int requestCodeHoppy = 3;
	private int requestCodeTeacher = 4;

	// add by lz
	boolean isManaging = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_same_city);
		mConnHelper = ZhuoConnHelper.getInstance(getApplicationContext());
		Intent intent = getIntent();
		int type = intent.getIntExtra("type", 1);
		mType = String.valueOf(type);
		initTitle();
		switch (type) {
		case 1:
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
		case 2:
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
		case 3:
			title.setText(R.string.title_activity_group_type5);
			break;
		case 4:
			title.setText(R.string.title_activity_group_type4);
			function.setText(R.string.label_hobby);
			// 选择行业
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
		case 5:
			title.setText(R.string.title_activity_group_type6);
			function.setText(R.string.label_filter2);
			// 选择行业
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
		case 6:
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
						((ZhuoUserListAdapter2) mAdapter)
								.setIsManaging(isManaging);
				}
			});
			break;
		}
		String sk = intent.getStringExtra("mSearchKey");
		uid = ResHelper.getInstance(getApplicationContext()).getUserid();
		mPullDownView = (PullDownView) findViewById(R.id.pull_down_view);
		mPullDownView.initHeaderViewAndFooterViewAndListView(this,
				R.layout.listview_header5);
		mPullDownView.setOnPullDownListener(this);
		mListView = mPullDownView.getListView();
		mListView.setOnItemClickListener(this);
		if (type != 3)
			mAdapter = new ZhuoUserListAdapter2(UserSameActivity.this, mList,
					R.layout.item_zhuouser_list2);
		else
			mAdapter = new ZhuoNearByUserListAdatper(UserSameActivity.this,
					mList, R.layout.item_zhuouser_near_list);
		mListView.setAdapter(mAdapter);
		mPullDownView.setShowHeader();
		mPullDownView.setShowFooter(false);
		initClick();
		if (sk != null && !sk.equals("")) {
			mSearchKey = sk;
			((EditText) findViewById(R.id.editTextSearch)).setText(sk);
		}
		// loadData();
		// test
		UserAndCollection a = new UserAndCollection();
		a.setIsCollection("1");
		a.setDistance("1公里");
		UserVO user = new UserVO();
		user.setUsername("张来才");
		user.setPost("php董事");
		user.setUheader("https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=2222979786,259610352&fm=116&gp=0.jpg");
		user.setCompany("php902大集团");
		a.setUser(user);
		UserAndCollection b = new UserAndCollection();
		b.setIsCollection("0");
		b.setDistance("1.5公里");
		b.setUser(user);
		mList.add(a);
		mList.add(b);
		mList.add(a);
		mList.add(b);
		mList.add(a);
		mList.add(b);
		mList.add(a);
		mList.add(b);
		mList.add(a);
		mList.add(b);
		mList.add(a);
		mList.add(b);
		mList.add(a);
		mList.add(b);
		mAdapter.notifyDataSetChanged();
	}

	private void updateItemList(String data, boolean refresh, boolean append) {
		try {
			if (data != null && !data.equals("")) {
				JsonHandler nljh = new JsonHandler(data,
						getApplicationContext());
				ArrayList<UserAndCollection> list = new ArrayList<UserAndCollection>();
				// =nljh.parseZhuoInfoList();
				if (!list.isEmpty()) {
					mPullDownView.hasData();
					if (!append) {
						mList.clear();
					}
					mList.addAll(list);
					mAdapter.notifyDataSetChanged();
					if (mList.size() > 0) {
						mLastId = mList.get(mList.size() - 1).getId();
					}
				} else {
					if (refresh && mSearchKey != null && !mSearchKey.equals("")) {
						CommonUtil.displayToast(getApplicationContext(),
								R.string.error18);
					}
					mPullDownView.noData(!refresh);
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
				boolean loadState = false;
				if (msg.obj != null && !msg.obj.equals("")) {
					loadState = true;
					updateItemList((String) msg.obj, true, false);
				}
				mPullDownView.finishLoadData(loadState);
				break;
			}
			case MsgTagVO.DATA_REFRESH: {
				boolean loadState = false;
				if (msg.obj != null && !msg.obj.equals("")) {
					loadState = true;
					updateItemList((String) msg.obj, false, false);
				}
				mPullDownView.RefreshComplete(loadState);
				break;
			}
			case MsgTagVO.DATA_MORE: {
				mPullDownView.notifyDidMore();
				updateItemList((String) msg.obj, false, true);
				break;
			}
			}

		}

	};

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		if (id != -1) {
			Intent i = new Intent(UserSameActivity.this,
					MsgDetailActivity.class);
			i.putExtra("msgid", (String) view.getTag(R.id.tag_id));
			startActivity(i);
		}
	}

	@Override
	public void onRefresh() {
		String params = ZhuoCommHelper.getUrlMsgList();
		params += "?pageflag=" + "0";
		params += "&reqnum=" + "20";
		params += "&lastid=" + "0";
		params += "&type=" + "0";
		if (null != mSearchKey) {
			params += "&key=" + mSearchKey.trim();
		}
		params += "&gongxutype=" + "0";
		params += "&from=" + mType;
		params += "&uid=" + uid;
		mConnHelper.getFromServer(params, mUIHandler, MsgTagVO.DATA_REFRESH);
	}

	@Override
	public void onMore() {
		String params = ZhuoCommHelper.getUrlMsgList();
		params += "?pageflag=" + "1";
		params += "&reqnum=" + "20";
		params += "&lastid=" + mLastId;
		params += "&type=" + "0";
		if (null != mSearchKey) {
			params += "&key=" + mSearchKey.trim();
		}
		params += "&gongxutype=" + "0";
		params += "&from=" + mType;
		params += "&uid=" + uid;
		mConnHelper.getFromServer(params, mUIHandler, MsgTagVO.DATA_MORE);
	}

	private void loadData() {
		if (mPullDownView.startLoadData()) {
			mList.clear();
			mAdapter.notifyDataSetChanged();
			String params = ZhuoCommHelper.getUrlMsgList();
			params += "?pageflag=" + "0";
			params += "&reqnum=" + "20";
			params += "&lastid=" + "0";
			params += "&type=" + "0";
			if (null != mSearchKey) {
				params += "&key=" + mSearchKey.trim();
			}
			params += "&gongxutype=" + "0";
			params += "&from=" + mType;
			params += "&uid=" + uid;
			mConnHelper.getFromServer(params, mUIHandler, MsgTagVO.DATA_LOAD);
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
					loadData();
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
				if (searchView.getText().toString().equals("")) {
					delSearch.setVisibility(View.GONE);
				} else {
					delSearch.setVisibility(View.VISIBLE);
				}
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
				function.setText(data.getStringExtra("city"));
				break;
			case 2:
				TextView view1 = (TextView) findViewById(R.id.lvh5_text);
				view1.setVisibility(View.VISIBLE);
				view1.setText("当前选择:" + data.getStringExtra("industry"));
				break;
			case 3:
				TextView view2 = (TextView) findViewById(R.id.lvh5_text);
				view2.setVisibility(View.VISIBLE);
				view2.setText("当前选择:" + data.getStringExtra("industry"));
				break;
			case 4:
				TextView view3 = (TextView) findViewById(R.id.lvh5_text);
				view3.setVisibility(View.VISIBLE);
				view3.setText("当前选择:" + data.getStringExtra("industry"));
				break;
			}
		}
	}
}
