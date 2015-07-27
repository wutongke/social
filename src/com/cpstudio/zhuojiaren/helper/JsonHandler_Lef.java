package com.cpstudio.zhuojiaren.helper;

import org.json.JSONException;
import org.json.JSONObject;

import com.cpstudio.zhuojiaren.model.LoginRes;

import android.content.Context;
import android.util.Log;

public class JsonHandler_Lef {

	/**
	 * ��¼ʱ��ȡsession
	 */
	public static LoginRes parseLoginRes(Context context,String data){
		if (data==null){
			Log.w("Debug", "��¼û�з���session");
		}
		try {
			JSONObject obj = new JSONObject(data);
			String session = obj.optString("session");
			if(session ==null||session.isEmpty()){
				Log.w("Debug", "sessionΪ��");
			}
			String qiniuToken = obj.optString("qiniuToken");
			if(qiniuToken ==null||qiniuToken.isEmpty()){
				Log.w("Debug", "qiniuTokenΪ��");
			}
			String rongyunToken = obj.optString("rongyunToken");
			if(rongyunToken ==null||rongyunToken.isEmpty()){
				Log.w("Debug", "rongyunTokenΪ��");
			}
			String userId = obj.optString("userid");
			//������Ϣ
			LoginRes res = new LoginRes();
			res.setQiniuToken(qiniuToken);
			res.setRongyunToken(rongyunToken);
			res.setUserid(userId);
			res.setSession(session);
			//�����session���ڵ��µ����µ�¼���������ﴦ��
			return res;
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.w("Debug", "��¼��Ϣ����ʧ��");
			return null;
		}
	}
}
