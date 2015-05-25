package com.cpstudio.zhuojiaren;

import java.util.ArrayList;
import com.cpstudio.zhuojiaren.R;
import com.cpstudio.zhuojiaren.adapter.ZhuoInfoListAdapter;
import com.cpstudio.zhuojiaren.helper.JsonHandler;
import com.cpstudio.zhuojiaren.helper.ResHelper;
import com.cpstudio.zhuojiaren.helper.ZhuoConnHelper;
import com.cpstudio.zhuojiaren.helper.ZhuoCommHelper;
import com.cpstudio.zhuojiaren.model.MsgTagVO;
import com.cpstudio.zhuojiaren.model.ZhuoInfoVO;
import com.cpstudio.zhuojiaren.util.CommonUtil;
import com.cpstudio.zhuojiaren.widget.PopupWindows;
import com.cpstudio.zhuojiaren.widget.PullDownView;
import com.cpstudio.zhuojiaren.widget.PopupWindows.ListItemSelected;
import com.cpstudio.zhuojiaren.widget.PullDownView.OnPullDownListener;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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

public class MsgResourceActivity extends Activity implements
		OnPullDownListener, OnItemClickListener {

	private ListView mListView;
	private ZhuoInfoListAdapter mAdapter;
	private PullDownView mPullDownView;
	private ArrayList<ZhuoInfoVO> mList = new ArrayList<ZhuoInfoVO>();
	private String mSearchKey = null;
	private String mLastId = null;
	private String mUid = null;
	private String mType = "0";
	private ZhuoConnHelper mConnHelper = null;
	private PopupWindows pwh = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_resource);
		mConnHelper = ZhuoConnHelper.getInstance(getApplicationContext());
		mUid = ResHelper.getInstance(getApplicationContext()).getUserid();
		pwh = new PopupWindows(MsgResourceActivity.this);
		mPullDownView = (PullDownView) findViewById(R.id.pull_down_view);
		mPullDownView.initHeaderViewAndFooterViewAndListView(this,
				R.layout.listview_header5);
		mPullDownView.setOnPullDownListener(this);
		mListView = mPullDownView.getListView();
		mAdapter = new ZhuoInfoListAdapter(MsgResourceActivity.this, mList);
		mListView.setAdapter(mAdapter);
		mListView.setOnItemClickListener(this);
		mPullDownView.setShowHeader();
		mPullDownView.setShowFooter(false);
		initClick();
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
	public void onRefresh() {
		String params = ZhuoCommHelper.getUrlMsgList();
		params += "?pageflag=" + "0";
		params += "&reqnum=" + "20";
		params += "&lastid=" + "0";
		params += "&type=" + "1";
		if (null != mSearchKey) {
			params += "&key=" + mSearchKey.trim();
		}
		params += "&gongxutype=" + mType;
		params += "&from=" + "4";
		params += "&uid=" + mUid;
		mConnHelper.getFromServer(params, mUIHandler,
				MsgTagVO.DATA_REFRESH);
	}

	public void onMore() {
		String params = ZhuoCommHelper.getUrlMsgList();
		params += "?pageflag=" + "1";
		params += "&reqnum=" + "20";
		params += "&lastid=" + mLastId;
		params += "&type=" + "1";
		if (null != mSearchKey) {
			params += "&key=" + mSearchKey.trim();
		}
		params += "&gongxutype=" + mType;
		params += "&from=" + "4";
		params += "&uid=" + mUid;
		mConnHelper.getFromServer(params, mUIHandler,
				MsgTagVO.DATA_MORE);
	}

	public void loadData() {
		if (mPullDownView.startLoadData()) {
			mList.clear();
			mAdapter.notifyDataSetChanged();
			String params = ZhuoCommHelper.getUrlMsgList();
			params += "?pageflag=" + "0";
			params += "&reqnum=" + "20";
			params += "&lastid=" + "0";
			params += "&type=" + "1";
			if (null != mSearchKey) {
				params += "&key=" + mSearchKey.trim();
			}
			params += "&gongxutype=" + mType;
			params += "&from=" + "4";
			params += "&uid=" + mUid;
			mConnHelper.getFromServer(params, mUIHandler,
					MsgTagVO.DATA_LOAD);
		}
	}

	private void initClick() {

		findViewById(R.id.buttonBack).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				MsgResourceActivity.this.finish();
			}
		});
		findViewById(R.id.filter).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				pwh.showTopPop(v, new ListItemSelected() {

					@Override
					public String onSelected(String str) {
						mType = ZhuoCommHelper.transferMsgStringToCategory(str,
								MsgResourceActivity.this);
						loadData();
						return null;
					}
				});
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

	@Override
	public void onItemClick(AdapterView<?> paramAdapterView, View arg1,
			int arg2, long arg3) {
		if (arg3 != -1) {
			Intent i = new Intent(MsgResourceActivity.this,
					MsgDetailActivity.class);
			i.putExtra("msgid", (String) arg1.getTag(R.id.tag_id));
			startActivity(i);
		}
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == Activity.RESULT_OK) {
			if (requestCode == MsgTagVO.MSG_CMT) {
				String forward = data.getStringExtra("forward");
				String msgid = data.getStringExtra("msgid");
				for (int i = 0; i < mList.size(); i++) {
					ZhuoInfoVO item = mList.get(i);
					if (msgid != null) {
						if (item.getMsgid().equals(msgid)) {
							if (forward != null && forward.equals("1")) {
								item.setForwardnum(String.valueOf(Integer
										.valueOf(item.getForwardnum()) + 1));
							}
							item.setCmtnum(String.valueOf(Integer.valueOf(item
									.getCmtnum()) + 1));
							mList.set(i, item);
							break;
						} 
					}
				}
				mAdapter.notifyDataSetChanged();
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

}
