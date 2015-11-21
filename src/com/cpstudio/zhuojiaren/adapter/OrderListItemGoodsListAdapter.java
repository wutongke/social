package com.cpstudio.zhuojiaren.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.cpstudio.zhuojiaren.R;
import com.cpstudio.zhuojiaren.imageloader.LoadImage;
import com.cpstudio.zhuojiaren.model.GoodsVO;

//lz
public class OrderListItemGoodsListAdapter extends BaseAdapter {
	private List<GoodsVO> mList = null;
	private LayoutInflater inflater = null;
	private LoadImage mLoadImage = LoadImage.getInstance();
	Context mContext;

	public OrderListItemGoodsListAdapter(Context context, List<GoodsVO> list) {
		this.mList = list;
		this.inflater = LayoutInflater.from(context);
		mContext = context;
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

			convertView = inflater.inflate(R.layout.order_item_detail, null);
			holder = initHolder(convertView);
			convertView.setTag(R.id.tag_view_holder, holder);
		} else {
			holder = (ViewHolder) convertView.getTag(R.id.tag_view_holder);
		}
		GoodsVO goods = mList.get(position);

		String picurl = goods.getGoodsImg();

		// 注意，在click事件中要传此参数
		String id = goods.getGoodsId();

		String info = goods.getGoodsName();

		String count = goods.getGoodsCount();
		String price = goods.getZhuoPrice();
		holder.tvPrice.setText("$ " + price);
		holder.tvCount.setText("x" + count);
		holder.tvInfo.setText(info);
		if (picurl != null) {
			mLoadImage.addTask(picurl, holder.ivPic);
			mLoadImage.doTask();
		}
		return convertView;
	}

	static class ViewHolder {

		ImageView ivPic;
		TextView tvInfo;
		TextView tvCount;
		TextView tvPrice;
	}

	private ViewHolder initHolder(View convertView) {
		ViewHolder holder = new ViewHolder();
		holder.ivPic = (ImageView) convertView.findViewById(R.id.goods_pic);

		holder.tvInfo = (TextView) convertView.findViewById(R.id.goods_info);

		holder.tvCount = (TextView) convertView.findViewById(R.id.goods_count);

		holder.tvPrice = (TextView) convertView.findViewById(R.id.goods_price);

		return holder;
	}
}
