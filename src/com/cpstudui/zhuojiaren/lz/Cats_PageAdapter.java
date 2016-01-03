package com.cpstudui.zhuojiaren.lz;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import com.cpstudio.zhuojiaren.helper.ResHelper;
import com.cpstudio.zhuojiaren.model.BeanCats;
import com.cpstudio.zhuojiaren.ui.AudioListActivity;
import com.cpstudio.zhuojiaren.ui.CrowdFundingActivity;
import com.cpstudio.zhuojiaren.ui.GrouthActivity;
import com.cpstudio.zhuojiaren.ui.GrouthListActivity;
import com.cpstudio.zhuojiaren.ui.MyZhuoBiActivity;
import com.cpstudio.zhuojiaren.ui.UserSameActivity;
import com.cpstudio.zhuojiaren.ui.ZhuoQuanActivity;
/**
 * 多页面可滑动ViewPage的数据Adapter：首页“公告”上部的不同模块界面的数据Adapter
 * @author lz
 *
 */
public class Cats_PageAdapter extends PagerAdapter {
	public List<BeanCats> mListData;
	int pages = 0;
	public ArrayList<View> mListView = new ArrayList<View>();
	Context mContext;

	public Cats_PageAdapter(final Context context, List<BeanCats> mListData,
			int w, int h) {
		pages = (int) Math.ceil(mListData.size() / 8.0);
		mContext = context;
		int cellWidth = (w - 5 * 5) / 4;

		int n = mListData.size();
		this.mListData = mListData;

		ImageView iv = null;
		TextView tv = null;
		for (int i = 0; i < pages; i++) {
			LinearLayout.LayoutParams rllp = new LinearLayout.LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
			LinearLayout linearLayout = new LinearLayout(context);
			linearLayout.setLayoutParams(rllp);
			linearLayout.setOrientation(LinearLayout.VERTICAL);

			for (int j = 0; j < 2; j++) {
				LinearLayout.LayoutParams childllp = new LinearLayout.LayoutParams(
						LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
				if(j==1)
					childllp.setMargins(0, 30, 0, 0);
				LinearLayout childlinearLayou = new LinearLayout(context);
				childlinearLayou.setLayoutParams(childllp);
				childlinearLayou.setOrientation(LinearLayout.HORIZONTAL);
				
				linearLayout.addView(childlinearLayou);

				final int base = i * 8 + j * 4;
				for (int k = 0; base + k < n && k < 4; k++) {
					final LinearLayout ccll = new LinearLayout(context);
					LinearLayout.LayoutParams ccp = new LinearLayout.LayoutParams(
							LayoutParams.WRAP_CONTENT,
							LayoutParams.WRAP_CONTENT);
					ccp.gravity = Gravity.CENTER_HORIZONTAL;
					ccll.setLayoutParams(ccp);
					ccll.setOrientation(LinearLayout.VERTICAL);
					final int index = base + k;
					iv = new ImageView(context);
					iv.setImageResource(mListData.get(index).getPicId());

					tv = new TextView(context);
					tv.setText(mListData.get(index).getText());
					tv.setTextSize(10);
					ccll.addView(iv, ccp);
					ccll.addView(tv, ccp);
					ccll.setTag(index);
					ccll.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							onClicked(index);

						}
					});
					LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
							cellWidth, LayoutParams.WRAP_CONTENT);
					lp.setMargins(5, 3, 5, 0);
					childlinearLayou.addView(ccll, lp);
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
	public Object instantiateItem(ViewGroup container, int position) { 
		container.addView(mListView.get(position), 0); 
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

	void onClicked(int x) {
		Intent i;
		switch (x) {
		case 0:
			i = new Intent(mContext, JiarenActiveNumListActivity.class);
			mContext.startActivity(i);
			break;
		case 1:
			mContext.startActivity(new Intent(mContext,
					GrouthListActivity.class));
			break;
		case 2:
			i = new Intent(mContext, AudioListActivity.class);
			mContext.startActivity(i);

			break;
		case 3:
			String uid = ResHelper
					.getInstance(mContext.getApplicationContext()).getUserid();
			i = new Intent(mContext, ZhuoMaiCardActivity.class);
			i.putExtra("userid", uid);
			mContext.startActivity(i);
			break;
		case 4:
			i = new Intent(mContext, ZhuoQuanActivity.class);
			mContext.startActivity(i);
			break;

		case 5:
			i = new Intent(mContext, ResourceGXActivity.class);
			mContext.startActivity(i);

			break;
		case 6:
			i = new Intent(mContext, StoreMainActivity.class);
			mContext.startActivity(i);
			break;
		case 7:
			i = new Intent(mContext, CrowdFundingActivity.class);
			mContext.startActivity(i);
			break;

		case 8:
			i = new Intent(mContext, UserSameActivity.class);
			i.putExtra("type", 2);
			mContext.startActivity(i);
			break;

		case 9:
			i = new Intent(mContext, UserSameActivity.class);
			i.putExtra("type", 1);
			mContext.startActivity(i);
			break;

		case 10:
			i = new Intent(mContext, UserSameActivity.class);
			i.putExtra("type", 4);
			mContext.startActivity(i);
			break;
		case 11:
			i = new Intent(mContext, UserSameActivity.class);
			i.putExtra("type", 3);
			mContext.startActivity(i);
			break;
		case 12://闂佸搫鎳愰崕銈夊矗鎼达絽鍨濋柟瑙勫姉缁狅拷
			i = new Intent(mContext, GrouthActivity.class);
			mContext.startActivity(i);
			break;
		case 13:
			i = new Intent(mContext, UserSameActivity.class);
			i.putExtra("type", 5);
			mContext.startActivity(i);
			break;
		case 14:
			i = new Intent(mContext, MyZhuoBiActivity.class);
			mContext.startActivity(i);
			break;
		}
	}
}
