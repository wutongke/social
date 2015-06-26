package com.cpstudio.zhuojiaren.adapter;

import java.util.List;

import android.content.Context;

import com.cpstudio.zhuojiaren.R;
import com.cpstudio.zhuojiaren.model.IncomeVO;
import com.cpstudio.zhuojiaren.util.CommonAdapter;
import com.cpstudio.zhuojiaren.util.CommonUtil;
import com.cpstudio.zhuojiaren.util.ViewHolder;

public class IncomeAdapter extends CommonAdapter<IncomeVO>{

	public IncomeAdapter(Context context, List<IncomeVO> mDatas,
			int itemLayoutId) {
		super(context, mDatas, itemLayoutId);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void convert(ViewHolder helper, IncomeVO item) {
		// TODO Auto-generated method stub
		helper.setText(R.id.ii_name, item.getName());
		String time = item.getTime();
		helper.setText(R.id.ii_time, CommonUtil.getTime(Long.parseLong(time)));
		helper.setText(R.id.ii_money, item.getMoney());
	}
	
}
