package com.cpstudio.zhuojiaren.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.AbsoluteSizeSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;

import com.cpstudio.zhuojiaren.R;
import com.cpstudio.zhuojiaren.helper.ResHelper;
import com.cpstudio.zhuojiaren.ui.QuanCreateActivity;
import com.cpstudio.zhuojiaren.util.DeviceInfoUtil;

public class QuanziCreateFra extends Fragment {

	View layout;
	@InjectView(R.id.before_crete)
	LinearLayout beforeCreate;
	@InjectView(R.id.begin_crete)
	LinearLayout beginCreate;
	@InjectView(R.id.before_btn)
	Button beforeBtn;
	@InjectView(R.id.begin_btn)
	Button beginBtn;
	@InjectView(R.id.before_crete_text)
	TextView text1;
	@InjectView(R.id.begin_crete_text)
	TextView text2;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		layout = inflater.inflate(R.layout.activity_before_create, null);
		ButterKnife.inject(this, layout);

		String name = ResHelper.getInstance(getActivity()).getUserid();
		text1.setText(String.format(getResources().getString(
				R.string.before_create_text, name)));
		String[]strs = {"¼Ò","·ÅÐÄ"};
		SpannableString str = getSpecialString(
				String.format(getResources().getString(
						R.string.begin_create_text, name)), strs);
		text2.setText(str);
		beforeBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				beforeCreate.setVisibility(View.GONE);
				beginCreate.setVisibility(View.VISIBLE);
			}
		});
		beginBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivity(new Intent(getActivity(),
						QuanCreateActivity.class));
			}
		});
		return layout;
	}

	private SpannableString getSpecialString(String text, String[] specifiedTexts) {

		int sTextLength1 = specifiedTexts[0].length();
		int sTextLength2 = specifiedTexts[1].length();
		
		int start1 = text.indexOf(specifiedTexts[0], 22);
		int start2 = text.indexOf(specifiedTexts[1],22);
		SpannableString styledText = new SpannableString(text);
		styledText.setSpan(
				new AbsoluteSizeSpan(DeviceInfoUtil.dip2px(getActivity(), 24)),
				start1, start1 + sTextLength1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		styledText.setSpan(
				new AbsoluteSizeSpan(DeviceInfoUtil.dip2px(getActivity(), 24)),
				start2, start2 + sTextLength2, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		return styledText;
	}
}
