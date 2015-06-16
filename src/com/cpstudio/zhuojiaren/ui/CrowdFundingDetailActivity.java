package com.cpstudio.zhuojiaren.ui;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;

import com.cpstudio.zhuojiaren.BaseFragmentActivity;
import com.cpstudio.zhuojiaren.R;
import com.cpstudio.zhuojiaren.fragment.ActivePagerAdapter;
import com.cpstudio.zhuojiaren.fragment.CrowdFundingFragment;
import com.cpstudio.zhuojiaren.fragment.PaybackFragment;
import com.cpstudio.zhuojiaren.fragment.PaybackFragment.ViewPagerHeight;
import com.cpstudio.zhuojiaren.model.CrowdFundingVO;
import com.cpstudio.zhuojiaren.model.MsgTagVO;
import com.cpstudio.zhuojiaren.model.UserVO;
import com.cpstudio.zhuojiaren.widget.PagerScrollView;
import com.cpstudio.zhuojiaren.widget.RoundImageView;
import com.cpstudio.zhuojiaren.widget.TabButton;
import com.cpstudio.zhuojiaren.widget.TabButton.PageChangeListener;
import com.cpstudio.zhuojiaren.widget.TabButton.TabsButtonOnClickListener;

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
	PagerScrollView scorllView;
	//滚动到顶部
	private Runnable scrollToTop;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_crowd_funding_detail);
		ButterKnife.inject(this);
		initTitle();
		title.setText(R.string.crowdfungding_detail);
		initOnclick();
		loadData();
	}

	private void initOnclick() {
		// TODO Auto-generated method stub
		scrollToTop = new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				scorllView.scrollTo(0, 0);
			}
		};
		viewPager.setAdapter(getPagerAdapter());
		tab.setViewPager(viewPager);
		//设置点击监听,主要是让scroll滚动到顶部
		tab.setTabsButtonOnClickListener(new TabsButtonOnClickListener() {
			
			@Override
			public void tabsButtonOnClick(int id, View v) {
				// TODO Auto-generated method stub
				uiHandler.post(scrollToTop);
				viewPager.setCurrentItem(v.getId());
			}
		});
		tab.setPageChangeListener(new PageChangeListener() {
			
			@Override
			public void onPageSelected(int arg0) {
				// TODO Auto-generated method stub
			}
			
			@Override
			public void onPageScrolled(int index, float arg1, int scroll) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onPageScrollStateChanged(int state) {
				// TODO Auto-generated method stub
				uiHandler.post(scrollToTop);
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
		result.setName("ceshi");
		result.setDes("asdf;lasjkdgl;aksdj;");
		result.setMoneyAim("10000");
		result.setMoneyGet("8000");
		result.setEndDay("49");
		result.setLikeCount("20");
		result.setSupportCount("50");
		UserVO user = new UserVO();
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
			}
		}
	};

	PagerAdapter getPagerAdapter() {

		ArrayList<Fragment> fragments = new ArrayList<Fragment>();
		List<CharSequence> titles = new ArrayList<CharSequence>();
		String payBack = getString(R.string.pay_back);
		String progress = getString(R.string.progress);
		String comment = getString(R.string.label_cmt);

		Fragment payBackFragment = addBundle(new PaybackFragment(),
				CrowdFundingVO.CROWDFUNDINGMY);
		fragments.add(payBackFragment);
		titles.add(payBack);
		((PaybackFragment) payBackFragment)
				.setViewPagerHeight(new ViewPagerHeight() {

					@Override
					public void onHeightChange(int height) {
						// TODO Auto-generated method stub
						ViewGroup.LayoutParams lp = viewPager.getLayoutParams();
						lp.height = height;
						viewPager.setLayoutParams(lp);
						uiHandler.post(new Runnable() {

							@Override
							public void run() {
								// TODO Auto-generated method stub
								scorllView.scrollTo(0, 0);
							}
						});
					}
				});

		Fragment quanCreateFragment = addBundle(new PaybackFragment(),
				CrowdFundingVO.CROWDFUNDINGQUERY);
		fragments.add(quanCreateFragment);
		titles.add(comment);
		((PaybackFragment) quanCreateFragment)
				.setViewPagerHeight(new ViewPagerHeight() {

					@Override
					public void onHeightChange(int height) {
						// TODO Auto-generated method stub
						ViewGroup.LayoutParams lp = viewPager.getLayoutParams();
						lp.height = height;
						viewPager.setLayoutParams(lp);
						uiHandler.post(new Runnable() {

							@Override
							public void run() {
								// TODO Auto-generated method stub
								scorllView.scrollTo(0, 0);
							}
						});
					}
				});
		Fragment quanRecommendFragment = addBundle(new PaybackFragment(),
				CrowdFundingVO.CROWDFUNDINGQUERY);
		fragments.add(quanRecommendFragment);
		titles.add(progress);
		((PaybackFragment) quanRecommendFragment)
				.setViewPagerHeight(new ViewPagerHeight() {

					@Override
					public void onHeightChange(int height) {
						// TODO Auto-generated method stub
						ViewGroup.LayoutParams lp = viewPager.getLayoutParams();
						lp.height = height;
						viewPager.setLayoutParams(lp);
						uiHandler.post(new Runnable() {

							@Override
							public void run() {
								// TODO Auto-generated method stub
								scorllView.scrollTo(0, 0);
							}
						});
					}
				});
		return new ActivePagerAdapter(getSupportFragmentManager(), fragments,
				titles);
	}

	protected Fragment addBundle(Fragment fragment, int catlog) {
		Bundle bundle = new Bundle();
		bundle.putInt(CrowdFundingVO.CROWDFUNDINGTYPE, catlog);
		fragment.setArguments(bundle);
		return fragment;
	}

	// 设置view 的weight
	private void setWeight(View view, float weight) {
		view.setLayoutParams(new LinearLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, 10, weight));
	}
}
