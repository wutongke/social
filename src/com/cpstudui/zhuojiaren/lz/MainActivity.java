package com.cpstudui.zhuojiaren.lz;

import java.util.ArrayList;
import java.util.List;

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
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;
import butterknife.ButterKnife;
import butterknife.InjectView;

import com.cpstudio.zhuojiaren.MsgDetailActivity;
import com.cpstudio.zhuojiaren.PublishActiveActivity;
import com.cpstudio.zhuojiaren.R;
import com.cpstudio.zhuojiaren.facade.InfoFacade;
import com.cpstudio.zhuojiaren.facade.UserFacade;
import com.cpstudio.zhuojiaren.helper.JsonHandler;
import com.cpstudio.zhuojiaren.helper.ResHelper;
import com.cpstudio.zhuojiaren.helper.ZhuoCommHelper;
import com.cpstudio.zhuojiaren.helper.ZhuoConnHelper;
import com.cpstudio.zhuojiaren.imageloader.LoadImage;
import com.cpstudio.zhuojiaren.model.BaseCodeData;
import com.cpstudio.zhuojiaren.model.Dynamic;
import com.cpstudio.zhuojiaren.model.GoodsPicAdVO;
import com.cpstudio.zhuojiaren.model.MainHeadInfo;
import com.cpstudio.zhuojiaren.model.MessagePubVO;
import com.cpstudio.zhuojiaren.model.MsgTagVO;
import com.cpstudio.zhuojiaren.model.PicAdVO;
import com.cpstudio.zhuojiaren.model.QuanTopicVO;
import com.cpstudio.zhuojiaren.model.ResultVO;
import com.cpstudio.zhuojiaren.model.UserNewVO;
import com.cpstudio.zhuojiaren.model.UserVO;
import com.cpstudio.zhuojiaren.model.ZhuoInfoVO;
import com.cpstudio.zhuojiaren.util.CommonUtil;
import com.cpstudio.zhuojiaren.util.DeviceInfoUtil;
import com.cpstudio.zhuojiaren.widget.PopupWindows;
import com.cpstudio.zhuojiaren.widget.PullDownView;
import com.cpstudio.zhuojiaren.widget.PullDownView.OnPullDownListener;
import com.external.viewpagerindicator.PageIndicator;

public class MainActivity extends Activity implements OnPullDownListener,
		OnItemClickListener {

	@InjectView(R.id.main_banner)
	ImageView idBanner;
	@InjectView(R.id.at_notices)
	AutoTextView antoText;
	@InjectView(R.id.ivHot1)
	ImageView ivHot1;
	@InjectView(R.id.ivHot2)
	ImageView ivHot2;
	@InjectView(R.id.ivHot3)
	ImageView ivHot3;
	private float times = 2;
	private PopupWindows phw = null;
	private ViewPager catsViewPager;

	GoodsPicAdVO picAd;

	private PageIndicator catsIndicator;

	private List<PicAdVO> hotListData;

	private List<MessagePubVO> noticesListData = new ArrayList<MessagePubVO>();

	private ArrayList<ImageView> hotImages = new ArrayList<ImageView>();

	private ArrayList<BeanCats> catsListData;
	private Cats_PageAdapter catPageAdapter;

	// ImageView idBanner;
	private ListView mListView;
	// lz .. private ZhuoUserListAdapter mAdapter;
	private DynamicListAdapter mAdapter;
	private PullDownView mPullDownView;
	// 圈话题的布局与动态一样，暂时用QuanTopicVO的List和adapter
	private ArrayList<Dynamic> mList = new ArrayList<Dynamic>();
	private String mSearchKey = null;
	private String mLastId = null;
	private String uid = null;
	private ZhuoConnHelper mConnHelper = null;
	// 存本地。需改写
	private InfoFacade infoFacade = null;
	private int mPage = 1;
	boolean isContinue = true;
	LoadImage imageLoader = new LoadImage(3);

	MainHeadInfo adInfo = new MainHeadInfo();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_lz);

		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

		mConnHelper = ZhuoConnHelper.getInstance(getApplicationContext());
		infoFacade = new InfoFacade(getApplicationContext(),
				InfoFacade.NEWSLIST);
		uid = ResHelper.getInstance(getApplicationContext()).getUserid();
		mPullDownView = (PullDownView) findViewById(R.id.main_pull_down_view);
		// /lz
		mPullDownView.initHeaderViewAndFooterViewAndListView(this,
				R.layout.main_header);

		ButterKnife.inject(this);

		mPullDownView.setOnPullDownListener(this);
		mListView = mPullDownView.getListView();
		mListView.setOnItemClickListener(this);
		// 是否和圈子话题公用一个数据结构还不一定
		mAdapter = new DynamicListAdapter(MainActivity.this, mList, 1);
		mListView.setAdapter(mAdapter);
		mPullDownView.setShowHeader();
		mPullDownView.setShowFooter(false);

		initClick();
		initHeadView();
		loadData();
		times = DeviceInfoUtil.getDeviceCsd(MainActivity.this);
	}

	// 获取屏幕宽度
	public int getDisplayMetricsWidth() {
		int i = getWindowManager().getDefaultDisplay().getWidth();
		int j = getWindowManager().getDefaultDisplay().getHeight();
		return Math.min(i, j);
	}

	private void initHeadView() {
		// idBanner = (ImageView) findViewById(R.id.main_banner);

		catsViewPager = (ViewPager) findViewById(R.id.cats_viewpager);
		// catsViewPager.setLayoutParams(params);
		catsListData = new ArrayList<BeanCats>();
		int drawId[] = { R.drawable.jiarendongtai,
				R.drawable.chengzhangzaixian, R.drawable.zhuoyuanyuyin,
				R.drawable.wodemingpian, R.drawable.zhuo, R.drawable.resource,
				R.drawable.shop, R.drawable.project, R.drawable.travel,
				R.drawable.city, R.drawable.interest, R.drawable.near,
				R.drawable.jinjing, R.drawable.teacher, R.drawable.money };
		String[] texts = getResources().getStringArray(R.array.main_cats);
		for (int i = 0; i < 14; i++) {
			BeanCats item = new BeanCats();
			item.setPicId(drawId[i]);
			item.setText(texts[i]);
			catsListData.add(item);
		}

		DisplayMetrics metric = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metric);
		int width = metric.widthPixels; // 屏幕宽度（像素）
		int height = metric.heightPixels; // 屏幕高度（像素）

		catPageAdapter = new Cats_PageAdapter(MainActivity.this, catsListData,
				width, height);
		catsViewPager.setAdapter(catPageAdapter);
		catsViewPager.setCurrentItem(0);
		catsIndicator = (PageIndicator) findViewById(R.id.cats_indicator);
		catsIndicator.setViewPager(catsViewPager);

		hotImages.add(ivHot1);
		hotImages.add(ivHot2);
		hotImages.add(ivHot3);
	}

	private void initClick() {

		idBanner.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (picAd != null)
					;
				// startActivity(new Intent(MainActivity.this,
				// GoodsDetailLActivity.class));
			}
		});

		findViewById(R.id.buttonSearch).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						Intent i = new Intent(MainActivity.this,
								com.cpstudui.zhuojiaren.lz.FindActivity.class);
						startActivity(i);
					}
				});
		final EditText searchView = (EditText) findViewById(R.id.search_input);

		searchView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				Intent i = new Intent(MainActivity.this,
						SearchMainActivity.class);
				// i.putExtra("filePath", filePath);
				startActivity(i);
			}
		});

		findViewById(R.id.buttonPlus).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (phw == null)
					phw = new PopupWindows(MainActivity.this);
				OnClickListener pubListener = new OnClickListener() {

					@Override
					public void onClick(View v) {
						Intent i = new Intent(MainActivity.this,
								PublishActiveActivity.class);
						// i.putExtra("filePath", filePath);
						// i.putExtra("filePath", filePath);
						startActivity(i);
					}
				};
				OnClickListener inviteListener = new OnClickListener() {

					@Override
					public void onClick(View v) {
						Toast.makeText(MainActivity.this, "完善中...", 2000)
								.show();
						// 此处应该是以微信等其他第三方方式邀请朋友
						// Intent i = new Intent(MainActivity.this,
						// UserSelectActivity.class);
						// ArrayList<String> tempids = new ArrayList<String>(1);
						// tempids.add(uid);
						// i.putStringArrayListExtra("otherids", tempids);
						// startActivity(i);
					}
				};
				phw.showAddOptionsPop(v, times, pubListener, inviteListener);
			}
		});
	}

	private void updateItemList(ArrayList<Dynamic> list, boolean refresh,
			boolean append) {
		if (!list.isEmpty()) {
			mPullDownView.hasData();
			if (!append) {
				mList.clear();
			}
			mList.addAll(list);
			mAdapter.notifyDataSetChanged();
			if (mList.size() > 0) {
				mLastId = mList.get(mList.size() - 1).getStatusid();
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
			case MsgTagVO.DATA_LOAD: {
				MainHeadInfo info = null;
				if (msg.obj instanceof MainHeadInfo) {// 加载的本地数据
					info = (MainHeadInfo) msg.obj;
				} else if (msg.obj != null && !msg.obj.equals("")) {
					JsonHandler nljh = new JsonHandler((String) msg.obj,
							getApplicationContext());
					info = nljh.parseMainInfo();
					if (info != null) {
						updateAdInfo(info);
					}
				}

				break;
			}
			case MsgTagVO.DATA_REFRESH: {
				// boolean loadState = false;
				// if (msg.obj != null && !msg.obj.equals("")) {
				// loadState = true;
				// JsonHandler nljh = new JsonHandler((String) msg.obj,
				// getApplicationContext());
				// ArrayList<QuanTopicVO> list = nljh.parseQuanTopicList();
				// updateItemList(list, false, false);
				// }
				// mPullDownView.RefreshComplete(loadState);
				// break;
			}
			case MsgTagVO.DATA_MORE: {
				// mPullDownView.notifyDidMore();
				// ArrayList<QuanTopicVO> list = new ArrayList<QuanTopicVO>();
				// if (msg.obj instanceof ArrayList) {
				// list = (ArrayList<QuanTopicVO>) msg.obj;
				// } else {
				// if (msg.obj != null && !msg.obj.equals("")) {
				// JsonHandler nljh = new JsonHandler((String) msg.obj,
				// getApplicationContext());
				// list = nljh.parseQuanTopicList();
				// if (!list.isEmpty()) {
				// // infoFacade.update(list);
				// }
				// }
				// }
				// updateItemList(list, false, true);
				break;
			}
			case MsgTagVO.DATA_OTHER: {
				// if (msg.obj != null && !msg.obj.equals("")) {
				// JsonHandler nljh = new JsonHandler((String) msg.obj,
				// getApplicationContext());
				// MainHeadInfo info = nljh.parseAdInfo();
				// if (info != null) {
				// updateAdInfo(info);
				// }
				// }
				break;
			}
			case MsgTagVO.UPDATE: {
				if (msg.obj != null && !msg.obj.equals("")) {
					JsonHandler nljh = new JsonHandler((String) msg.obj,
							getApplicationContext());
					UserNewVO user = nljh.parseNewUser();
					if (null != user) {
						UserFacade facade = new UserFacade(
								getApplicationContext());
						facade.saveOrUpdate(user);
					}
				}
				break;
			}
			case MsgTagVO.DATA_BASE: {// 基础编码数据，保存到内存中\
				ResultVO res;
				if (JsonHandler.checkResult((String) msg.obj,
						getApplicationContext())) {
					res = JsonHandler.parseResult((String) msg.obj);
					mConnHelper.saveObject((String) msg.obj,
							ZhuoConnHelper.BASEDATA);
				} else {
					return;
				}
				String data = res.getData();
				BaseCodeData dataset = JsonHandler.parseBaseCodeData(data);
				mConnHelper.setBaseDataSet(dataset);
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
		// String params = ZhuoCommHelper.getUrlMsgList();
		// params += "?pageflag=" + "0";
		// params += "&reqnum=" + "10";
		// params += "&lastid=" + "0";
		// params += "&type=" + "0";
		// if (null != mSearchKey) {
		// params += "&key=" + mSearchKey.trim();
		// }
		// params += "&gongxutype=" + "0";
		// params += "&from=" + "0";
		// params += "&uid=" + uid;
		// mConnHelper.getFromServer(params, mUIHandler, MsgTagVO.DATA_REFRESH);
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

	// private void loadData() {
	//
	// mConnHelper.getBaseCodeData(mUIHandler, MsgTagVO.DATA_BASE,
	// MainActivity.this, false, null, null);
	//
	// String url = ZhuoCommHelper.getUrlUserInfo() + "?uid="
	// + ResHelper.getInstance(getApplicationContext()).getUserid();
	// // 加载刷新个人信息
	// mConnHelper.getFromServer(url, mUIHandler, MsgTagVO.UPDATE);
	// if (mPullDownView.startLoadData()) {
	// mList.clear();
	// mAdapter.notifyDataSetChanged();
	// if (CommonUtil.getNetworkState(getApplicationContext()) == 2
	// && (mSearchKey == null || mSearchKey.equals(""))) {
	// // 获取本地数据
	// ArrayList<ZhuoInfoVO> list = infoFacade.getByPage(mPage);
	// Message msg = mUIHandler.obtainMessage(MsgTagVO.DATA_LOAD);
	// msg.obj = list;
	// msg.sendToTarget();
	// } else {
	// String params = ZhuoCommHelper.getUrlMsgList();
	// params += "?pageflag=" + "0";
	// params += "&reqnum=" + "10";
	// params += "&lastid=" + "0";
	// params += "&type=" + "0";
	// if (null != mSearchKey) {
	// params += "&key=" + mSearchKey.trim();
	// }
	// params += "&gongxutype=" + "0";
	// params += "&from=" + "0";
	// params += "&uid=" + uid;
	// mConnHelper.getFromServer(params, mUIHandler,
	// MsgTagVO.DATA_LOAD);
	// }
	// }
	// }

	private void loadData() {

		if (CommonUtil.getNetworkState(getApplicationContext()) == 2
				&& (mSearchKey == null || mSearchKey.equals(""))) {
			// 获取本地数据
			// adInfo = infoFacade.getByPage(mPage);
			// Message msg = mUIHandler.obtainMessage(MsgTagVO.DATA_OTHER);
			// msg.obj = adInfo;
			// msg.sendToTarget();
		} else {
			mConnHelper.getBaseCodeData(mUIHandler, MsgTagVO.DATA_BASE,
					MainActivity.this, false, null, null);
			mConnHelper.getMainInfo(mUIHandler, MsgTagVO.DATA_LOAD, 0, 0);
		}
	}

	private void updateAdInfo(MainHeadInfo info) {

		picAd = info.getAdtop();

		imageLoader.addTask(picAd.getAdlink(), idBanner);
		imageLoader.doTask();

		ArrayList<MessagePubVO> listData = (ArrayList<MessagePubVO>) info
				.getPub();
		if (noticesListData != null)
			noticesListData.clear();
		for (int i = 0; i < listData.size(); i++) {
			noticesListData.add(listData.get(i));
		}

		antoText.setList(noticesListData);
		antoText.updateUI();

		// antoText.stopAutoText();
		// LayoutParams params=new LayoutParams(LayoutParams.MATCH_PARENT,
		// height);
		// bannerViewPager.setLayoutParams(params);

		if (hotListData != null)
			hotListData.clear();
		hotListData = info.getAdmid();
		for (int i = 0; i < 3; i++) {
			String url = hotListData.get(i).getAdpic();
			hotImages.get(i).setTag(url);
			imageLoader.addTask(url, hotImages.get(i));
		}
		imageLoader.doTask();

		ArrayList<Dynamic> list = info.getStatus();
		boolean loadState = false;
		if (info != null) {
			loadState = true;
			mPullDownView.finishLoadData(loadState);
			updateItemList(list, true, false);
		}
	}

}