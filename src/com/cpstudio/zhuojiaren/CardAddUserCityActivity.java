package com.cpstudio.zhuojiaren;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import butterknife.ButterKnife;
import butterknife.InjectView;

import com.cpstudio.zhuojiaren.helper.JsonHandler;
import com.cpstudio.zhuojiaren.helper.ZhuoConnHelper;
import com.cpstudio.zhuojiaren.model.City;
import com.cpstudio.zhuojiaren.model.Province;
import com.cpstudio.zhuojiaren.widget.PlaceChooseDialog;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.Editable;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class CardAddUserCityActivity extends Activity {
	@InjectView(R.id.tvPlace)
	TextView tvPlace;
	@InjectView(R.id.tvTown)
	TextView tvTown;
	@InjectView(R.id.tvOtherTowns)
	TextView tvOtherTowns;

	private static int OTHER_TOWN = 0;
	List<City> cityList;
	ZhuoConnHelper mConnHelper;
	ArrayList<String> codes = new ArrayList<String>(3);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_card_add_user_city);
		ButterKnife.inject(this);
		mConnHelper = ZhuoConnHelper.getInstance(getApplicationContext());
		cityList = mConnHelper.getCitys();
		// 根据provList，从编号获得城市名称
		Intent i = getIntent();
		int place = i.getIntExtra(CardEditActivity.EDIT_PLACE_STR1, 0);
		int othertowns = i.getIntExtra(CardEditActivity.EDIT_PLACE_STR2, 0);
		String towns = i.getStringExtra(CardEditActivity.EDIT_PLACE_STR3);

		fillName(tvPlace, place);
		fillName(tvTown, othertowns);
		fillName(tvOtherTowns, towns);

		initClick();
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
			String text = getResources().getString(R.string.code_error);
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
		findViewById(R.id.buttonBack).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				CardAddUserCityActivity.this.finish();
			}
		});
		findViewById(R.id.buttonSubmit).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						Intent intent = new Intent();

						int code1 = 1, code2 = 1;
						if (!"".equals(codes.get(0)))
							code1 = Integer.parseInt(codes.get(0));
						if (!"".equals(codes.get(1)))
							code2 = Integer.parseInt(codes.get(1));
						intent.putExtra(CardEditActivity.EDIT_PLACE_STR1, code1);
						intent.putExtra(CardEditActivity.EDIT_PLACE_STR2, code2);
						intent.putExtra(CardEditActivity.EDIT_PLACE_STR3,
								codes.get(2));
						setResult(RESULT_OK, intent);
						CardAddUserCityActivity.this.finish();
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
						if (codeIndex == 2)// 多个地点
						{
							if (!"".equals(value)) {
								value = value
										+ ","
										+ placeChoose.getPlace().getText()
												.toString();
								codes.set(codeIndex, codes.get(codeIndex) + ","
										+ placeChoose.getCityCode());
							}

							else {
								value = placeChoose.getPlace().getText()
										.toString();
								codes.set(codeIndex, placeChoose.getCityCode()
										+ "");
							}
						} else {
							value = placeChoose.getPlace().getText().toString();
							codes.set(codeIndex, placeChoose.getCityCode() + "");
						}
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
