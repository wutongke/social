package com.cpstudio.zhuojiaren;

import java.util.ArrayList;

import com.cpstudio.zhuojiaren.R;
import com.cpstudio.zhuojiaren.adapter.ZhuoUserListAdapter;
import com.cpstudio.zhuojiaren.helper.JsonHandler;
import com.cpstudio.zhuojiaren.helper.ResHelper;
import com.cpstudio.zhuojiaren.helper.ZhuoCommHelper;
import com.cpstudio.zhuojiaren.helper.ZhuoConnHelper;
import com.cpstudio.zhuojiaren.model.MsgTagVO;
import com.cpstudio.zhuojiaren.model.ZhuoInfoVO;
import com.cpstudio.zhuojiaren.util.CommonUtil;
import com.cpstudio.zhuojiaren.widget.PullDownView;
import com.cpstudio.zhuojiaren.widget.PullDownView.OnPullDownListener;

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
import android.widget.EditText;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView.OnEditorActionListener;
import android.widget.TextView;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;

public class UserSameActivity extends Activity implements OnPullDownListener,
		OnItemClickListener {
	private ListView mListView;
	private ZhuoUserListAdapter mAdapter;
	private PullDownView mPullDownView;
	private ArrayList<ZhuoInfoVO> mList = new ArrayList<ZhuoInfoVO>();
	private String mSearchKey = null;
	private String mLastId = null;
	private String uid = null;
	private String mType = "1";
	private ZhuoConnHelper mConnHelper = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_same_city);
		mConnHelper = ZhuoConnHelper.getInstance(getApplicationContext());
		Intent intent = getIntent();
		int type = intent.getIntExtra("type", 1);
		mType = String.valueOf(type);
		TextView textViewNav = (TextView) findViewById(R.id.userNameShow);
		if (type == 1) {
			textViewNav.setText(R.string.title_activity_group_type2);
		} else if (type == 2) {
			textViewNav.setText(R.string.title_activity_group_type3);
		} else if (type == 3) {
			textViewNav.setText(R.string.title_activity_group_type4);
		}
		String sk = intent.getStringExtra("mSearchKey");
		uid = ResHelper.getInstance(getApplicationContext()).getUserid();
		mPullDownView = (PullDownView) findViewById(R.id.pull_down_view);
		mPullDownView.initHeaderViewAndFooterViewAndListView(this,
				R.layout.listview_header5);
		mPullDownView.setOnPullDownListener(this);
		mListView = mPullDownView.getListView();
		mListView.setOnItemClickListener(this);
		mAdapter = new ZhuoUserListAdapter(UserSameActivity.this, mList);
		mListView.setAdapter(mAdapter);
		mPullDownView.setShowHeader();
		mPullDownView.setShowFooter(false);
		initClick();
		if (sk != null && !sk.equals("")) {
			mSearchKey = sk;
			((EditText) findViewById(R.id.editTextSearch)).setText(sk);
		}
		loadData();
	}

	private void updateItemList(String data, boolean refresh, boolean append) {
		try {
			if (data != null && !data.equals("")) {
				JsonHandler nljh = new JsonHandler(data,
						getApplicationContext());
				ArrayList<ZhuoInfoVO> list = nljh.parseZhuoInfoList();
				if (!list.isEmpty()) {
					mPullDownView.hasData();
					if (!append) {
						mList.clear();
					}
					mList.addAll(list);
					mAdapter.notifyDataSetChanged();
					if (mList.size() > 0) {
						mLastId = mList.get(mList.size() - 1).getMsgid();
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
		mConnHelper
				.getFromServer(params, mUIHandler, MsgTagVO.DATA_REFRESH);
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
			mConnHelper.getFromServer(params, mUIHandler,
					MsgTagVO.DATA_LOAD);
		}
	}

	private void initClick() {

		findViewById(R.id.buttonBack).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				UserSameActivity.this.finish();
			}
		});
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

}
