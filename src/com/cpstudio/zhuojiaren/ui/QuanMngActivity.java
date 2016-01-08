package com.cpstudio.zhuojiaren.ui;

import java.util.ArrayList;
import java.util.HashSet;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.cpstudio.zhuojiaren.R;
import com.cpstudio.zhuojiaren.adapter.UserSelectListAdapter;
import com.cpstudio.zhuojiaren.helper.ConnHelper;
import com.cpstudio.zhuojiaren.helper.JsonHandler;
import com.cpstudio.zhuojiaren.helper.UrlHelper;
import com.cpstudio.zhuojiaren.model.MsgTagVO;
import com.cpstudio.zhuojiaren.model.QuanUserVO;
import com.cpstudio.zhuojiaren.model.UserVO;
import com.cpstudio.zhuojiaren.util.CommonUtil;

public class QuanMngActivity extends Activity implements OnItemClickListener {
	private ListView mListView;
	private UserSelectListAdapter mAdapter;
	private ArrayList<UserVO> mList = new ArrayList<UserVO>();
	private int mMax = -1;
	private LayoutInflater inflater;
	private ArrayList<String> mSelcted = new ArrayList<String>();
	private ConnHelper mConnHelper = null;
	private String mSearchKey = null;
	private String groupid = null;
	private int mPage = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_quan_mng);
		mConnHelper = ConnHelper.getInstance(getApplicationContext());
		Intent i = getIntent();
		mMax = i.getIntExtra("max", -1);
		groupid = i.getStringExtra("groupid");
		mSelcted = i.getStringArrayListExtra("selected");
		if (mSelcted == null) {
			mAdapter = new UserSelectListAdapter(this, mList, mMax);
		} else {
			mAdapter = new UserSelectListAdapter(this, mList,
					new HashSet<String>(mSelcted), mMax);
		}
		mListView = (ListView) findViewById(R.id.listViewUser);
		inflater = LayoutInflater.from(QuanMngActivity.this);
		RelativeLayout mFooterView = (RelativeLayout) inflater.inflate(
				R.layout.listview_footer2, null);
		mListView.addFooterView(mFooterView);
		mListView.setAdapter(mAdapter);
		mListView.setOnItemClickListener(this);
		initClick();
		loadData();
	}

	private void initClick() {
		findViewById(R.id.buttonBack).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				QuanMngActivity.this.finish();
			}
		});
		findViewById(R.id.buttonOK).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				ArrayList<String> ids = new ArrayList<String>(mAdapter
						.getSelectedId());
				ArrayList<String> headUrls = new ArrayList<String>();
				for (UserVO user : mList) {
					if (ids.contains(user.getUserid())) {
						headUrls.add(user.getUheader());
					}
				}
				Intent intent = new Intent();
				intent.putStringArrayListExtra("ids", ids);
				intent.putStringArrayListExtra("headers", headUrls);
				setResult(RESULT_OK, intent);
				finish();
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

	private void updateItemList(String data, boolean refresh, boolean append) {
		try {
			findViewById(R.id.progressLoading).setVisibility(View.GONE);
			findViewById(R.id.textViewLoading).setVisibility(View.GONE);
			findViewById(R.id.textViewFail).setVisibility(View.GONE);
			if (data != null && !data.equals("")) {
				JsonHandler nljh = new JsonHandler(data,
						getApplicationContext());
				ArrayList<QuanUserVO> memberlist = nljh.parseGroupMemberList();
				ArrayList<UserVO> list = new ArrayList<UserVO>();
				for (QuanUserVO member : memberlist) {
					if (!member.getGlevel().equals("0")) {
						list.add(member.getUser());
					}
				}
				if (!list.isEmpty()) {
					findViewById(R.id.textViewNoData).setVisibility(View.GONE);
					if (!append) {
						mList.clear();
					}
					mList.addAll(list);
					mAdapter.notifyDataSetChanged();
				} else {
					if (mSearchKey != null &&!mSearchKey.equals("")) {
						CommonUtil.displayToast(getApplicationContext(),
								R.string.error18);
					}
					findViewById(R.id.textViewNoData).setVisibility(
							View.VISIBLE);
				}
			} else {
				findViewById(R.id.textViewFail).setVisibility(View.VISIBLE);
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
				updateItemList((String) msg.obj, true, false);
				break;
			}
			}
		}
	};

	private void loadData() {
		findViewById(R.id.textViewFail).setVisibility(View.GONE);
		findViewById(R.id.progressLoading).setVisibility(View.VISIBLE);
		findViewById(R.id.textViewLoading).setVisibility(View.VISIBLE);
		findViewById(R.id.textViewNoData).setVisibility(View.GONE);
		mPage = 1;
		String params = UrlHelper.getUrlGroupMembers();
		params += "?page=" + mPage;
		params += "&groupid=" + groupid;
		if (mSearchKey != null && !mSearchKey.trim().equals("")) {
			params += "&key=" + mSearchKey.trim();
		}
		mConnHelper.getFromServer(params, mUIHandler, MsgTagVO.DATA_LOAD);
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		if (arg3 != -1) {

		}
	}
}
