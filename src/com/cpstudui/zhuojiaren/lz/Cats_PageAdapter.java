package com.cpstudui.zhuojiaren.lz;

//
//                       __
//                      /\ \   _
//    ____    ____   ___\ \ \_/ \           _____    ___     ___
//   / _  \  / __ \ / __ \ \    <     __   /\__  \  / __ \  / __ \
//  /\ \_\ \/\  __//\  __/\ \ \\ \   /\_\  \/_/  / /\ \_\ \/\ \_\ \
//  \ \____ \ \____\ \____\\ \_\\_\  \/_/   /\____\\ \____/\ \____/
//   \/____\ \/____/\/____/ \/_//_/         \/____/ \/___/  \/___/
//     /\____/
//     \/___/
//
//  Powered by BeeFramework
//

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout.LayoutParams;

import com.cpstudio.zhuojiaren.R;
import com.cpstudio.zhuojiaren.imageloader.LoadImage;

public class Cats_PageAdapter extends PagerAdapter {
	public List<BeanCats> mListData;
	// LoadImage mLoadImage=new LoadImage(5);
	int pages = 0;
	public ArrayList<View> mListView = new ArrayList<View>();

	public Cats_PageAdapter(Context context, List<BeanCats> mListData,int w,int h) {
		pages = (int) Math.ceil(mListData.size() / 8.0);

		int cellWidth=(w-5*5)/4;
		
		// for (int i = 0; i < pages; i++) {
		// ImageView iv = new ImageView(context);
		// iv.setImageResource(R.drawable.backspace);
		// mListView.add(iv);
		// }

		int n = mListData.size();
		this.mListData = mListData;

		ImageView iv = null;
		for (int i = 0; i < pages; i++) {
			LinearLayout.LayoutParams rllp = new LinearLayout.LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
			LinearLayout linearLayout = new LinearLayout(context);
			linearLayout.setLayoutParams(rllp);
			linearLayout.setOrientation(LinearLayout.VERTICAL);

			for (int j = 0; j < 2; j++) {
				LinearLayout.LayoutParams childllp = new LinearLayout.LayoutParams(
						LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
				LinearLayout childlinearLayou = new LinearLayout(context);
				childlinearLayou.setLayoutParams(childllp);
				childlinearLayou.setOrientation(LinearLayout.HORIZONTAL);

				linearLayout.addView(childlinearLayou);

				int base = i * 8 + j * 4;
				for (int k = 0; base + k < n && k < 4; k++) {
					iv = new ImageView(context);
					iv.setImageResource(mListData.get(base + k).getPicId());
					iv.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							// ��תҳ��
						}
					});
					LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
							cellWidth,cellWidth);
					lp.setMargins(5, 5, 5, 5);
					childlinearLayou.addView(iv, lp);
				}
			}
			mListView.add(linearLayout);
		}

	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		container.removeView(mListView.get(position));
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) { // 这个方法用来实例化页�?
																		// container.addView(mListViews.get(position),
		container.addView(mListView.get(position), 0); // 0);//添加页卡
		return mListView.get(position);
	}

	@Override
	public int getCount() {
		return pages;
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == arg1;
	}

}
