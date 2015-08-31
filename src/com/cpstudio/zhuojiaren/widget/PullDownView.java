package com.cpstudio.zhuojiaren.widget;

import com.cpstudio.zhuojiaren.widget.ScrollOverListView.OnScrollOverListener;
import com.cpstudio.zhuojiaren.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class PullDownView extends LinearLayout implements OnScrollOverListener {

	private static final int START_PULL_DEVIATION = 50;
	private static final int DATA_MORE = 5;
	private static final int DATA_REFRESH = 3;
	private RelativeLayout mFooterView;
	private TextView mFooterTextView;
	private TextView mFooterNoData;
	private ProgressBar mFooterLoadingView;
	private ScrollOverListView mListView;
	private OnPullDownListener mOnPullDownListener;
	private float mMotionDownLastY;
	private boolean mIsFetchMoreing;
	private boolean mIsPullUpDone;
	private boolean mEnableAutoFetchMore;

	public PullDownView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public PullDownView(Context context) {
		super(context);
	}

	public interface OnPullDownListener {
		void onRefresh();

		void onMore();
	}

	public void notifyDidMore() {
		mUIHandler.sendEmptyMessage(DATA_MORE);
	}

	public void RefreshComplete(boolean result) {
		Message msg = mUIHandler.obtainMessage(DATA_REFRESH);
		msg.obj = result;
		msg.sendToTarget();
		// mUIHandler.sendEmptyMessage(DATA_REFRESH);
	}

	public void setOnPullDownListener(OnPullDownListener listener) {
		mOnPullDownListener = listener;
	}

	public ListView getListView() {
		return mListView;
	}

	public void enableAutoFetchMore(boolean enable, int index) {
		if (enable) {
			mListView.setBottomPosition(index);
			mFooterLoadingView.setVisibility(View.VISIBLE);
		} else {
			mFooterLoadingView.setVisibility(View.GONE);
		}
		mEnableAutoFetchMore = enable;
	}

	public void initHeaderViewAndFooterViewAndListView(Context context,
			int header) {
		setOrientation(LinearLayout.VERTICAL);
		mFooterView = (RelativeLayout) LayoutInflater.from(context).inflate(
				R.layout.listview_footer, null);
		mFooterTextView = (TextView) mFooterView
				.findViewById(R.id.textViewFooterButton);
		mFooterNoData = (TextView) mFooterView
				.findViewById(R.id.textViewNoData);
		mFooterLoadingView = (ProgressBar) mFooterView
				.findViewById(R.id.progressFooterLoading);
		mFooterView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (!mIsFetchMoreing) {
					mIsFetchMoreing = true;
					mFooterNoData.setVisibility(View.GONE);
					mFooterLoadingView.setVisibility(View.VISIBLE);
					mOnPullDownListener.onMore();
				}
			}
		});

		mListView = new ScrollOverListView(context, header);
		mListView.setOnScrollOverListener(this);
		mListView.setCacheColorHint(0x00000000);
		mListView.setSelector(R.color.transparent);
		mListView.setDivider(getContext().getResources().getDrawable(
				R.drawable.bg_border3));
		mListView.setHeaderDividersEnabled(false);
		addView(mListView, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

		mOnPullDownListener = new OnPullDownListener() {
			@Override
			public void onRefresh() {
			}

			@Override
			public void onMore() {
			}
		};

		mListView.addFooterView(mFooterView);
	}

	@SuppressLint("HandlerLeak")
	private Handler mUIHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case DATA_REFRESH: {
				boolean result = (Boolean) msg.obj;
				mListView.onRefreshComplete(result);
				return;
			}

			case DATA_MORE: {
				mIsFetchMoreing = false;
				mFooterLoadingView.setVisibility(View.GONE);
			}
			}
		}

	};

	private boolean isFillScreenItem() {
		final int firstVisiblePosition = mListView.getFirstVisiblePosition();
		final int lastVisiblePostion = mListView.getLastVisiblePosition()
				- mListView.getFooterViewsCount();
		final int visibleItemCount = lastVisiblePostion - firstVisiblePosition
				+ 1;
		final int totalItemCount = mListView.getCount()
				- mListView.getFooterViewsCount();

		if (visibleItemCount < totalItemCount)
			return true;
		return false;
	}

	@Override
	public boolean onListViewTopAndPullDown(int delta) {
		return true;
	}

	@Override
	public boolean onListViewBottomAndPullUp(int delta) {
		if (!mEnableAutoFetchMore || mIsFetchMoreing)
			return false;
		if (isFillScreenItem()) {
			mIsFetchMoreing = true;
			mFooterLoadingView.setVisibility(View.VISIBLE);
			mOnPullDownListener.onMore();
			return true;
		}
		return false;
	}

	@Override
	public boolean onMotionDown(MotionEvent ev) {
		mIsPullUpDone = false;
		mMotionDownLastY = ev.getRawY();

		return false;
	}

	@Override
	public boolean onMotionMove(MotionEvent ev, int delta) {
		if (mIsPullUpDone)
			return true;

		final int absMotionY = (int) Math.abs(ev.getRawY() - mMotionDownLastY);
		if (absMotionY < START_PULL_DEVIATION)
			return true;

		return false;
	}

	@Override
	public boolean onMotionUp(MotionEvent ev) {
		if (ScrollOverListView.canRefleash) {
			ScrollOverListView.canRefleash = false;
			mOnPullDownListener.onRefresh();
		}
		return false;
	}

	public void setHideHeader() {
		mListView.showRefresh = false;
	}

	public void setShowHeader() {
		mListView.showRefresh = true;
	}
	public void setShowHeader(boolean fresh) {
		mListView.showRefresh = fresh;
	}
	public void setHideFooter(boolean auto) {
		mFooterView.setVisibility(View.GONE);
		mFooterTextView.setVisibility(View.GONE);
		mFooterLoadingView.setVisibility(View.GONE);
		enableAutoFetchMore(auto, 1);
	}

	public void setShowFooter(boolean auto) {
		mFooterView.setVisibility(View.VISIBLE);
		mFooterTextView.setVisibility(View.VISIBLE);
		mFooterLoadingView.setVisibility(View.GONE);
		enableAutoFetchMore(auto, 1);
	}

	public boolean startLoadData() {
		if (!mIsFetchMoreing) {
			mIsFetchMoreing = true;
			mFooterNoData.setVisibility(View.GONE);
			mFooterLoadingView.setVisibility(View.VISIBLE);
			return true;
		} else {
			return false;
		}
	}

	public void finishLoadData(boolean result) {
		mIsFetchMoreing = false;
		mFooterLoadingView.setVisibility(View.GONE);
		Message msg = mUIHandler.obtainMessage(DATA_REFRESH);
		msg.obj = result;
		msg.sendToTarget();
	}

	public void noData(boolean isMore) {
		mFooterTextView.setVisibility(View.INVISIBLE);
		if (!isMore) {
			mFooterNoData.setText(R.string.label_no_data2);
		}
		mFooterNoData.setVisibility(View.VISIBLE);
	}

	public void hasData() {
		mFooterTextView.setVisibility(View.VISIBLE);
		mFooterNoData.setVisibility(View.GONE);
	}
	public void noFoot(){
		mFooterView.setVisibility(View.GONE);
		mFooterNoData.setVisibility(View.GONE);
	}
}
