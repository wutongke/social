package com.cpstudui.zhuojiaren.lz;

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
import com.cpstudio.zhuojiaren.helper.ConnHelper;
import com.cpstudio.zhuojiaren.imageloader.LoadImage;
import com.cpstudio.zhuojiaren.model.BaseCodeData;
import com.cpstudio.zhuojiaren.model.QuanVO;
import com.cpstudio.zhuojiaren.model.UserNewVO;
/**
 * 圈子成员数据Adapter
 * @author lz
 *
 */
public class QuanMemberListAdapter extends BaseAdapter {
	private List<UserNewVO> mList = null;
	private LayoutInflater inflater = null;
	private LoadImage mLoadImage = LoadImage.getInstance();
	BaseCodeData baseDataSet;
	private ConnHelper mConnHelper = null;
	Context mContext;
	public QuanMemberListAdapter(Context context, ArrayList<UserNewVO> list) {
		this.mList = list;
		this.inflater = LayoutInflater.from(context);
		this.mContext = context;
		this.mConnHelper = ConnHelper.getInstance(mContext);
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
		holder.headIV.setTag(user.getUheader());
		mLoadImage.beginLoad(user.getUheader(), holder.headIV);
		return convertView;
	}

	static class ViewHolder {
		TextView nameTV;
		TextView workTV;

		ImageView headIV;
		TextView textViewStatus;
		TextView textViewRes;
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
