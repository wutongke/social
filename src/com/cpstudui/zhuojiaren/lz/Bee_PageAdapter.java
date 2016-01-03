package com.cpstudui.zhuojiaren.lz;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.RelativeLayout.LayoutParams;

import com.cpstudio.zhuojiaren.imageloader.LoadImage;
import com.cpstudio.zhuojiaren.model.BeanBanner;
/**
 * 多页面可滑动ViewPage的数据Adapter：商场首页及商品详情的图片展示
 * @author lz
 *
 */
public class Bee_PageAdapter extends PagerAdapter {
	public List<BeanBanner> mListData;
	LoadImage mLoadImage;
	public ArrayList<View> mListView = new ArrayList<View>();

	public Bee_PageAdapter(Context context, List<BeanBanner> mListData) {
		this.mListData = mListData;
		mLoadImage = new LoadImage(5);
		for (BeanBanner beanBanner : mListData) {
			ImageView iView = new ImageView(context);
			String url = beanBanner.getPicUrl();
			iView.setTag(url);
			iView.setScaleType(ScaleType.CENTER_CROP);
			LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT,
					LayoutParams.MATCH_PARENT);
			iView.setLayoutParams(params);

			iView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
				}
			});
			mLoadImage.addTask(url, iView);
			mLoadImage.doTask();
			mListView.add(iView);
		}

	}
	public Bee_PageAdapter(Context context, List<BeanBanner> mListData,LoadImage loadImage) {
		this.mListData = mListData;
		mLoadImage = loadImage;
		for (BeanBanner beanBanner : mListData) {
			ImageView iView = new ImageView(context);
			String url = beanBanner.getPicUrl();
			iView.setTag(url);
			iView.setScaleType(ScaleType.CENTER_CROP);
			LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT,
					LayoutParams.MATCH_PARENT);
			iView.setLayoutParams(params);

			iView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
				}
			});
			mLoadImage.addTask(url, iView);
			mLoadImage.doTask();
			mListView.add(iView);
		}

	}
	public Bee_PageAdapter(Context context, List<BeanBanner> mListData,LoadImage loadImage,ScaleType scaleType) {
		this.mListData = mListData;
		mLoadImage = loadImage;
		for (BeanBanner beanBanner : mListData) {
			ImageView iView = new ImageView(context);
			iView.setScaleType(scaleType);
			String url = beanBanner.getPicUrl();
			iView.setTag(url);
			iView.setScaleType(ScaleType.CENTER_CROP);
			LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT,
					LayoutParams.MATCH_PARENT);
			iView.setLayoutParams(params);

			iView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
				}
			});
			mLoadImage.addTask(url, iView);
			mLoadImage.doTask();
			mListView.add(iView);
		}

	}
	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		container.removeView(mListView.get(position));
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) { 
		container.addView(mListView.get(position), 0);
		return mListView.get(position);
	}

	@Override
	public int getCount() {
		return mListView.size();
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == arg1;
	}
	public int getItemPosition(Object object) {
	    return POSITION_NONE;
	}
	
}
