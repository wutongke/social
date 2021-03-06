package com.cpstudio.zhuojiaren.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.cpstudio.zhuojiaren.R;
import com.cpstudio.zhuojiaren.imageloader.LoadImage;
import com.cpstudio.zhuojiaren.model.PayBackVO;
import com.cpstudio.zhuojiaren.ui.PhotoViewMultiActivity;
import com.cpstudio.zhuojiaren.util.CommonAdapter;
import com.cpstudio.zhuojiaren.widget.MyGridView;
import com.cpstudio.zhuojiaren.widget.ViewHolder;
/**
 * 回报
 * @author lef
 *
 */
public class PayBackAdapter extends CommonAdapter<PayBackVO>{
	private LoadImage mLoadImage;
	public PayBackAdapter(Context context, List<PayBackVO> mDatas,
			int itemLayoutId) {
		super(context, mDatas, itemLayoutId);
		mLoadImage = LoadImage.getInstance();
		// TODO Auto-generated constructor stub
	}
	public PayBackAdapter(Context context, List<PayBackVO> mDatas,
			int itemLayoutId,LoadImage mLoadImage) {
		super(context, mDatas, itemLayoutId);
		this.mLoadImage = mLoadImage;
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
		if(item.getPics()!=null){
			images.setVisibility(View.VISIBLE);
			String[] keys = item.getPics().split(",");
			ArrayList<String> urls = new ArrayList<String>();
			for(String temp:keys){
				urls.add(temp);
			}
			images.setAdapter(new GridViewAdapter(mContext, urls, R.layout.item_gridview_image));
		}
		mLoadImage.doTask();
		price.setText(mContext.getResources().getString(R.string.crowdfunding_price_label2)+item.getAmount());
		peopleCount.setText("限制"+item.getLimits()+"人");
		des.setText(item.getIntro());
		supportCount.setText(item.getNum()+"人支持");
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
//			helper.setImageResource(R.id.gridview_image, R.drawable.ico_chat_pic);
			
			ImageView iv = helper.getView(R.id.gridview_image);
//			iv.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ico_chat_pic));
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
//			mLoadImage.doTask();
		}
		
	}
}
