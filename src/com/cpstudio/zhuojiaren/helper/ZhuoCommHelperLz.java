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

	private final static String SERVICE_QUAN_PERMIT = "/followgroup.do";

	private final static String SERVICE_MODIFY_GROUP_INFO = "/modifygroupinfo.do";

	private final static String SERVICE_GET_TOPIC_DETAIL = "/getgrouptopicdetail.do";

	private final static String GTOPIC_PRAISE = "/grouptopicpraise.do";
	private final static String GTOPIC_COMMENT = "/grouptopiccmt.do";

	// δ��
	private final static String SERVICE_GET_ADINFO = "/getadinfo.do";

	/**
	 * ��ȡ��ҳ����Ĺ����Ϣ
	 * 
	 * @return
	 */
	public static String getMainAdInfo() {
		return SERVER + SERVICE_MAIN_IDS;
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
		return SERVER + SERVICE_GET_USER_INFO;
	}

	/**
	 * ����ϴ��ļ���token
	 * 
	 * @return
	 */
	public static String getUploadToken() {
		return SERVER + SERVICE_GET_UPLOADTOKEN;
	}

	/**
	 * ����Ȧ����
	 * 
	 * @return
	 */
	public static String pubQuanTopic() {
		return SERVER + SERVICE_PUB_GROUP_TOPIC;
	}

	/**
	 * ���Ȧ��б�
	 * 
	 * @return
	 */
	public static String getQuanEventList() {
		return SERVER + SERVICE_GET_QUANEVENTLIST;
	}

	/**
	 * ���Ȧ����Ҫ��Ϣ
	 * 
	 * @return
	 */
	public static String getQuanInfo() {
		return SERVER + SERVICE_GET_QUANINFO;
	}

	/**
	 * Ȧ��Ȩ�޹����˳�Ȧ�ӡ�����Ȧ�ӡ����ܼ���
	 * 
	 * @return
	 */
	public static String manageQuanPermit() {
		return SERVER + SERVICE_QUAN_PERMIT;
	}

	/**
	 * �޸�Ȧ����Ϣ
	 * 
	 * @return
	 */
	public static String modifyGroupInfo() {
		return SERVER + SERVICE_MODIFY_GROUP_INFO;
	}

	/**
	 * ��ȡȦ�ӵĵ��޺�������Ϣ
	 * 
	 * @return
	 */
	public static String getTopicDetail() {
		return SERVER + SERVICE_GET_TOPIC_DETAIL;
	}

	/**
	 * Ȧ�������
	 * 
	 * @return
	 */
	public static String topicPraise() {
		return SERVER + GTOPIC_PRAISE;
	}
	/**
	 * Ȧ��������
	 * @return
	 */
	public static String topicComment() {
		return SERVER + GTOPIC_COMMENT;
	}
	
	/**
	 * δ�� ��ù����Ϣ
	 * 
	 * @return
	 */
	public static String getAdInfo() {
		return SERVER + SERVICE_GET_ADINFO;
	}
}