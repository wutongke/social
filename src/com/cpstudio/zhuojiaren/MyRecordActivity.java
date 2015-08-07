package com.cpstudio.zhuojiaren;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.cpstudio.zhuojiaren.adapter.MyRecordListAdapter;
import com.cpstudio.zhuojiaren.facade.RecordChatFacade;
import com.cpstudio.zhuojiaren.facade.RecordFacade;
import com.cpstudio.zhuojiaren.facade.UserFacade;
import com.cpstudio.zhuojiaren.helper.AsyncConnectHelperLZ.FinishCallback;
import com.cpstudio.zhuojiaren.helper.JsonHandler;
import com.cpstudio.zhuojiaren.helper.VoiceHelper;
import com.cpstudio.zhuojiaren.helper.VoiceHelper.PlayingListener;
import com.cpstudio.zhuojiaren.helper.VoiceHelper.RecordingListener;
import com.cpstudio.zhuojiaren.helper.ZhuoCommHelper;
import com.cpstudio.zhuojiaren.helper.ZhuoConnHelper;
import com.cpstudio.zhuojiaren.model.ImMsgVO;
import com.cpstudio.zhuojiaren.model.MsgTagVO;
import com.cpstudio.zhuojiaren.model.RecordVO;
import com.cpstudio.zhuojiaren.model.UserVO;
import com.cpstudio.zhuojiaren.util.CommonUtil;
import com.cpstudio.zhuojiaren.widget.PopupWindows;

public class MyRecordActivity extends Activity {
	private ListView mListView;
	private VoiceHelper mVoiceHelper = null;
	private ArrayList<String> mTempfiles = new ArrayList<String>();
	private String mFileName = null;
	private TextView recordTime;
	private int nowRecordTime = 0;
	private PopupWindows pwh = null;
	private MyRecordListAdapter mAdapter = null;
	private ArrayList<RecordVO> mList = new ArrayList<RecordVO>();
	private RecordFacade mFacade = null;
	private static final int USER_SELECT = 0;
	private ZhuoConnHelper mConnHelper = null;
	private String forwardId = null;
	private ArrayList<String> mSelectlist = new ArrayList<String>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_record);
		mFacade = new RecordFacade(getApplicationContext());
		mConnHelper = ZhuoConnHelper.getInstance(getApplicationContext());
		recordTime = (TextView) findViewById(R.id.textViewRecordTime);
		mListView = (ListView) findViewById(R.id.listView);
		mAdapter = new MyRecordListAdapter(MyRecordActivity.this, mList,
				playingListener, forwardListener);
		mListView.setAdapter(mAdapter);
		pwh = new PopupWindows(MyRecordActivity.this);
		mVoiceHelper = new VoiceHelper(MyRecordActivity.this);
		initClick();
		loadData();
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

	@Override
	protected void onResume() {
		if (null == mVoiceHelper) {
			mVoiceHelper = new VoiceHelper(MyRecordActivity.this);
		}
		super.onResume();
	}

	private String getFileSize(File file) {
		String filesize = null;
		if (file.length() > 1024 * 1024) {
			filesize = file.length() / 1024 / 1024 + "MB";
		} else if (file.length() > 1024) {
			filesize = file.length() / 1024 + "KB";
		} else {
			filesize = file.length() + "B";
		}
		return filesize;
	}

	private void loadData() {
		ArrayList<RecordVO> list = mFacade.getAllNotDelete();
		if (!list.isEmpty()) {
			Collections.reverse(list);
			mList.clear();
			mList.addAll(list);
			mAdapter.notifyDataSetChanged();
		}
	}

	private OnClickListener playingListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			String filePath = (String) v.getTag(R.id.tag_path);
			if (!mVoiceHelper.ismStartPlaying()) {
				View playingView = mVoiceHelper.getPlayingView();
				if (playingView != null) {
					playingView
							.setBackgroundResource(R.drawable.ico_start_play);
				}
				mVoiceHelper.finishPlaying();
				if (mVoiceHelper.getmTag().equals(filePath)) {
					mVoiceHelper.setPlayingView(null);
					return;
				}
			}
			mVoiceHelper.setPlayingView(v);
			v.setBackgroundResource(R.drawable.ico_pause_play);
			int max = (Integer) v.getTag(R.id.tag_int);
			final SeekBar seekBar = (SeekBar) ((View) v.getParent())
					.findViewById(R.id.seekBarRecord);
			seekBar.setMax(max);
			mVoiceHelper.newPlay(filePath);
			mVoiceHelper.setmTag(filePath);
			seekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
				@Override
				public void onStopTrackingTouch(SeekBar seekBar) {
				}

				@Override
				public void onStartTrackingTouch(SeekBar seekBar) {
				}

				@Override
				public void onProgressChanged(SeekBar seekBar, int progress,
						boolean fromUser) {
					if (fromUser) {
						mVoiceHelper.seekTo(progress * 4);
					}
				}
			});
			PlayingListener listener = null;
			listener = new PlayingListener() {
				@Override
				public void onPlaying(int pos) {
					if (!mVoiceHelper.ismStartPlaying()) {
						seekBar.setProgress(pos / 4);
					}
				}

				@Override
				public void onStop() {
					mVoiceHelper.getPlayingView().setBackgroundResource(
							R.drawable.ico_start_play);
					seekBar.setProgress(0);
					if (mVoiceHelper != null) {
						mVoiceHelper.release();
					}
				}
			};
			mVoiceHelper.playing(listener, false);
		}
	};

	private OnClickListener forwardListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			forwardId = (String) v.getTag(R.id.tag_id);
			Intent i = new Intent(MyRecordActivity.this,
					UserSelectActivity.class);
			startActivityForResult(i, USER_SELECT);
		}
	};

	private boolean startRecording() {
		String currentTime = getRecordName();
		((TextView) findViewById(R.id.textViewName)).setText(currentTime);
		((TextView) findViewById(R.id.textViewDate)).setText(CommonUtil
				.getNowTimeStr("yyyy-MM-dd"));
		String savePath = "temp/" + currentTime + mTempfiles.size();
		String filePath = mVoiceHelper.newRecord(savePath);
		if (mFileName == null) {
			mTempfiles.clear();
			mFileName = filePath.replace(savePath, currentTime);
		}
		mTempfiles.add(filePath);
		if (filePath == null) {
			CommonUtil.displayToast(getApplicationContext(), R.string.error9);
			return false;
		} else {
			mVoiceHelper.recording(new RecordingListener() {

				@Override
				public void onRecording(int volume, long time) {
					nowRecordTime = (int) (time / 1000);
					recordTime.setText(CommonUtil.getMSTime(nowRecordTime));
				}
			});
			return true;
		}
	}

	private String getRecordName() {
		String name = null;
		int i = 0;
		while (true) {
			i++;
			name = getString(R.string.label_new_record) + i;
			if (mFacade.getByCondition("name = ?", new String[] { name }, null,
					null, null).isEmpty()) {
				break;
			}
		}
		return name;
	}

	private boolean pauseRecording() {
		if (mVoiceHelper == null) {
			return false;
		} else {
			mVoiceHelper.pauseRecording();
			return true;
		}
	}

	private void stopRecording() {
		if (mVoiceHelper != null && mFileName != null) {
			TextView nameTV = (TextView) findViewById(R.id.textViewName);
			mVoiceHelper.finishRecordingAndSave(mFileName, mTempfiles);
			((ToggleButton) findViewById(R.id.toggleButtonRecord))
					.setChecked(false);
			pwh.showPopDlgEdit(findViewById(R.id.rootLayout),
					saveRecordListener, cancelSaveListener,
					R.string.label_new_name, nameTV.getText().toString());
			nameTV.setText("");
			((TextView) findViewById(R.id.textViewDate)).setText("");
		}

	}

	private OnClickListener saveRecordListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			String name = (String) v.getTag();
			File file = new File(mFileName);
			String filesize = getFileSize(file);
			String filetime = CommonUtil.getTime(file.lastModified());
			String tempid = String.valueOf(System.currentTimeMillis());
			String length = recordTime.getText().toString();
			RecordVO item = new RecordVO();
			item.setId(tempid);
			item.setPath(mFileName);
			item.setName(name);
			item.setSize(filesize);
			item.setDate(filetime);
			item.setLength(length);
			item.setUsers("");
			item.setState("2");// 0已删除 1已发送 2发送中 3发送失败
			mFacade.insert(item);
			mList.add(0, item);
			mAdapter.notifyDataSetChanged();
			recordTime.setText("00:00");
			mConnHelper.chat(mFileName, mUIHandler, MsgTagVO.PUB_INFO,
					MyRecordActivity.this, "", "cloudvoice", "",
					String.valueOf(CommonUtil.returnMStimeInt(length)), true,
					null, tempid);
			mFileName = null;
		}
	};

	private OnClickListener cancelSaveListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			mFileName = null;
			recordTime.setText("00:00");
		}
	};

	private void initClick() {
		findViewById(R.id.buttonBack).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						MyRecordActivity.this.finish();
					}
				});
		((ToggleButton) findViewById(R.id.toggleButtonRecord))
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton v,
							boolean isChecked) {
						if (isChecked) {
							if (!startRecording()) {
								v.setChecked(false);
							}
						} else {
							pauseRecording();
						}
					}
				});
		findViewById(R.id.buttonStopRecord).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						stopRecording();
					}
				});
	}

	@SuppressLint("HandlerLeak")
	private Handler mUIHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MsgTagVO.PUB_INFO: {
				Bundle bundle = msg.getData();
				String id = bundle.getString("data");
				RecordVO immsg = mFacade.getById(id);
				if (JsonHandler.checkResult((String) msg.obj,
						getApplicationContext())) {
					String serverId = JsonHandler
							.getSingleResult((String) msg.obj);
					if (serverId != null && !serverId.equals("0")
							&& !serverId.equals("")) {
						immsg.setState("1");
						immsg.setId(serverId);
						mFacade.updateWithNewId(immsg, id);
					} else {
						immsg.setState("3");
						mFacade.update(immsg);
					}
				} else {
					immsg.setState("3");
					mFacade.update(immsg);
				}
				for (int i = 0; i < mList.size(); i++) {
					if (mList.get(i).getId().equals(id)) {
						mList.set(i, immsg);
						break;
					}
				}
				mAdapter.notifyDataSetChanged();
				break;
			}
			case MsgTagVO.MSG_FOWARD:
				if (JsonHandler.checkResult((String) msg.obj,
						getApplicationContext())) {
					Bundle bundle = msg.getData();
					String fid = bundle.getString("fid");
					String forwardIds = bundle.getString("forwardIds");
					String forwardUsers = bundle.getString("forwardUsers");
					String[] fids = forwardIds.split(";");
					RecordVO item = mFacade.getById(fid);
					String users = item.getUsers();
					String usernames = "";
					if (users != null && !users.equals("")) {
						ArrayList<String> u = new ArrayList<String>();
						if (forwardUsers.indexOf("、") != -1) {
							String[] names = forwardUsers.split("、");
							u = new ArrayList<String>(Arrays.asList(names));
						} else {
							u.add(forwardUsers);
						}
						if (users.indexOf("、") != -1) {
							String[] user = users.split("、");
							ArrayList<String> us = new ArrayList<String>(
									Arrays.asList(user));
							for (String str : us) {
								if (!u.contains(str)) {
									u.add(str);
								}
							}
						} else {
							if (!u.contains(users)) {
								u.add(users);
							}
						}
						for (String name : u) {
							usernames += name + "、";
						}
						usernames = ZhuoCommHelper.subLast(usernames);
					} else {
						usernames = forwardUsers;
					}
					item.setUsers(usernames);
					mFacade.update(item);
					RecordChatFacade facade = new RecordChatFacade(
							getApplicationContext());
					UserFacade userFacade = new UserFacade(
							getApplicationContext());
					for (String id : fids) {
						ImMsgVO immsg = new ImMsgVO();
						immsg.setAddtime(CommonUtil
								.getNowTimeStr("yyyy-MM-dd HH:mm:ss"));
						immsg.setId(String.valueOf(System.currentTimeMillis()));
						immsg.setIsread("1");
//						UserVO receiver = userFacade.getSimpleInfoById(id);
//						immsg.setReceiver(receiver);
//						immsg.setSavepath(item.getPath());
//						immsg.setSecs(item.getLength());
//						UserVO sender = userFacade.getMySimpleInfo();
//						immsg.setSender(sender);
//						immsg.setType("cloudvoice");
//						immsg.setContent(usernames);
//						facade.insert(immsg);
					}
					for (int i = 0; i < mList.size(); i++) {
						if (mList.get(i).getId().equals(forwardId)) {
							mList.set(i, item);
							break;
						}
					}
					mAdapter.notifyDataSetChanged();
				}
				break;
			}
		}
	};

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == Activity.RESULT_OK) {
			switch (requestCode) {
			case USER_SELECT:
				mSelectlist = data.getStringArrayListExtra("ids");
				ArrayList<String> names = data.getStringArrayListExtra("names");
				if (mSelectlist != null && mSelectlist.size() > 0) {
					String useridlist = "";
					String usernamelist = "";
					for (String id : mSelectlist) {
						useridlist += id + ";";
					}
					for (String name : names) {
						usernamelist += name + "、";
					}
					final String forwardIds = ZhuoCommHelper
							.subLast(useridlist);
					final String forwardUsers = ZhuoCommHelper
							.subLast(usernamelist);
					final String fid = forwardId;
					FinishCallback callback = new FinishCallback() {
						@Override
						public boolean onReturn(String rs, int responseCode) {
							Message msg = mUIHandler
									.obtainMessage(MsgTagVO.MSG_FOWARD);
							Bundle bundle = new Bundle();
							bundle.putString("fid", fid);
							bundle.putString("forwardIds", forwardIds);
							bundle.putString("forwardUsers", forwardUsers);
							msg.setData(bundle);
							msg.obj = rs;
							msg.sendToTarget();
							return false;
						}
					};
					mConnHelper.shareCloud(fid, forwardIds, callback,
							MyRecordActivity.this, true, null);
				}
				break;
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
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
		return super.onKeyDown(keyCode, event);
	}
}
