package com.cpstudio.zhuojiaren.ui;

import java.lang.reflect.Type;
import java.util.ArrayList;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import butterknife.ButterKnife;
import butterknife.InjectView;

import com.cpstudio.zhuojiaren.R;
import com.cpstudio.zhuojiaren.helper.ConnHelper;
import com.cpstudio.zhuojiaren.helper.JsonHandler;
import com.cpstudio.zhuojiaren.model.GrouthType;
import com.cpstudio.zhuojiaren.model.ResultVO;
import com.cpstudio.zhuojiaren.util.CommonUtil;
import com.cpstudio.zhuojiaren.widget.PopupWindows;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
/**
 * @author lef
 *
 */
public class GrouthChooseActivity extends BaseActivity {
	@InjectView (R.id.layout)
	RelativeLayout ll;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_grouth_choose);
		ButterKnife.inject(this);
		initTitle();
		title.setText(R.string.grouth_online);
		
		findViewById(R.id.according_teacher).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				new PopupWindows(GrouthChooseActivity.this).showGrouthTeacher(ll);
			}
		});
		findViewById(R.id.according_type).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//œ¬‘ÿ¿‡–Õ
				ConnHelper.getInstance(GrouthChooseActivity.this).getGrowthOnlineType(new Handler(){
					@Override
					public void handleMessage(Message msg) {
						// TODO Auto-generated method stub
						super.handleMessage(msg);
						ResultVO res;
						if (JsonHandler.checkResult((String) msg.obj, GrouthChooseActivity.this)) {
							res = JsonHandler.parseResult((String) msg.obj);
						} else {
							CommonUtil.displayToast(GrouthChooseActivity.this, R.string.error0);
							return;
						}
						
						String data = res.getData();
						Type listType = new TypeToken<ArrayList<GrouthType>>() {
						}.getType();
						Gson gson = new Gson();
						final ArrayList<GrouthType> list = gson.fromJson(data, listType);
						new PopupWindows(GrouthChooseActivity.this).showGrouthType(list,ll, 0);
					}
				}, 1,GrouthChooseActivity.this);
			}
		});
	}
}
