package com.cpstudio.zhuojiaren.ui;

import io.rong.app.message.DeAgreedFriendRequestMessage;
import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.RongIMClient.ErrorCode;
import io.rong.imlib.RongIMClient.OperationCallback;
import io.rong.imlib.RongIMClient.SendMessageCallback;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Conversation.ConversationType;

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
import android.widget.Toast;

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
import com.cpstudui.zhuojiaren.lz.ZhuoQuanMainActivity;

/**
 * ���󽻻���Ƭ�ļ��ˣ��������Ȧ�ӵ���
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
	int type = 0;// 0:������뽻����Ƭ���ˣ�����Ӻ��ѣ����������Ȧ�ӵ���

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_peoples);
		mConnHelper = ZhuoConnHelper.getInstance(getApplicationContext());
		Intent intent = getIntent();
		type = intent.getIntExtra("type", 0);
		initTitle();
		if (type == 1)
			title.setText(R.string.label_reqquanmsg); // ����Ҫ��ʾ����
		else
			title.setText(R.string.label_active_reuqest_card); // ����Ҫ��ʾ����
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
				helper.setText(R.id.izul_position, position);// ְλ
				if (type == 0)
					helper.setText(R.id.izul_company, item.getCompany());
				else {
					if (item.getGname() != null)
						helper.setText(R.id.izul_company,
								"�������Ȧ�ӣ�" + item.getGname());
				}
				// CommonUtil.calcTimeToNow(time)
				helper.setText(R.id.tvTime, item.getRegisterTime());
				ImageView iv = helper.getView(R.id.izul_image);
				mLoader.beginLoad(item.getUheader(), iv);
				// ����Ҫ����д
				helper.setButton(R.id.izubtn_collect, null, -1,
						new OnClickListener() {
							@Override
							public void onClick(final View v) {
								// TODO Auto-generated method stub
								accept(item);
								// v.setEnabled(false);
							}
						});
			}
		};
		mListView.setAdapter(mAdapter);
		mPullDownView.setShowFooter(false);
		mPullDownView.noFoot();
		loadData();
	}

	void accept(final UserNewVO item) {
		if (0 == type)// ͬ����Ӻ���
		{
			// ������Ƭ(����Ӻ���)
			final DeAgreedFriendRequestMessage msg = new DeAgreedFriendRequestMessage(
					item.getUserid(), "agree");
			// msg.setUserInfo(userInfo);
			if (RongIM.getInstance() != null) {
				// ����һ����ӳɹ����Զ�����Ϣ��������Ϣ������ui��չʾ
				RongIM.getInstance()
						.getRongIMClient()
						.sendMessage(ConversationType.PRIVATE,
								item.getUserid(), msg, null,
								new SendMessageCallback() {
									@Override
									public void onSuccess(Integer arg0) {
										// TODO Auto-generated
										// method stub
										mConnHelper.makeFriends(mUIHandler,
												MsgTagVO.MSG_FOWARD,
												item.getUserid(), 2);
									}

									@Override
									public void onError(Integer arg0,
											ErrorCode arg1) {
										// TODO Auto-generated
										// method stub
										Toast.makeText(LZUserSameActivity.this,
												"ErrorCode��" + arg1, 1000)
												.show();
									}
								});
			}
		} else // ͬ�����Ȧ��
		{
			if (item.getGroupid() == null || item.getGname() == null)
				return;

			/**
			 * ����Ⱥ�顣
			 * 
			 * @param groupId
			 *            Ⱥ�� Id��
			 * @param groupName
			 *            Ⱥ�����ơ�
			 * @param callback
			 *            ����Ⱥ��״̬�Ļص���
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
											"�����ƣ�����Ȧ�ӳɹ�");
								}

								@Override
								public void onError(ErrorCode errorCode) {
									CommonUtil.displayToast(
											LZUserSameActivity.this,
											"�����ƣ�����Ȧ��ʧ��");
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

			}
				break;
			}

		}
	};

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// if (id != -1) {
		// Intent i = new Intent(LZUserSameActivity.this,
		// MsgDetailActivity.class);
		// i.putExtra("msgid", (String) view.getTag(R.id.tag_id));
		// startActivity(i);
		// }
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
