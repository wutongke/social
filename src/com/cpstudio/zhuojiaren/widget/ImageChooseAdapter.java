package com.cpstudio.zhuojiaren.widget;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.Toast;

import com.cpstudio.zhuojiaren.R;
import com.cpstudio.zhuojiaren.util.CommonAdapter;
import com.cpstudio.zhuojiaren.util.ViewHolder;



public class ImageChooseAdapter extends CommonAdapter<String>
{

	public interface ImageChooserCount{
		public void imageChoose(int count);
	}
	
	/**
	 * 用户选择的图片，存储为图片的完整路径
	 */
	public static List<String> mSelectedImage = new ArrayList<String>();

	/**
	 * 文件夹路径
	 */
	private String mDirPath;
	private Context mContext;
	//接口
	private ImageChooserCount imageCount;
	private int count;

	public ImageChooseAdapter(Context context, List<String> mDatas, int itemLayoutId,
			String dirPath,int count)
	{
		super(context, mDatas, itemLayoutId);
		this.mDirPath = dirPath;
		mContext = context;
		this.count = count;
	}

	@Override
	public void convert(final ViewHolder helper, final String item)
	{
		//设置no_pic
		helper.setImageResource(R.id.id_item_image, R.drawable.pictures_no);
		//设置no_selected
				helper.setImageResource(R.id.id_item_select,
						R.drawable.picture_unselected);
		//设置图片
		helper.setImageByUrl(R.id.id_item_image, mDirPath + "/" + item);
		
		final ImageView mImageView = helper.getView(R.id.id_item_image);
		final ImageView mSelect = helper.getView(R.id.id_item_select);
		
		mImageView.setColorFilter(null);
		//设置ImageView的点击事件
		mImageView.setOnClickListener(new OnClickListener()
		{
			//选择，则将图片变暗，反之则反之
			@Override
			public void onClick(View v)
			{
				
				// 已经选择过该图片
				if (mSelectedImage.contains(mDirPath + "/" + item))
				{
					mSelectedImage.remove(mDirPath + "/" + item);
					mSelect.setImageResource(R.drawable.picture_unselected);
					mImageView.setColorFilter(null);
				} else
				// 未选择该图片
				{
					if(mSelectedImage.size()>=count){
						Toast.makeText(mContext, "最多选"+count+"张哦", Toast.LENGTH_SHORT).show();
						return ;
					}
					mSelectedImage.add(mDirPath + "/" + item);
					mSelect.setImageResource(R.drawable.pictures_selected);
					mImageView.setColorFilter(Color.parseColor("#77000000"));
				}
				if(imageCount!=null){
					imageCount.imageChoose(mSelectedImage.size());
				}

			}
		});
		
		/**
		 * 已经选择过的图片，显示出选择过的效果
		 */
		if (mSelectedImage.contains(mDirPath + "/" + item))
		{
			mSelect.setImageResource(R.drawable.pictures_selected);
			mImageView.setColorFilter(Color.parseColor("#77000000"));
		}

	}

	public void setImageCount(ImageChooserCount imageCount) {
		this.imageCount = imageCount;
	}
}
