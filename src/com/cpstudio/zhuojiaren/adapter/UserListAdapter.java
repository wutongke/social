package com.cpstudio.zhuojiaren.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.cpstudio.zhuojiaren.R;
import com.cpstudio.zhuojiaren.imageloader.LoadImage;
import com.cpstudio.zhuojiaren.model.UserVO;

public class UserListAdapter extends BaseAdapter {
	private List<UserVO> mList = null;
	private LayoutInflater inflater = null;
	private LoadImage mLoadImage = new LoadImage();

	public UserListAdapter(Context context, ArrayList<UserVO> list) {
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
			convertView = inflater.inflate(R.layout.item_user_list, null);
			holder = initHolder(convertView);
			convertView.setTag(R.id.tag_view_holder, holder);
		} else {
			holder = (ViewHolder) convertView.getTag(R.id.tag_view_holder);
		}
		UserVO user = mList.get(position);
		String userid = user.getUserid();
		String authorName = user.getUsername();
		String headUrl = user.getUheader();
		String time = user.getJoinZhuoDate();
		String work = user.getPost();
		convertView.setTag(R.id.tag_id, userid);
		holder.nameTV.setText(authorName);
		holder.workTV.setText(work);
		holder.dateTV.setText(time);
		holder.headIV.setImageResource(R.drawable.default_userhead);
		holder.headIV.setTag(headUrl);
		mLoadImage.addTask(headUrl, holder.headIV);
		mLoadImage.doTask();
		return convertView;
	}

	static class ViewHolder {
		TextView nameTV;
		TextView workTV;
		TextView dateTV;
		ImageView headIV;
	}

	private ViewHolder initHolder(View convertView) {
		ViewHolder holder = new ViewHolder();
		holder.nameTV = (TextView) convertView
				.findViewById(R.id.textViewAuthorName);
		holder.workTV = (TextView) convertView
				.findViewById(R.id.textViewWork);
		holder.dateTV = (TextView) convertView.findViewById(R.id.textViewTime);
		holder.headIV = (ImageView) convertView
				.findViewById(R.id.imageViewAuthorHeader);
		return holder;
	}
}
