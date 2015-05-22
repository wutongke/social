package com.cpstudio.zhuojiaren.adapter;

import java.util.ArrayList;
import java.util.List;

import com.cpstudio.zhuojiaren.imageloader.LoadImage;
import com.cpstudio.zhuojiaren.model.TeacherVO;
import com.cpstudio.zhuojiaren.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class TeacherListAdapter extends BaseAdapter {
	private List<TeacherVO> mList = null;
	private LayoutInflater inflater = null;
	private LoadImage mLoadImage = new LoadImage();

	public TeacherListAdapter(Context context, ArrayList<TeacherVO> list) {
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
			convertView = inflater.inflate(R.layout.item_grid_teacher, null);
			holder = initHolder(convertView);
			convertView.setTag(R.id.tag_view_holder, holder);
		} else {
			holder = (ViewHolder) convertView.getTag(R.id.tag_view_holder);
		}
		TeacherVO item = mList.get(position);
		String id = item.getId();
		String name = item.getName();
		String level = item.getLevel();
		String header = item.getHeader();
		convertView.setTag(R.id.tag_id, id);
		holder.name.setText(name);
		holder.level.setText(level);
		holder.image.setImageResource(R.drawable.default_image);
		holder.image.setTag(header);
		mLoadImage.addTask(header, holder.image);
		mLoadImage.doTask();
		return convertView;
	}

	static class ViewHolder {
		TextView name;
		TextView level;
		ImageView image;
	}

	private ViewHolder initHolder(View convertView) {
		ViewHolder holder = new ViewHolder();
		holder.name = (TextView) convertView.findViewById(R.id.textViewName);
		holder.level = (TextView) convertView.findViewById(R.id.textViewLevel);
		holder.image = (ImageView) convertView.findViewById(R.id.imageView);
		return holder;
	}
}
