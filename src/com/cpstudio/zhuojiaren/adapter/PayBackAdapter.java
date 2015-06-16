package com.cpstudio.zhuojiaren.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.cpstudio.zhuojiaren.R;
import com.cpstudio.zhuojiaren.model.PayBackVO;
import com.cpstudio.zhuojiaren.util.CommonAdapter;
import com.cpstudio.zhuojiaren.util.ViewHolder;

public class PayBackAdapter extends CommonAdapter<PayBackVO>{
	public PayBackAdapter(Context context, List<PayBackVO> mDatas,
			int itemLayoutId) {
		super(context, mDatas, itemLayoutId);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void convert(ViewHolder helper, PayBackVO item) {
		// TODO Auto-generated method stub
		TextView price = helper.getView(R.id.ipb_price);
		TextView peopleCount =helper.getView(R.id.ipb_peopel_count);
		TextView des = helper.getView(R.id.ipb_des);
		TextView supportCount = helper.getView(R.id.ipb_support_count);
		TextView supportBtn = helper.getView(R.id.ipb_support_btn);
		price.setText(mContext.getResources().getString(R.string.crowdfunding_price_label2)+item.getPrice());
		peopleCount.setText("限制"+item.getName()+"人");
		des.setText(item.getDes());
		supportCount.setText(item.getSupportCount()+"人支持");
		supportBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
			}
		});
	}
}
