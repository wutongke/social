package com.cpstudui.zhuojiaren.lz;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;

import com.cpstudio.zhuojiaren.AboutZhuomaiActivity;
import com.cpstudio.zhuojiaren.MyAdviceActivity;
import com.cpstudio.zhuojiaren.R;
import com.cpstudio.zhuojiaren.UserHomeActivity;
import com.cpstudio.zhuojiaren.facade.UserFacade;
import com.cpstudio.zhuojiaren.helper.ConnHelper;
import com.cpstudio.zhuojiaren.helper.JsonHandler;
import com.cpstudio.zhuojiaren.helper.ResHelper;
import com.cpstudio.zhuojiaren.imageloader.LoadImage;
import com.cpstudio.zhuojiaren.model.BaseCodeData;
import com.cpstudio.zhuojiaren.model.MsgTagVO;
import com.cpstudio.zhuojiaren.model.UserNewVO;
import com.cpstudio.zhuojiaren.ui.MyCollectionActivity;
import com.cpstudio.zhuojiaren.ui.MyZhuoBiActivity;
import com.cpstudio.zhuojiaren.ui.SettingActivity;
import com.cpstudio.zhuojiaren.util.CommonUtil;
/**
 * "我的"界面
 * @author lz
 *
 */
public class LZMyHomeActivity extends Activity {
	@InjectView(R.id.rlbg)
	View rlbg;
	@InjectView(R.id.llMyCZ)
	View vCZ;
	@InjectView(R.id.llMyPurse)
	View vPurse;
	@InjectView(R.id.llZMSC)
	View vZMSC;
	@InjectView(R.id.llMyCollect)
	View vCollect;

	@InjectView(R.id.llMySet)
	View vSet;
	@InjectView(R.id.llMyCallback)
	View vCallback;
	@InjectView(R.id.llMyAbout)
	View vAbout;
	@InjectView(R.id.imageViewHead)
	ImageView imageViewHead;
	private ConnHelper mConnHelper = null;
	private String mUid = null;
	private UserFacade userFacade = null;
	UserNewVO userInfo;
	private LoadImage mLoadImage = new LoadImage();
	BaseCodeData baseDataSet;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_lzmy_home);
		ButterKnife.inject(this);
		userFacade = new UserFacade(getApplicationContext());
		mConnHelper = ConnHelper.getInstance(getApplicationContext());
		baseDataSet = mConnHelper.getBaseDataSet();
		mUid = ResHelper.getInstance(getApplicationContext()).getUserid();
		initClick();
	}

	@Override
	protected void onStart() {
		super.onStart();
	}

	@Override
	protected void onResume() {
		loadInfo();
		super.onResume();
	}

	private void initClick() {
		imageViewHead.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent i = new Intent(LZMyHomeActivity.this,
						ZhuoMaiCardActivity.class);
				i.putExtra("userid", mUid);
				startActivity(i);
			}
		});
		vPurse.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// 我的钱包
				startActivity(new Intent(LZMyHomeActivity.this,
						MyZhuoBiActivity.class));
			}
		});

		vCZ.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				Intent i = new Intent(LZMyHomeActivity.this,
						UserHomeActivity.class);
				i.putExtra("userid", mUid);
				i.putExtra("from", "home");
				startActivity(i);
			}
		});

		vZMSC.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent(LZMyHomeActivity.this,
						StoreMainActivity.class);
				startActivity(i);
			}
		});

		vCollect.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent(LZMyHomeActivity.this,
						MyCollectionActivity.class);
				startActivity(i);
			}
		});
		vCallback.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent(LZMyHomeActivity.this,
						MyAdviceActivity.class);
				startActivity(i);
			}
		});

		vAbout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent(LZMyHomeActivity.this,
						AboutZhuomaiActivity.class);
				startActivity(i);
			}
		});
		vSet.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent(LZMyHomeActivity.this,
						SettingActivity.class);
				startActivity(i);
			}
		});

	}

	/**
	 * 填充头部个人信息
	 */
	void fillHeadInfo() {
		if (userInfo == null)
			return;
		imageViewHead.setTag(userInfo.getUheader());
		mLoadImage.beginLoad(userInfo.getUheader(), imageViewHead);
		((TextView) findViewById(R.id.textViewName))
				.setText(userInfo.getName());
		String work = "";
		if (baseDataSet != null) {
			int pos = userInfo.getPosition();
			if (pos > 0)// 默认为0，城市标号从1开始
				pos--;
			else return;
			work = ((baseDataSet.getPosition()).get(pos)).getContent();
		}
		((TextView) findViewById(R.id.textViewContent)).setText(work);

		((TextView) findViewById(R.id.textViewCompany)).setText(userInfo
				.getCompany());
		int count = 1;
		try {
			count = Integer.parseInt(userInfo.getZhuobi());
		} catch (Exception e) {

		}
		if (count != -1)
			((TextView) findViewById(R.id.textViewMoney)).setText(String
					.format(getResources().getString(R.string.zhuobi_num), count));
		if (userInfo.getGender() == 0)// 男
			rlbg.setBackgroundResource(R.drawable.mbg6_wdzy_1);
		else
			rlbg.setBackgroundResource(R.drawable.mbg5_wdzy_1);
	}

	@SuppressLint("HandlerLeak")
	private Handler mUIHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MsgTagVO.DATA_LOAD: {
				if (msg.obj instanceof UserNewVO)// 加载的本地数据
				{
					userInfo = (UserNewVO) msg.obj;
					userInfo.setUserid(mUid);
				} else if (JsonHandler.checkResult((String) msg.obj,
						getApplicationContext())) {
					JsonHandler nljh = new JsonHandler((String) msg.obj,
							getApplicationContext());
					userInfo = nljh.parseNewUser();
					userInfo.setUserid(mUid);
					userFacade.saveOrUpdate(userInfo);
				}
				fillHeadInfo();
				break;
			}
			}
		}
	};

	private void loadInfo() {
		if (CommonUtil.getNetworkState(getApplicationContext()) == 2) {
			UserNewVO user = userFacade.getSimpleInfoById(mUid);
			if (user == null) {
				CommonUtil.displayToast(getApplicationContext(),
						R.string.error0);
			} else {
				Message msg = mUIHandler.obtainMessage(MsgTagVO.DATA_LOAD);
				msg.obj = user;
				msg.sendToTarget();
			}
		} else {
			mConnHelper.getUserInfo(mUIHandler, MsgTagVO.DATA_LOAD, mUid);
		}
	}
}
