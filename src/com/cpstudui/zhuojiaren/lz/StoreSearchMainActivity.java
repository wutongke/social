package com.cpstudui.zhuojiaren.lz;

import java.util.ArrayList;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import butterknife.ButterKnife;
import butterknife.InjectView;

import com.cpstudio.zhuojiaren.R;
import com.cpstudio.zhuojiaren.adapter.TypedStoreGoodsListAdapter;
import com.cpstudio.zhuojiaren.helper.JsonHandler;
import com.cpstudio.zhuojiaren.helper.ResHelper;
import com.cpstudio.zhuojiaren.helper.ZhuoCommHelper;
import com.cpstudio.zhuojiaren.helper.ConnHelper;
import com.cpstudio.zhuojiaren.model.GoodsVO;
import com.cpstudio.zhuojiaren.model.MsgTagVO;
import com.cpstudio.zhuojiaren.ui.GoodsDetailLActivity;
import com.cpstudio.zhuojiaren.util.CommonUtil;
import com.cpstudio.zhuojiaren.widget.PullDownView;
import com.cpstudio.zhuojiaren.widget.PullDownView.OnPullDownListener;

/**
 * 商城的搜索页面，未完成,可参考SearchMainActivity
 * 
 * @author lz
 * 
 */
public class StoreSearchMainActivity extends Activity implements
		OnPullDownListener, OnItemClickListener {
	@InjectView(R.id.search_pull_down_view)
	PullDownView mPullDownView;

	@InjectView(R.id.searchLayout)
	ViewGroup searchLayout;

	@InjectView(R.id.search_input)
	EditText searchView;

	private ListView mListView;
	private ListView mlvHistory;
	ArrayAdapter<String> historyAdapter;

	private TypedStoreGoodsListAdapter mAdapter = null;
	private ArrayList<GoodsVO> mList = new ArrayList<GoodsVO>();

	ArrayList<String> historyList = new ArrayList<String>();

	String mLastId;

	private SharedPreferences sharedPrefs;

	String uid = null;
	String mSearchKey = null;
	private ConnHelper mConnHelper = null;

	private int mPage = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_store_search_lz);
		ButterKnife.inject(this);
		// getWindow().setSoftInputMode(
		// WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

		mConnHelper = ConnHelper.getInstance(getApplicationContext());

		uid = ResHelper.getInstance(getApplicationContext()).getUserid();

		mPullDownView.setOnPullDownListener(this);

		// 必须设置头，因为mListView是在initHeaderViewAndFooterViewAndListView中初始化的
		mPullDownView.initHeaderViewAndFooterViewAndListView(this,
				R.layout.listview_header2);

		mListView = mPullDownView.getListView();
		mListView.setOnItemClickListener(this);
		mAdapter = new TypedStoreGoodsListAdapter(this, mList);
		mListView.setAdapter(mAdapter);
		mPullDownView.setHideHeader();
		mPullDownView.setHideFooter(false);

		initClick();
		loadHistory();

	}

	private void initClick() {
		// TODO Auto-generated method stub
		findViewById(R.id.buttonSearch).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(mSearchKey!=null && !mSearchKey.isEmpty()){
					loadData();
				}else{
					CommonUtil.displayToast(StoreSearchMainActivity.this, R.string.please_finish);
				}
			}
		});
		findViewById(R.id.activity_back).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				StoreSearchMainActivity.this.finish();
			}
		});
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
			public void onTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
				// TODO Auto-generated method stub
				if (searchView.getText().toString().equals("")) {
					searchLayout.setVisibility(View.VISIBLE);
					mPullDownView.setVisibility(View.GONE);
				}
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable arg0) {
				// TODO Auto-generated method stub

			}
		});
		findViewById(R.id.tvDeleteHistory).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						if (sharedPrefs == null)
							sharedPrefs = getSharedPreferences(
									Constants.SHARED_PREFERENCE_NAME,
									Context.MODE_PRIVATE);
						Editor editor = sharedPrefs.edit();
						editor.remove(Constants.STORE_SEARCH_HISTORY);
						historyList.clear();
						historyAdapter.notifyDataSetChanged();
					}
				});
	}

	private void loadHistory() {
		mlvHistory = (ListView) findViewById(R.id.lvHostory);
		historyAdapter = new ArrayAdapter<String>(StoreSearchMainActivity.this,
				android.R.layout.simple_list_item_1, historyList);
		mlvHistory.setAdapter(historyAdapter);
		mlvHistory.setOnItemClickListener(this);
		historyList.clear();
		if (sharedPrefs == null)
			sharedPrefs = getSharedPreferences(
					Constants.SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);
		if (sharedPrefs.contains(Constants.STORE_SEARCH_HISTORY)) {// 在虚拟机上运行，deviceId设置为空
			String hisStr = sharedPrefs.getString(
					Constants.STORE_SEARCH_HISTORY, "");
			if ("".equals(hisStr))
				return;
			String[] strArray = hisStr.split(";");
			for (int i = 0; i <strArray.length ; i++)
				historyList.add(strArray[i]);
		}
		if (historyAdapter != null)
			historyAdapter.notifyDataSetChanged();
	}

	private void saveHistory() {
		if (sharedPrefs == null) {
			sharedPrefs = getSharedPreferences(
					Constants.SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);
		}
		Editor editor = sharedPrefs.edit();

		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < historyList.size(); i++) {
			sb.append(historyList.get(i));
			sb.append(";");
		}
		editor.putString(Constants.STORE_SEARCH_HISTORY, sb.toString());
		editor.commit();
	}

	private void updateItemList(String data, boolean refresh, boolean append) {
		if (data != null && !data.equals("")) {
			JsonHandler nljh = new JsonHandler(data, getApplicationContext());
			ArrayList<GoodsVO> list = nljh.parseGoodsList();
			if (!list.isEmpty()) {
				mPullDownView.hasData();
				if (!append) {
					mList.clear();
				}
				mPage++;
				mList.addAll(list);
				mAdapter.notifyDataSetChanged();
			} else {
				mPullDownView.noData(!refresh);
			}
		}

	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		saveHistory();
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
			case MsgTagVO.DATA_MORE: {
				updateItemList((String) msg.obj, false, true);
				break;
			}
			}
		}
	};

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {

		if (view == mPullDownView) {
			searchLayout.setVisibility(View.VISIBLE);
			mPullDownView.setVisibility(View.GONE);

			if (id != -1) {
				Intent i = new Intent(StoreSearchMainActivity.this,
						GoodsDetailLActivity.class);
				i.putExtra("goodsId", (String) view.getTag(R.id.tag_id));
				startActivity(i);
			}
		} else {
			mSearchKey = historyList.get(position);
			loadData();
		}
	}

	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub
		loadData();
	}

	@Override
	public void onMore() {
		// TODO Auto-generated method stub

		// if (CommonUtil.getNetworkState(getApplicationContext()) == 2) {
		// ArrayList<ZhuoInfoVO> list = infoFacade.getByPage(mPage);
		// Message msg = mUIHandler.obtainMessage(MsgTagVO.DATA_MORE);
		// msg.obj = list;
		// msg.sendToTarget();
		// } else {
		// String params = ZhuoCommHelper.getUrlMsgList();
		// params += "?pageflag=" + "1";
		// params += "&reqnum=" + "10";
		// params += "&lastid=" + mLastId;
		// params += "&type=" + mType;
		// params += "&gongxutype=" + "0";
		// params += "&from=" + "6";
		// params += "&uid=" + mUid;
		// mConnHelper.getFromServer(params, mUIHandler, MsgTagVO.DATA_MORE);
		// }
		//
		if (mPullDownView.startLoadData()) {
			String params = ZhuoCommHelper.getUrlGetGoodsList();
			params += "?page=" + mPage;
			mConnHelper.getFromServer(params, mUIHandler, MsgTagVO.DATA_MORE);
		}
	}

	private void loadData() {
		searchLayout.setVisibility(View.GONE);
		mPullDownView.setVisibility(View.VISIBLE);
		if (!mSearchKey.trim().equals("")) {
			historyList.add(0, mSearchKey.trim());
			if (historyAdapter != null)
				historyAdapter.notifyDataSetChanged();
		}

		if (mPullDownView.startLoadData()) {
			mList.clear();
			mAdapter.notifyDataSetChanged();
			mPage = 1;
			String params = ZhuoCommHelper.getUrlGetGoodsList();
			params += "?page=" + mPage;
			mConnHelper.getFromServer(params, mUIHandler, MsgTagVO.DATA_LOAD);
		}
	}

}
