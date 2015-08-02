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
	 * 获得用户信息
	 * 
	 * @return
	 */
	public static String getUserInfo() {
		return SERVER + SERVICE_GET_USER_INFO;
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
	/**
	 * 圈话题评论
	 * @return
	 */
	public static String topicComment() {
		return SERVER + GTOPIC_COMMENT;
	}
	
	/**
	 * 未用 获得广告信息
	 * 
	 * @return
	 */
	public static String getAdInfo() {
		return SERVER + SERVICE_GET_ADINFO;
	}
}