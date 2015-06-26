package com.cpstudui.zhuojiaren.lz;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cpstudio.zhuojiaren.R;
import com.cpstudio.zhuojiaren.model.EventVO;

public class EventListAdapter extends BaseAdapter {
	private List<EventVO> mList = null;
	private LayoutInflater inflater = null;

	public EventListAdapter(Context context, ArrayList<EventVO> list) {
		this.mList = list;
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
	public View getView(int position, View convertView, ViewGroup parent) {
		final ViewHolderActive holder;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.item_event_list,
					null);
			
			holder=initHolderActive(convertView);
			convertView.setTag(R.id.tag_view_holder, holder);
		} else {
			holder = (ViewHolderActive) convertView.getTag(R.id.tag_view_holder);
		}

		return convertView;
	}

	static class ViewHolderActive {
		TextView textViewTitle;
		TextView textViewDateTime;
		
		TextView textViewPlace;
		TextView textViewIsOverTime;
		TextView textViewNums;
		TextView textViewDetail;
	}

	private ViewHolderActive initHolderActive(View convertView) {
		
		ViewHolderActive holder = new ViewHolderActive();
		

		holder.textViewTitle = (TextView) convertView
				.findViewById(R.id.tvActiveTitle);
		
		holder.textViewDateTime = (TextView) convertView.findViewById(R.id.textViewDateTime);
		holder.textViewPlace = (TextView) convertView
				.findViewById(R.id.textViewPlace);
		holder.textViewIsOverTime = (TextView) convertView.findViewById(R.id.textViewIsOverTime);
		holder.textViewNums = (TextView) convertView.findViewById(R.id.textViewNums);
		holder.textViewDetail = (TextView) convertView
				.findViewById(R.id.textViewDetail);
		
		return holder;
	}
}
