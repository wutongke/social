package com.cpstudui.zhuojiaren.lz;

import java.util.ArrayList;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.Toast;
import butterknife.ButterKnife;
import butterknife.InjectView;

import com.cpstudio.zhuojiaren.BaseActivity;
import com.cpstudio.zhuojiaren.R;
import com.cpstudio.zhuojiaren.adapter.ChangeBgGridViewAdatper;
import com.cpstudio.zhuojiaren.helper.JsonHandler;
import com.cpstudio.zhuojiaren.helper.ResHelper;
import com.cpstudio.zhuojiaren.helper.ZhuoConnHelper;
import com.cpstudio.zhuojiaren.model.ChangeBgAVO;
import com.cpstudio.zhuojiaren.model.MsgTagVO;
import com.cpstudio.zhuojiaren.util.CommonAdapter;
import com.cpstudio.zhuojiaren.util.CommonUtil;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class ChangeBackgroundActivity extends BaseActivity implements
		OnItemClickListener {

	@InjectView(R.id.gridview_bg)
	GridView gvBackGround;
	CommonAdapter mAdapter;
	ArrayList<ChangeBgAVO> mList = new ArrayList<ChangeBgAVO>();
	private ZhuoConnHelper mConnHelper = null;
	private ResHelper mResHelper = null;
	int bgVersion = 0;
	private Handler mUIHandler = new Handler() {
		@SuppressWarnings("unchecked")
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MsgTagVO.DATA_LOAD: {
				ArrayList<ChangeBgAVO> items = new ArrayList<ChangeBgAVO>();
				if (msg.obj instanceof ChangeBgAVO) {
					items = (ArrayList<ChangeBgAVO>) msg.obj;
				} else {
					if (msg.obj != null && !msg.obj.equals("")) {
						JsonHandler nljh = new JsonHandler((String) msg.obj,
								getApplicationContext());
						bgVersion = parseVersion((String) msg.obj);
						mResHelper.setBgVersion(bgVersion);
						items = nljh.parseBg();
						if (items != null && items.size() > 0) {
							mList.clear();
							mList.addAll(items);
							updataPics();
						} else {
							String str = mResHelper.getBgPics();
							if (str != null) {
								String[] arrays = str.split(";");
								if (arrays != null && arrays.length > 0) {
									mList.clear();
									for (int i = 0; i < arrays.length; i++) {
										String string = arrays[i];
										ChangeBgAVO vo = new ChangeBgAVO();
										vo.setBgid(i + 1);
										vo.setBgpic(string);
										mList.add(vo);
									}
								}
							}
						}
					}

				}
				mAdapter.notifyDataSetChanged();
			}
				break;
			case MsgTagVO.DATA_OTHER:
				if (JsonHandler.checkResult((String) msg.obj)) {
					Toast.makeText(ChangeBackgroundActivity.this,
							getString(R.string.bg_success), Toast.LENGTH_SHORT)
							.show();
				} else {
					Toast.makeText(ChangeBackgroundActivity.this,
							getString(R.string.bg_failed), Toast.LENGTH_SHORT)
							.show();
				}
				break;
			}

		}

	};

	private void updataPics() {
		if (mList != null) {
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < mList.size(); i++) {
				if (mList.get(i) == null)
					continue;
				if (!sb.equals(""))
					sb.append(";");
				sb.append(mList.get(i).getBgpic());

			}
			mResHelper.setBgPics(sb.toString());
		}
	}

	private int parseVersion(String str) {
		JsonParser parser = new JsonParser();
		JsonElement ele = parser.parse(str);
		JsonObject object = ele.getAsJsonObject();
		int version = 0;
		try {
			version = object.get("version").getAsInt();
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
		return version;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_change_background);
		initTitle();
		title.setText(R.string.title_activity_change_background);
		ButterKnife.inject(this);
		mConnHelper = ZhuoConnHelper.getInstance(getApplicationContext());
		mResHelper = ResHelper.getInstance(getApplicationContext());
		mAdapter = new ChangeBgGridViewAdatper(ChangeBackgroundActivity.this,
				mList, R.layout.item_imageview);
		gvBackGround.setAdapter(mAdapter);
		bgVersion = mResHelper.getBgVersion();
		initClick();
		loadData();
	}

	private void initClick() {
		// TODO Auto-generated method stub

	}

	private void loadData() {
		// TODO Auto-generated method stub
		if (CommonUtil.getNetworkState(getApplicationContext()) == 2) {
		} else {
			mConnHelper.getCardBg(mUIHandler, MsgTagVO.DATA_LOAD, bgVersion);
		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		// Toast.makeText(ChangeBackgroundActivity.this,
		// arg2 + mList.get(arg2).getBgid(), 1000).show();
		mConnHelper.setCardBg(mUIHandler, MsgTagVO.DATA_OTHER, bgVersion);
	}

}
