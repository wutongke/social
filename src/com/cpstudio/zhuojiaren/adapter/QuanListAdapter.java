package com.cpstudio.zhuojiaren.adapter;

import java.util.ArrayList;
import java.util.List;

import com.cpstudio.zhuojiaren.imageloader.LoadImage;
import com.cpstudio.zhuojiaren.model.QuanVO;
import com.cpstudio.zhuojiaren.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.TextView;

public class QuanListAdapter extends BaseAdapter {
	private List<QuanVO> mList = null;
	private LayoutInflater inflater = null;
	private LoadImage mLoadImage = new LoadImage();
	private Context mContext;
	private List<QuanVO> mSelectedList = new ArrayList<QuanVO>();
	private boolean managerVisible=false;

	public QuanListAdapter(Context context, ArrayList<QuanVO> list) {
		this.mList = list;
		this.mContext = context;
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
	public View getView(final int position, View convertView, ViewGroup parent) {
		final ViewHolder holder;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.item_msg_user_list, null);
			holder = initHolder(convertView);
			convertView.setTag(R.id.tag_view_holder, holder);
		} else {
			holder = (ViewHolder) convertView.getTag(R.id.tag_view_holder);
		}
		QuanVO quan = mList.get(position);
		String groupid = quan.getGroupid();
		String headUrl = quan.getGheader();
		String time = quan.getLastmsgtime();
		String groupname = quan.getGname();
		String membersnum = quan.getMembersnum();
		String membersmax = quan.getMembersmax();
		convertView.setTag(R.id.tag_id, groupid);
		if(!managerVisible)
			holder.selectCheck.setVisibility(View.GONE);
		else
			holder.selectCheck.setVisibility(View.VISIBLE);
		holder.selectCheck.setChecked(false);
		//设置是否选中
		if(mSelectedList.contains(mList.get(position))){
			holder.selectCheck.setChecked(true);
		}else{
			holder.selectCheck.setChecked(false);
		}
		holder.selectCheck.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				if(mSelectedList.contains(mList.get(position))){
					holder.selectCheck.setChecked(false);
					mSelectedList.remove(mList.get(position));
				}else{
					holder.selectCheck.setChecked(true);
					mSelectedList.add(mList.get(position));
				}
			}
		});
		holder.nameTV.setText(groupname);
		// holder.textViewTime.setText(time);
		holder.textViewMsg.setText(/*
									 * mContext.getString(R.string.label_qzhm) +
									 * groupid + "    " +
									 */mContext.getString(R.string.label_qzcy)
				+ membersnum + "/" + membersmax);
		holder.headIV.setImageResource(R.drawable.default_grouphead);
		holder.headIV.setTag(headUrl);
		if (time != null && !time.equals("")) {
			holder.textViewUpdateTime.setText(mContext
					.getString(R.string.label_qzgx) + time);
			holder.textViewUpdateTime.setVisibility(View.VISIBLE);
		}
		mLoadImage.addTask(headUrl, holder.headIV);
		mLoadImage.doTask();
		return convertView;
	}

	static class ViewHolder {
		TextView nameTV;
		CheckBox selectCheck;
		// TextView textViewTime;
		TextView textViewMsg;
		TextView textViewUpdateTime;
		ImageView resIV;
		ImageView headIV;
	}

	private ViewHolder initHolder(View convertView) {
		ViewHolder holder = new ViewHolder();
		holder.nameTV = (TextView) convertView
				.findViewById(R.id.textViewAuthorName);
		holder.selectCheck = (CheckBox) convertView
				.findViewById(R.id.imul_select);
		// holder.textViewTime = (TextView) convertView
		// .findViewById(R.id.textViewTime);
		holder.textViewMsg = (TextView) convertView
				.findViewById(R.id.textViewMsg);
		holder.textViewUpdateTime = (TextView) convertView
				.findViewById(R.id.textViewUpdateTime);
		holder.headIV = (ImageView) convertView
				.findViewById(R.id.imageViewAuthorHeader);
		return holder;
	}


	public boolean isManagerVisible() {
		return managerVisible;
	}


	public void setManagerVisible(boolean managerVisible) {
		this.managerVisible = managerVisible;
	}
}
