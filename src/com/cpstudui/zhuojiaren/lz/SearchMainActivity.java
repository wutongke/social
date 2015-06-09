package com.cpstudui.zhuojiaren.lz;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import com.cpstudio.zhuojiaren.JiarenActiveActivity;
import com.cpstudio.zhuojiaren.MsgCmtActivity;
import com.cpstudio.zhuojiaren.MsgDetailActivity;
import com.cpstudio.zhuojiaren.PublishActiveActivity;
import com.cpstudio.zhuojiaren.R;
import com.cpstudio.zhuojiaren.UserSelectActivity;
import com.cpstudio.zhuojiaren.adapter.ZhuoUserListAdapter;
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
import com.cpstudio.zhuojiaren.widget.PopupWindows;
import com.cpstudio.zhuojiaren.widget.PullDownView;
import com.cpstudio.zhuojiaren.widget.PullDownView.OnPullDownListener;
import com.external.viewpagerindicator.PageIndicator;

public class SearchMainActivity extends Activity implements OnPullDownListener,
		OnItemClickListener {
	private float times = 2;
	private PopupWindows phw = null;
	private ViewPager bannerViewPager, catsViewPager;
	private PageIndicator bannerIndicator, catsIndicator;

	private ArrayList<BeanBanner> bannerListData;
	private ArrayList<BeanBanner> hotListData;
	private ArrayList<BeanCats> catsListData;
	private ArrayList<BeanNotice> noticesListData;

	private ArrayList<ImageView> hotImages;
	private Bee_PageAdapter bannerPageAdapter;
	private Cats_PageAdapter catPageAdapter;

	AutoTextView antoText;

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
	boolean isContinue = true;
	LoadImage imageLoader = new LoadImage(3);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search_lz);

		mConnHelper = ZhuoConnHelper.getInstance(getApplicationContext());
		infoFacade = new InfoFacade(getApplicationContext(),
				InfoFacade.NEWSLIST);
		uid = ResHelper.getInstance(getApplicationContext()).getUserid();
		mPullDownView = (PullDownView) findViewById(R.id.main_pull_down_view);
		// /lz
		mPullDownView.initHeaderViewAndFooterViewAndListView(this,
				R.layout.main_header);

		mPullDownView.setOnPullDownListener(this);
		mListView = mPullDownView.getListView();
		mListView.setOnItemClickListener(this);
		mAdapter = new ZhuoUserListAdapter(SearchMainActivity.this, mList);
		mListView.setAdapter(mAdapter);
		mPullDownView.setShowHeader();
		mPullDownView.setShowFooter(false);

		initHeadView();

		initClick();
		loadData();

		times = DeviceInfoUtil.getDeviceCsd(SearchMainActivity.this);
		// loadAd();
	}

	// 获取屏幕宽度
	public int getDisplayMetricsWidth() {
		int i = getWindowManager().getDefaultDisplay().getWidth();
		int j = getWindowManager().getDefaultDisplay().getHeight();
		return Math.min(i, j);
	}

	private void initHeadView() {

		catsViewPager = (ViewPager) findViewById(R.id.cats_viewpager);
		// catsViewPager.setLayoutParams(params);
		catsListData = new ArrayList<BeanCats>();

		for (int i = 0; i < 14; i++) {
			BeanCats item = new BeanCats();
			item.setPicId(R.drawable.newmsg);
			catsListData.add(item);
		}

		DisplayMetrics metric = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metric);
		int width = metric.widthPixels; // 屏幕宽度（像素）
		int height = metric.heightPixels; // 屏幕高度（像素）

		catPageAdapter = new Cats_PageAdapter(SearchMainActivity.this, catsListData,
				width, height);
		catsViewPager.setAdapter(catPageAdapter);
		catsViewPager.setCurrentItem(0);
		catsIndicator = (PageIndicator) findViewById(R.id.cats_indicator);
		catsIndicator.setViewPager(catsViewPager);

		antoText = (AutoTextView) findViewById(R.id.at_notices);
		noticesListData = new ArrayList<BeanNotice>();
		for (int i = 0; i < 4; i++) {
			BeanNotice item = new BeanNotice();
			item.setContent("mseeage:" + i);
			noticesListData.add(item);
		}

		antoText.setList(noticesListData);

		// antoText.updateUI();

		// antoText.stopAutoText();

		addAd();
	}

	private void initClick() {
		findViewById(R.id.buttonSearch).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						Intent i = new Intent(SearchMainActivity.this,
								com.cpstudui.zhuojiaren.lz.FindActivity.class);
						startActivity(i);
					}
				});
		final EditText searchView = (EditText) findViewById(R.id.search_input);

		searchView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

			}
		});

		findViewById(R.id.buttonPlus).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if(phw==null) 
					phw=new PopupWindows(SearchMainActivity.this);
				
				OnClickListener pubListener = new OnClickListener() {

					@Override
					public void onClick(View v) {
						Intent i = new Intent(SearchMainActivity.this,
								PublishActiveActivity.class);
//						i.putExtra("filePath", filePath);
						startActivity(i);
					}
				};
				OnClickListener inviteListener = new OnClickListener() {

					@Override
					public void onClick(View v) {
						Intent i = new Intent(SearchMainActivity.this,
								UserSelectActivity.class);
						ArrayList<String> tempids = new ArrayList<String>(1);
						tempids.add(uid);
						i.putStringArrayListExtra("otherids", tempids);
						startActivity(i);
					}
				};
				phw.showAddOptionsPop(v, times, pubListener, inviteListener);
			}
		});
	}

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
						// addAd(ad.getFile(), ad.getLink());
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
						facade.saveOrUpdate(user);
					}
				}
				break;
			}
			case MsgTagVO.FLIP:
				bannerViewPager.setCurrentItem((bannerViewPager
						.getCurrentItem() + 1) % bannerPageAdapter.getCount());

				break;
			}
		}

	};

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		if (id != -1) {
			Intent i = new Intent(SearchMainActivity.this, MsgDetailActivity.class);
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

		// 加载刷新个人信息
		mConnHelper.getFromServer(url, mUIHandler, MsgTagVO.UPDATE);
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

	private void loadAd() {
		String params = ZhuoCommHelper.getUrlZhuoNotice();
		mConnHelper.getFromServer(params, mUIHandler, MsgTagVO.DATA_OTHER);
	}

	private void addAd() {
		// int height = (int) (75 * DeviceInfoUtil
		// .getDeviceCsd(getApplicationContext()));

		bannerViewPager = (ViewPager) findViewById(R.id.banner_viewpager);

		// LayoutParams params=new LayoutParams(LayoutParams.MATCH_PARENT,
		// height);
		// bannerViewPager.setLayoutParams(params);

		bannerListData = new ArrayList<BeanBanner>();

		for (int i = 0; i < 5; i++) {

			BeanBanner item = new BeanBanner();
			item.setPicUrl("http://pic.nipic.com/2008-05-07/20085722191339_2.jpg");
			bannerListData.add(item);
		}

		bannerPageAdapter = new Bee_PageAdapter(SearchMainActivity.this,
				bannerListData);
		bannerViewPager.setAdapter(bannerPageAdapter);
		bannerViewPager.setCurrentItem(0);
		bannerIndicator = (PageIndicator) findViewById(R.id.indicator);
		bannerIndicator.setViewPager(bannerViewPager);
		new Thread(new Runnable() {
			@Override
			public void run() {
				while (true) {
					try {
						Thread.sleep(3000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					if (isContinue) {
						mUIHandler.sendEmptyMessage(MsgTagVO.FLIP);
					}
				}
			}
		}).start();

		hotImages = new ArrayList<ImageView>();
		hotListData = new ArrayList<BeanBanner>();
		hotImages.add((ImageView) findViewById(R.id.ivHot1));
		hotImages.add((ImageView) findViewById(R.id.ivHot2));
		hotImages.add((ImageView) findViewById(R.id.ivHot3));
		for (int i = 0; i < 3; i++) {
			BeanBanner item = new BeanBanner();
			item.setPicUrl("http://img2.imgtn.bdimg.com/it/u=834958572,3645145128&fm=21&gp=0.jpg");
			hotListData.add(item);
		}
		for (int i = 0; i < 3; i++) {
			String url = hotListData.get(i).getPicUrl();
			hotImages.get(i).setTag(url);
			imageLoader.addTask(url, hotImages.get(i));
		}
		imageLoader.doTask();
	}

}