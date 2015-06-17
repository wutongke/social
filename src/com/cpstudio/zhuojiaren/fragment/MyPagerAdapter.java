package com.cpstudio.zhuojiaren.fragment;
import java.util.ArrayList;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class MyPagerAdapter extends FragmentPagerAdapter {

	public static final int TAB_PARAMS_INDEX = 0;
	public static final int TAB_DETAILS_INDEX = 1;
	public static final int TAB_REVIEWS_INDEX = 2;
	private ArrayList<Fragment> mFragmentList = new ArrayList<Fragment>();
	private String[]titles;
	public MyPagerAdapter(FragmentManager fm,String[]titles) {
		super(fm);
		this.titles = titles;
		mFragmentList.add(TAB_PARAMS_INDEX, new PaybackFragment());
		mFragmentList.add(TAB_DETAILS_INDEX, new CommentFragment());
		mFragmentList.add(TAB_REVIEWS_INDEX, new ProgressFragment());
	}

	@Override
	public Fragment getItem(int position) {
		return mFragmentList.get(position);
	}

	@Override
	public int getCount() {
		return mFragmentList.size();
	}
	@Override
	public CharSequence getPageTitle(int position) {
		// TODO Auto-generated method stub
		return titles[position];
	}
}