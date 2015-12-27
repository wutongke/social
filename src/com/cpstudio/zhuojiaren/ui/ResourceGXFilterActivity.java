package com.cpstudio.zhuojiaren.ui;

import java.util.ArrayList;
import java.util.List;

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
import com.cpstudio.zhuojiaren.helper.ZhuoConnHelper;
import com.cpstudio.zhuojiaren.model.GXTypeCodeData;
import com.cpstudio.zhuojiaren.model.GXTypeItemVO;
import com.cpstudio.zhuojiaren.model.ResourceGXVO;
import com.cpstudio.zhuojiaren.model.gtype;
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
	private int typeQuanzi = 0;
	// private String[] quanziType;

	int mainTypeIds = R.array.gongxu_main_type;
	// int[] subTypeIds = {R.array.subtype_fund, R.array.subtype_commercy,
	// R.array.subtype_people,
	// R.array.subtype_tec,R.array.subtype_renmai,R.array.subtype_zhihui};
	int requestType = 0;
	List<gtype> gtypes;

	String[] mainTypesArrays;
	List<List<String>> subStrings;

	void getCodedData() {
		GXTypeCodeData baseCodeData = ZhuoConnHelper.getInstance(
				getApplicationContext()).getGxTypeCodeDataSet();
		List<String> mainTypes = new ArrayList<String>();
		subStrings = new ArrayList<List<String>>();
		gtypes=new ArrayList<gtype>();
		List<GXTypeItemVO> sourcesTypes;
		if (requestType == ResourceGXVO.RESOURCE_FIND)
			sourcesTypes = baseCodeData.getSupply();

		else
			sourcesTypes = baseCodeData.getDemand();

		if (baseCodeData != null) {
			// gtypes = baseCodeData.getSdtype();

			for (GXTypeItemVO item : sourcesTypes) {
				if (item.getSdtype() != null && item.getSdtype().size() > 0) {
					mainTypes.add(item.getTitle());
					List<String> types = new ArrayList<String>();
					List<gtype> typeItems = item.getSdtype();
					for (gtype typeItem : typeItems) {
						gtypes.add(typeItem);
						types.add(typeItem.getContent());

					}
					subStrings.add(types);
				}

			}
		}
		mainTypesArrays = new String[mainTypes.size()];
		for (int i = 0; i < mainTypes.size(); i++)
			mainTypesArrays[i] = mainTypes.get(i);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_gx_filter);
		ButterKnife.inject(this);
		requestType = getIntent().getIntExtra(ResourceGXVO.RESOURCEGXTYPE, 0);

		// quanziType = getResources().getStringArray(R.array.quanzi_type);
		mContext = this;
		initTitle();
		title.setText(R.string.title_gx_filter);
		initOnclick();
		getCodedData();
	}

	private void initOnclick() {
		// TODO Auto-generated method stub
		filterOk.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// TODO Auto-generated method stub
				String content = type.getText().toString();
				String array[] = content.split(" ");
				String mCategory = array[1];
				int typecode = getCodeByName(mCategory);
				Intent data = new Intent();
				// 1按类型2按地区
				data.putExtra(ResourceGXVO.RESOURCEGXTYPE, requestType);
				data.putExtra(ResourceGXVO.RESOURCEGXFILTER_LOCATION, locate
						.getText().toString());
				data.putExtra(ResourceGXVO.RESOURCEGXFILTER_TYPE, typecode);
				setResult(RESULT_OK, data);
				((android.app.Activity) mContext).finish();
			}
		});
		typeLayout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				final TwoLeverChooseDialog typeChoose = new TwoLeverChooseDialog(
						mContext, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT,
						"资金资源", "所有资金", mainTypesArrays, subStrings);
				typeChoose.setButton(DialogInterface.BUTTON_POSITIVE, "确定",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub
								type.setText(typeChoose.getSelectedContent()
										.getText().toString());

							}
						});
				typeChoose.setButton(DialogInterface.BUTTON_NEGATIVE, "取消",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub

							}
						});
				typeChoose.setTitle("选择类型");
				typeChoose.show();
			}
		});
		locateLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// 可用更具有通用性的TwoLeverChooseDialog来代替
				final PlaceChooseDialog placeChoose = new PlaceChooseDialog(
						mContext, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT, "北京",
						"北京");
				placeChoose.setButton(DialogInterface.BUTTON_POSITIVE, "确定",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub
								locate.setText(placeChoose.getPlace().getText()
										.toString());
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

	int getCodeByName(String name) {
		if (gtypes == null)
			return -1;
		for (int i = 0; i < gtypes.size(); i++)
			if (name.equals(gtypes.get(i).getContent()))
				return gtypes.get(i).getId();
		return -1;
	}
}
