package com.cpstudui.zhuojiaren.lz;

import io.rong.message.TextMessage;

import org.json.JSONException;
import org.json.JSONObject;

public class CustomerMessageFactory {
	
	private static CustomerMessageFactory instance;
	/**
	 * 评论消息 11
	 */
	public final static int CMT=0; 
	/**
	 * 点赞消息 11
	 */
	public final static int ZAN=1; 
	/**
	 * 请求加入圈子消息11
	 */
	public final static int REQUEST_JOIN_QAUN=2; 
	/**
	 * 退出圈子消息
	 */
	public final static int REQUEST_QUIT_QAUN=3; 
	/**
	 * 解散圈子消息
	 */
	public final static int REQUEST_DISSOLVE_QAUN=4;
	/**
	 * 退出圈子消息
	 */
	public final static int SYS=5; 
	
	private CustomerMessageFactory()
	{
		
	}
	
	public synchronized static CustomerMessageFactory getInstance()
	{
		if(instance==null)
			instance=new CustomerMessageFactory();
		return instance;
	}
	
	public TextMessage getReqQuanMsg(String userid,String username,String groupid,String groupname)
	{
		JSONObject jobj=new JSONObject();
		try {
			jobj.put("userid", userid);
			jobj.put("username", username);
			jobj.put("groupid", groupid);
			jobj.put("groupname", groupname);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		TextMessage content = TextMessage.obtain(jobj.toString());
		content.setExtra(String.valueOf(REQUEST_JOIN_QAUN));
		return content;
	}
	
	
	
}
