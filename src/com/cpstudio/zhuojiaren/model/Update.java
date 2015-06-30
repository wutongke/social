package com.cpstudio.zhuojiaren.model;

import java.io.IOException;
import java.io.Serializable;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 应用程序更新实体类
 * @author liux (http://my.oschina.net/liux)
 * @version 1.0
 * @created 2012-3-21
 */
public class Update implements Serializable{
	
	public final static String UTF8 = "UTF-8";
	public final static String NODE_ROOT = "oschina";
	
	public int code;
	private int versionCode;
	private String versionName;
	private String downloadUrl;
	private String updateLog;
	
	public int getVersionCode() {
		return versionCode;
	}
	public void setVersionCode(int versionCode) {
		this.versionCode = versionCode;
	}
	public String getVersionName() {
		return versionName;
	}
	public void setVersionName(String versionName) {
		this.versionName = versionName;
	}
	public String getDownloadUrl() {
		return downloadUrl;
	}
	public void setDownloadUrl(String downloadUrl) {
		this.downloadUrl = downloadUrl;
	}
	public String getUpdateLog() {
		return updateLog;
	}
	public void setUpdateLog(String updateLog) {
		this.updateLog = updateLog;
	}
	
	public static Update parse(String jsonStr) throws IOException {
		Update update = new Update();
		JSONObject obj = null;
		//codecode 0 ：有更新
//		code 1： 当前为最新
//		code 2： 参数错误。
		//code -1 解析错误
		try {
			obj = new JSONObject(jsonStr);
			update.code = obj.getInt("code");
			if(update.code==0){
				update.setDownloadUrl(obj.getString("apk_url"));
				update.setVersionCode(obj.getInt("version_code"));
				update.setVersionName(obj.getString("version_name"));
			}
			
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			update.code=-1;
			return update;
		}
        
        return update;       
	}
}
