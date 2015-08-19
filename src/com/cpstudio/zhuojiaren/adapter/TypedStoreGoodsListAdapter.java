package com.cpstudio.zhuojiaren.adapter;

import java.util.ArrayList;
import java.util.List;

import com.cpstudio.zhuojiaren.imageloader.LoadImage;
import com.cpstudio.zhuojiaren.model.GoodsVO;
import com.cpstudio.zhuojiaren.model.PicVO;
import com.cpstudio.zhuojiaren.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

//lz
public class TypedStoreGoodsListAdapter extends BaseAdapter {
	private List<GoodsVO> mList = null;
	private LayoutInflater inflater = null;
	private LoadImage mLoadImage = new LoadImage();

	public TypedStoreGoodsListAdapter(Context context, ArrayList<GoodsVO> list) {
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

			convertView = inflater.inflate(R.layout.listview_item_goods, null);
			holder = initHolder(convertView);
			convertView.setTag(R.id.tag_view_holder, holder);
		} else {
			holder = (ViewHolder) convertView.getTag(R.id.tag_view_holder);
		}
		GoodsVO user = mList.get(position);
		String id = user.getGoodsId();
		String title = user.getGoodsName();

		String zhuobi = "无数据";
		if (user.getZhuoPrice() != null)
			zhuobi = user.getZhuoPrice();
		List<PicVO> pics = user.getPic();
		convertView.setTag(R.id.tag_id, id);
		holder.title.setText(title);
		holder.zhuobimoney.setText(zhuobi + "个");
		holder.image.setImageResource(R.drawable.default_image);
		if (pics != null && pics.size() > 0) {
			String pic = pics.get(0).getUrl();
			holder.image.setTag(pic);
			mLoadImage.addTask(pic, holder.image);
			mLoadImage.doTask();
		}

		String rate = user.getCommentPoint();
		int nums = user.getOrderNum();
		if (rate != null)
			holder.goodCmtRate.setText("好评：" + rate);

		holder.payerNum.setText(nums + "人付款");

		return convertView;
	}

	static class ViewHolder {
		TextView title;
		TextView money;
		ImageView image;
		TextView zhuobimoney;// 倬币
		TextView goodCmtRate;// 好评比例
		TextView payerNum;// 付款人数
	}

	private ViewHolder initHolder(View convertView) {
		ViewHolder holder = new ViewHolder();
		holder.title = (TextView) convertView.findViewById(R.id.tvSimpInfo);

		holder.zhuobimoney = (TextView) convertView.findViewById(R.id.tvZhuobi);
		holder.image = (ImageView) convertView.findViewById(R.id.ivPic);

		holder.goodCmtRate = (TextView) convertView
				.findViewById(R.id.tvGoodCmtRate);
		holder.payerNum = (TextView) convertView.findViewById(R.id.tvPayerNum);

		return holder;
	}
}
