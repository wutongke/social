package com.cpstudio.zhuojiaren.widget;

import java.lang.reflect.Type;
import java.util.ArrayList;

import kankan.wheel.widget.OnWheelChangedListener;
import kankan.wheel.widget.WheelView;
import kankan.wheel.widget.adapters.ArrayWheelAdapter;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.cpstudio.zhuojiaren.R;
import com.cpstudio.zhuojiaren.helper.ConnHelper;
import com.cpstudio.zhuojiaren.helper.JsonHandler;
import com.cpstudio.zhuojiaren.model.City;
import com.cpstudio.zhuojiaren.model.Province;
import com.cpstudio.zhuojiaren.model.ResultVO;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class PlaceChooseDialog extends AlertDialog{

	private Context mContext;
	private WheelView provinceWV = null;
	private WheelView cityWV = null;
	private TextView place;
	
	
	private String[]province;
	private String[][]city=new String[50][50];
	private int[] citys = {R.array.beijin_province_item, R.array.tianjin_province_item, R.array.heibei_province_item, R.array.shanxi1_province_item, R.array.neimenggu_province_item, R.array.liaoning_province_item, R.array.jilin_province_item, R.array.heilongjiang_province_item, R.array.shanghai_province_item, R.array.jiangsu_province_item, R.array.zhejiang_province_item, R.array.anhui_province_item, R.array.fujian_province_item, R.array.jiangxi_province_item, R.array.shandong_province_item, R.array.henan_province_item, R.array.hubei_province_item, R.array.hunan_province_item, R.array.guangdong_province_item,  R.array.guangxi_province_item, R.array.hainan_province_item, R.array.chongqing_province_item, R.array.sichuan_province_item, R.array.guizhou_province_item, R.array.yunnan_province_item, R.array.xizang_province_item, R.array.shanxi2_province_item, R.array.gansu_province_item, R.array.qinghai_province_item, R.array.linxia_province_item, R.array.xinjiang_province_item, R.array.hongkong_province_item, R.array.aomen_province_item, R.array.taiwan_province_item};
	private ArrayList<City> cityList = new ArrayList<City>();
	private String chooseCity;
	
	public PlaceChooseDialog(Context context,int theme,String mypro,String mycity) {
		super(context,theme);
		this.mContext = context;
		//下载城市数据，然后获取城市编码
		loadCity();
		// TODO Auto-generated constructor stub
		LayoutInflater inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View placeView = inflater.inflate(R.layout.place_choose, null);
		//初始化省市
		place = (TextView) placeView.findViewById(R.id.place);
		setView(placeView);
		province = context.getResources().getStringArray(R.array.province_item);
		for(int i= 0,n= province.length;i<n;i++){
			city[i]=context.getResources().getStringArray(citys[i]);
		}
		provinceWV = (WheelView) placeView.findViewById(R.id.place_province);
		cityWV = (WheelView)placeView.findViewById(R.id.place_city);
		place.setText(mypro+" "+mycity);
		chooseCity = mycity;
		setPlace(mypro,mycity);
		provinceWV.addChangingListener(new OnWheelChangedListener() {
			
			@Override
			public void onChanged(WheelView arg0, int arg1, int arg2) {
				// TODO Auto-generated method stub
				cityWV.setViewAdapter(new ArrayWheelAdapter<String>(mContext,city[arg2]));
				place.setText(province[arg2]+" "+city[arg2][0]);
				chooseCity = city[arg2][0];
			}
		});
		cityWV.addChangingListener(new OnWheelChangedListener() {
			
			@Override
			public void onChanged(WheelView arg0, int arg1, int arg2) {
				// TODO Auto-generated method stub
				place.setText(province[provinceWV.getCurrentItem()]+" "+city[provinceWV.getCurrentItem()][arg2]);
				chooseCity = city[provinceWV.getCurrentItem()][arg2];
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
		for(;cityIndex<city[provinceIndex].length && mycity != null;cityIndex++){
			if(mycity.equals(city[provinceIndex][cityIndex]))break;
		}
		if(cityIndex >= city[provinceIndex].length)cityIndex=0;
		provinceWV.setViewAdapter(new ArrayWheelAdapter<String>(mContext,province));
		provinceWV.setCurrentItem(provinceIndex);
		cityWV.setViewAdapter(new ArrayWheelAdapter<String>(mContext,city[provinceIndex]));
		cityWV.setCurrentItem(cityIndex);
	}
	
	public TextView getPlace() {
		return place;
	}


	public void setPlace(TextView place) {
		this.place = place;
	}
	private void loadCity() {
		// TODO Auto-generated method stub
		final ConnHelper load = ConnHelper.getInstance(mContext);
		load.getCitys(new Handler(){
			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				super.handleMessage(msg);
				ResultVO res;
				if (JsonHandler.checkResult((String) msg.obj, mContext)) {
					res = JsonHandler.parseResult((String) msg.obj);
					load.saveObject((String) msg.obj, "citys");
				} else {
					return;
				}
				String data = res.getData();
				Type listType = new TypeToken<ArrayList<Province>>() {
				}.getType();
				Gson gson = new Gson();
				ArrayList<Province> list = gson.fromJson(data, listType);
				
				for(Province temp:list){
					cityList.addAll(temp.getCitys());
				}
			}
		}, 1, (Activity)mContext, true, null, null);
	}
	public int getCityCode() {
		// TODO Auto-generated method stub
		for(City temp:cityList){
			if(temp!=null && temp.getCityName().contains(chooseCity))
				return Integer.parseInt(temp.getCityId());
		}
		return 1;
	}
}
