package com.cpstudio.zhuojiaren.fragment;

import java.util.List;
import java.util.Map;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.SparseArray;

public class ActivePagerAdapter extends FragmentPagerAdapter {
	private List<Fragment> fragments;
	private List<CharSequence> titles;
	
	public ActivePagerAdapter(FragmentManager fm, List<Fragment> fragments, List<CharSequence> titles) {
		super(fm);
		this.fragments = fragments;
		this.titles = titles;
	}
	public ActivePagerAdapter(FragmentManager fm, List<Fragment> fragments) {
		super(fm);
		this.fragments = fragments;
	}

	public Fragment getItem(int arg0) {
		return fragments.get(arg0);
	}

	public int getCount() {
		return fragments.size();
	}

	public CharSequence getPageTitle(int position) {
		return position<titles.size() ? titles.get(position) : "";
	}
	
	
}
