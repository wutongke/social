package com.cpstudio.zhuojiaren;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import com.cpstudio.zhuojiaren.adapter.ImMsgListAdapter;
import com.cpstudio.zhuojiaren.facade.ImChatFacade;
import com.cpstudio.zhuojiaren.facade.UserFacade;
import com.cpstudio.zhuojiaren.helper.AsyncConnectHelper.FinishCallback;
import com.cpstudio.zhuojiaren.helper.EmotionPopHelper;
import com.cpstudio.zhuojiaren.helper.JsonHandler;
import com.cpstudio.zhuojiaren.helper.ResHelper;
import com.cpstudio.zhuojiaren.helper.VoiceHelper;
import com.cpstudio.zhuojiaren.helper.ZhuoCommHelper;
import com.cpstudio.zhuojiaren.helper.ZhuoConnHelper;
import com.cpstudio.zhuojiaren.helper.VoiceHelper.PlayingListener;
import com.cpstudio.zhuojiaren.helper.VoiceHelper.RecordingListener;
import com.cpstudio.zhuojiaren.model.ImMsgVO;
import com.cpstudio.zhuojiaren.model.MsgTagVO;
import com.cpstudio.zhuojiaren.model.UserVO;
import com.cpstudio.zhuojiaren.util.CommonUtil;
import com.cpstudio.zhuojiaren.widget.PopupWindows;
import com.cpstudio.zhuojiaren.widget.PullDownView;
import com.cpstudio.zhuojiaren.widget.PullDownView.OnPullDownListener;
import com.utils.FileUtil;

import android.media.AudioManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
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

@SuppressWarnings("deprecation")
public class ChatActivity extends Activity implements OnPullDownListener,
		OnItemClickListener {

	private ImChatFacade mFacade = null;
	private ListView mListView;
	private PullDownView mPullDownView;
	private ImMsgListAdapter mAdapter;
	private ArrayList<ImMsgVO> mList = new ArrayList<ImMsgVO>();
	private VoiceHelper mVoiceHelper = null;
	private PopupWindow mPopupWindow = null;
	private PopupWindows pwh = null;
	private ZhuoConnHelper mConnHelper = null;
	private String userid;
	private String tofollow = "0";
	private String toBlack = "0";
	private Button blackButton;
	private Button followButton;
	private String myid = null;
	private UserFacade userFacade = null;
	private Set<String> showMsg = new HashSet<String>();
	private MsgReceiver msgReceiver = null;
	private EmotionPopHelper ep = null;
	private static final int SELECT_CARD = 100;
	private boolean toTab = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chat);
		Intent intent = getIntent();
		toTab = intent.getBooleanExtra("toTab", false);
		NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		notificationManager.cancelAll();
		userFacade = new UserFacade(ChatActivity.this);
		mConnHelper = ZhuoConnHelper.getInstance(getApplicationContext());
		myid = ResHelper.getInstance(getApplicationContext()).getUserid();
		pwh = new PopupWindows(ChatActivity.this);
		Intent i = getIntent();
		userid = i.getStringExtra("userid");
		boolean addCard = i.getBooleanExtra("card", false);
		TextView title = (TextView) findViewById(R.id.userNameShow);
		mFacade = new ImChatFacade(ChatActivity.this);
		UserVO user = userFacade.getById(userid);
		if (userid.equals("00000000000")) {
			title.setText(getString(R.string.label_servantname));
		} else {
			findViewById(R.id.buttonManage).setVisibility(View.VISIBLE);
			if (user != null) {
				title.setText(user.getUsername());
			} else {//获得聊天用户的信息
				title.setText(userid);
				mConnHelper.getFromServer(ZhuoCommHelper.getUrlUserInfo()
						+ "?uid=" + userid, mUIHandler, MsgTagVO.DATA_OTHER);
			}
		}
		ep = new EmotionPopHelper(ChatActivity.this, getEmotionClickListener());
		mAdapter = new ImMsgListAdapter(this, mList, clickListener,
				longClickListener, sendListener);
		mPullDownView = (PullDownView) findViewById(R.id.pull_down_view);
		mPullDownView.initHeaderViewAndFooterViewAndListView(this,
				R.layout.listview_header);
		mPullDownView.setOnPullDownListener(this);
		mListView = mPullDownView.getListView();
		mListView.setOnItemClickListener(this);
		mListView.setDividerHeight(0);
		mListView.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
		mListView.setAdapter(mAdapter);
		mPullDownView.setShowHeader();
		mPullDownView.setHideFooter(false);
		initClick();
		loadChatData();
		if (addCard) {
			sendChatMsg("card", myid, "");
		}
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
		ResHelper.getInstance(getApplicationContext()).setChatuser(userid);
		if (null == msgReceiver) {
			msgReceiver = new MsgReceiver();
		}
		//只接受指定用户的消息广播
		IntentFilter filter = new IntentFilter("com.cpstudio.userchat."
				+ userid);
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
		ResHelper.getInstance(getApplicationContext()).setChatuser(null);
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
			case MsgTagVO.DATA_OTHER: {//获得聊天对象得信息
				if (msg.obj != null && !msg.obj.equals("")) {
					JsonHandler jsonHandler = new JsonHandler((String) msg.obj);
					UserVO user = jsonHandler.parseUser();
					if (null != user) {
						userFacade.add(user);
					}
				}
				break;
			}
			case MsgTagVO.DATA_MORE: {//添加自己发送的消息到消息列表，尚未发送成功
				if (msg.obj != null && msg.obj instanceof ImMsgVO) {
					ImMsgVO immsg = (ImMsgVO) msg.obj;
					checkShow(immsg);
					mList.add(immsg);
					mAdapter.notifyDataSetChanged();
					mListView.setSelection(mList.size() - 1);
				}
				break;
			}
			case MsgTagVO.PUB_INFO: {//消息发送成功
				Bundle bundle = msg.getData();
				String id = bundle.getString("data");
				ImMsgVO immsg = mFacade.getById(id);
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
				Intent chatIntent = new Intent("com.cpstudio.chatlist");
				sendBroadcast(chatIntent);
				for (int i = 0; i < mList.size(); i++) {
					if (mList.get(i).getId().equals(id)) {
						checkShow(immsg);
						mList.set(i, immsg);
						break;
					}
				}
				mAdapter.notifyDataSetChanged();
				mListView.setSelection(mList.size() - 1);
				break;
			}
			case MsgTagVO.ADD_BACK: {//拉入黑名单
				if (JsonHandler.checkResult((String) msg.obj,
						getApplicationContext())) {
					if (toBlack.equals("0")) {
						blackButton.setText(R.string.label_unblack);
						toBlack = "1";
					} else {
						blackButton.setText(R.string.label_black);
						toBlack = "0";
					}
					CommonUtil.displayToast(getApplicationContext(),
							R.string.label_success);
				}
				break;
			}
			case MsgTagVO.MSG_FOWARD: {//关注
				if (JsonHandler.checkResult((String) msg.obj,
						getApplicationContext())) {
					if (tofollow.equals("0")) {
						followButton.setText(R.string.label_follow);
						tofollow = "1";
					} else {
						tofollow = "0";
						followButton.setText(R.string.label_unfollow);
					}
					CommonUtil.displayToast(getApplicationContext(),
							R.string.label_success);
				}
				break;
			}
			case MsgTagVO.MSG_RCMDUSER: {//操作是否成功
				if (JsonHandler.checkResult((String) msg.obj,
						getApplicationContext())) {
					CommonUtil.displayToast(getApplicationContext(),
							R.string.label_success);
				}
				break;
			}
			}
		}
	};
/**
 * 初始化时加载指定数量的聊天记录
 */
	private void loadChatData() {
		ArrayList<ImMsgVO> list = mFacade
				.getAllByCondition("senderid = ? or receiverid = ?",
						new String[] { userid, userid }, null,
						"addtime desc limit 0,18");
		if (!list.isEmpty()) {
			Collections.reverse(list);
			for (ImMsgVO msg : list) {
				showMsg.add(msg.getId());
			}
			mList.clear();
			checkShow(list, false);
			list.get(0).setShowtime(true);
			mList.addAll(list);
			mAdapter.notifyDataSetChanged();
			mListView.setSelection(mList.size() - 1);
			mFacade.updateReadState(userid);
		}
	}
/**
 * 加载更多聊天记录
 */
	private void loadChatBeforeData() {
		mListView.setTranscriptMode(ListView.TRANSCRIPT_MODE_NORMAL);
		boolean loadState = false;
		int offset = mList.size();
		ArrayList<ImMsgVO> list = mFacade.getAllByCondition(
				"senderid = ? or receiverid = ?",
				new String[] { userid, userid }, null,
				"addtime desc limit 10 offset " + offset);
		if (!list.isEmpty()) {
			loadState = true;
			int position = list.size();
			Collections.reverse(list);
			for (ImMsgVO msg : list) {
				showMsg.add(msg.getId());
			}
			checkShow(list, true);
			list.get(0).setShowtime(true);
			list.addAll(mList);
			mList.clear();
			mList.addAll(list);
			mAdapter.notifyDataSetChanged();
			mListView.setSelection(position);
		}
		mPullDownView.RefreshComplete(loadState);
	}
/**
 * 加载推送过来的新的聊天记录
 */
	private void loadChatAfterData() {
		//"limit 10"????
		ArrayList<ImMsgVO> list = mFacade.getAllByCondition(
				"senderid = ? and isread = ?", new String[] { userid, "0" },
				null, "addtime desc limit 10");
		ArrayList<ImMsgVO> listTemp = new ArrayList<ImMsgVO>();
		for (ImMsgVO msg : list) {
			if (msg.getType().equals("voice") || msg.getType().equals("image")) {
				if (msg.getSavepath() == null || msg.getSavepath().equals("")) {//未下载完。。。
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
			mList.addAll(listTemp);
			mAdapter.notifyDataSetChanged();
			mListView.setSelection(mList.size() - 1);
			mFacade.updateReadState(userid);
		}
	}

	private void checkShow(ArrayList<ImMsgVO> msgs, boolean before) {
		if (!msgs.isEmpty()) {
			if (mList.size() > 1 && !before) {
				ImMsgVO lastMsg = mList.get(mList.size() - 1);
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
//检查设置是否显示时间
	private void checkShow(ImMsgVO msg) {
		if (msg != null) {
			if (mList.size() > 1) {
				ImMsgVO lastMsg = mList.get(mList.size() - 1);
				long interTime = CommonUtil.calcTimeToTime(
						lastMsg.getAddtime(), msg.getAddtime());
				if (interTime / 60000 > 10) {
					msg.setShowtime(true);
				}
			}
		}
	}
/**
 * 添加自己发送的消息到消息列表
 * @param immsg
 */
	private void addChatAfterData(ImMsgVO immsg) {
		Message msg = mUIHandler.obtainMessage(MsgTagVO.DATA_MORE);
		msg.obj = immsg;
		msg.sendToTarget();
	}

	private void changeEditMode(boolean mode) {
		int visible = View.VISIBLE;
		int visible2 = View.GONE;
		if (!mode) {
			visible2 = View.VISIBLE;
			visible = View.GONE;
		}
		findViewById(R.id.linearLayoutChatBg).setVisibility(visible2);
		findViewById(R.id.linearLayoutDel).setVisibility(visible);
	}

	private void sendChatMsg(String type, String str, String secs) {
		ImMsgVO immsg = new ImMsgVO();
		UserVO sender = userFacade.getSimpleInfoById(myid);
		if (sender == null) {
			loadUserInfoBeforeAddToDB(type, str, secs, myid);
			return;
		}
		UserVO receiver = userFacade.getById(userid);
		if (receiver == null) {
			loadUserInfoBeforeAddToDB(type, str, secs, userid);
			return;
		}
		String tempId = System.currentTimeMillis() + "";
		immsg.setId(tempId);
		immsg.setSender(sender);
		immsg.setReceiver(receiver);
		immsg.setType(type);
		immsg.setIsread("4");
		immsg.setAddtime(CommonUtil.getNowTimeStr("yyyy-MM-dd HH:mm:ss"));
		if (type.equals("text")) {
			immsg.setContent(str);
			mConnHelper.chat((String) null, mUIHandler, MsgTagVO.PUB_INFO,
					null, str, type, userid, "", true, null, tempId);
		} else if (type.equals("image")) {
			immsg.setSavepath(str);
			mConnHelper.chat(str, mUIHandler, MsgTagVO.PUB_INFO, null, "",
					type, userid, "", true, null, tempId);
		} else if (type.equals("voice")) {
			immsg.setSavepath(str);
			immsg.setSecs(secs);
			mConnHelper.chat(str, mUIHandler, MsgTagVO.PUB_INFO, null, "",
					type, userid, secs, true, null, tempId);
		} else if (type.equals("emotion")) {
			immsg.setContent(str);
			mConnHelper.chat((String) null, mUIHandler, MsgTagVO.PUB_INFO,
					null, str, type, userid, "", true, null, tempId);
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
			mConnHelper.chat("", mUIHandler, MsgTagVO.PUB_INFO, null, str,
					type, userid, "", true, null, tempId);
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

	private void initClick() {
		findViewById(R.id.buttonBack).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				ChatActivity.this.finish();
			}
		});

		findViewById(R.id.buttonDel).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mAdapter.delSelected();
				changeEditMode(false);
			}
		});
		findViewById(R.id.buttonDelAll).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						mAdapter.delAll();
						changeEditMode(false);
					}
				});

		findViewById(R.id.buttonCancel).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						mAdapter.finishEdit();
						changeEditMode(false);
					}
				});

		findViewById(R.id.buttonManage).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						View view = findViewById(R.id.rootLayout);
						OnClickListener onClickListenerEdit = new OnClickListener() {

							@Override
							public void onClick(View paramView) {
								mAdapter.startEdit();
								changeEditMode(true);
							}
						};
						OnClickListener onClickListenerViewCard = new OnClickListener() {

							@Override
							public void onClick(View paramView) {
								Intent i = new Intent(ChatActivity.this,
										UserCardActivity.class);
								i.putExtra("userid", userid);
								startActivity(i);
							}
						};
						OnClickListener onClickListenerFollow = new OnClickListener() {

							@Override
							public void onClick(View v) {
								followButton = (Button) v;
								mConnHelper.followUser(userid, tofollow,
										mUIHandler, MsgTagVO.MSG_FOWARD, null,
										true, null, null);
							}
						};
						OnClickListener onClickListenerBlack = new OnClickListener() {

							@Override
							public void onClick(View v) {
								blackButton = (Button) v;
								mConnHelper.black(userid, toBlack, mUIHandler,
										MsgTagVO.ADD_BACK, null, true, null,
										null);
							}
						};

						int followInt = R.string.label_unfollow;
						if (tofollow.equals("1")) {
							followInt = R.string.label_follow;
						}
						pwh.showBottomPop(view, new OnClickListener[] {
								onClickListenerEdit, onClickListenerViewCard,
								onClickListenerFollow, onClickListenerBlack },
								new int[] { R.string.label_edit_chatmsg,
										R.string.label_viewcard, followInt,
										R.string.label_black }, 7, "manage");
					}
				});

		findViewById(R.id.buttonChatMore).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
						imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
						View more = findViewById(R.id.linearLayoutChatMore);
						if (more.getVisibility() == View.VISIBLE) {
							v.setSelected(false);
							more.setVisibility(View.GONE);
						} else {
							mListView.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
							v.setSelected(true);
							more.setVisibility(View.VISIBLE);
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
		
		findViewById(R.id.editTextChatText).setOnFocusChangeListener(new OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if(hasFocus){
					mListView.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
					findViewById(R.id.linearLayoutChatMore).setVisibility(
							View.GONE);
					findViewById(R.id.buttonChatMore).setSelected(false);
				}
			}
		});
		
		findViewById(R.id.editTextChatText).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						mListView.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
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
						Intent i = new Intent(ChatActivity.this,
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
										ChatActivity.this);
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
								mPopupWindow = pwh.showRecordDlg(mListView);
								View parent = mPopupWindow.getContentView();
								ll = parent
										.findViewById(R.id.linearLayoutVolume);
								lp = ll.getLayoutParams();
							}
							record.setText(R.string.label_release);
							record.setTextColor(Color.rgb(183, 183, 183));
							record.setBackgroundResource(R.drawable.button_release);
						} else if (event.getAction() == MotionEvent.ACTION_MOVE) {
							if (successStart) {
								float y = event.getY();
								View parent = mPopupWindow.getContentView();
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
							if (null != mPopupWindow) {
								mPopupWindow.dismiss();
							}
							if (successStart && filePath != null) {
								float y = event.getY();
								if (y > 0 && !cancel && mVoiceHelper != null) {
									int secs = mVoiceHelper.finishRecording();
									if (secs > 0) {
										sendChatMsg("voice", filePath,
												String.valueOf(secs));
									}
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
									volumeHeight = ((View) mPopupWindow
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
					if (mVoiceHelper != null && !mVoiceHelper.ismStartPlaying()) {
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

	@Override
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

	@Override
	public void onItemClick(AdapterView<?> arg0, View v, int arg2, long arg3) {
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
		findViewById(R.id.linearLayoutChatMore).setVisibility(View.GONE);
		findViewById(R.id.buttonChatMore).setSelected(false);
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
			new AlertDialog.Builder(ChatActivity.this)
					.setNegativeButton(R.string.CANCEL, null)
					.setItems(
							new String[] { getString(R.string.RESEND) },
							new android.content.DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									switch (which) {
									case 0:
										resend(msgid);
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
			// PopupDialog dialog =
			// PopupDialog.getInstance(v).setCancelable(true);
			// if (state.equals("2")) {
			// OnClickListener resend = new OnClickListener() {
			// @Override
			// public void onClick(View v) {
			// String msgid = (String) view
			// .getTag(R.id.tag_id);
			// ImMsgVO msg = mFacade.getById(msgid);
			// sendChatMsg(msg.getType(), msg.getContent(),
			// msg.getSecs());
			// }
			// };
			// dialog.setItems(new String[] { getString(R.string.RESEND) },
			// new OnClickListener[]{resend});
			// } else {
			// if (type.equals("voice")) {
			// OnClickListener playVoice = new OnClickListener() {
			// @Override
			// public void onClick(View v) {
			// playVoice(view);
			// }
			// };
			// OnClickListener copyVoice = new OnClickListener() {
			// @Override
			// public void onClick(View v) {
			// String newPath = copyFile(view, ".amr");
			// if (newPath != null) {
			// CommonUtil.displayToast(
			// getApplicationContext(),
			// getString(R.string.info80)
			// + newPath);
			// } else {
			// CommonUtil.displayToast(
			// getApplicationContext(),
			// R.string.info81);
			// }
			// }
			// };
			// dialog.setItems(new String[] { getString(R.string.PLAY),
			// getString(R.string.SAVE) },
			// new OnClickListener[]{playVoice,copyVoice});
			// } else if (type.equals("image-network")) {
			// OnClickListener viewNetImage = new OnClickListener() {
			// @Override
			// public void onClick(View v) {
			// viewNetImage(view);
			// }
			// };
			// OnClickListener copyImage = new OnClickListener() {
			// @Override
			// public void onClick(View v) {
			// String newPath = copyFile(view, ".jpg");
			// if (newPath != null) {
			// CommonUtil.displayToast(
			// getApplicationContext(),
			// getString(R.string.info80)
			// + newPath);
			// } else {
			// CommonUtil.displayToast(
			// getApplicationContext(),
			// R.string.info81);
			// }
			// }
			// };
			// dialog.setItems(new String[] { getString(R.string.FULLIMAGE),
			// getString(R.string.SAVE) },
			// new OnClickListener[]{viewNetImage,copyImage});
			// } else if (type.equals("image-local")) {
			// OnClickListener viewLocalImage = new OnClickListener() {
			// @Override
			// public void onClick(View v) {
			// viewLocalImage(view);
			// }
			// };
			// OnClickListener copyImage = new OnClickListener() {
			// @Override
			// public void onClick(View v) {
			// String newPath = copyFile(view, ".jpg");
			// if (newPath != null) {
			// CommonUtil.displayToast(
			// getApplicationContext(),
			// getString(R.string.info80)
			// + newPath);
			// } else {
			// CommonUtil.displayToast(
			// getApplicationContext(),
			// R.string.info81);
			// }
			// }
			// };
			// dialog.setItems(new String[] { getString(R.string.FULLIMAGE),
			// getString(R.string.SAVE) },
			// new OnClickListener[]{viewLocalImage,copyImage});
			// } else if (type.equals("card")) {
			// OnClickListener viewCard = new OnClickListener() {
			// @Override
			// public void onClick(View v) {
			// viewCard(view);
			// }
			// };
			// OnClickListener follow = new OnClickListener() {
			// @Override
			// public void onClick(View v) {
			// String uid = (String) view
			// .getTag(R.id.tag_string);
			// mConnHelper.followUser(uid, "1",
			// mUIHandler,
			// MsgTagVO.MSG_RCMDUSER, null,
			// true, null);
			// }
			// };
			// dialog.setItems(new String[] { getString(R.string.OPEN),
			// getString(R.string.FOLLOW) },
			// new OnClickListener[]{viewCard,follow});
			// } else if (type.equals("emotion")) {
			// return true;
			// } else {
			// OnClickListener copy = new OnClickListener() {
			// @Override
			// public void onClick(View v) {
			// String msgid = (String) view
			// .getTag(R.id.tag_id);
			// ImMsgVO msg = mFacade.getById(msgid);
			// ClipboardManager cbm = (ClipboardManager)
			// getSystemService(Context.CLIPBOARD_SERVICE);
			// cbm.setText(msg.getContent());
			// CommonUtil.displayToast(
			// getApplicationContext(),
			// R.string.info78);
			// }
			// };
			// dialog.setItems(new String[] { getString(R.string.COPY) },
			// new OnClickListener[]{copy});
			// }
			// }
			// dialog.show();
			AlertDialog.Builder adb = new AlertDialog.Builder(ChatActivity.this)
					.setNegativeButton(R.string.CANCEL, null);
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
									resend(msgid);
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
										ImMsgVO msg = mFacade.getById(msgid);
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

	private void resend(String msgid) {
		ImMsgVO msg = mFacade.getById(msgid);
		String type = msg.getType();
		String str = msg.getContent();
		if (type.equals("text") || type.equals("emotion")
				|| type.equals("card")) {
			str = msg.getContent();
		} else if (type.equals("image") || type.equals("voice")) {
			str = msg.getSavepath();
		}
		sendChatMsg(type, str, msg.getSecs());
	}

	private void playVoice(View v) {
		String savePath = (String) v.getTag(R.id.tag_path);
		boolean left = (Boolean) v.getTag(R.id.tag_boolean);
		String msgid = (String) v.getTag(R.id.tag_id);
		if (savePath == null || savePath.equals("")) {
			CommonUtil.displayToast(getApplicationContext(), R.string.error4);
			return;
		}
		if (mVoiceHelper == null) {
			mVoiceHelper = new VoiceHelper(ChatActivity.this);
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
		Intent intent = new Intent(ChatActivity.this,
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
		Intent intent = new Intent(ChatActivity.this,
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
		Intent intent = new Intent(ChatActivity.this, UserCardActivity.class);
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
