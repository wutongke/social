package com.cpstudio.zhuojiaren.ui;

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
import com.cpstudio.zhuojiaren.R;
import com.cpstudio.zhuojiaren.fragment.ActivePagerAdapter;
import com.cpstudio.zhuojiaren.fragment.QuanziCreateFra;
import com.cpstudio.zhuojiaren.fragment.QuanziFra;
import com.cpstudio.zhuojiaren.model.QuanVO;
import com.cpstudio.zhuojiaren.widget.TabButton;
import com.cpstudio.zhuojiaren.widget.TabButton.PageChangeListener;
/**
 * acitivity与fragment通信,当fragment选中我圈子时，设置管理，点击管理后，操作fragment
 * @author lef
 *
 */
public class ZhuoQuanActivity extends BaseFragmentActivity {
	@InjectView(R.id.azq_tab)
	TabButton tabButton;
	@InjectView(R.id.azq_viewpager)
	ViewPager viewPager;
	private Context mContext;
	//四个fragment 方便通信
	List<Fragment> fragments;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_zhuo_quan);
		ButterKnife.inject(this);
		mContext = this;
		initTitle();
		title.setText(R.string.title_activity_zhuojiaquan);
		//设置初始值 0 管理，1搜索，2筛选，3退出
		function.setTag(0);
		function.setText(R.string.label_manage);
		//初始化tab和viewpager
		viewPager.setAdapter(getPagerAdapter());
		
		tabButton.setViewPager(viewPager);
		initOnClick();
	}
	private void initOnClick() {
		// TODO Auto-generated method stub
		//选择不同的fragment，function按键不同
		tabButton.setPageChangeListener(new PageChangeListener() {
			
			@Override
			public void onPageSelected(int arg0) {
				// TODO Auto-generated method stub
				setFunctionText(arg0);
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
		function.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				int fun = (Integer) v.getTag();
				switch(fun){
				case 0:
					((QuanziFra)(fragments.get(0))).setManager();
					function.setTag(3);
					function.setText("退出");
					break;
				case 1:
					break;
				case 2:
					ZhuoQuanActivity.this.startActivityForResult(new Intent(mContext,QuanziFilterActivity.class), 1);
					break;
				case 3:
					((QuanziFra)(fragments.get(0))).offManager();
					function.setTag(0);
					function.setText("管理");
					break;
				}
			}
		});
	}
	
	PagerAdapter getPagerAdapter() {
		 fragments = new ArrayList<Fragment>();
		List<CharSequence> titles = new ArrayList<CharSequence>();
		String quanMyTitle = getString(R.string.quanzi_my);
		String quanQueryTitle = getString(R.string.quanzi_query);
		String quanRecommendTitle = getString(R.string.quanzi_recommend);
		String quanCreateTitle = getString(R.string.quanzi_create);
	
		Fragment quanMyFragment = addBundle(new QuanziFra(),QuanVO.QUANZIMY);
		fragments.add(quanMyFragment);
		titles.add(quanMyTitle);
		
		Fragment quanQueryFragment = addBundle(new QuanziFra(), QuanVO.QUANZIQUERY);
		fragments.add(quanQueryFragment);
		titles.add(quanQueryTitle);
		
		Fragment quanRecommendFragment = addBundle(new QuanziFra(), QuanVO.QUANZIRECOMMEND);
		fragments.add(quanRecommendFragment);
		titles.add(quanRecommendTitle);
		Fragment quanCreateFragment = addBundle(new QuanziCreateFra(), QuanVO.QUANZIRECOMMEND);
		fragments.add(quanCreateFragment);
		titles.add(quanCreateTitle);
		return new ActivePagerAdapter(getSupportFragmentManager(), fragments, titles);
	}
	protected Fragment addBundle(Fragment fragment, int catlog){
		Bundle bundle = new Bundle();
		bundle.putInt(QuanVO.QUANZITYPE, catlog);
		fragment.setArguments(bundle);
		return fragment;
	}
	
	private void setFunctionText(int arg0){
		switch(arg0){
		case 0:
			function.setText("管理");
			function.setTag(0);
			break;
		case 1:
			function.setTag(1);
			ImageSpan span = new ImageSpan(mContext, R.drawable.tab_good);
			SpannableString spanStr = new SpannableString(" ");
			spanStr.setSpan(span, 0, 1,Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
			function.setText(spanStr);
			break;
		case 2:
			function.setText("筛选");
			function.setTag(2);
			break;
		case 3:
			function.setText("");
			function.setTag(3);
			break;
		}
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult( requestCode,  resultCode,  data);
		//筛选
		if(requestCode==1){
			if(resultCode==RESULT_OK){
				
			}
		}
	}
}
