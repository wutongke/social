package com.cpstudio.zhuojiaren;

import java.util.ArrayList;
import java.util.HashMap;

import com.cpstudio.zhuojiaren.R;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.SimpleAdapter;

public class ZenghuiTypeActivity extends Activity {

	public final static String[] ZENGHUI_TYPE = new String[] { "zxzh", "ldzx",
			"qywh", "scyx", "xcsj", "jxkp", "zzjz", "rczp", "cwgl", "xlcz",
			"fyrw", "khfw", "ppcz", "yctx", "zlgh" };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_zenghui_type);
		initClick();
		addGridView();
	}

	private void addGridView() {/*
		GridView gridview = (GridView) findViewById(R.id.gridViewZenghui);
		String[] zenghuitype = getResources().getStringArray(
				R.array.array_zenghui_type);
		ArrayList<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
		HashMap<String, Object> map1 = new HashMap<String, Object>();
		map1.put("grid_image", R.drawable.ico_zh_zxzh);
		map1.put("grid_text", zenghuitype[0]);
		list.add(map1);
		HashMap<String, Object> map2 = new HashMap<String, Object>();
		map2.put("grid_image", R.drawable.ico_zh_ldzx);
		map2.put("grid_text", zenghuitype[1]);
		list.add(map2);
		HashMap<String, Object> map3 = new HashMap<String, Object>();
		map3.put("grid_image", R.drawable.ico_zh_qywh);
		map3.put("grid_text", zenghuitype[2]);
		list.add(map3);
		HashMap<String, Object> map4 = new HashMap<String, Object>();
		map4.put("grid_image", R.drawable.ico_zh_scyx);
		map4.put("grid_text", zenghuitype[3]);
		list.add(map4);
		HashMap<String, Object> map5 = new HashMap<String, Object>();
		map5.put("grid_image", R.drawable.ico_zh_xcsj);
		map5.put("grid_text", zenghuitype[4]);
		list.add(map5);
		HashMap<String, Object> map6 = new HashMap<String, Object>();
		map6.put("grid_image", R.drawable.ico_zh_jxkp);
		map6.put("grid_text", zenghuitype[5]);
		list.add(map6);
		HashMap<String, Object> map7 = new HashMap<String, Object>();
		map7.put("grid_image", R.drawable.ico_zh_zzjz);
		map7.put("grid_text", zenghuitype[6]);
		list.add(map7);
		HashMap<String, Object> map8 = new HashMap<String, Object>();
		map8.put("grid_image", R.drawable.ico_zh_rczp);
		map8.put("grid_text", zenghuitype[7]);
		list.add(map8);
		HashMap<String, Object> map9 = new HashMap<String, Object>();
		map9.put("grid_image", R.drawable.ico_zh_cwgl);
		map9.put("grid_text", zenghuitype[8]);
		list.add(map9);
		HashMap<String, Object> map10 = new HashMap<String, Object>();
		map10.put("grid_image", R.drawable.ico_zh_xlcz);
		map10.put("grid_text", zenghuitype[9]);
		list.add(map10);
		HashMap<String, Object> map11 = new HashMap<String, Object>();
		map11.put("grid_image", R.drawable.ico_zh_fyrw);
		map11.put("grid_text", zenghuitype[10]);
		list.add(map11);
		HashMap<String, Object> map12 = new HashMap<String, Object>();
		map12.put("grid_image", R.drawable.ico_zh_khfw);
		map12.put("grid_text", zenghuitype[11]);
		list.add(map12);
		HashMap<String, Object> map13 = new HashMap<String, Object>();
		map13.put("grid_image", R.drawable.ico_zh_ppcz);
		map13.put("grid_text", zenghuitype[12]);
		list.add(map13);
		HashMap<String, Object> map14 = new HashMap<String, Object>();
		map14.put("grid_image", R.drawable.ico_zh_yctx);
		map14.put("grid_text", zenghuitype[13]);
		list.add(map14);
		HashMap<String, Object> map15 = new HashMap<String, Object>();
		map15.put("grid_image", R.drawable.ico_zh_zlgh);
		map15.put("grid_text", zenghuitype[14]);
		list.add(map15);
		SimpleAdapter adapter = new SimpleAdapter(this, list,
				R.layout.item_grid, new String[] { "grid_image", "grid_text" },
				new int[] { R.id.grid_image, R.id.grid_text });
		gridview.setAdapter(adapter);
		gridview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Intent i = new Intent(ZenghuiTypeActivity.this,
						ZenghuiListActivity.class);

				i.putExtra("type", ZENGHUI_TYPE[arg2]);
				startActivity(i);
			}
		});
	*/}

	private void initClick() {

		findViewById(R.id.buttonBack).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				ZenghuiTypeActivity.this.finish();
			}
		});
	}
	
}
