package com.cpstudui.zhuojiaren.lz;

import java.util.ArrayList;
import java.util.HashMap;

import com.cpstudio.zhuojiaren.BaseActivity;
import com.cpstudio.zhuojiaren.R;
import com.cpstudio.zhuojiaren.R.id;
import com.cpstudio.zhuojiaren.R.layout;
import com.cpstudio.zhuojiaren.R.menu;
import com.cpstudio.zhuojiaren.R.string;
import com.cpstudio.zhuojiaren.adapter.ChangeBgGridViewAdatper;
import com.cpstudio.zhuojiaren.helper.JsonHandler;
import com.cpstudio.zhuojiaren.helper.ResHelper;
import com.cpstudio.zhuojiaren.helper.ZhuoConnHelper;
import com.cpstudio.zhuojiaren.model.ChangeBgAVO;
import com.cpstudio.zhuojiaren.model.MsgTagVO;
import com.cpstudio.zhuojiaren.model.OrderVO;
import com.cpstudio.zhuojiaren.util.CommonAdapter;
import com.cpstudio.zhuojiaren.util.CommonUtil;
import com.utils.PreferenceUtil;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import butterknife.ButterKnife;
import butterknife.InjectView;

public class ChangeBackgroundActivity extends BaseActivity implements
		OnItemClickListener {

	@InjectView(R.id.gridview_bg)
	GridView gvBackGround;
	CommonAdapter mAdapter;
	ArrayList<ChangeBgAVO> mList = new ArrayList<ChangeBgAVO>();
	private ZhuoConnHelper mConnHelper = null;
	private ResHelper mResHelper = null;
	int bgVersion=0;
	private Handler mUIHandler = new Handler() {
		@SuppressWarnings("unchecked")
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MsgTagVO.DATA_LOAD: {
				ChangeBgAVO item = null;
				if (msg.obj instanceof ChangeBgAVO) {
					item = (ChangeBgAVO) msg.obj;
				} else {
//					if (msg.obj != null && !msg.obj.equals("")) {
//						JsonHandler nljh = new JsonHandler((String) msg.obj,
//								getApplicationContext());
//						item = nljh.parseOrderVO();
//						if (item != null) {
//							// infoFacade.update(list);
//							updateData(item);
//						}
//					}
				}
				break;
			}
			}
		}

	};
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
		bgVersion=mResHelper.getBgVersion();
		initClick();
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
		Toast.makeText(ChangeBackgroundActivity.this,
				arg2 + mList.get(arg2).getBgid(), 1000).show();
		mConnHelper.setCardBg(mUIHandler, MsgTagVO.DATA_OTHER, bgVersion);
		
	}

}
