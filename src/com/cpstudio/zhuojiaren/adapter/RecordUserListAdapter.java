package com.cpstudio.zhuojiaren.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cpstudio.zhuojiaren.facade.RecordChatFacade;
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
import android.widget.SeekBar;
import android.widget.TextView;

public class RecordUserListAdapter extends BaseAdapter {
	private List<ImMsgVO> mList = null;
	private LayoutInflater inflater = null;
	private Map<String, ImMsgVO> map = new HashMap<String, ImMsgVO>();
	private OnClickListener mListener = null;
	private LoadImage mLoadImage = null;
	private RecordChatFacade mFacade = null;

	public RecordUserListAdapter(Context context, ArrayList<ImMsgVO> list,
			OnClickListener playingListener) {
		this.mList = list;
		this.inflater = LayoutInflater.from(context);
		this.mListener = playingListener;
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

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final ViewHolder holder;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.item_file_list, null);
			holder = initHolder(convertView);
			convertView.setTag(R.id.tag_view_holder, holder);
		} else {
			holder = (ViewHolder) convertView.getTag(R.id.tag_view_holder);
		}
		ImMsgVO item = mList.get(position);
		String id = item.getId();
		if (!map.containsKey(id)) {
			map.put(id, item);
		}
		convertView.setTag(R.id.tag_id, id);
		String path = item.getSavepath();
		UserVO sender = item.getSender();
		String name = sender.getUsername();
		String header = sender.getUheader();
		String date = item.getAddtime();
		String length = item.getSecs();
		String users = item.getContent();
		int max = CommonUtil.returnMStimeInt(length);
		if (length.indexOf(":") == -1) {
			try {
				length = CommonUtil.getMSTime(Integer.valueOf(length));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		holder.layoutBasic.scrollTo(0, 0);
		holder.layoutBasic.setOnTouchListener(new MoveTouchListener(70));
		holder.textViewUserName.setText(name);
		holder.textViewTime.setText(date);
		holder.textViewVoiceLength.setText(length);
		holder.textViewTotalTime.setText(length);
		holder.textViewUsers.setText(users);
		holder.layoutBasic.setOnClickListener(getChangeListener());
		holder.buttonDel.setOnClickListener(getDelListener(id));
		holder.viewPlay.setTag(R.id.tag_path, path);
		holder.viewPlay.setTag(R.id.tag_int, max);
		holder.viewPlay.setOnClickListener(mListener);
		holder.imageViewHead.setTag(header);
		holder.imageViewHead.setImageResource(R.drawable.default_userhead);
		mLoadImage.addTask(header, holder.imageViewHead);
		mLoadImage.doTask();
		return convertView;
	}

	private OnClickListener getChangeListener() {
		return new OnClickListener() {
			@Override
			public void onClick(View v) {
				View parent = (View) v.getParent();
				View more = parent.findViewById(R.id.linearLayoutMore);
				if (more.getVisibility() == View.VISIBLE) {
					more.setVisibility(View.GONE);
				} else {
					more.setVisibility(View.VISIBLE);
				}
			}
		};
	}

	private OnClickListener getDelListener(final String id) {
		return new OnClickListener() {
			@Override
			public void onClick(View v) {
				mFacade.delete(id);
				for (int i = 0; i < mList.size(); i++) {
					String itemid = mList.get(i).getId();
					if (itemid.equals(id)) {
						mList.remove(i);
						notifyDataSetChanged();
						break;
					}
				}
			}
		};
	}

	static class ViewHolder {
		TextView textViewUserName;
		ImageView imageViewHead;
		TextView textViewTime;
		TextView textViewVoiceLength;
		View viewPlay;
		TextView textViewNowTime;
		TextView textViewTotalTime;
		View imageViewForward;
		SeekBar seekBarRecord;
		TextView textViewUsers;
		View buttonDel;
		View layoutBasic;
	}

	private ViewHolder initHolder(View convertView) {
		ViewHolder holder = new ViewHolder();
		holder.textViewUserName = (TextView) convertView
				.findViewById(R.id.textViewUserName);
		holder.imageViewHead = (ImageView) convertView
				.findViewById(R.id.imageViewHead);
		holder.textViewTime = (TextView) convertView
				.findViewById(R.id.textViewTime);
		holder.textViewVoiceLength = (TextView) convertView
				.findViewById(R.id.textViewVoiceLength);
		holder.viewPlay = convertView.findViewById(R.id.viewPlay);
		holder.textViewNowTime = (TextView) convertView
				.findViewById(R.id.textViewNowTime);
		holder.textViewTotalTime = (TextView) convertView
				.findViewById(R.id.textViewTotalTime);
		holder.imageViewForward = convertView
				.findViewById(R.id.imageViewForward);
		holder.imageViewForward.setVisibility(View.GONE);
		holder.seekBarRecord = (SeekBar) convertView
				.findViewById(R.id.seekBarRecord);
		holder.textViewUsers = (TextView) convertView
				.findViewById(R.id.textViewUsers);
		holder.buttonDel = convertView.findViewById(R.id.buttonDel);
		holder.layoutBasic = convertView.findViewById(R.id.layoutBasic);
		convertView.findViewById(R.id.imageViewIco).setVisibility(View.VISIBLE);
		convertView.findViewById(R.id.relativeLayout).setVisibility(
				View.VISIBLE);
		return holder;
	}

}
