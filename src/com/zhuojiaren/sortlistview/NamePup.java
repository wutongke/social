package com.zhuojiaren.sortlistview;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.cpstudio.zhuojiaren.R;
import com.cpstudio.zhuojiaren.helper.AppClientLef;
import com.cpstudio.zhuojiaren.helper.JsonHandler;
import com.cpstudio.zhuojiaren.model.Province;
import com.cpstudio.zhuojiaren.model.ResultVO;
import com.cpstudio.zhuojiaren.model.Teacher;
import com.cpstudio.zhuojiaren.ui.GrouthListActivity;
import com.cpstudio.zhuojiaren.util.CommonUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhuojiaren.sortlistview.SideBar.OnTouchingLetterChangedListener;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
	 * ����ת����ƴ������
	 */
	CharacterParser characterParser;
	List<SortModel> SourceDateList;
	/**
	 * ����ƴ��������ListView�����������
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

		//��������
		AppClientLef appClient = AppClientLef.getInstance(mContext);
		appClient.getTeacherList(new Handler(){
			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				super.handleMessage(msg);
				ResultVO res;
				if (JsonHandler.checkResult((String) msg.obj, mContext)) {
					res = JsonHandler.parseResult((String) msg.obj);
				} else {
					CommonUtil.displayToast(mContext, R.string.error0);
					return;
				}
				String data = res.getData();
				Type listType = new TypeToken<ArrayList<Teacher>>() {
				}.getType();
				Gson gson = new Gson();
				ArrayList<Teacher> list = gson.fromJson(data, listType);
				SourceDateList = filledData(list);
				// ����a-z��������Դ����
				Collections.sort(SourceDateList, pinyinComparator);
				adapter = new SortAdapter(mContext, SourceDateList);
				sortListView.setAdapter(adapter);
			}
		}, 1,(Activity)mContext);

		// ʵ��������תƴ����
		characterParser = CharacterParser.getInstance();

		pinyinComparator = new PinyinComparator();

		sideBar = (SideBar) viewBreakQuanzi.findViewById(R.id.sidrbar);
		dialog = (TextView) viewBreakQuanzi.findViewById(R.id.dialog);
		sideBar.setTextView(dialog);

		// �����Ҳഥ������
		sideBar.setOnTouchingLetterChangedListener(new OnTouchingLetterChangedListener() {

			@Override
			public void onTouchingLetterChanged(String s) {
				// ����ĸ�״γ��ֵ�λ��
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
				// ����Ҫ����adapter.getItem(position)����ȡ��ǰposition����Ӧ�Ķ���
				Toast.makeText(mContext,
						((SortModel) adapter.getItem(position)).getName(),
						Toast.LENGTH_SHORT).show();
				Intent intent = new Intent(mContext,GrouthListActivity.class);
				intent.putExtra("tutorId",SourceDateList.get(position).getId());
				mContext.startActivity(intent);
				((Activity) mContext).finish();
			}
		});

		mClearEditText = (ClearEditText) viewBreakQuanzi.findViewById(R.id.filter_edit);

		// �������������ֵ�ĸı�����������
		mClearEditText.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// ������������ֵΪ�գ�����Ϊԭ�����б�����Ϊ���������б�
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

	private List<SortModel> filledData(ArrayList<Teacher> date) {
		List<SortModel> mSortList = new ArrayList<SortModel>();

		for (int i = 0; i < date.size(); i++) {
			SortModel sortModel = new SortModel();
			sortModel.setName(date.get(i).getTutorName());
			sortModel.setId(date.get(i).getId());
			// ����ת����ƴ��
			String pinyin = characterParser.getSelling(date.get(i).getTutorName());
			String sortString = pinyin.substring(0, 1).toUpperCase();

			// ������ʽ���ж�����ĸ�Ƿ���Ӣ����ĸ
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
	 * ����������е�ֵ���������ݲ�����ListView
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

		// ����a-z��������
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
