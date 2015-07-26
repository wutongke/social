package com.zhuojiaren.sortlistview;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.cpstudio.zhuojiaren.R;
import com.cpstudio.zhuojiaren.ui.GrouthListActivity;
import com.zhuojiaren.sortlistview.SideBar.OnTouchingLetterChangedListener;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout.LayoutParams;


public class NamePup {

	Context mContext;
	View view ;

	public NamePup(Context context,View view) {
		mContext = context;
		this.view = view;
	}

	public  void showPup() {
		// TODO Auto-generated method stub
		showView(view);
	}

	ListView sortListView;
	SideBar sideBar;
	TextView dialog;
	SortAdapter adapter;
	ClearEditText mClearEditText;

	/**
	 * 汉字转换成拼音的类
	 */
	CharacterParser characterParser;
	List<SortModel> SourceDateList;
	/**
	 * 根据拼音来排列ListView里面的数据类
	 */
	PinyinComparator pinyinComparator;

	public PopupWindow showView(View parent) {
		if (null != parent) {
			InputMethodManager imm = (InputMethodManager) mContext
					.getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(parent.getWindowToken(), 0);
		}
		LayoutInflater layoutInflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View viewBreakQuanzi = layoutInflater.inflate(R.layout.pup_name_list,
				null);
		PopupWindow popupWindow = new PopupWindow(viewBreakQuanzi,
				LayoutParams.MATCH_PARENT, (int) (view.getHeight()*0.6));

		

		// 实例化汉字转拼音类
		characterParser = CharacterParser.getInstance();

		pinyinComparator = new PinyinComparator();

		sideBar = (SideBar) viewBreakQuanzi.findViewById(R.id.sidrbar);
		dialog = (TextView) viewBreakQuanzi.findViewById(R.id.dialog);
		sideBar.setTextView(dialog);

		// 设置右侧触摸监听
		sideBar.setOnTouchingLetterChangedListener(new OnTouchingLetterChangedListener() {

			@Override
			public void onTouchingLetterChanged(String s) {
				// 该字母首次出现的位置
				int position = adapter.getPositionForSection(s.charAt(0));
				if (position != -1) {
					sortListView.setSelection(position);
				}

			}
		});

		sortListView = (ListView) viewBreakQuanzi.findViewById(R.id.country_lvcountry);
		sortListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// 这里要利用adapter.getItem(position)来获取当前position所对应的对象
				Toast.makeText(mContext,
						((SortModel) adapter.getItem(position)).getName(),
						Toast.LENGTH_SHORT).show();
				Intent intent = new Intent(mContext,GrouthListActivity.class);
				intent.putExtra("teacher",SourceDateList.get(position).getName());
				mContext.startActivity(intent);
				((Activity) mContext).finish();
			}
		});

		SourceDateList = filledData(mContext.getResources().getStringArray(R.array.date));

		// 根据a-z进行排序源数据
		Collections.sort(SourceDateList, pinyinComparator);
		adapter = new SortAdapter(mContext, SourceDateList);
		sortListView.setAdapter(adapter);

		mClearEditText = (ClearEditText) viewBreakQuanzi.findViewById(R.id.filter_edit);

		// 根据输入框输入值的改变来过滤搜索
		mClearEditText.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// 当输入框里面的值为空，更新为原来的列表，否则为过滤数据列表
				filterData(s.toString());
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {
			}
		});

		setPopupWindowParams(popupWindow);
		popupWindow.showAtLocation(parent,
				Gravity.BOTTOM, 0, 0);
		return popupWindow;
	}

	private List<SortModel> filledData(String[] date) {
		List<SortModel> mSortList = new ArrayList<SortModel>();

		for (int i = 0; i < date.length; i++) {
			SortModel sortModel = new SortModel();
			sortModel.setName(date[i]);
			// 汉字转换成拼音
			String pinyin = characterParser.getSelling(date[i]);
			String sortString = pinyin.substring(0, 1).toUpperCase();

			// 正则表达式，判断首字母是否是英文字母
			if (sortString.matches("[A-Z]")) {
				sortModel.setSortLetters(sortString.toUpperCase());
			} else {
				sortModel.setSortLetters("#");
			}

			mSortList.add(sortModel);
		}
		return mSortList;

	}

	/**
	 * 根据输入框中的值来过滤数据并更新ListView
	 * 
	 * @param filterStr
	 */
	private void filterData(String filterStr) {
		List<SortModel> filterDateList = new ArrayList<SortModel>();

		if (TextUtils.isEmpty(filterStr)) {
			filterDateList = SourceDateList;
		} else {
			filterDateList.clear();
			for (SortModel sortModel : SourceDateList) {
				String name = sortModel.getName();
				if (name.indexOf(filterStr.toString()) != -1
						|| characterParser.getSelling(name).startsWith(
								filterStr.toString())) {
					filterDateList.add(sortModel);
				}
			}
		}

		// 根据a-z进行排序
		Collections.sort(filterDateList, pinyinComparator);
		adapter.updateListView(filterDateList);
	}

	private void setPopupWindowParams(PopupWindow popupWindow) {
		setPopupWindowParams(popupWindow, false);
	}

	@SuppressWarnings("deprecation")
	private void setPopupWindowParams(PopupWindow popupWindow, boolean focus) {
		popupWindow.setFocusable(true);
		popupWindow.setOutsideTouchable(true);
		// popupWindow.setAnimationStyle(R.style.AnimBottom);
		if (!focus) {
			popupWindow.setBackgroundDrawable(new BitmapDrawable());
		}
	}
}
