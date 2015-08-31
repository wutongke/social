package com.cpstudio.zhuojiaren;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

import com.cpstudio.zhuojiaren.adapter.PhotoPagerAdapter;
import com.cpstudio.zhuojiaren.widget.HackyViewPager;

public class PhotoViewMultiActivity extends Activity {

	private LinearLayout layout;
	private int prev = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_photo_view_new);
		ViewPager mViewPager = (HackyViewPager) findViewById(R.id.view_pager);
		Intent intent = getIntent();
		ArrayList<String> picsNew = new ArrayList<String>();
		boolean start = false;
		ArrayList<String> pics = intent.getStringArrayListExtra("pics");
		String mType = intent.getStringExtra("type");
		if (mType == null)
			mType = "network";
		String pic = intent.getStringExtra("pic");
		if (pic == null) {
			if (pics == null || pics.size() < 1){
				PhotoViewMultiActivity.this.finish();
				return;
			}
			pic = pics.get(0);
		}
		View processbar = findViewById(R.id.progressBar);
		layout = (LinearLayout) findViewById(R.id.linearLayoutPointers);
		LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		lp.leftMargin = 5;
		lp.rightMargin = 5;
		for (int i = 0; i < pics.size(); i++) {
			ImageView iv = new ImageView(PhotoViewMultiActivity.this);
			iv.setLayoutParams(lp);
			if (i == prev) {
				iv.setImageResource(R.drawable.ico_pointer_on);
			} else {
				iv.setImageResource(R.drawable.ico_pointer_off);
			}
			if (pic.equals(pics.get(i))) {
				start = true;
			}
			if (start) {
				picsNew.add(pics.get(i));
			}
			layout.addView(iv);
		}
		for (int i = 0; i < pics.size(); i++) {
			if (!pic.equals(pics.get(i))) {
				picsNew.add(pics.get(i));
			} else {
				break;
			}
		}
		PhotoPagerAdapter mAdapter = new PhotoPagerAdapter(picsNew, processbar,
				mType);
		mViewPager.setAdapter(mAdapter);
		mViewPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int now) {
				((ImageView) layout.getChildAt(now))
						.setImageResource(R.drawable.ico_pointer_on);
				((ImageView) layout.getChildAt(prev))
						.setImageResource(R.drawable.ico_pointer_off);
				prev = now;
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
			}
		});
		initClick();
	}

	private void initClick() {
		findViewById(R.id.buttonBack).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				PhotoViewMultiActivity.this.finish();
			}
		});
	}

}
