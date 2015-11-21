package com.cpstudio.zhuojiaren.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.cpstudio.zhuojiaren.OrderDetailActivity;
import com.cpstudio.zhuojiaren.R;
import com.cpstudio.zhuojiaren.imageloader.LoadImage;
import com.cpstudio.zhuojiaren.model.GoodsVO;
import com.cpstudio.zhuojiaren.model.OrderVO;
import com.cpstudio.zhuojiaren.ui.CartActivity;
import com.cpstudio.zhuojiaren.ui.GoodsDetailLActivity;

//lz
public class OrderListAdapter extends BaseAdapter {
	private List<OrderVO> mList = null;
	private LayoutInflater inflater = null;
	private LoadImage mLoadImage = LoadImage.getInstance();
	Context mContext;

	public OrderListAdapter(Context context, ArrayList<OrderVO> list) {
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
			convertView = inflater.inflate(R.layout.order_item, null);
			holder = initHolder(convertView);
			convertView.setTag(R.id.tag_view_holder, holder);
		} else {
			holder = (ViewHolder) convertView.getTag(R.id.tag_view_holder);
		}
		 OrderVO order = mList.get(position);
		 final String id = order.getBillNo();
		
		String time = order.getGentime();
		final List<GoodsVO> goodsList= order.getBuyGoods();
		 int totalNum = 0;
		 if(goodsList!=null)
			 totalNum=goodsList.size();
		
		String totalNumStr = String.format( mContext.getString(R.string.text_order_total_num),totalNum );
		String priceStr=String.format( mContext.getString(R.string.text_total_pay),order.getTotalZhuobi() );
		
		 holder.tvDate.setText(time);
		 holder.tvTotalNum.setText(totalNumStr);
		 holder.tvTotalPrice.setText(priceStr);
		
		holder.btnDetail.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent i = new Intent(mContext, OrderDetailActivity.class);
				i.putExtra("orderId", id);
				mContext.startActivity(i);
			}
		});
		//
		// OrderListItemGoodsListAdapter goodsListAdapter = new
		// OrderListItemGoodsListAdapter(
		// mContext, goodsList);
		// holder.lvGoods.setAdapter(goodsListAdapter);

		OrderListItemGoodsListAdapter goodsListAdapter = new OrderListItemGoodsListAdapter(
				mContext, goodsList);
		holder.lvGoods.setAdapter(goodsListAdapter);
		holder.lvGoods.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(mContext,GoodsDetailLActivity.class);
				intent.putExtra("goodsId", goodsList.get(position).getGoodsId());
				mContext.startActivity(intent);
			}
		});
		// 固定子listview的高度
//		int totalHeight = 0;
//		for (int i = 0, len = goodsListAdapter.getCount(); i < len; i++) { // listAdapter.getCount()返回数据项的数目
//			View listItem = goodsListAdapter.getView(i, null, holder.lvGoods);
//			listItem.measure(0, 0); // 计算子项View 的宽高
//			totalHeight += listItem.getMeasuredHeight(); // 统计所有子项的总高度
//		}
//
//		ViewGroup.LayoutParams params = holder.lvGoods.getLayoutParams();
//		params.height = totalHeight
//				+ (holder.lvGoods.getDividerHeight() * (holder.lvGoods
//						.getCount() - 1));
//		// listView.getDividerHeight()获取子项间分隔符占用的高度
//		// params.height最后得到整个ListView完整显示需要的高度
//		holder.lvGoods.setLayoutParams(params);
		return convertView;
	}

	static class ViewHolder {
		TextView tvDate;
		ListView lvGoods;
		TextView tvTotalNum;
		TextView tvTotalPrice;
		Button btnDetail;
	}

	private ViewHolder initHolder(View convertView) {
		ViewHolder holder = new ViewHolder();
		holder.tvDate = (TextView) convertView.findViewById(R.id.order_date);

		holder.lvGoods = (ListView) convertView.findViewById(R.id.lvGoods);
		holder.tvTotalNum = (TextView) convertView
				.findViewById(R.id.order_total_num);

		holder.tvTotalPrice = (TextView) convertView
				.findViewById(R.id.order_total_price);

		holder.btnDetail = (Button) convertView.findViewById(R.id.btnDetail);

		return holder;
	}
}