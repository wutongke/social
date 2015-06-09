package com.cpstudio.zhuojiaren.widget;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import kankan.wheel.widget.OnWheelChangedListener;
import kankan.wheel.widget.OnWheelScrollListener;
import kankan.wheel.widget.WheelView;
import kankan.wheel.widget.adapters.ArrayWheelAdapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView.OnEditorActionListener;

import com.cpstudio.zhuojiaren.R;
import com.cpstudio.zhuojiaren.adapter.ResFilterListAdapter;
import com.cpstudio.zhuojiaren.adapter.TypeFilterListAdapter;
import com.cpstudio.zhuojiaren.helper.ResHelper;
import com.cpstudio.zhuojiaren.model.MsgTagVO;
import com.cpstudio.zhuojiaren.util.CommonUtil;
import com.cpstudio.zhuojiaren.util.DeviceInfoUtil;
import com.utils.ImageRectUtil;

public class PopupWindows {
	private PopupWindow popupWindow;
	private PopupWindow popupWindowTip;
	private PopupWindow popupWindowDlg;
	private PopupWindow popupWindowDlgOne;
	private PopupWindow popupWindowBottom;
	private PopupWindow popupWindowOptions;
	private PopupWindow fullScreenPopupWindow;
	private PopupWindow breakQaunziPopupWindow;
	private View view;
	private View viewOptions;
	private View viewTip;
	private View viewDlg;
	private View viewBottom;
	private View viewBreakQuanzi;
	private View fullScreenView;
	private View viewDlgOne;

	private Activity mActivity;
	public static final String WHOLESALE_CONV = ".jpg";
	private ResHelper mResHelper = null;
	private ListView lv_group;
	private ArrayList<String> groups;
	private ResFilterListAdapter rAdapter = null;
	private TypeFilterListAdapter tAdapter = null;
	private ImageView mImageView;
	private boolean scrolling = false;

	public PopupWindows(Activity activity) {
		mResHelper = ResHelper.getInstance(activity);
		this.mActivity = activity;
	}

	public String dealPhotoReturn(int requestCode, int resultCode, Intent data) {
		return dealPhotoReturn(requestCode, resultCode, data, true);
	}

	public String dealPhotoReturn(int requestCode, int resultCode, Intent data,
			boolean crop) {
		if (resultCode == Activity.RESULT_OK) {
			try {
				String filePath = null;
				if (requestCode == MsgTagVO.SELECT_PICTURE) {
					Uri uri = null;
					if (data.getData() != null) {
						uri = data.getData();
					} else {
						uri = Uri.parse(data.getAction());
					}
					filePath = ImageRectUtil.getPathFromUri(mActivity, uri);
				} else if (requestCode == MsgTagVO.SELECT_CAMER
						|| requestCode == MsgTagVO.CAMERA_REQUEST) {
					if (null != data) {
						if (null != data.getData()) {
							Uri uri = data.getData();
							filePath = ImageRectUtil.getPathFromUri(mActivity,
									uri);
						} else if (null != data.getAction()
								&& data.getAction() != "inline-data") {
							filePath = Uri.parse(data.getAction()).getPath();
						} else {
							Bundle bundle = data.getExtras();
							if (bundle != null) {
								if (bundle.get("data") != null) {
									Bitmap bitmap = (Bitmap) bundle.get("data");
									filePath = newThumbImage(bitmap, filePath);
								} else {
									if (crop) {
										filePath = mResHelper.getCaptruePath();
									} else {
										int degree = ImageRectUtil
												.readPictureDegree(mResHelper
														.getCaptruePath());
										filePath = newThumbImage(
												ImageRectUtil.rotaingBitmap(
														degree,
														ImageRectUtil
																.revitionImageSize(
																		mResHelper
																				.getCaptruePath(),
																		640,
																		960)),
												filePath);
									}
								}
							} else {
								CommonUtil.displayToast(mActivity,
										R.string.error1);
							}
						}
					} else {
						if (crop) {
							filePath = mResHelper.getCaptruePath();
						} else {
							int degree = ImageRectUtil
									.readPictureDegree(mResHelper
											.getCaptruePath());
							filePath = newThumbImage(
									ImageRectUtil.rotaingBitmap(
											degree,
											ImageRectUtil.revitionImageSize(
													mResHelper.getCaptruePath(),
													640, 960)), filePath);
						}
					}
				}
				if (crop && requestCode != MsgTagVO.CAMERA_REQUEST) {
					Uri imageUri = Uri.fromFile(new File(filePath));
					Intent cropIntent = new Intent(
							"com.android.camera.action.CROP");
					cropIntent.setDataAndType(imageUri, "image/*");
					cropIntent.putExtra("crop", "true");
					cropIntent.putExtra("aspectX", 1);
					cropIntent.putExtra("aspectY", 1);
					cropIntent.putExtra("outputX", 480);
					cropIntent.putExtra("outputY", 480);
					cropIntent.putExtra("scale", true);
					cropIntent.putExtra("noFaceDetection", true);
					cropIntent.putExtra(MediaStore.EXTRA_OUTPUT,
							mResHelper.getCaptrueUri());
					cropIntent.putExtra("return-data", false);
					cropIntent.putExtra("outputFormat",
							Bitmap.CompressFormat.JPEG.toString());
					mActivity.startActivityForResult(cropIntent,
							MsgTagVO.CAMERA_REQUEST);
				} else {
					if (!crop) {
						Bitmap bitmap = ImageRectUtil.thumbImage(filePath, 720,
								0);
						filePath = newThumbImage(bitmap, filePath);
					}
					return filePath;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	private String newThumbImage(Bitmap bitmap, String filePath) {
		String savePath = mResHelper.getImagePath();
		filePath = savePath + CommonUtil.get32RandomString(filePath) + "__0@0@"
				+ bitmap.getWidth() + "@" + bitmap.getHeight();
		ImageRectUtil.saveCaptrueImage(bitmap, filePath);
		bitmap.recycle();
		return filePath;
	}

	// 选择图片
	public PopupWindow showPop(View parent) {
		if (null != parent) {
			InputMethodManager imm = (InputMethodManager) mActivity
					.getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(parent.getWindowToken(), 0);
		}
		OnClickListener selectClickListener = new OnClickListener() {
			@Override
			public void onClick(View paramView) {
				Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
				intent.addCategory(Intent.CATEGORY_OPENABLE);
				intent.setType("image/*");
				mActivity.startActivityForResult(
						Intent.createChooser(intent,
								mActivity.getString(R.string.info0)),
						MsgTagVO.SELECT_PICTURE);
			}
		};
		OnClickListener makeClickListener = new OnClickListener() {
			@Override
			public void onClick(View paramView) {
				String state = Environment.getExternalStorageState();
				if (state.equals(Environment.MEDIA_MOUNTED)) {
					Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
					intent.putExtra(MediaStore.EXTRA_OUTPUT,
							mResHelper.getCaptrueUri());
					mActivity.startActivityForResult(intent,
							MsgTagVO.SELECT_CAMER);
				} else {
					CommonUtil.displayToast(mActivity, R.string.error2);
				}
			}
		};
		return showBottomPop(parent, new OnClickListener[] {
				selectClickListener, makeClickListener }, new int[] {
				R.string.label_select, R.string.label_capture }, "photo");
	}

	public PopupWindow showBottomPop(View parent,
			OnClickListener[] onClickListeners, int[] infoResId, String tag) {
		return showBottomPop(parent, onClickListeners, infoResId, 20, tag);
	}

	public PopupWindow showBottomPop(View parent,
			OnClickListener[] onClickListeners, int[] infoResId, int margin,
			String tag) {
		String[] info = new String[infoResId.length];
		for (int i = 0; i < infoResId.length; i++) {
			info[i] = mActivity.getString(infoResId[i]);
		}
		return showBottomPop(parent, onClickListeners, info, margin, tag);
	}

	public PopupWindow showBottomPop(View parent,
			OnClickListener[] onClickListeners, String[] info, String tag) {
		return showBottomPop(parent, onClickListeners, info, 20, tag);
	}

	public PopupWindow showBreakQuanzi(int tag,View parent,int layoutId,int margin,OnClickListener breakBtnListener){
		if (null != parent) {
			InputMethodManager imm = (InputMethodManager) mActivity
					.getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(parent.getWindowToken(), 0);
		}
		if (null == breakQaunziPopupWindow ||(Integer)viewBreakQuanzi.getTag()!=tag) {
			LayoutInflater layoutInflater = (LayoutInflater) mActivity
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			viewBreakQuanzi= layoutInflater
					.inflate(layoutId, null);
			viewBreakQuanzi.setTag(tag);
			breakQaunziPopupWindow = new PopupWindow(viewBreakQuanzi,
					LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
			
			//隐藏解散圈子
			viewBreakQuanzi.findViewById(R.id.fql_close).setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					breakQaunziPopupWindow.dismiss();
				}
			});
			final TextView breakReason = (TextView)viewBreakQuanzi.findViewById(R.id.fql_break_reason);
			final Button breakBtn = (Button)viewBreakQuanzi.findViewById(R.id.fql_break_btn);
			//确定按钮
			breakReason.addTextChangedListener(new TextWatcher() {
				
				@Override
				public void onTextChanged(CharSequence s, int start, int before, int count) {
					// TODO Auto-generated method stub
					if(!breakReason.getText().toString().isEmpty()){
						breakBtn.setEnabled(true);
					}else{
						breakBtn.setEnabled(false);
					}
				}
				
				@Override
				public void beforeTextChanged(CharSequence s, int start, int count,
						int after) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void afterTextChanged(Editable s) {
					// TODO Auto-generated method stub
					
				}
			});
			breakBtn.setOnClickListener(breakBtnListener);
		}
		setPopupWindowParams(breakQaunziPopupWindow);
		breakQaunziPopupWindow.showAtLocation(parent, Gravity.CENTER_HORIZONTAL, 0, margin);
		return breakQaunziPopupWindow;
	}
	/**
	 * 底部窗口，可以有多个button
	 * 
	 * @param parent
	 * @param onClickListeners
	 * @param info
	 * @param margin
	 * @param tag
	 * @return
	 */
	public PopupWindow showBottomPop(View parent,
			OnClickListener[] onClickListeners, String[] info, int margin,
			String tag) {
		if (null != parent) {
			InputMethodManager imm = (InputMethodManager) mActivity
					.getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(parent.getWindowToken(), 0);
		}
		if (null == popupWindowBottom || (String) viewBottom.getTag() != tag) {
			LayoutInflater layoutInflater = (LayoutInflater) mActivity
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			viewBottom = layoutInflater
					.inflate(R.layout.pop_bottom_group, null);
			popupWindowBottom = new PopupWindow(viewBottom,
					LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
			for (int i = 0; i < onClickListeners.length; i++) {
				RelativeLayout rl = new RelativeLayout(mActivity);
				LayoutParams lllp = new LayoutParams(LayoutParams.MATCH_PARENT,
						LayoutParams.WRAP_CONTENT);
				lllp.topMargin = (int) (margin * DeviceInfoUtil
						.getDeviceCsd(mActivity));
				rl.setLayoutParams(lllp);
				viewBottom.findViewById(R.id.relativeLayoutCancel)
						.setLayoutParams(lllp);
				viewBottom.setTag(tag);
				Button button = new Button(mActivity);
				RelativeLayout.LayoutParams rllp = new RelativeLayout.LayoutParams(
						RelativeLayout.LayoutParams.WRAP_CONTENT,
						RelativeLayout.LayoutParams.WRAP_CONTENT);
				rllp.addRule(RelativeLayout.CENTER_IN_PARENT);
				if (info[i].equals(mActivity.getString(R.string.label_del))
						|| info[i].equals(mActivity
								.getString(R.string.label_black))) {
					button.setBackgroundResource(R.drawable.button_bg_red);
					button.setTextColor(Color.WHITE);
				} else {
					button.setBackgroundResource(R.drawable.button_bg_white2);
				}
				button.setText(info[i]);
				button.setLayoutParams(rllp);
				final OnClickListener onClickListener = onClickListeners[i];
				button.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View paramView) {
						popupWindowBottom.dismiss();
						onClickListener.onClick(paramView);
					}
				});
				rl.addView(button);
				((LinearLayout) viewBottom
						.findViewById(R.id.linearLayoutButtonContainer))
						.addView(rl);
			}
			viewBottom.findViewById(R.id.buttonCancel).setOnClickListener(
					new OnClickListener() {

						@Override
						public void onClick(View v) {
							popupWindowBottom.dismiss();
						}
					});
		}
		setPopupWindowParams(popupWindowBottom);
		popupWindowBottom.showAtLocation(parent, Gravity.BOTTOM
				| Gravity.CENTER_HORIZONTAL, 0, 0);
		return popupWindowBottom;
	}

	/**
	 * 底部推荐等
	 * 
	 * @param parent
	 * @param zanClickListener
	 * @param cmtClickListener
	 * @param recommandClickListener
	 * @param collectClickListener
	 * @param isCollect
	 * @return
	 */
	public PopupWindow showBottomOption(View parent,
			final OnClickListener zanClickListener,
			final OnClickListener cmtClickListener,
			final OnClickListener recommandClickListener,
			final OnClickListener collectClickListener, boolean isCollect) {
		if (null != parent) {
			InputMethodManager imm = (InputMethodManager) mActivity
					.getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(parent.getWindowToken(), 0);
		}
		if (null == popupWindowBottom) {
			LayoutInflater layoutInflater = (LayoutInflater) mActivity
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			viewBottom = layoutInflater.inflate(R.layout.pop_bottom_option,
					null);
			popupWindowBottom = new PopupWindow(viewBottom,
					LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
			viewBottom.findViewById(R.id.buttonTabZan).setOnClickListener(
					new OnClickListener() {
						@Override
						public void onClick(View paramView) {
							popupWindowBottom.dismiss();
							zanClickListener.onClick(paramView);
						}
					});
			viewBottom.findViewById(R.id.buttonTabCmt).setOnClickListener(
					new OnClickListener() {
						@Override
						public void onClick(View paramView) {
							popupWindowBottom.dismiss();
							cmtClickListener.onClick(paramView);
						}
					});
			viewBottom.findViewById(R.id.buttonTabZf).setOnClickListener(
					new OnClickListener() {
						@Override
						public void onClick(View paramView) {
							popupWindowBottom.dismiss();
							recommandClickListener.onClick(paramView);
						}
					});
			TextView buttonCollect = (TextView) viewBottom
					.findViewById(R.id.buttonTabCollect);
			if (isCollect) {
				Drawable drawable = mActivity.getResources().getDrawable(
						R.drawable.tab_collect_on);
				drawable.setBounds(0, 0, drawable.getMinimumWidth(),
						drawable.getMinimumHeight());
				buttonCollect.setCompoundDrawables(null, drawable, null, null);
				buttonCollect.setText(R.string.label_collectCancel);
			}
			buttonCollect.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View paramView) {
					popupWindowBottom.dismiss();
					collectClickListener.onClick(paramView);
				}
			});
			viewBottom.findViewById(R.id.buttonCancel).setOnClickListener(
					new OnClickListener() {

						@Override
						public void onClick(View v) {
							popupWindowBottom.dismiss();
						}
					});
		}
		setPopupWindowParams(popupWindowBottom);
		popupWindowBottom.showAtLocation(parent, Gravity.BOTTOM
				| Gravity.CENTER_HORIZONTAL, 0, 0);
		return popupWindowBottom;
	}

	@SuppressWarnings("deprecation")
	public PopupWindow showTopPop(View parent,
			final ListItemSelected listItemSelectedListener) {
		if (null != parent) {
			InputMethodManager imm = (InputMethodManager) mActivity
					.getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(parent.getWindowToken(), 0);
		}
		if (null == popupWindow) {
			LayoutInflater layoutInflater = (LayoutInflater) mActivity
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = layoutInflater.inflate(R.layout.pop_top_list, null);
			lv_group = (ListView) view.findViewById(R.id.listViewTopPop);
			lv_group.setDividerHeight(0);
			groups = new ArrayList<String>();
			String[] reses = mActivity.getResources().getStringArray(
					R.array.array_res_type);
			for (String res : reses) {
				groups.add(res);
			}
			String ft = mResHelper.getFilterType();
			rAdapter = new ResFilterListAdapter(lv_group.getContext(), groups,
					ft);
			lv_group.setAdapter(rAdapter);
			popupWindow = new PopupWindow(view, LayoutParams.WRAP_CONTENT,
					LayoutParams.WRAP_CONTENT);
		}
		setPopupWindowParams(popupWindow);
		WindowManager windowManager = (WindowManager) mActivity
				.getSystemService(Context.WINDOW_SERVICE);
		int xPos = windowManager.getDefaultDisplay().getWidth() / 2
				- popupWindow.getWidth() / 2;
		popupWindow.showAsDropDown(parent, xPos, 0);

		lv_group.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				rAdapter.getmSelectView().findViewById(R.id.imageViewTick)
						.setVisibility(View.GONE);
				rAdapter.setmSelectView(arg1);
				arg1.findViewById(R.id.imageViewTick).setVisibility(
						View.VISIBLE);
				HashMap<String, Object> map = new HashMap<String, Object>();
				map.put(ResHelper.FILTER_TYPE, groups.get(arg2));
				mResHelper.setPreference(map);
				if (null != popupWindow) {
					popupWindow.dismiss();
				}
				if (listItemSelectedListener != null) {
					listItemSelectedListener.onSelected(groups.get(arg2));
				}
			}
		});
		return popupWindow;
	}

	@SuppressWarnings("deprecation")
	public PopupWindow showTopPop(View parent, final ArrayList<String> groups,
			final ListItemSelected listItemSelectedListener) {
		if (null != parent) {
			InputMethodManager imm = (InputMethodManager) mActivity
					.getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(parent.getWindowToken(), 0);
		}
		if (null == popupWindow) {
			LayoutInflater layoutInflater = (LayoutInflater) mActivity
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = layoutInflater.inflate(R.layout.pop_top_list, null);
			View rlt = view.findViewById(R.id.relativeLayoutTitle);
			android.view.ViewGroup.LayoutParams lp = rlt.getLayoutParams();
			lp.height = 1;
			rlt.setLayoutParams(lp);
			rlt.setVisibility(View.INVISIBLE);
			lv_group = (ListView) view.findViewById(R.id.listViewTopPop);
			lv_group.setDividerHeight(0);
			tAdapter = new TypeFilterListAdapter(lv_group.getContext(), groups,
					null);
			lv_group.setAdapter(tAdapter);
			popupWindow = new PopupWindow(view, LayoutParams.WRAP_CONTENT,
					LayoutParams.WRAP_CONTENT);
		}
		setPopupWindowParams(popupWindow);

		WindowManager windowManager = (WindowManager) mActivity
				.getSystemService(Context.WINDOW_SERVICE);
		int xPos = windowManager.getDefaultDisplay().getWidth() / 2
				- popupWindow.getWidth() / 2;
		popupWindow.showAsDropDown(parent, xPos, 0);

		lv_group.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				View selected = tAdapter.getmSelectView();
				if (selected != null) {
					selected.findViewById(R.id.imageViewTick).setVisibility(
							View.GONE);
				}
				tAdapter.setmSelectView(arg1);
				arg1.findViewById(R.id.imageViewTick).setVisibility(
						View.VISIBLE);
				if (null != popupWindow) {
					popupWindow.dismiss();
				}
				if (listItemSelectedListener != null) {
					listItemSelectedListener.onSelected(groups.get(arg2));
				}
			}
		});
		return popupWindow;
	}

	@SuppressWarnings("deprecation")
	public PopupWindow showWheelPop(View parent, final String[] type1,
			final String[][] type2) {
		if (null != parent) {
			InputMethodManager imm = (InputMethodManager) mActivity
					.getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(parent.getWindowToken(), 0);
		}
		if (null == popupWindow) {
			LayoutInflater layoutInflater = (LayoutInflater) mActivity
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = layoutInflater.inflate(R.layout.pop_wheel, null);
			final WheelView wheel1 = (WheelView) view
					.findViewById(R.id.wheelType1);
			wheel1.setVisibleItems(2);
			wheel1.setViewAdapter(new ArrayWheelAdapter<String>(mActivity,
					type1));
			final WheelView wheel2 = (WheelView) view
					.findViewById(R.id.wheelType2);
			wheel2.setVisibleItems(8);
			wheel1.addChangingListener(new OnWheelChangedListener() {
				public void onChanged(WheelView wheel, int oldValue,
						int newValue) {
					if (!scrolling) {
						updateWheel(wheel2, type2, newValue);
					}
				}
			});
			wheel1.addScrollingListener(new OnWheelScrollListener() {
				public void onScrollingStarted(WheelView wheel) {
					scrolling = true;
				}

				public void onScrollingFinished(WheelView wheel) {
					scrolling = false;
					updateWheel(wheel2, type2, wheel1.getCurrentItem());
				}
			});
			wheel1.setCurrentItem(1);
			popupWindow = new PopupWindow(view, LayoutParams.WRAP_CONTENT,
					LayoutParams.WRAP_CONTENT);
		}
		setPopupWindowParams(popupWindow);
		WindowManager windowManager = (WindowManager) mActivity
				.getSystemService(Context.WINDOW_SERVICE);
		int xPos = windowManager.getDefaultDisplay().getWidth() / 2
				- popupWindow.getWidth() / 2;
		popupWindow.showAsDropDown(parent, xPos, 0);
		return popupWindow;
	}

	@SuppressWarnings("deprecation")
	public PopupWindow showWheelPop(View parent, final String[] type1,
			final String[] type2, final WheelOKClick wheelOKClick) {
		if (null != parent) {
			InputMethodManager imm = (InputMethodManager) mActivity
					.getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(parent.getWindowToken(), 0);
		}
		if (null == popupWindow) {
			LayoutInflater layoutInflater = (LayoutInflater) mActivity
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = layoutInflater.inflate(R.layout.pop_wheel, null);
			final WheelView wheel1 = (WheelView) view
					.findViewById(R.id.wheelType1);
			wheel1.setVisibleItems(type1.length);
			final ArrayWheelAdapter<String> adapter1 = new ArrayWheelAdapter<String>(
					mActivity, type1);
			adapter1.setTextSize(19);
			wheel1.setViewAdapter(adapter1);
			wheel1.setCurrentItem(type1.length / 2);

			final WheelView wheel2 = (WheelView) view
					.findViewById(R.id.wheelType2);
			wheel2.setVisibleItems(type2.length);
			final ArrayWheelAdapter<String> adapter2 = new ArrayWheelAdapter<String>(
					mActivity, type2);
			adapter2.setTextSize(19);
			wheel2.setViewAdapter(adapter2);
			wheel2.setCurrentItem(type2.length / 2);
			view.findViewById(R.id.buttonOK).setOnClickListener(
					new OnClickListener() {

						@Override
						public void onClick(View v) {
							popupWindow.dismiss();
							String[] selected = new String[] {
									adapter1.getItemText(
											wheel1.getCurrentItem()).toString(),
									adapter2.getItemText(
											wheel2.getCurrentItem()).toString() };
							int[] selectedId = new int[] {
									wheel1.getCurrentItem(),
									wheel2.getCurrentItem() };
							wheelOKClick.onClick(selected, selectedId);
						}
					});
			popupWindow = new PopupWindow(view, LayoutParams.WRAP_CONTENT,
					LayoutParams.WRAP_CONTENT);
		}
		setPopupWindowParams(popupWindow);
		WindowManager windowManager = (WindowManager) mActivity
				.getSystemService(Context.WINDOW_SERVICE);
		int xPos = windowManager.getDefaultDisplay().getWidth() / 2
				- popupWindow.getWidth() / 2;
		popupWindow.showAsDropDown(parent, xPos, 0);
		return popupWindow;
	}

	private void updateWheel(WheelView wheel, String type[][], int index) {
		ArrayWheelAdapter<String> adapter = new ArrayWheelAdapter<String>(
				mActivity, type[index]);
		adapter.setTextSize(18);
		wheel.setViewAdapter(adapter);
		wheel.setCurrentItem(type[index].length / 2);
	}

	public PopupWindow showFullScreenPop(View parent, String filePath) {
		if (null != parent) {
			InputMethodManager imm = (InputMethodManager) mActivity
					.getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(parent.getWindowToken(), 0);
		}
		if (null == fullScreenPopupWindow || fullScreenView != null
				|| fullScreenView.getTag() != null
				|| !fullScreenView.getTag().toString().equals(filePath)) {
			LayoutInflater layoutInflater = (LayoutInflater) mActivity
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			fullScreenView = layoutInflater.inflate(R.layout.pop_image, null);
			fullScreenView.setTag(filePath);
			mImageView = (ImageView) fullScreenView
					.findViewById(R.id.popImageShow);
			fullScreenPopupWindow = new PopupWindow(fullScreenView,
					LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		}
		// try {
		// mImageView.setImageBitmap(ImageRectUtil.memoryMaxImage(filePath));
		// } catch (IOException e) {
		// CommonUtil.displayToast(getApplicationContext(),
		// R.string.error1);
		// e.printStackTrace();
		// }
		mImageView.setImageURI(Uri.fromFile(new File(filePath)));
		setPopupWindowParams(fullScreenPopupWindow);
		fullScreenPopupWindow.showAtLocation(
				fullScreenView.findViewById(R.id.popImageShowParent),
				Gravity.CENTER, 0, 0);
		return fullScreenPopupWindow;

	}

	public PopupWindow showPopDlg(View parent, final OnClickListener ok,
			final OnClickListener cancel, int title) {
		return showPopDlg(parent, R.string.OK, R.string.CANCEL, ok, cancel,
				title);
	}

	public PopupWindow showPopDlg(View parent, final OnClickListener ok,
			final OnClickListener cancel, String title) {
		return showPopDlg(parent, mActivity.getString(R.string.OK),
				mActivity.getString(R.string.CANCEL), ok, cancel, title);
	}

	public PopupWindow showPopDlg(View parent, int okText, int cancelText,
			OnClickListener ok, OnClickListener cancel, int title) {
		return showPopDlg(parent, mActivity.getString(okText),
				mActivity.getString(cancelText), ok, cancel,
				mActivity.getString(title));
	}

	public PopupWindow showPopDlg(View parent, String okText,
			String cancelText, final OnClickListener ok,
			final OnClickListener cancel, String title) {
		if (null != parent) {
			InputMethodManager imm = (InputMethodManager) mActivity
					.getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(parent.getWindowToken(), 0);
		}
		if (null == popupWindowDlg || viewDlg != null
				|| viewDlg.getTag() != null
				|| !viewDlg.getTag().toString().equals(title)) {
			LayoutInflater layoutInflater = (LayoutInflater) mActivity
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			viewDlg = layoutInflater.inflate(R.layout.pop_middle_dlg, null);
			viewDlg.setTag(title);
			popupWindowDlg = new PopupWindow(viewDlg,
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			Button okButton = (Button) viewDlg.findViewById(R.id.buttonOK);
			okButton.setText(okText);
			okButton.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if (null != popupWindowDlg) {
						popupWindowDlg.dismiss();
					}
					if (ok != null) {
						ok.onClick(v);
					}
				}
			});
			Button cancelButton = (Button) viewDlg
					.findViewById(R.id.buttonCancel);
			cancelButton.setText(cancelText);
			cancelButton.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if (null != popupWindowDlg) {
						popupWindowDlg.dismiss();
					}
					if (cancel != null) {
						cancel.onClick(v);
					}
				}
			});
			TextView titleView = (TextView) viewDlg
					.findViewById(R.id.textViewTitle);
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
		setPopupWindowParams(popupWindowDlg);
		popupWindowDlg.showAtLocation(parent, Gravity.CENTER
				| Gravity.CENTER_HORIZONTAL, 0, 0);
		return popupWindowDlg;
	}

	public PopupWindow showPopDlgEdit(View parent, OnClickListener ok,
			OnClickListener cancel, int title, String text) {
		return showPopDlgEdit(parent, ok, cancel, mActivity.getString(title),
				text);
	}

	public PopupWindow showPopDlgEdit(View parent, final OnClickListener ok,
			final OnClickListener cancel, String title, String text) {
		if (null != parent) {
			InputMethodManager imm = (InputMethodManager) mActivity
					.getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(parent.getWindowToken(), 0);
		}
		if (null == popupWindowDlg || viewDlg != null
				|| viewDlg.getTag() != null
				|| !viewDlg.getTag().toString().equals(title)) {
			LayoutInflater layoutInflater = (LayoutInflater) mActivity
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			viewDlg = layoutInflater
					.inflate(R.layout.pop_middle_dlg_edit, null);
			viewDlg.setTag(title);
			popupWindowDlg = new PopupWindow(viewDlg,
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			final EditText edit = (EditText) viewDlg
					.findViewById(R.id.editText);
			edit.setOnEditorActionListener(new OnEditorActionListener() {

				@Override
				public boolean onEditorAction(TextView v, int actionId,
						KeyEvent event) {
					if (actionId == EditorInfo.IME_ACTION_DONE) {
						InputMethodManager imm = (InputMethodManager) mActivity
								.getSystemService(Context.INPUT_METHOD_SERVICE);
						imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
						if (null != popupWindowDlg) {
							popupWindowDlg.dismiss();
						}
						if (ok != null) {
							String value = v.getText().toString();
							v.setTag(value);
							ok.onClick(v);
						}
					}
					return false;
				}
			});
			viewDlg.findViewById(R.id.buttonOK).setOnClickListener(
					new OnClickListener() {

						@Override
						public void onClick(View v) {
							if (null != popupWindowDlg) {
								popupWindowDlg.dismiss();
							}
							if (ok != null) {
								String value = edit.getText().toString();
								v.setTag(value);
								ok.onClick(v);
							}
						}
					});
			viewDlg.findViewById(R.id.buttonCancel).setOnClickListener(
					new OnClickListener() {

						@Override
						public void onClick(View v) {
							if (null != popupWindowDlg) {
								popupWindowDlg.dismiss();
							}
							if (cancel != null) {
								cancel.onClick(v);
							}
						}
					});
			((EditText) viewDlg.findViewById(R.id.editText)).setText(text);
			TextView titleView = (TextView) viewDlg
					.findViewById(R.id.textViewTitle);
			titleView.setText(title);
			if (title.length() > 14) {
				titleView.setGravity(3);
			} else {
				titleView.setGravity(17);
			}
			if (title.length() > 27) {
				titleView.setTextSize(14);
			} else {
				titleView.setTextSize(18);
			}
		}
		setPopupWindowParams(popupWindowDlg, true);
		popupWindowDlg.showAtLocation(parent, Gravity.CENTER
				| Gravity.CENTER_HORIZONTAL, 0, 0);
		return popupWindowDlg;
	}

	public PopupWindow showPopDlgOne(View parent, OnClickListener ok, int title) {
		return showPopDlgOne(parent, ok, mActivity.getString(title));
	}

	public PopupWindow showPopDlgOne(View parent, final OnClickListener ok,
			String title) {
		if (null != parent) {
			InputMethodManager imm = (InputMethodManager) mActivity
					.getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(parent.getWindowToken(), 0);
		}
		if (null == popupWindowDlgOne || viewDlgOne != null
				|| viewDlgOne.getTag() != null
				|| !viewDlgOne.getTag().toString().equals(title)) {
			LayoutInflater layoutInflater = (LayoutInflater) mActivity
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			viewDlgOne = layoutInflater.inflate(R.layout.pop_middle_dlg_one,
					null);
			viewDlgOne.setTag(title);
			popupWindowDlgOne = new PopupWindow(viewDlgOne,
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			Button okbutton = (Button) viewDlgOne.findViewById(R.id.buttonOK);
			okbutton.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if (null != popupWindowDlgOne) {
						popupWindowDlgOne.dismiss();
					}
					if (ok != null) {
						ok.onClick(v);
					}
				}
			});
			TextView titleView = (TextView) viewDlgOne
					.findViewById(R.id.textViewTitle);
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
		setPopupWindowParams(popupWindowDlgOne, true);
		popupWindowDlgOne.showAtLocation(parent, Gravity.CENTER
				| Gravity.CENTER_HORIZONTAL, 0, 0);
		return popupWindowDlgOne;
	}

	public PopupWindow showPopTip(View parent, final OnClickListener ok,
			String tipInfo) {
		if (null != parent) {
			InputMethodManager imm = (InputMethodManager) mActivity
					.getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(parent.getWindowToken(), 0);
		}
		if (null == popupWindowTip || viewTip != null
				|| viewTip.getTag() != null
				|| !viewTip.getTag().toString().equals(tipInfo)) {
			LayoutInflater layoutInflater = (LayoutInflater) mActivity
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			viewTip = layoutInflater.inflate(R.layout.pop_bottom_tip, null);
			viewTip.setTag(tipInfo);
			popupWindowTip = new PopupWindow(viewTip,
					LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
			viewTip.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if (null != popupWindowTip) {
						popupWindowTip.dismiss();
					}
					if (ok != null) {
						ok.onClick(v);
					}
				}
			});
			((TextView) viewTip.findViewById(R.id.textViewInfo))
					.setText(tipInfo);
		}
		setPopupWindowParams(popupWindowTip);
		popupWindowTip.showAtLocation(parent, Gravity.BOTTOM
				| Gravity.CENTER_HORIZONTAL, 0, 0);
		return popupWindowTip;
	}

	public PopupWindow showRecordDlg(View parent) {
		InputMethodManager imm = (InputMethodManager) mActivity
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(parent.getWindowToken(), 0);
		if (null == popupWindow) {
			LayoutInflater layoutInflater = (LayoutInflater) mActivity
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View view = layoutInflater
					.inflate(R.layout.pop_middle_window, null);
			popupWindow = new PopupWindow(view,
					RelativeLayout.LayoutParams.WRAP_CONTENT,
					RelativeLayout.LayoutParams.WRAP_CONTENT);
		}
		popupWindow.showAtLocation(parent, Gravity.CENTER, 0, 0);
		return popupWindow;
	}

	private void setPopupWindowParams(PopupWindow popupWindow) {
		setPopupWindowParams(popupWindow, false);
	}

	@SuppressWarnings("deprecation")
	private void setPopupWindowParams(PopupWindow popupWindow, boolean focus) {
		popupWindow.setFocusable(true);
		popupWindow.setOutsideTouchable(true);
		popupWindow.setAnimationStyle(R.style.AnimBottom);
		if (!focus) {
			popupWindow.setBackgroundDrawable(new BitmapDrawable());
		}
	}

	public void showPopTip(View parent, OnClickListener ok, int tipResId) {
		showPopTip(parent, ok, mActivity.getString(tipResId));
	}

	@SuppressWarnings("deprecation")
	public PopupWindow showOptionsPop(View parent, float times,
			final OnClickListener good, final OnClickListener cmt) {
		if (null != parent) {
			InputMethodManager imm = (InputMethodManager) mActivity
					.getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(parent.getWindowToken(), 0);
		}
		if (null == popupWindowOptions) {
			LayoutInflater layoutInflater = (LayoutInflater) mActivity
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			viewOptions = layoutInflater.inflate(R.layout.pop_options, null);
			popupWindowOptions = new PopupWindow(viewOptions,
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		}
		viewOptions.findViewById(R.id.buttonAuthorGongXuZan)
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						if (null != popupWindowOptions) {
							popupWindowOptions.dismiss();
						}
						if (good != null) {
							good.onClick(v);
						}
					}
				});
		viewOptions.findViewById(R.id.buttonAuthorGongXuCmt)
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						if (null != popupWindowOptions) {
							popupWindowOptions.dismiss();
						}
						if (cmt != null) {
							cmt.onClick(v);
						}
					}
				});
		popupWindowOptions.setFocusable(false);
		popupWindowOptions.setOutsideTouchable(true);
		popupWindowOptions.setAnimationStyle(R.style.AnimBottomScaleRM);
		popupWindowOptions.setBackgroundDrawable(new BitmapDrawable());
		int[] location = new int[2];
		parent.getLocationOnScreen(location);
		popupWindowOptions.showAtLocation(parent, Gravity.NO_GRAVITY,
				(int) (location[0] - times * 212),
				(int) (location[1] - times * 10));
		return popupWindowOptions;
	}

	
	@SuppressWarnings("deprecation")
	public PopupWindow showAddOptionsPop(View parent, float times,
			final OnClickListener pub, final OnClickListener invite) {
		if (null != parent) {
			InputMethodManager imm = (InputMethodManager) mActivity
					.getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(parent.getWindowToken(), 0);
		}
		if (null == popupWindowOptions) {
			LayoutInflater layoutInflater = (LayoutInflater) mActivity
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			viewOptions = layoutInflater.inflate(R.layout.pop_add_options, null);
			popupWindowOptions = new PopupWindow(viewOptions,
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		}
		viewOptions.findViewById(R.id.buttonPubActivity)
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						if (null != popupWindowOptions) {
							popupWindowOptions.dismiss();
						}
						if (pub != null) {
							pub.onClick(v);
						}
					}
				});
		viewOptions.findViewById(R.id.buttonInviteFriends)
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						if (null != popupWindowOptions) {
							popupWindowOptions.dismiss();
						}
						if (invite != null) {
							invite.onClick(v);
						}
					}
				});
		popupWindowOptions.setFocusable(false);
		popupWindowOptions.setOutsideTouchable(true);
		popupWindowOptions.setAnimationStyle(R.style.AnimBottomScaleRM);
		popupWindowOptions.setBackgroundDrawable(new BitmapDrawable());
		int[] location = new int[2];
		parent.getLocationOnScreen(location);
		popupWindowOptions.showAtLocation(parent, Gravity.NO_GRAVITY,
				(int) (location[0] + times * 8),
				(int) (location[1] + times * 35));
		return popupWindowOptions;
	}

	
	@SuppressWarnings("deprecation")
	public PopupWindow showCmtOptionsPop(View parent, float times,
			final OnClickListener good, final OnClickListener cmt,
			final OnClickListener copy, final View v) {
		if (null != parent) {
			InputMethodManager imm = (InputMethodManager) mActivity
					.getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(parent.getWindowToken(), 0);
		}
		if (null == popupWindowOptions) {
			LayoutInflater layoutInflater = (LayoutInflater) mActivity
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			viewOptions = layoutInflater
					.inflate(R.layout.pop_options_cmt, null);
			popupWindowOptions = new PopupWindow(viewOptions,
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		}
		viewOptions.findViewById(R.id.viewGood).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						if (null != popupWindowOptions) {
							popupWindowOptions.dismiss();
						}
						if (good != null) {
							good.onClick(v);
						}
					}
				});
		viewOptions.findViewById(R.id.viewCmt).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						if (null != popupWindowOptions) {
							popupWindowOptions.dismiss();
						}
						if (cmt != null) {
							cmt.onClick(v);
						}
					}
				});
		viewOptions.findViewById(R.id.viewCopy).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						if (null != popupWindowOptions) {
							popupWindowOptions.dismiss();
						}
						if (copy != null) {
							copy.onClick(v);
						}
					}
				});
		popupWindowOptions.setFocusable(false);
		popupWindowOptions.setOutsideTouchable(true);
		popupWindowOptions.setAnimationStyle(R.style.AnimBottomScaleBM);
		popupWindowOptions.setBackgroundDrawable(new BitmapDrawable());
		int[] location = new int[2];
		parent.getLocationOnScreen(location);
		popupWindowOptions.showAtLocation(parent, Gravity.NO_GRAVITY,
				location[0], (int) (location[1] - 32 * times));
		popupWindowOptions.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss() {
				v.setBackgroundColor(Color.TRANSPARENT);
			}
		});
		return popupWindowOptions;
	}

	public void hidePop() {
		if (popupWindow != null) {
			popupWindow.dismiss();
		} else if (popupWindowBottom != null) {
			popupWindowBottom.dismiss();
		} else if (popupWindowDlg != null) {
			popupWindowDlg.dismiss();
		} else if (popupWindowDlgOne != null) {
			popupWindowDlgOne.dismiss();
		} else if (popupWindowTip != null) {
			popupWindowTip.dismiss();
		} else if (popupWindowOptions != null) {
			popupWindowOptions.dismiss();
		}
	}

	public interface ListItemSelected {
		public String onSelected(String str);
	}

	public interface WheelOKClick {
		public String onClick(String[] selected, int[] selectedId);
	}
}
