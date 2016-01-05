package com.cpstudio.zhuojiaren.fragment;

import java.util.ArrayList;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import butterknife.ButterKnife;
import butterknife.InjectView;

import com.cpstudio.zhuojiaren.R;
import com.cpstudio.zhuojiaren.adapter.ProgressAdapter;
import com.cpstudio.zhuojiaren.helper.AppClient;
import com.cpstudio.zhuojiaren.helper.JsonHandler;
import com.cpstudio.zhuojiaren.helper.JsonHandler_Lef;
import com.cpstudio.zhuojiaren.model.CrowdFundingVO;
import com.cpstudio.zhuojiaren.model.MsgTagVO;
import com.cpstudio.zhuojiaren.model.ProgressVO;
import com.cpstudio.zhuojiaren.model.ResultVO;
import com.cpstudio.zhuojiaren.model.UserVO;
import com.cpstudio.zhuojiaren.util.CommonUtil;
import com.cpstudio.zhuojiaren.widget.OverScrollableScrollView.OverScrollController;

public class ProgressFragment extends Fragment implements OverScrollController {

	@InjectView(R.id.payback_listview)
	ListView mListView;
	@InjectView(R.id.fp_comment_edit)
	EditText editProgress;
	@InjectView(R.id.fp_comment_pub)
	Button pubBtn;
	@InjectView(R.id.fp_pub_layout)
	LinearLayout layout;
	View view;
	ProgressAdapter mAdapter;
	ArrayList<ProgressVO> mDataList = new ArrayList<ProgressVO>();
	private Context mContext;
	private boolean mCanScrollUp = false;
	private String isCreater;
	private String id;
	private AppClient appClient;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		view = inflater.inflate(R.layout.fragment_progress, null);
		ButterKnife.inject(this, view);
		id = getArguments().getString("id");
		isCreater = getArguments().getString("isCreater");
		appClient = AppClient.getInstance(getActivity());
		if (!isCreater.equals(CrowdFundingVO.likeOrSupport)) {
			layout.setVisibility(View.VISIBLE);
		}
		mContext = getActivity();
		mAdapter = new ProgressAdapter(mContext, mDataList,
				R.layout.item_progress);
		mListView.setAdapter(mAdapter);
		mListView.setOnScrollListener(new OnScrollListener() {
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {

			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				int childTop = 0;
				if (view.getChildCount() > 0) {
					childTop = view.getChildAt(0).getTop();
				}
				if (firstVisibleItem == 0 && childTop == 0) {
					mCanScrollUp = true;
				} else {
					mCanScrollUp = false;
				}
			}
		});
		init();
		return view;
	}

	private void init() {
		// TODO Auto-generated method stub
		pubBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (editProgress.getText().toString().isEmpty()) {
					CommonUtil.displayToast(getActivity(),
							R.string.please_finish);
				} else {
					appClient.pubProgress(getActivity(), handler,
							MsgTagVO.PUB_INFO, id, editProgress.getText()
									.toString());
				}
			}
		});
		loadData();
	}

	private void loadData() {
		// TODO Auto-generated method stub
		appClient.getProgress(getActivity(), handler, MsgTagVO.DATA_LOAD, id);
	}

	@Override
	public boolean canScrollUp() {
		// TODO Auto-generated method stub
		return mCanScrollUp;
	}

	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case MsgTagVO.PUB_INFO:
				if (JsonHandler.checkResult((String) msg.obj, getActivity())) {
					CommonUtil.displayToast(getActivity(),
							R.string.label_success);
				} else {
					CommonUtil.displayToast(getActivity(), R.string.data_error);
					return;
				}

				break;
			case MsgTagVO.DATA_LOAD:
				ResultVO res;
				if (JsonHandler.checkResult((String) msg.obj, getActivity())) {
					res = JsonHandler.parseResult((String) msg.obj);

					mDataList.clear();
					mDataList.addAll(JsonHandler_Lef.parseProgressVOList(res
							.getData()));
					mAdapter.notifyDataSetChanged();
				} else {
					CommonUtil.displayToast(getActivity(), R.string.data_error);
					return;
				}
			default:
				break;
			}
		};
	};
}
