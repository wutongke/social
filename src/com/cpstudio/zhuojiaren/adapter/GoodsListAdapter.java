package com.cpstudio.zhuojiaren.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.cpstudio.zhuojiaren.R;
import com.cpstudio.zhuojiaren.imageloader.LoadImage;
import com.cpstudio.zhuojiaren.model.GoodsVO;

public class GoodsListAdapter extends BaseAdapter {
	private List<GoodsVO> mList = null;
	private LayoutInflater inflater = null;
	private LoadImage mLoadImage = new LoadImage();

	public GoodsListAdapter(Context context, ArrayList<GoodsVO> list) {
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
			convertView = inflater.inflate(R.layout.item_grid_goods, null);
			holder = initHolder(convertView);
			convertView.setTag(R.id.tag_view_holder, holder);
		} else {
			holder = (ViewHolder) convertView.getTag(R.id.tag_view_holder);
		}
		GoodsVO user = mList.get(position);
		String id = user.getGoodsId();
		String title = user.getGoodsName();
		String money = user.getZhuoPrice();
		List<String> pics = user.getPic();
		convertView.setTag(R.id.tag_id, id);
		holder.title.setText(title);
		holder.money.setText(money);
		holder.image.setImageResource(R.drawable.default_image);
		if (pics != null && pics.size() > 0) {
			String pic = pics.get(0);
			holder.image.setTag(pic);
			mLoadImage.addTask(pic, holder.image);
			mLoadImage.doTask();
		}
		return convertView;
	}

	static class ViewHolder {
		TextView title;
		TextView money;
		ImageView image;
	}

	private ViewHolder initHolder(View convertView) {
		ViewHolder holder = new ViewHolder();
		holder.title = (TextView) convertView.findViewById(R.id.textViewTitle);
		holder.money = (TextView) convertView.findViewById(R.id.textViewMoney);
		holder.image = (ImageView) convertView.findViewById(R.id.imageView);
		return holder;
	}
}
