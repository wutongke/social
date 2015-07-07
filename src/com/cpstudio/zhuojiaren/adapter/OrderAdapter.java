package com.cpstudio.zhuojiaren.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.cpstudio.zhuojiaren.R;
import com.cpstudio.zhuojiaren.imageloader.LoadImage;
import com.cpstudio.zhuojiaren.model.GoodsVO;
import com.cpstudio.zhuojiaren.model.OrderVO;
import com.cpstudio.zhuojiaren.util.CommonAdapter;
import com.cpstudio.zhuojiaren.util.ViewHolder;

public class OrderAdapter extends CommonAdapter<GoodsVO> {
	LoadImage loader = new LoadImage();
	ArrayList<GoodsVO> selectList = new ArrayList<GoodsVO>();
	SelectGoodsChangeListener goodsChangeListenter;
	

	public OrderAdapter(Context context, List<GoodsVO> mDatas, int itemLayoutId) {
		super(context, mDatas, itemLayoutId);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void convert(ViewHolder helper, final GoodsVO item) {
		// TODO Auto-generated method stub
		final CheckBox checkBox = (CheckBox) helper.getView(R.id.icg_select);
		checkBox.setChecked(false);
		if (selectList.contains(item)) {
			checkBox.setChecked(true);
		} 
		checkBox.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (selectList.contains(item)) {
					checkBox.setChecked(false);
					selectList.remove(item);
					if(goodsChangeListenter!=null)
						goodsChangeListenter.onGoodsChange(addAllGoodsPrice(),selectList.size());
				} else {
					checkBox.setChecked(true);
					selectList.add(item);
					if(goodsChangeListenter!=null)
						goodsChangeListenter.onGoodsChange(addAllGoodsPrice(),selectList.size());
				}
			}
		});
		// ����
		helper.setText(R.id.icg_name, item.getName());
		helper.setText(R.id.icg_price, item.getZhuobi());
		final TextView goodsNum = (TextView) helper.getView(R.id.icg_num_text);
		if (item.getGoodsCount() == null||item.getGoodsCount().isEmpty()) {
			helper.setText(R.id.icg_num_text, "1");
		} else {
			helper.setText(R.id.icg_num_text, item
					.getGoodsCount());
		}
		// ��Ʒ����+1
		helper.getView(R.id.icg_num_add).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						int num = Integer.parseInt(goodsNum.getText()
								.toString());
						goodsNum.setText(num + 1 + "");
						item.setGoodsCount(num + 1 + "");
						if(goodsChangeListenter!=null)
							goodsChangeListenter.onGoodsChange(addAllGoodsPrice(),selectList.size());
							
					}
				});
		helper.getView(R.id.icg_num_minus).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						int num = Integer.parseInt(goodsNum.getText()
								.toString());
						if (num > 1)
							goodsNum.setText(num - 1 + "");
						item.setGoodsCount(num - 1 + "");
						if(goodsChangeListenter!=null)
							goodsChangeListenter.onGoodsChange(addAllGoodsPrice(),selectList.size());
					}
				});
		loader.beginLoad(
				item.getPic().get(0).getOrgurl(),
				(ImageView) helper.getView(R.id.icg_goods_image));
	}
	public interface SelectGoodsChangeListener{
		//��Ʒ�۸��ܶ����Ʒ�����ܶ�
		public void onGoodsChange(int sum,int count);
	}
	public ArrayList<GoodsVO> getSelectList() {
		return selectList;
	}

	public void setSelectList(ArrayList<GoodsVO> selectList) {
		this.selectList = selectList;
	}

	public SelectGoodsChangeListener getGoodsChangeListenter() {
		return goodsChangeListenter;
	}

	public void setGoodsChangeListenter(
			SelectGoodsChangeListener goodsChangeListenter) {
		this.goodsChangeListenter = goodsChangeListenter;
	}
	private int addAllGoodsPrice(){
		int sum = 0;
		int tempCount=0;
		for(GoodsVO temp:selectList){
			if(temp.getGoodsCount()==null||temp.getGoodsCount().isEmpty())
				tempCount=1;
			else tempCount = Integer.parseInt(temp.getGoodsCount());
				
			sum+=Integer.parseInt(temp.getZhuobi())*tempCount;
		}
		return sum;
	}
}