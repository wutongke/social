package com.cpstudio.zhuojiaren.helper;


public class ZhuoCommHelperLz {

	private final static String SERVER_PREFIX = "http://";
	private final static String SERVER_IP = "115.28.167.196";// "115.29.145.63";//
																// "zjr.ci77.com";//

	private final static String SERVER_PORT = ":9001";
	private final static String SERVER_APP = "/zhuo-api";
	public final static String SERVER = SERVER_PREFIX + SERVER_IP + SERVER_PORT
			+ SERVER_APP;

	private final static String SERVICE_MAIN_IDS = "/getportalinfo.do";

	/**
	 * 获取主页界面的广告信息
	 * 
	 * @return
	 */
	public static String getMainAdInfo() {
		return SERVER + SERVICE_MAIN_IDS;
	}

}