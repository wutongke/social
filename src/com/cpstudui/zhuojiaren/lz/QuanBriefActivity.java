package com.cpstudui.zhuojiaren.lz;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;

import com.cpstudio.zhuojiaren.BaseActivity;
import com.cpstudio.zhuojiaren.R;
import com.cpstudio.zhuojiaren.facade.QuanFacade;
import com.cpstudio.zhuojiaren.helper.JsonHandler;
import com.cpstudio.zhuojiaren.helper.ZhuoConnHelper;
import com.cpstudio.zhuojiaren.imageloader.LoadImage;
import com.cpstudio.zhuojiaren.model.MsgTagVO;
import com.cpstudio.zhuojiaren.widget.PopupWindows;

public class QuanBriefActivity extends BaseActivity {
	@InjectView(R.id.imageViewGroupHeader)
	ImageView ivGroupHeader;
	@InjectView(R.id.textViewName)
	TextView tvName;
	@InjectView(R.id.textViewCy)
	TextView tvMemNum;
	@InjectView(R.id.textViewTopic)
	TextView tvTopicNum;
	
	@InjectView(R.id.btnJoinQuan)
	Button btnJoinQuan;// 非成员操作菜单
	@InjectView(R.id.btnQuitQuan)
	Button btnQuitQuan;// 非成员操作菜单
	
	@InjectView(R.id.lt_chengyuan_menue)
	View ltMember;// 成员操作菜单
	@InjectView(R.id.lt_youke_menue)
	View ltYouke;// 非成员操作菜单
	
	private PopupWindows phw = null;
	private LoadImage mLoadImage = new LoadImage();
	// 不同身份，功能不同
	private String memberType = "";
	private PopupWindows pwh = null;
	private String groupid = null;
	private ZhuoConnHelper mConnHelper = null;
	private boolean isfollow = false;// 是否已经加入该圈子
	private QuanFacade mFacade = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_quan_brief);
		
		initTitle();
		ButterKnife.inject(this);
		title.setText(R.string.label_quan_brief);
		
		mConnHelper = ZhuoConnHelper.getInstance(getApplicationContext());
		mFacade = new QuanFacade(getApplicationContext());
		Intent i = getIntent();
		groupid = i.getStringExtra("groupid");
		pwh = new PopupWindows(QuanBriefActivity.this);
		
		//在简介里与详情里公用基本信息但是不需要循环展示公告
		findViewById(R.id.linearLayoutBroadcast).setVisibility(View.GONE);
		
//		loadData();
		initOnClick();
	}
	
	@SuppressLint("HandlerLeak")
	private Handler mUIHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			
			case MsgTagVO.FOLLOW_QUAN: {
				if (JsonHandler.checkResult((String) msg.obj,
						getApplicationContext())) {
					if (isfollow) {
						isfollow = false;
						pwh.showPopTip(findViewById(R.id.scrollViewGroupInfo),
								null, R.string.label_exitSuccess);
//						loadData();
					} else {
						pwh.showPopTip(findViewById(R.id.scrollViewGroupInfo),
								null, R.string.label_applysuccess);
					}
				}
				break;
			}
			
			}
		}
	};
	
	private void initOnClick() {
		// TODO Auto-generated method stub
		btnJoinQuan.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				mConnHelper.followGroup(groupid, "1", mUIHandler,
						MsgTagVO.FOLLOW_QUAN, null, true, null, null);

			}
		});
	
		btnQuitQuan.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				pwh.showPopDlg(v,
						new OnClickListener() {

							@Override
							public void onClick(View v) {
								mConnHelper
										.followGroup(
												groupid,
												"0",
												mUIHandler,
												MsgTagVO.FOLLOW_QUAN,
												null, true,
												null, null);
							}
						}, null, R.string.info13);
///
			}
		});
	}
	
}
