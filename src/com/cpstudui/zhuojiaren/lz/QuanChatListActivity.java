package com.cpstudui.zhuojiaren.lz;

import io.rong.imkit.fragment.ConversationListFragment;
import io.rong.imlib.model.Conversation;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;

import com.cpstudio.zhuojiaren.R;

public class QuanChatListActivity extends FragmentActivity {
	@InjectView(R.id.activity_back)
	TextView tvBack;
	@InjectView(R.id.activity_title)
	TextView tvTitle;
	
	ConversationListFragment listFragment;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_group_chat_list_active);
		ButterKnife.inject(this);
		tvTitle.setText(getString(R.string.label_groupchat));
		tvBack.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				QuanChatListActivity.this.finish();
			}
			
		});
		loadMessageSession();
	}

	void loadMessageSession() {
		ViewGroup root = (ViewGroup) findViewById(R.id.ryConversationListContainer);
		listFragment = ConversationListFragment.getInstance();
		Uri uri = Uri
				.parse("rong://" + getApplicationInfo().packageName)
				.buildUpon()
				.appendPath("conversationlist")
//				.appendQueryParameter(
//						Conversation.ConversationType.PRIVATE.getName(),
//						"false") // ����˽�ĻỰ�Ƿ�ۺ���ʾ
				.appendQueryParameter(
						Conversation.ConversationType.GROUP.getName(), "false")// Ⱥ��
				// .appendQueryParameter(
				// Conversation.ConversationType.DISCUSSION.getName(),
				// "false")// ������
				// .appendQueryParameter(
				// Conversation.ConversationType.APP_PUBLIC_SERVICE
				// .getName(),
				// "false")// Ӧ�ù��ڷ���
				// .appendQueryParameter(
				// Conversation.ConversationType.PUBLIC_SERVICE.getName(),
				// "false")// ���������
//				.appendQueryParameter(
//						Conversation.ConversationType.SYSTEM.getName(), "false")// ϵͳ
				.build();
		listFragment.setUri(uri);
		FragmentManager fragmentManager = getSupportFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager
				.beginTransaction();
		fragmentTransaction.add(R.id.ryConversationListContainer, listFragment);
		fragmentTransaction.commit();
	}

}
