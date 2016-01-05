package com.cpstudio.zhuojiaren;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

import com.cpstudio.zhuojiaren.R;
import com.cpstudio.zhuojiaren.R.id;
import com.cpstudio.zhuojiaren.R.layout;
import com.cpstudio.zhuojiaren.R.string;
import com.cpstudio.zhuojiaren.helper.JsonHandler;
import com.cpstudio.zhuojiaren.helper.ConnHelper;
import com.cpstudio.zhuojiaren.model.MsgTagVO;
import com.cpstudio.zhuojiaren.model.UserNewVO;
import com.cpstudio.zhuojiaren.util.CommonUtil;
import com.cpstudio.zhuojiaren.widget.PopupWindows;

public class CardAddUserDreamActivity extends Activity {

	private LinearLayout linearLayout;
	private ArrayList<String> dreamTags = new ArrayList<String>();
	private ArrayList<String> selected = new ArrayList<String>();
	private PopupWindows pwh = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_card_add_user_dream);
		pwh = new PopupWindows(CardAddUserDreamActivity.this);
		linearLayout = (LinearLayout) findViewById(R.id.linearLayoutDreams);
		findViewById(R.id.editText).setTag("dream0");
		initClick();
		Intent intent = getIntent();
		String str = intent.getStringExtra(CardEditActivity.EDIT_DREAM_STR);
		String strs[] = null;
		ArrayList<String> dreams = new ArrayList<String>();
		if (str != null) {
			strs = str.split(";");
			for (String dr : strs)
				dreams.add(dr);
		}
		if (dreams != null && dreams.size() > 0) {
			((EditText) findViewById(R.id.editText)).setText(dreams.get(0));
			dreamTags.add(dreams.get(0));
			for (int i = 1; i < dreams.size(); i++) {
				addEditText(dreams.get(i));
			}
		}
	}

	private void initClick() {
		findViewById(R.id.buttonBack).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);  
				imm.hideSoftInputFromWindow(CardAddUserDreamActivity.this.getCurrentFocus().getWindowToken(), 0);
				CardAddUserDreamActivity.this.finish();
			}
		});
		findViewById(R.id.buttonSubmit).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						String dreamsStr = ((EditText) findViewById(R.id.editText)).getText().toString();
						if(dreamTags.size()>1)
						for (int i = 1; i < dreamTags.size(); i++) {
							String dream = ((EditText) linearLayout
									.findViewWithTag(dreamTags.get(i)))
									.getText().toString();
							if (!dreamsStr.trim().equals("")) {
								dreamsStr +=  ";"+dream;
								selected.add(dream);
							}
						}
						
						UserNewVO userInfo = new UserNewVO();
						userInfo.setDream(dreamsStr);
						ConnHelper.getInstance(getApplicationContext())
								.modifyUserInfo(mUIHandler, MsgTagVO.DATA_LOAD,
										userInfo);
					}
				});
		findViewById(R.id.buttonAdd).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				addEditText("");
			}
		});
	}

	@SuppressLint("HandlerLeak")
	private Handler mUIHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MsgTagVO.PUB_INFO: {
				if (JsonHandler.checkResult((String) msg.obj)) {
					Intent intent = new Intent();
					intent.putStringArrayListExtra(
							CardEditActivity.EDIT_DREAM_STR, selected);
					setResult(RESULT_OK, intent);
				} else {
					pwh.showPopDlgOne(findViewById(R.id.rootLayout), null,
							R.string.error5);
				}
				break;
			}
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

	private void addEditText(String text) {
		LayoutInflater inflater = LayoutInflater
				.from(CardAddUserDreamActivity.this);
		LayoutParams lp = (LayoutParams) findViewById(R.id.linearLayoutDream)
				.getLayoutParams();
		View view = inflater.inflate(R.layout.item_dream_add, null);
		view.setLayoutParams(lp);
		((EditText) view.findViewById(R.id.editText)).setText(text);
		view.findViewById(R.id.editText).setTag(text);
		dreamTags.add(text);
		linearLayout.addView(view);
	}
}
