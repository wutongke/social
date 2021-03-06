package com.cpstudio.zhuojiaren.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.cpstudio.zhuojiaren.R;
import com.cpstudio.zhuojiaren.model.ImageRadioButton;
import com.cpstudio.zhuojiaren.util.CommonAdapter;
import com.cpstudio.zhuojiaren.widget.ViewHolder;

/**
 * 众筹及供需项目中的使用gridView显示各个子选项：科技、出版、娱乐
 * 
 * @author lef/lz
 * 
 */
public class TitleAdapter extends CommonAdapter<ImageRadioButton> {
	private ImageRadioButton mSelect;
	private ImageOnclick imageOnclick;

	public interface ImageOnclick {
		public void OnClickItem(ImageRadioButton item);
	}

	public TitleAdapter(Context context, List<ImageRadioButton> mDatas,
			int itemLayoutId, boolean none) {
		super(context, mDatas, itemLayoutId);
		if (none) {
			mSelect = null;
			return;
		}
		if (mDatas.size() > 0)
			mSelect = mDatas.get(0);
		// TODO Auto-generated constructor stub
	}

	public TitleAdapter(Context context, List<ImageRadioButton> mDatas,
			int itemLayoutId) {
		super(context, mDatas, itemLayoutId);
		if (mDatas.size() > 0)
			mSelect = mDatas.get(0);
	}

	@Override
	public void convert(ViewHolder helper, final ImageRadioButton item) {
		// TODO Auto-generated method stub

		final ImageView image = helper.getView(R.id.iti_image);
		image.setBackgroundResource(item.getaImage());
		if (mSelect!=null && mSelect.equals(item))
			image.setBackgroundResource(item.getbImage());
		image.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (!item.equals(mSelect)) {
					mSelect = item;
					image.setBackgroundResource(item.getbImage());
					if (imageOnclick != null)
						imageOnclick.OnClickItem(item);
				}
			}
		});

	}

	public ImageOnclick getImageOnclick() {
		return imageOnclick;
	}

	public void setImageOnclick(ImageOnclick imageOnclick) {
		this.imageOnclick = imageOnclick;
	}

}
