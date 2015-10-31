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
	//服务器暂未提供
	private final static String NEAR_JIAREN = "/fujin.do";
	private final static String INDUSTRY_JIAREN = "/getJiarenByIndustry.do";
	private final static String TEATURE_JIAREN = "/getTeacher.do";

	//此接口已在FOLLOEW_REQ_LIST中
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
	 * 用户加入的或创建的活动
	 */
	private final static String USER_ACTIVITY = "/getUserActivityList.do";

	// lef的
	private final static String SERVICE_MYGROUPLIST = "/getmygroup.do";
	private final static String PUB_GONGXU = "/addSupplyDemand.do";
	private final static String USER_BUSINESS = "/getUserBusiness.do";

	// 未用
	private final static String SERVICE_GET_ADINFO = "/getadinfo.do";

	/**
	 * 获取主页界面的广告信息
	 * 
	 * @return
	 */
	public static String getMainAdInfo() {
		return SERVER + SERVICE_MAIN_IDS;
	}

	/**
	 * 获取圈子话题列表数据
	 * 
	 * @return
	 */
	public static String getQuanTopicList() {
		return SERVER + SERVICE_QUAN_TOPIC_LIST;
	}

	/**
	 * 获得上传文件的token
	 * 
	 * @return
	 */
	public static String getUploadToken() {
		return SERVER + SERVICE_GET_UPLOADTOKEN;
	}

	/**
	 * 发布圈主题
	 * 
	 * @return
	 */
	public static String pubQuanTopic() {
		return SERVER + SERVICE_PUB_GROUP_TOPIC;
	}
	
	/**
	 * 发布反馈意见
	 * 
	 * @return
	 */
	public static String pubAdvice() {
		return SERVER + SERVICE_ADVICE;
	}

	/**
	 * 获得圈活动列表
	 * 
	 * @return
	 */
	public static String getQuanEventList() {
		return SERVER + SERVICE_GET_QUANEVENTLIST;
	}

	/**
	 * 获得圈子主要信息
	 * 
	 * @return
	 */
	public static String getQuanInfo() {
		return SERVER + SERVICE_GET_QUANINFO;
	}

	/**
	 * 圈子权限管理：退出圈子、加入圈子、接受加入
	 * 
	 * @return
	 */
	public static String manageQuanPermit() {
		return SERVER + SERVICE_QUAN_PERMIT;
	}

	/**
	 * 修改圈子信息
	 * 
	 * @return
	 */
	public static String modifyGroupInfo() {
		return SERVER + SERVICE_MODIFY_GROUP_INFO;
	}

	/**
	 * 获取圈子的点赞和评论信息
	 * 
	 * @return
	 */
	public static String getTopicDetail() {
		return SERVER + SERVICE_GET_TOPIC_DETAIL;
	}

	/**
	 * 圈话题点赞
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
	 * 圈话题评论
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
	 * 获得用户信息
	 * 
	 * @return
	 */
	public static String getUserInfo() {
		return SERVER + SERVICE_GET_USER_INFO;
	}

	/**
	 * 获取基本编码数据(除城市以外)
	 * 
	 * @return
	 */
	public static String getBaseCodeData() {
		return SERVER + GET_BASECODE_DATA;
	}

	/**
	 * 未用 获得广告信息
	 * 
	 * @return
	 */
	public static String getAdInfo() {
		return SERVER + SERVICE_GET_ADINFO;
	}

	/**
	 * 修改用户信息
	 * 
	 * @return
	 */
	public static String modifyUserInfo() {
		return SERVER + MODIFY_USER_INFO;
	}

	/**
	 * 设置用户头像
	 * 
	 * @return
	 */
	public static String setUserHeadImage() {
		return SERVER + SET_UHEAD;
	}

	/**
	 * 获得动态详细信息
	 * 
	 * @return
	 */
	public static String getDetailDynamic() {
		return SERVER + GET_FAMILY_STATUS_DETAIL;
	}

	/**
	 * 获得动态列表
	 * 
	 * @return
	 */
	public static String getDynamicList() {
		return SERVER + GET_LIST_FAMILY_STATUS;
	}

	/**
	 * 发布动态
	 * 
	 * @return
	 */
	public static String pubDynamic() {
		return SERVER + PUB_STATUS;
	}

	/**
	 * 删除动态
	 * 
	 * @return
	 */
	public static String deleteDynamic() {
		return SERVER + DELETE_FAMILY_STATUS;
	}

	/**
	 * 获取圈子成员列表
	 * 
	 * @return
	 */
	public static String getGroupMemberList() {
		return SERVER + GROUP_MEMBER_LIST;
	}

	/**
	 * 获取用户加入的或创建的活动
	 */
	public static String getUserEvent() {
		return SERVER + USER_ACTIVITY;
	}

	public static String getMainInfo() {
		return SERVER + SERVICE_MAIN_IDS;
	}

	/**
	 * 动态收藏
	 * 
	 * @return
	 */
	public static String collectStatusFamily() {
		return SERVER + COLLECT_STATUS;
	}

	/**
	 * 圈话题收藏
	 * 
	 * @return
	 */
	public static String collectTopic() {
		return SERVER + COLLECT_TOPIC;
	}

	/**
	 * 好友关系处理
	 * 
	 * @return
	 */
	public static String followUser() {
		return SERVER + FOLLOW_USER;
	}

	/**
	 * 获取圈子动态
	 * 
	 * @return
	 */
	public static String groupStatus() {
		return SERVER + STATUS_GROUP;
	}

	/**
	 * 获取请求添加好友的用户列表
	 * 
	 * @return
	 */
	public static String getFollowReqList() {
		return SERVER + FOLLOEW_REQ_LIST;
	}

	/**
	 * 名片点赞
	 * 
	 * @return
	 */
	public static String zanCard() {
		return SERVER + ZAN_CARD;
	}

	/**
	 * 公告详情
	 * 
	 * @return
	 */
	public static String gonggaoDetail() {
		return SERVER + GG_DETAIL;
	}

	/**
	 * 倬脉动态列表
	 * 
	 * @return
	 */
	public static String getPutList() {
		return SERVER + PUB_LIST;
	}

	/**
	 * 删除活动
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
	 * lef的，获取我的圈子，用来提供融云的圈子信息
	 * 
	 * @return
	 */
	public static String getUrlMyGroupList() {
		return SERVER + SERVICE_MYGROUPLIST;
	}

	/**
	 * 发布供需
	 * 
	 * @return
	 */
	public static String pubGongxu() {
		return SERVER + PUB_GONGXU;
	}

	/**
	 * 用户商务信息：供需
	 * 
	 * @return
	 */
	public static String getUserBusinessInfo() {
		return SERVER + USER_BUSINESS;
	}

	/**
	 * 上传个人相册图片
	 * 
	 * @return
	 */
	public static String pubPhoto() {
		return SERVER + SET_PHOTO;
	}
	/**
	 * 请求交换名片的家人
	 * @return
	 */
	public static String getFriendReq() {
		return SERVER + GET_REQ_FRIEND;
	}
	
	//我的企业中的公司及其产品的增删改查
	
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
	 * 倬脉动态的数量(被浏览等)
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
	 * 请求加入圈子的人
	 * @return
	 */
	public static String getReqQuanUsers() {
		return SERVER + QUAN_REQ_USER_LIST;
	}
	/**
	 * 与好友关系相关
	 * @return
	 */
	public static String makeFriends() {
		return SERVER + MAKE_FRIENDS;
	}
	
}