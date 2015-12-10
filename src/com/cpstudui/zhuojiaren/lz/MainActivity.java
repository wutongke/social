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
import android.view.Gravity;
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

import com.cpstudio.zhuojiaren.JiarenActiveActivity;
import com.cpstudio.zhuojiaren.PublishActiveActivity;
import com.cpstudio.zhuojiaren.R;
import com.cpstudio.zhuojiaren.facade.InfoFacade;
import com.cpstudio.zhuojiaren.helper.JsonHandler;
import com.cpstudio.zhuojiaren.helper.ResHelper;
import com.cpstudio.zhuojiaren.helper.ZhuoConnHelper;
import com.cpstudio.zhuojiaren.imageloader.LoadImage;
import com.cpstudio.zhuojiaren.model.Comment;
import com.cpstudio.zhuojiaren.model.Dynamic;
import com.cpstudio.zhuojiaren.model.GoodsPicAdVO;
import com.cpstudio.zhuojiaren.model.MainHeadInfo;
import com.cpstudio.zhuojiaren.model.MessagePubVO;
import com.cpstudio.zhuojiaren.model.MsgTagVO;
import com.cpstudio.zhuojiaren.model.PicAdVO;
import com.cpstudio.zhuojiaren.ui.GoodsDetailLActivity;
import com.cpstudio.zhuojiaren.ui.PubDetailActivity;
import com.cpstudio.zhuojiaren.util.CommonUtil;
import com.cpstudio.zhuojiaren.util.DeviceInfoUtil;
import com.cpstudio.zhuojiaren.widget.CustomShareBoard;
import com.cpstudio.zhuojiaren.widget.PopupWindows;
import com.cpstudio.zhuojiaren.widget.PullDownView;
import com.cpstudio.zhuojiaren.widget.PullDownView.OnPullDownListener;
import com.external.viewpagerindicator.PageIndicator;
import com.umeng.socialize.media.UMImage;

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
	LoadImage imageLoader = LoadImage.getInstance();

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
//		mPullDownView.setHideHeader();
		mPullDownView.setShowFooter(false);
		mPullDownView.noFoot();

		initClick();
		initHeadView();
//		loadData();
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
		for (int i = 0; i < drawId.length; i++) {
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
						startActivity(i);
					}
				};
				OnClickListener inviteListener = new OnClickListener() {

					@Override
					public void onClick(View v) {
						CustomShareBoard cb = new CustomShareBoard(
								MainActivity.this);
						cb.showCustomShareContent();
					}
				};
				phw.showAddOptionsPop(v, times, pubListener, inviteListener);
			}
		});
	}

	private void updateItemList(ArrayList<Dynamic> list, boolean refresh,
			boolean append) {
		// mPullDownView.noData(false);
		if (!list.isEmpty()) {
			// mPullDownView.hasData();
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
			// mPullDownView.noData(!refresh);
		}
	}

	@SuppressLint("HandlerLeak")
	private Handler mUIHandler = new Handler() {
		@SuppressWarnings("unchecked")
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MsgTagVO.DATA_LOAD: {
				boolean loadState=false;
				MainHeadInfo info = null;
				if (msg.obj instanceof MainHeadInfo) {// 加载的本地数据
					info = (MainHeadInfo) msg.obj;
				} else if (msg.obj != null && !msg.obj.equals("")) {
					JsonHandler nljh = new JsonHandler((String) msg.obj,
							getApplicationContext());
					info = nljh.parseMainInfo();
					if (info != null) {
						loadState=true;
						
						updateAdInfo(info);
					}
				}
				mPullDownView.finishLoadData(loadState);
				break;
			}
			case MsgTagVO.DATA_REFRESH: {
				break;
			}

			}
		}

	};

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		if (id != -1) {
			Intent i = new Intent(MainActivity.this,
					DynamicDetailActivity.class);
			i.putExtra("msgid", (String) view.getTag(R.id.tag_id));
			startActivity(i);
		}
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == Activity.RESULT_OK) {
			if (requestCode == MsgTagVO.MSG_CMT) {
				Toast.makeText(MainActivity.this, "评论成功！", 2000).show();
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
	// refresh刷新加载的新的数据没有写数据库
	@Override
	public void onRefresh() {
		loadData();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		loadData();
		super.onResume();
	}
	
	private void loadData() {

		if (CommonUtil.getNetworkState(getApplicationContext()) == 2
				&& (mSearchKey == null || mSearchKey.equals(""))) {
			// 获取本地数据
			// adInfo = infoFacade.getByPage(mPage);
			// Message msg = mUIHandler.obtainMessage(MsgTagVO.DATA_OTHER);
			// msg.obj = adInfo;
			// msg.sendToTarget();
		} else {
			mConnHelper.getMainInfo(mUIHandler, MsgTagVO.DATA_LOAD, 0, 0);
		}
	}

	private void updateAdInfo(MainHeadInfo info) {

		picAd = info.getAdtop();

		imageLoader.beginLoad(picAd.getAdpic(), idBanner);

		ArrayList<MessagePubVO> listData = (ArrayList<MessagePubVO>) info
				.getPub();
		if (noticesListData != null)
			noticesListData.clear();
		for (int i = 0; i < listData.size(); i++) {
			noticesListData.add(listData.get(i));
		}
		antoText.stopAutoText();
		antoText.setList(noticesListData);
		
		antoText.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String id = antoText.getCurrentId();
				Intent i = new Intent(MainActivity.this,
						PubDetailActivity.class);
				i.putExtra("id", id);
				startActivity(i);
			}
		});
		antoText.updateUI();
		
		if (hotListData != null)
			hotListData.clear();
		hotListData = info.getAdmid();
		for (int i = 0; i < 3; i++) {
			String url = hotListData.get(i).getAdpic();
			final String id = hotListData.get(i).getGoodsid();
			hotImages.get(i).setTag(url);
			imageLoader.beginLoad(url, hotImages.get(i));

			hotImages.get(i).setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent i = new Intent(MainActivity.this,
							GoodsDetailLActivity.class);
					i.putExtra("goodsId", id);
					startActivity(i);
				}
			});
		}

		ArrayList<Dynamic> list = info.getStatus();
		// boolean loadState = false;
		if (info != null) {
			// loadState = true;
			// mPullDownView.finishLoadData(loadState);
			updateItemList(list, true, false);
		}
	}

	@Override
	public void onMore() {
		// TODO Auto-generated method stub

	}

}