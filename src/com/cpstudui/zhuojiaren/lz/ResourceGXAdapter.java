package com.cpstudui.zhuojiaren.lz;

import java.util.List;

import android.content.Context;
import android.widget.ImageView;

import com.cpstudio.zhuojiaren.R;
import com.cpstudio.zhuojiaren.imageloader.LoadImage;
import com.cpstudio.zhuojiaren.model.ResourceGXVO;
import com.cpstudio.zhuojiaren.util.CommonAdapter;
import com.cpstudio.zhuojiaren.util.ViewHolder;

public class ResourceGXAdapter extends CommonAdapter<ResourceGXVO>{
	private LoadImage mLoadImage = LoadImage.getInstance();
	public ResourceGXAdapter(Context context, List<ResourceGXVO> mDatas,
			int itemLayoutId) {
		super(context, mDatas, itemLayoutId);
		// TODO Auto-generated constructor stub
		mContext = context;
	}

	@Override
	public void convert(ViewHolder helper, ResourceGXVO item) {
		// TODO Auto-generated method stub
		ImageView image = helper.getView(R.id.irg_image);
		helper.setText(R.id.irg_title, item.getTitle());
		String content = item.getContent();
		if(content.length()>33){
			content = content.substring(0,30)+"...";
		}
		helper.setText(R.id.irg_fund, content);
		mLoadImage.beginLoad(item.getPicture(), image);
	}

}
