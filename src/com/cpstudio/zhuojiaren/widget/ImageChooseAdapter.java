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
	 * �û�ѡ���ͼƬ���洢ΪͼƬ������·��
	 */
	public static List<String> mSelectedImage = new ArrayList<String>();

	/**
	 * �ļ���·��
	 */
	private String mDirPath;
	private Context mContext;
	//�ӿ�
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
		//����no_pic
		helper.setImageResource(R.id.id_item_image, R.drawable.pictures_no);
		//����no_selected
				helper.setImageResource(R.id.id_item_select,
						R.drawable.picture_unselected);
		//����ͼƬ
		helper.setImageByUrl(R.id.id_item_image, mDirPath + "/" + item);
		
		final ImageView mImageView = helper.getView(R.id.id_item_image);
		final ImageView mSelect = helper.getView(R.id.id_item_select);
		
		mImageView.setColorFilter(null);
		//����ImageView�ĵ���¼�
		mImageView.setOnClickListener(new OnClickListener()
		{
			//ѡ����ͼƬ�䰵����֮��֮
			@Override
			public void onClick(View v)
			{
				
				// �Ѿ�ѡ�����ͼƬ
				if (mSelectedImage.contains(mDirPath + "/" + item))
				{
					mSelectedImage.remove(mDirPath + "/" + item);
					mSelect.setImageResource(R.drawable.picture_unselected);
					mImageView.setColorFilter(null);
				} else
				// δѡ���ͼƬ
				{
					if(mSelectedImage.size()>=count){
						Toast.makeText(mContext, "���ѡ"+count+"��Ŷ", Toast.LENGTH_SHORT).show();
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
		 * �Ѿ�ѡ�����ͼƬ����ʾ��ѡ�����Ч��
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
