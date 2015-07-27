package com.cpstudio.zhuojiaren.helper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.cpstudio.zhuojiaren.LoginActivity;
import com.cpstudio.zhuojiaren.model.LoginRes;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.AvoidXfermode.Mode;

public class AppClientLef {
	private String userId;
	private String password;
	String session;
	String uploadFileToken;
	String imToken;
	private static AppClientLef instance;

	public static AppClientLef getInstance(Context context) {
		if (null == instance) {
			instance = new AppClientLef();
		}
		if (instance.password == null || instance.password.equals("")) {
			instance.init(context);
		}
		return instance;
	}

	private void init(Context context) {
		ResHelper resHelper = ResHelper.getInstance(context);
		this.userId = resHelper.getUserid();
		this.password = resHelper.getPassword();
		this.session = resHelper.getSessionForAPP();
		this.imToken = resHelper.getImTokenForRongyun();
		this.uploadFileToken = resHelper.getUpLoadTokenForQiniu();
	}
	/**
	 * 刷新session
	 * @param context
	 */
	public void refreshSession(final Context context) {
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		AsyncConnectHelperLZ conn = new AsyncConnectHelperLZ(
				addUserInfo(nameValuePairs), ZhuoCommHelper.getUrlLogin(),
				new AsyncConnectHelperLZ.FinishCallback() {

					@Override
					public boolean onReturn(String rs, int responseCode) {
						// TODO Auto-generated method stub
						if (JsonHandler
								.checkResult(rs, context.getApplicationContext())) {
							// 获取session
							LoginRes res = JsonHandler_Lef.parseLoginRes(
									context, JsonHandler
											.parseResult(rs).getData());
							SharedPreferences sp = context
									.getSharedPreferences("cpzhuojiaren",
											Activity.MODE_PRIVATE);
							SharedPreferences.Editor editor = sp.edit();
							editor.putString(ResHelper.SESSION, res.getSession());
							editor.putString(ResHelper.UPLIOAD_TOKEN,
									res.getQiniuToken());
							editor.putString(ResHelper.IM_TOKEN, res.getRongyunToken());
							editor.commit();
							
							// 保存到两个单例中
							ResHelper mResHelper = ResHelper.getInstance(context);
							mResHelper.setSessionForAPP(res.getSession());
							mResHelper.setUpLoadTokenForQiniu(res
									.getQiniuToken());
							mResHelper.setImTokenForRongyun(res
									.getRongyunToken());
							ZhuoConnHelper connHelper = ZhuoConnHelper.getInstance(context);
							connHelper.setSession(res.getSession());
							connHelper.setUploadFileToken(res.getQiniuToken());
							connHelper.setImToken(res.getRongyunToken());
						}
						return false;
					}
				}, null);
		conn.execute();
	}

	private List<NameValuePair> addUserInfo(List<NameValuePair> nameValuePairs) {
		nameValuePairs.add(new BasicNameValuePair("username", userId));
		nameValuePairs.add(new BasicNameValuePair("password", password));
		return nameValuePairs;
	}
}
