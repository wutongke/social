package com.cpstudio.zhuojiaren;

import com.cpstudio.zhuojiaren.facade.RecordChatFacade;
import com.cpstudio.zhuojiaren.facade.UserFacade;
import com.cpstudio.zhuojiaren.helper.JsonHandler;
import com.cpstudio.zhuojiaren.helper.ResHelper;
import com.cpstudio.zhuojiaren.helper.ZhuoCommHelper;
import com.cpstudio.zhuojiaren.helper.ZhuoConnHelper;
import com.cpstudio.zhuojiaren.imageloader.LoadImage;
import com.cpstudio.zhuojiaren.model.MsgTagVO;
import com.cpstudio.zhuojiaren.model.UserVO;
import com.cpstudio.zhuojiaren.util.CommonUtil;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MyHomeActivity extends Activity {

	private ZhuoConnHelper mConnHelper = null;
	private String mUid = null;
	private UserFacade userFacade = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_home);
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
		findViewById(R.id.buttonEditCard).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						Intent i = new Intent(MyHomeActivity.this,
								CardEditActivity.class);
						startActivity(i);
					}
				});

		findViewById(R.id.relativeLayoutPlan).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						Intent i = new Intent(MyHomeActivity.this,
								GoodsExchangeActivity.class);
						startActivity(i);
					}
				});

		findViewById(R.id.relativeLayoutCollect).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						Intent i = new Intent(MyHomeActivity.this,
								MyCollectListActivity.class);
						startActivity(i);
					}
				});
		findViewById(R.id.relativeLayoutRecord).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						Intent i = new Intent(MyHomeActivity.this,
								RecordListActivity.class);
						startActivity(i);
					}
				});
		findViewById(R.id.relativeLayoutPosition).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						Intent i = new Intent(MyHomeActivity.this,
								MyLocationActivity.class);
						startActivity(i);
					}
				});
		findViewById(R.id.relativeLayoutAdvice).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						Intent i = new Intent(MyHomeActivity.this,
								MyAdviceActivity.class);
						startActivity(i);
					}
				});

		findViewById(R.id.relativeLayoutAboutus).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						Intent i = new Intent(MyHomeActivity.this,
								MyAboutUsListActivity.class);
						startActivity(i);
					}
				});
		findViewById(R.id.relativeLayoutServant).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						Intent i = new Intent(MyHomeActivity.this,
								ChatActivity.class);
						i.putExtra("userid", "00000000000");
						startActivity(i);
					}
				});

		findViewById(R.id.relativeLayoutVisit).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						Intent i = new Intent(MyHomeActivity.this,
								MyLastestVisitActivity.class);
						startActivity(i);
					}
				});
		findViewById(R.id.relativeLayoutSyssetting).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						Intent i = new Intent(MyHomeActivity.this,
								MySettingActivity.class);
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
					((TextView) findViewById(R.id.textViewName)).setText(name);
					String url = user.getUheader();
					String levelStr = user.getLevel();
					if (levelStr != null && !levelStr.equals("")) {
						int level = Integer.valueOf(levelStr);
						String[] levels = getResources().getStringArray(
								R.array.array_level_type);
						LinearLayout parent = (LinearLayout) findViewById(R.id.linearLayoutLevel);
						if (level >= levels.length - 1) {
							level = levels.length - 1;
							findViewById(R.id.linearLayoutBg)
									.setBackgroundResource(
											R.drawable.bg_border2);
							parent.removeAllViews();
						} else {
							for (int i = 0; i < level; i++) {
								((ImageView) parent.getChildAt(i))
										.setImageResource(R.drawable.ico_level_star_on);
							}
						}
						((TextView) findViewById(R.id.textViewLevel))
								.setText(levels[level]);
					}
					ImageView iv = (ImageView) findViewById(R.id.imageViewHead);
					iv.setTag(url);
					final String userid = user.getUserid();
					iv.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View paramView) {
							Intent intent = new Intent(MyHomeActivity.this,
									UserCardActivity.class);
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
		RecordChatFacade mFacade = new RecordChatFacade(getApplicationContext());
		int all = mFacade.getUnread().size();
		TextView allNum = (TextView) findViewById(R.id.textViewRecordNum);
		allNum.setText(all + "");
		if (all == 0) {
			allNum.setVisibility(View.GONE);
		} else {
			allNum.setVisibility(View.VISIBLE);
		}
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
			mConnHelper.getFromServer(ZhuoCommHelper.getUrlGetZhuoRMB(), mUIHandler, MsgTagVO.DATA_OTHER);
		}
	}

}
