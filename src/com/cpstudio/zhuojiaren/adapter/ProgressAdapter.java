package com.cpstudio.zhuojiaren.adapter;

import java.util.List;

import android.content.Context;

import com.cpstudio.zhuojiaren.R;
import com.cpstudio.zhuojiaren.model.ProgressVO;
import com.cpstudio.zhuojiaren.util.CommonAdapter;
import com.cpstudio.zhuojiaren.util.ViewHolder;

public class ProgressAdapter extends CommonAdapter<ProgressVO>{

	public ProgressAdapter(Context context, List<ProgressVO> mDatas,
			int itemLayoutId) {
		super(context, mDatas, itemLayoutId);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void convert(ViewHolder helper, ProgressVO item) {
		// TODO Auto-generated method stub
		helper.setText(R.id.ip_people_name, item.getUser().getUsername());
		helper.setText(R.id.ip_content, item.getContent());
		
	}

	

}
