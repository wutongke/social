package com.cpstudui.zhuojiaren.lz;

import java.util.ArrayList;
import java.util.List;

import com.cpstudio.zhuojiaren.helper.ZhuoCommHelper;
import com.cpstudio.zhuojiaren.imageloader.LoadImage;
import com.cpstudio.zhuojiaren.model.EventVO;
import com.cpstudio.zhuojiaren.model.ZhuoInfoVO;
import com.cpstudio.zhuojiaren.model.PicVO;
import com.cpstudio.zhuojiaren.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

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
		final ViewHolder holder;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.item_event_list, null);
			holder = initHolder(convertView);
			convertView.setTag(R.id.tag_view_holder, holder);
		} else {
			holder = (ViewHolder) convertView.getTag(R.id.tag_view_holder);
		}
//		ZhuoInfoVO zhuoinfo = mList.get(position);
//		convertView.setTag(R.id.tag_id, zhuoinfo.getMsgid());
//		convertView.setTag(R.id.tag_string, zhuoinfo.getType());
//		String text = zhuoinfo.getText();
//		if (text != null && !text.equals("")) {
//			holder.textViewTitle.setText(text);
//		} else {
//			holder.textViewTitle.setText("");
//		}
//		holder.rl.removeAllViews();
//		holder.textViewAll.setText("");
//		List<PicVO> Pics = zhuoinfo.getPic();
//		Context context = convertView.getContext();
//		if (Pics != null && Pics.size() > 0) {
//			holder.textViewAll.setText(context.getString(R.string.label_gong)
//					+ Pics.size() + context.getString(R.string.label_zhang));
//			LinearLayout ll = null;
//			ArrayList<ImageView> ivs = new ArrayList<ImageView>();
//			switch (Pics.size()) {
//			case 1:
//				ll = (LinearLayout) inflater.inflate(R.layout.item_one_image,
//						null);
//				ivs.add((ImageView) ll.findViewById(R.id.tab_image));
//				break;
//			case 2:
//				ll = (LinearLayout) inflater.inflate(R.layout.item_two_image,
//						null);
//				ivs.add((ImageView) ll.findViewById(R.id.tab_image));
//				ivs.add((ImageView) ll.findViewById(R.id.tab_image2));
//				break;
//			case 3:
//				ll = (LinearLayout) inflater.inflate(R.layout.item_three_image,
//						null);
//				ivs.add((ImageView) ll.findViewById(R.id.tab_image));
//				ivs.add((ImageView) ll.findViewById(R.id.tab_image2));
//				ivs.add((ImageView) ll.findViewById(R.id.tab_image3));
//				break;
//			default:
//				ll = (LinearLayout) inflater.inflate(R.layout.item_four_image,
//						null);
//				ivs.add((ImageView) ll.findViewById(R.id.tab_image));
//				ivs.add((ImageView) ll.findViewById(R.id.tab_image2));
//				ivs.add((ImageView) ll.findViewById(R.id.tab_image3));
//				ivs.add((ImageView) ll.findViewById(R.id.tab_image4));
//				break;
//			}
//			holder.rl.addView(ll);
//			RelativeLayout.LayoutParams rllp = (RelativeLayout.LayoutParams) ll
//					.getLayoutParams();
//			rllp.addRule(RelativeLayout.CENTER_IN_PARENT);
//			ll.setLayoutParams(rllp);
//			for (int i = 0; i < ivs.size(); i++) {
//				ImageView iv = ivs.get(i);
//				iv.setTag(Pics.get(i).getUrl());
//				mLoadImage.addTask(Pics.get(i).getUrl(), iv);
//			}
//		}
//		String place = zhuoinfo.getPosition();
//		if (null == place || place.equals("")) {
//			place = "";
//		}
//		String time = zhuoinfo.getAddtime();
//		if (null != time && !time.equals("") && time.indexOf("-") != -1) {
//			String month = ZhuoCommHelper.getMonthFromTime(time);
//			String date = ZhuoCommHelper.getDateFromTime(time);
//			holder.monthTV.setText(month
//					+ context.getString(R.string.label_month));
//			holder.dateTV.setText(date);
//		} else {
//			holder.monthTV.setText("");
//			holder.dateTV.setText("");
//		}
//		holder.placeTV.setText(place);
//		mLoadImage.doTask();
		return convertView;
	}

	static class ViewHolder {
		TextView textViewTitle;
		TextView textViewAll;
		RelativeLayout rl;
		TextView monthTV;
		TextView dateTV;
		TextView placeTV;
	}

	private ViewHolder initHolder(View convertView) {
		ViewHolder holder = new ViewHolder();
//		holder.textViewTitle = (TextView) convertView
//				.findViewById(R.id.textViewDetail);
//		holder.textViewAll = (TextView) convertView
//				.findViewById(R.id.textViewAll);
//		holder.rl = (RelativeLayout) convertView
//				.findViewById(R.id.relativeLayoutImageContainer);
//		
//		holder.dateTV = (TextView) convertView.findViewById(R.id.textViewDate);
//		holder.placeTV = (TextView) convertView
//				.findViewById(R.id.textViewPlace);
		return holder;
	}
}
