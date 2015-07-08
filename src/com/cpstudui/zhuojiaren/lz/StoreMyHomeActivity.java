package com.cpstudui.zhuojiaren.lz;

import android.annotation.SuppressLint;
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

import com.cpstudio.zhuojiaren.BaseActivity;
import com.cpstudio.zhuojiaren.GoodsExchangeActivity;
import com.cpstudio.zhuojiaren.MyAboutUsListActivity;
import com.cpstudio.zhuojiaren.MyAdviceActivity;
import com.cpstudio.zhuojiaren.R;
import com.cpstudio.zhuojiaren.UserHomeActivity;
import com.cpstudio.zhuojiaren.ViewOrderActivity;
import com.cpstudio.zhuojiaren.facade.UserFacade;
import com.cpstudio.zhuojiaren.helper.JsonHandler;
import com.cpstudio.zhuojiaren.helper.ResHelper;
import com.cpstudio.zhuojiaren.helper.ZhuoCommHelper;
import com.cpstudio.zhuojiaren.helper.ZhuoConnHelper;
import com.cpstudio.zhuojiaren.imageloader.LoadImage;
import com.cpstudio.zhuojiaren.model.MsgTagVO;
import com.cpstudio.zhuojiaren.model.UserVO;
import com.cpstudio.zhuojiaren.ui.CartActivity;
import com.cpstudio.zhuojiaren.ui.GoodsCollectionActivity;
import com.cpstudio.zhuojiaren.ui.LocateActivity;
import com.cpstudio.zhuojiaren.ui.MyCollectionActivity;
import com.cpstudio.zhuojiaren.ui.MyMoneyActivity;
import com.cpstudio.zhuojiaren.ui.SettingActivity;
import com.cpstudio.zhuojiaren.util.CommonUtil;

public class StoreMyHomeActivity extends BaseActivity {
	@InjectView(R.id.llViewOrder)
	View vOrder;// 查看订单
	@InjectView(R.id.llGWC)
	View vGWC;// 购物车
	@InjectView(R.id.llMyStore)
	View vMyStore;// 我的店铺
	@InjectView(R.id.llMyCollect)
	View vCollect;

	@InjectView(R.id.llAddress)
	View vAddress;// 收货地址

	@InjectView(R.id.imageViewHome)
	ImageView ivHome;

	private ZhuoConnHelper mConnHelper = null;
	private String mUid = null;
	private UserFacade userFacade = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_store_lzmy_home);
		initTitle();
		title.setText(R.string.title_activity_lzmy_home);
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

		vCollect.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// 收藏的

				 startActivity(new
				 Intent(StoreMyHomeActivity.this,GoodsCollectionActivity.class));
			}
		});

		vAddress.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				 startActivity(new
				 Intent(StoreMyHomeActivity.this,LocateActivity.class));
			}
		});

		vGWC.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				 startActivity(new
				 Intent(StoreMyHomeActivity.this,CartActivity.class));
			}
		});

		vMyStore.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				// startActivity(new
				// Intent(StoreMyHomeActivity.this,MyMoneyActivity.class));
			}
		});
		vOrder.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				 startActivity(new
				 Intent(StoreMyHomeActivity.this,ViewOrderActivity.class));
			}
		});
		ivHome.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				// startActivity(new
				// Intent(StoreMyHomeActivity.this,MyMoneyActivity.class));
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
					String url = user.getUheader();
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
							Intent intent = new Intent(
									StoreMyHomeActivity.this,
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
