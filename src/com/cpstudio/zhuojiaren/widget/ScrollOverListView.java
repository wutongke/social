package com.cpstudio.zhuojiaren.widget;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import com.cpstudio.zhuojiaren.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * 该类主要是完成 头部部分的功能封装
 * 
 * 一个可以监听ListView是否滚动到最顶部或最底部的自定义控件 只能监听由触摸产生的，如果是ListView本身Flying导致的，则不能监听</br>
 * 如果加以改进，可以实现监听scroll滚动的具体位置等
 * 
 * @author 进
 */

public class ScrollOverListView extends ListView implements OnScrollListener {

	private int mLastY;

	private int mBottomPosition;

	/** 松开更新 **/
	private final static int RELEASE_To_REFRESH = 0;
	/** 下拉更新 **/
	private final static int PULL_To_REFRESH = 1;
	/** 更新中 **/
	private final static int REFRESHING = 2;
	/** 无 **/
	private final static int DONE = 3;
	/** 加载中 **/
	private final static int LOADING = 4;
	/** 实际的padding的距离与界面上偏移距离的比例 **/
	private final static int RATIO = 3;

	private LayoutInflater inflater;
	/** 头部刷新的布局 **/
	private ViewGroup headView;
	/** 头部显示下拉刷新等的控件 **/
	private TextView tipsTextview;
	/** 刷新控件 **/
	private TextView lastUpdatedTextView;
	/** 箭头图标 **/
	private ImageView arrowImageView;
	/** 头部滚动条 **/
	private ProgressBar progressBar;
	/** 显示动画 **/
	private RotateAnimation animation;
	/** 头部回退显示动画 **/
	private RotateAnimation reverseAnimation;
	/** 用于保证startY的值在一个完整的touch事件中只被记录一次 **/
	private boolean isRecored;
	/** 头部高度 **/
	private int headContentHeight;
	/** 开始的Y坐标 **/
	private int startY;
	/** 第一个item **/
	private int firstItemIndex;
	/** 状态 **/
	private int state;
	private int mHeader;

	private boolean isBack;
	/** 是否要使用下拉刷新功能 **/
	public boolean showRefresh = true;

	public static boolean canRefleash = false;

	private Context mContext = null;

	public ScrollOverListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	public ScrollOverListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public ScrollOverListView(Context context, int header) {
		super(context);
		this.mHeader = header;
		init(context);
	}

	/** 出事化控件 **/
	private void init(Context context) {
		mContext = context;
		mBottomPosition = 0;
		setCacheColorHint(0);
		inflater = LayoutInflater.from(context);

		headView = (ViewGroup) inflater.inflate(mHeader, null);
		if (headView.findViewById(R.id.head_arrowImageView) != null) {
			arrowImageView = (ImageView) headView
					.findViewById(R.id.head_arrowImageView);
			arrowImageView.setMinimumWidth(70);
			arrowImageView.setMinimumHeight(50);
		}
		progressBar = (ProgressBar) headView
				.findViewById(R.id.head_progressBar);
		if (headView.findViewById(R.id.head_tipsTextView) != null) {
			tipsTextview = (TextView) headView
					.findViewById(R.id.head_tipsTextView);
		}
		if (headView.findViewById(R.id.head_lastUpdatedTextView) != null) {
			lastUpdatedTextView = (TextView) headView
					.findViewById(R.id.head_lastUpdatedTextView);
		}
		measureView(headView);
		try {
			headContentHeight = headView.findViewById(R.id.head_contentLayout)
					.getMeasuredHeight();
		} catch (Exception e) {
			headContentHeight = headView.getMeasuredHeight();
		}
		headView.setPadding(0, -1 * headContentHeight, 0, 0);
		headView.invalidate();
		addHeaderView(headView, null, false);
		setOnScrollListener(this);
		animation = new RotateAnimation(0, -180,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f);
		animation.setInterpolator(new LinearInterpolator());
		animation.setDuration(250);
		animation.setFillAfter(true);

		reverseAnimation = new RotateAnimation(-180, 0,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f);

		reverseAnimation.setInterpolator(new LinearInterpolator());
		reverseAnimation.setDuration(200);
		reverseAnimation.setFillAfter(true);

		state = DONE;
	}

	/** 触摸事件的处理 **/
	@Override
	public boolean onTouchEvent(MotionEvent ev) {

		final int action = ev.getAction();
		final int y = (int) ev.getRawY();
		cancelLongPress();
		switch (action) {
		case MotionEvent.ACTION_DOWN: {
			if (firstItemIndex == 0 && !isRecored) {
				isRecored = true;
				startY = (int) ev.getY();
			}
			mLastY = y;
			final boolean isHandled = mOnScrollOverListener.onMotionDown(ev);
			if (isHandled) {
				mLastY = y;
				return isHandled;
			}
			break;
		}

		case MotionEvent.ACTION_MOVE: {
			int tempY = (int) ev.getY();
			if (showRefresh) {
				if (!isRecored && firstItemIndex == 0) {
					isRecored = true;
					startY = tempY;
				}
				if (state != REFRESHING && isRecored && state != LOADING) {
					if (state == RELEASE_To_REFRESH) {
						setSelection(0);
						if (((tempY - startY) / RATIO < headContentHeight)
								&& (tempY - startY) > 0) {
							state = PULL_To_REFRESH;
							changeHeaderViewByState();

						} else if (tempY - startY <= 0) {
							state = DONE;
							changeHeaderViewByState();
						} else {
						}
					}
					if (state == PULL_To_REFRESH) {
						setSelection(0);
						if ((tempY - startY) / RATIO >= headContentHeight) {
							state = RELEASE_To_REFRESH;
							isBack = true;
							changeHeaderViewByState();
						} else if (tempY - startY <= 0) {
							state = DONE;
							changeHeaderViewByState();
						}
					}

					if (state == DONE) {
						if (tempY - startY > 0) {
							state = PULL_To_REFRESH;
							changeHeaderViewByState();
						}
					}

					if (state == PULL_To_REFRESH) {
						headView.setPadding(0, -1 * headContentHeight
								+ (tempY - startY) / RATIO, 0, 0);
					}

					if (state == RELEASE_To_REFRESH) {
						int paddingTop = 0;
						if(arrowImageView != null){	
							paddingTop = (tempY - startY) / RATIO
									- headContentHeight;
						}
						headView.setPadding(0,paddingTop , 0, 0);
					}
				}
			}

			final int childCount = getChildCount();
			if (childCount == 0)
				return super.onTouchEvent(ev);
			final int itemCount = getAdapter().getCount() - mBottomPosition;
			final int deltaY = y - mLastY;
			final int lastBottom = getChildAt(childCount - 1).getBottom();
			final int end = getHeight() - getPaddingBottom();

			final int firstVisiblePosition = getFirstVisiblePosition();

			final boolean isHandleMotionMove = mOnScrollOverListener
					.onMotionMove(ev, deltaY);

			if (isHandleMotionMove) {
				mLastY = y;
				return true;
			}

			if (firstVisiblePosition + childCount >= itemCount
					&& lastBottom <= end && deltaY < 0) {
				final boolean isHandleOnListViewBottomAndPullDown;
				isHandleOnListViewBottomAndPullDown = mOnScrollOverListener
						.onListViewBottomAndPullUp(deltaY);
				if (isHandleOnListViewBottomAndPullDown) {
					mLastY = y;
					return true;
				}
			}
			break;
		}

		case MotionEvent.ACTION_UP: {
			if (state != REFRESHING && state != LOADING) {
				if (state == DONE) {
				}
				if (state == PULL_To_REFRESH) {
					state = DONE;
					changeHeaderViewByState();
				}

				if (state == RELEASE_To_REFRESH) {
					state = REFRESHING;
					changeHeaderViewByState();
					canRefleash = true;
				}
			}

			isRecored = false;
			isBack = false;

			final boolean isHandlerMotionUp = mOnScrollOverListener
					.onMotionUp(ev);
			if (isHandlerMotionUp) {
				mLastY = y;
				return true;
			}
			break;
		}
		}
		mLastY = y;
		return super.onTouchEvent(ev);
	}

	private OnScrollOverListener mOnScrollOverListener = new OnScrollOverListener() {

		@Override
		public boolean onListViewTopAndPullDown(int delta) {
			return false;
		}

		@Override
		public boolean onListViewBottomAndPullUp(int delta) {
			return false;
		}

		@Override
		public boolean onMotionDown(MotionEvent ev) {
			return false;
		}

		@Override
		public boolean onMotionMove(MotionEvent ev, int delta) {
			return false;
		}

		@Override
		public boolean onMotionUp(MotionEvent ev) {
			return false;
		}

	};

	/**
	 * 可以自定义其中一个条目为头部，头部触发的事件将以这个为准，默认为第一个
	 * 
	 * @param index
	 *            正数第几个，必须在条目数范围之内
	 */
	public void setTopPosition(int index) {
		if (getAdapter() == null)
			throw new NullPointerException(
					"You must set adapter before setTopPosition!");
		if (index < 0)
			throw new IllegalArgumentException("Top position must > 0");
	}

	/**
	 * 可以自定义其中一个条目为尾部，尾部触发的事件将以这个为准，默认为最后一个
	 * 
	 * @param index
	 *            倒数第几个，必须在条目数范围之内
	 */
	public void setBottomPosition(int index) {
		if (getAdapter() == null)
			throw new NullPointerException(
					"You must set adapter before setBottonPosition!");
		if (index < 0)
			throw new IllegalArgumentException("Bottom position must > 0");

		mBottomPosition = index;
	}

	/**
	 * 设置这个Listener可以监听是否到达顶端，或者是否到达低端等事件</br>
	 * 
	 * @see OnScrollOverListener
	 */
	public void setOnScrollOverListener(
			OnScrollOverListener onScrollOverListener) {
		mOnScrollOverListener = onScrollOverListener;
	}

	/**
	 * 滚动监听接口
	 * 
	 * @see ScrollOverListView#setOnScrollOverListener(OnScrollOverListener)
	 * 
	 */
	public interface OnScrollOverListener {
		/**
		 * 到达最顶部触发
		 * 
		 * @param delta
		 *            手指点击移动产生的偏移量
		 * @return
		 */
		boolean onListViewTopAndPullDown(int delta);

		/**
		 * 到达最底部触发
		 * 
		 * @param delta
		 *            手指点击移动产生的偏移量
		 * @return
		 */
		boolean onListViewBottomAndPullUp(int delta);

		/**
		 * 手指触摸按下触发，相当于{@link MotionEvent#ACTION_DOWN}
		 * 
		 * @return 返回true表示自己处理
		 * @see View#onTouchEvent(MotionEvent)
		 */
		boolean onMotionDown(MotionEvent ev);

		/**
		 * 手指触摸移动触发，相当于{@link MotionEvent#ACTION_MOVE}
		 * 
		 * @return 返回true表示自己处理
		 * @see View#onTouchEvent(MotionEvent)
		 */
		boolean onMotionMove(MotionEvent ev, int delta);

		/**
		 * 手指触摸后提起触发，相当于{@link MotionEvent#ACTION_UP}
		 * 
		 * @return 返回true表示自己处理
		 * @see View#onTouchEvent(MotionEvent)
		 */
		boolean onMotionUp(MotionEvent ev);

	}

	@Override
	public void onScroll(AbsListView arg0, int firstVisiableItem, int arg2,
			int arg3) {
		firstItemIndex = firstVisiableItem;
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {

	}

	private void measureView(View child) {
		if(child==null)
			return;
		ViewGroup.LayoutParams p = child.getLayoutParams();
		if (p == null) {
			p = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
					ViewGroup.LayoutParams.WRAP_CONTENT);
		}
		int childWidthSpec = ViewGroup.getChildMeasureSpec(0, 0 + 0, p.width);
		int lpHeight = p.height;
		int childHeightSpec;
		if (lpHeight > 0) {
			childHeightSpec = MeasureSpec.makeMeasureSpec(lpHeight,
					MeasureSpec.EXACTLY);
		} else {
			childHeightSpec = MeasureSpec.makeMeasureSpec(0,
					MeasureSpec.UNSPECIFIED);
		}
		child.measure(childWidthSpec, childHeightSpec);
	}

	public void onRefreshComplete(boolean result) {
		state = DONE;
		if (result && lastUpdatedTextView != null) {
			SimpleDateFormat localSimpleDateFormat = new SimpleDateFormat(
					"MM月dd日 HH:mm分", Locale.CHINESE);
			String str = localSimpleDateFormat.format(new Date());
			lastUpdatedTextView.setText(mContext
					.getText(R.string.label_last_update) + str);
		}
		changeHeaderViewByState();
	}

	private void changeHeaderViewByState() {
		switch (state) {
		case RELEASE_To_REFRESH:
			if (tipsTextview != null) {
				tipsTextview.setVisibility(View.VISIBLE);
			}
			if (arrowImageView != null) {
				progressBar.setVisibility(View.GONE);
				arrowImageView.setVisibility(View.VISIBLE);
				arrowImageView.clearAnimation();
				arrowImageView.startAnimation(animation);
			}
			if (tipsTextview != null) {
				tipsTextview.setText(R.string.label_header_refresh_up);
			}
			break;
		case PULL_To_REFRESH:
			if (tipsTextview != null) {
				tipsTextview.setVisibility(View.VISIBLE);
			}
			if (arrowImageView != null) {
				progressBar.setVisibility(View.GONE);
				arrowImageView.clearAnimation();
				arrowImageView.setVisibility(View.VISIBLE);
			}else{
				progressBar.setVisibility(View.VISIBLE);
			}
			if (isBack) {
				isBack = false;
				if (arrowImageView != null) {
					arrowImageView.startAnimation(reverseAnimation);
				}
			}
			if (tipsTextview != null) {
				tipsTextview.setText(R.string.label_header_refresh_down);
			}
			break;
		case REFRESHING:
			headView.setPadding(0, 0, 0, 0);
			if (arrowImageView != null) {
				progressBar.setVisibility(View.VISIBLE);
				arrowImageView.clearAnimation();
				arrowImageView.setVisibility(View.GONE);
			}
			if (tipsTextview != null) {
				tipsTextview.setText(R.string.label_header_refresh_last_update);
			}
			break;
		case DONE:
			headView.setPadding(0, -1 * headContentHeight, 0, 0);
			progressBar.setVisibility(View.GONE);
			if (arrowImageView != null) {
				arrowImageView.clearAnimation();
				arrowImageView.setImageResource(R.drawable.ico_zhuo);
			}
			if (tipsTextview != null) {
				tipsTextview.setText(R.string.label_header_refresh_down);
			}
			break;
		}
	}
}
