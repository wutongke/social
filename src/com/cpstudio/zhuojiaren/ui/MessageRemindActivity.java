package com.cpstudio.zhuojiaren.ui;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;
import butterknife.ButterKnife;
import butterknife.InjectView;

import com.cpstudio.zhuojiaren.BaseActivity;
import com.cpstudio.zhuojiaren.R;
import com.cpstudio.zhuojiaren.helper.JsonHandler;
import com.cpstudio.zhuojiaren.helper.ZhuoCommHelper;
import com.cpstudio.zhuojiaren.helper.ZhuoConnHelper;
import com.cpstudio.zhuojiaren.model.MsgTagVO;
import com.cpstudio.zhuojiaren.model.UserVO;

public class MessageRemindActivity extends BaseActivity {
	@InjectView(R.id.amr_message_remind)
	ImageView messageRemind;
	@InjectView(R.id.amr_sound)
	ImageView sound;
	@InjectView(R.id.amr_vibrate)
	ImageView vibrate;
	private ZhuoConnHelper mConnHelper = null;
	private String isAlert = "1";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_messaeg_remind);
		ButterKnife.inject(this);
		mConnHelper = ZhuoConnHelper.getInstance(getApplicationContext());
		initTitle();
		TextView goBack = (TextView)findViewById(R.id.activity_back);
		goBack.setText(R.string.new_message);
		initClick();
		loadData();
	}
	private void initClick() {
		// TODO Auto-generated method stub
		messageRemind.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (isAlert.equals("0")) {
					isAlert = "1";
					messageRemind.setBackgroundResource(R.drawable.open);
				} else {
					isAlert = "0";
					messageRemind.setBackgroundResource(R.drawable.closed);
				}
				mConnHelper.updateConfig(mUIHandler, MsgTagVO.PUB_INFO,
						null, isAlert, false, null, null);
			}
		});
	}
	@SuppressLint("HandlerLeak")
	private Handler mUIHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MsgTagVO.DATA_LOAD:
				if (null != msg.obj && !msg.obj.equals("")) {
					JsonHandler nljh = new JsonHandler((String) msg.obj,
							getApplicationContext());
					UserVO user = nljh.parseUser();
					if (user != null && user.getIsalert().equals("0")) {
						isAlert = "0";
						messageRemind.setBackgroundResource(R.drawable.closed);
					} else {
						isAlert = "1";
						messageRemind.setBackgroundResource(R.drawable.open);
					}
				}
				break;
			case MsgTagVO.PUB_INFO:
				if (JsonHandler.checkResult((String) msg.obj,
						getApplicationContext())) {

				}
				break;
			default:
				break;
			}
		}
	};

	private void loadData() {
		String params = ZhuoCommHelper.getUrlUserConfig();
		mConnHelper.getFromServer(params, mUIHandler, MsgTagVO.DATA_LOAD);
	}

}
