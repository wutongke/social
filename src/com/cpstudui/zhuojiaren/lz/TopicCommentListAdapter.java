package com.cpstudui.zhuojiaren.lz;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.cpstudio.zhuojiaren.R;
import com.cpstudio.zhuojiaren.imageloader.LoadImage;
import com.cpstudio.zhuojiaren.model.Comment;
import com.cpstudio.zhuojiaren.ui.MsgCmtActivity;
/**
 * 评论列表数据Adapter，包括圈子话题、动态、供需的评论
 * @author lz
 *
 */
public class TopicCommentListAdapter extends BaseAdapter {
	private List<Comment> mList = null;
	private LayoutInflater inflater = null;
	private LoadImage mLoadImage;
	private String msgid = null;
	int bgColor = -1;

	public TopicCommentListAdapter(Context context, ArrayList<Comment> list,
			String msgid) {
		TopicCommentListAdapterCore(context, list, msgid, -1);
	}

	private void TopicCommentListAdapterCore(Context context,
			ArrayList<Comment> list, String msgid2, int color) {
		// TODO Auto-generated method stub
		if (bgColor != -1)
			bgColor = color;
		this.mList = list;
		this.msgid = msgid2;
		this.inflater = LayoutInflater.from(context);
		mLoadImage = LoadImage.getInstance();
	}

	public TopicCommentListAdapter(Context context, ArrayList<Comment> list,
			String msgid, int color) {
		TopicCommentListAdapterCore(context, list, msgid, color);
	}

	public TopicCommentListAdapter(Context context, ArrayList<Comment> list,
			String msgid, LoadImage loadImage) {
		this.mList = list;
		this.msgid = msgid;
		this.inflater = LayoutInflater.from(context);
		this.mLoadImage = loadImage;
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
		if (bgColor != -1)
			convertView.setBackgroundColor(bgColor);
		if (position == 0) {
			holder.icoCmt.setVisibility(View.GONE);
		} else {
			holder.icoCmt.setVisibility(View.GONE);
		}
		Comment cmt = mList.get(position);
		String authorName = cmt.getName();
		String headUrl = cmt.getUheader();
		String time = cmt.getAddtime();
		// time = CommonUtil.calcTime(time);
		String content = cmt.getComment();
		final Context context = convertView.getContext();
		holder.cmt.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent(context, MsgCmtActivity.class);
				i.putExtra("msgid", msgid);
				i.putExtra("parentid", msgid);
				context.startActivity(i);
			}
		});
		if (cmt.getToId() != null)
			authorName = authorName + " 回复  " + cmt.getToName();
		holder.nameTV.setText(authorName);
		holder.timeTV.setText(time);
		holder.contentTV.setText(content);
		holder.headIV.setImageResource(R.drawable.default_userhead);
		holder.headIV.setTag(headUrl);
		holder.headIV.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// Intent intent = new Intent(mContext, UserCardActivity.class);
				// intent.putExtra("userid", userid);
				// mContext.startActivity(intent);
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
