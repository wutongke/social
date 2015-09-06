package com.cpstudio.zhuojiaren.ui;

import io.rong.app.message.DeAgreedFriendRequestMessage;
import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient.ErrorCode;
import io.rong.imlib.RongIMClient.SendMessageCallback;
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
import com.cpstudio.zhuojiaren.model.UserNewVO;
import com.cpstudio.zhuojiaren.util.CommonAdapter;
import com.cpstudio.zhuojiaren.util.ViewHolder;
import com.cpstudio.zhuojiaren.widget.PullDownView;
/**
 * 请求交换名片的家人
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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_peoples);
		mConnHelper = ZhuoConnHelper.getInstance(getApplicationContext());
		Intent intent = getIntent();
		initTitle();
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
				if (baseDataSet != null)
				{
					int pos=item.getPosition();
					if(pos!=0)
						pos--;
					position = ((baseDataSet.getPosition()).get(pos)).getContent();
				}
				helper.setText(R.id.izul_position, position);// 职位
				helper.setText(R.id.izul_company, item.getCompany());
				// CommonUtil.calcTimeToNow(time)
				helper.setText(R.id.tvTime, item.getRegisterTime());
				ImageView iv = helper.getView(R.id.izul_image);
				mLoader.beginLoad(item.getUheader(),iv);
				//还需要继续写
//				helper.setImageResource(R.id.izul_collect,
//						R.drawable.cardex_zx_1, new OnClickListener() {
//							@Override
//							public void onClick(final View v) {
//								// TODO Auto-generated method stub
//								accept(item);
//								v.setEnabled(false);
//							}
//						});
			}
		};
		mListView.setAdapter(mAdapter);
		mPullDownView.setShowFooter(false);
		mPullDownView.noFoot();
		
		loadData();
	}

	void accept(final UserNewVO item) {
		// 递送名片(即添加好友)
		DeAgreedFriendRequestMessage msg = new DeAgreedFriendRequestMessage(
				uid, "同意");
		RongIM.getInstance()
				.getRongIMClient()
				.sendMessage(ConversationType.PRIVATE, item.getUserid(), msg,
						"同意", new SendMessageCallback() {

							@Override
							public void onSuccess(Integer arg0) {
								// TODO Auto-generated
								// method stub
								mConnHelper.followUser(mUIHandler,
										MsgTagVO.MSG_FOWARD, item.getUserid(),
										2);
							}
							@Override
							public void onError(Integer arg0, ErrorCode arg1) {
								// TODO Auto-generated
								// method stub
								Toast.makeText(LZUserSameActivity.this,
										"ErrorCode：" + arg1, 1000).show();
							}
						});
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
					mList.addAll( nljh.parseUserNewList());
					mAdapter.notifyDataSetChanged();
					 if(mList.size()<1)
						 mPullDownView.noData(false);
				}
				break;
			}

			}

		}
	};

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
//		if (id != -1) {
//			Intent i = new Intent(LZUserSameActivity.this,
//					MsgDetailActivity.class);
//			i.putExtra("msgid", (String) view.getTag(R.id.tag_id));
//			startActivity(i);
//		}
	}

	private void loadData() {
		if (mPullDownView.startLoadData()) {
			mConnHelper.getFollowReqList(mUIHandler, MsgTagVO.DATA_LOAD);
		}
	}
}
