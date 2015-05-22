package com.cpstudio.zhuojiaren.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cpstudio.zhuojiaren.facade.RecordFacade;
import com.cpstudio.zhuojiaren.model.RecordVO;
import com.cpstudio.zhuojiaren.util.CommonUtil;
import com.cpstudio.zhuojiaren.widget.MoveTouchListener;
import com.cpstudio.zhuojiaren.R;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.SeekBar;
import android.widget.TextView;

public class MyRecordListAdapter extends BaseAdapter {
	private List<RecordVO> mList = null;
	private LayoutInflater inflater = null;
	private RecordFacade mFacade = null;
	private Map<String, RecordVO> map = new HashMap<String, RecordVO>();
	private OnClickListener mListener = null;
	private OnClickListener mForwardListener = null;

	public MyRecordListAdapter(Context context, ArrayList<RecordVO> list,
			OnClickListener playingListener, OnClickListener forwardListener) {
		this.mList = list;
		this.inflater = LayoutInflater.from(context);
		this.mFacade = new RecordFacade(context);
		this.mListener = playingListener;
		this.mForwardListener = forwardListener;
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
		holder.layoutBasic.scrollTo(0, 0);
		holder.layoutBasic.setOnTouchListener(new MoveTouchListener(70));
		RecordVO item = mList.get(position);
		String id = item.getId();
		if (!map.containsKey(id)) {
			map.put(id, item);
		}
		convertView.setTag(R.id.tag_id, id);
		String path = item.getPath();
		String name = item.getName();
		String size = item.getSize();
		String date = item.getDate();
		String length = item.getLength();
		String users = item.getUsers();
		int max = CommonUtil.returnMStimeInt(length);
		holder.textViewFilename.setText(name);
		holder.textViewFilesize.setText(size);
		holder.textViewFiledate.setText(date);
		holder.textViewVoiceLength.setText(length);
		holder.textViewTotalTime.setText(length);
		holder.textViewUsers.setText(users);
		holder.layoutBasic.setOnClickListener(getChangeListener());
		holder.buttonDel.setOnClickListener(getDelListener(id));
		holder.imageViewForward.setOnClickListener(mForwardListener);
		holder.imageViewForward.setTag(R.id.tag_path, path);
		holder.imageViewForward.setTag(R.id.tag_id, id);
		holder.imageViewForward.setTag(R.id.tag_string, length);
		holder.viewPlay.setTag(R.id.tag_path, path);
		holder.viewPlay.setTag(R.id.tag_int, max);
		holder.viewPlay.setOnClickListener(mListener);
		return convertView;
	}

	private OnClickListener getDelListener(final String id) {
		return new OnClickListener() {
			@Override
			public void onClick(View v) {
				RecordVO item = map.get(id);
				item.setState("0");
				mFacade.update(item);
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

	private OnClickListener getChangeListener() {
		return new OnClickListener() {
			@Override
			public void onClick(View v) {
				View parent = (View) v.getParent();
				View more = parent.findViewById(R.id.linearLayoutMore);
				if (more.getVisibility() == View.VISIBLE) {
					more.setVisibility(View.GONE);
					parent.setBackgroundColor(0xf4f4f4);
				} else {
					more.setVisibility(View.VISIBLE);
					parent.setBackgroundColor(Color.WHITE);
				}
			}
		};
	}

	static class ViewHolder {
		TextView textViewFilename;
		TextView textViewFilesize;
		TextView textViewFiledate;
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
		holder.textViewFilename = (TextView) convertView
				.findViewById(R.id.textViewFilename);
		holder.textViewFilesize = (TextView) convertView
				.findViewById(R.id.textViewFilesize);
		holder.textViewFiledate = (TextView) convertView
				.findViewById(R.id.textViewFiledate);
		holder.textViewVoiceLength = (TextView) convertView
				.findViewById(R.id.textViewVoiceLength);
		holder.viewPlay = convertView.findViewById(R.id.viewPlay);
		holder.textViewNowTime = (TextView) convertView
				.findViewById(R.id.textViewNowTime);
		holder.textViewTotalTime = (TextView) convertView
				.findViewById(R.id.textViewTotalTime);
		holder.imageViewForward = convertView
				.findViewById(R.id.imageViewForward);
		holder.seekBarRecord = (SeekBar) convertView
				.findViewById(R.id.seekBarRecord);
		holder.textViewUsers = (TextView) convertView
				.findViewById(R.id.textViewUsers);
		holder.buttonDel = convertView.findViewById(R.id.buttonDel);
		holder.layoutBasic = convertView.findViewById(R.id.layoutBasic);
		return holder;
	}

}
