package com.cpstudio.zhuojiaren;

import java.util.ArrayList;
import java.util.List;

import com.cpstudio.zhuojiaren.helper.JsonHandler;
import com.cpstudio.zhuojiaren.helper.ResHelper;
import com.cpstudio.zhuojiaren.helper.ZhuoCommHelper;
import com.cpstudio.zhuojiaren.helper.ZhuoConnHelper;
import com.cpstudio.zhuojiaren.imageloader.LoadImage;
import com.cpstudio.zhuojiaren.imageloader.LoadImage.Callback;
import com.cpstudio.zhuojiaren.model.LoadImageResultVO;
import com.cpstudio.zhuojiaren.model.MsgTagVO;
import com.cpstudio.zhuojiaren.model.ZhuoInfoVO;
import com.cpstudio.zhuojiaren.model.PicVO;
import com.cpstudio.zhuojiaren.util.CommonUtil;
import com.cpstudio.zhuojiaren.widget.PopupWindows;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.GestureDetector.OnGestureListener;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class PhotoActiveActivity extends Activity implements OnGestureListener {

	private LoadImage mLoadImage = new LoadImage();
	private PopupWindows pwh = null;
	private TextView textViewTitle = null;
	private ImageView imageViewPhoto = null;
	private String isCollect = "0";
	private GestureDetector mDetector;
	private List<PicVO> pics;
	private int now = 0;
	private ZhuoConnHelper mConnHelper = null;
	private String msgid = null;
	private Button mCollectButton;
	private boolean isMy = false;
	private String uid = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_active_photo);
		mConnHelper = ZhuoConnHelper.getInstance(getApplicationContext());
		Intent i = getIntent();
		uid = i.getStringExtra("userid");
		String myid = ResHelper.getInstance(getApplicationContext())
				.getUserid();
		if (myid.equals(uid)) {
			isMy = true;
		}
		msgid = i.getStringExtra("msgid");
		pwh = new PopupWindows(PhotoActiveActivity.this);
		textViewTitle = (TextView) findViewById(R.id.userNameShow);
		imageViewPhoto = (ImageView) findViewById(R.id.imageViewPhoto);
		mDetector = new GestureDetector(this, this);
		mLoadImage.setOnReturnListener(onReturnListener);
		initClick();
		loadData();
	}

	private Callback onReturnListener = new Callback() {

		@Override
		public boolean onReturn(boolean result, String url) {
			LoadImageResultVO resultVO = new LoadImageResultVO();
			resultVO.setLoadSuccess(result);
			resultVO.setUrl(url);
			Message msg = mUIHandler.obtainMessage(MsgTagVO.UPDATE);
			msg.obj = resultVO;
			msg.sendToTarget();
			return false;
		}
	};

	private void initClick() {
		findViewById(R.id.buttonSetting).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						final View parent = findViewById(R.id.relativeLayoutPhoto);
						OnClickListener recommandClickListener = new OnClickListener() {

							@Override
							public void onClick(View v) {
								Intent i = new Intent(PhotoActiveActivity.this,
										UserSelectActivity.class);
								ArrayList<String> tempids = new ArrayList<String>(
										1);
								tempids.add(uid);
								i.putStringArrayListExtra("otherids", tempids);
								startActivityForResult(i, MsgTagVO.MSG_FOWARD);
							}
						};
						OnClickListener delClickListener = new OnClickListener() {

							@Override
							public void onClick(View v) {
								mConnHelper.delResource(mUIHandler,
										MsgTagVO.MSG_DEL, null, msgid, true,
										null, null);
							}
						};
						OnClickListener collectClickListener = new OnClickListener() {

							@Override
							public void onClick(View v) {
								if (mCollectButton == null) {
									mCollectButton = (Button) v;
								}
								if (isCollect.equals("1")) {
									mConnHelper.collectMsg(msgid, "0",
											mUIHandler, MsgTagVO.MSG_COLLECT,
											null, true, null, null);
								} else {
									mConnHelper.collectMsg(msgid, "1",
											mUIHandler, MsgTagVO.MSG_COLLECT,
											null, true, null, null);
								}
							}
						};
						if (isMy) {
							int[] infoResId = new int[] {
									R.string.label_sendrecommand,
									R.string.label_del };
							OnClickListener[] onClickListeners = new OnClickListener[] {
									recommandClickListener, delClickListener };
							pwh.showBottomPop(parent, onClickListeners,
									infoResId, "manage");

						} else {
							int[] infoResId = new int[] {
									R.string.label_sendrecommand,
									R.string.label_collect };
							OnClickListener[] onClickListeners = new OnClickListener[] {
									recommandClickListener,
									collectClickListener };
							pwh.showBottomPop(parent, onClickListeners,
									infoResId, "follow");

						}
					}
				});

		findViewById(R.id.buttonTabCmt).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						Intent i = new Intent(PhotoActiveActivity.this,
								MsgCmtActivity.class);
						i.putExtra("msgid", msgid);
						i.putExtra("parentid", msgid);
						startActivityForResult(i, MsgTagVO.MSG_CMT);
					}
				});

		findViewById(R.id.buttonTabZan).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						mConnHelper.goodMsg(msgid, mUIHandler,
								MsgTagVO.MSG_LIKE, null, true, null, null);
					}
				});

		findViewById(R.id.buttonBack).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				PhotoActiveActivity.this.finish();
			}
		});
		findViewById(R.id.relativeLayoutDetail).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						Intent i = new Intent(PhotoActiveActivity.this,
								MsgDetailActivity.class);
						i.putExtra("msgid", msgid);
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
					ZhuoInfoVO active = nljh.parseZhuoInfo();
					String text = active.getText();
					String cmtNum = active.getCmtnum();
					String goodNum = active.getGoodnum();
					pics = active.getPic();
					if (pics.size() > 0 && pics.size() > now) {
						textViewTitle.setText((now + 1) + "/" + pics.size());
						((TextView) findViewById(R.id.textViewContent))
								.setText(text);
						((TextView) findViewById(R.id.textViewZan))
								.setText(goodNum);
						((TextView) findViewById(R.id.textViewCmt))
								.setText(cmtNum);
						String url = pics.get(now).getOrgurl();
						imageViewPhoto.setTag(url);
						mLoadImage.addTask(url, imageViewPhoto);
						mLoadImage.doTask();
					}
				}
				break;
			}
			case MsgTagVO.MSG_FOWARD: {
				if (JsonHandler.checkResult((String) msg.obj,
						getApplicationContext())) {

				}
				break;
			}
			case MsgTagVO.MSG_COLLECT: {
				if (JsonHandler.checkResult((String) msg.obj,
						getApplicationContext())) {
					View parent = findViewById(R.id.relativeLayoutPhoto);
					if (isCollect.equals("1")) {
						isCollect = "0";
						mCollectButton.setText(R.string.label_collect);
						pwh.showPopTip(parent, null,
								R.string.label_cancelCollect);
					} else {
						isCollect = "1";
						mCollectButton.setText(R.string.label_collectCancel);
						pwh.showPopTip(parent, null,
								R.string.label_collectSuccess);
					}
				}
				break;
			}
			case MsgTagVO.MSG_LIKE: {
				if (JsonHandler.checkResult((String) msg.obj,
						getApplicationContext())) {
					CommonUtil.displayToast(getApplicationContext(),
							R.string.label_zanSuccess);
					TextView goodNumTV = (TextView) findViewById(R.id.textViewZan);
					goodNumTV.setText(String.valueOf(Integer.valueOf(goodNumTV
							.getText().toString()) + 1));
				}
				break;
			}
			case MsgTagVO.MSG_DEL: {
				if (JsonHandler.checkResult((String) msg.obj,
						getApplicationContext())) {
					PhotoActiveActivity.this.finish();
				}
				break;
			}
			case MsgTagVO.UPDATE: {
				LoadImageResultVO resultVO = (LoadImageResultVO) msg.obj;
				boolean result = resultVO.isLoadSuccess();
				if (!result) {
					if (((String) imageViewPhoto.getTag()).equals(resultVO
							.getUrl())) {
						imageViewPhoto
								.setImageResource(R.drawable.default_image);
					}
				}
				break;
			}
			}

		}

	};

	private void loadData() {
		String params = ZhuoCommHelper.getUrlMsgDetail() + "?msgid=" + msgid;
		mConnHelper.getFromServer(params, mUIHandler, MsgTagVO.DATA_LOAD,
				PhotoActiveActivity.this, true, new OnCancelListener() {

					@Override
					public void onCancel(DialogInterface dialog) {
						PhotoActiveActivity.this.finish();
					}
				});
	}

	@Override
	public boolean onDown(MotionEvent arg0) {
		return false;
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float arg2,
			float arg3) {
		if (pics != null && pics.size() > 0) {
			if (e1.getX() - e2.getX() > 60) {
				if (pics.size() == now + 1) {
					now = 0;
				} else {
					now++;
				}
				textViewTitle.setText((now + 1) + "/" + pics.size());
				String url = pics.get(now).getOrgurl();
				imageViewPhoto.setTag(url);
				mLoadImage.addTask(url, imageViewPhoto);
				mLoadImage.doTask();
				return true;
			} else if (e1.getX() - e2.getX() < -60) {
				if (now == 0) {
					now = pics.size() - 1;
				} else {
					now--;
				}
				textViewTitle.setText((now + 1) + "/" + pics.size());
				String url = pics.get(now).getOrgurl();
				imageViewPhoto.setTag(url);
				mLoadImage.addTask(url, imageViewPhoto);
				mLoadImage.doTask();
				return true;
			}
		}
		return false;
	}

	@Override
	public void onLongPress(MotionEvent arg0) {

	}

	@Override
	public boolean onScroll(MotionEvent arg0, MotionEvent arg1, float arg2,
			float arg3) {
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
	public boolean onTouchEvent(MotionEvent event) {
		return this.mDetector.onTouchEvent(event);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == Activity.RESULT_OK) {
			switch (requestCode) {
			case MsgTagVO.MSG_FOWARD:
				ArrayList<String> ids = data.getStringArrayListExtra("ids");
				String useridlist = "";
				if (ids != null && ids.size() > 0) {
					for (int i = 0; i < ids.size(); i++) {
						useridlist += ids.get(i) + ";";
					}
					useridlist = ZhuoCommHelper.subLast(useridlist);
					mConnHelper.recommandMsg(msgid, useridlist, mUIHandler,
							MsgTagVO.MSG_FOWARD, null, true, null, null);
				}
				break;
			case MsgTagVO.MSG_CMT:
				TextView numTV = (TextView) findViewById(R.id.textViewCmt);
				numTV.setText(String.valueOf(Integer.valueOf(numTV.getText()
						.toString()) + 1));
				break;
			default:
				break;
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

}
