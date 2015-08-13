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
import com.cpstudio.zhuojiaren.model.ChangeBgAVO;
import com.cpstudio.zhuojiaren.util.CommonAdapter;

import android.os.Bundle;
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
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_change_background);
		initTitle();
		title.setText(R.string.title_activity_change_background);
		ButterKnife.inject(this);
		
		for(int i=0;i<30;i++)
		{
			ChangeBgAVO iterm=new ChangeBgAVO();
			iterm.setUrl("http://pica.nipic.com/2007-11-09/2007119124513598_2.jpg");
			mList.add(iterm);
		}
		
		mAdapter = new ChangeBgGridViewAdatper(ChangeBackgroundActivity.this,
				mList, R.layout.item_imageview);
		gvBackGround.setAdapter(mAdapter);
		initClick();
	}

	private void initClick() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		Toast.makeText(ChangeBackgroundActivity.this,
				arg2 + mList.get(arg2).getUrl(), 1000).show();
	}

}
