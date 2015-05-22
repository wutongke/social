package com.cpstudio.zhuojiaren.adapter;

import java.util.ArrayList;
import java.util.List;

import com.cpstudio.zhuojiaren.imageloader.LoadImage;
import com.cpstudio.zhuojiaren.model.RecentVisitVO;
import com.cpstudio.zhuojiaren.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class VisitUsersListAdapter extends BaseAdapter {
	private List<RecentVisitVO> mUsersList = null;
	private LayoutInflater inflater = null;
	private LoadImage mLoadImage = new LoadImage();

	public VisitUsersListAdapter(Context context, ArrayList<RecentVisitVO> usersList) {
		this.mUsersList = usersList;
		this.inflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		return mUsersList.size();
	}

	@Override
	public Object getItem(int arg0) {
		return mUsersList.get(arg0);
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
			convertView = inflater.inflate(R.layout.item_visit_list, null);
			holder = initHolder(convertView);
			convertView.setTag(R.id.tag_view_holder, holder);
		} else {
			holder = (ViewHolder) convertView.getTag(R.id.tag_view_holder);
		}
		RecentVisitVO user = mUsersList.get(position);
		final String userid = user.getUserid();
		final String authorName = user.getUsername();
		final String headUrl = user.getUheader();
		String time = user.getAddtime();
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
				.findViewById(R.id.textViewContent);
		holder.dateTV = (TextView) convertView.findViewById(R.id.textViewDate);
		holder.headIV = (ImageView) convertView
				.findViewById(R.id.imageViewAuthorHeader);
		return holder;
	}
}
