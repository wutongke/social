package com.cpstudio.zhuojiaren.adapter;

import java.io.IOException;
import java.util.List;

import android.content.Context;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.SeekBar;

import com.cpstudio.zhuojiaren.R;
import com.cpstudio.zhuojiaren.model.RecordVO;
import com.cpstudio.zhuojiaren.util.CommonAdapter;
import com.cpstudio.zhuojiaren.util.ViewHolder;

public class AudioAdapter extends CommonAdapter<RecordVO> {
	private MediaPlayer mediaPlayer;
	private SeekBar seekBar;
	private View curPlayView;
	private volatile boolean isPlaying = false;

	public AudioAdapter(Context context, List<RecordVO> mDatas, int itemLayoutId) {
		super(context, mDatas, itemLayoutId);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void convert(final ViewHolder helper, final RecordVO item) {
		// TODO Auto-generated method stub
		helper.setText(R.id.ir_name, item.getName());
		helper.setText(
				R.id.ir_creater,
				mContext.getResources().getString(R.string.creater)
						+ item.getUsers());
		helper.setText(
				R.id.ir_time,
				mContext.getResources().getString(R.string.create_time)
						+ item.getDate());
		helper.setText(R.id.ir_duration, item.getLength());
		final ImageView play = (ImageView) helper.getView(R.id.ir_play);
		play.setBackgroundResource(R.drawable.jjplay2);
		if (play.getTag() != null) {
			play.setBackgroundResource(R.drawable.ico_voice);
		}
		play.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (mediaPlayer != null) {
					mediaPlayer.stop();
					mediaPlayer.release();
					mediaPlayer = null;
					//
					getCurPlayView().setBackgroundResource(
							R.drawable.ico_start_play);
					getCurPlayView().setTag(null);
					isPlaying = false;
					if(seekBar!=null)
					seekBar.setProgress(0);
					//如果是
					if(v==getCurPlayView())
					return;
				}
				mediaPlayer = new MediaPlayer();
				play.setBackgroundResource(R.drawable.ico_voice);
				play.setTag(1);
				setCurPlayView(v);
				isPlaying = true;
				// give data to mediaPlayer
				try {
					mediaPlayer.setDataSource(item.getPath());
					// media player asynchronous preparation
					mediaPlayer.prepareAsync();
					// execute this code at the end of asynchronous media player
					// preparation
					mediaPlayer.setOnPreparedListener(new OnPreparedListener() {
						public void onPrepared(final MediaPlayer mp) {

							// start media player
							mp.start();

							// link seekbar to bar view
							seekBar = (SeekBar) helper.getView(R.id.ir_seekbar);

							// update seekbar
							mHandler.post(mRunnable);

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
		});
	}

	private Handler mHandler = new Handler();
	private Runnable mRunnable = new Runnable() {

		@Override
		public void run() {
			if (mediaPlayer != null && isPlaying) {

				// set max value
				int mDuration = mediaPlayer.getDuration();
				seekBar.setMax(mDuration);

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
			mHandler.postDelayed(this, 10);
		}
	};

	public View getCurPlayView() {
		return curPlayView;
	}

	public void setCurPlayView(View curPlayView) {
		this.curPlayView = curPlayView;
	}

	// 根据毫秒值获取时分秒
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
	public void stop(){
		if (mediaPlayer != null) {
			mediaPlayer.stop();
			mediaPlayer.release();
			mediaPlayer = null;
			getCurPlayView().setBackgroundResource(
					R.drawable.ico_start_play);
			getCurPlayView().setTag(null);
			setCurPlayView(null);
			isPlaying = false;
			if(seekBar!=null)
			seekBar.setProgress(0);
		}
	}
}
