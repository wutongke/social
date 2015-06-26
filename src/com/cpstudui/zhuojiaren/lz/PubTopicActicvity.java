package com.cpstudui.zhuojiaren.lz;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import butterknife.ButterKnife;
import butterknife.InjectView;

import com.cpstudio.zhuojiaren.BaseActivity;
import com.cpstudio.zhuojiaren.PublishActiveActivity;
import com.cpstudio.zhuojiaren.R;
import com.cpstudio.zhuojiaren.adapter.ImageGridAdapter;
import com.cpstudio.zhuojiaren.facade.QuanFacade;
import com.cpstudio.zhuojiaren.helper.ResHelper;
import com.cpstudio.zhuojiaren.helper.ZhuoConnHelper;
import com.cpstudio.zhuojiaren.model.MsgTagVO;
import com.cpstudio.zhuojiaren.util.CommonAdapter;
import com.cpstudio.zhuojiaren.widget.ImageChooseAdapter;
import com.cpstudio.zhuojiaren.widget.PicChooseActivity;
import com.cpstudio.zhuojiaren.widget.PopupWindows;

public class PubTopicActicvity extends BaseActivity {

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
	private ZhuoConnHelper mConnHelper = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pub_topic);
		initTitle();
		pwh = new PopupWindows(PubTopicActicvity.this);
		ButterKnife.inject(this);
		title.setText(R.string.title_pub_topic);
		mConnHelper = ZhuoConnHelper.getInstance(getApplicationContext());
		uid = ResHelper.getInstance(getApplicationContext()).getUserid();
		function.setText(R.string.label_publish);
		initClick();
	}

	@SuppressLint("HandlerLeak")
	private Handler mUIHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MsgTagVO.FOLLOW_QUAN: {
				// if (JsonHandler.checkResult((String) msg.obj,
				// getApplicationContext())) {
				// if (isfollow) {
				// isfollow = false;
				// pwh.showPopTip(findViewById(R.id.scrollViewGroupInfo),
				// null, R.string.label_exitSuccess);
				// loadData();
				// } else {
				// pwh.showPopTip(findViewById(R.id.scrollViewGroupInfo),
				// null, R.string.label_applysuccess);
				// }
				// }
				break;
			}

			}
		}
	};

	private void initClick() {
		imageAdatper = new ImageGridAdapter(PubTopicActicvity.this, imageDir,
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
						Intent intent = new Intent(PubTopicActicvity.this,
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
