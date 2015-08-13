package com.cpstudio.zhuojiaren.util;

import com.cpstudio.zhuojiaren.R;
import com.cpstudio.zhuojiaren.util.ImageLoader.Type;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

public class ViewHolder {
	private final SparseArray<View> mViews;
	private int mPosition;
	private View mConvertView;
	private Context mContext;

	private ViewHolder(Context context, ViewGroup parent, int layoutId,
			int position) {
		this.mPosition = position;
		this.mViews = new SparseArray<View>();
		mConvertView = LayoutInflater.from(context).inflate(layoutId, parent,
				false);
		mContext = context;
		// setTag
		mConvertView.setTag(this);
	}

	/**
	 * 拿到到ViewHolder对象
	 * 
	 * @param context
	 * @param convertView
	 * @param parent
	 * @param layoutId
	 * @param position
	 * @return
	 */
	public static ViewHolder get(Context context, View convertView,
			ViewGroup parent, int layoutId, int position) {
		if (convertView == null) {
			return new ViewHolder(context, parent, layoutId, position);
		}
		return (ViewHolder) convertView.getTag();
	}

	public View getConvertView() {
		return mConvertView;
	}

	/**
	 * 通过控件的Id获取对于的控件，如果没有则加入views
	 * 
	 * @param viewId
	 * @return
	 */
	public <T extends View> T getView(int viewId) {
		View view = mViews.get(viewId);
		if (view == null) {
			view = mConvertView.findViewById(viewId);
			mViews.put(viewId, view);
		}
		return (T) view;
	}

	/**
	 * 为TextView设置字符�?
	 * 
	 * @param viewId
	 * @param text
	 * @return
	 */
	public ViewHolder setText(int viewId, String text) {
		TextView view = getView(viewId);
		if (text == null) {
			Log.d("Debug",
					mContext.getResources().getString(R.string.error_null));
			view.setText("");
		}
		view.setText(text);
		return this;
	}

	/**
	 * 为ImageView设置图片
	 * 
	 * @param viewId
	 * @param drawableId
	 * @return
	 */
	public ViewHolder setImageResource(int viewId, int drawableId,
			OnClickListener listener) {
		ImageView view = getView(viewId);
		view.setImageResource(drawableId);
		if (listener != null)
			view.setOnClickListener(listener);
		return this;
	}

	public ViewHolder setImageResource(int viewId, int drawableId) {

		return setImageResource(viewId, drawableId, null);
	}

	/**
	 * 为ImageView设置图片
	 * 
	 * @param viewId
	 * @param drawableId
	 * @return
	 */
	public ViewHolder setImageBitmap(int viewId, Bitmap bm) {
		ImageView view = getView(viewId);
		view.setImageBitmap(bm);
		return this;
	}

	/**
	 * 为ImageView设置图片
	 * 
	 * @param viewId
	 * @param drawableId
	 * @return
	 */
	public ViewHolder setImageByUrl(int viewId, String url) {
		// 先创建，后使用
		ImageLoader il = ImageLoader.getInstance(3, Type.LIFO);
		il.loadImage(url, (ImageView) getView(viewId));
		return this;
	}

	// add by lz
	/**
	 * 为CheckBox赋值
	 * 
	 * @param viewId
	 * @param drawableId
	 * @return
	 */
	public ViewHolder setCheckBox(int viewId, boolean flag, int visibility) {
		// 先创建，后使用
		CheckBox cb = getView(viewId);
		cb.setChecked(flag);
		cb.setVisibility(visibility);
		return this;
	}

	public int getPosition() {
		return mPosition;
	}

}
