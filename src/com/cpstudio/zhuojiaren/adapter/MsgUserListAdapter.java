package com.cpstudio.zhuojiaren.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cpstudio.zhuojiaren.helper.ResHelper;
import com.cpstudio.zhuojiaren.imageloader.LoadImage;
import com.cpstudio.zhuojiaren.model.ImMsgVO;
import com.cpstudio.zhuojiaren.model.UserVO;
import com.cpstudio.zhuojiaren.util.CommonUtil;
import com.cpstudio.zhuojiaren.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MsgUserListAdapter extends BaseAdapter {
	private List<ImMsgVO> mList = null;
	private LayoutInflater inflater = null;
	private LoadImage mLoadImage = new LoadImage(10);
	private String myid = null;
	private Map<String, Integer> unreadCount = new HashMap<String, Integer>();
	private Context mContext;

	public MsgUserListAdapter(Context context, ArrayList<ImMsgVO> list) {
		this.mList = list;
		this.inflater = LayoutInflater.from(context);
		this.myid = ResHelper.getInstance(mContext).getUserid();
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
		ImMsgVO msgUser = mList.get(position);
		String time = msgUser.getAddtime();
		time = CommonUtil.calcTime(time);
		UserVO user = msgUser.getSender();
		String authorName = user.getUsername();
		boolean tome = true;
		if (myid.equals(user.getUserid())) {
			authorName = convertView.getContext().getString(R.string.label_me);
			user = msgUser.getReceiver();
			tome = false;
		}
		String userid = user.getUserid();
		holder.textViewMsgAll.setText("");
		holder.textViewMsgAll.setVisibility(View.GONE);
		if (unreadCount.get(userid) != null) {
			int unread = unreadCount.get(userid);
			if (unread > 0) {
				holder.textViewMsgAll.setText(unread + "");
				holder.textViewMsgAll.setVisibility(View.VISIBLE);
			}
		}
		convertView.setTag(R.id.tag_id, userid);
		String username = user.getUsername();
		String headUrl = user.getUheader();
		String content = "";
		String type = msgUser.getType();
		if (type.equals("voice")) {
			content = mContext.getString(R.string.voice);
		} else if (type.equals("image")) {
			content = mContext.getString(R.string.image);
		} else if(type.equals("card")){
			String to = "";
			if(!tome){
				to = mContext.getString(R.string.label_sendedcardtohe) + user.getUsername();
			}else{
				to = mContext.getString(R.string.label_sendedcardtome);
			}
			content = mContext.getString(R.string.label_sended) + msgUser.getContent().split("____")[1] + to;
		} else  if(type.equals("emotion")){
			content = mContext.getString(R.string.label_emotion);
		} else {
			content = msgUser.getContent();
		}
		if(msgUser.getIsread().equals("4")){
			holder.sendingView.setVisibility(View.VISIBLE);
			holder.sendingView.setImageResource(R.drawable.ico_sending);
		}else if(msgUser.getIsread().equals("2")){
			holder.sendingView.setVisibility(View.VISIBLE);
			holder.sendingView.setImageResource(R.drawable.ico_sending_failed);
		}else{
			holder.sendingView.setVisibility(View.GONE);
		}
		holder.nameTV.setText(username);
		holder.textViewTime.setText(time);
		holder.textViewMsg.setText(authorName + ":" + content);
		holder.headIV.setImageResource(R.drawable.default_userhead);
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
