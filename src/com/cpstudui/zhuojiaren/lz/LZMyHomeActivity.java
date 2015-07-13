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
import android.widget.Toast;
import butterknife.ButterKnife;
import butterknife.InjectView;

import com.cpstudio.zhuojiaren.AboutZhuomaoActivity;
import com.cpstudio.zhuojiaren.GoodsExchangeActivity;
import com.cpstudio.zhuojiaren.MyAboutUsListActivity;
import com.cpstudio.zhuojiaren.MyAdviceActivity;
import com.cpstudio.zhuojiaren.MyCollectListActivity;
import com.cpstudio.zhuojiaren.MySettingActivity;
import com.cpstudio.zhuojiaren.R;
import com.cpstudio.zhuojiaren.UserHomeActivity;
import com.cpstudio.zhuojiaren.facade.UserFacade;
import com.cpstudio.zhuojiaren.helper.JsonHandler;
import com.cpstudio.zhuojiaren.helper.ResHelper;
import com.cpstudio.zhuojiaren.helper.ZhuoCommHelper;
import com.cpstudio.zhuojiaren.helper.ZhuoConnHelper;
import com.cpstudio.zhuojiaren.imageloader.LoadImage;
import com.cpstudio.zhuojiaren.model.MsgTagVO;
import com.cpstudio.zhuojiaren.model.UserVO;
import com.cpstudio.zhuojiaren.ui.MyCollectionActivity;
import com.cpstudio.zhuojiaren.ui.MyMoneyActivity;
import com.cpstudio.zhuojiaren.ui.SettingActivity;
import com.cpstudio.zhuojiaren.util.CommonUtil;

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

	private ZhuoConnHelper mConnHelper = null;
	private String mUid = null;
	private UserFacade userFacade = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_lzmy_home);
		ButterKnife.inject(this);
		userFacade = new UserFacade(getApplicationContext());
		mConnHelper = ZhuoConnHelper.getInstance(getApplicationContext());
		mUid = ResHelper.getInstance(getApplicationContext()).getUserid();
		initClick();
	}

	@Override
	protected void onStart() {
		loadInfo();
		super.onStart();
	}

	@Override
	protected void onResume() {
		loadDb();
		super.onResume();
	}

	private void initClick() {

		vPurse.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// 我的钱包
				// Toast.makeText(LZMyHomeActivity.this, "待完善！", 1000).show();
				startActivity(new Intent(LZMyHomeActivity.this,
						MyMoneyActivity.class));
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
				// 需要重写倬脉商城的Activity
				// Intent i = new Intent(LZMyHomeActivity.this,
				// GoodsExchangeActivity.class);
				// startActivity(i);
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
		// findViewById(R.id.relativeLayoutRecord).setOnClickListener(
		// new OnClickListener() {
		// @Override
		// public void onClick(View v) {
		// Intent i = new Intent(LZMyHomeActivity.this,
		// RecordListActivity.class);
		// startActivity(i);
		// }
		// });

		// 调用百度地图定位
		// findViewById(R.id.relativeLayoutPosition).setOnClickListener(
		// new OnClickListener() {
		// @Override
		// public void onClick(View v) {
		// Intent i = new Intent(LZMyHomeActivity.this,
		// MyLocationActivity.class);
		// startActivity(i);
		// }
		// });
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
						AboutZhuomaoActivity.class);
				startActivity(i);
			}
		});
		// i.putExtra("userid", "00000000000");可用做联系小卓秘书
		// findViewById(R.id.relativeLayoutServant).setOnClickListener(
		// new OnClickListener() {
		// @Override
		// public void onClick(View v) {
		// Intent i = new Intent(LZMyHomeActivity.this,
		// ChatActivity.class);
		// i.putExtra("userid", "00000000000");
		// startActivity(i);
		// }
		// });

		// findViewById(R.id.relativeLayoutVisit).setOnClickListener(
		// new OnClickListener() {
		// @Override
		// public void onClick(View v) {
		// Intent i = new Intent(LZMyHomeActivity.this,
		// MyLastestVisitActivity.class);
		// startActivity(i);
		// }
		// });
		vSet.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent(LZMyHomeActivity.this,
						SettingActivity.class);
				startActivity(i);
			}
		});

	}

	@SuppressLint("HandlerLeak")
	private Handler mUIHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MsgTagVO.DATA_LOAD: {
				UserVO user = null;
				if (msg.obj instanceof UserVO) {
					user = (UserVO) msg.obj;
				} else {
					JsonHandler nljh = new JsonHandler((String) msg.obj,
							getApplicationContext());
					user = nljh.parseUser();
				}
				if (null != user) {
					String name = user.getUsername();
					String work = user.getPost();
					String company = user.getCompany();
					((TextView) findViewById(R.id.textViewName)).setText(name);
					((TextView) findViewById(R.id.textViewContent))
							.setText(work);
					((TextView) findViewById(R.id.textViewCompany))
							.setText(company);
					String url = user.getUheader();
					String sex = user.getSex();

					if (sex!=null && sex.equals("男"))
						rlbg.setBackgroundResource(R.drawable.mbg6_wdzy_1);
					else
						rlbg.setBackgroundResource(R.drawable.mbg5_wdzy_1);

					// String levelStr = user.getLevel();
					// if (levelStr != null && !levelStr.equals("")) {
					// int level = Integer.valueOf(levelStr);
					// String[] levels = getResources().getStringArray(
					// R.array.array_level_type);
					// LinearLayout parent = (LinearLayout)
					// findViewById(R.id.linearLayoutLevel);
					// if (level >= levels.length - 1) {
					// level = levels.length - 1;
					// findViewById(R.id.linearLayoutBg)
					// .setBackgroundResource(
					// R.drawable.bg_border2);
					// parent.removeAllViews();
					// } else {
					// for (int i = 0; i < level; i++) {
					// ((ImageView) parent.getChildAt(i))
					// .setImageResource(R.drawable.ico_level_star_on);
					// }
					// }
					// ((TextView) findViewById(R.id.textViewLevel))
					// .setText(levels[level]);
					// }
					ImageView iv = (ImageView) findViewById(R.id.imageViewHead);
					iv.setTag(url);
					final String userid = user.getUserid();
					iv.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View paramView) {
							Intent intent = new Intent(LZMyHomeActivity.this,
									ZhuoMaiCardActivity.class);
							intent.putExtra("userid", userid);
							startActivity(intent);
						}
					});
					LoadImage mLoadImage = new LoadImage(10);
					mLoadImage.addTask(url, iv);
					mLoadImage.doTask();
				}
				break;
			}
			case MsgTagVO.DATA_OTHER: {
				if (JsonHandler.checkResult((String) msg.obj,
						getApplicationContext())) {
					String rmb = JsonHandler.getSingleResult((String) msg.obj);
					((TextView) findViewById(R.id.textViewMoney)).setText(rmb);
				}
				break;
			}
			}
		}
	};

	private void loadDb() {
		// RecordChatFacade mFacade = new
		// RecordChatFacade(getApplicationContext());
		// int all = mFacade.getUnread().size();
		// TextView allNum = (TextView) findViewById(R.id.textViewRecordNum);
		// allNum.setText(all + "");
		// if (all == 0) {
		// allNum.setVisibility(View.GONE);
		// } else {
		// allNum.setVisibility(View.VISIBLE);
		// }
	}

	private void loadInfo() {
		if (CommonUtil.getNetworkState(getApplicationContext()) == 2) {
			UserVO user = userFacade.getSimpleInfoById(mUid);
			if (user == null) {
				CommonUtil.displayToast(getApplicationContext(),
						R.string.error0);
			} else {
				Message msg = mUIHandler.obtainMessage(MsgTagVO.DATA_LOAD);
				msg.obj = user;
				msg.sendToTarget();
			}
		} else {
			String params = ZhuoCommHelper.getUrlUserInfo() + "?uid=" + mUid;
			mConnHelper.getFromServer(params, mUIHandler, MsgTagVO.DATA_LOAD);
			mConnHelper.getFromServer(ZhuoCommHelper.getUrlGetZhuoRMB(),
					mUIHandler, MsgTagVO.DATA_OTHER);
		}
	}
}
