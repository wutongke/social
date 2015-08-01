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

	private final static String SERVICE_QUAN_TOPIC_LIST = "/getgrouptopiclist.do";

	private final static String SERVICE_GET_USER_INFO = "/userinfo.do";

	private final static String SERVICE_GET_UPLOADTOKEN = "/qiniu/getUploadToken.do";
	
	private final static String SERVICE_PUB_GROUP_TOPIC = "/addgrouptopic.do";
	
	private final static String SERVICE_GET_QUANEVENTLIST = "/groupActivityList.do";
	
	private final static String SERVICE_GET_QUANINFO = "/getgroupinfo.do";
	
	//δ��
	private final static String SERVICE_GET_ADINFO = "/getadinfo.do";
	
	/**
	 * ��ȡ��ҳ����Ĺ����Ϣ
	 * 
	 * @return
	 */
	public static String getMainAdInfo() {
		return SERVER  + SERVICE_MAIN_IDS;
	}

	/**
	 * ��ȡȦ�ӻ����б�����
	 * 
	 * @return
	 */
	public static String getQuanTopicList() {
		return SERVER + SERVICE_QUAN_TOPIC_LIST;
	}

	/**
	 * ����û���Ϣ
	 * 
	 * @return
	 */
	public static String getUserInfo() {
		return SERVER +  SERVICE_GET_USER_INFO;
	}

	/**
	 * ����ϴ��ļ���token
	 * 
	 * @return
	 */
	public static String getUploadToken() {
		return SERVER +  SERVICE_GET_UPLOADTOKEN;
	}

	
	/**
	 * ����Ȧ����
	 * 
	 * @return
	 */
	public static String pubQuanTopic() {
		return SERVER +  SERVICE_PUB_GROUP_TOPIC;
	}
	
	public static String getQuanEventList() {
		return SERVER +  SERVICE_GET_QUANEVENTLIST;
	}
	
	public static String getQuanInfo() {
		return SERVER +  SERVICE_GET_QUANINFO;
	}
	
	
	
	
	
	/**δ��
	 * ��ù����Ϣ
	 * 
	 * @return
	 */
	public static String getAdInfo() {
		return SERVER +  SERVICE_GET_ADINFO;
	}
}