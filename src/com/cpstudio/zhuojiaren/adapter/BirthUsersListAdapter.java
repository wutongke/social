package com.cpstudio.zhuojiaren.adapter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import com.cpstudio.zhuojiaren.helper.ZhuoCommHelper;
import com.cpstudio.zhuojiaren.imageloader.LoadImage;
import com.cpstudio.zhuojiaren.model.UserVO;
import com.cpstudio.zhuojiaren.R;
import com.utils.LundarToSolar;
import com.utils.SolarToLundar;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class BirthUsersListAdapter extends BaseAdapter {
	private List<UserVO> mList = null;
	private LayoutInflater inflater = null;
	private LoadImage mLoadImage = new LoadImage();

	public BirthUsersListAdapter(Context context, ArrayList<UserVO> list) {
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
			convertView = inflater.inflate(R.layout.item_birth_list, null);
			holder = initHolder(convertView);
			convertView.setTag(R.id.tag_view_holder, holder);
		} else {
			holder = (ViewHolder) convertView.getTag(R.id.tag_view_holder);
		}
		UserVO user = mList.get(position);
		final String userid = user.getUserid();
		final String authorName = user.getUsername();
		final String headUrl = user.getUheader();
		String work = user.getPost();
		convertView.setTag(R.id.tag_id, userid);
		holder.nameTV.setText(authorName);
		holder.workTV.setText(work);
		String showType = user.getBirthdayType();
		try {
			if (showType != null) {
				String yue = convertView.getContext().getString(
						R.string.label_month);
				String ri = convertView.getContext().getString(
						R.string.label_day);

				Calendar today = Calendar.getInstance(Locale.getDefault());
				int year = today.get(Calendar.YEAR);
				int month = today.get(Calendar.MONTH) + 1;
				int date = today.get(Calendar.DATE);
				int bMonth = 0;
				int bDate = 0;
				int age = 0;
				if (showType.equals("1")) {
					Drawable drawable = convertView.getResources().getDrawable(
							R.drawable.ico_birthtype2);
					drawable.setBounds(0, 0, drawable.getMinimumWidth(),
							drawable.getMinimumHeight());
					holder.birthTV.setCompoundDrawables(drawable, null, null,
							null);
					String birthday = user.getBirthday();
					int birthYearInt = year;
					int birthMonthInt = 1;
					int birthDateInt = 1;
					String birthYear = "";
					if (birthday != null && birthday.indexOf("-") != -1) {
						String[] bd = ZhuoCommHelper.getBirthday(birthday);
						String y = bd[0];
						String m = bd[1];
						String d = bd[2];
						int[] rs = SolarToLundar.getLunar(Integer.valueOf(y),
								Integer.valueOf(m), Integer.valueOf(d));
						birthYear = String.valueOf(rs[0]);
						birthMonthInt = rs[1];
						birthDateInt = rs[2];
					}
					int[] rs = SolarToLundar.getLunar(year, month, date);
					holder.birthTV.setText(SolarToLundar
							.getChinaMonthString(birthMonthInt)
							+ yue
							+ SolarToLundar.getChinaDayString(birthDateInt)
							+ ri);
					if (rs[1] > birthMonthInt
							|| (rs[1] == birthMonthInt && rs[2] > birthDateInt)) {
						birthYearInt += 1;
					}
					rs = LundarToSolar.getLundarToSolar(birthYearInt,
							birthMonthInt, birthDateInt);
					bMonth = rs[1];
					bDate = rs[2];
					age = birthYearInt - Integer.valueOf(birthYear);
				} else {
					String birthYear = String.valueOf(year);
					String birthMonth = "1";
					String birthDate = "1";
					String birthday = user.getBirthday();
					if (birthday != null && birthday.indexOf("-") != -1) {
						String[] bd = ZhuoCommHelper.getBirthday(birthday);
						birthYear = bd[0];
						birthMonth = bd[1];
						birthDate = bd[2];
					}
					holder.birthTV.setText(birthMonth + yue + birthDate + ri);
					bMonth = Integer.valueOf(birthMonth);
					bDate = Integer.valueOf(birthDate);
					int birthYearInt = year;
					if (month > bMonth || (month == bMonth && date > bDate)) {
						birthYearInt += 1;
					}
					age = birthYearInt - Integer.valueOf(birthYear);
				}
				int[] monthDates = new int[] { 31, 28, 31, 30, 31, 30, 31, 31,
						30, 31, 30, 31 };
				if (bMonth < month || (bMonth == month && bDate < date)) {
					bMonth += 12;
				}
				while (bMonth > month) {
					int offset = monthDates[(month - 1) % 12];
					if (month == 2) {
						if (year % 4 == 0) {
							offset = 29;
						}
					}
					bDate += offset;
					month++;
				}
				int dateDelay = bDate - date;
				if (dateDelay == 0) {
					holder.dateTV.setText(R.string.label_today);
					holder.dateInfoTV.setVisibility(View.GONE);
				} else {
					holder.dateTV.setText(String.valueOf(dateDelay));
				}
				String ageStr = user.getAge();
				if (ageStr != null && !ageStr.equals("")) {
					age = Integer.valueOf(ageStr);
				} else {

				}
				holder.ageTV.setText(String.valueOf(age + 1));
			} else {
				holder.birthTV.setVisibility(View.GONE);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		holder.headIV.setImageResource(R.drawable.default_userhead);
		holder.headIV.setTag(headUrl);
		mLoadImage.addTask(headUrl, holder.headIV);
		mLoadImage.doTask();
		return convertView;
	}

	static class ViewHolder {
		TextView nameTV;
		TextView workTV;
		TextView birthTV;
		TextView ageTV;
		TextView dateTV;
		TextView dateInfoTV;
		ImageView headIV;
	}

	private ViewHolder initHolder(View convertView) {
		ViewHolder holder = new ViewHolder();
		holder.nameTV = (TextView) convertView
				.findViewById(R.id.textViewAuthorName);
		holder.workTV = (TextView) convertView
				.findViewById(R.id.textViewContent);
		holder.birthTV = (TextView) convertView.findViewById(R.id.textViewDate);

		holder.ageTV = (TextView) convertView.findViewById(R.id.textViewAge);
		holder.dateTV = (TextView) convertView
				.findViewById(R.id.textViewDateDelay);
		holder.dateInfoTV = (TextView) convertView
				.findViewById(R.id.textViewDateInfo);
		holder.headIV = (ImageView) convertView
				.findViewById(R.id.imageViewAuthorHeader);
		return holder;
	}
}
