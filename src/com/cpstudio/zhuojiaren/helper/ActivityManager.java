package com.cpstudio.zhuojiaren.helper;

import java.util.LinkedList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
/***
 * activityջ
 * @author lef
 *
 */
public class ActivityManager {
	private List<Activity> activities = new LinkedList<Activity>();
	private static ActivityManager instance;
	private ActivityManager(){
		
	}
	
	public static ActivityManager getInstance() {
		if(null == instance){
			instance = new ActivityManager();
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
