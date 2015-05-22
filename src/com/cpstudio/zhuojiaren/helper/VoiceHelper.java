package com.cpstudio.zhuojiaren.helper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import com.cpstudio.zhuojiaren.R;
import com.cpstudio.zhuojiaren.helper.AsyncConnectHelper.FinishCallback;
import com.cpstudio.zhuojiaren.util.AudioCombineHelper;
import com.cpstudio.zhuojiaren.util.CommonUtil;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaRecorder;
import android.os.Handler;
import android.os.Message;
import android.view.View;

public class VoiceHelper implements SensorEventListener {
	private Context mContext;
	private String mFileName = null;
	private String mTag = null;
	private MediaRecorder mRecorder = null;
	private MediaPlayer mPlayer = null;
	private boolean mStartPlaying = true;
	private boolean mStartRecording = true;
	private Timer playingTimer = null;
	private Timer recordingTimer = null;
	private TimerTask playingTask = null;
	private TimerTask recordingTask = null;
	private Handler playingHandler = null;
	private Handler recordingHandler = null;
	private static String SUFFIX = ".amr";
	private PlayingListener mPlayingListener = null;
	private RecordingListener mRecordingListener = null;
	private boolean auto = true;
	private long recordTime = 0;
	private AudioManager am;
	private boolean mVoiceMode = false;
	private boolean autoVoiceMode = true;
	private int mPlayingPosition = 0;
	private SensorManager sm = null;
	private View playingView = null;

	public VoiceHelper(Context context) {
		this.mContext = context;
		sm = (SensorManager) mContext.getSystemService(Context.SENSOR_SERVICE);
		// sm.unregisterListener(this);
		sm.registerListener(this, sm.getDefaultSensor(Sensor.TYPE_PROXIMITY),
				SensorManager.SENSOR_DELAY_GAME);
		am = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
		playingTimer = new Timer();
		initPlayingHandler();
		recordingTimer = new Timer();
		initRecordingHandler();
	}

	public boolean isAuto() {
		return auto;
	}

	public void setAuto(boolean auto) {
		this.auto = auto;
	}

	public boolean ismStartPlaying() {
		return mStartPlaying;
	}

	public void setmStartPlaying(boolean mStartPlaying) {
		this.mStartPlaying = mStartPlaying;
	}

	public boolean ismStartRecording() {
		return mStartRecording;
	}

	public void setmStartRecording(boolean mStartRecording) {
		this.mStartRecording = mStartRecording;
	}

	public String getmFileName() {
		return mFileName;
	}

	public void setmFileName(String fileName) {
		mFileName = fileName;
	}

	public String getmTag() {
		return mTag;
	}

	public void setmTag(String mTag) {
		this.mTag = mTag;
	}

	public View getPlayingView() {
		return playingView;
	}

	public void setPlayingView(View playingView) {
		this.playingView = playingView;
	}

	public String newRecord(String fileName) {
		try {
			release();
			mFileName = ResHelper.getInstance(mContext).getVoicePath()
					+ fileName + SUFFIX;
			if (!new File(new File(mFileName).getParent()).exists()) {
				new File(new File(mFileName).getParent()).mkdirs();
			}
			return mFileName;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public boolean recording(RecordingListener recordingListener) {
		try {
			// if(this.mRecordingListener == null){
			this.mRecordingListener = recordingListener;
			// }
			return onRecord(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public int delRecord() {
		if (!mStartRecording) {
			return R.string.error6;
		}
		if (!mStartPlaying) {
			return R.string.error7;
		}
		try {
			if (new File(mFileName).delete()) {
				return R.string.info12;
			} else {
				return R.string.error8;
			}
		} catch (Exception e) {
			return R.string.error8;
		}
	}

	public String newPlay(String fileName) {
		try {
			release();
			mPlayingPosition = 0;
			mFileName = fileName;// mPreferenceHelper.getVoicePath() + fileName
									// + SUFFIX;
			return mFileName;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public boolean playing(PlayingListener playingListener) {
		return playing(playingListener, false);
	}

	public boolean playing(PlayingListener playingListener, boolean voiceMode) {
		try {
			// if(this.mPlayingListener == null){
			this.mVoiceMode = voiceMode;
			this.mPlayingListener = playingListener;
			// }
			return onPlay(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public boolean finishPlaying() {
		return onPlay(false);
	}

	public int finishRecording() {
		int second = 0;
		if (onRecord(false)) {
			if (recordTime >= 1000) {
				second = (int) (recordTime / 1000);
				if (auto) {
					// sendFile(mFileName, callback);
				}
			}
			recordTime = 0;
			mFileName = null;
		}
		return second;
	}

	public void pauseRecording() {
		onRecord(false);
	}

	public boolean finishRecordingAndSave(String fileName,
			ArrayList<String> tempFiles) {
		if (onRecord(false)) {
			AudioCombineHelper ach = new AudioCombineHelper();
			ach.getInputCollection(tempFiles, fileName);
			recordTime = 0;
			mFileName = null;
			return true;
		}
		return false;
	}

	public int gettVoiceFileLength(String file) {
		try {
			mPlayer = new MediaPlayer();
			mPlayer.setDataSource(file);
			mPlayer.prepare();
			return mPlayer.getDuration();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		mPlayer.release();
		mPlayer = null;
		return 0;
	}

	public void seekTo(int progress) {
		if (mPlayer != null) {
			mPlayer.seekTo(progress * 250);
		}
	}

	private boolean onRecord(boolean start) {
		if (start) {
			return startRecording();
		} else {
			return stopRecording();
		}
	}

	@SuppressWarnings("deprecation")
	private boolean startRecording() {
		try {
			if (!mStartPlaying) {
				stopPlaying();
			}
			mRecorder = new MediaRecorder();
			mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
			mRecorder.setOutputFormat(MediaRecorder.OutputFormat.RAW_AMR);
			mRecorder.setOutputFile(mFileName);
			mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
			mRecorder.setAudioEncodingBitRate(5600);
			mRecorder.prepare();
			mRecorder.start();
			initRecordingTask();
			recordingTimer.schedule(recordingTask, 0, 100);
			mStartRecording = false;
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			CommonUtil.displayToast(mContext, R.string.error9);
			mRecorder = null;
			return false;
		}
	}

	private boolean stopRecording() {
		if (null != mRecorder) {
			try {
				recordingTask.cancel();
				recordingTimer.purge();
				mRecorder.stop();
				mRecorder.release();
				mRecorder = null;
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
		}
		// mFileName = null;
		mStartRecording = true;
		return true;
	}

	private boolean onPlay(boolean start) {
		if (start) {
			return startPlaying();
		} else {
			return stopPlaying();
		}
	}

	private boolean startPlaying() {
		try {
			if (mVoiceMode) {
				am.setSpeakerphoneOn(false);
				((Activity) mContext)
						.setVolumeControlStream(AudioManager.STREAM_VOICE_CALL);
				am.setMode(AudioManager.MODE_IN_CALL);
			}
			mPlayer = new MediaPlayer();
			mPlayer.setDataSource(mFileName);
			mPlayer.prepare();
			mPlayer.start();
			mPlayer.setOnCompletionListener(new OnCompletionListener() {
				@Override
				public void onCompletion(MediaPlayer mp) {
					stopPlaying();
				}
			});
			initPlayingTask();
			playingTimer.schedule(playingTask, 0, 250);
			mStartPlaying = false;
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	private boolean stopPlaying() {
		if (mPlayer != null) {
			try {
				mPlayingPosition = 0;
				am.setMode(AudioManager.MODE_NORMAL);
				playingTask.cancel();
				playingTimer.purge();
				mPlayer.release();
				mPlayer = null;
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
		}
		// mFileName = null;
		mStartPlaying = true;
		mPlayingListener.onStop();
		return true;
	}

	@SuppressLint("HandlerLeak")
	private void initPlayingHandler() {
		playingHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				try {
					if (null != mPlayingListener && null != mPlayer) {
						int pos = mPlayer.getCurrentPosition() / 250;
						mPlayingPosition = pos;
						mPlayingListener.onPlaying(pos);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		};
	}

	private void initPlayingTask() {
		playingTask = new TimerTask() {
			@Override
			public void run() {
				Message message = new Message();
				message.what = 1;
				playingHandler.sendMessage(message);
			}
		};
	}

	@SuppressLint("HandlerLeak")
	private void initRecordingHandler() {
		recordingHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				try {
					if (null != mRecorder && null != mRecordingListener) {
						mRecordingListener.onRecording(
								mRecorder.getMaxAmplitude(), recordTime);
						recordTime += 100;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		};
	}

	private void initRecordingTask() {
		recordingTask = new TimerTask() {
			@Override
			public void run() {
				Message message = new Message();
				message.what = 1;
				recordingHandler.sendMessage(message);
			}
		};
	}

	public void release() {
		if (mRecorder != null) {
			mRecorder.release();
			mRecorder = null;
		}
		if (mPlayer != null) {
			mPlayer.release();
			mPlayer = null;
		}
	}

	public void sendFile(String fileName, FinishCallback callback) {
		if (fileName != null) {
			int nextworkState = CommonUtil.getNetworkState(mContext);
			if (nextworkState == 0) {

			} else {
				if (nextworkState == 1) {
					CommonUtil.displayToast(mContext, R.string.info11);
				} else {
					CommonUtil.startNetWorkDialog(mContext);
				}
			}
		} else {
			CommonUtil.displayToast(mContext, R.string.error4);
		}
	}

	public interface PlayingListener {
		public abstract void onPlaying(int pos);

		public abstract void onStop();
	}

	public interface RecordingListener {
		public abstract void onRecording(int volume, long recordingTime);
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {

	}

	@Override
	public void onSensorChanged(SensorEvent e) {
		if (!autoVoiceMode) {
			return;
		}
		if (e.values[0] < 5) {
			this.mVoiceMode = true;
		} else {
			this.mVoiceMode = false;
		}
		if (!mStartPlaying && mStartRecording) {
			int pp = mPlayingPosition;
			stopPlaying();
			startPlaying();
			seekTo(pp);
		}
	}
}
