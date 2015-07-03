package com.cpstudui.zhuojiaren.lz;

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
import com.cpstudio.zhuojiaren.helper.BaiduLocationHelper;
import com.cpstudio.zhuojiaren.helper.ZhuoConnHelper;
import com.cpstudio.zhuojiaren.model.MsgTagVO;
import com.cpstudio.zhuojiaren.ui.CrowdFundingActivity;
import com.cpstudio.zhuojiaren.ui.UserSameActivity;
import com.cpstudio.zhuojiaren.ui.ZhuoQuanActivity;

public class FindActivity extends Activity {
	private String mLocation = "";

	private BaiduLocationHelper locationHelper = null;
	private ZhuoConnHelper mConnHelper = null;
	private ArrayList<BeanNotice> noticesListData;

	int[] imageIds = { R.drawable.circleu, R.drawable.resourceu,
			R.drawable.fianceu, R.drawable.zcityu,
			R.drawable.travelu, R.drawable.nearu,
			R.drawable.interestu, R.drawable.teacheru,
			R.drawable.allu };

//	Class[] classArrays = { QuanListActivity.class, MsgResourceActivity.class,

	Class[] classArrays = { ZhuoQuanActivity.class, ResourceGXActivity.class,

			CrowdFundingActivity.class,// 众筹
			UserSameActivity.class,// 同城
			UserSameActivity.class,// 同行
			UserSameActivity.class,// 附件
			UserSameActivity.class,// 同趣
			UserSameActivity.class, UserSameActivity.class };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_find_lz);

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

		initClick();
		mConnHelper = ZhuoConnHelper.getInstance(getApplicationContext());
		locationHelper = new BaiduLocationHelper(getApplicationContext(),
				mUIHandler, MsgTagVO.UPDATE_LOCAL);

	}

	@Override
	protected void onDestroy() {
		if (locationHelper != null) {
			locationHelper.stopLocation();
		}
		super.onDestroy();
	}

	@Override
	protected void onPause() {
		if (locationHelper != null) {
			locationHelper.stopLocation();
		}
		super.onPause();
	}

	@Override
	protected void onResume() {
		if (locationHelper != null) {
			locationHelper.startLocation();
		}
		super.onResume();
	}

	@SuppressLint("HandlerLeak")
	private Handler mUIHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {

			case MsgTagVO.UPDATE_LOCAL: {
				String locationinfo = (String) msg.obj;
				if (null != locationinfo && !locationinfo.trim().equals("")) {
					mLocation = locationinfo;
					((TextView) findViewById(R.id.textViewPosInfo))
							.setText("当前位置：" + locationinfo);
				} else {
					((TextView) findViewById(R.id.textViewPosInfo))
							.setText(getString(R.string.error10));
				}
				break;
			}
			default: {

			}

			}
		}
	};

	private void initClick() {

		findViewById(R.id.buttonBack).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				FindActivity.this.finish();
			}
		});
		// findViewById(R.id.relativeLayoutZJQZ).setOnClickListener(
		// new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// Intent i = new Intent(FindActivity.this,
		// QuanListActivity.class);
		// startActivity(i);
		// }
		// });
		// findViewById(R.id.relativeLayoutTCJR).setOnClickListener(
		// new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// Intent i = new Intent(FindActivity.this,
		// UserSameActivity.class);
		// i.putExtra("type", 1);
		// startActivity(i);
		// }
		// });
		// findViewById(R.id.relativeLayoutTHJR).setOnClickListener(
		// new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// Intent i = new Intent(FindActivity.this,
		// FieldSelectUserActivity.class);
		// startActivity(i);
		// }
		// });
		// findViewById(R.id.relativeLayoutTXJR).setOnClickListener(
		// new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// Intent i = new Intent(FindActivity.this,
		// UserSameActivity.class);
		// i.putExtra("type", 3);
		// startActivity(i);
		// }
		// });
		//
		// findViewById(R.id.relativeLayoutZYGX).setOnClickListener(
		// new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// Intent i = new Intent(FindActivity.this,
		// MsgResourceActivity.class);
		// startActivity(i);
		// }
		// });
		// findViewById(R.id.relativeLayoutZXDS).setOnClickListener(
		// new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// Intent i = new Intent(FindActivity.this,
		// TeacherListActivity.class);
		// startActivity(i);
		// }
		// });
		//
		// findViewById(R.id.relativeLayoutSYJR).setOnClickListener(
		// new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// Intent i = new Intent(FindActivity.this,
		// UserAllActivity.class);
		// startActivity(i);
		// }
		// });

	}
}
