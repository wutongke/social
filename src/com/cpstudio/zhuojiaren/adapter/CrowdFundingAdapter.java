package com.cpstudio.zhuojiaren.adapter;

import java.util.List;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.widget.ImageView;
import android.widget.TextView;

import com.cpstudio.zhuojiaren.R;
import com.cpstudio.zhuojiaren.imageloader.LoadImage;
import com.cpstudio.zhuojiaren.model.CrowdFundingVO;
import com.cpstudio.zhuojiaren.util.CommonAdapter;
import com.cpstudio.zhuojiaren.util.ViewHolder;
import com.utils.ImageRectUtil;

public class CrowdFundingAdapter extends CommonAdapter<CrowdFundingVO>{
	private LoadImage mLoadImage = new LoadImage(10);
	private Context mContext;
	public CrowdFundingAdapter(Context context, List<CrowdFundingVO> mDatas,
			int itemLayoutId) {
		super(context, mDatas, itemLayoutId);
		// TODO Auto-generated constructor stub
		mContext = context;
	}

	@Override
	public void convert(ViewHolder helper, CrowdFundingVO item) {
		// TODO Auto-generated method stub
		ImageView image = helper.getView(R.id.icf_image);
		TextView name = helper.getView(R.id.icf_name);
		TextView price = helper.getView(R.id.icf_price);
		TextView finishRate = helper.getView(R.id.icf_Finish_rate);
		image.setImageBitmap(ImageRectUtil.toRoundCorner(BitmapFactory
				.decodeResource(mContext.getResources(),
						R.drawable.ico_chat_pic), 10));
		image.setTag(item.getThumbPic());
		mLoadImage.addTask(item.getThumbPic(), image);
		name.setText(item.getTitle());
		price.setText(mContext.getResources().getString(R.string.crowdfunding_price_label2)+item.getMinSupport());
		finishRate.setText(mContext.getResources().getString(R.string.crowdfunding_finish_rate)+item.getReach());
	}

}
