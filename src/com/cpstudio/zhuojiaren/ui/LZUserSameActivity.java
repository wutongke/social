package com.cpstudio.zhuojiaren.ui;

import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient.ErrorCode;
import io.rong.imlib.RongIMClient.OperationCallback;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;

import com.cpstudio.zhuojiaren.BaseActivity;
import com.cpstudio.zhuojiaren.R;
import com.cpstudio.zhuojiaren.helper.JsonHandler;
import com.cpstudio.zhuojiaren.helper.ResHelper;
import com.cpstudio.zhuojiaren.helper.ZhuoConnHelper;
import com.cpstudio.zhuojiaren.imageloader.LoadImage;
import com.cpstudio.zhuojiaren.model.BaseCodeData;
import com.cpstudio.zhuojiaren.model.MsgTagVO;
import com.cpstudio.zhuojiaren.model.QuanVO;
import com.cpstudio.zhuojiaren.model.UserNewVO;
import com.cpstudio.zhuojiaren.util.CommonAdapter;
import com.cpstudio.zhuojiaren.util.CommonUtil;
import com.cpstudio.zhuojiaren.util.ViewHolder;
import com.cpstudio.zhuojiaren.widget.PullDownView;
import com.cpstudui.zhuojiaren.lz.ZhuoMaiCardActivity;

/**
 * 请求交换名片的家人，请求加入圈子的人
 * 
 * @author lz
 * 
 */
public class LZUserSameActivity extends BaseActivity implements
		OnItemClickListener {
	private ListView mListView;
	private CommonAdapter mAdapter;
	private PullDownView mPullDownView;
	private ArrayList<UserNewVO> mList = new ArrayList<UserNewVO>();
	private String uid = null;
	private ZhuoConnHelper mConnHelper = null;
	BaseCodeData baseDataSet;
	private LoadImage mLoader = LoadImage.getInstance();
	// add by lz
	boolean isManaging = false;
	int type = 0;// 0:请求加入交换名片的人（请求加好友）、请求加入圈子的人

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_peoples);
		mConnHelper = ZhuoConnHelper.getInstance(getApplicationContext());
		Intent intent = getIntent();
		type = intent.getIntExtra("type", 0);
		initTitle();
		function.setText(getString(R.string.clear));
		function.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (mList != null)
					mList.clear();
				if (mAdapter != null)
					mAdapter.notifyDataSetChanged();
			}
		});
		if (type == 1)
			title.setText(R.string.label_reqquanmsg); // 还需要显示人数
		else
			title.setText(R.string.label_active_reuqest_card); // 还需要显示人数
		baseDataSet = mConnHelper.getBaseDataSet();
		uid = ResHelper.getInstance(getApplicationContext()).getUserid();
		mPullDownView = (PullDownView) findViewById(R.id.pull_down_view);
		mPullDownView.initHeaderViewAndFooterViewAndListView(this,
				R.layout.listview_header);
		mListView = mPullDownView.getListView();
		mAdapter = new CommonAdapter<UserNewVO>(LZUserSameActivity.this, mList,
				R.layout.item_carduser_list) {
			@Override
			public void convert(ViewHolder helper, final UserNewVO item) {
				// TODO Auto-generated method stub
				helper.setCheckBox(R.id.isChecked, false, View.GONE);
				helper.setText(R.id.izul_name, item.getName());
				String position = "";
				if (baseDataSet != null) {
					int pos = item.getPosition();
					if (pos != 0)
						pos--;
					position = ((baseDataSet.getPosition()).get(pos))
							.getContent();
				}
				helper.setText(R.id.izul_position, position);// 职位
				if (type == 0)
					helper.setText(R.id.izul_company, item.getCompany());
				else {
					if (item.getGname() != null)
						helper.setText(R.id.izul_company,
								"请求加入圈子：" + item.getGname());
				}
				// CommonUtil.calcTimeToNow(time)
				helper.setText(R.id.tvTime, item.getRegisterTime());
				ImageView iv = helper.getView(R.id.izul_image);
				iv.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						Intent i = new Intent(LZUserSameActivity.this,
								ZhuoMaiCardActivity.class);
						i.putExtra("userid", item.getUserid());
						startActivity(i);
					}
				});

				mLoader.beginLoad(item.getUheader(), iv);
				// 还需要继续写
				helper.setButton(R.id.izubtn_collect, null, -1,
						new OnClickListener() {
							@Override
							public void onClick(final View v) {
								// TODO Auto-generated method stub
								accept(item, v);
								// v.setEnabled(false);
							}
						});
			}
		};
		mListView.setAdapter(mAdapter);
		mPullDownView.setShowFooter(false);
		mPullDownView.noFoot();
		if (type == 1)
			mPullDownView.setHideHeader();
		loadData();
	}

	void accept(final UserNewVO item, final View v) {
		if (0 == type)// 同意添加好友
		{
			// // 递送名片(即添加好友)
			// final DeAgreedFriendRequestMessage msg = new
			// DeAgreedFriendRequestMessage(
			// item.getUserid(), "agree");
			// UserInfo userInfo =new UserInfo(item.getUserid(), item.getName(),
			// Uri.parse(item.getUheader()));
			// //把用户信息设置到消息体中，直接发送给对方，可以不设置，非必选项
			// msg.setUserInfo(userInfo);
			// // msg.setUserInfo(userInfo);
			// if (RongIM.getInstance() != null) {
			// // 发送一条添加成功的自定义消息，此条消息不会在ui上展示
			// RongIM.getInstance()
			// .getRongIMClient()
			// .sendMessage(ConversationType.PRIVATE,
			// item.getUserid(), msg, null,
			// new SendMessageCallback() {
			// @Override
			// public void onSuccess(Integer arg0) {
			// // TODO Auto-generated
			// // method stub
			// mConnHelper.makeFriends(mUIHandler,
			// MsgTagVO.MSG_FOWARD,
			// item.getUserid(), 2);
			// }
			//
			// @Override
			// public void onError(Integer arg0,
			// ErrorCode arg1) {
			// // TODO Auto-generated
			// // method stub
			// Toast.makeText(LZUserSameActivity.this,
			// "ErrorCode：" + arg1, 1000)
			// .show();
			// }
			// });
			// }
			if (v != null)
				v.setVisibility(View.GONE);
			// 权宜之计，应该用上面的
			mConnHelper.makeFriends(mUIHandler, MsgTagVO.MSG_FOWARD,
					item.getUserid(), 2);

		} else // 同意加入圈子
		{
			if (item.getGroupid() == null || item.getGname() == null)
				return;

			/**
			 * 加入群组。
			 * 
			 * @param groupId
			 *            群组 Id。
			 * @param groupName
			 *            群组名称。
			 * @param callback
			 *            加入群组状态的回调。
			 */
			RongIM.getInstance()
					.getRongIMClient()
					.joinGroup(item.getGroupid(), item.getGname(),
							new OperationCallback() {
								@Override
								public void onSuccess() {
									mConnHelper.followGroup(mUIHandler,
											MsgTagVO.FOLLOW_QUAN,
											item.getGroupid(),
											QuanVO.QUAN_PERMIT,
											item.getUserid(), "agree");
									CommonUtil.displayToast(
											LZUserSameActivity.this,
											"向融云，加入圈子成功");
									if (v != null)
										v.setVisibility(View.GONE);
								}

								@Override
								public void onError(ErrorCode errorCode) {
									CommonUtil.displayToast(
											LZUserSameActivity.this,
											"向融云，加入圈子失败");
								}
							});
		}
	}

	@SuppressLint("HandlerLeak")
	private Handler mUIHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MsgTagVO.DATA_LOAD: {
				if (JsonHandler.checkResult((String) msg.obj,
						getApplicationContext())) {
					JsonHandler nljh = new JsonHandler((String) msg.obj,
							getApplicationContext());
					mList.clear();
					mList.addAll(nljh.parseUserNewList());
					mAdapter.notifyDataSetChanged();
					if (mList.size() < 1)
						mPullDownView.noData(false);
				}
				break;
			}
			case MsgTagVO.FOLLOW_QUAN: {
				if (JsonHandler.checkResult((String) msg.obj,
						getApplicationContext())) {
					CommonUtil.displayToast(getApplicationContext(),
							R.string.join_quan_success);
				}
				break;
			}
			case MsgTagVO.MSG_FOWARD: {
				if (JsonHandler.checkResult((String) msg.obj,
						getApplicationContext())) {
					CommonUtil.displayToast(getApplicationContext(),
							R.string.admit_user_success);
				}
			}
				break;
			}

		}
	};

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {

	}

	private void loadData() {
		if (mPullDownView.startLoadData()) {
			if (0 == type)
				mConnHelper.getFollowReqList(mUIHandler, MsgTagVO.DATA_LOAD);
			else
				mConnHelper.getReqQuanUsers(mUIHandler, MsgTagVO.DATA_LOAD,
						null);
		}
	}
}
