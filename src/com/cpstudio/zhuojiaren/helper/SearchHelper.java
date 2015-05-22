package com.cpstudio.zhuojiaren.helper;

import android.app.Activity;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.cpstudio.zhuojiaren.R;

public class SearchHelper {
	private String searchKey = "";
	private OnSearchListener onSearch = null;

	public SearchHelper(OnSearchListener onSearch) {
		this.onSearch = onSearch;
	}

	public String getSearchKey() {
		return searchKey;
	}

	public void initSearchEvent(final Activity mActivity) {
		final EditText searchView = (EditText) mActivity
				.findViewById(R.id.editTextSearch);
		final View delSearch = mActivity.findViewById(R.id.imageViewDelSearch);
		searchView.setOnEditorActionListener(new OnEditorActionListener() {

			@Override
			public boolean onEditorAction(TextView v, int actionId,
					KeyEvent event) {
				if (actionId == EditorInfo.IME_ACTION_SEARCH) {
					searchKey = v.getText().toString();
					InputMethodManager imm = (InputMethodManager) mActivity
							.getSystemService(Context.INPUT_METHOD_SERVICE);
					imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
					if (onSearch != null) {
						onSearch.search();
					}
				}
				return false;
			}
		});
		searchView.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {
				if (searchView.getText().toString().equals("")) {
					delSearch.setVisibility(View.GONE);
				} else {
					delSearch.setVisibility(View.VISIBLE);
				}
			}
		});

		delSearch.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				searchView.setText("");
				if (searchKey != null && !searchKey.equals("")) {
					searchKey = "";
					if (onSearch != null) {
						onSearch.search();
					}
				}
			}
		});
	}

	public interface OnSearchListener {
		public void search();
	}
}
