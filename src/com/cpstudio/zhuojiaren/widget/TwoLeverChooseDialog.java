package com.cpstudio.zhuojiaren.widget;

import kankan.wheel.widget.OnWheelChangedListener;
import kankan.wheel.widget.WheelView;
import kankan.wheel.widget.adapters.ArrayWheelAdapter;
import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.cpstudio.zhuojiaren.R;

public class TwoLeverChooseDialog extends AlertDialog{

	private Context mContext;
	private WheelView firstLeverWV = null;
	private WheelView secondLeverWV = null;
	private TextView selectedContent;//存放选择的结果
	
	
	private String[]province;
	private String[][]itemArray;//=new String[50][50];
	private int[] itemIds ;//= {R.array.beijin_province_item, R.array.tianjin_province_item, R.array.heibei_province_item, R.array.shanxi1_province_item, R.array.neimenggu_province_item, R.array.liaoning_province_item, R.array.jilin_province_item, R.array.heilongjiang_province_item, R.array.shanghai_province_item, R.array.jiangsu_province_item, R.array.zhejiang_province_item, R.array.anhui_province_item, R.array.fujian_province_item, R.array.jiangxi_province_item, R.array.shandong_province_item, R.array.henan_province_item, R.array.hubei_province_item, R.array.hunan_province_item, R.array.guangdong_province_item,  R.array.guangxi_province_item, R.array.hainan_province_item, R.array.chongqing_province_item, R.array.sichuan_province_item, R.array.guizhou_province_item, R.array.yunnan_province_item, R.array.xizang_province_item, R.array.shanxi2_province_item, R.array.gansu_province_item, R.array.qinghai_province_item, R.array.linxia_province_item, R.array.xinjiang_province_item, R.array.hongkong_province_item, R.array.aomen_province_item, R.array.taiwan_province_item};
	
	/**
	 * 
	 * @param context
	 * @param theme
	 * @param mypro
	 * @param mycity
	 * @param firstLeverId R.array.province_item
	 * @param secondLecerIds //= {R.array.beijin_province_item, R.array.tianjin_province_item, R.array.heibei_province_item, R.array.shanxi1_province_item, R.array.neimenggu_province_item, R.array.liaoning_province_item, R.array.jilin_province_item, R.array.heilongjiang_province_item, R.array.shanghai_province_item, R.array.jiangsu_province_item, R.array.zhejiang_province_item, R.array.anhui_province_item, R.array.fujian_province_item, R.array.jiangxi_province_item, R.array.shandong_province_item, R.array.henan_province_item, R.array.hubei_province_item, R.array.hunan_province_item, R.array.guangdong_province_item,  R.array.guangxi_province_item, R.array.hainan_province_item, R.array.chongqing_province_item, R.array.sichuan_province_item, R.array.guizhou_province_item, R.array.yunnan_province_item, R.array.xizang_province_item, R.array.shanxi2_province_item, R.array.gansu_province_item, R.array.qinghai_province_item, R.array.linxia_province_item, R.array.xinjiang_province_item, R.array.hongkong_province_item, R.array.aomen_province_item, R.array.taiwan_province_item};
	 */
	public TwoLeverChooseDialog(Context context,int theme,String defaultFirstLeverItem,String defaultSecondLeverItem,int firstLeverId,int[] secondLecerIds) {
		super(context,theme);
		this.mContext = context;
		this.itemIds=secondLecerIds;
		// TODO Auto-generated constructor stub
		LayoutInflater inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View placeView = inflater.inflate(R.layout.place_choose, null);
		//初始化省市
		selectedContent = (TextView) placeView.findViewById(R.id.place);
		setView(placeView);
		province = context.getResources().getStringArray(firstLeverId);
		itemArray=new String[province.length][secondLecerIds.length];
		for(int i= 0,n= province.length;i<n;i++){
			itemArray[i]=context.getResources().getStringArray(itemIds[i]);
		}
		firstLeverWV = (WheelView) placeView.findViewById(R.id.place_province);
		secondLeverWV = (WheelView)placeView.findViewById(R.id.place_city);
		
		setPlace(defaultFirstLeverItem,defaultSecondLeverItem);
//		place.setText(province[0]+" "+city[0][0]);
//		firstLeverWV.setLabel("省/直辖市");
//		secondLeverWV.setLabel("市");
		firstLeverWV.addChangingListener(new OnWheelChangedListener() {
			
			@Override
			public void onChanged(WheelView arg0, int arg1, int arg2) {
				// TODO Auto-generated method stub
				secondLeverWV.setViewAdapter(new ArrayWheelAdapter<String>(mContext,itemArray[arg2]));
				selectedContent.setText(province[arg2]+" "+itemArray[arg2][0]);
			}
		});
		secondLeverWV.addChangingListener(new OnWheelChangedListener() {
			
			@Override
			public void onChanged(WheelView arg0, int arg1, int arg2) {
				// TODO Auto-generated method stub
				selectedContent.setText(province[firstLeverWV.getCurrentItem()]+" "+itemArray[firstLeverWV.getCurrentItem()][arg2]);
			}
		});
		
	}
	public void setPlace(String mypro,String mycity){
		int provinceIndex=0;
		int cityIndex=0;
		for(provinceIndex=0;provinceIndex<province.length && mypro != null;provinceIndex++){
			if(province[provinceIndex].equals(mypro))break;
		}
		if(provinceIndex >= province.length)provinceIndex=0;
		for(;cityIndex<itemArray[provinceIndex].length && mycity != null;cityIndex++){
			if(mycity.equals(itemArray[provinceIndex][cityIndex]))break;
		}
		if(cityIndex >= itemArray[provinceIndex].length)cityIndex=0;
		firstLeverWV.setViewAdapter(new ArrayWheelAdapter<String>(mContext,province));
		firstLeverWV.setCurrentItem(provinceIndex);
		secondLeverWV.setViewAdapter(new ArrayWheelAdapter<String>(mContext,itemArray[provinceIndex]));
		secondLeverWV.setCurrentItem(cityIndex);
	}
	public TextView getSelectedContent() {
		return selectedContent;
	}
	public void setSelectedContent(TextView selectedContent) {
		this.selectedContent = selectedContent;
	}
}
