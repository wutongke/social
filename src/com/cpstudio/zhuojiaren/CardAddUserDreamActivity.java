package com.cpstudio.zhuojiaren;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

import com.cpstudio.zhuojiaren.helper.JsonHandler;
import com.cpstudio.zhuojiaren.helper.ZhuoConnHelper;
import com.cpstudio.zhuojiaren.model.MsgTagVO;
import com.cpstudio.zhuojiaren.widget.PopupWindows;

public class CardAddUserDreamActivity extends Activity {

	private LinearLayout linearLayout;
	private ArrayList<String> dreamTags = new ArrayList<String>();
	private ArrayList<String> selected = new ArrayList<String>();
	private PopupWindows pwh = null;
	private ZhuoConnHelper mConnHelper = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_card_add_user_dream);
		mConnHelper = ZhuoConnHelper.getInstance(getApplicationContext());
		pwh = new PopupWindows(CardAddUserDreamActivity.this);
		linearLayout = (LinearLayout) findViewById(R.id.linearLayoutDreams);
		findViewById(R.id.editText).setTag("dream0");
		dreamTags.add("dream0");
		initClick();
		Intent intent = getIntent();
		ArrayList<String> dreams = intent
				.getStringArrayListExtra(CardEditActivity.EDIT_DREAM_STR);
		if (dreams != null && dreams.size() > 0) {
			((EditText) findViewById(R.id.editText)).setText(dreams.get(0));
			for (int i = 1; i < dreams.size(); i++) {
				addEditText(dreams.get(i));
			}
		}
	}

	private void initClick() {
		findViewById(R.id.buttonBack).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				CardAddUserDreamActivity.this.finish();
			}
		});
		findViewById(R.id.buttonSubmit).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						String dreamsStr = "[";
						for (int i = 0; i < dreamTags.size(); i++) {
							String dream = ((EditText) linearLayout
									.findViewWithTag(dreamTags.get(i)))
									.getText().toString();
							if (!dream.trim().equals("")) {
								selected.add(dream);
								dreamsStr += "{\"dream\":\"" + dream + "\"},";
							}
						}
						if (dreamsStr.length() > 1) {
							dreamsStr = dreamsStr.substring(0,
									dreamsStr.length() - 1);
						}
						dreamsStr += "]";
						mConnHelper
								.addDream(dreamsStr, mUIHandler,
										MsgTagVO.PUB_INFO,
										CardAddUserDreamActivity.this, true,
										null, null);
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
					CardAddUserDreamActivity.this.finish();
				} else {
					pwh.showPopDlgOne(findViewById(R.id.rootLayout), null,
							R.string.error5);
				}
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
		view.findViewById(R.id.editText).setTag("dream" + dreamTags.size());
		dreamTags.add("dream" + dreamTags.size());
		linearLayout.addView(view);
	}
}
