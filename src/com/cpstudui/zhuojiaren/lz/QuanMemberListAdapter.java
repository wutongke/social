package com.cpstudui.zhuojiaren.lz;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.cpstudio.zhuojiaren.R;
import com.cpstudio.zhuojiaren.helper.ZhuoConnHelper;
import com.cpstudio.zhuojiaren.imageloader.LoadImage;
import com.cpstudio.zhuojiaren.model.BaseCodeData;
import com.cpstudio.zhuojiaren.model.QuanVO;
import com.cpstudio.zhuojiaren.model.UserNewVO;
import com.cpstudio.zhuojiaren.model.UserVO;
import com.cpstudio.zhuojiaren.widget.PopupWindows;

public class QuanMemberListAdapter extends BaseAdapter {
	private List<UserNewVO> mList = null;
	private LayoutInflater inflater = null;
	private Map<String, Integer> map = new HashMap<String, Integer>();
	private LoadImage mLoadImage = new LoadImage();
	BaseCodeData baseDataSet;
	private ZhuoConnHelper mConnHelper = null;
	Context mContext;

	// private String mSections = "#ABCDEFGHIJKLMNOPQRSTUVWXYZ";

	public QuanMemberListAdapter(Context context, ArrayList<UserNewVO> list) {
		this.mList = list;
		this.inflater = LayoutInflater.from(context);
		this.mContext = context;
		this.mConnHelper = ZhuoConnHelper.getInstance(mContext);
		baseDataSet = mConnHelper.getBaseDataSet();
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
		// initLetter();
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
		UserNewVO user = mList.get(position);

		String company = "接口无";
		if (user.getCompany() != null)
			company = user.getCompany();
		holder.textViewRes.setText(company);
		int role = user.getRole();
		role = role % (QuanVO.QUAN_ROLE_NAME.length);
		holder.textViewStatus.setText(QuanVO.QUAN_ROLE_NAME[role]);

		holder.nameTV.setText(user.getName());
		String work = "null";
		int p = user.getPosition();
		if (p != 0)
			p--;
		if (baseDataSet != null && p >= 1
				&& p <= baseDataSet.getPosition().size())
			work = ((baseDataSet.getPosition()).get(p - 1)).getContent();
		holder.workTV.setText(work);
		holder.headIV.setImageResource(R.drawable.default_userhead);
		holder.headIV.setTag(user.getUheader());
		mLoadImage.beginLoad(user.getUheader(), holder.headIV);

		return convertView;
	}

	static class ViewHolder {
		TextView nameTV;
		TextView workTV;// 鑱屼綅

		ImageView headIV;
		TextView textViewStatus;// 缇や富鎴栫鐞嗗憳
		TextView textViewRes;// 鍏徃鍚嶇О
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
