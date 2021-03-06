package com.cpstudio.zhuojiaren.adapter;

import java.util.List;

import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.cpstudio.zhuojiaren.R;
import com.cpstudio.zhuojiaren.helper.ConnHelper;
import com.cpstudio.zhuojiaren.imageloader.LoadImage;
import com.cpstudio.zhuojiaren.model.BaseCodeData;
import com.cpstudio.zhuojiaren.model.MsgTagVO;
import com.cpstudio.zhuojiaren.model.UserAndCollection;
import com.cpstudio.zhuojiaren.util.CommonAdapter;
import com.cpstudio.zhuojiaren.widget.ViewHolder;

/**
 * 
 * @author lef
 * 
 */
public class ZhuoUserListAdapter extends CommonAdapter<UserAndCollection> {
	// addByLz
	boolean isManageing = false;

	LoadImage loadImage = LoadImage.getInstance();
	Handler handler;
	BaseCodeData baseData;
	private ConnHelper mConnHelper = null;

	public ZhuoUserListAdapter(Context context,
			List<UserAndCollection> mDatas, int itemLayoutId, Handler handler) {
		super(context, mDatas, itemLayoutId);
		mConnHelper = ConnHelper.getInstance(context
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
		// 閺�儼妫�
		helper.getView(R.id.izul_collect).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						if (item.getIsCollect() == 1) {
							mConnHelper.followUser(handler,
									MsgTagVO.MSG_FOWARD, item.getUserid(), 0);// 鍙栨秷鐐硅禐
							helper.setImageResource(R.id.izul_collect,
									R.drawable.zuncollect2);
						} else {
							helper.setImageResource(R.id.izul_collect,
									R.drawable.zcollect2);
							mConnHelper.followUser(handler,
									MsgTagVO.MSG_FOWARD, item.getUserid(), 1);// 鐐硅禐
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
