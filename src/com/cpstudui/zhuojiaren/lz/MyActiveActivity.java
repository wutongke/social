package com.cpstudui.zhuojiaren.lz;

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
import butterknife.ButterKnife;
import butterknife.InjectView;

import com.cpstudio.zhuojiaren.BaseActivity;
import com.cpstudio.zhuojiaren.MsgDetailActivity;
import com.cpstudio.zhuojiaren.PublishActiveActivity;
import com.cpstudio.zhuojiaren.R;
import com.cpstudio.zhuojiaren.helper.JsonHandler;
import com.cpstudio.zhuojiaren.helper.ResHelper;
import com.cpstudio.zhuojiaren.helper.ZhuoCommHelper;
import com.cpstudio.zhuojiaren.helper.ZhuoConnHelper;
import com.cpstudio.zhuojiaren.model.EventVO;
import com.cpstudio.zhuojiaren.model.MsgTagVO;
import com.cpstudio.zhuojiaren.util.CommonUtil;
import com.cpstudio.zhuojiaren.widget.ListViewFooter;
import com.cpstudio.zhuojiaren.widget.PullDownView;
import com.cpstudio.zhuojiaren.widget.PullDownView.OnPullDownListener;

public class MyActiveActivity extends BaseActivity implements
		OnPullDownListener, OnItemClickListener {
	@InjectView(R.id.llMenueTop)
	View pubView;
	
	boolean isManaging = false;
	private String mSearchKey = null;
	private ArrayList<EventVO> mList = new ArrayList<EventVO>();
	private ZhuoConnHelper mConnHelper = null;
	private int mPage = 1;
	private int mType = 6;
	private EventListAdapter mAdapter;
	private PullDownView mPullDownView;
	private ListViewFooter mListViewFooter = null;
	private ListView mListView;
	private String mLastId = null;
	private String uid = null;
	String sk = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_active);
		ButterKnife.inject(this);
		initTitle();
		title.setText(R.string.title_activity_my_active);
		function.setVisibility(View.VISIBLE);
		function.setText(R.string.label_manage);

		mConnHelper = ZhuoConnHelper.getInstance(MyActiveActivity.this
				.getApplicationContext());

		uid = ResHelper.getInstance(getApplicationContext()).getUserid();

		mPullDownView = (PullDownView) findViewById(R.id.pull_down_view);
		mPullDownView.initHeaderViewAndFooterViewAndListView(this,
				R.layout.listview_header5);
		mPullDownView.setOnPullDownListener(this);
		mListView = mPullDownView.getListView();
		mListView.setOnItemClickListener(this);

		mAdapter = new EventListAdapter(MyActiveActivity.this, mList);
		

		RelativeLayout mFooterView = (RelativeLayout) LayoutInflater.from(this).inflate(
				R.layout.listview_footer, null);

		mListView.addFooterView(mFooterView);
		mListViewFooter = new ListViewFooter(mFooterView, onMoreClick);
		mListView.setAdapter(mAdapter);
		mListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// Intent i = new Intent(getActivity(),
				// EventDetailActivity.class);
				// i.putExtra("eventId", mList.get(position).getEventId());
				// startActivity(i);
			}

		});

		// 测试，用死的数据
		// loadData();
		for (int i = 0; i < 8; i++)
			mList.add(new EventVO());
		mAdapter.notifyDataSetChanged();

		mListView.setAdapter(mAdapter);
		mPullDownView.setShowHeader();
		mPullDownView.setShowFooter(false);
		initClick();
		if (sk != null && !sk.equals("")) {
			mSearchKey = sk;
			((EditText) findViewById(R.id.editTextSearch)).setText(sk);
		}
		mAdapter.notifyDataSetChanged();
	}

	private void updateItemList(String data, boolean refresh, boolean append) {
		try {
			if (data != null && !data.equals("")) {
				JsonHandler nljh = new JsonHandler(data,
						getApplicationContext());
				ArrayList<EventVO> list = new ArrayList<EventVO>();
				// =nljh.parseZhuoInfoList();
				if (!list.isEmpty()) {
					mPullDownView.hasData();
					if (!append) {
						mList.clear();
					}
					mList.addAll(list);
					mAdapter.notifyDataSetChanged();
					if (mList.size() > 0) {
						mLastId = mList.get(mList.size() - 1).getActivityid();
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
			Intent i = new Intent(MyActiveActivity.this,
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
		function.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				isManaging = !isManaging;
				mAdapter.setManaging(isManaging);
				if (isManaging)
					function.setText(R.string.EXIT);
				else
					function.setText(R.string.label_manage);
			}
		});

		pubView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent i = new Intent(MyActiveActivity.this,
						PublishActiveActivity.class);
				startActivity(i);
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

	private void loadMore() {
		if (mListViewFooter.startLoading()) {
			String params = ZhuoCommHelper.getUrlMsgList();
			params += "?pageflag=" + "1";
			params += "&reqnum=" + "10";
			params += "&lastid=" + mLastId;
			params += "&type=" + "0";
			params += "&gongxutype=" + "0";
			params += "&from=" + "0";
			params += "&uid=" + uid;
			mConnHelper.getFromServer(params, mUIHandler, MsgTagVO.DATA_MORE);
		}
	}

	private OnClickListener onMoreClick = new OnClickListener() {
		@Override
		public void onClick(View v) {
			loadMore();
		}
	};

}
