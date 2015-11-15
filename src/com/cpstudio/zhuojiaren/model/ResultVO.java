package com.cpstudio.zhuojiaren.model;

public class ResultVO {
	/*# 0-99 系统信息
	0=成功
	1=失败
	2=超时
	3=数据库错误
	4=缓存错误
	5=请求格式不正确
	6=权限不足

	# 100-200 账号业务
	100=手机号格式不正确
	101=密码格式不正确,请输入6-20位密码
	102=名字为空
	103=用户名格式不正确
	104=密码错误
	105=用户已存在
	106=用户不存在
	107=session失效
	200=圈子不存在*/
	public static final int Success = 0;
	public static final int FAILURE = 1;
	public static final int OUTOFTIME = 2;
	public static final int DATABASEERROR = 3;
	public static final int CASHERROR = 4;
	public static final int REQUESFORMATERROR = 5;
	public static final int NOAUTHORITY = 6;
	public static final int PHONENUMBERFORMATERROR = 100;
	public static final int PASSWORDFORMATERROR = 101;
	public static final int NONAME = 102;
	public static final int PASSWORDERROR = 104;
	public static final int NOPHONENUMBER = 106;
	public static final int PHONENUMBER = 105;
	public static final int SESSIONOUT = 107;
	private String code;
	private String msg="";
	private String data;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}
}
