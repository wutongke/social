package com.cpstudio.zhuojiaren.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cpstudio.zhuojiaren.R;

public class TabButton extends HorizontalScrollView {
// HorizontalScrollView是一个 FrameLayout   ， 这意味着你只能在它下面放置一个子控件 
	private static final int DEF_LINE_COLOR = 0x6600000;
	private static final int DEF_SLIDER_COLOR = R.color.black;
	private static final int DEF_TEXT_COLOR = R.color.graywhite;
	private static final int DEF_TAB_PADDING = 5;

	private static final float DEF_BOTTON_LINE_SIZE = 0.5f;
	private static final float DEF_DIVIDER_SIZE = 20f;
	private static final float DEF_SLIDER_SIZE = 3f;
	private static final float DEF_TEXT_SIZE = 18f;

	private int screenWidth;

	private LinearLayout mLinearLayout;
	private ViewPager mViewPager;

	private int mButtonBackground; // 按钮背景
	private int mSelectButtonBackground; // 选中按钮背景
	private float mTextSize; // 字体大小
	private int mTextColor; // 字体颜色
	private int mSelectTextColor; // 字体颜色

	private Paint sliderPaint; // 滑块的画�?
	private Paint bottomPaint; // 底部线条的画�?
	private Paint dividerPaint; // 分割线的画笔
	private Paint mButtonBackgroundPaint; // 分割线的画笔
	private Paint mSelectButtonBackgroundPaint; // 分割线的画笔

	private float dividerSize; // 分割线高�?
	private float bottomLineSize; // 底部线高�?
	private float sliderSize; // 滑块高度

	private int sliderWidth; // 滑块宽度
	private int tabSize; // 选项卡的个数
	private int tabPadding; // 选项卡的边距
	private int pageSelect; // 当前选中的页�?
	private int childCount;
	private float scrollOffset; // 滑块已经滑动过的宽度

	private TabsButtonOnClickListener onClickListener;
	private PageChangeListener pageChangeListener;

	Context mContext;

	public TabButton(Context context) {
		super(context);
		init(context, null);
		this.mContext = context;
	}

	public TabButton(Context context, AttributeSet attrs) {
		super(context, attrs);

		init(context, attrs);
		this.mContext = context;
	}

	@SuppressLint("NewApi")
	public TabButton(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context, attrs);
		this.mContext = context;
	}

	@SuppressLint("ResourceAsColor")
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		final int layoutHeight = getHeight();
		final int layoutWidth = getWidth();
		// 绘制分割线
		for (int i = 0; i < mLinearLayout.getChildCount(); i++) {
			View view = mLinearLayout.getChildAt(i);
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
					layoutWidth / mLinearLayout.getChildCount() + 1,
					layoutHeight);
			view.setLayoutParams(params);
			float pianyi = (layoutHeight - dividerSize) / 2;
			canvas.drawRect(view.getLeft() - 1f, pianyi, view.getLeft(),
					layoutHeight - pianyi, dividerPaint);
			if(i==pageSelect){
				canvas.drawRect((float)view.getLeft(), 0f, (float)view.getRight(),
						(float)(layoutHeight - sliderSize), mSelectButtonBackgroundPaint);
			}else{
				canvas.drawRect((float)view.getLeft(), 0f, (float)view.getRight(),
						(float)(layoutHeight - sliderSize), mButtonBackgroundPaint);
			}
			// width = view.getWidth();
		}
		// 绘制滑块
		canvas.drawRect(scrollOffset, layoutHeight - sliderSize, scrollOffset
				+ sliderWidth, layoutHeight , sliderPaint);
		// 绘制底线
		canvas.drawRect(0, layoutHeight - bottomLineSize, layoutWidth,
				layoutHeight, bottomPaint);
	}

	private void init(Context context, AttributeSet attrs) {
		TypedArray tArray = context.obtainStyledAttributes(attrs,
				R.styleable.TabButton);
		mButtonBackground = tArray.getColor(
				R.styleable.TabButton_buttonBackground, 0xfff2f2f2);
		mSelectButtonBackground = tArray.getColor(
				R.styleable.TabButton_buttonSelectBackground, 0xffffffff);

		int bottomLineColor = tArray.getColor(
				R.styleable.TabButton_bottomLineColor, DEF_LINE_COLOR);
		int dividerColor = tArray.getColor(R.styleable.TabButton_dividerColor,
				DEF_LINE_COLOR);
		int sliderColor = tArray.getColor(R.styleable.TabButton_sliderColor,
				R.color.graywhitem);
		//变为标准尺寸
		float defBottonLineSize = TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_DIP, DEF_BOTTON_LINE_SIZE,
				getResources().getDisplayMetrics());
		float defDividerSize = TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_DIP, DEF_DIVIDER_SIZE, getResources()
						.getDisplayMetrics());
		float defSliderSize = TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_DIP, DEF_SLIDER_SIZE, getResources()
						.getDisplayMetrics());
		float defTextSize = TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_SP, DEF_TEXT_SIZE, getResources()
						.getDisplayMetrics());
		tabPadding = (int) TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_DIP, DEF_TAB_PADDING, getResources()
						.getDisplayMetrics());

		bottomLineSize = tArray.getDimension(
				R.styleable.TabButton_bottomLineSize, defBottonLineSize);
		dividerSize = tArray.getDimension(R.styleable.TabButton_dividerSize,
				defDividerSize);
		sliderSize = tArray.getDimension(R.styleable.TabButton_sliderSize,
				defSliderSize);

		mTextSize = tArray.getDimension(R.styleable.TabButton_textSize,
				defTextSize);
		mTextColor = tArray.getColor(R.styleable.TabButton_textColor,
				DEF_TEXT_COLOR);
		mSelectTextColor = tArray.getColor(R.styleable.TabButton_slecttextColor,
				DEF_TEXT_COLOR);
		tArray.recycle();

		if (isInEditMode()) {
			return;
		}

		mLinearLayout = new LinearLayout(getContext());
		mLinearLayout.setLayoutParams(new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.MATCH_PARENT));
		mLinearLayout.setOrientation(LinearLayout.HORIZONTAL);
		mLinearLayout.setGravity(Gravity.FILL_HORIZONTAL);
		mLinearLayout.setWeightSum(1f);

		addView(mLinearLayout);

		setWillNotDraw(false);
		setHorizontalScrollBarEnabled(false);

		sliderPaint = new Paint();
		sliderPaint.setAntiAlias(true);
		sliderPaint.setStyle(Style.FILL);
		sliderPaint.setColor(sliderColor);

		dividerPaint = new Paint();
		dividerPaint.setAntiAlias(true);
		dividerPaint.setStyle(Style.FILL);
		dividerPaint.setColor(dividerColor);

		bottomPaint = new Paint();
		bottomPaint.setAntiAlias(true);
		bottomPaint.setStyle(Style.FILL);
		bottomPaint.setColor(bottomLineColor);
		
		mButtonBackgroundPaint = new Paint();
		mButtonBackgroundPaint.setAntiAlias(true);
		mButtonBackgroundPaint.setStyle(Style.FILL);
		mButtonBackgroundPaint.setColor(mButtonBackground);
		
		mSelectButtonBackgroundPaint = new Paint();
		mSelectButtonBackgroundPaint.setAntiAlias(true);
		mSelectButtonBackgroundPaint.setStyle(Style.FILL);
		mSelectButtonBackgroundPaint.setColor(mSelectButtonBackground);
		

	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		super.onLayout(changed, l, t, r, b);
		// 当布�?��定大小的时�?，初始化滑块的位置�?
		drawUnderLine(pageSelect, 0);
	}

	public void setViewPager(ViewPager vp) {
		if(vp==null)
			return;
		this.mViewPager = vp;
		vp.setOnPageChangeListener(getOnPageChangeListener());
		PagerAdapter pagerAdapter = vp.getAdapter();
		int count = pagerAdapter.getCount();
		childCount = count;
		for (int i = 0; i < count; i++) {
			addTab(newTextTab(pagerAdapter.getPageTitle(i), i), count);
		}
	}

	@SuppressLint("NewApi")
	@SuppressWarnings("deprecation")
	public View newTextTab(CharSequence text, int position) {
		TextView tv = new TextView(getContext());
		LinearLayout.LayoutParams lParams = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.MATCH_PARENT, 1f);
		tv.setLayoutParams(lParams);
		WindowManager wm = (WindowManager) mContext
				.getSystemService(Context.WINDOW_SERVICE);

		int width = wm.getDefaultDisplay().getWidth();
		tv.setTag(position);
		tv.setWidth(width / childCount);
		tv.setGravity(Gravity.CENTER);
		tv.setPadding(tabPadding, 0, tabPadding, 0);
		tv.setOnClickListener(buttonOnClick);
		//第一个tab的颜色
		if (position == 0) {
			tv.setTextColor(mSelectTextColor);
//			tv.setBackgroundResource(mSelectButtonBackground);
		} else {
			tv.setTextColor(mTextColor);
//			tv.setBackgroundResource(mButtonBackground);
		}
		
		tv.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTextSize);
		tv.setSingleLine();
		tv.setText(text);

		return tv;
	}

	public void addTab(View view, int count) {
		view.setId(tabSize++);
		// LinearLayout.LayoutParams params =
		// (android.widget.LinearLayout.LayoutParams) view.getLayoutParams();
		// LinearLayout.LayoutParams temp = new
		// LinearLayout.LayoutParams(params.width/3-1,params.height);
		// view.setLayoutParams(temp);
		mLinearLayout.addView(view);
	}

	public void setTab(String... strings) {
		childCount=strings.length;
		for (int i=0;i<strings.length;i++) {
			addTab(newTextTab(strings[i], i), strings.length);
		}
	}

	public OnPageChangeListener getOnPageChangeListener() {
		return new OnPageChangeListener() {

			public void onPageSelected(int arg0) {
				pageSelect = arg0;
				setTabBackgroundByIndex(pageSelect);
				//写在后边，否则会被覆盖
				drawUnderLine(pageSelect, 0);
				if(pageChangeListener!=null)
					pageChangeListener.onPageSelected(arg0);
			}

			public void onPageScrolled(int index, float arg1, int scroll) {
				if (scroll != 0)
					drawUnderLine(index, arg1);
				if(pageChangeListener!=null)
					pageChangeListener.onPageScrolled(index, arg1, scroll);
			}

			@SuppressLint("ResourceAsColor")
			public void onPageScrollStateChanged(int state) {
//				if (state == ViewPager.SCROLL_STATE_IDLE)
//					drawUnderLine(pageSelect, 0);
//				setTabBackgroundByIndex(pageSelect);
				if(pageChangeListener!=null)
					pageChangeListener.onPageScrollStateChanged(state);
			}
		};
	}

	/**
	 * 实现pagechange监听
	 * @author lef
	 * 
	 */
	public interface PageChangeListener {
		public void onPageSelected(int arg0);

		public void onPageScrolled(int index, float arg1, int scroll);

		public void onPageScrollStateChanged(int state);
	}

	public void setTabBackgroundByIndex(int Index) {
		for (int i = 0; i < mLinearLayout.getChildCount(); i++) {
			if (i == Index) {
				TextView view = (TextView) mLinearLayout.getChildAt(i);
				view.setTextColor(mSelectTextColor);
//				view.setBackgroundResource(mSelectButtonBackground);
//				view.setBackgroundColor(Color.WHITE);
			} else {
				TextView view = (TextView) mLinearLayout.getChildAt(i);
				view.setTextColor(mTextColor);
				//必须是setBackgroundResource
//				view.setBackgroundResource(mButtonBackground);
			}
		}
	}
	/**
	 * 清除tab
	 */
	public void clearTab(){
		tabSize=0;
		mLinearLayout.removeAllViews();
	}
	/**
	 * 
	 * @param index
	 * @param scroll
	 */
	private void drawUnderLine(int index, float scroll) {
		int itemWidth = mLinearLayout.getChildAt(index).getWidth();
		// 滑块的长度radio
		int add = 0;
		if (index < tabSize - 1) {
			add = mLinearLayout.getChildAt(index + 1).getWidth() - itemWidth;
			// sliderWidth = (int) (scroll * add + itemWidth + 0.5f)/2;
			sliderWidth = 80;
		}
		// 滑块已经滑动的距离�?
		scrollOffset = mLinearLayout.getChildAt(index).getLeft() + scroll
				* itemWidth + itemWidth / 2 - sliderWidth / 2;
		// 控件宽度�?
		screenWidth = getWidth();
		// 滑块中间点距离控件左边的距离�?
		int half = (int) (scrollOffset + sliderWidth / 2);
		// 当滑块中间点大于控件宽度�?��或水平滚动量大于零时，让滑块保持在控件中间�?
		if (half > (screenWidth / 2) || computeHorizontalScrollOffset() != 0)
			scrollTo(half - screenWidth / 2, 0);
		invalidate();
	}

	private OnClickListener buttonOnClick = new OnClickListener() {

		public void onClick(View v) {
			if (onClickListener != null) {
				onClickListener.tabsButtonOnClick(v.getId(), v);
			}
			else if (mViewPager != null) {
				mViewPager.setCurrentItem(v.getId());
			}
		}
	};

	/**
	 * 监听按钮条被点击的接口�?
	 * 
	 */
	public interface TabsButtonOnClickListener {
		public void tabsButtonOnClick(int id, View v);
	}

	public void setPageChangeListener(PageChangeListener pageChangeListener) {
		this.pageChangeListener = pageChangeListener;
	}

	public void setTabsButtonOnClickListener(
			TabsButtonOnClickListener onClickListener) {
		this.onClickListener = onClickListener;
	}

	/**
	 * 保存滑块的当前位置�?当横竖屏切换或Activity被系统杀死时调用�?
	 */
	protected Parcelable onSaveInstanceState() {
		Parcelable parcelable = super.onSaveInstanceState();
		TabsSaveState tabsSaveState = new TabsSaveState(parcelable);
		tabsSaveState.select = pageSelect;
		return tabsSaveState;
	}

	/**
	 * 还原滑块的位置�?
	 */
	protected void onRestoreInstanceState(Parcelable state) {
		TabsSaveState tabsSaveState = (TabsSaveState) state;
		super.onRestoreInstanceState(tabsSaveState.getSuperState());
		pageSelect = tabsSaveState.select;
		drawUnderLine(pageSelect, 0);
	}

	static class TabsSaveState extends BaseSavedState {
		private int select;

		public TabsSaveState(Parcelable arg0) {
			super(arg0);
		}

		public TabsSaveState(Parcel arg0) {
			super(arg0);
			select = arg0.readInt();
		}

		public int describeContents() {
			return super.describeContents();
		}

		public void writeToParcel(Parcel dest, int flags) {
			super.writeToParcel(dest, flags);
			dest.writeInt(select);
		}

		public static final Parcelable.Creator<TabButton.TabsSaveState> CREATOR = new Parcelable.Creator<TabButton.TabsSaveState>() {

			public TabsSaveState createFromParcel(Parcel source) {
				return new TabsSaveState(source);
			};

			public TabsSaveState[] newArray(int size) {
				return new TabsSaveState[size];
			}
		};
	}
}