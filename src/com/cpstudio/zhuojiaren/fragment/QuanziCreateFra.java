package com.cpstudio.zhuojiaren.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.cpstudio.zhuojiaren.LoginActivity;
import com.cpstudio.zhuojiaren.QuanCreateActivity;

public class QuanziCreateFra extends Fragment{

	
	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		// TODO Auto-generated method stub
		super.setUserVisibleHint(isVisibleToUser);
		if(isVisibleToUser){
			startActivity(new Intent(getActivity(),QuanCreateActivity.class));
		}
	}
}
