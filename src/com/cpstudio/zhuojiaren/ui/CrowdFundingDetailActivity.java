package com.cpstudio.zhuojiaren.ui;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;

import com.cpstudio.zhuojiaren.BaseFragmentActivity;
import com.cpstudio.zhuojiaren.PhotoViewMultiActivity;
import com.cpstudio.zhuojiaren.R;
import com.cpstudio.zhuojiaren.R.color;
import com.cpstudio.zhuojiaren.fragment.CommentFragment;
import com.cpstudio.zhuojiaren.fragment.MyPagerAdapter;
import com.cpstudio.zhuojiaren.fragment.PaybackFragment;
import com.cpstudio.zhuojiaren.fragment.ProgressFragment;
import com.cpstudio.zhuojiaren.helper.AppClientLef;
import com.cpstudio.zhuojiaren.helper.JsonHandler;
import com.cpstudio.zhuojiaren.helper.ZhuoCommHelper;
import com.cpstudio.zhuojiaren.imageloader.LoadImage;
import com.cpstudio.zhuojiaren.model.CrowdFundingDes;
import com.cpstudio.zhuojiaren.model.CrowdFundingVO;
import com.cpstudio.zhuojiaren.model.MsgTagVO;
import com.cpstudio.zhuojiaren.model.ResultVO;
import com.cpstudio.zhuojiaren.util.CommonUtil;
import com.cpstudio.zhuojiaren.util.DeviceInfoUtil;
import com.cpstudio.zhuojiaren.util.ImageLoader;
import com.cpstudio.zhuojiaren.widget.OverScrollableScrollView;
import com.cpstudio.zhuojiaren.widget.RoundImageView;
import com.cpstudio.zhuojiaren.widget.TabButton;
import com.cpstudio.zhuojiaren.widget.TabButton.PageChangeListener;
import com.google.gson.Gson;

/***
 * 众筹详情
 * 
 * @author lef
 * 
 */
public class CrowdFundingDetailActivity extends BaseFragmentActivity {
	@InjectView(R.id.acfd_state)
	TextView state;
	@InjectView(R.id.acfd_name)
	TextView name;
	@InjectView(R.id.acfd_des)
	LinearLayout des;
	@InjectView(R.id.acfd_finish_rate)
	TextView finishRate;
	@InjectView(R.id.acfd_finish_day)
	TextView finishDay;
	@InjectView(R.id.acfd_finish_rate_image)
	ImageView finishRateImage;
	@InjectView(R.id.acfd_finish_day_image)
	ImageView finishDayImage;
	@InjectView(R.id.acfd_total_rmb)
	TextView totalRmb;
	@InjectView(R.id.acfd_aim_rmb)
	TextView aimRmb;
	@InjectView(R.id.acfd_like_count)
	TextView likeCount;
	@InjectView(R.id.acfd_support_count)
	TextView supportCount;
	@InjectView(R.id.acfd_people_image)
	ImageView peopleImage;
	@InjectView(R.id.acfd_people_name)
	TextView peopleName;
	@InjectView(R.id.acfd_people_position)
	TextView peoplePostion;
	@InjectView(R.id.acfd_people_company)
	TextView peopleCompany;
	@InjectView(R.id.acfd_viewpager)
	ViewPager viewPager;
	@InjectView(R.id.acfd_tab)
	TabButton tab;
	@InjectView(R.id.acfd_scrollview)
	OverScrollableScrollView scorllView;
	@InjectView(R.id.acfd_like_image)
	ImageView likeImage;
	@InjectView(R.id.acfd_support_image)
	ImageView supportImage;
	// tab
	private View mRoot;
	private MyPagerAdapter mAdapter;
	private LoadImage mLoadImage ;
	String[] tabTitles;
	AppClientLef appClient;
	private String crowdFundingId;
	private Context mContext;
	private ArrayList<ImageView> IVList = new ArrayList<ImageView>();
	private CrowdFundingVO crowdFunding;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_crowd_funding_detail);
		ButterKnife.inject(this);
		initTitle();
		mContext = this;
		
		mLoadImage = LoadImage.getInstance();
		crowdFundingId = getIntent().getStringExtra(
				CrowdFundingVO.CROWDFUNDINGID);
		title.setText(R.string.crowdfungding_detail);
		appClient = AppClientLef.getInstance(CrowdFundingDetailActivity.this);
		loadData();

	}

	private void initTab() {
		// 初始化tab
		tabTitles = new String[3];
		tabTitles[0] = getString(R.string.pay_back);
		tabTitles[1] = getString(R.string.label_cmt);
		tabTitles[2] = getString(R.string.progress);
		tab.setTab(tabTitles);

		final View root = findViewById(R.id.acfd_root);
		ViewTreeObserver vto2 = root.getViewTreeObserver();
		vto2.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
			@Override
			public void onGlobalLayout() {
				root.getViewTreeObserver().removeGlobalOnLayoutListener(this);
				init();
			}
		});

		mRoot = root;
	}

	/**
	 * 计算高度
	 */
	private void init() {
		LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) viewPager
				.getLayoutParams();
		
		params.height = mRoot.getHeight() - tab.getHeight() - DeviceInfoUtil.dip2px(CrowdFundingDetailActivity.this, 40);
		viewPager.setLayoutParams(params);
		mAdapter = new MyPagerAdapter(getSupportFragmentManager(), tabTitles,crowdFundingId,
				crowdFunding);
		viewPager.setAdapter(mAdapter);
		tab.clearTab();
		tab.setViewPager(viewPager);
		// 第一个可用
		PaybackFragment fragment = (PaybackFragment) mAdapter.getItem(0);
		scorllView.setController(fragment);
		tab.setPageChangeListener(new PageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {
				// TODO Auto-generated method stub

				switch (arg0) {
				case 0:
					PaybackFragment fragment = (PaybackFragment) mAdapter
							.getItem(arg0);
					scorllView.setController(fragment);
					break;
				case 1:
					CommentFragment fragment1 = (CommentFragment) mAdapter
							.getItem(arg0);
					scorllView.setController(fragment1);
					break;
				case 2:

					ProgressFragment fragment2 = (ProgressFragment) mAdapter
							.getItem(arg0);
					scorllView.setController(fragment2);
					break;
				}
			}

			@Override
			public void onPageScrolled(int index, float arg1, int scroll) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onPageScrollStateChanged(int state) {
				// TODO Auto-generated method stub

			}
		});
		// 数据加载完成后，滚动到顶部
		uiHandler.post(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				scorllView.scrollTo(0, 0);
			}
		});
	}

	private void loadData() {
		// TODO Auto-generated method stub
		ImageView ad = (ImageView)findViewById(R.id.acfd_advertisiment);
		LoadImage.getInstance().beginLoad("http://7xkb2a.com1.z0.glb.clouddn.com/android-gg.png", ad);
		appClient.getCrowdFunding(CrowdFundingDetailActivity.this, uiHandler,
				MsgTagVO.INIT,
				getIntent().getStringExtra(CrowdFundingVO.CROWDFUNDINGID));
		// appClient.getCrowdFunding(CrowdFundingDetailActivity.this, uiHandler,
		// MsgTagVO.INIT, "17");
	}

	Handler uiHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case MsgTagVO.INIT:
				ResultVO res;
				if (JsonHandler.checkResult((String) msg.obj,
						CrowdFundingDetailActivity.this)) {
					res = JsonHandler.parseResult((String) msg.obj);
				} else {
					CommonUtil.displayToast(CrowdFundingDetailActivity.this,
							R.string.data_error);
					return;
				}
				Gson gson = new Gson();
				try {
					crowdFunding = gson.fromJson(res.getData(), CrowdFundingVO.class);
				} catch (Exception e) {
					// TODO: handle exception
					CommonUtil.displayToast(CrowdFundingDetailActivity.this,
							R.string.data_error);
					Log.d("Debug", "json数据出错。。。。。。。。。。。。。。");
					return;
				}
				initTab();
				tab.setVisibility(View.VISIBLE);
				if (Integer.parseInt(crowdFunding.getRemainDay()) >= 0) {

					state.setText("进行中");
				} else {
					state.setText("已完成");
				}
				name.setText(crowdFunding.getTitle());
				for (CrowdFundingDes temp : crowdFunding.getDescription()) {
					if (temp.getType().equals("text")) {
						addTextView(temp.getContent());
					} else if (temp.getType().equals("text")) {
						addImage(temp.getContent());
					}
				}

				// 完成率
				int aim = Integer.parseInt(crowdFunding.getTargetZb());
				int get = Integer.parseInt(crowdFunding.getReach());
				int rate = (int) (get / (float) aim * 100);
				finishRate.setText(rate + "%");
				setWeight(finishRateImage, (float) rate);
				setWeight(finishDayImage, (float) (100 - rate));
				finishDay.setText(crowdFunding.getRemainDay() + "天");
				totalRmb.setText("￥" + crowdFunding.getReach());
				aimRmb.setText("￥" + crowdFunding.getTargetZb());
				likeCount.setText(crowdFunding.getLikeNum());
				supportCount.setText(crowdFunding.getSupportNum());
				peopleName.setText(crowdFunding.getName());
				peopleCompany.setText(crowdFunding.getCompany());
				peoplePostion.setText(crowdFunding.getPosition());
				mLoadImage.beginLoad(crowdFunding.getUheader(), peopleImage);

				if (crowdFunding.getIsLike().equals(CrowdFundingVO.likeOrSupport)) {
					likeImage.setBackgroundResource(R.drawable.zcollect);
				} else {
					likeImage.setBackgroundResource(R.drawable.zuncollect);
				}
				if (crowdFunding.getIsSupport().equals(CrowdFundingVO.likeOrSupport)) {
					supportImage
							.setBackgroundResource(R.drawable.zhan2_crowd_cmt);
				} else {
					supportImage
							.setBackgroundResource(R.drawable.zhan_crowd_cmt);
				}
				likeImage.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						appClient.collection((Activity)mContext,
								ZhuoCommHelper.getLikecrowdfunding(), "id",
								crowdFunding.getId(), "", "");
						if (crowdFunding.getIsLike().equals(
								CrowdFundingVO.likeOrSupport)) {
							crowdFunding.setIsLike(CrowdFundingVO.NOlikeOrSupport);
							likeImage
									.setBackgroundResource(R.drawable.zuncollect);

						} else {
							crowdFunding.setIsLike(CrowdFundingVO.likeOrSupport);
							likeImage
									.setBackgroundResource(R.drawable.zcollect);
						}
					}
				});
			}
		}
	};

	// 设置view 的weight
	private void setWeight(View view, float weight) {
		view.setLayoutParams(new LinearLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, 10, weight));
	}

	private void addImage(String path) {
		ImageView iv = new ImageView(mContext);
		WindowManager wm = (WindowManager) mContext
				.getSystemService(WINDOW_SERVICE);
		DisplayMetrics metric = new DisplayMetrics();
		wm.getDefaultDisplay().getMetrics(metric);
		int width = metric.widthPixels;
		int height = width * 9 / 10;
		LayoutParams lp = new LayoutParams(width, height);
		iv.setLayoutParams(lp);
		mLoadImage.beginLoad(path, iv);
		iv.setTag(path);
		iv.setScaleType(ScaleType.CENTER_CROP);
		iv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(mContext,
						PhotoViewMultiActivity.class);
				ArrayList<String> orgs = new ArrayList<String>();
				for (ImageView iv : IVList) {
					orgs.add((String) iv.getTag());
				}
				intent.putStringArrayListExtra("pics", orgs);
				intent.putExtra("pic", (String) v.getTag());
				intent.putExtra("type", "network");
				mContext.startActivity(intent);
			}
		});
		des.addView(iv);
	}

	@SuppressLint("ResourceAsColor")
	private void addTextView(String text) {
		TextView iv = new TextView(mContext);
		LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT);
		iv.setLayoutParams(lp);
		// iv.setBackgroundResource(R.color.graywhitem);
		iv.setTextColor(color.graywhite);
		iv.setText(text);
		iv.setTextSize(18f);
		des.addView(iv);
	}
}
