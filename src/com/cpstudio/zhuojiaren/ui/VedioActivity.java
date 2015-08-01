package com.cpstudio.zhuojiaren.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;

import com.cpstudio.zhuojiaren.BaseActivity;
import com.cpstudio.zhuojiaren.R;
import com.cpstudio.zhuojiaren.helper.AppClientLef;
import com.cpstudio.zhuojiaren.imageloader.LoadImage;
import com.cpstudio.zhuojiaren.model.GrouthVedio;
import com.cpstudio.zhuojiaren.util.Util;
import com.cpstudio.zhuojiaren.widget.CustomShareBoard;
import com.cpstudio.zhuojiaren.widget.VedioPlayer;

public class VedioActivity extends BaseActivity {
	@InjectView(R.id.avedio_layout)
	View view;
	@InjectView(R.id.avedio_control)
	View controlLayout;
	@InjectView(R.id.avedio_title)
	View titleView;
	@InjectView(R.id.avedio_textureView)
	VedioPlayer vedioPlayer;
	@InjectView(R.id.frameLayout)
	View frame;
	@InjectView(R.id.avedio_bottom)
	View bottom;
	@InjectView(R.id.avedio_bg)
	View bg;
	@InjectView(R.id.avedio_image)
	ImageView Image;
	@InjectView(R.id.avedio_start)
	ImageButton startIV;
	@InjectView(R.id.avedio_next)
	ImageButton Next;
	@InjectView(R.id.avedio_time)
	TextView time;
	@InjectView(R.id.avedio_length)
	TextView length;
	@InjectView(R.id.avedio_sound_seekbar)
	SeekBar soundBar;
	@InjectView(R.id.avedio_vedio_seekbar)
	SeekBar vedioBar;
	@InjectView(R.id.avedio_full_screen)
	ImageView fullScreen;
	@InjectView(R.id.avedio_vedio_time)
	TextView vedioTime;
	@InjectView(R.id.avedio_vedio_name)
	TextView vedioName;
	@InjectView(R.id.avedio_share_inspiration)
	EditText share;
	// 播放器初始化是否完成 保证屏幕旋转后的状态
	boolean init = false;
	// 是否全屏
	static boolean isFullscreen = false;
	Activity activity;
	GrouthVedio vedio;
	// 声音控制
	private AudioManager mAudioManager;
	private int maxVolume, currentVolume;
	private mediac vedioControl = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_vedio);
		ButterKnife.inject(this);
		vedio = (GrouthVedio) getIntent().getSerializableExtra("vedio");
		activity = this;
		//统计观看次数
		submit();
		initTitle();
		title.setText(R.string.grouth_online_detail);
		function.setBackgroundResource(R.drawable.share_hqxq1);
		function.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				CustomShareBoard shareBoard = new CustomShareBoard(VedioActivity.this);
				shareBoard.showAtLocation(VedioActivity.this.getWindow().getDecorView(),
						Gravity.BOTTOM, 0, 0);
			}
		});
		mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
		setVolum();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if(vedio!=null){
			vedioName.setText(vedio.getTutorName());
			vedioName.setText(vedio.getDuration());
			LoadImage load = new LoadImage();
			load.beginLoad(vedio.getImageAddr(), Image);
		}
		if (VedioPlayer.getmCurrentState() != 0) {
			// 初始化time
			init = true;
			length.setText(Util.getTimeString(vedioPlayer.getDuration()));
			time.setText(Util.getTimeString(vedioPlayer.getCurrentPosition()));
			startIV.setBackgroundResource(R.drawable.jjst);
			// 初始化seekbar
			vedioBar.setMax(vedioPlayer.getDuration());
			vedioBar.setProgress(vedioPlayer.getCurrentPosition());
			init = true;
			uiHandler.post(mRunnable);
			visible(!isFullscreen);
			Image.setVisibility(View.GONE);
			vedioPlayer.setVisibility(View.VISIBLE);
			if (isFullscreen) {
				if (vedioControl == null) {
					vedioControl = new mediac(VedioActivity.this, vedioPlayer);
				}
				vedioPlayer.setMediaController(vedioControl);
			}
		} else {
			Image.setBackgroundColor(Color.BLACK);
		}
		InitPlayer();
	}

	private void InitPlayer() {
		// TODO Auto-generated method stub
		if (vedio != null) {
			Util.setText(vedioName, vedio.getTutorName());
			Util.setText(vedioTime, vedio.getDuration());
		}
		// 声音控制
		soundBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				// TODO Auto-generated method stub
				mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC,
						progress, 0);
			}
		});
		Next.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (isFullscreen) {
					visible(false);
				} else {
					visible(true);
				}
			}
		});
		startIV.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (!init) {
					startIV.setEnabled(false);
					vedioPlayer
							.setVideoPath(vedio.getVedioAddr());
					vedioPlayer.setSeekListener(new VedioPlayer.SeekListener() {
						@Override
						public void onSeek(int msec) {
							time.setText(Util.getTimeString(vedioPlayer
									.getCurrentPosition()));

						}

						@Override
						public void onSeekComplete(MediaPlayer mp) {
							time.setText(Util.getTimeString(vedioPlayer
									.getCurrentPosition()));

						}
					});

					vedioPlayer
							.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
								@Override
								public void onPrepared(final MediaPlayer mp) {
									// 完成之后再设置高宽
									Image.setVisibility(View.GONE);
									vedioPlayer.setVisibility(View.VISIBLE);
									frame.setLayoutParams(frame.getLayoutParams());
									vedioPlayer.start();
									// 初始化time
									length.setText(Util
											.getTimeString(vedioPlayer
													.getDuration()));
									time.setText(Util.getTimeString(vedioPlayer
											.getCurrentPosition()));

									// 初始化seekbar
									vedioBar.setMax(vedioPlayer.getDuration());
									vedioBar.setProgress(vedioPlayer
											.getCurrentPosition());
									init = true;
									startIV.setEnabled(true);
									vedioBar.setEnabled(true);
									uiHandler.post(mRunnable);
								}
							});
					startIV.setBackgroundResource(R.drawable.jjst);
					return;
				}
				if (vedioPlayer.isPlaying()) {
					vedioPlayer.pause();
					startIV.setBackgroundResource(R.drawable.jjpl);
				} else {
					vedioPlayer.start();
					startIV.setBackgroundResource(R.drawable.jjst);
				}
			}
		});
		vedioBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onProgressChanged(SeekBar seekBar,
					int progress, boolean fromUser) {
				// TODO Auto-generated method stub
				if (vedioPlayer != null && fromUser)
					vedioPlayer.seekTo(progress);
			}
		});
		fullScreen.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				fullScreen();
			}
		});
	}

	// 更新进度
	private Runnable mRunnable = new Runnable() {

		@Override
		public void run() {
			setVolum();
			if (vedioPlayer != null && vedioPlayer.isPlaying()) {

				// set max value
				int mDuration = vedioPlayer.getDuration();
				vedioBar.setMax(mDuration);
				time.setText(Util.getTimeString(vedioPlayer
						.getCurrentPosition()));
				// set progress to current position
				int mCurrentPosition = vedioPlayer.getCurrentPosition();
				vedioBar.setProgress(mCurrentPosition);
			}
			// repeat above code every second
			uiHandler.postDelayed(this, 10);
		}
	};

	public boolean onKeyDown(int keyCode, android.view.KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (isFullscreen) {
				fullScreen();
				return true;
			} else {
				if (vedioPlayer != null) {
					vedioPlayer.release();
					vedioPlayer = null;
				}
				VedioActivity.this.finish();
			}
		}
		return super.onKeyDown(keyCode, event);
	};

	private Handler uiHandler = new Handler();

	@SuppressLint("InlinedApi")
	private void fullScreen() {
		if (isFullscreen) {
			activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
			// Make the status bar and navigation bar visible again.
			activity.getWindow().getDecorView().setSystemUiVisibility(0);
			DisplayMetrics metric = new DisplayMetrics();
			getWindowManager().getDefaultDisplay().getMetrics(metric);

			frame.setLayoutParams(frame.getLayoutParams());
			frame.requestLayout();
			quitFullScreen();
			vedioPlayer.setMediaController(null);
			isFullscreen = false;
		} else {
			activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

			activity.getWindow()
					.getDecorView()
					.setSystemUiVisibility(
							View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
									| View.SYSTEM_UI_FLAG_FULLSCREEN);
			DisplayMetrics metric = new DisplayMetrics();
			getWindowManager().getDefaultDisplay().getMetrics(metric);
			VedioPlayer.getLayoutParamsBasedOnParent(frame, metric.widthPixels,
					metric.heightPixels);
			setFullScreen();
			isFullscreen = true;
		}
	}

	private void visible(boolean visivle) {
		if (visivle) {
			bg.setVisibility(View.VISIBLE);
			bottom.setVisibility(View.VISIBLE);
			vedioName.setVisibility(View.VISIBLE);
			vedioTime.setVisibility(View.VISIBLE);
			titleView.setVisibility(View.VISIBLE);
		} else {
			bg.setVisibility(View.GONE);
			bottom.setVisibility(View.GONE);
			vedioName.setVisibility(View.GONE);
			vedioTime.setVisibility(View.GONE);
			titleView.setVisibility(View.GONE);
		}
	}

	private void setFullScreen() {
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
	}

	// 退出全屏函数：

	private void quitFullScreen() {
		getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
	}

	private void setVolum() {

		maxVolume = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
		soundBar.setMax(maxVolume);
		currentVolume = mAudioManager
				.getStreamVolume(AudioManager.STREAM_MUSIC);
		soundBar.setProgress(currentVolume);
	}

	public class mediac extends MediaController {
		public mediac(Context context, View anchor) {
			super(context);
			super.setAnchorView(anchor);
		}

		@Override
		public void setAnchorView(View view) {
		}
	}
	private void submit() {
		// TODO Auto-generated method stub
		if(vedio!=null&&vedio.getId()!=null){
			AppClientLef.getInstance(this).submitVedio(vedio.getId());
		}
	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if (vedioPlayer != null) {
			vedioPlayer.release();
			vedioPlayer = null;
		}
		VedioActivity.this.finish();
	}
}
