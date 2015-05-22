package com.cpstudio.zhuojiaren;

import java.util.ArrayList;
import java.util.List;

import com.cpstudio.zhuojiaren.helper.JsonHandler;
import com.cpstudio.zhuojiaren.helper.ZhuoCommHelper;
import com.cpstudio.zhuojiaren.helper.ZhuoConnHelper;
import com.cpstudio.zhuojiaren.imageloader.LoadImage;
import com.cpstudio.zhuojiaren.model.GoodsVO;
import com.cpstudio.zhuojiaren.model.MsgTagVO;
import com.cpstudio.zhuojiaren.model.PicVO;
import com.cpstudio.zhuojiaren.util.DeviceInfoUtil;
import com.cpstudio.zhuojiaren.widget.PopupWindows;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.GestureDetector.OnGestureListener;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;
import android.widget.LinearLayout.LayoutParams;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;

public class GoodsDetailActivity extends Activity implements OnGestureListener {
	private PopupWindows mPopHelper;
	private ZhuoConnHelper mConnHelper = null;
	private LoadImage mLoadImage = new LoadImage();
	private String id = null;
	private GestureDetector mDetector;
	private ViewFlipper mViewFlipper;
	private int topY = 0;
	private float csd = 2;
	private float rmb = 0;
	private float money = 0;
	private float startX = 0;
	private float startY = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_goods_detail);
		csd = DeviceInfoUtil.getDeviceCsd(getApplicationContext());
		mPopHelper = new PopupWindows(GoodsDetailActivity.this);
		mConnHelper = ZhuoConnHelper.getInstance(getApplicationContext());
		Intent intent = getIntent();
		id = intent.getStringExtra("gid");
		mDetector = new GestureDetector(this, this);
		mViewFlipper = (ViewFlipper) findViewById(R.id.viewFlipperContainer);
		initClick();
		loadData();
	}

	@Override
	protected void onResume() {
		loadInfo();
		super.onResume();
	}

	private void initClick() {
		findViewById(R.id.buttonExchange).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						float rest = rmb - money;
						if (rest >= 0) {
							String title = getString(R.string.info75);
							title = title.replace("*****", String.valueOf(rmb));
							title = title.replace("****", String.valueOf(rest));
							mPopHelper.showPopDlg(
									findViewById(R.id.rootLayout),
									getString(R.string.label_ownuse),
									getString(R.string.label_friuse),
									new OnClickListener() {

										@Override
										public void onClick(View v) {
											Intent intent = new Intent(
													GoodsDetailActivity.this,
													ReceiverInfoActivity.class);
											intent.putExtra("ismy", true);
											intent.putExtra("gid", id);
											startActivity(intent);
										}
									}, new OnClickListener() {

										@Override
										public void onClick(View v) {
											Intent intent = new Intent(
													GoodsDetailActivity.this,
													ReceiverInfoActivity.class);
											intent.putExtra("ismy", false);
											intent.putExtra("gid", id);
											startActivity(intent);
										}
									}, title);
						} else {
							mPopHelper.showPopDlgOne(
									findViewById(R.id.rootLayout), null,
									R.string.error23);
						}
					}
				});

		findViewById(R.id.buttonBack).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				GoodsDetailActivity.this.finish();
			}
		});

		findViewById(R.id.buttonRule).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i = new Intent(GoodsDetailActivity.this,
						RuleActivity.class);
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
				if (msg.obj != null && !msg.obj.equals("")) {
					JsonHandler nljh = new JsonHandler((String) msg.obj,
							getApplicationContext());
					GoodsVO item = nljh.parseGoods();
					((TextView) findViewById(R.id.textViewTitle)).setText(item
							.getName());
					try {
						String moneyStr = item.getMoney();
						((TextView) findViewById(R.id.textViewMoney))
								.setText(moneyStr);
						money = Float.valueOf(moneyStr);
					} catch (Exception e) {
						e.printStackTrace();
					}
					((TextView) findViewById(R.id.textViewRMB)).setText(item
							.getPrice());
					((TextView) findViewById(R.id.textViewDetail)).setText(item
							.getDetail());
					ViewFlipper.LayoutParams vflp = new ViewFlipper.LayoutParams(
							LayoutParams.MATCH_PARENT, (int) (csd * 165));
					RelativeLayout.LayoutParams rllp = new RelativeLayout.LayoutParams(
							LayoutParams.MATCH_PARENT,
							LayoutParams.MATCH_PARENT);
					final List<PicVO> pics = item.getPic();
					if (pics != null) {
						for (PicVO pic : pics) {
							RelativeLayout rl = new RelativeLayout(
									GoodsDetailActivity.this);
							rl.setLayoutParams(vflp);
							rl.setTag(pic.getOrgurl());
							mViewFlipper.addView(rl);
							ImageView iv = new ImageView(
									GoodsDetailActivity.this);
							iv.setLayoutParams(rllp);
							rl.addView(iv);
							String url = pic.getUrl();
							iv.setImageResource(R.drawable.default_image);
							iv.setTag(url);
							mLoadImage.addTask(url, iv);
							rl.setOnClickListener(new OnClickListener() {

								@Override
								public void onClick(View v) {
									Intent intent = new Intent(
											GoodsDetailActivity.this,
											PhotoViewMultiActivity.class);
									ArrayList<String> orgs = new ArrayList<String>();
									for (int j = 0; j < pics.size(); j++) {
										orgs.add(pics.get(j).getOrgurl());
									}
									intent.putStringArrayListExtra("pics", orgs);
									intent.putExtra("pic", (String) v.getTag());
									startActivity(intent);
								}
							});
						}
						mLoadImage.doTask();
					}
				}
				break;
			}
			case MsgTagVO.DATA_OTHER: {
				if (JsonHandler.checkResult((String) msg.obj,
						getApplicationContext())) {
					try {
						String rmbStr = JsonHandler
								.getSingleResult((String) msg.obj);
						((TextView) findViewById(R.id.textViewRest))
								.setText(rmbStr);
						rmb = Float.valueOf(rmbStr);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				break;
			}
			}
		}
	};

	public void loadData() {
		String params = ZhuoCommHelper.getUrlGetGoodsDetail();
		params += "?gid=" + id;
		mConnHelper.getFromServer(params, mUIHandler, MsgTagVO.DATA_LOAD,
				GoodsDetailActivity.this, true, new OnCancelListener() {

					@Override
					public void onCancel(DialogInterface dialog) {
						GoodsDetailActivity.this.finish();
					}
				});
		loadInfo();
	}

	public void loadInfo() {
		mConnHelper.getFromServer(ZhuoCommHelper.getUrlGetZhuoRMB(),
				mUIHandler, MsgTagVO.DATA_OTHER);
	}

	@Override
	public boolean onDown(MotionEvent arg0) {
		return false;
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float arg2,
			float arg3) {
		if (e1.getY() - topY <= 190 * csd
				&& Math.abs((e2.getX() - e1.getX()) / (e2.getY() - e1.getY())) > 2) {
			if (mViewFlipper.getChildCount() > 1
					&& e1.getX() - e2.getX() > 60 * csd
					&& mViewFlipper.getDisplayedChild() < mViewFlipper
							.getChildCount() - 1) {
				mViewFlipper.setInAnimation(getApplicationContext(),
						R.anim.push_left_in);
				mViewFlipper.setOutAnimation(getApplicationContext(),
						R.anim.push_left_out);
				mViewFlipper.showNext();
			} else if (mViewFlipper.getChildCount() > 1
					&& e1.getX() - e2.getX() < -60 * csd
					&& mViewFlipper.getDisplayedChild() > 0) {
				mViewFlipper.setInAnimation(getApplicationContext(),
						R.anim.push_right_in);
				mViewFlipper.setOutAnimation(getApplicationContext(),
						R.anim.push_right_out);
				mViewFlipper.showPrevious();
			}
			return true;
		}
		return false;
	}

	@Override
	public void onLongPress(MotionEvent arg0) {

	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		return false;
	}

	@Override
	public void onShowPress(MotionEvent arg0) {

	}

	@Override
	public boolean onSingleTapUp(MotionEvent arg0) {
		return false;
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		if (topY == 0) {
			int[] location = new int[2];
			mViewFlipper.getLocationInWindow(location);
			topY = location[1];
		}
		mDetector.onTouchEvent(ev);
		if (ev.getAction() == MotionEvent.ACTION_DOWN) {
			startX = ev.getX();
			startY = ev.getY();
		}
		if (Math.abs((ev.getX() - startX) / (ev.getY() - startY)) > 1
				&& startY < (topY + 190 * csd)) {
			return true;
		}
		return super.dispatchTouchEvent(ev);
	}
}
