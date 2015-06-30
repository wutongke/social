package com.cpstudio.zhuojiaren.adapter;

import java.util.List;

import android.content.Context;
import android.widget.ImageView;

import com.cpstudio.zhuojiaren.R;
import com.cpstudio.zhuojiaren.imageloader.LoadImage;
import com.cpstudio.zhuojiaren.model.ChangeBgAVO;
import com.cpstudio.zhuojiaren.model.UserAndCollection;
import com.cpstudio.zhuojiaren.util.CommonAdapter;
import com.cpstudio.zhuojiaren.util.ViewHolder;

public class ChangeBgGridViewAdatper extends CommonAdapter<ChangeBgAVO> {

	LoadImage loadImage = new LoadImage();//此处参数50或5等在滑动时会OOM

	public ChangeBgGridViewAdatper(Context context, List<ChangeBgAVO> mDatas,
			int itemLayoutId) {
		super(context, mDatas, itemLayoutId);
		// TODO Auto-generated constructor stub 
	}

	@Override
	public void convert(ViewHolder helper, ChangeBgAVO item) {
		// TODO Auto-generated method stub

		ImageView iv = (ImageView) helper.getView(R.id.imageView);
		iv.setTag(item.getUrl());
		loadImage.addTask(item.getUrl(), iv);
		loadImage.doTask();
	}
}
