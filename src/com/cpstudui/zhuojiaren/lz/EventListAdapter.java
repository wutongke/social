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
import android.widget.TextView;

import com.cpstudio.zhuojiaren.R;
import com.cpstudio.zhuojiaren.model.EventVO;
import com.cpstudio.zhuojiaren.ui.EventDetailActivity;
/**
 * 圈子活动列表数据Adapter
 * @author lz
 *
 */
public class EventListAdapter extends BaseAdapter {
	private List<EventVO> mList = null;
	private LayoutInflater inflater = null;
	public boolean isManaging = false;
	Context c;
	private List<EventVO> mSelectedList = new ArrayList<EventVO>();

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
		if (event.getGname() != null) {
			holder.tvQuanName.setVisibility(View.VISIBLE);
			holder.tvQuanName.setText(event.getGname());
		} else
			holder.tvQuanName.setVisibility(View.GONE);
		holder.textViewTitle.setText(event.getTitle());
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
		holder.cbSelected.setChecked(false);
		// 设置是否选中
		if (mSelectedList.contains(mList.get(position))) {
			holder.cbSelected.setChecked(true);
		} else {
			holder.cbSelected.setChecked(false);
		}
		holder.cbSelected.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				if (mSelectedList.contains(mList.get(position))) {
					holder.cbSelected.setChecked(false);
					mSelectedList.remove(mList.get(position));
				} else {
					holder.cbSelected.setChecked(true);
					mSelectedList.add(mList.get(position));
				}
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
		TextView tvQuanName;
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
		holder.tvQuanName = (TextView) convertView
				.findViewById(R.id.tvQuanName);
		return holder;
	}

	public List<EventVO> getmSelectedList() {
		return mSelectedList;
	}

}
