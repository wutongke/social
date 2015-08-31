package com.cpstudio.zhuojiaren.ui;

import java.io.IOException;

import android.app.Activity;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;

import com.cpstudio.zhuojiaren.BaseActivity;
import com.cpstudio.zhuojiaren.R;
import com.cpstudio.zhuojiaren.imageloader.LoadImage;
import com.cpstudio.zhuojiaren.model.RecordVO;
import com.cpstudio.zhuojiaren.util.CommonUtil;

public class AudioDetailActivity extends BaseActivity {
	@InjectView(R.id.aad_duration)
	TextView duration;
	@InjectView(R.id.aad_more_inspiration)
	Button moreInspriration;
	@InjectView(R.id.aad_name)
	TextView name;
	@InjectView(R.id.aad_start)
	ImageView playImage;
	@InjectView(R.id.aad_play)
	ImageView playImage2;
	@InjectView(R.id.aad_seekbar)
	SeekBar seekBar;
	@InjectView(R.id.aad_share_inspiration)
	EditText shareInspriration;
	@InjectView(R.id.aad_adv)
	ImageView advertisementIamge;
	MediaPlayer mediaPlayer;
	RecordVO record;
	private volatile boolean isPlaying = false;
	String mId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_audio_detail);
		ButterKnife.inject(this);
		initTitle();
		record = (RecordVO) getIntent().getSerializableExtra("audio");
		title.setText(R.string.zhuo_audio);
		mId = getIntent().getStringExtra("id");
		playImage.setBackgroundResource(R.drawable.jjplay);
		playImage2.setBackgroundResource(R.drawable.jjplay3);
		loadData();
		initOnclick();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if (mediaPlayer != null)
			stop();
	}

	private void initOnclick() {
		// TODO Auto-generated method stub
		playImage.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (record != null) {
					if (mediaPlayer != null) {
						// ��ͣ
						if (mediaPlayer.isPlaying()) {
							mediaPlayer.pause();
							playImage.setBackgroundResource(R.drawable.jjplay);
							playImage2
									.setBackgroundResource(R.drawable.jjplay3);
						} else {
							// ����
							mediaPlayer.start();
							playImage.setBackgroundResource(R.drawable.jjstop);
							playImage2
									.setBackgroundResource(R.drawable.ico_voice);
						}

					}

					else {
						mediaPlayer = new MediaPlayer();
						// give data to mediaPlayer
						try {
							if (record.getAudioAddr() != null)
								mediaPlayer.setDataSource(record.getAudioAddr());
							else {
								CommonUtil.displayToast(
										AudioDetailActivity.this,
										R.string.error4);
								return;
							}

							// media player asynchronous preparation
							mediaPlayer.prepareAsync();
							// execute this code at the end of asynchronous
							// media player
							// preparation
							mediaPlayer
									.setOnPreparedListener(new OnPreparedListener() {
										public void onPrepared(
												final MediaPlayer mp) {
											playImage
													.setBackgroundResource(R.drawable.jjstop);
											playImage2
													.setBackgroundResource(R.drawable.ico_voice);
											isPlaying = true;
											// start media player
											mp.start();

											uiHandler.post(mRunnable);

										}
									});
						} catch (IllegalArgumentException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (SecurityException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IllegalStateException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			}
		});
	}

	private void loadData() {
		// TODO Auto-generated method stub
//		new LoadImage().beginLoad(
//				"http://pic6.nipic.com/20100404/4635053_162100094928_2.jpg",
//				advertisementIamge);
	}

	Handler uiHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			RecordVO res = (RecordVO) msg.obj;
			record = res;
			name.setText(res.getName());
			playImage.setBackgroundResource(R.drawable.audio_start);
		};
	};
	private Runnable mRunnable = new Runnable() {

		@Override
		public void run() {
			if (mediaPlayer != null && isPlaying) {

				// set max value
				int mDuration = mediaPlayer.getDuration();
				seekBar.setMax(mDuration);
				duration.setText(getTimeString(mDuration));
				// set progress to current position
				int mCurrentPosition = mediaPlayer.getCurrentPosition();
				seekBar.setProgress(mCurrentPosition);

				// handle drag on seekbar
				seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

					@Override
					public void onStopTrackingTouch(SeekBar seekBar) {

					}

					@Override
					public void onStartTrackingTouch(SeekBar seekBar) {

					}

					@Override
					public void onProgressChanged(SeekBar seekBar,
							int progress, boolean fromUser) {
						if (mediaPlayer != null && fromUser) {
							mediaPlayer.seekTo(progress);
						}
					}
				});

			}
			// repeat above code every second
			uiHandler.postDelayed(this, 10);
		}
	};

	// ���ݺ���ֵ��ȡʱ����
	private String getTimeString(long millis) {
		StringBuffer buf = new StringBuffer();

		long hours = millis / (1000 * 60 * 60);
		long minutes = (millis % (1000 * 60 * 60)) / (1000 * 60);
		long seconds = ((millis % (1000 * 60 * 60)) % (1000 * 60)) / 1000;

		buf.append(String.format("%02d", hours)).append(":")
				.append(String.format("%02d", minutes)).append(":")
				.append(String.format("%02d", seconds));

		return buf.toString();
	}

	public void stop() {
		if (mediaPlayer != null) {
			mediaPlayer.stop();
			mediaPlayer.release();
			mediaPlayer = null;
		}
	}
}
