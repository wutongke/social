package com.cpstudio.zhuojiaren;

import java.util.ArrayList;
import java.util.List;
import com.cpstudio.zhuojiaren.adapter.CmtListAdapter;
import com.cpstudio.zhuojiaren.helper.JsonHandler;
import com.cpstudio.zhuojiaren.helper.ZhuoCommHelper;
import com.cpstudio.zhuojiaren.helper.ZhuoConnHelper;
import com.cpstudio.zhuojiaren.model.CmtVO;
import com.cpstudio.zhuojiaren.model.MsgTagVO;
import com.cpstudio.zhuojiaren.widget.ListViewFooter;
import com.cpstudio.zhuojiaren.widget.PopupWindows;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.AdapterView.OnItemClickListener;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;

public class CmtListActivity extends Activity implements OnItemClickListener {
	private ListView mListView;
	private CmtListAdapter mAdapter;
	private ArrayList<CmtVO> mList = new ArrayList<CmtVO>();
	private PopupWindows pwh;
	private String msgid = null;
	private int mPage = 1;
	private ZhuoConnHelper mConnHelper = null;
	private ListViewFooter mListViewFooter = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_cmt_list);
		mConnHelper = ZhuoConnHelper.getInstance(getApplicationContext());
		Intent i = getIntent();
		msgid = i.getStringExtra("msgid");
		pwh = new PopupWindows(CmtListActivity.this);
		mAdapter = new CmtListAdapter(this, mList);
		mListView = (ListView) findViewById(R.id.listView);
		LayoutInflater inflater = LayoutInflater.from(CmtListActivity.this);
		RelativeLayout footerView = (RelativeLayout) inflater.inflate(
				R.layout.listview_footer, null);
		mListView.addFooterView(footerView);
		mListViewFooter = new ListViewFooter(footerView, onMoreClick);
		mListView.setAdapter(mAdapter);
		mListView.setOnItemClickListener(this);
		initClick();
		loadData();
	}

	private void updateItemList(String data, boolean refresh, boolean append) {
		try {
			if (data != null && !data.equals("")) {
				JsonHandler nljh = new JsonHandler(data,
						getApplicationContext());
				List<CmtVO> list = nljh.parsePagesCmt().getData();
				if (!list.isEmpty()) {
					mListViewFooter.hasData();
					if (!append) {
						mList.clear();
					}
					mPage++;
					mList.addAll(list);
					mAdapter.notifyDataSetChanged();
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
				updateItemList((String) msg.obj, true, false);
				break;
			}
			case MsgTagVO.DATA_MORE: {
				mListViewFooter.finishLoading();
				updateItemList((String) msg.obj, false, true);
				break;
			}
			case MsgTagVO.PUB_INFO: {
				if (JsonHandler.checkResult((String) msg.obj)) {
					((EditText) findViewById(R.id.editTextCmt)).setText("");
					pwh.showPopDlgOne(findViewById(R.id.listView), null,
							R.string.info62);
				} else {
					pwh.showPopDlgOne(findViewById(R.id.listView), null,
							R.string.error5);
				}
				break;
			}
			}
		}
	};

	private void loadData() {
		if (mListViewFooter.startLoading()) {
			mList.clear();
			mAdapter.notifyDataSetChanged();
			mPage = 1;
			String params = ZhuoCommHelper.getUrlCmtList();
			params += "?msgid=" + msgid;
			params += "&page=" + mPage;
			mConnHelper.getFromServer(params, mUIHandler, MsgTagVO.DATA_LOAD);
		}
	}

	private void loadMore() {
		if (mListViewFooter.startLoading()) {
			String params = ZhuoCommHelper.getUrlCmtList();
			params += "?msgid=" + msgid;
			params += "&page=" + mPage;
			mConnHelper.getFromServer(params, mUIHandler, MsgTagVO.DATA_MORE);
		}
	}

	private OnClickListener onMoreClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			loadMore();
		}
	};

	private void initClick() {
		findViewById(R.id.buttonBack).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				CmtListActivity.this.finish();
				// overridePendingTransition(R.anim.activity_left_in,
				// R.anim.activity_right_out);
			}
		});

		findViewById(R.id.editTextCmt).setOnFocusChangeListener(
				new OnFocusChangeListener() {

					@Override
					public void onFocusChange(View v, boolean hasFocus) {
						if (hasFocus) {
							findViewById(R.id.buttonOrigin).setVisibility(
									View.INVISIBLE);
							findViewById(R.id.buttonPub).setVisibility(
									View.VISIBLE);
							findViewById(R.id.viewHidden).setVisibility(
									View.VISIBLE);
						} else {
							findViewById(R.id.buttonOrigin).setVisibility(
									View.VISIBLE);
							findViewById(R.id.buttonPub).setVisibility(
									View.INVISIBLE);
							findViewById(R.id.viewHidden).setVisibility(
									View.INVISIBLE);
							InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
							imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
						}
					}
				});

		findViewById(R.id.buttonOrigin).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						CmtListActivity.this.finish();
						// overridePendingTransition(R.anim.activity_left_in,
						// R.anim.activity_right_out);
					}
				});

		findViewById(R.id.buttonPub).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				EditText edit = (EditText) findViewById(R.id.editTextCmt);
				String content = edit.getText().toString();
				if (content.trim().equals("")) {
					pwh.showPopDlgOne(findViewById(R.id.rootLayout), null,
							R.string.info65);
					edit.requestFocus();
					return;
				}
				String forward = "0";
				edit.clearFocus();
				mConnHelper.pubCmt(msgid, msgid, content, forward, mUIHandler,
						MsgTagVO.PUB_INFO, CmtListActivity.this, true, null,
						null);
			}
		});
		findViewById(R.id.viewHidden).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				EditText edit = (EditText) findViewById(R.id.editTextCmt);
				edit.clearFocus();
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == Activity.RESULT_OK) {
			if (requestCode == MsgTagVO.MSG_CMT) {
				loadData();
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		if (arg3 != -1) {
		}
	}
}