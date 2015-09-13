package com.cpstudio.zhuojiaren.util;

import com.cpstudio.zhuojiaren.R;
import com.cpstudio.zhuojiaren.util.ImageLoader.Type;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
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
	 * 鎷垮埌鍒癡iewHolder瀵硅薄
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
	 * 閫氳繃鎺т欢鐨処d鑾峰彇瀵逛簬鐨勬帶浠讹紝濡傛灉娌℃湁鍒欏姞鍏iews
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
	 * 涓篢extView璁剧疆瀛楃锟�
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
	 * 涓篒mageView璁剧疆鍥剧墖
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

	/**
	 * 涓篒mageView璁剧疆鍥剧墖
	 * 
	 * @param viewId
	 * @param drawableId
	 * @return
	 */
	public ViewHolder setButton(int viewId, String txt,int visible,
			OnClickListener listener) {

		Button view = getView(viewId);
		if (view == null)
			return this;
		if (txt != null)
			view.setText(txt);
		if (listener != null)
			view.setOnClickListener(listener);
		if (visible != -1)
			view.setVisibility(visible);
		return this;
	}

	public ViewHolder setImageResource(int viewId, int drawableId) {

		return setImageResource(viewId, drawableId, null);
	}

	/**
	 * 涓篒mageView璁剧疆鍥剧墖
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
	 * 涓篒mageView璁剧疆鍥剧墖
	 * 
	 * @param viewId
	 * @param drawableId
	 * @return
	 */
	public ViewHolder setImageByUrl(int viewId, String url) {
		// 鍏堝垱寤猴紝鍚庝娇鐢�
		ImageLoader il = ImageLoader.getInstance(3, Type.LIFO);
		il.loadImage(url, (ImageView) getView(viewId));
		return this;
	}

	// add by lz
	/**
	 * 涓篊heckBox璧嬪�
	 * 
	 * @param viewId
	 * @param drawableId
	 * @return
	 */
	public ViewHolder setCheckBox(int viewId, boolean flag, int visibility) {
		// 鍏堝垱寤猴紝鍚庝娇鐢�
		CheckBox cb = getView(viewId);
		cb.setChecked(flag);
		cb.setVisibility(visibility);
		return this;
	}

	public int getPosition() {
		return mPosition;
	}

}
