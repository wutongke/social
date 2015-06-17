package com.cpstudio.zhuojiaren.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.cpstudio.zhuojiaren.PhotoViewMultiActivity;
import com.cpstudio.zhuojiaren.R;
import com.cpstudio.zhuojiaren.imageloader.LoadImage;
import com.cpstudio.zhuojiaren.model.PayBackVO;
import com.cpstudio.zhuojiaren.util.CommonAdapter;
import com.cpstudio.zhuojiaren.util.ViewHolder;
import com.cpstudio.zhuojiaren.widget.MyGridView;
/**
 * 回报
 * @author lef
 *
 */
public class PayBackAdapter extends CommonAdapter<PayBackVO>{
	private LoadImage mLoadImage = new LoadImage();
	public PayBackAdapter(Context context, List<PayBackVO> mDatas,
			int itemLayoutId) {
		super(context, mDatas, itemLayoutId);
		// TODO Auto-generated constructor stub
	}
	public void add(int position, PayBackVO item) {
        mDatas.add(position, item);
        notifyDataSetChanged();
    }
	@Override
	public void convert(ViewHolder helper, PayBackVO item) {
		// TODO Auto-generated method stub
		TextView price = helper.getView(R.id.ipb_price);
		TextView peopleCount =helper.getView(R.id.ipb_peopel_count);
		TextView des = helper.getView(R.id.ipb_des);
		TextView supportCount = helper.getView(R.id.ipb_support_count);
		TextView supportBtn = helper.getView(R.id.ipb_support_btn);
		MyGridView images =(MyGridView) helper.getView(R.id.ipb_image);
		images.setVisibility(View.GONE);
		//显示图片
		if(item.getImageUrl()!=null){
			images.setVisibility(View.VISIBLE);
			images.setAdapter(new GridViewAdapter(mContext, item.getImageUrl(), R.layout.item_gridview_image));
		}
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
	//多张图片
	class GridViewAdapter extends CommonAdapter<String>{

		public GridViewAdapter(Context context, List<String> mDatas,
				int itemLayoutId) {
			super(context, mDatas, itemLayoutId);
			// TODO Auto-generated constructor stub
		}

		@Override
		public void convert(ViewHolder helper, String item) {
			// TODO Auto-generated method stub
			helper.setImageResource(R.id.gridview_image, R.drawable.ico_grid_on);
			ImageView iv = helper.getView(R.id.gridview_image);
			iv.setTag(item);
			iv.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent intent = new Intent(mContext,
							PhotoViewMultiActivity.class);
					ArrayList<String> orgs = new ArrayList<String>();
					orgs = (ArrayList<String>) mDatas;
					intent.putStringArrayListExtra("pics", orgs);
					intent.putExtra("pic", (String) v.getTag());
					mContext.startActivity(intent);
				}
			});
			
			mLoadImage.addTask(item, (ImageView)helper.getView(R.id.gridview_image));
			mLoadImage.doTask();
		}
		
	}
}
