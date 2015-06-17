package com.cpstudio.zhuojiaren.ui;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;

import com.cpstudio.zhuojiaren.BaseFragmentActivity;
import com.cpstudio.zhuojiaren.R;
import com.cpstudio.zhuojiaren.fragment.CommentFragment;
import com.cpstudio.zhuojiaren.fragment.MyPagerAdapter;
import com.cpstudio.zhuojiaren.fragment.PaybackFragment;
import com.cpstudio.zhuojiaren.fragment.ProgressFragment;
import com.cpstudio.zhuojiaren.imageloader.LoadImage;
import com.cpstudio.zhuojiaren.model.CrowdFundingVO;
import com.cpstudio.zhuojiaren.model.MsgTagVO;
import com.cpstudio.zhuojiaren.model.UserVO;
import com.cpstudio.zhuojiaren.util.ImageLoader;
import com.cpstudio.zhuojiaren.widget.OverScrollableScrollView;
import com.cpstudio.zhuojiaren.widget.RoundImageView;
import com.cpstudio.zhuojiaren.widget.TabButton;
import com.cpstudio.zhuojiaren.widget.TabButton.PageChangeListener;

public class CrowdFundingDetailActivity extends BaseFragmentActivity {
	@InjectView(R.id.acfd_state)
	TextView state;
	@InjectView(R.id.acfd_name)
	TextView name;
	@InjectView(R.id.acfd_des)
	TextView des;
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
	RoundImageView peopleImage;
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
	// tab
	private View mRoot;
	private MyPagerAdapter mAdapter;
	private LoadImage mLoadImage = new LoadImage();
	String[] tabTitles;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_crowd_funding_detail);
		ButterKnife.inject(this);
		initTitle();
		title.setText(R.string.crowdfungding_detail);

		// 初始化tab
		tabTitles = new String[3];
		tabTitles[0] = getString(R.string.pay_back);
		tabTitles[1] = getString(R.string.progress);
		tabTitles[2] = getString(R.string.label_cmt);
		tab.setTab(tabTitles);

		final View root = findViewById(R.id.acfd_root);
		ViewTreeObserver vto2 = root.getViewTreeObserver();
		vto2.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
			@Override
			public void onGlobalLayout() {
				root.getViewTreeObserver().removeGlobalOnLayoutListener(this);
				int height = root.getHeight();
				init();
			}
		});

		mRoot = root;
		loadData();

	}

	/**
	 * 计算高度
	 */
	private void init() {
		LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) viewPager
				.getLayoutParams();
		params.height = mRoot.getHeight() - tab.getHeight() - 50;
		viewPager.setLayoutParams(params);
		mAdapter = new MyPagerAdapter(getSupportFragmentManager(), tabTitles);
		viewPager.setAdapter(mAdapter);
		tab.clearTab();
		tab.setViewPager(viewPager);
		//第一个可用
		PaybackFragment fragment = (PaybackFragment) mAdapter
				.getItem(0);
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
		// test
		Message msg = uiHandler.obtainMessage();
		msg.what = MsgTagVO.INIT;
		CrowdFundingVO result = new CrowdFundingVO();
		result.setState("进行中");
		result.setName("三星S6钢铁侠定制版");
		result.setDes("参与1元抢三星S6的用户请注意：① 请登录京东众筹【三星S6钢铁侠限量版】项目页面参与活动。②每日得奖的用户名单都会在H5的“S6拼抢榜”内显示。③每日10点到次日10点为榜单统计时间，活动暂时锁定。次日中午12:00在活动H5页面的“S6拼抢榜”公布最终获奖名单！");
		result.setMoneyAim("10000");
		result.setMoneyGet("8000");
		result.setEndDay("49");
		result.setLikeCount("20");
		result.setSupportCount("50");
		UserVO user = new UserVO();
		user.setUheader("https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=2222979786,259610352&fm=116&gp=0.jpg");
		user.setUsername("jack");
		user.setPost("aa");
		user.setCompany("BUPT");
		result.setBoss(user);
		msg.obj = result;
		msg.sendToTarget();
	}

	Handler uiHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case MsgTagVO.INIT:
				CrowdFundingVO result = new CrowdFundingVO();
				if (msg.obj instanceof CrowdFundingVO)
					result = (CrowdFundingVO) msg.obj;
				state.setText(result.getState());
				name.setText(result.getName());
				des.setText(result.getDes());
				
				// 完成率
				int aim = Integer.parseInt(result.getMoneyAim());
				int get = Integer.parseInt(result.getMoneyGet());
				int rate = (int) (get / (float) aim * 100);
				finishRate.setText(rate + "%");
				setWeight(finishRateImage, (float) rate);
				setWeight(finishDayImage, (float) (100 - rate));
				finishDay.setText(result.getEndDay() + "天");
				totalRmb.setText("￥" + result.getMoneyGet());
				aimRmb.setText("￥" + result.getMoneyAim());
				likeCount.setText(result.getLikeCount());
				supportCount.setText(result.getSupportCount());
				peopleName.setText(result.getBoss().getUsername());
				peopleCompany.setText(result.getBoss().getCompany());
				peoplePostion.setText(result.getBoss().getPost());
				mLoadImage.addTask(result.getBoss().getUheader(), peopleImage);
				mLoadImage.doTask();
			}
		}
	};

	// 设置view 的weight
	private void setWeight(View view, float weight) {
		view.setLayoutParams(new LinearLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, 10, weight));
	}
}
