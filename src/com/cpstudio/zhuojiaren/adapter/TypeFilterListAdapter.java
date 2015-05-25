package com.cpstudio.zhuojiaren.adapter;

import java.util.ArrayList;
import java.util.List;

import com.cpstudio.zhuojiaren.R;
import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class TypeFilterListAdapter extends BaseAdapter {
	private List<String> mStringList = null;
	private SparseArray<View> rowViews = new SparseArray<View>();
	private LayoutInflater inflater = null;
	private String mSelect = null;
	private View mSelectView = null;

	public TypeFilterListAdapter(Context context, ArrayList<String> stringList,String select) {
		this.mStringList = stringList;
		this.mSelect = select;
		this.inflater = LayoutInflater.from(context);
	}
	
	public View getmSelectView() {
		return mSelectView;
	}

	public void setmSelectView(View mSelectView) {
		mSelectView.findViewById(R.id.imageViewTick).setVisibility(View.VISIBLE);
		this.mSelectView = mSelectView;
	}

	@Override
	public int getCount() {
		return mStringList.size();
	}

	@Override
	public Object getItem(int arg0) {
		return mStringList.get(arg0);
	}

	@Override
	public void notifyDataSetChanged() {
		super.notifyDataSetChanged();
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View rowView = rowViews.get(position);
		if (null == rowView) {
			rowView = gentView(position);
			rowViews.put(position, rowView);
		}
		return rowView;
	}
	
	private View gentView(int position){
		View rowView = inflater.inflate(R.layout.pop_top_item, null);
		String str = mStringList.get(position);
		TextView tv = (TextView) rowView.findViewById(R.id.groupItem);
		tv.setText(str);
		if(mSelect != null){
			if(str.equals(mSelect)){
				rowView.findViewById(R.id.imageViewTick).setVisibility(View.VISIBLE);
				mSelectView = rowView;
			}else{
				rowView.findViewById(R.id.imageViewTick).setVisibility(View.GONE);
			}
		}
		return rowView;
	}
}
