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
		g.setName("�Ų�ʿ����");
		g.setContent("�����Ǹ��ӱ�����ӹ���ɡ��������Ǹ�������սʿ�������޿����ɵ��츳����������������������˵��һ���������һ�����ѣ���������һ�����˾���������������µ��ǣ����й���δ���Ĺ滮���ȴ�ϡ�����һ�������������������������Ե��ǿ�����Զ�����ػ��۷����ʵ�������ʵ��������˵��̫���пᡣ");
		g.setTime("2013.5.6");
		g.setOrder("��ʮ����");
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
