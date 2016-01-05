package com.cpstudio.zhuojiaren.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.widget.ImageView;

import com.cpstudio.zhuojiaren.R;
import com.cpstudio.zhuojiaren.imageloader.LoadImage;
import com.cpstudio.zhuojiaren.util.CommonAdapter;
import com.cpstudio.zhuojiaren.widget.ViewHolder;

public class ImageGridAdapter extends CommonAdapter<String>{
	public List<String> mImages ;
	LoadImage mLoadImage = LoadImage.getInstance();
	public ImageGridAdapter(Context context, List<String> mDatas,
			int itemLayoutId) {
		super(context, mDatas, itemLayoutId);
		// TODO Auto-generated constructor stub
		mImages=mDatas;
	}

	@Override
	public void convert(ViewHolder helper, String item) {
		// TODO Auto-generated method stub
		helper.setImageResource(R.id.igi_image, R.drawable.pictures_no);
		mLoadImage.beginLoad(item, (ImageView)helper.getView(R.id.igi_image));
		helper.setImageByUrl(R.id.igi_image, item);
	}

	
}
