package com.cpstudui.zhuojiaren.lz;

import java.util.List;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.widget.ImageView;
import android.widget.TextView;

import com.cpstudio.zhuojiaren.R;
import com.cpstudio.zhuojiaren.imageloader.LoadImage;
import com.cpstudio.zhuojiaren.model.CrowdFundingVO;
import com.cpstudio.zhuojiaren.model.ResourceGXVO;
import com.cpstudio.zhuojiaren.util.CommonAdapter;
import com.cpstudio.zhuojiaren.util.ViewHolder;
import com.utils.ImageRectUtil;

public class ResourceGXAdapter extends CommonAdapter<ResourceGXVO>{
	private LoadImage mLoadImage = new LoadImage(10);
	private Context mContext;
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
		TextView title = helper.getView(R.id.irg_title);
		TextView price = helper.getView(R.id.irg_fund);
		TextView tvImageTag = helper.getView(R.id.irg_tag);
		image.setImageBitmap(ImageRectUtil.toRoundCorner(BitmapFactory
				.decodeResource(mContext.getResources(),
						R.drawable.ico_chat_pic), 10));
	}

}
