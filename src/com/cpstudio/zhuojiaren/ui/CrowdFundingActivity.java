package com.cpstudio.zhuojiaren.ui;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import butterknife.ButterKnife;
import butterknife.InjectView;

import com.cpstudio.zhuojiaren.BaseFragmentActivity;
import com.cpstudio.zhuojiaren.R;
import com.cpstudio.zhuojiaren.fragment.ActivePagerAdapter;
import com.cpstudio.zhuojiaren.fragment.CrowdFundingFragment;
import com.cpstudio.zhuojiaren.fragment.QuanziCreateFra;
import com.cpstudio.zhuojiaren.fragment.QuanziFra;
import com.cpstudio.zhuojiaren.model.CrowdFundingVO;
import com.cpstudio.zhuojiaren.model.QuanVO;
import com.cpstudio.zhuojiaren.widget.TabButton;

public class CrowdFundingActivity extends BaseFragmentActivity {
	@InjectView(R.id.acf_tab)
	TabButton tab;
	@InjectView(R.id.acf_viewpager)
	ViewPager viewPager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_crowd_funding);
		ButterKnife.inject(this);
		initTitle();
		title.setText(R.string.crowdfunding);
		function.setBackgroundResource(R.drawable.dir_choose);
		// ≥ı ºªØtab∫Õviewpager
		viewPager.setAdapter(getPagerAdapter());

		tab.setViewPager(viewPager);
	}

	PagerAdapter getPagerAdapter() {

		ArrayList<Fragment> fragments = new ArrayList<Fragment>();
		List<CharSequence> titles = new ArrayList<CharSequence>();
		String quanMyTitle = getString(R.string.quanzi_my);
		String quanQueryTitle = getString(R.string.quanzi_query);
		String quanRecommendTitle = getString(R.string.quanzi_recommend);
		String quanCreateTitle = getString(R.string.quanzi_create);

//		Fragment quanMyFragment = addBundle(new CrowdFundingFragment(), QuanVO.QUANZIMY);
//		fragments.add(quanMyFragment);
//		titles.add(quanMyTitle);

		Fragment quanQueryFragment = addBundle(new CrowdFundingFragment(),
				CrowdFundingVO.CROWDFUNDINGMY);
		fragments.add(quanQueryFragment);
		titles.add(quanQueryTitle);

		Fragment quanRecommendFragment = addBundle(new CrowdFundingFragment(),
				CrowdFundingVO.CROWDFUNDINGQUERY);
		fragments.add(quanRecommendFragment);
		titles.add(quanRecommendTitle);
		Fragment quanCreateFragment = addBundle(new CrowdFundingFragment(),
				CrowdFundingVO.CROWDFUNDINGQUERY);
		fragments.add(quanCreateFragment);
		titles.add(quanCreateTitle);
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
