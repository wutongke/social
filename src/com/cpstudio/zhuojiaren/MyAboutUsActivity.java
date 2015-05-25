package com.cpstudio.zhuojiaren;

import com.cpstudio.zhuojiaren.helper.JsonHandler;
import com.cpstudio.zhuojiaren.helper.ZhuoCommHelper;
import com.cpstudio.zhuojiaren.helper.ZhuoConnHelper;
import com.cpstudio.zhuojiaren.imageloader.LoadImage;
import com.cpstudio.zhuojiaren.model.AboutUsVO;
import com.cpstudio.zhuojiaren.model.MsgTagVO;
import com.cpstudio.zhuojiaren.model.PicVO;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;

public class MyAboutUsActivity extends Activity {
	private ZhuoConnHelper mConnHelper = null;
	private LoadImage mLoadImage = new LoadImage();
	private String id = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_about_us);
		mConnHelper = ZhuoConnHelper.getInstance(getApplicationContext());
		Intent intent = getIntent();
		id = intent.getStringExtra("id");
		loadData();
		initClick();
	}

	private void loadData() {
		String params = ZhuoCommHelper.getUrlGetAboutDetail();
		params += "?id=" + id;
		mConnHelper.getFromServer(params, mUIHandler, MsgTagVO.DATA_LOAD,
				MyAboutUsActivity.this, true, new OnCancelListener() {

					@Override
					public void onCancel(DialogInterface dialog) {
						MyAboutUsActivity.this.finish();
					}
				});
	}

	private void initClick() {
		findViewById(R.id.buttonBack).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				MyAboutUsActivity.this.finish();
			}
		});
	}

	@SuppressLint("HandlerLeak")
	private Handler mUIHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MsgTagVO.DATA_LOAD: {
				if (msg.obj != null && !msg.obj.equals("")) {
					JsonHandler nljh = new JsonHandler((String) msg.obj,
							getApplicationContext());
					AboutUsVO item = nljh.parseAboutUs();
					((TextView) findViewById(R.id.userNameShow)).setText(item
							.getTitle());
					((TextView) findViewById(R.id.textViewContent))
							.setText(item.getContent());
					PicVO pic = item.getPic();
					ImageView iv = (ImageView) findViewById(R.id.imageViewContainer);
					String url = pic.getOrgurl();
					iv.setImageResource(R.drawable.default_image);
					iv.setTag(url);
					mLoadImage.addTask(url, iv);
					mLoadImage.doTask();
				}
				break;
			}
			}
		}
	};

}
