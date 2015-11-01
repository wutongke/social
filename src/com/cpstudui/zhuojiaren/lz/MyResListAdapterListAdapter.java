package com.cpstudui.zhuojiaren.lz;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.cpstudio.zhuojiaren.R;
import com.cpstudio.zhuojiaren.imageloader.LoadImage;
import com.cpstudio.zhuojiaren.model.ResourceGXVO;

public class MyResListAdapterListAdapter extends BaseAdapter {
	private List<ResourceGXVO> mList = null;
	private LayoutInflater inflater = null;
	public boolean isManaging = false;
	Context c;
	private List<ResourceGXVO> mSelectedList = new ArrayList<ResourceGXVO>();
	private LoadImage mLoadImage = new LoadImage();

	public MyResListAdapterListAdapter(Context context,
			ArrayList<ResourceGXVO> list) {
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
		final ViewHolder holder;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.item_resource_gx, null);
			holder = initHolder(convertView);
			convertView.setTag(R.id.tag_view_holder, holder);
		} else {
			holder = (ViewHolder) convertView.getTag(R.id.tag_view_holder);
		}
		ResourceGXVO item = mList.get(position);
		holder.irg_title.setText(item.getTitle());

		if (isManaging)
			holder.cb.setVisibility(View.VISIBLE);
		else
			holder.cb.setVisibility(View.GONE);
		holder.cb.setChecked(false);
		// 设置是否选中
		if (mSelectedList.contains(mList.get(position))) {
			holder.cb.setChecked(true);
		} else {
			holder.cb.setChecked(false);
		}
		holder.cb.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				if (mSelectedList.contains(mList.get(position))) {
					holder.cb.setChecked(false);
					mSelectedList.remove(mList.get(position));
				} else {
					holder.cb.setChecked(true);
					mSelectedList.add(mList.get(position));
				}
			}
		});
		mLoadImage.beginLoad(item.getPicture(), holder.ivHead);
		return convertView;
	}

	static class ViewHolder {
		TextView irg_title;
		ImageView ivTag;
		TextView content;
		ImageView ivHead;
		CheckBox cb;
	}

	private ViewHolder initHolder(View convertView) {
		ViewHolder holder = new ViewHolder();
		holder.irg_title = (TextView) convertView.findViewById(R.id.irg_title);
		holder.ivHead = (ImageView) convertView.findViewById(R.id.irg_image);
		holder.ivTag = (ImageView) convertView.findViewById(R.id.irg_tag);
		holder.content = (TextView) convertView.findViewById(R.id.irg_fund);
		holder.cb = (CheckBox) convertView.findViewById(R.id.irg_select);
		return holder;
	}

	public List<ResourceGXVO> getmSelectedList() {
		return mSelectedList;
	}

}
