package com.cpstudio.zhuojiaren.ui;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import butterknife.ButterKnife;
import butterknife.InjectView;

import com.cpstudio.zhuojiaren.R;
import com.cpstudio.zhuojiaren.fragment.ActivePagerAdapter;
import com.cpstudio.zhuojiaren.fragment.QuanziCreateFra;
import com.cpstudio.zhuojiaren.fragment.QuanziFra;
import com.cpstudio.zhuojiaren.model.QuanVO;
import com.cpstudio.zhuojiaren.widget.TabButton;
import com.cpstudio.zhuojiaren.widget.TabButton.PageChangeListener;

/**
 * @author lef
 * 
 */
public class ZhuoQuanActivity extends BaseFragmentActivity {
	@InjectView(R.id.azq_tab)
	TabButton tabButton;
	@InjectView(R.id.azq_viewpager)
	ViewPager viewPager;
	private Context mContext;
	// 四个fragment 方便通信
	List<Fragment> fragments;
	String userid = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_zhuo_quan);
		ButterKnife.inject(this);
		mContext = this;
		initTitle();
		title.setText(R.string.title_activity_zhuojiaquan);
		int selected=getIntent().getIntExtra("selected", 2);
		userid = getIntent().getStringExtra("userid");
		if (userid == null)//查看的是别人的不需要管理
			function.setVisibility(View.GONE);
		else
		{
		// 设置初始值 0 管理，1搜索，2筛选，3退出
		function.setTag(0);
		function.setTextSize(14);
		function.setBackgroundResource(R.drawable.ibutton);
		function.setText(R.string.label_manage);
		}
		// 初始化tab和viewpager
		viewPager.setAdapter(getPagerAdapter());
		viewPager.setOffscreenPageLimit(2);
		viewPager.setCurrentItem(selected);
		tabButton.setViewPager(viewPager);
		tabButton.setTabBackgroundByIndex(selected);
		initOnClick();
	}

	private void initOnClick() {
		// TODO Auto-generated method stub
		tabButton.setPageChangeListener(new PageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {
				// TODO Auto-generated method stub
				setFunctionText(arg0);
				((QuanziFra) (fragments.get(0))).offManager();
				if (arg0 == 3 || arg0 == 1) {
					function.setVisibility(View.GONE);
				} else {
					function.setVisibility(View.VISIBLE);
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
		function.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				int fun = (Integer) v.getTag();
				switch (fun) {
				case 0:
					((QuanziFra) (fragments.get(0))).setManager();
					function.setTag(3);
					function.setBackgroundResource(R.drawable.button_bg);
					function.setText(R.string.finish);
					break;
				case 1:
					break;
				case 2:
					// 1请求参数
					ZhuoQuanActivity.this.startActivityForResult(new Intent(
							mContext, QuanziFilterActivity.class), 1);
					break;
				case 3:
					((QuanziFra) (fragments.get(0))).offManager();
					setOffManager();
					break;
				}
			}
		});
	}

	public void setOffManager() {
		function.setTag(0);
		function.setBackgroundResource(R.drawable.ibutton);
		function.setText(R.string.label_manage);
	}

	PagerAdapter getPagerAdapter() {
		fragments = new ArrayList<Fragment>();
		List<CharSequence> titles = new ArrayList<CharSequence>();
		String quanMyTitle = getString(R.string.quanzi_my);
		String quanQueryTitle = getString(R.string.quanzi_query);
		String quanRecommendTitle = getString(R.string.quanzi_recommend);
		String quanCreateTitle = getString(R.string.quanzi_create);

		Fragment quanMyFragment = addBundle(new QuanziFra(),
				QuanVO.QUANZIMYCTEATE);
		fragments.add(quanMyFragment);
		titles.add(quanMyTitle);

		Fragment quanQueryFragment = addBundle(new QuanziFra(),
				QuanVO.QUANZIQUERY);
		fragments.add(quanQueryFragment);
		titles.add(quanQueryTitle);

		Fragment quanRecommendFragment = addBundle(new QuanziFra(),
				QuanVO.QUANZIRECOMMEND);
		fragments.add(quanRecommendFragment);
		titles.add(quanRecommendTitle);
		Fragment quanCreateFragment = addBundle(new QuanziCreateFra(),
				QuanVO.QUANZIRECOMMEND);
		fragments.add(quanCreateFragment);
		titles.add(quanCreateTitle);
		return new ActivePagerAdapter(getSupportFragmentManager(), fragments,
				titles);
	}

	protected Fragment addBundle(Fragment fragment, int catlog) {
		Bundle bundle = new Bundle();
		bundle.putInt(QuanVO.QUANZITYPE, catlog);
		bundle.putString("userid", userid);
		fragment.setArguments(bundle);
		return fragment;
	}

	private void setFunctionText(int arg0) {
		function.setVisibility(View.VISIBLE);
		switch (arg0) {
		case 0:
			function.setText(R.string.label_manage);
			function.setBackgroundResource(R.drawable.ibutton);
			function.setTag(0);
			break;
		case 1:
			function.setTag(1);
			function.setText("");
			function.setVisibility(View.INVISIBLE);
			break;
		case 2:
			function.setText("筛选");
			function.setBackgroundResource(R.drawable.ibutton);
			function.setTag(2);
			break;
		case 3:
			function.setText("");
			function.setBackgroundColor(Color.BLACK);
			function.setTag(3);
			break;
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 1) {
			if (resultCode == RESULT_OK) {

				((QuanziFra) (fragments.get(2))).onResult(data);
			}
		}
	}
}
