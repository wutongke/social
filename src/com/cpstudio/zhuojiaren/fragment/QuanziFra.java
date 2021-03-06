package com.cpstudio.zhuojiaren.fragment;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;

import com.cpstudio.zhuojiaren.R;
import com.cpstudio.zhuojiaren.adapter.QuanListAdapter;
import com.cpstudio.zhuojiaren.adapter.TitleAdapter;
import com.cpstudio.zhuojiaren.adapter.TitleAdapter.ImageOnclick;
import com.cpstudio.zhuojiaren.helper.ConnHelper;
import com.cpstudio.zhuojiaren.helper.JsonHandler;
import com.cpstudio.zhuojiaren.helper.UrlHelper;
import com.cpstudio.zhuojiaren.imageloader.LoadImage;
import com.cpstudio.zhuojiaren.model.ImageRadioButton;
import com.cpstudio.zhuojiaren.model.MsgTagVO;
import com.cpstudio.zhuojiaren.model.QuanVO;
import com.cpstudio.zhuojiaren.model.ResultVO;
import com.cpstudio.zhuojiaren.ui.ZhuoQuanActivity;
import com.cpstudio.zhuojiaren.ui.ZhuoQuanMainActivity;
import com.cpstudio.zhuojiaren.widget.ListViewFooter;
import com.cpstudio.zhuojiaren.widget.PopupWindows;
/**
 * 圈子列表
 */
public class QuanziFra extends Fragment {
	@InjectView(R.id.hgm_adv)
	ImageView advImage;
	@InjectView(R.id.fql_list)
	ListView mListView;
	@InjectView(R.id.fql_my_layout)
	RadioGroup myQuanziLayout;
	@InjectView(R.id.fql_my_create)
	RadioButton myCreate;
	@InjectView(R.id.fql_my_add)
	RadioButton myAdd;
	@InjectView(R.id.fql_quanzi_recommend)
	GridView quanziRecommend;
	@InjectView(R.id.fql_footer)
	LinearLayout shareFooter;
	@InjectView(R.id.fql_share)
	TextView share;
	@InjectView(R.id.fql_dissolve)
	TextView dissolve;
	private QuanListAdapter mAdapter;
	private ArrayList<QuanVO> mList = new ArrayList<QuanVO>();
	private ConnHelper mConnHelper = null;
	private int mPage = 0;
	private int mType = 6;
	private String url = "";
	private ListViewFooter mListViewFooter = null;
	private Context mContext;
	private PopupWindows pupWindow;
	// 筛选圈子
	private String city;
	private String gtype;
	// 六个类别
	ArrayList<ImageRadioButton> titleList = new ArrayList<ImageRadioButton>();

	TitleAdapter mTitleAdapter;
	// 主View
	View layout;
	String userid = null;

	public interface functionListener {
		//
		public void onTypeChange(int type);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		layout = inflater.inflate(R.layout.fragment_quanzi_list, container,
				false);
		ButterKnife.inject(this, layout);
		pupWindow = new PopupWindows(getActivity());
		mContext = getActivity();
		mConnHelper = ConnHelper.getInstance(getActivity());
		
		// 加载的圈子类型
		Bundle intent = getArguments();
		mType = intent.getInt(QuanVO.QUANZITYPE);
		if(mType == QuanVO.QUANZIRECOMMEND){
			advImage.setVisibility(View.VISIBLE);
			LoadImage.getInstance().beginLoad(
					"http://7xkb2a.com1.z0.glb.clouddn.com/android-gg.png",
					advImage);
		}
		userid = intent.getString("userid");
		mAdapter = new QuanListAdapter(mContext, mList);
		RelativeLayout mFooterView = (RelativeLayout) inflater.inflate(
				R.layout.listview_footer, null);
		mListView.addFooterView(mFooterView);
		mListViewFooter = new ListViewFooter(mFooterView, onMoreClick);
		mListView.setAdapter(mAdapter);
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				if (position != -1) {
					String groupid = (String) view.getTag(R.id.tag_id);
					Intent i = new Intent(mContext, ZhuoQuanMainActivity.class);
					i.putExtra("groupid", groupid);
					// 点击更多是出错
					if (groupid != null)
						startActivity(i);
				}
			}

		});
		initOnclick();
		loadData();
		return layout;
	}

	private void initOnclick() {
		// TODO Auto-generated method stub
		share.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				com.cpstudio.zhuojiaren.widget.CustomShareBoard shareBoard = new com.cpstudio.zhuojiaren.widget.CustomShareBoard(
						QuanziFra.this.getActivity());
				shareBoard.showCustomShareContent();
			}
		});
		// 1为解散，2为退出
		dissolve.setTag(1);
		dissolve.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if ((Integer) (v.getTag()) == 1) {
					// TODO Auto-generated method stub
					final View view = View.inflate(getActivity(),
							R.layout.popup_window_break_quazi, null);

					final EditText text = (EditText) view
							.findViewById(R.id.fql_break_reason);
					pupWindow.showBreakQuanzi(1, layout, view, 10,
							new OnClickListener() {

								@Override
								public void onClick(View v) {
									// TODO Auto-generated method stub
									StringBuilder groupid = new StringBuilder();
									for (QuanVO quan : mAdapter
											.getmSelectedList()) {
										groupid.append(quan.getGroupid() + ",");
									}
									mConnHelper.disolveQuan(groupid.toString(),
											text.getText().toString(),
											mUIHandler, MsgTagVO.disolve_quan,
											getActivity(), true, null, null);
									pupWindow.hidePop();
									if (QuanziFra.this.getActivity() instanceof ZhuoQuanActivity) {
										((ZhuoQuanActivity) (QuanziFra.this
												.getActivity()))
												.setOffManager();
									}
								}
							});
				} else if ((Integer) (v.getTag()) == 2) {
					final View view = View.inflate(getActivity(),
							R.layout.popup_window_off_quazi, null);
					final EditText text = (EditText) view
							.findViewById(R.id.fql_break_reason);
					pupWindow.showBreakQuanzi(2, layout, view, 10,
							new OnClickListener() {

								@Override
								public void onClick(View v) {
									// TODO Auto-generated method stub
									StringBuilder groupid = new StringBuilder();
									for (QuanVO quan : mAdapter
											.getmSelectedList()) {
										groupid.append(quan.getGroupid() + ",");
									}
									mConnHelper.quitQuan(groupid.toString(),
											"1", text.getText().toString(),
											mUIHandler, MsgTagVO.out_quan,
											getActivity(), true, null, null);
									pupWindow.hidePop();
									if (QuanziFra.this.getActivity() instanceof ZhuoQuanActivity) {
										((ZhuoQuanActivity) (QuanziFra.this
												.getActivity()))
												.setOffManager();
									}
								}
							});
				}
			}
		});

		// 如果是我的圈子，则展示我的圈子
		if (mType == QuanVO.QUANZIMYCTEATE || mType == QuanVO.QUANZIMYADD) {
			url = UrlHelper.getUrlMyGroupList();

			quanziRecommend.setVisibility(View.GONE);
			myQuanziLayout.setVisibility(View.VISIBLE);
			// 设置默认选中第一个；
			myQuanziLayout.check(myCreate.getId());
			myQuanziLayout
					.setOnCheckedChangeListener(new OnCheckedChangeListener() {

						@Override
						public void onCheckedChanged(RadioGroup group,
								int checkedId) {
							// TODO Auto-generated method stub
							if (checkedId == myCreate.getId()) {
								mType = QuanVO.QUANZIMYCTEATE;
								loadData();
								dissolve.setTag(1);
								dissolve.setText(R.string.dissolve);
							}

							else if (checkedId == myAdd.getId()) {
								mType = QuanVO.QUANZIMYADD;
								dissolve.setTag(2);
								dissolve.setText(R.string.off);
								loadData();
							}
						}
					});
		} else if (mType == QuanVO.QUANZIQUERY) {
			// url = ZhuoCommHelper.getUrlReGroupList();
			myQuanziLayout.setVisibility(View.GONE);
			quanziRecommend.setVisibility(View.VISIBLE);
			url = UrlHelper.getServiceSearchQuan();
			gtype = "1";
			titleList.clear();
			titleList.add(new ImageRadioButton(R.drawable.resourceu_quan,
					R.drawable.resource_quan));
			titleList.add(new ImageRadioButton(R.drawable.investu_quan,
					R.drawable.invest_quan));
			titleList.add(new ImageRadioButton(R.drawable.interestu_quan,
					R.drawable.interest_quan));
			titleList.add(new ImageRadioButton(R.drawable.growupu_quan,
					R.drawable.growup));
			titleList.add(new ImageRadioButton(R.drawable.cityu_quan,
					R.drawable.city2));
			titleList.add(new ImageRadioButton(R.drawable.activityu_quan,
					R.drawable.activity));
			mTitleAdapter = new TitleAdapter(getActivity(), titleList,
					R.layout.item_title_image);
			mTitleAdapter.setImageOnclick(new ImageOnclick() {

				@Override
				public void OnClickItem(ImageRadioButton item) {
					// TODO Auto-generated method stub
					int pos = 0;
					for (ImageRadioButton temp : titleList) {
						pos++;
						if (item.equals(temp)) {
							gtype = pos + "";
							loadData();
						}
					}
					mTitleAdapter.notifyDataSetChanged();
				}
			});
			quanziRecommend.setAdapter(mTitleAdapter);
		} else if (mType == QuanVO.QUANZIRECOMMEND) {
			url = UrlHelper.getUrlReGroupList();
		}

	}

	/**
	 * 管理圈子
	 */
	public void setManager() {
		if (mAdapter != null) {
			mAdapter.setManagerVisible(true);
			mAdapter.getmSelectedList().clear();
			mAdapter.notifyDataSetChanged();
		}
		shareFooter.setVisibility(View.VISIBLE);
	}

	/**
	 * 退出管理
	 */
	public void offManager() {
		if (mAdapter != null) {
			mAdapter.setManagerVisible(false);
			mAdapter.getmSelectedList().clear();
			mAdapter.notifyDataSetChanged();
		}
		shareFooter.setVisibility(View.GONE);
	}

	private void updateItemList(String data, boolean refresh, boolean append) {
		try {
			if (data != null && !data.equals("")) {
				ArrayList<QuanVO> list = JsonHandler.parseQuanList(data);
				if (!list.isEmpty()) {
					mListViewFooter.hasData();
					if (!append) {
						mList.clear();
					}
					mList.addAll(list);
					mAdapter.notifyDataSetChanged();
					mPage++;
				} else {
					mListViewFooter.noData(!refresh);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@SuppressLint("HandlerLeak")
	private Handler mUIHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MsgTagVO.DATA_LOAD: {
				mListViewFooter.finishLoading();
				ResultVO res;
				if (JsonHandler.checkResult((String) msg.obj, getActivity())) {
					res = JsonHandler.parseResult((String) msg.obj);
				} else {
					return;
				}
				String data = res.getData();
				// 如果是我的圈子
				/*
				 * { "code" : <int> (返回码) , "msg" : <string> (返回信息) , "data" :
				 * <json> { "createGroups" : <array> (我创建的圈子) [
				 * {"groupid":<string>(圈子ID), "gname":<string>(圈子名字), "gintro":
				 * <string>(圈子简介), "memberCount":<int>(成员数)} ... ]
				 * "followGroups" : <array> (我加入的圈子) [
				 * {"groupid":<string>(圈子ID), "gname":<string>(圈子名字), "gintro":
				 * <string>(圈子简介), "memberCount":<int>(成员数)} ... ] } }
				 */
				if (mType == QuanVO.QUANZIMYCTEATE) {
					try {
						JSONObject obj = new JSONObject(data);
						data = obj.getString("createGroups");
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} else if (mType == QuanVO.QUANZIMYADD) {
					try {
						JSONObject obj = new JSONObject(data);
						data = obj.getString("followGroups");
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				updateItemList(data, true, false);
				break;
			}
			case MsgTagVO.DATA_MORE: {
				mListViewFooter.finishLoading();
				ResultVO res;
				if (JsonHandler.checkResult((String) msg.obj, getActivity())) {
					res = JsonHandler.parseResult((String) msg.obj);
				} else {
					return;
				}
				String data = res.getData();
				updateItemList(data, false, true);
				break;
			}
			case MsgTagVO.disolve_quan:
			case MsgTagVO.out_quan: {
				// 退出管理，重新下载数据
				offManager();
				loadData();
			}
			}
		}
	};

	private void loadData() {
		if (mListViewFooter.startLoading()) {
			mList.clear();
			mPage = 0;
			mAdapter.notifyDataSetChanged();
			if (url != null && (!url.isEmpty()))
				mConnHelper.getQuanzi(url, gtype, city, 0, 5, mUIHandler,
						MsgTagVO.DATA_LOAD, getActivity(), true, null, null,
						userid);
		}
	}

	private void loadMore() {
		if (mListViewFooter.startLoading()) {

			mConnHelper
					.getQuanzi(url, gtype, city, mPage, 5, mUIHandler,
							MsgTagVO.DATA_MORE, getActivity(), true, null,
							null, userid);
		}
	}

	private OnClickListener onMoreClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			loadMore();
		}
	};

	public void onResult(Intent data) {
		// TODO Auto-generated method stub
		url = UrlHelper.getServiceSearchQuan();
		// 先设置为空
		gtype = null;
		city = null;
		int type = data.getIntExtra("type", 1);
		if (type == 1) {
			gtype = data.getIntExtra("quanzitype", 1) + "";
		} else {
			city = data.getIntExtra("city", 1) + "";
		}
		loadData();
	}
}
