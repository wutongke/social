package com.cpstudio.zhuojiaren.adapter;

import java.util.ArrayList;
import java.util.List;

import com.cpstudio.zhuojiaren.helper.ZhuoCommHelper;
import com.cpstudio.zhuojiaren.imageloader.LoadImage;
import com.cpstudio.zhuojiaren.model.ZhuoInfoVO;
import com.cpstudio.zhuojiaren.model.PicVO;
import com.cpstudio.zhuojiaren.util.CommonUtil;
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

public class ResListAdapter extends BaseAdapter {
	private List<ZhuoInfoVO> mList = null;
	private LoadImage mLoadImage = new LoadImage();
	private LayoutInflater inflater = null;

	public ResListAdapter(Context context, ArrayList<ZhuoInfoVO> list) {
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
			convertView = inflater.inflate(R.layout.item_resource_list, null);
			holder = initHolder(convertView);
			convertView.setTag(R.id.tag_view_holder, holder);
		} else {
			holder = (ViewHolder) convertView.getTag(R.id.tag_view_holder);
		}
		Context context = convertView.getContext();
		ZhuoInfoVO zhuoinfo = mList.get(position);
		convertView.setTag(R.id.tag_id, zhuoinfo.getMsgid());
		String type = zhuoinfo.getType();
		String category = zhuoinfo.getCategory();
		String title = zhuoinfo.getTitle();
		String detail = zhuoinfo.getText();
		String msgType = "";
		if (type != null && !type.equals("")) {
			msgType = ZhuoCommHelper.transferMsgTypeToString(type, context);
			category = ZhuoCommHelper.transferMsgCategoryToString(category,
					context);
			holder.textViewTitle.setText("【" + category + "】" + title
					+ "//" + detail);
		}
		holder.rl.removeAllViews();
		holder.textViewAll.setText("");
		List<PicVO> Pics = zhuoinfo.getPic();
		if (Pics != null && Pics.size() > 0) {
			holder.textViewAll.setText(context.getString(R.string.label_gong)
					+ Pics.size() + context.getString(R.string.label_zhang));
			LinearLayout ll = null;
			ArrayList<ImageView> ivs = new ArrayList<ImageView>();
			switch (Pics.size()) {
			case 1:
				ll = (LinearLayout) inflater.inflate(R.layout.item_one_image,
						null);
				ivs.add((ImageView) ll.findViewById(R.id.tab_image));
				break;
			case 2:
				ll = (LinearLayout) inflater.inflate(R.layout.item_two_image,
						null);
				ivs.add((ImageView) ll.findViewById(R.id.tab_image));
				ivs.add((ImageView) ll.findViewById(R.id.tab_image2));
				break;
			case 3:
				ll = (LinearLayout) inflater.inflate(R.layout.item_three_image,
						null);
				ivs.add((ImageView) ll.findViewById(R.id.tab_image));
				ivs.add((ImageView) ll.findViewById(R.id.tab_image2));
				ivs.add((ImageView) ll.findViewById(R.id.tab_image3));
				break;
			default:
				ll = (LinearLayout) inflater.inflate(R.layout.item_four_image,
						null);
				ivs.add((ImageView) ll.findViewById(R.id.tab_image));
				ivs.add((ImageView) ll.findViewById(R.id.tab_image2));
				ivs.add((ImageView) ll.findViewById(R.id.tab_image3));
				ivs.add((ImageView) ll.findViewById(R.id.tab_image4));
				break;
			}
			holder.rl.addView(ll);
			RelativeLayout.LayoutParams rllp = (RelativeLayout.LayoutParams) ll
					.getLayoutParams();
			rllp.addRule(RelativeLayout.CENTER_IN_PARENT);
			ll.setLayoutParams(rllp);
			for (int i = 0; i < ivs.size(); i++) {
				ImageView iv = ivs.get(i);
				iv.setTag(Pics.get(i).getUrl());
				mLoadImage.addTask(Pics.get(i).getUrl(), iv);
			}
		}
		String time = zhuoinfo.getAddtime();
		time = CommonUtil.calcTime(time);
		holder.typeTV.setText(msgType);
		holder.dateTV.setText(time);
		mLoadImage.doTask();
		return convertView;
	}

	static class ViewHolder {
		TextView textViewTitle;
		TextView textViewAll;
		RelativeLayout rl;
		TextView typeTV;
		TextView dateTV;
	}

	private ViewHolder initHolder(View convertView) {
		ViewHolder holder = new ViewHolder();
		holder.textViewTitle = (TextView) convertView
				.findViewById(R.id.textViewDetail);
		holder.textViewAll = (TextView) convertView
				.findViewById(R.id.textViewAll);
		holder.rl = (RelativeLayout) convertView
				.findViewById(R.id.relativeLayoutImageContainer);
		holder.typeTV = (TextView) convertView.findViewById(R.id.textViewType);
		holder.dateTV = (TextView) convertView.findViewById(R.id.textViewDate);
		return holder;
	}
}
