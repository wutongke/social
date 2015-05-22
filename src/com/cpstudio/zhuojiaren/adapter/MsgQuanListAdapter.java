package com.cpstudio.zhuojiaren.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cpstudio.zhuojiaren.imageloader.LoadImage;
import com.cpstudio.zhuojiaren.model.ImQuanVO;
import com.cpstudio.zhuojiaren.model.QuanVO;
import com.cpstudio.zhuojiaren.util.CommonUtil;
import com.cpstudio.zhuojiaren.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MsgQuanListAdapter extends BaseAdapter {
	private List<ImQuanVO> mList = null;
	private LayoutInflater inflater = null;
	private LoadImage mLoadImage = new LoadImage();
	private Map<String, Integer> unreadCount = new HashMap<String, Integer>();
	private Context mContext;

	public MsgQuanListAdapter(Context context, ArrayList<ImQuanVO> list) {
		this.mList = list;
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

	public void setUnreadCount(Map<String, Integer> unreadCount) {
		this.unreadCount = unreadCount;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final ViewHolder holder;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.item_msg_user_list, null);
			holder = initHolder(convertView);
			convertView.setTag(R.id.tag_view_holder, holder);
		} else {
			holder = (ViewHolder) convertView.getTag(R.id.tag_view_holder);
		}
		ImQuanVO msgUser = mList.get(position);
		QuanVO group = msgUser.getGroup();
		String groupid = group.getGroupid();
		String time = msgUser.getAddtime();
		time = CommonUtil.calcTime(time);
		String groupname = group.getGname();
		String headUrl = group.getGheader();
		String content = "";
		String type = msgUser.getType();
		if (type.equals("voice")) {
			content = mContext.getString(R.string.voice);
		} else if (type.equals("image")) {
			content = mContext.getString(R.string.image);
		} else if (type.equals("card")) {
			content = mContext.getString(R.string.label_sended)
					+ msgUser.getContent().split("____")[1]
					+ mContext.getString(R.string.label_sendedcardtoall);
		} else if (type.equals("emotion")) {
			content = mContext.getString(R.string.label_emotion);
		} else {
			content = msgUser.getContent();
		}
		if (msgUser.getIsread().equals("4")) {
			holder.sendingView.setVisibility(View.VISIBLE);
			holder.sendingView.setImageResource(R.drawable.ico_sending);
		} else if (msgUser.getIsread().equals("2")) {
			holder.sendingView.setVisibility(View.VISIBLE);
			holder.sendingView.setImageResource(R.drawable.ico_sending_failed);
		} else {
			holder.sendingView.setVisibility(View.GONE);
		}
		convertView.setTag(R.id.tag_id, groupid);
		if (unreadCount.get(groupid) != null) {
			int unread = unreadCount.get(groupid);
			holder.textViewMsgAll.setText(unread + "");
			if (unread > 0) {
				holder.textViewMsgAll.setVisibility(View.VISIBLE);
			} else {
				holder.textViewMsgAll.setVisibility(View.GONE);
			}
		}
		holder.nameTV.setText(groupname);
		holder.textViewTime.setText(time);
		holder.textViewMsg.setText(content);
		holder.headIV.setImageResource(R.drawable.default_grouphead);
		holder.headIV.setTag(headUrl);
		mLoadImage.addTask(headUrl, holder.headIV);
		mLoadImage.doTask();
		return convertView;
	}

	static class ViewHolder {
		TextView nameTV;
		TextView textViewTime;
		TextView textViewMsgAll;
		TextView textViewMsg;
		ImageView resIV;
		ImageView headIV;
		ImageView sendingView;
	}

	private ViewHolder initHolder(View convertView) {
		ViewHolder holder = new ViewHolder();
		holder.nameTV = (TextView) convertView
				.findViewById(R.id.textViewAuthorName);
		holder.textViewTime = (TextView) convertView
				.findViewById(R.id.textViewTime);
		holder.textViewMsg = (TextView) convertView
				.findViewById(R.id.textViewMsg);
		holder.textViewMsgAll = (TextView) convertView
				.findViewById(R.id.textViewMsgAll);
		holder.headIV = (ImageView) convertView
				.findViewById(R.id.imageViewAuthorHeader);
		holder.sendingView = (ImageView) convertView
				.findViewById(R.id.imageViewIco);
		return holder;
	}
}
