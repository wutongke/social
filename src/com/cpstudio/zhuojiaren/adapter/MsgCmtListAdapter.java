package com.cpstudio.zhuojiaren.adapter;

import java.util.ArrayList;
import java.util.List;

import com.cpstudio.zhuojiaren.imageloader.LoadImage;
import com.cpstudio.zhuojiaren.model.CmtVO;
import com.cpstudio.zhuojiaren.model.UserVO;
import com.cpstudio.zhuojiaren.util.CommonUtil;
import com.cpstudio.zhuojiaren.MsgCmtActivity;
import com.cpstudio.zhuojiaren.R;
import com.cpstudio.zhuojiaren.UserCardActivity;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MsgCmtListAdapter extends BaseAdapter {
	private List<CmtVO> mList = null;
	private LayoutInflater inflater = null;
	private LoadImage mLoadImage = new LoadImage();
	private String msgid = null;
	private Context mContext;

	public MsgCmtListAdapter(Context context, ArrayList<CmtVO> list,String msgid) {
		this.mList = list;
		this.msgid = msgid;
		this.inflater = LayoutInflater.from(context);
		this.mContext = context;
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
			convertView = inflater.inflate(R.layout.item_msg_cmt_list, null);
			holder = initHolder(convertView);
			convertView.setTag(R.id.tag_view_holder, holder);
		} else {
			holder = (ViewHolder) convertView.getTag(R.id.tag_view_holder);
		}
		if (position == 0) {
//			holder.icoCmt.setVisibility(View.VISIBLE);
			holder.icoCmt.setVisibility(View.GONE);
		}else{
			holder.icoCmt.setVisibility(View.GONE);
		}
		CmtVO cmt = mList.get(position);
		final String id = cmt.getId();
		convertView.setTag(R.id.tag_id, id);
		UserVO user = cmt.getUser();
		final String userid = user.getUserid();
		String authorName = user.getUsername();
		String headUrl = user.getUheader();
		String time = cmt.getAddtime();
		time = CommonUtil.calcTime(time);
		String content = cmt.getContent();
		final Context context = convertView.getContext();
		holder.cmt.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent i = new Intent(context,MsgCmtActivity.class);
				i.putExtra("msgid", msgid);
				i.putExtra("parentid", id);
				context.startActivity(i);
			}
		});
		holder.nameTV.setText(authorName);
		holder.timeTV.setText(time);
		holder.contentTV.setText(content);
		holder.headIV.setImageResource(R.drawable.default_userhead);
		holder.headIV.setTag(headUrl);
		holder.headIV.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(mContext, UserCardActivity.class);
				intent.putExtra("userid", userid);
				mContext.startActivity(intent);
			}
		});
		mLoadImage.addTask(headUrl, holder.headIV);
		mLoadImage.doTask();
		return convertView;
	}

	static class ViewHolder {
		TextView nameTV;
		TextView contentTV;
		TextView timeTV;
		ImageView icoCmt;
		ImageView cmt;
		ImageView headIV;
	}

	private ViewHolder initHolder(View convertView) {
		ViewHolder holder = new ViewHolder();
		holder.nameTV = (TextView) convertView
				.findViewById(R.id.textViewAuthorName);
		holder.contentTV = (TextView) convertView
				.findViewById(R.id.textViewContent);
		holder.timeTV = (TextView) convertView.findViewById(R.id.textViewTime);
		holder.icoCmt = (ImageView) convertView
				.findViewById(R.id.imageViewIcoCmt);
		holder.cmt = (ImageView) convertView.findViewById(R.id.imageViewHuiFu);
		holder.headIV = (ImageView) convertView
				.findViewById(R.id.imageViewAuthorHeader);
		return holder;
	}
}
