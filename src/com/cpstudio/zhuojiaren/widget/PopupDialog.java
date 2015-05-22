package com.cpstudio.zhuojiaren.widget;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import com.cpstudio.zhuojiaren.R;

public class PopupDialog {
	private static PopupDialog instance;
	private boolean cancelable = true;
	private View view;
	private View customerView;
	private PopupWindow dialog;
	private String title;
	private String cancelText;
	private String okText;
	private String dismissText;
	private OnClickListener cancelListener;
	private OnClickListener okListener;
	private OnClickListener dismissListener;
	private int animationStyle = 0;
	private String[] items;
	private OnClickListener[] itemListener;

	private PopupDialog(View parent) {
		this.view = parent;
	}

	public static PopupDialog getInstance(View parent) {
		if (instance == null) {
			instance = new PopupDialog(parent);
		}
		return instance;
	}

	private String getString(int resId) {
		return view.getContext().getString(resId);
	}

	public PopupDialog setTitle(String title) {
		this.title = title;
		return instance;
	}

	public PopupDialog setTitle(int titleRedId) {
		this.title = getString(titleRedId);
		return instance;
	}

	public PopupDialog setCancelButton(String cancelText,
			OnClickListener listener) {
		this.cancelText = cancelText;
		this.cancelListener = listener;
		return instance;
	}

	public PopupDialog setOkButton(String okText, OnClickListener listener) {
		this.okText = okText;
		this.okListener = listener;
		return instance;
	}

	public PopupDialog setDismissButton(String dismissText,
			OnClickListener listener) {
		this.dismissText = dismissText;
		this.dismissListener = listener;
		return instance;
	}

	public PopupDialog setCancelButton(int cancelResId, OnClickListener listener) {
		this.cancelText = getString(cancelResId);
		this.cancelListener = listener;
		return instance;
	}

	public PopupDialog setOkButton(int okResId, OnClickListener listener) {
		this.okText = getString(okResId);
		this.okListener = listener;
		return instance;
	}

	public PopupDialog setDismissButton(int dismissResId,
			OnClickListener listener) {
		this.dismissText = getString(dismissResId);
		this.dismissListener = listener;
		return instance;
	}

	public PopupDialog setItems(String[] items, OnClickListener[] listener) {
		this.items = items;
		this.itemListener = listener;
		return instance;
	}

	public PopupDialog setItems(int itemsResId, OnClickListener[] listener) {
		this.items = view.getContext().getResources()
				.getStringArray(itemsResId);
		this.itemListener = listener;
		return instance;
	}

	public PopupDialog setCancelable(boolean cancelable) {
		this.cancelable = cancelable;
		return instance;
	}

	public PopupDialog setAnimationStyle(int animation) {
		// R.style.AnimBottom
		this.animationStyle = animation;
		return instance;
	}

	public PopupDialog setView(View view) {
		this.customerView = view;
		return instance;
	}

	@SuppressWarnings("deprecation")
	public PopupDialog create() {
		InputMethodManager imm = (InputMethodManager) view.getContext()
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
		LayoutInflater inflater = (LayoutInflater) view.getContext()
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View v = inflater.inflate(R.layout.pop_middle_dlg_all, null);
		dialog = new PopupWindow(v, -2, -2);
		if (okText != null) {
			Button okButton = (Button) v.findViewById(R.id.buttonOK);
			okButton.setVisibility(View.VISIBLE);
			okButton.setText(okText);
			okButton.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					dismiss();
					if (okListener != null) {
						okListener.onClick(v);
					}
				}
			});
		}
		if (cancelText != null) {
			Button cancelButton = (Button) v.findViewById(R.id.buttonCancel);
			cancelButton.setVisibility(View.VISIBLE);
			cancelButton.setText(cancelText);
			cancelButton.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					dismiss();
					if (cancelListener != null) {
						cancelListener.onClick(v);
					}
				}
			});
		}
		if (dismissText != null) {
			Button dismissButton = (Button) v.findViewById(R.id.buttonDismiss);
			dismissButton.setVisibility(View.VISIBLE);
			dismissButton.setText(dismissText);
			dismissButton.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					dismiss();
					if (dismissListener != null) {
						dismissListener.onClick(v);
					}
				}
			});
		}
		if (title != null) {
			TextView titleView = (TextView) v.findViewById(R.id.textViewTitle);
			titleView.setVisibility(View.VISIBLE);
			titleView.setText(title);
			if (title.length() > 14) {
				titleView.setGravity(3);
			} else {
				titleView.setGravity(17);
			}
			if (title.length() > 40) {
				titleView.setTextSize(14);
			} else {
				titleView.setTextSize(18);
			}
		}
		LinearLayout parent = (LinearLayout) (v.findViewById(R.id.layoutParent));
		if (customerView != null) {
			parent.addView(customerView);
		}
		if (items != null) {
			for (int i = 0; i < items.length; i++) {
				TextView tv = new TextView(view.getContext());
				tv.setLayoutParams(new LayoutParams(-1, -2));
				tv.setPadding(5, 10, 0, 10);
				tv.setText(items[i]);
				tv.setTextSize(18);
				tv.setTextColor(Color.WHITE);
				tv.setOnClickListener(itemListener[i]);
				parent.addView(tv);
			}
		}
		dialog.setFocusable(true);
		dialog.setOutsideTouchable(true);
		dialog.setAnimationStyle(animationStyle);
		if (cancelable) {
			dialog.setBackgroundDrawable(new BitmapDrawable());
		}
		return instance;
	}

	public PopupDialog show() {
		if (dialog == null) {
			create();
		}
		dialog.showAtLocation(view, Gravity.CENTER | Gravity.CENTER_HORIZONTAL,
				0, 0);
		return instance;
	}

	public PopupDialog dismiss() {
		if (dialog != null) {
			dialog.dismiss();
		}
		return instance;
	}
}
