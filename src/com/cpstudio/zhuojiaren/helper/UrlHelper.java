package com.cpstudio.zhuojiaren.helper;

public class UrlHelper {

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
	
	private final static String SERVICE_ADVICE = "/addFeedback.do";

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
	private final static String FOLLOEW_REQ_LIST = "/getFriendReq.do";
	private final static String ZAN_CARD = "/praiseStatusCard.do";
	private final static String GG_DETAIL = "/getpubdetail.do";
	private final static String PUB_LIST = "/getPubList.do";
	private final static String DEL_ACTIVE = "/deleteActivity.do";
	private final static String GET_MY_STATUS_CARD = "/getMyStatusCard.do";
	private final static String SET_PHOTO = "/setPhoto.do";

	 private final static String ALL_JIAREN = "/getJiaren.do";
	private final static String CITY_JIAREN = "/getJiarenByCity.do";
	private final static String HOBBY_JIAREN = "/getJiarenByHobby.do";
	//��������δ�ṩ
	private final static String NEAR_JIAREN = "/fujin.do";
	private final static String INDUSTRY_JIAREN = "/getJiarenByIndustry.do";
	private final static String TEATURE_JIAREN = "/getTeacher.do";

	//�˽ӿ�����FOLLOEW_REQ_LIST��
	private final static String GET_REQ_FRIEND = "/getFriendReq.do";
	
	
	
	private final static String ADD_PRODUCT = "/addProduct.do";
	private final static String ADD_COMPANY = "/addCompany.do";
	
	private final static String DELETE_PRODUCT = "/deleteProduct.do";
	private final static String DELETE_COMPANY = "/deleteCompany.do";
	
	private final static String UPDATE_PRODUCT = "/updateProduct.do";
	private final static String UPDATE_COMPANY = "/updateCompany.do";
	
	private final static String GET_PRODUCT = "/getComProduct.do";
	private final static String GET_COMPANY = "/getUserCompany.do";
	private final static String STATUS_CARD = "/viewStatusCard.do";
	
	private final static String GET_HOT_KEY = "/hotKeyword.do";
	private final static String PORTAL_SEARCH = "/portalSearch.do";
	private final static String FRIENDS_LIST = "/getFriend.do";
	
	private final static String QUAN_REQ_USER_LIST = "/groupFollowReqUser.do";
	
	private final static String MAKE_FRIENDS = "/makeFriend.do";
	private final static String MY_RENMAI = "/getFollowUser.do";
	private final static String CMT_GONGXU = "/supplyDemandCmt.do";
	private final static String ORDERS_LIST = "/goods/getOrderList.do";
	private final static String ORDER_DETAIL = "/goods/getOrder.do";
	private final static String CARD_BG = "/getBackground.do";
	private final static String SET_CARD_BG  = "/setUserBackground.do";
	private final static String QuanEventCollection  = "/getCollectActivity.do";
	private final static String gongList  = "/getCollectSupplyDemand.do";
	private final static String TopicList  = "/getCollectTopic.do";
	private final static String peopleList  = "/getFollowUser.do";
	private final static String grouthThought  = "/growthOnlineCmt.do";
	private final static String audioThought  = "/audioCmt.do";
	private final static String visitThought  = "/interviewCmt.do";
	private final static String gxtyles  = "/getSupplyDemandType.do";
	
	public static String getVisitthought() {
		return SERVER + visitThought;
	}

	public static String getAudiothought() {
		return SERVER + audioThought;
	}

	public static String getGrouththought() {
		return SERVER + grouthThought;
	}

	public static String getPeoplelist() {
		return SERVER + peopleList;
	}

	public static String getTopiclist() {
		return SERVER + TopicList;
	}

	public static String getGonglist() {
		return SERVER +  gongList;
	}

	public static String getQuaneventcollection() {
		return SERVER + QuanEventCollection;
	}

	private final static String SET_QUAN_LOG  = "/setgheader.do";
	public static String getAllJiaren() {
		return ALL_JIAREN;
	}

	public static String getNearJiaren() {
		return NEAR_JIAREN;
	}

	public static String getCityJiaren() {
		return CITY_JIAREN;
	}

	public static String getHobbyJiaren() {
		return HOBBY_JIAREN;
	}

	public static String getIndustryJiaren() {
		return INDUSTRY_JIAREN;
	}

	public static String getTeatureJiaren() {
		return TEATURE_JIAREN;
	}

	/**
	 * �û�����Ļ򴴽��Ļ
	 */
	private final static String USER_ACTIVITY = "/getUserActivityList.do";

	// lef��
	private final static String SERVICE_MYGROUPLIST = "/getmygroup.do";
	private final static String PUB_GONGXU = "/addSupplyDemand.do";
	private final static String USER_BUSINESS = "/getUserBusiness.do";

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
	 * �����������
	 * 
	 * @return
	 */
	public static String pubAdvice() {
		return SERVER + SERVICE_ADVICE;
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
	 * Ȧ��Ȩ�޹������˳�Ȧ�ӡ�����Ȧ�ӡ����ܼ���
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
	 * ��ȡ�������Ӻ��ѵ��û��б�
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
	 * 
	 * @return
	 */
	public static String gonggaoDetail() {
		return SERVER + GG_DETAIL;
	}

	/**
	 * پ����̬�б�
	 * 
	 * @return
	 */
	public static String getPutList() {
		return SERVER + PUB_LIST;
	}

	/**
	 * ɾ���
	 * 
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
	 * 
	 * @return
	 */
	public static String getUrlMyGroupList() {
		return SERVER + SERVICE_MYGROUPLIST;
	}

	/**
	 * ��������
	 * 
	 * @return
	 */
	public static String pubGongxu() {
		return SERVER + PUB_GONGXU;
	}

	/**
	 * �û�������Ϣ������
	 * 
	 * @return
	 */
	public static String getUserBusinessInfo() {
		return SERVER + USER_BUSINESS;
	}

	/**
	 * �ϴ��������ͼƬ
	 * 
	 * @return
	 */
	public static String pubPhoto() {
		return SERVER + SET_PHOTO;
	}
	/**
	 * ���󽻻���Ƭ�ļ���
	 * @return
	 */
	public static String getFriendReq() {
		return SERVER + GET_REQ_FRIEND;
	}
	
	//�ҵ���ҵ�еĹ�˾�����Ʒ����ɾ�Ĳ�
	
	public static String addCompany() {
		return SERVER + ADD_COMPANY;
	}
	public static String addProduct() {
		return SERVER + ADD_PRODUCT;
	}
	public static String deleteCompany() {
		return SERVER + DELETE_COMPANY;
	}
	public static String deleteProduct() {
		return SERVER + DELETE_PRODUCT;
	}
	
	public static String updateProduct() {
		return SERVER + UPDATE_PRODUCT;
	}
	
	public static String updateCompany() {
		return SERVER + UPDATE_COMPANY;
	}
	public static String getProduct() {
		return SERVER + GET_PRODUCT;
	}
	
	public static String getCompany() {
		return SERVER + GET_COMPANY;
	}
	/**
	 * پ����̬������(�������)
	 * @return
	 */
	public static String getZMDT() {
		return SERVER + STATUS_CARD;
	}
	public static String getHotKey() {
		return SERVER + GET_HOT_KEY;
	}
	public static String getPortalSearch() {
		return SERVER + PORTAL_SEARCH;
	}
	public static String getMyFriends() {
		return SERVER + FRIENDS_LIST;
	}
	/**
	 * �������Ȧ�ӵ���
	 * @return
	 */
	public static String getReqQuanUsers() {
		return SERVER + QUAN_REQ_USER_LIST;
	}
	/**
	 * ����ѹ�ϵ���
	 * @return
	 */
	public static String makeFriends() {
		return SERVER + MAKE_FRIENDS;
	}
	/**
	 * �ҵ�����
	 * @return
	 */
	public static String myRenmai() {
		return SERVER + MY_RENMAI;
	}
	/**
	 * ���ۺͻظ�����
	 * @return
	 */
	public static String cmtGX() {
		return SERVER + CMT_GONGXU;
	}
	/**
	 * �����б�
	 * @return
	 */
	public static String orderList() {
		return SERVER + ORDERS_LIST;
	}
	/**
	 * ��������
	 * @return
	 */
	public static String orderDetail() {
		return SERVER + ORDER_DETAIL;
	}
	/**
	 * ��Ƭ����ͼƬ
	 * @return
	 */
	public static String cardBg() {
		return SERVER + CARD_BG;
	}
	public static String setCardBg() {
		return SERVER + SET_CARD_BG;
	}
	public static String setQuanLogo() {
		return SERVER + SET_QUAN_LOG;
	}
	public static String getGXTypes() {
		return SERVER + gxtyles;
	}
	
}