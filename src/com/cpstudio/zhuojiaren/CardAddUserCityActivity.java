package com.cpstudio.zhuojiaren;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;

import com.cpstudio.zhuojiaren.helper.JsonHandler;
import com.cpstudio.zhuojiaren.helper.ZhuoConnHelper;
import com.cpstudio.zhuojiaren.model.City;
import com.cpstudio.zhuojiaren.model.MsgTagVO;
import com.cpstudio.zhuojiaren.model.UserNewVO;
import com.cpstudio.zhuojiaren.util.CommonUtil;
import com.cpstudio.zhuojiaren.widget.PlaceChooseDialog;

public class CardAddUserCityActivity extends Activity {
	@InjectView(R.id.tvPlace)
	TextView tvPlace;
	@InjectView(R.id.tvTown)
	TextView tvTown;
	@InjectView(R.id.tvOtherTowns)
	TextView tvOtherTowns;

	List<City> cityList;
	ZhuoConnHelper mConnHelper;
	ArrayList<String> codes = new ArrayList<String>(3);
	boolean isEditable = false;
	private Handler mUIHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MsgTagVO.DATA_LOAD: {
				if (JsonHandler.checkResult((String) msg.obj,
						getApplicationContext())) {
					CommonUtil.displayToast(getApplicationContext(),
							R.string.bg_success);
				} else
					CommonUtil.displayToast(getApplicationContext(),
							R.string.bg_failed);
				break;
			}
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_card_add_user_city);
		ButterKnife.inject(this);
		mConnHelper = ZhuoConnHelper.getInstance(getApplicationContext());
		cityList = mConnHelper.getCitys();
		Intent i = getIntent();
		isEditable = i.getBooleanExtra(CardEditActivity.EDITABLE, false);
		int place = i.getIntExtra(CardEditActivity.EDIT_PLACE_STR1, 0);
		int othertowns = i.getIntExtra(CardEditActivity.EDIT_PLACE_STR2, 0);
		String towns = i.getStringExtra(CardEditActivity.EDIT_PLACE_STR3);

		fillName(tvPlace, place);
		fillName(tvTown, othertowns);
		fillName(tvOtherTowns, towns);
		findViewById(R.id.buttonBack).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);  
				imm.hideSoftInputFromWindow(CardAddUserCityActivity.this.getCurrentFocus().getWindowToken(), 0);
				CardAddUserCityActivity.this.finish();
			}
		});
		if (isEditable)
			initClick();
		else
			findViewById(R.id.buttonSubmit).setVisibility(View.GONE);
		codes.add("");
		codes.add("");
		codes.add("");
	}

	void fillName(TextView tv, int code) {
		if (tv == null)
			return;
		if (cityList == null || code < 1 || code > cityList.size())
			tv.setText(code + "");
		else
			tv.setText(cityList.get(code - 1).getCityName());
	}

	void fillName(TextView tv, String codes) {
		if (tv == null || codes == null)
			return;
		if (cityList == null)
			tv.setText(codes);
		else {
			String text = "";
			String[] codeArray = codes.split(",");
			if (codeArray == null || codeArray.length < 1) {
				tv.setText(codes);
				return;
			}
			int num = 0;
			String tmpStr = "";
			for (String str : codeArray) {
				try {
					num = Integer.parseInt(str);
					if (num >= 1 && num <= cityList.size()) {
						tmpStr += cityList.get(num - 1).getCityName() + ",";
					}
				} catch (Exception e) {
					text = tmpStr + text + "str";
					tv.setText(text);
					return;
				}
			}
			tv.setText(tmpStr);
		}
	}

	private void initClick() {
		
		findViewById(R.id.buttonSubmit).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						int code1 = 1, code2 = 1;
						if (!"".equals(codes.get(0)))
							code1 = Integer.parseInt(codes.get(0));
						if (!"".equals(codes.get(1)))
							code2 = Integer.parseInt(codes.get(1));
						UserNewVO userInfo = new UserNewVO();
						userInfo.setCity(code1);
						userInfo.setHometown(code2);
						userInfo.setTravelCity(codes.get(2));
						ZhuoConnHelper.getInstance(getApplicationContext())
								.modifyUserInfo(mUIHandler, MsgTagVO.DATA_LOAD,
										userInfo);
					}
				});
		tvPlace.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				choosePlace(tvPlace, 0);
			}
		});

		tvTown.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				choosePlace(tvTown, 1);
			}
		});
		tvOtherTowns.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				choosePlace(tvOtherTowns, 2);
			}
		});
		findViewById(R.id.buttonBack).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				CardAddUserCityActivity.this.finish();
			}
		});

	}

	void choosePlace(final TextView edtView, final int codeIndex) {
		// TODO Auto-generated method stub
		final PlaceChooseDialog placeChoose = new PlaceChooseDialog(
				CardAddUserCityActivity.this,
				AlertDialog.THEME_DEVICE_DEFAULT_LIGHT, "北京", "北京");
		placeChoose.setButton(DialogInterface.BUTTON_POSITIVE, "确定",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						String value = edtView.getText().toString();
							value = placeChoose.getPlace().getText().toString();
							codes.set(codeIndex, placeChoose.getCityCode() + "");
						edtView.setText(value);
					}
				});
		placeChoose.setButton(DialogInterface.BUTTON_NEGATIVE, "取消",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub

					}
				});
		placeChoose.setTitle(R.string.choose_place);
		placeChoose.show();
	}

}
