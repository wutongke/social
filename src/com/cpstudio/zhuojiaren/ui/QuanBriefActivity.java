package com.cpstudio.zhuojiaren.ui;

import io.rong.imlib.model.Group;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;

import com.cpstudio.zhuojiaren.R;
import com.cpstudio.zhuojiaren.facade.QuanFacade;
import com.cpstudio.zhuojiaren.helper.ConnHelper;
import com.cpstudio.zhuojiaren.helper.JsonHandler;
import com.cpstudio.zhuojiaren.imageloader.LoadImage;
import com.cpstudio.zhuojiaren.model.City;
import com.cpstudio.zhuojiaren.model.MsgTagVO;
import com.cpstudio.zhuojiaren.model.Province;
import com.cpstudio.zhuojiaren.model.QuanVO;
import com.cpstudio.zhuojiaren.model.ResultVO;
import com.cpstudio.zhuojiaren.util.CommonUtil;
import com.cpstudio.zhuojiaren.widget.PopupWindows;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * 圈子简介界面
 * @author lz
 *
 */
public class QuanBriefActivity extends BaseActivity {
	@InjectView(R.id.imageViewGroupHeader)
	ImageView ivGroupHeader;
	@InjectView(R.id.textViewName)
	TextView tvName;
	@InjectView(R.id.mtextViewCy)
	TextView tvMemNum;
	@InjectView(R.id.mtextViewTopic)
	TextView tvTopicNum;
	@InjectView(R.id.mtextViewGType)
	TextView tvxxx;
	@InjectView(R.id.imageViewCj)
	ImageView imageViewCj;

	@InjectView(R.id.tvQunzhuName)
	TextView tvQunzhuName;
	@InjectView(R.id.tvGonggao)
	TextView tvGonggao;

	@InjectView(R.id.tvClass)
	TextView tvClass;
	@InjectView(R.id.tvLocal)
	TextView tvLocal;
	@InjectView(R.id.tvBrief)
	TextView tvBrief;

	@InjectView(R.id.btnJoinQuan)
	Button btnJoinQuan;// 非成员操作菜单
	@InjectView(R.id.btnQuitQuan)
	Button btnQuitQuan;// 非成员操作菜单

	@InjectView(R.id.lt_chengyuan_menue)
	View ltMember;// 成员操作菜单
	@InjectView(R.id.lt_youke_menue)
	View ltYouke;// 非成员操作菜单
	@InjectView(R.id.activity_function_image)
	ImageView ivFun;
	int localCode = -1;
	private LoadImage mLoadImage = LoadImage.getInstance();

	// 不同身份，功能不同
	private int memberType = 0;
	private PopupWindows pwh = null;
	private String groupid = null, groupName = null, gheadurl = null;
	private String owerName = null, owerId = null;
	private ConnHelper mConnHelper = null;
	private boolean isfollow = false;// 是否已经加入该圈子
	private QuanFacade mFacade = null;
	List<City> citys;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_quan_brief);
		ButterKnife.inject(this);
		initTitle();
		// 是管理员的时候才出现此菜单
		ivFun.setVisibility(View.GONE);
		ivFun.setImageResource(R.drawable.write);

		title.setText(R.string.label_quan_brief);

		mConnHelper = ConnHelper.getInstance(getApplicationContext());

		mFacade = new QuanFacade(getApplicationContext());
		Intent i = getIntent();
		groupid = i.getStringExtra("groupid");
		pwh = new PopupWindows(QuanBriefActivity.this);

		// 在简介里与详情里公用基本信息但是不需要循环展示公告
		findViewById(R.id.linearLayoutBroadcast).setVisibility(View.GONE);

		loadData();
		initOnClick();
	}

	private void loadData() {
		if (CommonUtil.getNetworkState(getApplicationContext()) == 2) {
			QuanVO quan = mFacade.getById(groupid);
			if (quan == null) {
				CommonUtil.displayToast(getApplicationContext(),
						R.string.error0);
			} else {
				Message msg = mUIHandler.obtainMessage(MsgTagVO.DATA_LOAD);
				msg.obj = quan;
				msg.sendToTarget();
			}
		} else {
			mConnHelper.getQuanInfo(mUIHandler, MsgTagVO.DATA_LOAD, groupid);
		}
	}

	@SuppressLint("HandlerLeak")
	private Handler mUIHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MsgTagVO.DATA_LOAD:
				if (msg.obj != null && !msg.obj.equals("")) {
					QuanVO detail = null;
					if (msg.obj instanceof QuanVO) {
						detail = (QuanVO) msg.obj;
					} else {
						JsonHandler nljh = new JsonHandler((String) msg.obj,
								getApplicationContext());
						detail = nljh.parseQuan();
						if (null != detail) {
							// 是否需要保存到本地
						}
					}
					if (null != detail) {
						memberType = detail.getRole();
						groupName = detail.getGname();
						tvName.setText(groupName);
						gheadurl = detail.getGheader();
						ivGroupHeader.setTag(gheadurl);
						owerName = detail.getName();
						owerId = detail.getUserid();
						tvQunzhuName.setText(owerName);
						tvGonggao.setText(detail.getGpub());
						tvBrief.setText(detail.getGintro());
						// 圈子类型
						String[] quanziType = getResources().getStringArray(
								R.array.quanzi_type);
						int type = detail.getGtype();
						tvClass.setText(quanziType[type]);
						tvMemNum.setText(detail.getMemberCount() + "");
						tvTopicNum.setText(detail.getTopicCount() + "");
						localCode = detail.getCity();
						citys = mConnHelper.getCitys();
						if (citys != null && localCode >= 1) {
							tvLocal.setText(citys.get(localCode - 1)
									.getCityName());
						}
						if (memberType != QuanVO.QUAN_ROLE_YOUKE) {
							isfollow = true;
						} else {
							isfollow = false;
						}
						if (memberType >= QuanVO.QUAN_ROLE_MANAGER) {
							ivFun.setVisibility(View.VISIBLE);
							ivFun.setOnClickListener(new OnClickListener() {
								@Override
								public void onClick(View arg0) {
									// TODO Auto-generated method stub
									Intent intent = new Intent(
											QuanBriefActivity.this,
											QuanCreateActivity.class);
									intent.putExtra("groupid", groupid);
									startActivityForResult(intent, 1);
								}
							});
						}
						changeType(isfollow);
						mLoadImage
								.beginLoad(detail.getGheader(), ivGroupHeader);
						mLoadImage.beginLoad(detail.getUheader(), imageViewCj);
					}
				}
				break;
			case MsgTagVO.DATA_OTHER:
				ResultVO res;
				if (JsonHandler.checkResult((String) msg.obj,
						QuanBriefActivity.this)) {
					res = JsonHandler.parseResult((String) msg.obj);
					// 每次都写文件，没必要，如果是从网络获取则缓存，否则不用再缓存了
					ConnHelper.getInstance(getApplicationContext())
							.saveObject((String) msg.obj, "citys");
				} else {
					return;
				}
				String data = res.getData();
				Type listType = new TypeToken<ArrayList<Province>>() {
				}.getType();
				Gson gson = new Gson();
				ArrayList<Province> list = gson.fromJson(data, listType);
				ArrayList<City> cityList = new ArrayList<City>();
				for (Province temp : list) {
					cityList.addAll(temp.getCitys());
				}
				if (cityList != null && localCode != -1) {
					tvLocal.setText(cityList.get(localCode).getCityName());
				}
				break;
			case MsgTagVO.FOLLOW_QUAN: {
				if (JsonHandler.checkResult((String) msg.obj,
						getApplicationContext())) {
					int type = 0;
					if (isfollow) {
						pwh.showPopTip(findViewById(R.id.scrollViewGroupInfo),
								null, R.string.label_exitSuccess);
						type = 0;
					} else {
						pwh.showPopTip(findViewById(R.id.scrollViewGroupInfo),
								null, R.string.label_applysuccess);

						type = 1;
					}
					mConnHelper.followQuan(
							QuanBriefActivity.this,
							owerId,
							new Group(groupid, groupName, android.net.Uri
									.parse(gheadurl)), type);
					isfollow = !isfollow;
					loadData();
				}
				break;
			}
			}
		}
	};

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			loadData();
		}
	}

	private void initOnClick() {
		// TODO Auto-generated method stub
		btnJoinQuan.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mConnHelper.followGroup(mUIHandler, MsgTagVO.FOLLOW_QUAN,
						groupid, QuanVO.QUAN_JOIN, null, "");
			}
		});

		btnQuitQuan.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				pwh.showPopDlg(v, new OnClickListener() {
					@Override
					public void onClick(View v) {
						mConnHelper.followGroup(mUIHandler,
								MsgTagVO.FOLLOW_QUAN, groupid,
								QuanVO.QUAN_QUIT, null, "");
					}
				}, null, R.string.info13);
			}
		});
	}

	private void changeType(boolean isMember) {

		if (isMember) {
			ltMember.setVisibility(View.VISIBLE);
			ltYouke.setVisibility(View.GONE);
		} else {
			ltMember.setVisibility(View.GONE);
			ltYouke.setVisibility(View.VISIBLE);
		}
	}
}
