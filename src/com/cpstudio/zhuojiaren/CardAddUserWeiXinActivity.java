package com.cpstudio.zhuojiaren;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.TextView;

public class CardAddUserWeiXinActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_card_add_user_weixin);
		Intent i = getIntent();
		String weixin = i.getStringExtra(CardEditActivity.EDIT_WEIXIN_STR1);
		((TextView) findViewById(R.id.textViewWeixin)).setText(weixin);
		String weixinopen = i.getStringExtra(CardEditActivity.EDIT_WEIXIN_STR2);
		if (weixinopen != null && weixinopen.equals("1")) {
			((CheckBox) findViewById(R.id.checkBoxIsOpen)).setChecked(true);
		}else{
			((CheckBox) findViewById(R.id.checkBoxIsOpen)).setChecked(false);
		}
		initClick();
	}

	private void initClick() {
		findViewById(R.id.buttonBack).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				CardAddUserWeiXinActivity.this.finish();
			}
		});
		findViewById(R.id.buttonSubmit).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						Intent i = new Intent();
						i.putExtra(CardEditActivity.EDIT_WEIXIN_STR1,
								((TextView) findViewById(R.id.textViewWeixin))
										.getText().toString());
						if (((CheckBox) findViewById(R.id.checkBoxIsOpen))
								.isChecked()) {
							i.putExtra(CardEditActivity.EDIT_WEIXIN_STR2, "1");
						} else {
							i.putExtra(CardEditActivity.EDIT_PHONE_STR2, "0");
						}
						setResult(RESULT_OK, i);
						CardAddUserWeiXinActivity.this.finish();
					}
				});
	}

}
