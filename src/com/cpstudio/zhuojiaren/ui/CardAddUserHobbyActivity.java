package com.cpstudio.zhuojiaren.ui;

import java.util.HashMap;
import java.util.Map;

import com.cpstudio.zhuojiaren.R;
import com.cpstudio.zhuojiaren.R.array;
import com.cpstudio.zhuojiaren.R.id;
import com.cpstudio.zhuojiaren.R.layout;
import com.cpstudio.zhuojiaren.R.string;
import com.cpstudio.zhuojiaren.helper.JsonHandler;
import com.cpstudio.zhuojiaren.helper.ConnHelper;
import com.cpstudio.zhuojiaren.model.MsgTagVO;
import com.cpstudio.zhuojiaren.model.UserNewVO;
import com.cpstudio.zhuojiaren.util.CommonUtil;
import com.cpstudio.zhuojiaren.util.DeviceInfoUtil;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.ToggleButton;

public class CardAddUserHobbyActivity extends Activity {
	private int width = 720;
	private float times = 2;
	private int padding = 5;
	private int baseMargin = 19;
	private Map<String, Boolean> selected = new HashMap<String, Boolean>();
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
		setContentView(R.layout.activity_card_add_user_hobby);
		Intent intent = getIntent();
		String hobby = intent.getStringExtra(CardEditActivity.EDIT_HOBBY_STR);
		String[] hobbys = hobby.split(";|£º| ");
		Map<String, Boolean> hobbymap = new HashMap<String, Boolean>();
		for (String item : hobbys) {
			hobbymap.put(item, false);
		}
		LayoutInflater inflater = LayoutInflater
				.from(CardAddUserHobbyActivity.this);
		TableLayout tl = (TableLayout) findViewById(R.id.tableLayout);
		this.width = DeviceInfoUtil.getDeviceCsw(getApplicationContext());
		this.times = DeviceInfoUtil.getDeviceCsd(getApplicationContext());
		padding = (int) (padding * times);
		baseMargin = (int) (baseMargin * times);
		int restWidth = width - 2 * padding - 3 * baseMargin;
		int widthOne = restWidth / 4;
		baseMargin = (int) ((restWidth - widthOne * 4) / 3) + baseMargin;
		tl.setPadding(padding, 0, padding, 0);
		String[] items = getResources().getStringArray(R.array.array_hobby);
		TableRow tr = null;
		for (int i = 0; i < items.length; i++) {
			if (i % 4 == 0) {
				tr = new TableRow(CardAddUserHobbyActivity.this);
				tl.addView(tr);
			}
			ToggleButton tb = (ToggleButton) inflater.inflate(
					R.layout.item_toggle, null);
			tb.setText(items[i]);
			tb.setTextOn(items[i]);
			tb.setTextOff(items[i]);
			tb.setOnCheckedChangeListener(new OnCheckedChangeListener() {

				@Override
				public void onCheckedChanged(CompoundButton buttonView,
						boolean isChecked) {
					if (isChecked) {
						buttonView.setTextColor(Color.rgb(0, 72, 255));
						selected.put(buttonView.getText().toString(), true);
					} else {
						buttonView.setTextColor(Color.rgb(52, 52, 52));
						selected.put(buttonView.getText().toString(), false);
					}
				}
			});
			tr.addView(tb);
			TableRow.LayoutParams trlp = new TableRow.LayoutParams(widthOne,
					TableRow.LayoutParams.WRAP_CONTENT);
			trlp.rightMargin = baseMargin;
			trlp.bottomMargin = baseMargin * 3 / 8;
			trlp.topMargin = baseMargin * 3 / 8;
			tb.setLayoutParams(trlp);
			if (hobbymap.containsKey(items[i])) {
				tb.setChecked(true);
				hobbymap.put(items[i], true);
				selected.put(items[i], true);
			} else {
				selected.put(items[i], false);
			}
		}
		String hobbyStr = "";
		for (String item : hobbymap.keySet()) {
			if (!hobbymap.get(item)) {
				hobbyStr += item + " ";
			}
		}
		((EditText) findViewById(R.id.editText)).setText(hobbyStr);
		initClick();
	}

	private void initClick() {
		findViewById(R.id.buttonBack).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);  
				imm.hideSoftInputFromWindow(CardAddUserHobbyActivity.this.getCurrentFocus().getWindowToken(), 0);
				CardAddUserHobbyActivity.this.finish();
			}
		});
		findViewById(R.id.buttonSubmit).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						String hobby = ((EditText) findViewById(R.id.editText))
								.getText().toString();
						for (String item : selected.keySet()) {
							if (selected.get(item)) {
								hobby += " " + item;
							}
						}
						if (hobby.startsWith(" ")) {
							hobby = hobby.substring(1);
						}
						
						UserNewVO userInfo = new UserNewVO();
						userInfo.setHobby(hobby);
						ConnHelper.getInstance(getApplicationContext())
								.modifyUserInfo(mUIHandler, MsgTagVO.DATA_LOAD,
										userInfo);
					}
				});
	}
}
