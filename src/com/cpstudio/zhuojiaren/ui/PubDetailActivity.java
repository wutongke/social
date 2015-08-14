package com.cpstudio.zhuojiaren.ui;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;

import com.cpstudio.zhuojiaren.BaseActivity;
import com.cpstudio.zhuojiaren.R;
import com.cpstudio.zhuojiaren.helper.JsonHandler;
import com.cpstudio.zhuojiaren.helper.ZhuoConnHelper;
import com.cpstudio.zhuojiaren.imageloader.LoadImage;
import com.cpstudio.zhuojiaren.model.MessagePubVO;
import com.cpstudio.zhuojiaren.model.MsgTagVO;
import com.cpstudio.zhuojiaren.util.CommonUtil;

/**
 * ¹«¸æÏêÇé
 * 
 * @author lz
 * 
 */
public class PubDetailActivity extends BaseActivity {
	private ZhuoConnHelper mConnHelper = null;
	@InjectView(R.id.agvd_image)
	ImageView image;
	@InjectView(R.id.agvd_name_and_order)
	TextView name;
	@InjectView(R.id.agvd_content)
	TextView content;
	@InjectView(R.id.agvd_time)
	TextView time;
	LoadImage imageLoader;
	private MessagePubVO pubMsg;
	String id;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pub_detail);
		ButterKnife.inject(this);
		initTitle();
		id = getIntent().getStringExtra("id");
		title.setText(R.string.lab_pub_detail);
		imageLoader = new LoadImage();
		mConnHelper = ZhuoConnHelper.getInstance(getApplicationContext());
		loadData();
	}

	private void loadData() {
		// TODO Auto-generated method stub
		if (CommonUtil.getNetworkState(getApplicationContext()) == 2) {
		} else {
			mConnHelper.getGonggaoDetail(mUIHandler, MsgTagVO.DATA_LOAD, id);
		}
	}

	private void fillData() {
		// TODO Auto-generated method stub
		if (pubMsg != null) {
			// imageLoader.beginLoad(visit.getImageAddr(), image);
			content.setText(pubMsg.getContent());
			name.setText(pubMsg.getPublish());
			time.setText(pubMsg.getPubtime());
		}
	}

	@SuppressLint("HandlerLeak")
	private Handler mUIHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MsgTagVO.DATA_LOAD: {
				if (JsonHandler.checkResult((String) msg.obj,
						getApplicationContext())) {
					JsonHandler nljh = new JsonHandler((String) msg.obj,
							getApplicationContext());
					pubMsg = nljh.parseMessagePub();
					fillData();
				}
				break;
			}

			}
		}
	};
}
