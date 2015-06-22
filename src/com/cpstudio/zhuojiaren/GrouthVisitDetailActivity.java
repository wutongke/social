package com.cpstudio.zhuojiaren;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;

import com.cpstudio.zhuojiaren.imageloader.LoadImage;
import com.cpstudio.zhuojiaren.model.GrouthVisit;

public class GrouthVisitDetailActivity extends BaseActivity {
	@InjectView(R.id.agvd_image)
	ImageView image;
	@InjectView(R.id.agvd_name_and_order)
	TextView name;
	@InjectView(R.id.agvd_content)
	TextView content;
	@InjectView(R.id.agvd_time)
	TextView time;
	LoadImage imageLoader;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_grouth_visit_detail);
		ButterKnife.inject(this);
		initTitle();
		title.setText(R.string.visit_detail);
		imageLoader = new LoadImage();
		loadData();
	}
	private void loadData() {
		// TODO Auto-generated method stub
		GrouthVisit g = new GrouthVisit();
		g.setImageUrl("http://img0.imgtn.bdimg.com/it/u=3317101867,3739965699&fm=11&gp=0.jpg");
		g.setName("张博士亲授");
		g.setContent("三井是个逃兵，毋庸置疑。他曾经是个骄傲的战士，带着无可置疑的天赋与荣誉。对于这样的人来说，一场意外就是一次灾难，他陷入了一个令人绝望的困境，最可怕的是，所有关于未来的规划被迫打断。这是一个少年遇到的最大困境，他面对的是可能永远不能重回巅峰的现实，这个现实对于他来说，太过残酷。");
		g.setTime("2013.5.6");
		g.setOrder("第十二期");
		Message msg = uiHandler.obtainMessage();
		msg.obj = g;
		msg.sendToTarget();
	}
	Handler uiHandler =new Handler(){
		public void handleMessage(android.os.Message msg) {
			GrouthVisit result = (GrouthVisit) msg.obj;
			content.setText(result.getContent());
			name.setText(result.getName()+"  "+result.getOrder());
			time.setText(result.getTime());
			imageLoader.beginLoad(result.getImageUrl(), image) ;
		};
	};
}
