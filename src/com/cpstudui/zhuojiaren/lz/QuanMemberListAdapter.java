package com.cpstudui.zhuojiaren.lz;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.cpstudio.zhuojiaren.R;
import com.cpstudio.zhuojiaren.imageloader.LoadImage;
import com.cpstudio.zhuojiaren.model.UserVO;

public class QuanMemberListAdapter extends BaseAdapter {
	private List<UserVO> mList = null;
	private LayoutInflater inflater = null;
	private Map<String, Integer> map = new HashMap<String, Integer>();
	private LoadImage mLoadImage = new LoadImage();

//	private String mSections = "#ABCDEFGHIJKLMNOPQRSTUVWXYZ";

	public QuanMemberListAdapter(Context context, ArrayList<UserVO> list) {
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
//		initLetter();
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
			convertView = inflater.inflate(R.layout.item_quanzi_member_list,
					null);
			holder = initHolder(convertView);
			convertView.setTag(R.id.tag_view_holder, holder);
		} else {
			holder = (ViewHolder) convertView.getTag(R.id.tag_view_holder);
		}
//		UserVO user = mList.get(position);
//		String userid = user.getUserid();
//		String authorName = user.getUsername();
//		String headUrl = user.getUheader();
//		String work = user.getPost();
//		String company = user.getCompany();
//		holder.textViewRes.setText(company);
//		String start = user.getStartletter().toUpperCase(Locale.getDefault());
//		convertView.setTag(R.id.tag_id, userid);
//
//		holder.textViewStatus.setText(start);
//		
//		holder.nameTV.setText(authorName);
//		holder.workTV.setText(work);
//		holder.headIV.setImageResource(R.drawable.default_userhead);
//		holder.headIV.setTag(headUrl);
//		mLoadImage.addTask(headUrl, holder.headIV);
//		mLoadImage.doTask();
//		
		
		
		return convertView;
	}

	static class ViewHolder {
		TextView nameTV;
		TextView workTV;// 职位

		ImageView headIV;
		TextView textViewStatus;// 群主或管理员
		TextView textViewRes;// 公司名称
	}

	private ViewHolder initHolder(View convertView) {
		ViewHolder holder = new ViewHolder();
		holder.nameTV = (TextView) convertView
				.findViewById(R.id.textViewAuthorName);
		holder.workTV = (TextView) convertView
				.findViewById(R.id.textViewContent);
		holder.textViewStatus = (TextView) convertView
				.findViewById(R.id.tvStatus);
		holder.textViewRes = (TextView) convertView
				.findViewById(R.id.textViewRes);
		holder.headIV = (ImageView) convertView
				.findViewById(R.id.imageViewAuthorHeader);
		return holder;
	}

}
