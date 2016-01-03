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
import butterknife.OnClick;
import butterknife.Optional;

import com.cpstudio.zhuojiaren.BaseActivity;
import com.cpstudio.zhuojiaren.R;
import com.cpstudio.zhuojiaren.ViewOrderActivity;
import com.cpstudio.zhuojiaren.helper.AppClientLef;
import com.cpstudio.zhuojiaren.helper.JsonHandler;
import com.cpstudio.zhuojiaren.helper.ResHelper;
import com.cpstudio.zhuojiaren.helper.ZhuoConnHelper;
import com.cpstudio.zhuojiaren.imageloader.LoadImage;
import com.cpstudio.zhuojiaren.model.MsgTagVO;
import com.cpstudio.zhuojiaren.model.ResultVO;
import com.cpstudio.zhuojiaren.model.UserNewVO;
import com.cpstudio.zhuojiaren.model.UserVO;
import com.cpstudio.zhuojiaren.ui.CartActivity;
import com.cpstudio.zhuojiaren.ui.GoodsCollectionActivity;
import com.cpstudio.zhuojiaren.ui.LocateActivity;
import com.cpstudio.zhuojiaren.util.CommonUtil;
/**
 * 倬脉商场中的“我的”界面
 * @author lz
 *
 */
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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_store_lzmy_home);
		initTitle();
		title.setText(R.string.title_activity_lzmy_home);
		ButterKnife.inject(this);
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

	// @OnClick(R.id.llMyCollect)
	// public void gotoCollection(View v) {
	// startActivity(new Intent(StoreMyHomeActivity.this,
	// GoodsCollectionActivity.class));
	// }

	@Optional
	@OnClick({ R.id.llMyCollect, R.id.llAddress })
	public void gotoCollection(View v) {
		switch (v.getId()) {
		case R.id.llMyCollect:
			startActivity(new Intent(StoreMyHomeActivity.this,
					GoodsCollectionActivity.class));
			break;
		case R.id.llAddress:
			startActivity(new Intent(StoreMyHomeActivity.this,
					LocateActivity.class));
			break;
		}
	}

	private void initClick() {

		vGWC.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				startActivity(new Intent(StoreMyHomeActivity.this,
						CartActivity.class));
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

				startActivity(new Intent(StoreMyHomeActivity.this,
						ViewOrderActivity.class));
			}
		});
		ivHome.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				StoreMyHomeActivity.this.finish();
			}
		});
	}

	@SuppressLint("HandlerLeak")
	private Handler mUIHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MsgTagVO.DATA_LOAD: {
				UserNewVO user = null;
				if (msg.obj instanceof UserVO) {
					user = (UserNewVO) msg.obj;
				} else {
					JsonHandler nljh = new JsonHandler((String) msg.obj,
							getApplicationContext());
					user = nljh.parseNewUser();
				}
				if (null != user) {
					String name = user.getName();
					((TextView) findViewById(R.id.textViewName)).setText(name);
					String url = user.getUheader();
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
					LoadImage mLoadImage = LoadImage.getInstance();
					mLoadImage.addTask(url, iv);
					mLoadImage.doTask();
				}
				break;
			}
			case MsgTagVO.DATA_OTHER: {
				ResultVO res = null;
				res = JsonHandler.parseResult((String) msg.obj);
				if (JsonHandler.checkResult((String) msg.obj,
						StoreMyHomeActivity.this)) {
				} else {
					CommonUtil.displayToast(StoreMyHomeActivity.this,
							res.getMsg());
					return;
				}
				((TextView) findViewById(R.id.textViewMoney)).setText(res
						.getData());
			}
			}
		}
	};

	private void loadInfo() {
		if (CommonUtil.getNetworkState(getApplicationContext()) == 2) {
		} else {
			mConnHelper.getUserInfo(mUIHandler, MsgTagVO.DATA_LOAD, mUid);
		}
	}

	private void loadDb() {
		// TODO Auto-generated method stub
		AppClientLef.getInstance(this.getApplicationContext()).getMyZhuoBi(
				StoreMyHomeActivity.this, mUIHandler, MsgTagVO.DATA_OTHER);
	}

}
