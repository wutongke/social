package com.cpstudio.zhuojiaren;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;

import com.cpstudio.zhuojiaren.adapter.ImageGridAdapter;
import com.cpstudio.zhuojiaren.helper.JsonHandler;
import com.cpstudio.zhuojiaren.helper.ResHelper;
import com.cpstudio.zhuojiaren.helper.ZhuoConnHelper;
import com.cpstudio.zhuojiaren.model.MsgTagVO;
import com.cpstudio.zhuojiaren.util.CommonAdapter;
import com.cpstudio.zhuojiaren.widget.ImageChooseAdapter;
import com.cpstudio.zhuojiaren.widget.PicChooseActivity;
import com.cpstudio.zhuojiaren.widget.PopupWindows;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class MyAdviceActivity extends BaseActivity {

	@InjectView(R.id.aee_image_layout)
	GridView imageGrid;
	@InjectView(R.id.aee_add_image)
	TextView addImage;
	@InjectView(R.id.editTextContent)
	EditText etContent;
	
	private int requestCode = 1;
	private CommonAdapter<String> imageAdatper;
	private ArrayList<String> imageDir = new ArrayList<String>();
	private ArrayList<EditText> phoneList = new ArrayList<EditText>();
	PopupWindows pwh;
	String uid;
	String groupid;
	private ZhuoConnHelper mConnHelper = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pub_topic);
		ButterKnife.inject(this);
		initTitle();
		etContent.setHint("请填写您的反馈意见");
		findViewById(R.id.feedbacktext).setVisibility(View.VISIBLE);
		title.setText("反馈");
		pwh = new PopupWindows(MyAdviceActivity.this);
		title.setText(R.string.title_pub_topic);
		mConnHelper = ZhuoConnHelper.getInstance(getApplicationContext());
		uid = ResHelper.getInstance(getApplicationContext()).getUserid();
		groupid=getIntent().getStringExtra("groupid");
		function.setText(R.string.label_publish);
		initClick();
	}

	@SuppressLint("HandlerLeak")
	private Handler mUIHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MsgTagVO.PUB_INFO: {
				if (msg.obj != null && !msg.obj.equals("")) {
					if (JsonHandler.checkResult((String) msg.obj)) {
						OnClickListener listener = new OnClickListener() {

							@Override
							public void onClick(View v) {
								setResult(RESULT_OK);
								MyAdviceActivity.this.finish();
							}
						};
						try {
							pwh.showPopDlgOne(findViewById(R.id.rootLayout),
									listener, R.string.info62);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
				break;
			}

			}
		}
	};

	private void initClick() {
		imageAdatper = new ImageGridAdapter(MyAdviceActivity.this, imageDir,
				R.layout.item_grid_image2);
		imageGrid.setAdapter(imageAdatper);
		imageGrid.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				if (imageDir.size() > position) {
					imageDir.remove(position);
					imageAdatper.notifyDataSetChanged();
				}
			}
		});
		addImage.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				addImage.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						ImageChooseAdapter.mSelectedImage.clear();
						Intent intent = new Intent(MyAdviceActivity.this,
								PicChooseActivity.class);
						intent.putExtra("IMAGECOUNT", 9 - imageDir.size());
						startActivityForResult(intent, requestCode);
					}
				});
			}
		});
		function.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				publish();
			}
		});
	}
	private void publish() {
		
		String content = etContent.getText().toString();
		
		if (content.trim().equals("")) {
			pwh.showPopDlgOne(findViewById(R.id.rootLayout), null,
					R.string.info24);
			etContent.requestFocus();
			return;
		}
		mConnHelper.pubQuanTopic(MyAdviceActivity.this, mUIHandler, MsgTagVO.PUB_INFO, groupid, content, imageDir);
//		mConnHelper.pubZhuoInfo(mIsh.getTags(), mUIHandler, MsgTagVO.PUB_INFO,
//				PublishActiveActivity.this, content, tag, mLocation, imgCnt,
//				"daily", true, null, null);
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == this.requestCode && resultCode == RESULT_OK) {
			imageDir.addAll(data.getStringArrayListExtra("data"));
			imageAdatper.notifyDataSetChanged();
		}
	}

}
