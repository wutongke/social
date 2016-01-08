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
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import butterknife.ButterKnife;
import butterknife.InjectView;

import com.cpstudio.zhuojiaren.R;
import com.cpstudio.zhuojiaren.adapter.CommentAdapter;
import com.cpstudio.zhuojiaren.adapter.CommentAdapter.ReplyInterface;
import com.cpstudio.zhuojiaren.helper.ConnHelper;
import com.cpstudio.zhuojiaren.helper.JsonHandler;
import com.cpstudio.zhuojiaren.model.CommentVO;
import com.cpstudio.zhuojiaren.model.MsgTagVO;
import com.cpstudio.zhuojiaren.model.ResultVO;
import com.cpstudio.zhuojiaren.util.CommonUtil;
import com.cpstudio.zhuojiaren.widget.OverScrollableScrollView.OverScrollController;

public class CommentFragment extends Fragment implements OverScrollController {

	@InjectView(R.id.fc_comment_listview)
	ListView mListView;
	@InjectView(R.id.fc_comment_edit)
	EditText commentEdit;
	@InjectView(R.id.fc_comment_pub)
	Button pubComment;
	View view;
	CommentAdapter mAdapter;
	ArrayList<CommentVO> mDataList = new ArrayList<CommentVO>();
	private Context mContext;
	private boolean mCanScrollUp = false;
	String replyId = "-1";
	String toUserId = "-1";
	String crowdFundingId = "1";
	ConnHelper appClient;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub

		view = inflater.inflate(R.layout.fragment_comment, null);
		appClient = ConnHelper.getInstance(getActivity());
		crowdFundingId = getArguments().getString("id");
		ButterKnife.inject(this, view);
		mContext = getActivity();
		mAdapter = new CommentAdapter(mContext, mDataList,
				R.layout.item_comment);
		mAdapter.setReply(new ReplyInterface() {

			@Override
			public void reply(String id, String userid,String name) {
				// TODO Auto-generated method stub
				replyId = id;
				toUserId = userid;
				commentEdit.setHint("»Ø¸´" + name);
				commentEdit.setFocusable(true);
				commentEdit.setFocusableInTouchMode(true);
				commentEdit.requestFocus();
				InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);  
				imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);  
			}
		});
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
				replyId = "-1";
				toUserId = "-1";
				commentEdit.setHint("");
			}
		});
		init();

		return view;
	}

	private void init() {
		// TODO Auto-generated method stub
		pubComment.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (commentEdit.getText().toString().isEmpty()) {
					CommonUtil.displayToast(getActivity(),
							R.string.please_finish);
					return;
				} else {
					appClient.pubComment(getActivity(), handler,
							MsgTagVO.PUB_INFO, crowdFundingId, commentEdit
									.getText().toString(), replyId,toUserId);
				}
			}
		});
		loadData();
	}

	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case MsgTagVO.PUB_INFO:
				if (JsonHandler.checkResult((String) msg.obj, getActivity())) {
					CommonUtil.displayToast(getActivity(), R.string.label_success);
					commentEdit.setText("");
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
					mDataList.addAll(JsonHandler.parseCommentList(res
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

	private void loadData() {
		// TODO Auto-generated method stub
		appClient.getCrowdFundingComment(getActivity(), handler, MsgTagVO.DATA_LOAD, crowdFundingId);
	}

	@Override
	public boolean canScrollUp() {
		// TODO Auto-generated method stub
		return mCanScrollUp;
	}

}
