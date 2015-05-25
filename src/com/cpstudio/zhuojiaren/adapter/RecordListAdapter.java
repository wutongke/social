package com.cpstudio.zhuojiaren.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cpstudio.zhuojiaren.facade.RecordChatFacade;
import com.cpstudio.zhuojiaren.helper.ResHelper;
import com.cpstudio.zhuojiaren.imageloader.LoadImage;
import com.cpstudio.zhuojiaren.model.ImMsgVO;
import com.cpstudio.zhuojiaren.model.UserVO;
import com.cpstudio.zhuojiaren.util.CommonUtil;
import com.cpstudio.zhuojiaren.util.DeviceInfoUtil;
import com.cpstudio.zhuojiaren.widget.MoveTouchListener;
import com.cpstudio.zhuojiaren.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class RecordListAdapter extends BaseAdapter {
	private List<ImMsgVO> mList = null;
	private LayoutInflater inflater = null;
	private LoadImage mLoadImage = null;
	private String myid = null;
	private Map<String, Integer> unreadCount = new HashMap<String, Integer>();
	private Context mContext;
	private RecordChatFacade mFacade = null;

	public RecordListAdapter(Context context, ArrayList<ImMsgVO> list) {
		this.mList = list;
		this.inflater = LayoutInflater.from(context);
		this.myid = ResHelper.getInstance(mContext).getUserid();
		this.mContext = context;
		this.mLoadImage = new LoadImage(
				(int) (DeviceInfoUtil.getDeviceCsd(context) * 10));
		this.mFacade = new RecordChatFacade(context);
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
		if (myid.equals(user.getUserid())) {
			user = msgUser.getReceiver();
		}
		final String userid = user.getUserid();
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
		String content = msgUser.getSecs();
		if (content.indexOf(":") == -1) {
			try {
				content = CommonUtil.getMSTime(Integer.valueOf(content));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		holder.layoutBasic.scrollTo(0, 0);
		holder.layoutBasic.setOnTouchListener(new MoveTouchListener(70, true));
		holder.buttonDel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mFacade.deleteByUserId(userid);
				for (int i = 0; i < mList.size(); i++) {
					String itemid = mList.get(i).getSender().getUserid();
					if (myid.equals(itemid)) {
						itemid = mList.get(i).getReceiver().getUserid();
					}
					if (itemid.equals(userid)) {
						mList.remove(i);
						notifyDataSetChanged();
						break;
					}
				}
			}
		});
		holder.nameTV.setText(username);
		holder.textViewTime.setText(time);
		holder.textViewMsg.setText(content);
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
		View layoutBasic;
		View buttonDel;
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
		convertView.findViewById(R.id.imageViewIco).setVisibility(View.VISIBLE);
		convertView.findViewById(R.id.imageViewArrow).setVisibility(
				View.VISIBLE);
		holder.layoutBasic = convertView.findViewById(R.id.layoutBasic);
		holder.buttonDel = convertView.findViewById(R.id.buttonDel);
		return holder;
	}
}
