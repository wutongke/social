package com.cpstudio.zhuojiaren.widget;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cpstudio.zhuojiaren.R;

public class ListViewFooter {
	private boolean startLoading = false;
	private View mFooterTextView;
	private TextView mFooterNoData;
	private ProgressBar mFooterLoadingView;

	public ListViewFooter(RelativeLayout mFooterView,
			OnClickListener onClickListener) {
		mFooterTextView = mFooterView.findViewById(R.id.textViewFooterButton);
		mFooterTextView.setOnClickListener(onClickListener);
		mFooterNoData = (TextView) mFooterView
				.findViewById(R.id.textViewNoData);
		mFooterLoadingView = (ProgressBar) mFooterView
				.findViewById(R.id.progressFooterLoading);
	}

	public boolean startLoading() {
		if (!startLoading) {
			startLoading = true;
			mFooterNoData.setVisibility(View.GONE);
			mFooterLoadingView.setVisibility(View.VISIBLE);
			return true;
		}
		return false;
	}

	public void finishLoading() {
		startLoading = false;
		mFooterLoadingView.setVisibility(View.GONE);
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

}
