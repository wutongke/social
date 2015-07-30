package com.cpstudio.zhuojiaren.adapter;

import java.util.List;

import android.content.Context;
import android.widget.ImageView;

import com.cpstudio.zhuojiaren.R;
import com.cpstudio.zhuojiaren.imageloader.LoadImage;
import com.cpstudio.zhuojiaren.model.GrouthVisit;
import com.cpstudio.zhuojiaren.util.CommonAdapter;
import com.cpstudio.zhuojiaren.util.ViewHolder;

public class GrouthVisitAdapter extends CommonAdapter<GrouthVisit>{
	LoadImage imageLoad = new LoadImage();
	public GrouthVisitAdapter(Context context, List<GrouthVisit> mDatas,
			int itemLayoutId) {
		super(context, mDatas, itemLayoutId);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void convert(ViewHolder helper, GrouthVisit item) {
		// TODO Auto-generated method stub
		helper.setText(R.id.iv_grouth_name, item.getTitle());
		String content = item.getContent();
		if(item.getContent().length()>30)
			content = content.substring(0,30)+"...";
		helper.setText(R.id.iv_grouth_content, content);
		if(item.getImageAddr()!=null)
		imageLoad.beginLoad(item.getImageAddr(), (ImageView)helper.getView(R.id.iv_grouth_image));
	}


}
