package com.cpstudio.zhuojiaren.helper;

import org.json.JSONException;
import org.json.JSONObject;

import com.cpstudio.zhuojiaren.model.LoginRes;

import android.content.Context;
import android.util.Log;

public class JsonHandler_Lef {

	/**
	 * 登录时获取session
	 */
	public static LoginRes parseLoginRes(Context context,String data){
		if (data==null){
			Log.w("Debug", "登录没有返回session");
		}
		try {
			JSONObject obj = new JSONObject(data);
			String session = obj.optString("session");
			if(session ==null||session.isEmpty()){
				Log.w("Debug", "session为空");
			}
			String qiniuToken = obj.optString("qiniuToken");
			if(qiniuToken ==null||qiniuToken.isEmpty()){
				Log.w("Debug", "qiniuToken为空");
			}
			String rongyunToken = obj.optString("rongyunToken");
			if(rongyunToken ==null||rongyunToken.isEmpty()){
				Log.w("Debug", "rongyunToken为空");
			}
			String userId = obj.optString("userid");
			//保存信息
			LoginRes res = new LoginRes();
			res.setQiniuToken(qiniuToken);
			res.setRongyunToken(rongyunToken);
			res.setUserid(userId);
			res.setSession(session);
			//如果是session过期导致的重新登录，则在这里处理
			return res;
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.w("Debug", "登录信息解析失败");
			return null;
		}
	}
}
