package com.cpstudio.zhuojiaren.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;

import com.cpstudio.zhuojiaren.BaseActivity;
import com.cpstudio.zhuojiaren.R;
import com.cpstudio.zhuojiaren.widget.PlaceChooseDialog;

public class QuanziFilterActivity extends BaseActivity {
	protected static final String Activity = null;
	@InjectView(R.id.according_locate)
	LinearLayout locateLayout;
	@InjectView(R.id.according_type)
	LinearLayout typeLayout;
	@InjectView(R.id.locate_text)
	TextView locate;
	@InjectView(R.id.type_text)
	TextView type;
	@InjectView(R.id.filter_ok_btn)
	Button filterOk;
	private Context mContext;
	private int typeQuanzi=0;
	private String[] quanziType;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_quanzi_filter);
		ButterKnife.inject(this);
		quanziType = getResources().getStringArray(R.array.quanzi_type);
		mContext = this;
		initTitle();
		title.setText(R.string.filter_quanzi);
		initOnclick();
	}
	private void initOnclick() {
		// TODO Auto-generated method stub
		filterOk.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
					// TODO Auto-generated method stub
					Intent data = new Intent();
					//1按类型2按地区
					data.putExtra("type", 1);
					setResult(RESULT_OK, data);
					((android.app.Activity) mContext).finish();
			}
		});
		typeLayout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				
				// TODO Auto-generated method stub
				new AlertDialog.Builder(mContext,AlertDialog.THEME_DEVICE_DEFAULT_LIGHT)
						.setTitle("选择圈子类型")
						.setIcon(R.drawable.ico_syzy)
						.setSingleChoiceItems(quanziType,0,
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										// TODO Auto-generated method stub
										typeQuanzi = which;
									}
								})
						.setPositiveButton("确定", new DialogInterface.OnClickListener() {
									
									@Override
									public void onClick(DialogInterface dialog, int which) {
										// TODO Auto-generated method stub
										type.setText(quanziType[typeQuanzi]);
									}
								})
						.setNegativeButton("取消", null).create().show();
			
			}
		});
		locateLayout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				final PlaceChooseDialog placeChoose = new PlaceChooseDialog(
						mContext, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT,"北京","北京");
				placeChoose.setButton(DialogInterface.BUTTON_POSITIVE, "确定",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub
								locate.setText(placeChoose.getPlace().getText().toString());
							}
						});
				placeChoose.setButton(DialogInterface.BUTTON_NEGATIVE, "取消",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub

							}
						});
				placeChoose.setTitle("选择地点");
				placeChoose.show();
			}
		});
	}
}
