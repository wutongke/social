package com.cpstudio.zhuojiaren;

import java.util.ArrayList;
import java.util.Collections;
import com.cpstudio.zhuojiaren.adapter.RecordUserListAdapter;
import com.cpstudio.zhuojiaren.facade.RecordChatFacade;
import com.cpstudio.zhuojiaren.helper.VoiceHelper;
import com.cpstudio.zhuojiaren.helper.VoiceHelper.PlayingListener;
import com.cpstudio.zhuojiaren.model.ImMsgVO;
import android.media.AudioManager;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;

public class RecordUserActivity extends Activity {

	private ListView mListView;
	private RecordUserListAdapter mAdapter = null;
	private VoiceHelper mVoiceHelper = null;
	private RecordChatFacade mFacade = null;
	private String userid = null;
	private ArrayList<ImMsgVO> mList = new ArrayList<ImMsgVO>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_record_user);
		Intent i = getIntent();
		userid = i.getStringExtra("userid");
		mFacade = new RecordChatFacade(getApplicationContext());
		mVoiceHelper = new VoiceHelper(RecordUserActivity.this);
		mListView = (ListView) findViewById(R.id.listView);
		mAdapter = new RecordUserListAdapter(RecordUserActivity.this, mList,
				playingListener);
		mListView.setAdapter(mAdapter);
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
			mVoiceHelper = new VoiceHelper(RecordUserActivity.this);
		}
		super.onResume();
	}

	private void loadData() {
		ArrayList<ImMsgVO> list = mFacade.getAllByCondition(
				"senderid = ? or receiverid = ?",
				new String[] { userid, userid }, null,
				"length(id) desc,id desc limit 0,3");
		if (!list.isEmpty()) {
			Collections.reverse(list);
			mList.clear();
			mList.addAll(list);
			mAdapter.notifyDataSetChanged();
			mListView.setSelection(mList.size());
			mFacade.updateReadState(userid);
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

	private void initClick() {
		findViewById(R.id.buttonBack).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						RecordUserActivity.this.finish();
					}
				});
		findViewById(R.id.buttonChat).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i = new Intent(RecordUserActivity.this,
						ChatActivity.class);
				i.putExtra("userid", userid);
				startActivity(i);
			}
		});
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
