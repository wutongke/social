package com.cpstudui.zhuojiaren.lz;

import java.util.List;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.widget.ImageView;
import android.widget.TextView;

import com.cpstudio.zhuojiaren.R;
import com.cpstudio.zhuojiaren.imageloader.LoadImage;
import com.cpstudio.zhuojiaren.model.ZhuoMaiVO;
import com.cpstudio.zhuojiaren.util.CommonAdapter;
import com.cpstudio.zhuojiaren.util.ViewHolder;
import com.utils.ImageRectUtil;

public class ZhuoMaiActiveAdapter extends CommonAdapter<ZhuoMaiVO> {
	private LoadImage mLoadImage = LoadImage.getInstance();
	private Context mContext;

	public ZhuoMaiActiveAdapter(Context context, List<ZhuoMaiVO> mDatas,
			int itemLayoutId) {
		super(context, mDatas, itemLayoutId);
		// TODO Auto-generated constructor stub
		mContext = context;
	}

	@Override
	public void convert(ViewHolder helper, ZhuoMaiVO item) {
		// TODO Auto-generated method stub
		ImageView image = helper.getView(R.id.izm_image);
		TextView title = helper.getView(R.id.izm_title);
		TextView content = helper.getView(R.id.izm_content);
		TextView time = helper.getView(R.id.izm_time);
		image.setImageBitmap(ImageRectUtil.toRoundCorner(BitmapFactory
				.decodeResource(mContext.getResources(),
						R.drawable.ico_chat_pic), 10));
		
	}

}
