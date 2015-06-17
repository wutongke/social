package com.cpstudui.zhuojiaren.lz;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.view.View;
import android.view.View.OnClickListener;
import butterknife.ButterKnife;
import butterknife.InjectView;

import com.cpstudio.zhuojiaren.BaseFragmentActivity;
import com.cpstudio.zhuojiaren.PublishResourceActivity;
import com.cpstudio.zhuojiaren.R;
import com.cpstudio.zhuojiaren.fragment.ActivePagerAdapter;
import com.cpstudio.zhuojiaren.fragment.QuanziCreateFra;
import com.cpstudio.zhuojiaren.fragment.QuanziFra;
import com.cpstudio.zhuojiaren.fragment.ResourceGXFragment;
import com.cpstudio.zhuojiaren.model.QuanVO;
import com.cpstudio.zhuojiaren.model.ResourceGXVO;
import com.cpstudio.zhuojiaren.ui.PublishCrowdFundingActivity;
import com.cpstudio.zhuojiaren.ui.ResourceGXFilterActivity;
import com.cpstudio.zhuojiaren.widget.TabButton;
import com.cpstudio.zhuojiaren.widget.TabButton.PageChangeListener;
import com.cpstudio.zhuojiaren.widget.TabButton.TabsButtonOnClickListener;

/**
 * acitivity与fragment通信,当fragment选中我圈子时，设置管理，点击管理后，操作fragment
 * 
 * @author lef
 * 
 */
public class ResourceGXActivity extends BaseFragmentActivity {
	@InjectView(R.id.azq_tab)
	TabButton tabButton;
	@InjectView(R.id.azq_viewpager)
	ViewPager viewPager;
	private Context mContext;
	// 四个fragment 方便通信
	List<Fragment> fragments;
	int currentFragmentIndex=0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_resource_gx);
		ButterKnife.inject(this);
		mContext = this;
		initTitle();
		title.setText(R.string.mp_gongxu);
		function.setText(R.string.label_filter2);
		// 初始化tab和viewpager
		viewPager.setAdapter(getPagerAdapter());
		tabButton.setViewPager(viewPager);
		initOnClick();
	}

	private void initOnClick() {
		// TODO Auto-generated method stub
		tabButton.setPageChangeListener(new PageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {
				// TODO Auto-generated method stub
				currentFragmentIndex=arg0;
				if(arg0==2){
					//不展示第三个page
					viewPager.setCurrentItem(1);
					mContext.startActivity(new Intent(mContext,PublishResourceActivity.class));
				//跳转到供需发布页面
				}
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub

			}
		});
		
		tabButton.setTabsButtonOnClickListener(new TabsButtonOnClickListener() {
			
			@Override
			public void tabsButtonOnClick(int id, View v) {
				// TODO Auto-generated method stub
				if((Integer)(v.getTag())==2)
					mContext.startActivity(new Intent(mContext,PublishResourceActivity.class));
				else{
					viewPager.setCurrentItem((Integer)v.getTag());
				}
			}
		});
		
		function.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
	//			跳转到筛选页面
				Intent i=new Intent(ResourceGXActivity.this,ResourceGXFilterActivity.class);
				i.putExtra(ResourceGXVO.RESOURCEGXTYPE, currentFragmentIndex);
				startActivityForResult(i, currentFragmentIndex);
			}
		});
	}

	PagerAdapter getPagerAdapter() {
		fragments = new ArrayList<Fragment>();
		List<CharSequence> titles = new ArrayList<CharSequence>();

		String searchResourceTitle = getString(R.string.title_search_resource);
		String findneedTitle = getString(R.string.title_find_need);
		String pubgxTitle = getString(R.string.title_pub_gx);

		Fragment quanMyFragment = addBundle(new ResourceGXFragment(),
				ResourceGXVO.RESOURCE_FIND);
		fragments.add(quanMyFragment);
		titles.add(searchResourceTitle);

		Fragment quanQueryFragment = addBundle(new ResourceGXFragment(),
				ResourceGXVO.NEED_FIND);
		fragments.add(quanQueryFragment);
		titles.add(findneedTitle);


		Fragment quanRecommendFragment = addBundle(new ResourceGXFragment(),
				ResourceGXVO.PUB_NEED_RESOURCE);
		fragments.add(quanRecommendFragment);
		titles.add(pubgxTitle);

		return new ActivePagerAdapter(getSupportFragmentManager(), fragments,
				titles);
	}

	protected Fragment addBundle(Fragment fragment, int catlog) {
		Bundle bundle = new Bundle();
		bundle.putInt(ResourceGXVO.RESOURCEGXTYPE, catlog);
		fragment.setArguments(bundle);
		return fragment;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		// 筛选
		if (resultCode == RESULT_OK) {
			int type=data.getIntExtra(ResourceGXVO.RESOURCEGXFILTER_TYPE, 0);
			String loc=data.getStringExtra(ResourceGXVO.RESOURCEGXFILTER_LOCATION);
			String subType=data.getStringExtra(ResourceGXVO.RESOURCEGXFILTER_TYPE);
			((ResourceGXFragment)(fragments.get(currentFragmentIndex))).filterData(loc, subType);
		}
		
	}
}
