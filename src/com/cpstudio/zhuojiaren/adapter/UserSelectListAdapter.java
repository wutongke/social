package com.cpstudio.zhuojiaren.adapter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.cpstudio.zhuojiaren.imageloader.LoadImage;
import com.cpstudio.zhuojiaren.model.UserVO;
import com.cpstudio.zhuojiaren.util.CommonUtil;
import com.cpstudio.zhuojiaren.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.TextView;

public class UserSelectListAdapter extends BaseAdapter {
	private List<UserVO> mUsersList = null;
	private LayoutInflater inflater = null;
	private LoadImage mLoadImage = new LoadImage();
	private Set<String> selectedId = new HashSet<String>();
	private int mMax = -1;

	public UserSelectListAdapter(Context context, ArrayList<UserVO> usersList,
			int max) {
		this.mMax = max;
		this.mUsersList = usersList;
		this.inflater = LayoutInflater.from(context);
	}

	public UserSelectListAdapter(Context context, ArrayList<UserVO> usersList,
			HashSet<String> selected, int max) {
		if (selected != null) {
			this.selectedId = selected;
		}
		this.mMax = max;
		this.mUsersList = usersList;
		this.inflater = LayoutInflater.from(context);
	}

	public Set<String> getSelectedId() {
		return selectedId;
	}

	public void setSelectedId(Set<String> selectedId) {
		this.selectedId = selectedId;
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
			convertView = inflater.inflate(R.layout.item_select_list, null);
			holder = initHolder(convertView);
			convertView.setTag(R.id.tag_view_holder, holder);
		} else {
			holder = (ViewHolder) convertView.getTag(R.id.tag_view_holder);
		}
		final Context context = convertView.getContext();
		UserVO user = mUsersList.get(position);
		final String userid = user.getUserid();
		final String authorName = user.getUsername();
		final String headUrl = user.getUheader();
		String work = user.getPost();
		convertView.setTag(R.id.tag_id, userid);
		holder.nameTV.setText(authorName);
		holder.workTV.setText(work);
		if (selectedId.contains(userid)) {
			holder.cb.setChecked(true);
		}
		holder.cb.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton v, boolean isChecked) {
				if (isChecked) {
					if (mMax > 0 && (mMax > selectedId.size()) || mMax == -1) {
						selectedId.add(userid);
					} else {
						v.setChecked(false);
						CommonUtil.displayToast(context, R.string.error11);
					}
				} else {
					selectedId.remove(userid);
				}
			}
		});
		holder.headIV.setImageResource(R.drawable.default_userhead);
		holder.headIV.setTag(headUrl);
		mLoadImage.addTask(headUrl, holder.headIV);
		mLoadImage.doTask();
		return convertView;
	}

	static class ViewHolder {
		TextView nameTV;
		TextView workTV;
		CheckBox cb;
		ImageView headIV;
	}

	private ViewHolder initHolder(View convertView) {
		ViewHolder holder = new ViewHolder();
		holder.nameTV = (TextView) convertView
				.findViewById(R.id.textViewAuthorName);
		holder.workTV = (TextView) convertView
				.findViewById(R.id.textViewContent);
		holder.cb = (CheckBox) convertView.findViewById(R.id.checkBoxSelect);
		holder.headIV = (ImageView) convertView
				.findViewById(R.id.imageViewAuthorHeader);
		return holder;
	}

}
