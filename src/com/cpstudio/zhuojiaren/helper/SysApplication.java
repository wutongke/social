package com.cpstudio.zhuojiaren.helper;

import java.util.LinkedList;
import java.util.List;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
/***
 * activityջ
 * @author lef
 *
 */
public class SysApplication extends Application{
	private List<Activity> activities = new LinkedList<Activity>();
	private static SysApplication instance;
	private SysApplication(){
		
	}
	
	public static SysApplication getInstance() {
		if(null == instance){
			instance = new SysApplication();
		}
		return instance;
	}
	
	public void addActivity(Activity activity) {
		activities.add(activity);
	}
	
	public void exit(boolean full,Context context){
		try{
			for (Activity activity : activities) {
				if (activity != null)
					activity.finish();
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(full){
				System.exit(0);
			}
		}
	}
}
