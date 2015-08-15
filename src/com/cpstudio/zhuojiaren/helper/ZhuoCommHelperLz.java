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
	 * 用户加入的或创建的活动
	 */
	private final static String USER_ACTIVITY = "/getUserActivityList.do";

	//lef的
	private final static String SERVICE_MYGROUPLIST = "/getmygroup.do";
	
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
	 * @return
	 */
	public static String gonggaoDetail() {
		return SERVER + GG_DETAIL;
	}
	/**
	 * 倬脉动态列表
	 * @return
	 */
	public static String getPutList() {
		return SERVER + PUB_LIST;
	}
	/**
	 * 删除活动
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
	 * @return
	 */
	public static String getUrlMyGroupList() {
		return SERVER + SERVICE_MYGROUPLIST;
	}
}