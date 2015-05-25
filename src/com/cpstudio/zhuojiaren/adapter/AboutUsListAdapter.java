package com.cpstudio.zhuojiaren.adapter;

import java.util.ArrayList;
import java.util.List;

import com.cpstudio.zhuojiaren.imageloader.LoadImage;
import com.cpstudio.zhuojiaren.model.AboutUsVO;
import com.cpstudio.zhuojiaren.model.PicVO;
import com.cpstudio.zhuojiaren.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class AboutUsListAdapter extends BaseAdapter {
	private List<AboutUsVO> mList = null;
	private LoadImage mLoadImage = new LoadImage();
	private LayoutInflater inflater = null;

	public AboutUsListAdapter(Context context, ArrayList<AboutUsVO> list) {
		this.mList = list;
		this.inflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		return mList.size();
	}

	@Override
	public Object getItem(int arg0) {
		return mList.get(arg0);
	}

	@Override
	public void notifyDataSetChanged() {
		super.notifyDataSetChanged();
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final ViewHolder holder;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.item_aboutus, null);
			holder = initHolder(convertView);
			convertView.setTag(R.id.tag_view_holder, holder);
		} else {
			holder = (ViewHolder) convertView.getTag(R.id.tag_view_holder);
		}
		AboutUsVO item = mList.get(position);
		String id = item.getId();
		String title = item.getTitle();
		String detail = item.getBrief();
		PicVO pic = item.getPic();
		convertView.setTag(R.id.tag_id, id);
		holder.textViewTitle.setText(title);
		holder.textViewDetail.setText(detail);
		if (pic != null) {
			holder.imageViewPic.setTag(pic.getUrl());
			mLoadImage.addTask(pic.getUrl(), holder.imageViewPic);
			mLoadImage.doTask();
		} else {
			holder.imageViewPic.setImageResource(R.drawable.default_image);
		}
		return convertView;
	}

	static class ViewHolder {
		TextView textViewTitle;
		TextView textViewDetail;
		ImageView imageViewPic;
	}

	private ViewHolder initHolder(View convertView) {
		ViewHolder holder = new ViewHolder();
		holder.textViewTitle = (TextView) convertView
				.findViewById(R.id.textViewTitle);
		holder.textViewDetail = (TextView) convertView
				.findViewById(R.id.textViewDetail);
		holder.imageViewPic = (ImageView) convertView
				.findViewById(R.id.imageViewPic);
		return holder;
	}
}
