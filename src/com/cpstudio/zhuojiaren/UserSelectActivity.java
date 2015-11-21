package com.cpstudio.zhuojiaren;

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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.cpstudio.zhuojiaren.adapter.UserSelectListAdapter;
import com.cpstudio.zhuojiaren.helper.JsonHandler;
import com.cpstudio.zhuojiaren.helper.ResHelper;
import com.cpstudio.zhuojiaren.helper.ZhuoCommHelper;
import com.cpstudio.zhuojiaren.helper.ZhuoConnHelper;
import com.cpstudio.zhuojiaren.model.MsgTagVO;
import com.cpstudio.zhuojiaren.model.UserVO;
import com.cpstudio.zhuojiaren.util.CommonUtil;
import com.cpstudio.zhuojiaren.widget.ListViewFooter;

public class UserSelectActivity extends Activity implements OnItemClickListener {
	private ListView mListView;
	private UserSelectListAdapter mAdapter;
	private ArrayList<UserVO> mList = new ArrayList<UserVO>();
	private int mMax = -1;
	private View mHeadView = null;
	private LayoutInflater inflater;
	private ArrayList<String> mSelcted = new ArrayList<String>();
	private ZhuoConnHelper mConnHelper = null;
	private String mSearchKey = null;
	private String mLastId = null;
	private ListViewFooter mListViewFooter = null;
	private ArrayList<String> otherids = new ArrayList<String>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_select);
		mConnHelper = ZhuoConnHelper.getInstance(getApplicationContext());
		Intent i = getIntent();
		mMax = i.getIntExtra("max", -1);
		mSelcted = i.getStringArrayListExtra("selected");
		if (mSelcted == null) {
			mAdapter = new UserSelectListAdapter(this, mList, mMax);
		} else {
			mAdapter = new UserSelectListAdapter(this, mList,
					new HashSet<String>(mSelcted), mMax);
		}
		ArrayList<String> tempids = i.getStringArrayListExtra("otherids");
		if (tempids != null) {
			otherids.addAll(tempids);
		}
		String myid = ResHelper.getInstance(getApplicationContext())
				.getUserid();
		otherids.add(myid);
		mListView = (ListView) findViewById(R.id.listViewUser);
		inflater = LayoutInflater.from(UserSelectActivity.this);
		mHeadView = (LinearLayout) inflater.inflate(R.layout.listview_header10,
				null);
		mListView.addHeaderView(mHeadView);
		RelativeLayout mFooterView = (RelativeLayout) inflater.inflate(
				R.layout.listview_footer, null);
		mListView.addFooterView(mFooterView);
		mListViewFooter = new ListViewFooter(mFooterView, onMoreClick);
		mListView.setAdapter(mAdapter);
		mListView.setOnItemClickListener(this);
		initClick();
		loadData();
	}

	private void initClick() {
		findViewById(R.id.buttonBack).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				UserSelectActivity.this.finish();
			}
		});
		findViewById(R.id.buttonOK).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				ArrayList<String> ids = new ArrayList<String>(mAdapter
						.getSelectedId());
				ArrayList<String> headUrls = new ArrayList<String>();
				ArrayList<String> names = new ArrayList<String>();
				for (UserVO user : mList) {
					if (ids.contains(user.getUserid())) {
						headUrls.add(user.getUheader());
						names.add(user.getUsername());
					}
				}
				Intent intent = new Intent();
				intent.putStringArrayListExtra("ids", ids);
				intent.putStringArrayListExtra("names", names);
				intent.putStringArrayListExtra("headers", headUrls);
				setResult(RESULT_OK, intent);
				UserSelectActivity.this.finish();
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
				ArrayList<UserVO> list = nljh.parseUserList();
				if (!list.isEmpty()) {
					mListViewFooter.hasData();
					if (!append) {
						mList.clear();
					}
					for (int i = 0; i < list.size(); i++) {
						if (otherids.contains(list.get(i).getUserid())) {
							list.remove(i);
						}
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
				updateItemList((String) msg.obj, true, false);
				break;
			}
			case MsgTagVO.DATA_MORE: {
				mListViewFooter.finishLoading();
				updateItemList((String) msg.obj, false, true);
				break;
			}
			}
		}
	};

	private void loadData() {
		if (mListViewFooter.startLoading()) {
			mList.clear();
			mAdapter.notifyDataSetChanged();
			String params = ZhuoCommHelper.getUrlUserList();
			params += "?pageflag=" + "0";
			params += "&reqnum=" + "20";
			params += "&lastid=" + "0";
			params += "&type=" + "2";
			if (null != mSearchKey) {
				params += "&key=" + mSearchKey.trim();
			}
			mConnHelper.getFromServer(params, mUIHandler, MsgTagVO.DATA_LOAD);
		}
	}

	private void loadMore() {
		if (mListViewFooter.startLoading()) {
			String params = ZhuoCommHelper.getUrlUserList();
			params += "?pageflag=" + "1";
			params += "&reqnum=" + "20";
			params += "&lastid=" + mLastId;
			params += "&type=" + "2";
			if (null != mSearchKey) {
				params += "&key=" + mSearchKey.trim();
			}
			mConnHelper.getFromServer(params, mUIHandler, MsgTagVO.DATA_MORE);
		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		if (arg3 != -1) {
		}
	}

}
