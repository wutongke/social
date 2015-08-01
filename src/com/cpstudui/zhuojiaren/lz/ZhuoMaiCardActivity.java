package com.cpstudui.zhuojiaren.lz;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;

import com.cpstudio.zhuojiaren.CardEditActivity;
import com.cpstudio.zhuojiaren.R;
import com.cpstudio.zhuojiaren.facade.CardMsgFacade;
import com.cpstudio.zhuojiaren.facade.UserFacade;
import com.cpstudio.zhuojiaren.fragment.ActivePagerAdapter;
import com.cpstudio.zhuojiaren.fragment.ZhuomaiActiveInfoFra;
import com.cpstudio.zhuojiaren.fragment.ZhuomaiCardCommercyInfoFra;
import com.cpstudio.zhuojiaren.fragment.ZhuomaiMoreInfoFra;
import com.cpstudio.zhuojiaren.helper.ResHelper;
import com.cpstudio.zhuojiaren.helper.ZhuoConnHelper;
import com.cpstudio.zhuojiaren.imageloader.LoadImage;
import com.cpstudio.zhuojiaren.model.QuanVO;
import com.cpstudio.zhuojiaren.widget.PopupWindows;
import com.cpstudio.zhuojiaren.widget.TabButton;
import com.cpstudio.zhuojiaren.widget.TabButton.PageChangeListener;
import com.cpstudio.zhuojiaren.widget.TabButton.TabsButtonOnClickListener;

/**
 * 倬脉名片
 * 
 * @author lz
 * 
 */
public class ZhuoMaiCardActivity extends FragmentActivity {
	@InjectView(R.id.activity_back)
	TextView tvBack;
	@InjectView(R.id.activity_title)
	TextView tvTitle;
	@InjectView(R.id.activity_function)
	ImageView ivShare;
	@InjectView(R.id.activity_function2)
	ImageView ivZan;

	@InjectView(R.id.azq_tab)
	TabButton tabButton;
	@InjectView(R.id.azq_viewpager)
	ViewPager viewPager;
	@InjectView(R.id.imageViewHeader)
	ImageView ivHeader;
	@InjectView(R.id.textViewName)
	TextView tvName;

	@InjectView(R.id.lt_menue)
	View ltMenue;// 个人资料编辑菜单

	@InjectView(R.id.btnEditBG)
	View btnEditBG;// 个性化背景
	@InjectView(R.id.btnEditCard)
	View btnEditCard;//
	@InjectView(R.id.rootmain)
	View rootMainBG;//

	private final static int USER_SELECT = 0;
	private Context mContext;
	// 四个fragment 方便通信
	List<Fragment> fragments;

	private PopupWindows phw = null;
	private LoadImage mLoadImage = new LoadImage();
	// 不同身份，功能不同
	private String memberType = "";
	private PopupWindows pwh = null;
	private String groupid = null;
	private ZhuoConnHelper mConnHelper = null;
	private boolean isfollow = false;// 是否已经加入该圈子

	private ArrayList<String> tempids = new ArrayList<String>();

	String userid, myid, ismy;
	private UserFacade userFacade = null;
	private CardMsgFacade mFacade = null;

	// 用于在fragment中获得groupid
	public String getGroupid() {
		return groupid;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_zhuo_card_main);
		ButterKnife.inject(this);
		mContext = this;

		tvTitle.setText(R.string.title_zhuomai_card);

		// 初始化tab和viewpager
		viewPager.setAdapter(getPagerAdapter());

		tabButton.setViewPager(viewPager);

		userFacade = new UserFacade(ZhuoMaiCardActivity.this);
		mFacade = new CardMsgFacade(ZhuoMaiCardActivity.this);
		mConnHelper = ZhuoConnHelper.getInstance(getApplicationContext());
		pwh = new PopupWindows(ZhuoMaiCardActivity.this);
		Intent i = getIntent();
		userid = i.getStringExtra("userid");
		myid = ResHelper.getInstance(getApplicationContext()).getUserid();
		if (userid.equals(myid)) {
			ismy = "1";
			ltMenue.setVisibility(View.VISIBLE);
		} else
			ltMenue.setVisibility(View.GONE);
		mLoadImage = new LoadImage();

		// 设置个性背景图片，在个人信息里。个人可以选择更换
		rootMainBG.setBackgroundResource(R.drawable.manbg_zmmp_1);

		initOnClick();
		// loadInfo();
		// initClick();

	}

	private void initOnClick() {
		// TODO Auto-generated method stub
		// 选择不同的fragment，function按键不同
		tvTitle.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				ZhuoMaiCardActivity.this.finish();
			}
		});
		tabButton.setPageChangeListener(new PageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {
				// TODO Auto-generated method stub

				if (arg0 == 2) {
					// 不展示第三个page
					viewPager.setCurrentItem(1);
					mContext.startActivity(new Intent(mContext,
							CardEditActivity.class));
					// 跳转到供需发布页面
				}
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub

			}
		});

		tabButton.setTabsButtonOnClickListener(new TabsButtonOnClickListener() {

			@Override
			public void tabsButtonOnClick(int id, View v) {
				// TODO Auto-generated method stub
				if ((Integer) (v.getTag()) == 2)
					mContext.startActivity(new Intent(mContext,
							CardEditActivity.class));
				else {
					viewPager.setCurrentItem((Integer) v.getTag());
				}
			}
		});
		ivZan.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				// 赞名片

				// 这是赞评论
				// mConnHelper.goodMsg(msgid, mUIHandler,
				// MsgTagVO.MSG_LIKE, null, true, null, null);
			}
		});
		ivShare.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// 分享，具体呢？
				// if (phw == null)
				// phw = new PopupWindows(ZhuoMaiCardActivity.this);
				//
				// OnClickListener briefListener = new OnClickListener() {
				//
				// @Override
				// public void onClick(View v) {
				// Intent i = new Intent(ZhuoMaiCardActivity.this,
				// QuanBriefActivity.class);
				// i.putExtra("groupid", groupid);
				// startActivity(i);
				// }
				// };
				//
				// OnClickListener shareListener = new OnClickListener() {
				//
				// @Override
				// public void onClick(View v) {
				// // 通过第三方软件分享，QQ，微信等
				// }
				// };
				//
				// OnClickListener inviteListener = new OnClickListener() {
				//
				// @Override
				// public void onClick(View v) {
				// Intent i = new Intent(ZhuoMaiCardActivity.this,
				// UserSelectActivity.class);
				// i.putStringArrayListExtra("otherids", tempids);
				// startActivityForResult(i, USER_SELECT);
				// }
				// };
				// // 需要另外设置菜单选项布局及响应事件
				// phw.showQuanOptionsMenue(v, 2, briefListener, shareListener,
				// inviteListener);
			}
		});
		btnEditBG.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(ZhuoMaiCardActivity.this,
						ChangeBackgroundActivity.class);
				startActivity(i);
			}
		});
		btnEditCard.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(ZhuoMaiCardActivity.this,
						CardEditActivity.class);
				startActivity(i);
			}
		});

	}

	protected void onPause() {
		super.onPause();
		viewPager.setCurrentItem(0, false);
		tabButton.setTabBackgroundByIndex(0);
	};

	PagerAdapter getPagerAdapter() {
		fragments = new ArrayList<Fragment>();
		List<CharSequence> titles = new ArrayList<CharSequence>();
		String commercyInfo = getString(R.string.label_commercy_info);
		String activeZM = getString(R.string.label_active_zhuomai);
		String moreInfo = getString(R.string.label_more_info);

		Fragment quanTopic = addBundle(new ZhuomaiCardCommercyInfoFra(),
				QuanVO.QUANZITOPIC);
		fragments.add(quanTopic);
		titles.add(commercyInfo);

		Fragment quanEvent = addBundle(new ZhuomaiActiveInfoFra(),
				QuanVO.QUANZIEVENT);
		fragments.add(quanEvent);
		titles.add(activeZM);

		Fragment quanMember = addBundle(new ZhuomaiMoreInfoFra(),
				QuanVO.QUANZIMEMBER);
		fragments.add(quanMember);
		titles.add(moreInfo);
		return new ActivePagerAdapter(getSupportFragmentManager(), fragments,
				titles);
	}

	protected Fragment addBundle(Fragment fragment, int catlog) {
		Bundle bundle = new Bundle();
		bundle.putInt(QuanVO.QUANZIMAINTYPE, catlog);
		fragment.setArguments(bundle);
		return fragment;
	}

	private void setFunctionText(int arg0) {
		switch (arg0) {
		// case 0:
		// function.setText("管理");
		// function.setTag(0);
		// break;
		// case 1:
		// function.setTag(1);
		// ImageSpan span = new ImageSpan(mContext, R.drawable.tab_good);
		// SpannableString spanStr = new SpannableString(" ");
		// spanStr.setSpan(span, 0, 1, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
		// function.setText(spanStr);
		// break;
		// case 2:
		// function.setText("筛选");
		// function.setTag(2);
		// break;
		// case 3:
		// function.setText("");
		// function.setTag(3);
		// break;
		}
	}

}
