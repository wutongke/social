package com.cpstudio.zhuojiaren;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class CardAddUserStarActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_card_add_user_star);
		Intent i = getIntent();
		int level = 1;
		String levelStr = i.getStringExtra(CardEditActivity.EDIT_LEVEL_STR);
		if (levelStr != null && !levelStr.equals("")) {
			level = Integer.valueOf(levelStr);
		}
		LinearLayout ll = (LinearLayout) findViewById(R.id.linearLayoutLevel);
		String[] levels = getResources().getStringArray(
				R.array.array_level_type);
		if (level > levels.length - 1) {
			level = levels.length - 1;
		}
		((TextView) findViewById(R.id.textViewLevel))
				.setText(levels[level]);
		for (int j = 0; j < level; j++) {
			((ImageView) ll.getChildAt(j))
					.setImageResource(R.drawable.ico_level_star_on);
		}
		initClick();
	}

	private void initClick() {
		findViewById(R.id.buttonBack).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				CardAddUserStarActivity.this.finish();
			}
		});
	}

}
