package com.cpstudio.zhuojiaren.adapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.cpstudio.zhuojiaren.helper.ZhuoCommHelper;
import com.cpstudio.zhuojiaren.imageloader.LoadImage;
import com.cpstudio.zhuojiaren.model.UserVO;
import com.cpstudio.zhuojiaren.model.ZhuoInfoVO;
import com.cpstudio.zhuojiaren.util.CommonUtil;
import com.cpstudio.zhuojiaren.util.DeviceInfoUtil;
import com.cpstudio.zhuojiaren.R;
import com.cpstudio.zhuojiaren.UserCardActivity;
import com.utils.ImageRectUtil;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.text.TextPaint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
/**
 * 倬家人adapter
 * 
 * @author lef
 *
 */
public class ZhuoUserListAdapter extends BaseAdapter {
	private List<ZhuoInfoVO> mList = null;
	private LayoutInflater inflater = null;
	private LoadImage mLoadImage = new LoadImage(10);
	private Context mContext = null;
	private int width = 720;
	private float times = 2;

	public ZhuoUserListAdapter(Context context, ArrayList<ZhuoInfoVO> list) {
		this.mContext = context;
		this.mList = list;
		this.inflater = LayoutInflater.from(context);
		this.width = DeviceInfoUtil.getDeviceCsw(context);
		this.times = DeviceInfoUtil.getDeviceCsd(context);
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
			convertView = inflater.inflate(R.layout.item_zhuouser_list, null);
			holder = initHolder(convertView);
			convertView.setTag(R.id.tag_view_holder, holder);
		} else {
			holder = (ViewHolder) convertView.getTag(R.id.tag_view_holder);
		}
		ZhuoInfoVO item = mList.get(position);
		UserVO user = item.getUser();
		final String userid = user.getUserid();
		String company = user.getCompany();
		String authorName = user.getUsername();
		String headUrl = user.getUheader();
		String work = user.getPost();
		String type = item.getType();
		String category = item.getCategory();
		String title = item.getTitle();
		String detail = item.getText();
		String time = item.getAddtime();
		time = CommonUtil.calcTime(time);
		convertView.setTag(R.id.tag_id, item.getMsgid());
		holder.nameTV.setText(authorName);
		holder.timeTV.setText(time);
		String woco = ZhuoCommHelper.concatStringWithTag(work, company, "|");
		if (woco.length() > 1) {
			holder.workTV.setText(woco);
			holder.workTV.setVisibility(View.VISIBLE);
		} else {
			holder.workTV.setText("");
			holder.workTV.setVisibility(View.GONE);
		}
		if (type != null && !type.equals("")) {
			Map<String, Object> resinfo = ZhuoCommHelper.gentResInfo(type,
					category, title, detail, mContext);
			holder.resIV.setImageResource((Integer) resinfo.get("ico"));
			String text = (String) resinfo.get("category")
					+ (String) resinfo.get("title")
					+ (String) resinfo.get("content");
			holder.resTV.setText(text.trim());
			Rect bounds = new Rect();
			TextPaint paint = holder.resTV.getPaint();
			paint.getTextBounds(text, 0, text.length(), bounds);
			int width = bounds.width();
			if (width / (this.width - 85 * times) > 4) {
				holder.moreTV.setVisibility(View.VISIBLE);
				holder.moreTV.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View view) {
						TextView showTypeView = (TextView) view;
						if (showTypeView.getText().equals(
								mContext.getString(R.string.info2))) {
							showTypeView.setText(R.string.info1);
							holder.resTV.setMaxLines(4);
						} else {

							showTypeView.setText(R.string.info2);
							holder.resTV.setMaxLines(200);
						}
					}
				});
			} else {
				holder.moreTV.setVisibility(View.GONE);
			}
		} else {
			holder.resIV.setImageResource(0);
			holder.resTV.setText("");
		}
		holder.headIV.setImageBitmap(ImageRectUtil.toRoundCorner(BitmapFactory
				.decodeResource(mContext.getResources(),
						R.drawable.default_userhead), 10));
		holder.headIV.setTag(headUrl);
		holder.headIV.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(mContext, UserCardActivity.class);
				intent.putExtra("userid", userid);
				mContext.startActivity(intent);
			}
		});
		mLoadImage.addTask(headUrl, holder.headIV);
		mLoadImage.doTask();
		return convertView;
	}

	static class ViewHolder {
		TextView nameTV;
		TextView timeTV;
		TextView workTV;
		TextView resTV;
		ImageView resIV;
		ImageView headIV;
		TextView moreTV;
	}

	private ViewHolder initHolder(View convertView) {
		ViewHolder holder = new ViewHolder();
		holder.nameTV = (TextView) convertView
				.findViewById(R.id.textViewAuthorName);
		holder.timeTV = (TextView) convertView.findViewById(R.id.textViewTime);
		holder.workTV = (TextView) convertView
				.findViewById(R.id.textViewContent);
		holder.resTV = (TextView) convertView.findViewById(R.id.textViewRes);
		holder.resIV = (ImageView) convertView.findViewById(R.id.imageViewRes);
		holder.headIV = (ImageView) convertView
				.findViewById(R.id.imageViewAuthorHeader);
		holder.moreTV = (TextView) convertView
				.findViewById(R.id.textViewViewMore);
		return holder;
	}
}
