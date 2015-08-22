package com.cpstudio.zhuojiaren.fragment;
import java.util.ArrayList;

import com.cpstudio.zhuojiaren.model.CrowdFundingVO;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class MyPagerAdapter extends FragmentPagerAdapter {

	public static final int TAB_PARAMS_INDEX = 0;
	public static final int TAB_DETAILS_INDEX = 1;
	public static final int TAB_REVIEWS_INDEX = 2;
	private ArrayList<Fragment> mFragmentList = new ArrayList<Fragment>();
	private String[]titles;
	public MyPagerAdapter(FragmentManager fm,String[]titles,String id,CrowdFundingVO crowdFundingVO) {
		super(fm);
		this.titles = titles;
		Bundle bundle = new Bundle();
		bundle.putString("id", id);
		bundle.putString("isCreater", crowdFundingVO.getIsCreater());
		PaybackFragment paybackFragment = new PaybackFragment();
		CommentFragment commentFragment = new CommentFragment();
		ProgressFragment progressFragment = new ProgressFragment();
		paybackFragment.setArguments(bundle);
		commentFragment.setArguments(bundle);
		progressFragment.setArguments(bundle);
		mFragmentList.add(TAB_PARAMS_INDEX, paybackFragment);
		mFragmentList.add(TAB_DETAILS_INDEX, commentFragment);
		mFragmentList.add(TAB_REVIEWS_INDEX, progressFragment);
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