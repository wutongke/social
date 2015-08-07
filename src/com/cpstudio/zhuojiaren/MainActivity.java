package com.cpstudio.zhuojiaren;

import java.util.ArrayList;

import com.cpstudio.zhuojiaren.adapter.ZhuoUserListAdapter;
import com.cpstudio.zhuojiaren.adapter.ZhuoUserListAdapter2;
import com.cpstudio.zhuojiaren.facade.InfoFacade;
import com.cpstudio.zhuojiaren.facade.UserFacade;
import com.cpstudio.zhuojiaren.helper.JsonHandler;
import com.cpstudio.zhuojiaren.helper.ResHelper;
import com.cpstudio.zhuojiaren.helper.ZhuoCommHelper;
import com.cpstudio.zhuojiaren.helper.ZhuoConnHelper;
import com.cpstudio.zhuojiaren.imageloader.LoadImage;
import com.cpstudio.zhuojiaren.model.AdVO;
import com.cpstudio.zhuojiaren.model.MsgTagVO;
import com.cpstudio.zhuojiaren.model.UserVO;
import com.cpstudio.zhuojiaren.model.ZhuoInfoVO;
import com.cpstudio.zhuojiaren.util.CommonUtil;
import com.cpstudio.zhuojiaren.util.DeviceInfoUtil;
import com.cpstudio.zhuojiaren.widget.PullDownView;
import com.cpstudio.zhuojiaren.widget.PullDownView.OnPullDownListener;
import com.cpstudio.zhuojiaren.R;

import android.net.Uri;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView.OnEditorActionListener;

public class MainActivity extends Activity implements OnPullDownListener,
		OnItemClickListener {
	private ListView mListView;
	private ZhuoUserListAdapter mAdapter;
	private PullDownView mPullDownView;
	private ArrayList<ZhuoInfoVO> mList = new ArrayList<ZhuoInfoVO>();
	private String mSearchKey = null;
	private String mLastId = null;
	private String uid = null;
	private ZhuoConnHelper mConnHelper = null;
	private InfoFacade infoFacade = null;
	private int mPage = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mConnHelper = ZhuoConnHelper.getInstance(getApplicationContext());
		infoFacade = new InfoFacade(getApplicationContext(),
				InfoFacade.NEWSLIST);
		uid = ResHelper.getInstance(getApplicationContext()).getUserid();
		mPullDownView = (PullDownView) findViewById(R.id.pull_down_view);
		/**
		 * 下拉刷新头view
		 */
		mPullDownView.initHeaderViewAndFooterViewAndListView(this,
				R.layout.listview_header5);
		mPullDownView.setOnPullDownListener(this);
		mListView = mPullDownView.getListView();
		mListView.setOnItemClickListener(this);
		mAdapter = new ZhuoUserListAdapter(MainActivity.this, mList);
		mListView.setAdapter(mAdapter);
		mPullDownView.setShowHeader();
		mPullDownView.setShowFooter(false);
		initClick();
		loadData();
		loadAd();
	}

	private void initClick() {
		findViewById(R.id.buttonSearchUser).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						Intent i = new Intent(MainActivity.this,
								FindActivity.class);
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

	/**
	 * 
	 * @param list
	 * @param refresh
	 * @param append
	 *            是否添加到后边
	 */
	private void updateItemList(ArrayList<ZhuoInfoVO> list, boolean refresh,
			boolean append) {
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
			mPage++;
		} else {
			mPullDownView.noData(!refresh);
		}
	}

	@SuppressLint("HandlerLeak")
	private Handler mUIHandler = new Handler() {
		@SuppressWarnings("unchecked")
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MsgTagVO.DATA_LOAD: { // 加载数据（本地或网络），本地数据返回一个list,网络数据返回一个json
				ArrayList<ZhuoInfoVO> list = new ArrayList<ZhuoInfoVO>();
				boolean loadState = false;
				if (msg.obj instanceof ArrayList) {// 加载的本地数据
					list = (ArrayList<ZhuoInfoVO>) msg.obj;
				} else {
					if (msg.obj != null && !msg.obj.equals("")) {
						loadState = true;
						JsonHandler nljh = new JsonHandler((String) msg.obj,
								getApplicationContext());
						list = nljh.parseZhuoInfoList();
						if (!list.isEmpty()) {
							infoFacade.update(list);
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
					ArrayList<ZhuoInfoVO> list = nljh.parseZhuoInfoList();
					updateItemList(list, false, false);
				}
				mPullDownView.RefreshComplete(loadState);
				break;
			}
			case MsgTagVO.DATA_MORE: {
				mPullDownView.notifyDidMore();
				ArrayList<ZhuoInfoVO> list = new ArrayList<ZhuoInfoVO>();
				if (msg.obj instanceof ArrayList) {
					list = (ArrayList<ZhuoInfoVO>) msg.obj;
				} else {
					if (msg.obj != null && !msg.obj.equals("")) {
						JsonHandler nljh = new JsonHandler((String) msg.obj,
								getApplicationContext());
						list = nljh.parseZhuoInfoList();
						if (!list.isEmpty()) {
							infoFacade.update(list);
						}
					}
				}
				updateItemList(list, false, true);
				break;
			}
			case MsgTagVO.DATA_OTHER: {
				if (msg.obj != null && !msg.obj.equals("")) {
					JsonHandler nljh = new JsonHandler((String) msg.obj,
							getApplicationContext());
					AdVO ad = nljh.parseAd();
					if (ad != null) {
						addAd(ad.getFile(), ad.getLink());
					}
				}
				break;
			}
			case MsgTagVO.UPDATE: {
				if (msg.obj != null && !msg.obj.equals("")) {
					JsonHandler nljh = new JsonHandler((String) msg.obj,
							getApplicationContext());
					UserVO user = nljh.parseUser();
					if (null != user) {
						UserFacade facade = new UserFacade(
								getApplicationContext());
//						facade.saveOrUpdate(user);
					}
				}
				break;
			}
			}
		}

	};

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		if (id != -1) {
			Intent i = new Intent(MainActivity.this, MsgDetailActivity.class);
			i.putExtra("msgid", (String) view.getTag(R.id.tag_id));
			startActivity(i);
		}
	}

	// refresh刷新加载的新的数据没有写数据库
	@Override
	public void onRefresh() {
		String params = ZhuoCommHelper.getUrlMsgList();
		params += "?pageflag=" + "0";
		params += "&reqnum=" + "10";
		params += "&lastid=" + "0";
		params += "&type=" + "0";
		if (null != mSearchKey) {
			params += "&key=" + mSearchKey.trim();
		}
		params += "&gongxutype=" + "0";
		params += "&from=" + "0";
		params += "&uid=" + uid;
		mConnHelper.getFromServer(params, mUIHandler, MsgTagVO.DATA_REFRESH);
	}

	@Override
	public void onMore() {
		if (CommonUtil.getNetworkState(getApplicationContext()) == 2
				&& (mSearchKey == null || mSearchKey.equals(""))) {
			ArrayList<ZhuoInfoVO> list = infoFacade.getByPage(mPage);
			Message msg = mUIHandler.obtainMessage(MsgTagVO.DATA_MORE);
			msg.obj = list;
			msg.sendToTarget();
		} else {
			String params = ZhuoCommHelper.getUrlMsgList();
			params += "?pageflag=" + "1";
			params += "&reqnum=" + "10";
			params += "&lastid=" + mLastId;
			params += "&type=" + "0";
			if (null != mSearchKey) {
				params += "&key=" + mSearchKey.trim();
			}
			params += "&gongxutype=" + "0";
			params += "&from=" + "0";
			params += "&uid=" + uid;
			mConnHelper.getFromServer(params, mUIHandler, MsgTagVO.DATA_MORE);
		}
	}

	private void loadData() {
		String url = ZhuoCommHelper.getUrlUserInfo() + "?uid="
				+ ResHelper.getInstance(getApplicationContext()).getUserid();
		if (mPullDownView.startLoadData()) {
			mList.clear();
			mAdapter.notifyDataSetChanged();
			if (CommonUtil.getNetworkState(getApplicationContext()) == 2
					&& (mSearchKey == null || mSearchKey.equals(""))) {
				// 获取本地数据
				ArrayList<ZhuoInfoVO> list = infoFacade.getByPage(mPage);
				Message msg = mUIHandler.obtainMessage(MsgTagVO.DATA_LOAD);
				msg.obj = list;
				msg.sendToTarget();
			} else {
				String params = ZhuoCommHelper.getUrlMsgList();
				params += "?pageflag=" + "0";
				params += "&reqnum=" + "10";
				params += "&lastid=" + "0";
				params += "&type=" + "0";
				if (null != mSearchKey) {
					params += "&key=" + mSearchKey.trim();
				}
				params += "&gongxutype=" + "0";
				params += "&from=" + "0";
				params += "&uid=" + uid;
				mConnHelper.getFromServer(params, mUIHandler,
						MsgTagVO.DATA_LOAD);
			}
		}
	}

	/**
	 * 获取通知
	 */
	private void loadAd() {
		String params = ZhuoCommHelper.getUrlZhuoNotice();
		mConnHelper.getFromServer(params, mUIHandler, MsgTagVO.DATA_OTHER);
	}

	private void addAd(String url, final String link) {
		int height = (int) (75 * DeviceInfoUtil
				.getDeviceCsd(getApplicationContext()));
		LayoutParams rllp = new LayoutParams(LayoutParams.MATCH_PARENT, height);
		LoadImage loadImage = new LoadImage();
		ImageView iv = new ImageView(MainActivity.this);
		iv.setScaleType(ScaleType.FIT_XY);
		iv.setLayoutParams(rllp);
		RelativeLayout rl = (RelativeLayout) findViewById(R.id.relativeLayoutAds);
		rl.addView(iv);
		iv.setTag(url);
		loadImage.addTask(url, iv);
		rl.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 打开链接
				Uri uri = Uri.parse(link);
				Intent it = new Intent(Intent.ACTION_VIEW, uri);
				startActivity(it);
			}
		});
		loadImage.doTask();
	}

}