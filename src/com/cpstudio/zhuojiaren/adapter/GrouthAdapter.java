package com.cpstudio.zhuojiaren.adapter;

import java.util.List;

import android.content.Context;
import android.widget.ImageView;

import com.cpstudio.zhuojiaren.R;
import com.cpstudio.zhuojiaren.imageloader.LoadImage;
import com.cpstudio.zhuojiaren.model.GrouthVedio;
import com.cpstudio.zhuojiaren.util.CommonAdapter;
import com.cpstudio.zhuojiaren.util.Util;
import com.cpstudio.zhuojiaren.util.ViewHolder;

public class GrouthAdapter extends CommonAdapter<GrouthVedio>{
	LoadImage imageLoader =LoadImage.getInstance();
	public GrouthAdapter(Context context, List<GrouthVedio> mDatas, int itemLayoutId) {
		super(context, mDatas, itemLayoutId);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void convert(ViewHolder helper, GrouthVedio item) {
		// TODO Auto-generated method stub
//		helper.setImageResource(R.id.ig_grouth_image, R.drawable.pop_cancel2);
		helper.setText(R.id.ig_grouth_name, item.getTutorName());
		helper.setText(R.id.ig_browse_count,item.getViewCount());
		helper.setText(R.id.ig_duration, Util.getTimeString(item.getDuration()));
		imageLoader.beginLoad(item.getImageAddr(), (ImageView)helper.getView(R.id.ig_grouth_image));
	}


}
