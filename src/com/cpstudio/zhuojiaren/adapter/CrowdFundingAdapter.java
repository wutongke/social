package com.cpstudio.zhuojiaren.adapter;

import java.util.List;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import com.cpstudio.zhuojiaren.R;
import com.cpstudio.zhuojiaren.model.CrowdFundingVO;
import com.cpstudio.zhuojiaren.util.CommonAdapter;
import com.cpstudio.zhuojiaren.util.ImageLoader;
import com.cpstudio.zhuojiaren.util.ViewHolder;

public class CrowdFundingAdapter extends CommonAdapter<CrowdFundingVO>{

	public CrowdFundingAdapter(Context context, List<CrowdFundingVO> mDatas,
			int itemLayoutId) {
		super(context, mDatas, itemLayoutId);
		// TODO Auto-generated constructor stub
		
	}

	@Override
	public void convert(ViewHolder helper, CrowdFundingVO item) {
		// TODO Auto-generated method stub
		ImageView image = helper.getView(R.id.icf_image);
		TextView name = helper.getView(R.id.icf_name);
		TextView price = helper.getView(R.id.icf_price);
		TextView finishRate = helper.getView(R.id.icf_Finish_rate);
	}


}
