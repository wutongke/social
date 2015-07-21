package com.cpstudio.zhuojiaren;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.ClipboardManager;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import butterknife.ButterKnife;

import com.cpstudio.zhuojiaren.adapter.ImQuanListAdapter;
import com.cpstudio.zhuojiaren.adapter.ZhuoInfoListAdapter;
import com.cpstudio.zhuojiaren.facade.ImQuanFacade;
import com.cpstudio.zhuojiaren.facade.QuanFacade;
import com.cpstudio.zhuojiaren.facade.UserFacade;
import com.cpstudio.zhuojiaren.helper.AsyncConnectHelper.FinishCallback;
import com.cpstudio.zhuojiaren.helper.EmotionPopHelper;
import com.cpstudio.zhuojiaren.helper.JsonHandler;
import com.cpstudio.zhuojiaren.helper.ResHelper;
import com.cpstudio.zhuojiaren.helper.VoiceHelper;
import com.cpstudio.zhuojiaren.helper.VoiceHelper.PlayingListener;
import com.cpstudio.zhuojiaren.helper.VoiceHelper.RecordingListener;
import com.cpstudio.zhuojiaren.helper.ZhuoCommHelper;
import com.cpstudio.zhuojiaren.helper.ZhuoConnHelper;
import com.cpstudio.zhuojiaren.model.ImQuanVO;
import com.cpstudio.zhuojiaren.model.MsgTagVO;
import com.cpstudio.zhuojiaren.model.QuanVO;
import com.cpstudio.zhuojiaren.model.UserVO;
import com.cpstudio.zhuojiaren.util.CommonUtil;
import com.cpstudio.zhuojiaren.util.DeviceInfoUtil;
import com.cpstudio.zhuojiaren.widget.PopupWindows;
import com.cpstudio.zhuojiaren.widget.PullDownView;
import com.cpstudio.zhuojiaren.widget.PullDownView.OnPullDownListener;
import com.cpstudui.zhuojiaren.lz.ZhuoMaiCardActivity;
import com.cpstudui.zhuojiaren.lz.ZhuoQuanMainActivity;
import com.utils.FileUtil;

@SuppressWarnings("deprecation")
public class QuanBoardChatActivity extends BaseActivity implements
		OnPullDownListener {

	private PullDownView mPullDownView;

	private ListView mListView2;
	private ImQuanListAdapter mAdapter2;
	private ArrayList<ImQuanVO> mList2 = new ArrayList<ImQuanVO>();
	private VoiceHelper mVoiceHelper = null;
	private ImQuanFacade mFacade = null;
	private String isCreater = "0";

	private String myid = null;
	private String groupid = null;
	private ZhuoConnHelper mConnHelper = null;
	private PopupWindows pwh = null;

	private UserFacade userFacade = null;
	private QuanFacade quanFacade = null;
	private Set<String> showMsg = new HashSet<String>();

	private MsgReceiver msgReceiver = null;
	private EmotionPopHelper ep = null;
	private float times = 2;
	private static final int SELECT_CARD = 100;
	private boolean toTab = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_quan_board_chat);

		initTitle();
		ButterKnife.inject(this);

		Intent intent = getIntent();
		toTab = intent.getBooleanExtra("toTab", false);
		userFacade = new UserFacade(QuanBoardChatActivity.this);
		quanFacade = new QuanFacade(QuanBoardChatActivity.this);
		mConnHelper = ZhuoConnHelper.getInstance(getApplicationContext());
		myid = ResHelper.getInstance(getApplicationContext()).getUserid();
		times = DeviceInfoUtil.getDeviceCsd(getApplicationContext());
		Intent i = getIntent();
		groupid = i.getStringExtra("groupid");
//		String type = i.getStringExtra("type");//"board","chat"旧版本两种，现在不用了
		QuanVO detail = quanFacade.getById(groupid);

		UserVO founder = detail.getFounder();
		if (founder != null) {
			String founderid = founder.getUserid();
			if (founderid != null && founderid.equals(myid)) {
				isCreater = "1";
			}
		}
		String quanName = detail.getGname();
		title.setText(quanName);

		pwh = new PopupWindows(QuanBoardChatActivity.this);
		mFacade = new ImQuanFacade(QuanBoardChatActivity.this);

		ep = new EmotionPopHelper(QuanBoardChatActivity.this,
				getEmotionClickListener());
		mAdapter2 = new ImQuanListAdapter(this, mList2, clickListener,
				longClickListener, sendListener);
		mPullDownView = (PullDownView) findViewById(R.id.pull_down_view);
		mPullDownView.initHeaderViewAndFooterViewAndListView(this,
				R.layout.listview_header);
		mPullDownView.setOnPullDownListener(this);
		mListView2 = mPullDownView.getListView();
		mListView2.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(arg1.getWindowToken(), 0);
				findViewById(R.id.linearLayoutChatMore)
						.setVisibility(View.GONE);
				findViewById(R.id.buttonChatMore).setSelected(false);
			}
		});
		mListView2.setDividerHeight(0);
		mListView2.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
		mListView2.setAdapter(mAdapter2);
		mPullDownView.setShowHeader();
		mPullDownView.setHideFooter(false);
		initClick();
		loadChatData();
	}

	private OnItemClickListener getEmotionClickListener() {
		return new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				String key = ((TextView) arg1.findViewById(R.id.text))
						.getText().toString();
				sendChatMsg("emotion", key, "");
			}
		};
	}

	@Override
	protected void onStart() {
		super.onStart();
	}

	@Override
	protected void onRestart() {
		super.onRestart();
	}

	@Override
	protected void onResume() {
		NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		notificationManager.cancelAll();
		ResHelper.getInstance(getApplicationContext()).setChatgroup(groupid);
		if (null == msgReceiver) {
			msgReceiver = new MsgReceiver();
		}
		IntentFilter filter = new IntentFilter("com.cpstudio.groupchat."
				+ groupid);
		registerReceiver(msgReceiver, filter);
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		if (null != mVoiceHelper) {
			mVoiceHelper.release();
			mVoiceHelper = null;
		}
		super.onDestroy();
	}

	@Override
	protected void onPause() {
		ResHelper.getInstance(getApplicationContext()).setChatgroup(null);
		if (msgReceiver != null) {
			unregisterReceiver(msgReceiver);
		}
		if (null != mVoiceHelper) {
			mVoiceHelper.release();
			mVoiceHelper = null;
		}
		super.onPause();
	}

	@Override
	protected void onStop() {
		if (null != mVoiceHelper) {
			mVoiceHelper.release();
			mVoiceHelper = null;
		}
		super.onStop();
	}

	@SuppressLint("HandlerLeak")
	private Handler mUIHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
//			case MsgTagVO.DATA_OTHER: {
//				if (msg.obj != null && !msg.obj.equals("")) {
//					JsonHandler nljh = new JsonHandler((String) msg.obj,
//							getApplicationContext());
//					ArrayList<ZhuoInfoVO> list = nljh.parseZhuoInfoList();
//					if (!list.isEmpty()) {
//						mList.clear();
//						mList.addAll(list);
//						mAdapter.notifyDataSetChanged();
//					}
//				}
//				break;
//			}
			case MsgTagVO.DATA_MORE: {
				if (msg.obj != null && msg.obj instanceof ImQuanVO) {
					ImQuanVO immsg = (ImQuanVO) msg.obj;
					mList2.add(immsg);
					checkShow(immsg);
					mAdapter2.notifyDataSetChanged();
					mListView2.setSelection(mList2.size() - 1);
				}
				break;
			}
			case MsgTagVO.PUB_INFO: {
				Bundle bundle = msg.getData();
				String id = bundle.getString("data");
				ImQuanVO immsg = mFacade.getById(id);
				if (JsonHandler.checkResult((String) msg.obj,
						getApplicationContext())) {
					String serverId = JsonHandler
							.getSingleResult((String) msg.obj);
					if (serverId != null && !serverId.equals("0")
							&& !serverId.equals("")) {
						immsg.setIsread("1");
						immsg.setId(serverId);
						mFacade.updateWithNewId(immsg, id);
					} else {
						immsg.setIsread("2");
						mFacade.update(immsg);
					}
				} else {
					immsg.setIsread("2");
					mFacade.update(immsg);
				}
				Intent groupChat = new Intent("com.cpstudio.groupchat");
				sendBroadcast(groupChat);
				Intent chatIntent = new Intent("com.cpstudio.chatlist");
				sendBroadcast(chatIntent);
				for (int i = 0; i < mList2.size(); i++) {
					if (mList2.get(i).getId().equals(id)) {
						checkShow(immsg);
						mList2.set(i, immsg);
						break;
					}
				}
				mAdapter2.notifyDataSetChanged();
				mListView2.setSelection(mList2.size() - 1);
				break;
			}
			}
		}
	};

	private void loadChatData() {
		ArrayList<ImQuanVO> list = mFacade.getAllByCondition("groupid = ?",
				new String[] { groupid }, null, "addtime desc limit 0,18");
		if (!list.isEmpty()) {
			Collections.reverse(list);
			for (ImQuanVO msg : list) {
				showMsg.add(msg.getId());
			}
			mList2.clear();
			checkShow(list, false);
			list.get(0).setShowtime(true);
			mList2.addAll(list);
			mAdapter2.notifyDataSetChanged();
			mListView2.setSelection(mList2.size() - 1);
			mFacade.updateReadState(groupid);
		}
	}

	private void loadChatBeforeData() {
		mListView2.setTranscriptMode(ListView.TRANSCRIPT_MODE_NORMAL);
		boolean loadState = false;
		int offset = mList2.size();
		ArrayList<ImQuanVO> list = mFacade.getAllByCondition("groupid = ?",
				new String[] { groupid }, null, "addtime desc limit 10 offset "
						+ offset);
		if (!list.isEmpty()) {
			loadState = true;
			int position = list.size();
			Collections.reverse(list);
			for (ImQuanVO msg : list) {
				showMsg.add(msg.getId());
			}
			list.addAll(mList2);
			checkShow(list, true);
			list.get(0).setShowtime(true);
			mList2.clear();
			mList2.addAll(list);
			mAdapter2.notifyDataSetChanged();
			mListView2.setSelection(position);
		}
		mPullDownView.RefreshComplete(loadState);
	}

	private void loadChatAfterData() {
		ArrayList<ImQuanVO> list = mFacade.getAllByCondition(
				"groupid = ? and isread = ?", new String[] { groupid, "0" },
				null, "addtime desc limit 10");
		ArrayList<ImQuanVO> listTemp = new ArrayList<ImQuanVO>();
		for (ImQuanVO msg : list) {
			if (msg.getType().equals("voice") || msg.getType().equals("image")) {
				if (msg.getSavepath() == null || msg.getSavepath().equals("")) {
					continue;
				}
			}
			if (!showMsg.contains(msg.getId())) {
				listTemp.add(msg);
				showMsg.add(msg.getId());
			}
		}
		if (!listTemp.isEmpty()) {
			Collections.reverse(listTemp);
			checkShow(listTemp, false);
			mList2.addAll(listTemp);
			mAdapter2.notifyDataSetChanged();
			mListView2.setSelection(mList2.size() - 1);
			mFacade.updateReadState(groupid);
		}
	}

	private void checkShow(ArrayList<ImQuanVO> msgs, boolean before) {
		if (!msgs.isEmpty()) {
			if (mList2.size() > 1 && !before) {
				ImQuanVO lastMsg = mList2.get(mList2.size() - 1);
				long interTime = CommonUtil.calcTimeToTime(
						lastMsg.getAddtime(), msgs.get(0).getAddtime());
				if (interTime / 60000 > 10) {
					msgs.get(0).setShowtime(true);
				}
			}
			for (int i = 0; i < msgs.size() - 1; i++) {
				long interTime = CommonUtil.calcTimeToTime(msgs.get(i)
						.getAddtime(), msgs.get(i + 1).getAddtime());
				if (interTime / 60000 > 10) {
					msgs.get(i + 1).setShowtime(true);
				}
			}
		}
	}

	private void checkShow(ImQuanVO msg) {
		if (msg != null) {
			if (mList2.size() > 1) {
				ImQuanVO lastMsg = mList2.get(mList2.size() - 1);
				long interTime = CommonUtil.calcTimeToTime(
						lastMsg.getAddtime(), msg.getAddtime());
				if (interTime / 60000 > 10) {
					msg.setShowtime(true);
				}
			}
		}
	}

	private void addChatAfterData(ImQuanVO immsg) {
		Message msg = mUIHandler.obtainMessage(MsgTagVO.DATA_MORE);
		msg.obj = immsg;
		msg.sendToTarget();
	}

	private void sendChatMsg(String type, String str, String secs) {
		ImQuanVO immsg = new ImQuanVO();
		UserVO sender = userFacade.getSimpleInfoById(myid);
		if (sender == null) {
			loadUserInfoBeforeAddToDB(type, str, secs, myid);
			return;
		}
		QuanVO quan = quanFacade.getById(groupid);
		if (quan == null) {
			loadQuanInfoBeforeAddToDB(type, str, secs);
			return;
		}
		String tempId = System.currentTimeMillis() + "";
		immsg.setId(tempId);
		immsg.setSender(sender);
		immsg.setGroup(quan);
		immsg.setType(type);
		immsg.setIsread("4");
		immsg.setAddtime(CommonUtil.getNowTimeStr("yyyy-MM-dd HH:mm:ss"));
		if (type.equals("text")) {
			immsg.setContent(str);
			mConnHelper.groupChat((String) null, mUIHandler, MsgTagVO.PUB_INFO,
					null, str, type, groupid, "", true, null, tempId);
		} else if (type.equals("image")) {
			immsg.setSavepath(str);
			mConnHelper.groupChat(str, mUIHandler, MsgTagVO.PUB_INFO, null, "",
					type, groupid, "", true, null, tempId);
		} else if (type.equals("voice")) {
			immsg.setSavepath(str);
			immsg.setSecs(secs);
			mConnHelper.groupChat(str, mUIHandler, MsgTagVO.PUB_INFO, null, "",
					type, groupid, secs, true, null, tempId);
		} else if (type.equals("emotion")) {
			immsg.setContent(str);
			mConnHelper.groupChat((String) null, mUIHandler, MsgTagVO.PUB_INFO,
					null, str, type, groupid, "", true, null, tempId);
		} else if (type.equals("card")) {
			UserVO cardUser = userFacade.getById(str);
			if (cardUser == null) {
				loadUserInfoBeforeAddToDB(type, str, secs, str);
				return;
			}
			str = cardUser.getUserid() + "____" + cardUser.getUsername()
					+ "____" + cardUser.getCompany() + "____"
					+ cardUser.getUheader();
			immsg.setContent(str);
			mConnHelper.groupChat("", mUIHandler, MsgTagVO.PUB_INFO, null, str,
					type, groupid, "", true, null, tempId);
		}
		mFacade.insert(immsg);
		addChatAfterData(immsg);
	}

	private void loadUserInfoBeforeAddToDB(final String type, final String str,
			final String secs, final String userid) {
		FinishCallback callback = new FinishCallback() {

			@Override
			public boolean onReturn(String rs, int responseCode) {
				JsonHandler nljh = new JsonHandler(rs, getApplicationContext());
				UserVO user = nljh.parseUser();
				if (null != user) {
					userFacade.insert(user);
					sendChatMsg(type, str, secs);
				}
				return false;
			}
		};
		mConnHelper.getFromServer(ZhuoCommHelper.getUrlUserInfo() + "?uid="
				+ userid, callback);
	}

	private void loadQuanInfoBeforeAddToDB(final String type, final String str,
			final String secs) {
		FinishCallback callback = new FinishCallback() {

			@Override
			public boolean onReturn(String rs, int responseCode) {
				JsonHandler nljh = new JsonHandler(rs, getApplicationContext());
				QuanVO quan = nljh.parseQuan();
				if (null != quan) {
					quanFacade.insert(quan);
					sendChatMsg(type, str, secs);
				}
				return false;
			}
		};
		mConnHelper.getFromServer(ZhuoCommHelper.getUrlGroupDetail()
				+ "?groupid=" + groupid, callback);
	}

	private void initClick() {
		function.setBackgroundResource(R.drawable.zxhome_zx_1);
		function.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent i = new Intent(QuanBoardChatActivity.this,
						ZhuoQuanMainActivity.class);
				i.putExtra("groupid", groupid);
				startActivity(i);
			}
		});
		// findViewById(R.id.buttonPublish).setOnClickListener(
		// new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// Intent i = new Intent(QuanBoardChatActivity.this,
		// PublishBoardActivity.class);
		// i.putExtra("groupid", groupid);
		// startActivity(i);
		// }
		// });
//		findViewById(R.id.buttonBack).setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				QuanBoardChatActivity.this.finish();
//			}
//		});

		findViewById(R.id.buttonChatMore).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
						imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
						View more = findViewById(R.id.linearLayoutChatMore);
						if (more.getVisibility() == View.VISIBLE) {
							more.setVisibility(View.GONE);
							v.setSelected(false);
						} else {
							mListView2
									.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
							more.setVisibility(View.VISIBLE);
							v.setSelected(true);
						}
					}
				});
		findViewById(R.id.buttonChatSend).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						EditText text = (EditText) findViewById(R.id.editTextChatText);
						String str = text.getText().toString();
						findViewById(R.id.linearLayoutChatMore).setVisibility(
								View.GONE);
						findViewById(R.id.buttonChatMore).setSelected(false);
						if (!str.equals("")) {
							InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
							imm.hideSoftInputFromWindow(text.getWindowToken(),
									0);
							sendChatMsg("text", str, null);
							text.setText("");
						} else {
							CommonUtil.displayToast(getApplicationContext(),
									R.string.label_null);
						}
					}
				});
		findViewById(R.id.buttonChatRecord).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
						imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
						findViewById(R.id.buttonChatRecord).setVisibility(
								View.GONE);
						findViewById(R.id.buttonChatText).setVisibility(
								View.VISIBLE);
						findViewById(R.id.editTextChatText).setVisibility(
								View.GONE);
						findViewById(R.id.buttonChatRecorder).setVisibility(
								View.VISIBLE);
						findViewById(R.id.buttonChatSend).setVisibility(
								View.GONE);
						findViewById(R.id.linearLayoutChatMore).setVisibility(
								View.GONE);
						findViewById(R.id.buttonChatMore).setSelected(false);
					}
				});
		findViewById(R.id.editTextChatText).setOnFocusChangeListener(
				new OnFocusChangeListener() {

					@Override
					public void onFocusChange(View v, boolean hasFocus) {
						if (hasFocus) {
							mListView2
									.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
							findViewById(R.id.linearLayoutChatMore)
									.setVisibility(View.GONE);
							findViewById(R.id.buttonChatMore)
									.setSelected(false);
						}
					}
				});
		findViewById(R.id.editTextChatText).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						mListView2
								.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
						findViewById(R.id.linearLayoutChatMore).setVisibility(
								View.GONE);
						findViewById(R.id.buttonChatMore).setSelected(false);
					}
				});
		findViewById(R.id.buttonChatText).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						findViewById(R.id.buttonChatRecord).setVisibility(
								View.VISIBLE);
						findViewById(R.id.buttonChatText).setVisibility(
								View.GONE);
						findViewById(R.id.editTextChatText).setVisibility(
								View.VISIBLE);
						findViewById(R.id.buttonChatRecorder).setVisibility(
								View.GONE);
						findViewById(R.id.buttonChatSend).setVisibility(
								View.VISIBLE);
						findViewById(R.id.linearLayoutChatMore).setVisibility(
								View.GONE);
						findViewById(R.id.buttonChatMore).setSelected(false);
					}
				});
		findViewById(R.id.imageViewChatEmotion).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						ep.showBottomPop(findViewById(R.id.layoutChatOptions));
					}
				});
		findViewById(R.id.imageViewChatPhoto).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
						imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
						String state = Environment.getExternalStorageState();
						if (state.equals(Environment.MEDIA_MOUNTED)) {
							Intent intent = new Intent(
									MediaStore.ACTION_IMAGE_CAPTURE);
							startActivityForResult(intent,
									MsgTagVO.SELECT_CAMER);
						} else {
							CommonUtil.displayToast(getApplicationContext(),
									R.string.error2);
						}
					}
				});
		findViewById(R.id.imageViewChatCard).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
						imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
						Intent i = new Intent(QuanBoardChatActivity.this,
								UserSelectActivity.class);
						i.putExtra("max", 1);
						startActivityForResult(i, SELECT_CARD);
					}
				});
		findViewById(R.id.imageViewChatPic).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {

						InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
						imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
						Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
						intent.addCategory(Intent.CATEGORY_OPENABLE);
						intent.setType("image/*");
						startActivityForResult(Intent.createChooser(intent,
								getString(R.string.info0)),
								MsgTagVO.SELECT_PICTURE);
					}
				});
		findViewById(R.id.buttonChatRecorder).setOnTouchListener(
				new OnTouchListener() {
					boolean cancel = false, successStart = true;
					String filePath = null;
					PopupWindow popupWindow = null;
					int volumeHeight = 0;
					LayoutParams lp = null;
					View ll = null;
					int[] volumes = new int[] { 0, 0, 0, 0, 0, 0, 0, 0 };

					@Override
					public boolean onTouch(View v, MotionEvent event) {
						Button record = (Button) v;
						if (event.getAction() == MotionEvent.ACTION_DOWN) {
							cancel = false;
							if (mVoiceHelper == null) {
								mVoiceHelper = new VoiceHelper(
										QuanBoardChatActivity.this);
							}
							filePath = mVoiceHelper.newRecord(String
									.valueOf(System.currentTimeMillis()));
							if (filePath == null) {
								successStart = false;
								CommonUtil.displayToast(
										getApplicationContext(),
										R.string.error9);
							} else {
								mVoiceHelper.recording(recordingListener);
								popupWindow = pwh.showRecordDlg(mListView2);
								ll = popupWindow.getContentView().findViewById(
										R.id.linearLayoutVolume);
								lp = ll.getLayoutParams();
							}
							findViewById(R.id.buttonChatRecorder)
									.setBackgroundResource(
											R.drawable.button_release);
							record.setText(R.string.label_release);
							record.setTextColor(Color.rgb(183, 183, 183));
							record.setBackgroundResource(R.drawable.button_release);
						} else if (event.getAction() == MotionEvent.ACTION_MOVE) {
							if (successStart) {
								float y = event.getY();
								View parent = popupWindow.getContentView();
								TextView text = (TextView) parent
										.findViewById(R.id.textViewTips);
								if (y > 0) {
									cancel = false;
									parent.setBackgroundResource(R.drawable.pop_cancel2);
									text.setText(R.string.label_move_cancel);
									text.setTextColor(Color.rgb(196, 196, 196));
								} else {
									cancel = true;
									parent.setBackgroundResource(R.drawable.pop_cancel);
									text.setText(R.string.label_release_cancel);
									text.setTextColor(Color.WHITE);
								}
							}
						} else if (event.getAction() == MotionEvent.ACTION_UP) {
							if (null != popupWindow) {
								popupWindow.dismiss();
							}
							if (successStart && filePath != null) {
								float y = event.getY();
								if (y > 0 && !cancel && mVoiceHelper != null) {
									int secs = mVoiceHelper.finishRecording();
									if (secs > 0) {
										sendChatMsg("voice", filePath,
												String.valueOf(secs));
									}
								} else {
									mVoiceHelper.finishRecording();
								}
							}
							record.setText(R.string.label_press);
							record.setTextColor(Color.BLACK);
							record.setBackgroundResource(R.drawable.button_record);
						}
						return false;
					}

					private RecordingListener recordingListener = new RecordingListener() {

						@Override
						public void onRecording(int volume, long time) {
							if (!cancel) {
								if (volumeHeight == 0) {
									volumeHeight = ((View) popupWindow
											.getContentView()).findViewById(
											R.id.imageViewHeight)
											.getMeasuredHeight();
								}
								volumes[(int) ((time / 100) % volumes.length)] = volume + 1;
								int sum = 0;
								for (int vol : volumes) {
									sum += vol;
								}
								int v = (int) (sum * 8 * 1.5 / (volumes.length * 32768));
								v = v > 8 ? 8 : v;
								int newHeight = volumeHeight * v;
								lp.height = newHeight;
								ll.setLayoutParams(lp);
							}
						}
					};
				});

	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			switch (requestCode) {
			case SELECT_CARD:
				ArrayList<String> ids = data.getStringArrayListExtra("ids");
				if (ids != null && ids.size() == 1) {
					String id = ids.get(0);
					sendChatMsg("card", id, "");
				}
				break;
//			case MsgTagVO.DATA_REFRESH: {
//				loadBoardData();
//			}
//			case MsgTagVO.MSG_CMT: {
//				String forward = data.getStringExtra("forward");
//				String msgid = data.getStringExtra("msgid");
//				for (int i = 0; i < mList.size(); i++) {
//					ZhuoInfoVO item = mList.get(i);
//					if (msgid != null) {
//						if (item.getMsgid().equals(msgid)) {
//							if (forward != null && forward.equals("1")) {
//								item.setForwardnum(String.valueOf(Integer
//										.valueOf(item.getForwardnum()) + 1));
//							}
//							item.setCmtnum(String.valueOf(Integer.valueOf(item
//									.getCmtnum()) + 1));
//							mList.set(i, item);
//							break;
//						}
//					}
//				}
//				mAdapter.notifyDataSetChanged();
//				break;
//			}
			default:
				String filePath = pwh.dealPhotoReturn(requestCode, resultCode,
						data, false);
				if (filePath != null) {
					sendChatMsg("image", filePath, null);
					findViewById(R.id.linearLayoutChatMore).setVisibility(
							View.GONE);
					findViewById(R.id.buttonChatMore).setSelected(false);
				}
				break;
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	

	
	
	private PlayingListener playingListener(final View v, boolean left) {
		if (!left) {
			return new PlayingListener() {
				@Override
				public void onPlaying(int pos) {
					if (!mVoiceHelper.ismStartPlaying()) {
						int nr = pos % 3;
						switch (nr) {
						case 0:
							v.setBackgroundResource(R.drawable.right_voice_1r);
							break;
						case 1:
							v.setBackgroundResource(R.drawable.right_voice_2r);
							break;
						case 2:
							v.setBackgroundResource(R.drawable.right_voice_3r);
							break;
						default:
							v.setBackgroundResource(R.drawable.right_voice_3r);
							break;
						}
					}
				}

				@Override
				public void onStop() {
					v.setBackgroundResource(R.drawable.right_voice_3r);
				}
			};
		} else {
			return new PlayingListener() {
				@Override
				public void onPlaying(int pos) {
					if (!mVoiceHelper.ismStartPlaying()) {
						int nr = pos % 3;
						switch (nr) {
						case 0:
							v.setBackgroundResource(R.drawable.left_voice_1l);
							break;
						case 1:
							v.setBackgroundResource(R.drawable.left_voice_2l);
							break;
						case 2:
							v.setBackgroundResource(R.drawable.left_voice_3l);
							break;
						default:
							v.setBackgroundResource(R.drawable.left_voice_3l);
							break;
						}
					}
				}

				@Override
				public void onStop() {
					v.setBackgroundResource(R.drawable.left_voice_3l);
				}
			};
		}
	}

	private class MsgReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			loadChatAfterData();
		}

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		AudioManager mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
		int currentVolume = 0;
		switch (keyCode) {
		case KeyEvent.KEYCODE_VOLUME_UP:// 音量增大
			currentVolume = mAudioManager
					.getStreamVolume(AudioManager.STREAM_MUSIC);
			mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC,
					currentVolume + 1, 1);
			return true;
		case KeyEvent.KEYCODE_VOLUME_DOWN:// 音量减小
			currentVolume = mAudioManager
					.getStreamVolume(AudioManager.STREAM_MUSIC);
			mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC,
					currentVolume - 1, 1);
			return true;
		}
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			View more = findViewById(R.id.linearLayoutChatMore);
			if (more.getVisibility() == View.VISIBLE) {
				more.setVisibility(View.GONE);
				findViewById(R.id.buttonChatMore).setSelected(false);
				return true;
			}
		}
		return super.onKeyDown(keyCode, event);
	}

	private OnClickListener clickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			String type = (String) v.getTag(R.id.tag_type);
			if (type.equals("voice")) {
				playVoice(v);
			} else if (type.equals("image-network")) {
				viewNetImage(v);
			} else if (type.equals("image-local")) {
				viewLocalImage(v);
			} else if (type.equals("card")) {
				viewCard(v);
			}
		}
	};

	private OnClickListener sendListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			final String msgid = (String) v.getTag(R.id.tag_id);
			new AlertDialog.Builder(QuanBoardChatActivity.this)
					.setNegativeButton(R.string.CANCEL, null)
					.setItems(
							new String[] { getString(R.string.RESEND) },
							new android.content.DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									switch (which) {
									case 0:
										ImQuanVO msg = mFacade.getById(msgid);
										sendChatMsg(msg.getType(),
												msg.getContent(), msg.getSecs());
										break;
									}
								}
							}).show();
		}
	};

	private OnLongClickListener longClickListener = new OnLongClickListener() {

		@Override
		public boolean onLongClick(View v) {
			String type = (String) v.getTag(R.id.tag_type);
			String state = (String) v.getTag(R.id.tag_state);
			final View view = v;
			AlertDialog.Builder adb = new AlertDialog.Builder(
					QuanBoardChatActivity.this).setNegativeButton(
					R.string.CANCEL, null);
			if (state.equals("2")) {
				adb.setItems(new String[] { getString(R.string.RESEND) },
						new android.content.DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								switch (which) {
								case 0:
									String msgid = (String) view
											.getTag(R.id.tag_id);
									ImQuanVO msg = mFacade.getById(msgid);
									sendChatMsg(msg.getType(),
											msg.getContent(), msg.getSecs());
									break;
								}
							}
						});
			} else {
				if (type.equals("voice")) {
					adb.setItems(
							new String[] { getString(R.string.PLAY),
									getString(R.string.SAVE) },
							new android.content.DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									switch (which) {
									case 0:
										playVoice(view);
										break;
									case 1:
										String newPath = copyFile(view, ".amr");
										if (newPath != null) {
											CommonUtil.displayToast(
													getApplicationContext(),
													getString(R.string.info80)
															+ newPath);
										} else {
											CommonUtil.displayToast(
													getApplicationContext(),
													R.string.info81);
										}
										break;
									}
								}
							});
				} else if (type.equals("image-network")) {
					adb.setItems(
							new String[] { getString(R.string.FULLIMAGE),
									getString(R.string.SAVE) },
							new android.content.DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									switch (which) {
									case 0:
										viewNetImage(view);
										break;
									case 1:
										String newPath = copyFile(view, ".jpg");
										if (newPath != null) {
											CommonUtil.displayToast(
													getApplicationContext(),
													getString(R.string.info80)
															+ newPath);
										} else {
											CommonUtil.displayToast(
													getApplicationContext(),
													R.string.info81);
										}
										break;
									}
								}
							});
				} else if (type.equals("image-local")) {
					adb.setItems(
							new String[] { getString(R.string.FULLIMAGE),
									getString(R.string.SAVE) },
							new android.content.DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									switch (which) {
									case 0:
										viewLocalImage(view);
										break;
									case 1:
										String newPath = copyFile(view, ".jpg");
										if (newPath != null) {
											CommonUtil.displayToast(
													getApplicationContext(),
													getString(R.string.info80)
															+ newPath);
										} else {
											CommonUtil.displayToast(
													getApplicationContext(),
													R.string.info81);
										}
										break;
									}
								}
							});
				} else if (type.equals("card")) {
					adb.setItems(
							new String[] { getString(R.string.OPEN),
									getString(R.string.FOLLOW) },
							new android.content.DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									switch (which) {
									case 0:
										viewCard(view);
										break;
									case 1:
										String uid = (String) view
												.getTag(R.id.tag_string);
										mConnHelper.followUser(uid, "1",
												mUIHandler,
												MsgTagVO.MSG_RCMDUSER, null,
												true, null, null);
										break;
									}
								}
							});
				} else if (type.equals("emotion")) {
					return true;
				} else {
					adb.setItems(
							new String[] { getString(R.string.COPY) },
							new android.content.DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									switch (which) {
									case 0:
										String msgid = (String) view
												.getTag(R.id.tag_id);
										ImQuanVO msg = mFacade.getById(msgid);
										ClipboardManager cbm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
										cbm.setText(msg.getContent());
										CommonUtil.displayToast(
												getApplicationContext(),
												R.string.info78);
										break;
									}
								}
							});
				}
			}
			adb.show();
			return true;
		}
	};

	private void playVoice(View v) {
		String savePath = (String) v.getTag(R.id.tag_path);
		boolean left = (Boolean) v.getTag(R.id.tag_boolean);
		String msgid = (String) v.getTag(R.id.tag_id);
		if (savePath == null || savePath.equals("")) {
			CommonUtil.displayToast(getApplicationContext(), R.string.error4);
			return;
		}
		if (mVoiceHelper == null) {
			mVoiceHelper = new VoiceHelper(QuanBoardChatActivity.this);
		}
		boolean doFinish = false;
		if (!mVoiceHelper.ismStartPlaying() && mVoiceHelper.getmTag() != null) {
			mVoiceHelper.finishPlaying();
			doFinish = true;
		}
		if (!doFinish || !mVoiceHelper.getmTag().equals(msgid)) {
			if (mVoiceHelper.ismStartPlaying()) {
				mVoiceHelper.newPlay(savePath);
				mVoiceHelper.setmTag(msgid);
				View voiceView = null;
				if (left) {
					voiceView = v.findViewById(R.id.textViewMsgContent);
				} else {
					voiceView = v.findViewById(R.id.textViewMsgContent2);
				}
				mVoiceHelper.playing(playingListener(voiceView, left), false);
			}
		}
	}

	private void viewNetImage(View v) {
		String savePath = (String) v.getTag(R.id.tag_path);
		if (savePath == null || savePath.equals("")) {
			CommonUtil.displayToast(getApplicationContext(), R.string.error4);
			return;
		}
		Intent intent = new Intent(QuanBoardChatActivity.this,
				PhotoViewMultiActivity.class);
		ArrayList<String> pics = new ArrayList<String>();
		pics.add(savePath);
		intent.putStringArrayListExtra("pics", pics);
		intent.putExtra("pic", savePath);
		startActivity(intent);
	}

	private void viewLocalImage(View v) {
		String savePath = (String) v.getTag(R.id.tag_path);
		if (savePath == null || savePath.equals("")) {
			CommonUtil.displayToast(getApplicationContext(), R.string.error4);
			return;
		}
		Intent intent = new Intent(QuanBoardChatActivity.this,
				PhotoViewMultiActivity.class);
		ArrayList<String> pics = new ArrayList<String>();
		pics.add(savePath);
		intent.putStringArrayListExtra("pics", pics);
		intent.putExtra("pic", savePath);
		intent.putExtra("type", "local");
		startActivity(intent);
	}

	private void viewCard(View v) {
		String uid = (String) v.getTag(R.id.tag_string);
		Intent intent = new Intent(QuanBoardChatActivity.this,
				ZhuoMaiCardActivity.class);
		intent.putExtra("userid", uid);
		startActivity(intent);
	}

	private String copyFile(View v, String endwith) {
		String oldPath = (String) v.getTag(R.id.tag_path);
		String newPath = FileUtil.getSDPATH() + "download/"
				+ CommonUtil.getNowTimeStr("yyMMddHHmmss") + endwith;
		if (FileUtil.copyFile(oldPath, newPath)) {
			return newPath;
		}
		return null;
	}

	@Override
	public void finish() {
		if (toTab
				&& !ResHelper.getInstance(getApplicationContext()).isAppShow()) {
			Intent intent = new Intent(getApplicationContext(),
					TabContainerActivity.class);
			intent.putExtra(TabContainerActivity.SHOW_PAGE,
					TabContainerActivity.MSG_PAGE);
			startActivity(intent);
		}
		super.finish();
	}

	@Override
	public void onRefresh() {
		loadChatBeforeData();
	}

	@Override
	public void onMore() {
	}

}
