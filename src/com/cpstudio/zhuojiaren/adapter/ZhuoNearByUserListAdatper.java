package com.cpstudio.zhuojiaren.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.cpstudio.zhuojiaren.R;
import com.cpstudio.zhuojiaren.imageloader.LoadImage;
import com.cpstudio.zhuojiaren.model.UserAndCollection;
import com.cpstudio.zhuojiaren.util.CommonAdapter;
import com.cpstudio.zhuojiaren.util.ViewHolder;

public class ZhuoNearByUserListAdatper extends CommonAdapter<UserAndCollection> {

	LoadImage loadImage = new LoadImage(50);

	public ZhuoNearByUserListAdatper(Context context,
			List<UserAndCollection> mDatas, int itemLayoutId) {
		super(context, mDatas, itemLayoutId);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void convert(final ViewHolder helper, final UserAndCollection item) {
		// TODO Auto-generated method stub
		helper.setText(R.id.izul_name, item.getUser().getUsername());
		helper.setText(R.id.izul_company, item.getUser().getCompany());
		helper.setText(R.id.izul_position, item.getUser().getPost());
		helper.setText(R.id.izul_distance, item.getDistance());
		helper.setImageResource(R.id.izul_collect, R.drawable.zuncollect);
		if (item.getIsCollection().equals(UserAndCollection.collection)) {
			helper.setImageResource(R.id.izul_collect, R.drawable.zcollect);
		}
		// 收藏
		helper.getView(R.id.izul_collect).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						if (item.getIsCollection().equals(
								UserAndCollection.collection)) {
							helper.setImageResource(R.id.izul_collect,
									R.drawable.zuncollect);
							// 需要加网络部分
						} else {
							helper.setImageResource(R.id.izul_collect,
									R.drawable.zcollect);
						}
					}
				});
		ImageView iv = (ImageView) helper.getView(R.id.izul_image);
		iv.setTag(item.getUser().getUheader());
		loadImage.addTask(item.getUser().getUheader(), iv);
		loadImage.doTask();
	}
}
