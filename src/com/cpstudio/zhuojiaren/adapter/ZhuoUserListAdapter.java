package com.cpstudio.zhuojiaren.adapter;

import java.util.List;

import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.cpstudio.zhuojiaren.R;
import com.cpstudio.zhuojiaren.helper.ZhuoConnHelper;
import com.cpstudio.zhuojiaren.imageloader.LoadImage;
import com.cpstudio.zhuojiaren.model.BaseCodeData;
import com.cpstudio.zhuojiaren.model.MsgTagVO;
import com.cpstudio.zhuojiaren.model.UserAndCollection;
import com.cpstudio.zhuojiaren.util.CommonAdapter;
import com.cpstudio.zhuojiaren.util.ViewHolder;

/**
 * 鍊浜篴dapter 鍚屽煄銆佺瓒ｏ拷?鍚岃锟�
 * 
 * @author lef
 * 
 */
public class ZhuoUserListAdapter extends CommonAdapter<UserAndCollection> {
	// addByLz
	boolean isManageing = false;// 锟斤拷锟揭碉拷锟斤拷锟斤拷锟斤拷-> 锟角凤拷删锟斤拷

	LoadImage loadImage = LoadImage.getInstance();
	Handler handler;
	BaseCodeData baseData;
	private ZhuoConnHelper mConnHelper = null;

	public ZhuoUserListAdapter(Context context,
			List<UserAndCollection> mDatas, int itemLayoutId, Handler handler) {
		super(context, mDatas, itemLayoutId);
		mConnHelper = ZhuoConnHelper.getInstance(context
				.getApplicationContext());
		baseData = mConnHelper.getBaseDataSet();
		this.handler = handler;
		// TODO Auto-generated constructor stub
	}

	public void setIsManaging(boolean flag) {
		isManageing = flag;
		notifyDataSetChanged();
	}

	@Override
	public void convert(final ViewHolder helper, final UserAndCollection item) {
		// TODO Auto-generated method stub
		helper.setText(R.id.izul_name, item.getName());
		helper.setText(R.id.izul_company, item.getCompany());
		int pos = item.getPosition();
		if (baseData != null && pos >= 1
				&& pos <= baseData.getPosition().size())
			helper.setText(R.id.izul_position,
					baseData.getPosition().get(pos - 1).getContent());
		helper.setImageResource(R.id.izul_collect, R.drawable.zuncollect2);
		if (item.getIsCollect() == 1) {
			helper.setImageResource(R.id.izul_collect, R.drawable.zcollect2);
		}
		// 鏀惰棌
		helper.getView(R.id.izul_collect).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						if (item.getIsCollect() == 1) {
							mConnHelper.followUser(handler,
									MsgTagVO.MSG_FOWARD, item.getUserid(), 0);// 取消点赞
							helper.setImageResource(R.id.izul_collect,
									R.drawable.zuncollect2);
						} else {
							helper.setImageResource(R.id.izul_collect,
									R.drawable.zcollect2);
							mConnHelper.followUser(handler,
									MsgTagVO.MSG_FOWARD, item.getUserid(), 1);// 点赞
						}

					}
				});
		ImageView iv = (ImageView) helper.getView(R.id.izul_image);
		iv.setTag(item.getUheader());

		// add by lz
		helper.setCheckBox(R.id.isChecked, false, isManageing ? View.VISIBLE
				: View.GONE);
		helper.getConvertView().setTag(R.id.tag_id,item.getUserid() );
		loadImage.beginLoad(item.getUheader(), iv);
	}
}
