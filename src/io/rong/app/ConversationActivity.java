package io.rong.app;

import io.rong.imkit.common.RongConst;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Conversation.ConversationType;
import io.rong.imlib.model.Group;

import java.util.Locale;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;

import com.cpstudio.zhuojiaren.R;
import com.cpstudio.zhuojiaren.facade.UserFacade;
import com.cpstudio.zhuojiaren.helper.JsonHandler;
import com.cpstudio.zhuojiaren.helper.ConnHelper;
import com.cpstudio.zhuojiaren.model.MsgTagVO;
import com.cpstudio.zhuojiaren.model.UserNewVO;
import com.cpstudio.zhuojiaren.ui.ZhuoMaiCardActivity;
import com.cpstudio.zhuojiaren.ui.ZhuoQuanMainActivity;
import com.cpstudio.zhuojiaren.widget.PopupWindows;
/**
 * �Ự����
 * @author lz
 *
 */
public class ConversationActivity extends FragmentActivity {
	@InjectView(R.id.buttonManage)
	Button btnManage;
	@InjectView(R.id.userNameShow)
	TextView tvTitle;
	int type = 0;// 0:˽�� 1��Ⱥ��
	private PopupWindows pwh = null;
	String myid;
	String targetId;
	UserNewVO guest = null;
	private ConnHelper mConnHelper = null;
	private Conversation.ConversationType mConversationType;
	// ��Ҫ����һ���ӿ��ж������Ƿ�Ϊ���ѹ�ϵ
	String tofollow = "1";// 0:�Ѿ������עΪ���ѣ�1�������Ǻ��ѹ�ϵ
	Group group = null;
	@SuppressLint("HandlerLeak")
	private Handler mUIHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MsgTagVO.DATA_LOAD: {// �������������Ϣ
				if (JsonHandler.checkResult((String) msg.obj,
						getApplicationContext())) {
					JsonHandler nljh = new JsonHandler((String) msg.obj,
							getApplicationContext());
					guest = nljh.parseNewUser();
					if (guest != null
							&& (guest.getRelation() == UserNewVO.USER_RELATION.RELATION_STRANGER
									.ordinal())) {
						tofollow = "1";
						tvTitle.setText(guest.getName());
					} else
						tofollow = "0";
					UserFacade facade = new UserFacade(ConversationActivity.this
							.getApplicationContext());
					facade.saveOrUpdate(guest);
				}
				break;
			}
			case MsgTagVO.ADD_BACK: {// ���������
			}
			case MsgTagVO.MSG_FOWARD: {// ������Ƭ���Ӻ���
				break;
			}
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.conversation);
		ButterKnife.inject(this);
		// getIntent().getData()
		mConnHelper = ConnHelper.getInstance(getApplicationContext());
		pwh = new PopupWindows(ConversationActivity.this);

		Intent intent = getIntent();


		// push��֪ͨ����
		if (intent != null && intent.getData() != null
				&& intent.getData().getScheme().equals("rong")) {
			// ͨ��intent.getData().getQueryParameter("push") Ϊtrue���ж��Ƿ���push��Ϣ
			if (intent.getData().getQueryParameter("push") == null
					|| intent.getData().getQueryParameter("push")
							.equals("true")) {
				String title = intent.getData().getQueryParameter("title");

				if (title != null)
					tvTitle.setText(title);
				enterFragment(intent);
			}
			else
			{
				//push��Ϣ�Ĵ���
			}
		}

		initClick();
	}

	/**
	 * ��Ϣ�ַ���ѡ����ת���ĸ�fragment
	 * 
	 * @param intent
	 */
	private void enterFragment(Intent intent) {
		if (intent != null) {
			// Fragment fragment = null;
			if (intent.getExtras() != null
					&& intent.getExtras().containsKey(RongConst.EXTRA.CONTENT)) {
			} else if (intent.getData() != null) {

				targetId = intent.getData().getQueryParameter("targetId");
				mConversationType = Conversation.ConversationType
						.valueOf(intent.getData().getLastPathSegment()
								.toUpperCase(Locale.getDefault()));
				if (mConversationType == ConversationType.PRIVATE)
					mConnHelper.getUserInfo(mUIHandler, MsgTagVO.DATA_LOAD,
							targetId);
			}
		}
	}


	private void initClick() {
		// TODO Auto-generated method stub
		findViewById(R.id.buttonBack).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				ConversationActivity.this.finish();
			}
		});
		btnManage.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				View view = findViewById(R.id.root_layout);
				OnClickListener onClickListenerViewCard = new OnClickListener() {

					@Override
					public void onClick(View paramView) {
						Intent i = new Intent(ConversationActivity.this,
								ZhuoMaiCardActivity.class);
						i.putExtra("userid", targetId);
						startActivity(i);
					}
				};
				OnClickListener onClickListenerViewQuan = new OnClickListener() {

					@Override
					public void onClick(View paramView) {
						Intent i = new Intent(ConversationActivity.this,
								ZhuoQuanMainActivity.class);
						i.putExtra("groupid", targetId);
						startActivity(i);
					}
				};
				if (mConversationType == ConversationType.PRIVATE) {
					pwh.showBottomPop(view,
							new OnClickListener[] { onClickListenerViewCard },
							new int[] { R.string.label_viewcard }, 7, "manage");
				} else
					pwh.showBottomPop(
							view,
							new OnClickListener[] { onClickListenerViewQuan },
							new int[] { R.string.title_activity_zhuojiaquan_detail },
							7, "manage");
			}
		});
	}
}
