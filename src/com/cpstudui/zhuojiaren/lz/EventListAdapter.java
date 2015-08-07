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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

import com.cpstudio.zhuojiaren.R;
import com.cpstudio.zhuojiaren.model.EventVO;
import com.cpstudio.zhuojiaren.ui.EventDetailActivity;
import com.cpstudio.zhuojiaren.util.CommonUtil;

public class EventListAdapter extends BaseAdapter {
	private List<EventVO> mList = null;
	private LayoutInflater inflater = null;
	public boolean isManaging = false;
	Context c;

	public EventListAdapter(Context context, ArrayList<EventVO> list) {
		this.mList = list;
		this.inflater = LayoutInflater.from(context);
		c = context;
	}

	public void setManaging(boolean isManaging) {
		this.isManaging = isManaging;
		notifyDataSetChanged();
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
		final ViewHolderActive holder;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.item_event_list, null);
			holder = initHolderActive(convertView);
			convertView.setTag(R.id.tag_view_holder, holder);
		} else {
			holder = (ViewHolderActive) convertView
					.getTag(R.id.tag_view_holder);
		}
		EventVO event = mList.get(position);
		holder.textViewTitle.setText(event.getTitle());
		// time = CommonUtil.calcTime(time);
		holder.textViewDateTime.setText(event.getStarttime());
		if (event.getOutdate() == 1)
			holder.textViewIsOverTime.setText("已过期");
		else
			holder.textViewIsOverTime.setText("未过期");
		holder.textViewNums.setText(event.getJoinCount() + "人报名");

		holder.textViewPlace.setText(event.getAddress());
		holder.textViewDetail.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent i = new Intent(c, EventDetailActivity.class);
				String id = "111";
				if (mList.get(position).getActivityid() != null)
					id = mList.get(position).getActivityid();
				i.putExtra("eventId", id);

				c.startActivity(i);
			}
		});

		if (isManaging)
			holder.cbSelected.setVisibility(View.VISIBLE);
		else
			holder.cbSelected.setVisibility(View.GONE);
		holder.cbSelected
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton arg0,
							boolean arg1) {
						// TODO Auto-generated method stub
						mList.get(position).setSelected(arg1);
					}
				});
		return convertView;
	}

	static class ViewHolderActive {
		TextView textViewTitle;
		TextView textViewDateTime;

		TextView textViewPlace;
		TextView textViewIsOverTime;
		TextView textViewNums;
		TextView textViewDetail;
		CheckBox cbSelected;
	}

	private ViewHolderActive initHolderActive(View convertView) {

		ViewHolderActive holder = new ViewHolderActive();

		holder.textViewTitle = (TextView) convertView
				.findViewById(R.id.tvActiveTitle);

		holder.textViewDateTime = (TextView) convertView
				.findViewById(R.id.textViewDateTime);
		holder.textViewPlace = (TextView) convertView
				.findViewById(R.id.textViewPlace);
		holder.textViewIsOverTime = (TextView) convertView
				.findViewById(R.id.textViewIsOverTime);
		holder.textViewNums = (TextView) convertView
				.findViewById(R.id.textViewNums);
		holder.textViewDetail = (TextView) convertView
				.findViewById(R.id.textViewDetail);

		holder.cbSelected = (CheckBox) convertView.findViewById(R.id.cbChecked);

		return holder;
	}
}
