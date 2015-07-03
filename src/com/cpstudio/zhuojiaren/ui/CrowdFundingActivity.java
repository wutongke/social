package com.cpstudio.zhuojiaren.ui;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import butterknife.ButterKnife;
import butterknife.InjectView;

import com.cpstudio.zhuojiaren.BaseFragmentActivity;
import com.cpstudio.zhuojiaren.R;
import com.cpstudio.zhuojiaren.fragment.ActivePagerAdapter;
import com.cpstudio.zhuojiaren.fragment.CrowdFundingFragment;
import com.cpstudio.zhuojiaren.model.CrowdFundingVO;
import com.cpstudio.zhuojiaren.widget.TabButton;
import com.cpstudio.zhuojiaren.widget.TabButton.PageChangeListener;
import com.cpstudio.zhuojiaren.widget.TabButton.TabsButtonOnClickListener;
/**
 * 众筹主页
 * @author lef
 *
 */
public class CrowdFundingActivity extends BaseFragmentActivity {
	@InjectView(R.id.acf_tab)
	TabButton tab;
	@InjectView(R.id.acf_viewpager)
	ViewPager viewPager;
	private Context mContext;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_crowd_funding);
		ButterKnife.inject(this);
		mContext = this;
		initTitle();
		title.setText(R.string.crowdfunding);
		function.setBackgroundResource(R.drawable.magnifiercrowd);
		// 初始化tab和viewpager
		viewPager.setAdapter(getPagerAdapter());

		tab.setViewPager(viewPager);
		//点击创建
		tab.setPageChangeListener(new PageChangeListener() {
			
			@Override
			public void onPageSelected(int arg0) {
				// TODO Auto-generated method stub
				if(arg0==2){
					//不展示第三个page
					viewPager.setCurrentItem(1);
					mContext.startActivity(new Intent(mContext,PublishCrowdFundingActivity.class));
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
		tab.setTabsButtonOnClickListener(new TabsButtonOnClickListener() {
			
			@Override
			public void tabsButtonOnClick(int id, View v) {
				// TODO Auto-generated method stub
				if((Integer)(v.getTag())==2)
					mContext.startActivity(new Intent(mContext,PublishCrowdFundingActivity.class));
				else{
					viewPager.setCurrentItem((Integer)v.getTag());
				}
			}
		});
	}

	PagerAdapter getPagerAdapter() {

		ArrayList<Fragment> fragments = new ArrayList<Fragment>();
		List<CharSequence> titles = new ArrayList<CharSequence>();
		String crowdfundingQueryTitle = getString(R.string.crowdfunding_my);
		String crowdfundingRecommendTitle = getString(R.string.crowdfunding_query);
		String crowdfundingCreateTitle = getString(R.string.crowdfunding_create);


		Fragment quanQueryFragment = addBundle(new CrowdFundingFragment(),
				CrowdFundingVO.CROWDFUNDINGMY);
		fragments.add(quanQueryFragment);
		titles.add(crowdfundingQueryTitle);

		Fragment quanRecommendFragment = addBundle(new CrowdFundingFragment(),
				CrowdFundingVO.CROWDFUNDINGQUERY);
		fragments.add(quanRecommendFragment);
		titles.add(crowdfundingRecommendTitle);
		Fragment quanCreateFragment = addBundle(new CrowdFundingFragment(),
				CrowdFundingVO.CROWDFUNDINGQUERY);
		fragments.add(quanCreateFragment);
		titles.add(crowdfundingCreateTitle);
		return new ActivePagerAdapter(getSupportFragmentManager(), fragments,
				titles);
	}

	protected Fragment addBundle(Fragment fragment, int catlog) {
		Bundle bundle = new Bundle();
		bundle.putInt(CrowdFundingVO.CROWDFUNDINGTYPE, catlog);
		fragment.setArguments(bundle);
		return fragment;
	}

}
