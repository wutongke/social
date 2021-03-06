package com.cpstudio.zhuojiaren.ui;

import java.util.ArrayList;
import java.util.List;


import android.annotation.SuppressLint;
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
import com.cpstudio.zhuojiaren.adapter.ZhuoUserListAdapter;
import com.cpstudio.zhuojiaren.helper.JsonHandler;
import com.cpstudio.zhuojiaren.helper.ResHelper;
import com.cpstudio.zhuojiaren.helper.ConnHelper;
import com.cpstudio.zhuojiaren.model.Constants;
import com.cpstudio.zhuojiaren.model.MsgTagVO;
import com.cpstudio.zhuojiaren.model.SearchHotKeyWord;
import com.cpstudio.zhuojiaren.model.UserAndCollection;
import com.cpstudio.zhuojiaren.util.CommonAdapter;
import com.cpstudio.zhuojiaren.util.CommonUtil;
import com.cpstudio.zhuojiaren.widget.PullDownView;
import com.cpstudio.zhuojiaren.widget.PullDownView.OnPullDownListener;
/**
 * 首页顶部搜索框的搜索界面
 * @author lz
 *
 */
public class SearchMainActivity extends BaseActivity implements
		OnPullDownListener {
	@InjectView(R.id.search_pull_down_view)
	PullDownView mPullDownView;
	@InjectView(R.id.searchLayout)
	ViewGroup searchLayout;
	@InjectView(R.id.hotContainer)
	ViewGroup hotwordsLayout;
	@InjectView(R.id.search_input)
	EditText searchView;
	@InjectView(R.id.hotLayout)
	View hotView;

	private ListView mListView;
	private ListView mlvHistory;
	ArrayAdapter<String> historyAdapter;

	private ArrayList<UserAndCollection> mList = new ArrayList<UserAndCollection>();
	private CommonAdapter mAdapter;

	ArrayList<String> historyList = new ArrayList<String>();
	String mLastId;
	private SharedPreferences sharedPrefs;

	String uid = null;
	String mSearchKey = null;
	private ConnHelper mConnHelper = null;
	private int mPage = 0;
	private int mPageNum = 5;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search_lz);
		ButterKnife.inject(this);
		initTitle();
		title.setText(R.string.label_searchUserHint);

		mConnHelper = ConnHelper.getInstance(getApplicationContext());
		uid = ResHelper.getInstance(getApplicationContext()).getUserid();

		mPullDownView = (PullDownView) findViewById(R.id.search_pull_down_view);
		mPullDownView.initHeaderViewAndFooterViewAndListView(this,
				R.layout.listview_header);

		mPullDownView.setOnPullDownListener(this);
		mListView = mPullDownView.getListView();

		mAdapter = new ZhuoUserListAdapter(SearchMainActivity.this, mList,
				R.layout.item_zhuouser_list2, mUIHandler);
		mListView.setAdapter(mAdapter);
		mPullDownView.setHideHeader();
		mPullDownView.setHideFooter(false);
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub

				searchLayout.setVisibility(View.VISIBLE);
				mPullDownView.setVisibility(View.GONE);
				if (id != -1) {
					Intent i = new Intent();
					i.setClass(SearchMainActivity.this,
							ZhuoMaiCardActivity.class);
					i.putExtra("userid", (String) view.getTag(R.id.tag_id));
					startActivity(i);
				}
			}
		});
		loadHistory();
		initClick();
		loadHotWordData();
	}

	private void initClick() {
		// TODO Auto-generated method stub
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
						editor.remove(Constants.SEARCH_HISTORY);
						editor.commit();
						historyList.clear();
						historyAdapter.notifyDataSetChanged();
					}
				});
	}

	private void loadHistory() {
		mlvHistory = (ListView) findViewById(R.id.lvHostory);
		historyAdapter = new ArrayAdapter<String>(SearchMainActivity.this,
				R.layout.simple_text_item, historyList);
		mlvHistory.setAdapter(historyAdapter);
		mlvHistory.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				if (position >= 0) {
					mSearchKey = historyList.get(position);
					loadData();
				}
			}
		});
		historyList.clear();
		if (sharedPrefs == null)
			sharedPrefs = getSharedPreferences(
					Constants.SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);
		if (sharedPrefs.contains(Constants.SEARCH_HISTORY)) {
			String hisStr = sharedPrefs.getString(Constants.SEARCH_HISTORY, "");
			if ("".equals(hisStr))
				return;
			String[] strArray = hisStr.split(";");
			for (int i = 0; i < strArray.length; i++)
				historyList.add(strArray[i]);
			historyAdapter.notifyDataSetChanged();
		}

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
		editor.putString(Constants.SEARCH_HISTORY, sb.toString());
		editor.commit();
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
			mPage++;
		} else {
			mPullDownView.noData(!refresh);
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
		@SuppressWarnings("unchecked")
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MsgTagVO.DATA_LOAD: { // 加载数据（本地或网络），本地数据返回一个list,网络数据返回一个json
				ArrayList<UserAndCollection> list = new ArrayList<UserAndCollection>();
				boolean loadState = false;
				if (msg.obj instanceof ArrayList) {// 加载的本地数据
					list = (ArrayList<UserAndCollection>) msg.obj;
				} else {
					if (msg.obj != null && !msg.obj.equals("")) {
						loadState = true;
						JsonHandler nljh = new JsonHandler((String) msg.obj,
								getApplicationContext());
						list = nljh.parseUserCollection();
						if (!list.isEmpty()) {
							// infoFacade.update(list);
						} else if (mSearchKey != null && !mSearchKey.equals("")) {
							CommonUtil.displayToast(getApplicationContext(),
									R.string.error18);
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
					updateItemList(list, true, false);
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
						// if (!list.isEmpty()) {
						// infoFacade.update(list);
						// }
					}
				}
				updateItemList(list, false, true);
				break;
			}

			case MsgTagVO.UPDATE: {
				break;
			}

			case MsgTagVO.HOT_WORD:
				// 添加热词
				if (msg.obj != null && !msg.obj.equals("")) {
					JsonHandler nljh = new JsonHandler((String) msg.obj,
							getApplicationContext());
					ArrayList<SearchHotKeyWord> datas = nljh.parseHotWords();
					if (datas != null && datas.size() > 0) {
						displayHotWord(datas);
					}
				}
				break;
			}
		}
	};

	private void displayHotWord(List<SearchHotKeyWord> datas) {
		// TODO Auto-generated method stub

		int[] ids = { R.id.hotword1, R.id.hotword2, R.id.hotword3,
				R.id.hotword4, R.id.hotword5, R.id.hotword6 };
		hotView.setVisibility(View.VISIBLE);
		for (int i = 0; i < datas.size() && i < ids.length; i++) {
			final TextView tv = (TextView) findViewById(ids[i]);
			if (tv == null)
				continue;
			tv.setText(datas.get(i).getKeyword());
			tv.setTag(datas.get(i).getKeyword());
			tv.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					mSearchKey = (String) tv.getTag();
					searchView.setText(mSearchKey);
					loadData();
				}
			});
		}

	}

	@Override
	public void onRefresh() {
		mPage = 0;
		mConnHelper.getSearchContent(mUIHandler, MsgTagVO.DATA_REFRESH,
				mSearchKey, mPage, mPageNum);
	}

	@Override
	public void onMore() {
		if (CommonUtil.getNetworkState(getApplicationContext()) == 2
				&& (mSearchKey == null || mSearchKey.equals(""))) {
		} else {
			mConnHelper.getSearchContent(mUIHandler, MsgTagVO.DATA_MORE,
					mSearchKey, mPage, mPageNum);
		}
	}

	/**
	 * 获取推荐热词数据
	 */
	private void loadHotWordData() {
		if (CommonUtil.getNetworkState(getApplicationContext()) == 2) {
		} else {
			mConnHelper.getHotKey(mUIHandler, MsgTagVO.HOT_WORD);
		}
	}

	private void loadData() {
		searchLayout.setVisibility(View.GONE);
		mPullDownView.setVisibility(View.VISIBLE);
		if (!mSearchKey.trim().equals("")) {
			historyList.add(0, mSearchKey);
			historyAdapter.notifyDataSetChanged();
		}

		if (!mPullDownView.startLoadData()) {
			mList.clear();
			mAdapter.notifyDataSetChanged();
			if (CommonUtil.getNetworkState(getApplicationContext()) == 2
					&& (mSearchKey == null || mSearchKey.equals(""))) {
			} else {
				mConnHelper.getSearchContent(mUIHandler, MsgTagVO.DATA_LOAD,
						mSearchKey, mPage, mPageNum);
			}
		}
	}
}
