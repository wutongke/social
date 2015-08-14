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
	private final static String DYNAMIC_PRAISE = "/statusFamilyPraise.do";

	private final static String GTOPIC_COMMENT = "/grouptopiccmt.do";
	private final static String DYNAMIC_COMMENT = "/statusFamilyCmt.do";
	private final static String GET_BASECODE_DATA = "/getbasiccode.do";
	private final static String MODIFY_USER_INFO = "/modifyuserinfo.do";
	private final static String SET_UHEAD = "/setuheader.do";

	private final static String GET_FAMILY_STATUS_DETAIL = "/getStatusFamily.do";

	private final static String GET_LIST_FAMILY_STATUS = "/statusFamilyList.do";
	private final static String PUB_STATUS = "/addStatusFamily.do";
	private final static String DELETE_FAMILY_STATUS = "/deleteStatusFamily.do";

	private final static String GROUP_MEMBER_LIST = "/groupMemberList.do";
	private final static String COLLECT_STATUS = "/collectStatusFamily.do";
	private final static String COLLECT_TOPIC = "/collectTopic.do";
	private final static String FOLLOW_USER = "/followUser.do";
	private final static String STATUS_GROUP = "/getStatusGroup.do";
	private final static String FOLLOEW_REQ_LIST = "/getFollowReq.do";
	private final static String ZAN_CARD = "/praiseStatusCard.do";
	private final static String GG_DETAIL = "/getpubdetail.do";
	private final static String PUB_LIST = "/getPubList.do";
	private final static String DEL_ACTIVE = "/deleteActivity.do";
	private final static String GET_MY_STATUS_CARD = "/getMyStatusCard.do";
	/**
	 * �û�����Ļ򴴽��Ļ
	 */
	private final static String USER_ACTIVITY = "/getUserActivityList.do";

	//lef��
	private final static String SERVICE_MYGROUPLIST = "/getmygroup.do";
	
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

	public static String dynamicPraise() {
		return SERVER + DYNAMIC_PRAISE;
	}

	/**
	 * Ȧ��������
	 * 
	 * @return
	 */
	public static String topicComment() {
		return SERVER + GTOPIC_COMMENT;
	}

	public static String dynamicComment() {
		return SERVER + DYNAMIC_COMMENT;
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
	 * ��ȡ������������(����������)
	 * 
	 * @return
	 */
	public static String getBaseCodeData() {
		return SERVER + GET_BASECODE_DATA;
	}

	/**
	 * δ�� ��ù����Ϣ
	 * 
	 * @return
	 */
	public static String getAdInfo() {
		return SERVER + SERVICE_GET_ADINFO;
	}

	/**
	 * �޸��û���Ϣ
	 * 
	 * @return
	 */
	public static String modifyUserInfo() {
		return SERVER + MODIFY_USER_INFO;
	}

	/**
	 * �����û�ͷ��
	 * 
	 * @return
	 */
	public static String setUserHeadImage() {
		return SERVER + SET_UHEAD;
	}

	/**
	 * ��ö�̬��ϸ��Ϣ
	 * 
	 * @return
	 */
	public static String getDetailDynamic() {
		return SERVER + GET_FAMILY_STATUS_DETAIL;
	}

	/**
	 * ��ö�̬�б�
	 * 
	 * @return
	 */
	public static String getDynamicList() {
		return SERVER + GET_LIST_FAMILY_STATUS;
	}

	/**
	 * ������̬
	 * 
	 * @return
	 */
	public static String pubDynamic() {
		return SERVER + PUB_STATUS;
	}

	/**
	 * ɾ����̬
	 * 
	 * @return
	 */
	public static String deleteDynamic() {
		return SERVER + DELETE_FAMILY_STATUS;
	}

	/**
	 * ��ȡȦ�ӳ�Ա�б�
	 * 
	 * @return
	 */
	public static String getGroupMemberList() {
		return SERVER + GROUP_MEMBER_LIST;
	}

	/**
	 * ��ȡ�û�����Ļ򴴽��Ļ
	 */
	public static String getUserEvent() {
		return SERVER + USER_ACTIVITY;
	}

	public static String getMainInfo() {
		return SERVER + SERVICE_MAIN_IDS;
	}

	/**
	 * ��̬�ղ�
	 * 
	 * @return
	 */
	public static String collectStatusFamily() {
		return SERVER + COLLECT_STATUS;
	}

	/**
	 * Ȧ�����ղ�
	 * 
	 * @return
	 */
	public static String collectTopic() {
		return SERVER + COLLECT_TOPIC;
	}

	/**
	 * ���ѹ�ϵ����
	 * 
	 * @return
	 */
	public static String followUser() {
		return SERVER + FOLLOW_USER;
	}

	/**
	 * ��ȡȦ�Ӷ�̬
	 * 
	 * @return
	 */
	public static String groupStatus() {
		return SERVER + STATUS_GROUP;
	}

	/**
	 * ��ȡ������Ӻ��ѵ��û��б�
	 * 
	 * @return
	 */
	public static String getFollowReqList() {
		return SERVER + FOLLOEW_REQ_LIST;
	}

	/**
	 * ��Ƭ����
	 * 
	 * @return
	 */
	public static String zanCard() {
		return SERVER + ZAN_CARD;
	}
	/**
	 * ��������
	 * @return
	 */
	public static String gonggaoDetail() {
		return SERVER + GG_DETAIL;
	}
	/**
	 * پ����̬�б�
	 * @return
	 */
	public static String getPutList() {
		return SERVER + PUB_LIST;
	}
	/**
	 * ɾ���
	 * @return
	 */
	public static String deleteActives() {
		return SERVER + DEL_ACTIVE;
	}
	public static String getMyStatusCard() {
		return SERVER + GET_MY_STATUS_CARD;
	}
	/**
	 * lef�ģ���ȡ�ҵ�Ȧ�ӣ������ṩ���Ƶ�Ȧ����Ϣ
	 * @return
	 */
	public static String getUrlMyGroupList() {
		return SERVER + SERVICE_MYGROUPLIST;
	}
}