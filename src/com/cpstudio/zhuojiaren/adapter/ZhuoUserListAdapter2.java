package com.cpstudio.zhuojiaren.adapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.cpstudio.zhuojiaren.helper.ZhuoCommHelper;
import com.cpstudio.zhuojiaren.imageloader.LoadImage;
import com.cpstudio.zhuojiaren.model.UserAndCollection;
import com.cpstudio.zhuojiaren.model.UserVO;
import com.cpstudio.zhuojiaren.model.ZhuoInfoVO;
import com.cpstudio.zhuojiaren.util.CommonAdapter;
import com.cpstudio.zhuojiaren.util.CommonUtil;
import com.cpstudio.zhuojiaren.util.DeviceInfoUtil;
import com.cpstudio.zhuojiaren.util.ViewHolder;
import com.cpstudio.zhuojiaren.R;
import com.cpstudio.zhuojiaren.UserCardActivity;
import com.utils.ImageRectUtil;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.text.TextPaint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
/**
 * 倬家人adapter
 * 同城、童趣、同行等
 * @author lef
 *
 */
public class ZhuoUserListAdapter2 extends CommonAdapter<UserAndCollection> {
	LoadImage loadImage = new LoadImage(50);
	public ZhuoUserListAdapter2(Context context, List<UserAndCollection> mDatas,
			int itemLayoutId) {
		super(context, mDatas, itemLayoutId);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void convert(ViewHolder helper, UserAndCollection item) {
		// TODO Auto-generated method stub
		helper.setText(R.id.izul_name, item.getUser().getUsername());
		helper.setText(R.id.izul_company, item.getUser().getCompany());
		helper.setText(R.id.izul_position, item.getUser().getPost());
		helper.setImageResource(R.id.izul_collect, R.drawable.tab_collect_off);
		if(item.getIsCollection().equals(UserAndCollection.collection)){
			helper.setImageResource(R.id.izul_collect, R.drawable.tab_collect_on);
		}
		ImageView iv = (ImageView)helper.getView(R.id.izul_image);
		iv.setTag(item.getUser().getUheader());
		loadImage.addTask(item.getUser().getUheader(), iv);
		loadImage.doTask();
	}
	
}
