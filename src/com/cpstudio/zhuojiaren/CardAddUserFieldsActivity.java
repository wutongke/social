package com.cpstudio.zhuojiaren;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.cpstudio.zhuojiaren.helper.ZhuoCommHelper;
import com.cpstudio.zhuojiaren.widget.PopupWindows;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

public class CardAddUserFieldsActivity extends Activity {
	private int mMax = 3;
	private ArrayList<String> mSelcted = new ArrayList<String>();
	private PopupWindows pwh;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		
		
		setContentView(R.layout.activity_card_add_user_fields);
		pwh = new PopupWindows(CardAddUserFieldsActivity.this);
		Intent intent = getIntent();
		String fieldStr = intent
				.getStringExtra(CardEditActivity.EDIT_FIELD_STR);
		List<String> fieldsSelect = new ArrayList<String>();
		if (fieldStr.contains(";")) {
			fieldsSelect = Arrays.asList(fieldStr.split(";"));
		} else {
			fieldsSelect.add(fieldStr);
		}
		LayoutInflater inflater = LayoutInflater
				.from(CardAddUserFieldsActivity.this);
		String[] fields = getResources().getStringArray(
				R.array.array_field_type);
		LinearLayout ll = (LinearLayout) findViewById(R.id.linearLayoutField);
		for (int i = 0; i < fields.length; i++) {
			View v = inflater.inflate(R.layout.item_text_select_list, null);
			final String field = fields[i];
			((TextView) v.findViewById(R.id.textViewTitle)).setText(field);
			CheckBox cb = (CheckBox) v.findViewById(R.id.checkBoxSelect);
			cb.setOnCheckedChangeListener(new OnCheckedChangeListener() {

				@Override
				public void onCheckedChanged(CompoundButton v, boolean isChecked) {
					if (isChecked) {
						if (mMax > 0 && (mMax > mSelcted.size()) || mMax == -1) {
							mSelcted.add(field);
						} else {
							v.setChecked(false);
							pwh.showPopDlgOne(
									findViewById(R.id.rootLayout),
									null, R.string.error11);
						}
					} else {
						mSelcted.remove(field);
					}

				}
			});
			if (fieldsSelect.contains(field)) {
				cb.setChecked(true);
			}
			ll.addView(v);
			if (i + 1 != fields.length) {
				LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT,
						LayoutParams.WRAP_CONTENT);
				ImageView iv = new ImageView(CardAddUserFieldsActivity.this);
				iv.setImageResource(R.drawable.bg_border3);
				iv.setLayoutParams(lp);
				ll.addView(iv);
			}
		}

		initClick();
	}

	private void initClick() {
		findViewById(R.id.buttonBack).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);  
			imm.hideSoftInputFromWindow(CardAddUserFieldsActivity.this.getCurrentFocus().getWindowToken(), 0);
				CardAddUserFieldsActivity.this.finish();
			}
		});
		findViewById(R.id.buttonSubmit).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						String selected = "";
						for (String str : mSelcted) {
							selected += str + ";";
						}
						selected = ZhuoCommHelper.subLast(selected);
						Intent intent = new Intent();
						intent.putExtra(CardEditActivity.EDIT_FIELD_STR,
								selected);
						setResult(RESULT_OK, intent);
						CardAddUserFieldsActivity.this.finish();
					}
				});
	}

}
