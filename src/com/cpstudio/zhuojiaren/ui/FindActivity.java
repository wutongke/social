package com.cpstudio.zhuojiaren.ui;

import java.util.ArrayList;
import java.util.HashMap;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.cpstudio.zhuojiaren.R;
import com.cpstudio.zhuojiaren.facade.UserFacade;
import com.cpstudio.zhuojiaren.helper.JsonHandler;
import com.cpstudio.zhuojiaren.helper.ConnHelper;
import com.cpstudio.zhuojiaren.model.MsgTagVO;
import com.cpstudio.zhuojiaren.model.UserNewVO;
import com.cpstudio.zhuojiaren.util.CommonUtil;
/**
 * 首页左上角发现按钮跳转到的界面
 * @author lz
 *
 */
public class FindActivity extends Activity {
	int[] imageIds = { R.drawable.circleu, R.drawable.resourceu,
			R.drawable.fianceu, R.drawable.zcityu, R.drawable.travelu,
			R.drawable.nearu, R.drawable.interestu, R.drawable.teacheru,
			R.drawable.allu };

	Class[] classArrays = { ZhuoQuanActivity.class, ResourceGXActivity.class,

	CrowdFundingActivity.class,// 众筹
			UserSameActivity.class,// 同城
			UserSameActivity.class,// 同行
			UserSameActivity.class,// 附件
			UserSameActivity.class,// 同趣
			UserSameActivity.class, UserSameActivity.class };
	private ConnHelper mConnHelper = null;
	private UserFacade mFacade = null;
	String uid = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_find_lz);
		mConnHelper = ConnHelper.getInstance(getApplicationContext());

		mFacade = new UserFacade(getApplicationContext());
		uid = mConnHelper.getUserid();
		GridView gridview = (GridView) findViewById(R.id.gridview_procedure);

		ArrayList<HashMap<String, Object>> imagelist = new ArrayList<HashMap<String, Object>>();
		// 使用HashMap将图片添加到一个数组中，注意一定要是HashMap<String,Object>类型的，因为装到map中的图片要是资源ID，而不是图片本身
		// 如果是用findViewById(R.drawable.image)这样把真正的图片取出来了，放到map中是无法正常显示的
		for (int i = 0; i < imageIds.length; i++) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("image", imageIds[i]);
			map.put("id", imageIds[i]);
			imagelist.add(map);
		}

		SimpleAdapter simpleAdapter = new SimpleAdapter(FindActivity.this,
				imagelist, R.layout.gridview_item,
				new String[] { "image", "id" },
				new int[] { R.id.image, R.id.id });
		// 设置GridView的适配器为新建的simpleAdapter
		gridview.setAdapter(simpleAdapter);

		gridview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub

				Intent i = new Intent(FindActivity.this, classArrays[arg2]);
				if (arg2 == 3)
					i.putExtra("type", 1);
				if (arg2 == 4)
					i.putExtra("type", 2);
				if (arg2 == 5)
					i.putExtra("type", 3);
				if (arg2 == 6)
					i.putExtra("type", 4);
				if (arg2 == 7)
					i.putExtra("type", 5);
				if (arg2 == 8)
					i.putExtra("type", 6);
				startActivity(i);

			}
		});
		loadInfo();
		initClick();
		mConnHelper = ConnHelper.getInstance(getApplicationContext());
	}

	@SuppressLint("HandlerLeak")
	private Handler mUIHandler = new Handler() {
		@SuppressWarnings("unchecked")
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MsgTagVO.DATA_OTHER: {
				UserNewVO user = null;
				if (msg.obj instanceof UserNewVO) {
					user = (UserNewVO) msg.obj;
				} else if (msg.obj != null && !msg.obj.equals("")) {
					JsonHandler nljh = new JsonHandler((String) msg.obj,
							getApplicationContext());
					user = nljh.parseNewUser();
				}

				int place = user.getHometown();

				if (mConnHelper != null && mConnHelper.getCitys() != null
						&& place >= 1 && place <= mConnHelper.getCitys().size())
					((TextView) findViewById(R.id.textViewPosInfo))
							.setText(mConnHelper.getCitys().get(place - 1)
									.getCityName());
				((TextView) findViewById(R.id.textViewPosInfo))
						.setVisibility(View.VISIBLE);
				break;
			}
			}
		}
	};

	private void loadInfo() {
		((TextView) findViewById(R.id.textViewPosInfo))
				.setVisibility(View.GONE);
		if (CommonUtil.getNetworkState(getApplicationContext()) == 2
				&& uid != null) {

			UserNewVO user = mFacade.getSimpleInfoById(uid);
			if (user == null) {
				CommonUtil.displayToast(getApplicationContext(),
						R.string.error0);
			} else {
				Message msg = mUIHandler.obtainMessage(MsgTagVO.DATA_OTHER);
				msg.obj = user;
				msg.sendToTarget();
			}
		} else {
			mConnHelper.getUserInfo(mUIHandler, MsgTagVO.DATA_OTHER, null);
		}
	}

	private void initClick() {

		findViewById(R.id.buttonBack).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				FindActivity.this.finish();
			}
		});
	}
}
