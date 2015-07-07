package com.cpstudio.zhuojiaren.ui;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;

import com.cpstudio.zhuojiaren.R;
import com.cpstudio.zhuojiaren.model.GoodsVO;

public class OrderSubmitActivity extends Activity {
	
	ArrayList<GoodsVO> mDataList = new ArrayList<GoodsVO>();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_order_submit);
		
		
	}

}
