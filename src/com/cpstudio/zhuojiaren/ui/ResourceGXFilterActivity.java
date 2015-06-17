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
import com.cpstudio.zhuojiaren.model.ResourceGXVO;
import com.cpstudio.zhuojiaren.widget.PlaceChooseDialog;
import com.cpstudio.zhuojiaren.widget.TwoLeverChooseDialog;

public class ResourceGXFilterActivity extends BaseActivity {
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
//	private String[] quanziType;
	
	int mainTypeIds=R.array.gongxu_main_type;
	int[] subTypeIds = {R.array.subtype_fund, R.array.subtype_commercy, 
			R.array.subtype_people, R.array.subtype_tec,R.array.subtype_renmai,R.array.subtype_zhihui};
	int requestType=0;;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_gx_filter);
		ButterKnife.inject(this);
		requestType=getIntent().getIntExtra(ResourceGXVO.RESOURCEGXTYPE, 0);

//		quanziType = getResources().getStringArray(R.array.quanzi_type);
		mContext = this;
		initTitle();
		title.setText(R.string.title_gx_filter);
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
					//1������2������
					data.putExtra(ResourceGXVO.RESOURCEGXTYPE, type.getText().toString());
					data.putExtra(ResourceGXVO.RESOURCEGXFILTER_LOCATION, locate.getText().toString());
					data.putExtra(ResourceGXVO.RESOURCEGXFILTER_TYPE, requestType);
					setResult(RESULT_OK, data);
					((android.app.Activity) mContext).finish();
					
			}
		});
		typeLayout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				final TwoLeverChooseDialog typeChoose = new TwoLeverChooseDialog(
						mContext, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT,"�ʽ���Դ","�����ʽ�",mainTypeIds,subTypeIds);
				typeChoose.setButton(DialogInterface.BUTTON_POSITIVE, "ȷ��",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub
								type.setText(typeChoose.getSelectedContent().getText().toString());
							}
						});
				typeChoose.setButton(DialogInterface.BUTTON_NEGATIVE, "ȡ��",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub

							}
						});
				typeChoose.setTitle("ѡ������");
				typeChoose.show();
			}
		});
		locateLayout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
//���ø�����ͨ���Ե�TwoLeverChooseDialog������
				final PlaceChooseDialog placeChoose = new PlaceChooseDialog(
						mContext, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT,"����","����");
				placeChoose.setButton(DialogInterface.BUTTON_POSITIVE, "ȷ��",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub
								locate.setText(placeChoose.getPlace().getText().toString());
							}
						});
				placeChoose.setButton(DialogInterface.BUTTON_NEGATIVE, "ȡ��",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub

							}
						});
				placeChoose.setTitle("ѡ��ص�");
				placeChoose.show();
			}
		});
	}
}