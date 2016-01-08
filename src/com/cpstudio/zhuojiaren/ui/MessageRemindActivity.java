package com.cpstudio.zhuojiaren.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;

import com.cpstudio.zhuojiaren.R;
import com.cpstudio.zhuojiaren.helper.ConnHelper;
import com.cpstudio.zhuojiaren.helper.JsonHandler;
import com.cpstudio.zhuojiaren.helper.UrlHelper;
import com.cpstudio.zhuojiaren.model.MsgTagVO;
import com.cpstudio.zhuojiaren.model.UserVO;
/**
 * 消息提示设置
 * @author lef
 *
 */
public class MessageRemindActivity extends BaseActivity {
	@InjectView(R.id.amr_message_remind)
	ImageView messageRemind;
	@InjectView(R.id.amr_sound)
	ImageView sound;
	@InjectView(R.id.amr_vibrate)
	ImageView vibrate;
	private ConnHelper mConnHelper = null;
	private String isAlert = "1";
	int soundStatus = 1;
	int vibrateStatus = 1;
	SharedPreferences sp ;
	SharedPreferences.Editor editor;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_messaeg_remind);
		ButterKnife.inject(this);
		mConnHelper = ConnHelper.getInstance(getApplicationContext());
		sp=getSharedPreferences("remind", Context.MODE_PRIVATE);
		editor = sp.edit();
		soundStatus = sp.getInt("sound", 1);
		vibrateStatus = sp.getInt("vibrate", 1);
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
					messageRemind.setBackgroundResource(R.drawable.opencheck);
				} else {
					isAlert = "0";
					messageRemind.setBackgroundResource(R.drawable.closecheck);
				}
//				mConnHelper.updateConfig(mUIHandler, MsgTagVO.PUB_INFO,
//						null, isAlert, false, null, null);
			}
		});
		sound.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(soundStatus==1){
					soundStatus=0;
					editor.putInt("sound", 0);
					editor.commit();
					sound.setBackgroundResource(R.drawable.closecheck);
				}else{
					editor.putInt("sound", 1);
					editor.commit();
					soundStatus=1;
					sound.setBackgroundResource(R.drawable.opencheck);
				}
			}
		});
		vibrate.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(vibrateStatus==1){
					vibrateStatus=0;
					editor.putInt("vibrate", 0);
					editor.commit();
					vibrate.setBackgroundResource(R.drawable.closecheck);
				}else{
					editor.putInt("vibrate", 1);
					editor.commit();
					vibrateStatus=1;
					vibrate.setBackgroundResource(R.drawable.opencheck);
				}
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
		String params = UrlHelper.getUrlUserConfig();
		mConnHelper.getFromServer(params, mUIHandler, MsgTagVO.DATA_LOAD);
	}

}
