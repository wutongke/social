package com.cpstudio.zhuojiaren.helper;
/**
 * 网络请求常量
 * @author lz
 *
 */
public class UrlHelper {

	private final static String SERVER_PREFIX = "http://";
	private final static String SERVER_IP = "115.28.167.196";

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
	/**
	 * 用户加入的或创建的活动
	 */
	private final static String USER_ACTIVITY = "/getUserActivityList.do";

	private final static String PUB_GONGXU = "/addSupplyDemand.do";
	private final static String USER_BUSINESS = "/getUserBusiness.do";

	private final static String SERVICE_GET_ADINFO = "/getadinfo.do";
	
	
	private final static String SERVICE_RESOURCE_GONGXU_DETAIL = "/resource_gongxu_detail";

	private final static String DISOLVE_QUAN = "/grouprelease.do";
	private final static String QUIT_QUAN = "//followgroup.do";
	private final static String SERVICE_LOGIN = "/login.do";
	private final static String SERVICE_MODIFYPWD = "/modifypwd";
	private final static String SERVICE_MSGLIST = "/msglist";
	private final static String SERVICE_PUBINFO = "/pubzhuoinfo";
	private final static String SERVICE_USERINFO = "/usersimple";
	private final static String SERVICE_CMT = "/pubcmt";
	private final static String SERVICE_GOOD = "/good";
	private final static String SERVICE_MSGDETAIL = "/msgdetail";
	private final static String SERVICE_CMTLIST = "/cmtlist";
	private final static String SERVICE_COLLECT = "/collect";
	private final static String SERVICE_FOLLOWGROUP = "/followgroup";
	private final static String SERVICE_GROUPMEMBERS = "/groupmembers";
	private final static String SERVICE_GROUPDETAIL = "/groupdetail";
	private final static String SERVICE_GROUPCHAT = "/groupchat";
	private final static String SERVICE_CHATMSGLIST = "/chatmsglist";
	private final static String SERVICE_CHAT = "/chat";
	private final static String SERVICE_CHATLIST = "/chatlist";
	private final static String SERVICE_LASTVISITUSERLIST = "/lastvisituserlist";
	private final static String SERVICE_CARD = "/card";
	private final static String SERVICE_CMTRECOMMANDMSGLIST = "/cmtrecommandmsglist";
	private final static String SERVICE_RECOMMANDMSG = "/recommandmsg";
	private final static String SERVICE_SYSMSGLIST = "/sysmsglist";
	private final static String SERVICE_USERLIST = "/userlist";
	private final static String SERVICE_CARDCHANGELIST = "/cardchangelist";
	private final static String SERVICE_PLANLIST = "/planlist";
	private final static String SERVICE_HOTGROUPLIST = "/hotgrouplist";
	private final static String SERVICE_REGROUPLIST = "/grouprecommend.do";
	private final static String SERVICE_MYGROUPLIST = "/getmygroup.do";
	private final static String SERVICE_COLLECTLIST = "/collectlist";
	private final static String SERVICE_GETMSGCOUNT = "/getmsgcount";
	private final static String SERVICE_CONTACT = "/contact";
	private final static String SERVICE_GETFOLLOWS = "/getfollows";
	private final static String SERVICE_LASTCHATLIST = "/lastchatlist";
	private final static String SERVICE_BIRTHDAYUSERS = "/birthdayusers";
	private final static String SERVICE_MYRESOURCE = "/myresource";
	private final static String SERVICE_USERCONFIG = "/userconfig";
	private final static String SERVICE_ZENGHUITITLELIST = "/zenghuititlelist";
	private final static String SERVICE_FAMILY = "/family";
	private final static String SERVICE_USERFOLLOWSGROUPS = "/userfollowsgroups";
	private final static String SERVICE_ANDROIDNAME = "/androidname";
	private final static String SERVICE_GETABOUTLIST = "/getaboutlist";
	private final static String SERVICE_GETABOUTDETAIL = "/getaboutdetail";
	private final static String SERVICE_GETLASTRECORD = "/getlastrecord";
	private final static String SERVICE_GETCLOUDVOICE = "/getcloundvoice";
	private final static String SERVICE_GETGOODSLIST = "/getgoodslist";
	private final static String SERVICE_GETGOODSDETAIL = "/getgoodsdetail";
	private final static String SERVICE_GETGOODSRULE = "/getgoodsrule";
	private final static String SERVICE_APPLYFOREXCH = "/applyforexch";
	private final static String SERVICE_GETZHUORMB = "/getzhuormb";
	private final static String SERVICE_GETMYCLOUDCHAT = "/getmycloudchat";
	private final static String SERVICE_ZHUONOTICE = "/zhuonotice";
	private final static String SERVICE_TEACHERLIST = "/teacherlist";
	private final static String SERVICE_TEACHER = "/teacher";
	private final static String SERVICE_CMT_LIKE = "/cmt_like";
	private final static String SERVICE_SHARECLOUD = "/sharecloud";
	private final static String SERVICE_SEARCH_QUAN = "/groupsearch.do";
	private final static String SERVICE_CITY_LIST = "/getcitylist.do";
	private final static String SERVICE_VEDIO_LIST = "/getGrowthOnlineList.do";
	private static String SERVICE_VEDIO_LIST_Collection = "/myCollectGrouthOnline.do";
	private final static String SERVICE_AUDIO_LIST = "/getAudio.do";
	private final static String SERVICE_Visit_LIST = "/getInterviewList.do";
	private final static String SUBMIT_VISIT= "/getGrowthOnlineType.do";

	private final static String SERVICE_Teacher_LIST = "/getTutor.do";
	private final static String SERVICE_Funding_LIST = "/crowdfunding/list.do";
	private static String SERVICE_Funding_LIST_Invest = "/crowdfunding/investList.do";
	private final static String AUDIO_COLL = "/praiseAudio.do";
	private final static String GrowthOnlineType = "/getGrowthOnlineType.do";
	private final static String CREATEGROUP = "/creategroup.do";
	private final static String ADDGROUPACTIVITY = "/addGroupActivity.do";
	private final static String CREATECROWDFUNDING = "/crowdfunding/insert.do";
	private final static String GETCROWDFUNDING = "/crowdfunding/detail.do";
	private final static String GETEVENTDETAIL = "/groupActivity.do";
	private final static String EVENTCOLLECTION = "/collectActivity.do";
	private final static String EVENTADD = "/joinActivity.do";
	private final static String COMMENT = "/crowdfunding/createComment.do";
	private final static String LIKEINCOMMENT = "/crowdfunding/commentLike.do";
	private final static String LIKECrowdFunding = "/crowdfunding/like.do";
	private final static String getCrowdFundingComment = "/crowdfunding/commentList.do";
	private final static String GETCROWDFUNDINGPROGRESS = "/crowdfunding/progressList.do";
	private final static String TIME = "/crowdfunding/progressList.do";
	private final static String PUBCROWDFUNDINGCOMMENT = "/crowdfunding/createProgress.do";
	private final static String GONGXULIST = "/supplyDemandList.do";
	private final static String GONGXUDETAIL = "/getSupplyDemand.do";
	private static String COMMONCOLLECTION = "/collectSupplyDemand.do";
	private static String SHARETOZHUO = "/shareSupplyDemand.do";
	private static String DELETEGONGXU = "/deleteSupplyDemand.do";
	private static String GOODSDETAIL = "/goods/goodsDetail.do";
	private static String GOODSLIST = "/goods/goodsList.do";
	private static String GOODSCOLLECTION = "/goods/collectList.do";
	private static String CARTGOODSLIST = "/goods/goodCart.do";
	private static String GoodsCollection = "/goods/collect.do";
	private static String GoodsAddToCart = "/goods/addGoodsCart.do";
	private static String PaybackList = "/crowdfunding/supportList.do";
	private static String GOODSCATEGORY = "/goods/topCategory.do";
	private static String GOODSNUMBER = "/genZhuobiOrder.do";
	private static String GIVEMONEYTOFRIEND = "/presentZhuobi.do";
	private static String MYZHUOBI = "/getMyZhuobi.do";
	private static String MALLPAY = "/mallPay.do";
	private static String GENERATEORDER = "/goods/generateOrder.do";
	private static String POSTPAYSTATUS = "/goods/setOrderStatus.do";
	private static String SHIPPINGADDRESS = "/goods/setOrderAddress.do";
	private static String INCOME = "/getMyZhuobiRecord.do";
	private static String REMOVEGOODS = "/goods/delGoodsCart.do";
	private static String AudioCollection = "/myAudio.do";
	
	
	
	
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
	 * 
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
	/**
	 * 我的人脉
	 * @return
	 */
	public static String myRenmai() {
		return SERVER + MY_RENMAI;
	}
	/**
	 * 评论和回复供需
	 * @return
	 */
	public static String cmtGX() {
		return SERVER + CMT_GONGXU;
	}
	/**
	 * 订单列表
	 * @return
	 */
	public static String orderList() {
		return SERVER + ORDERS_LIST;
	}
	/**
	 * 订单详情
	 * @return
	 */
	public static String orderDetail() {
		return SERVER + ORDER_DETAIL;
	}
	/**
	 * 名片背景图片
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
	public static String getAudioCollection() {
		return SERVER + AudioCollection;
	}
	public static String getServiceVedioListCollection() {
		return SERVER + SERVICE_VEDIO_LIST_Collection;
	}
	public static String getServiceFundingListInvest() {
		return SERVER +SERVICE_Funding_LIST_Invest;
	}


	public static String getREMOVEGOODS() {
		return SERVER +  REMOVEGOODS;
	}

	public static String getINCOME() {
		return SERVER +  INCOME;
	}

	public static String getSHIPPINGADDRESS() {
		return SERVER + SHIPPINGADDRESS;
	}

	public static String getPOSTPAYSTATUS() {
		return SERVER + POSTPAYSTATUS;
	}

	public static String getGENERATEORDER() {
		return SERVER + GENERATEORDER;
	}

	public static String getMALLPAY() {
		return SERVER + MALLPAY;
	}

	public static String getMYZHUOBI() {
		return SERVER + MYZHUOBI;
	}

	public static String getGIVEMONEYTOFRIEND() {
		return SERVER + GIVEMONEYTOFRIEND;
	}

	public static String getGOODSNUMBER() {
		return SERVER + GOODSNUMBER;
	}

	public static String getGOODSCATEGORY() {
		return SERVER + GOODSCATEGORY;
	}

	public static String getPaybackList() {
		return SERVER +  PaybackList;
	}

	public static String getGoodsAddToCart() {
		return SERVER + GoodsAddToCart;
	}

	public static String getGoodsCollection() {
		return SERVER + GoodsCollection;
	}

	public static String getCARTGOODSLIST() {
		return SERVER + CARTGOODSLIST;
	}

	public static String getGOODSCOLLECTION() {
		return SERVER + GOODSCOLLECTION;
	}

	public static String getGOODSLIST() {
		return SERVER + GOODSLIST;
	}

	public static String getGOODSDETAIL() {
		return SERVER + GOODSDETAIL;
	}

	public static String getDELETEGONGXU() {
		return SERVER + DELETEGONGXU;
	}

	public static String getSHARETOZHUO() {
		return SERVER + SHARETOZHUO;
	}

	public static String getCOMMONCOLLECTION() {
		return SERVER + COMMONCOLLECTION;
	}


	public static String getGongxudetail() {
		return SERVER + GONGXUDETAIL;
	}

	public static String getGongxulist() {
		return SERVER + GONGXULIST;
	}

	public static String getTime() {
		return SERVER + TIME;
	}

	//收藏列表urls
	public static final String[] collectionUrls = {
		SERVER + SERVICE_VEDIO_LIST,
		SERVER + SERVICE_VEDIO_LIST,
		SERVER + SERVICE_VEDIO_LIST,
		SERVER + SERVICE_VEDIO_LIST,
		SERVER + SERVICE_VEDIO_LIST,
		SERVER + SERVICE_VEDIO_LIST,
		SERVER + SERVICE_VEDIO_LIST,
		SERVER + SERVICE_VEDIO_LIST,
		SERVER + SERVICE_VEDIO_LIST,
		SERVER + SERVICE_VEDIO_LIST,
		SERVER + SERVICE_VEDIO_LIST,
		SERVER + SERVICE_VEDIO_LIST,
		SERVER + SERVICE_VEDIO_LIST,
		SERVER + SERVICE_VEDIO_LIST,
		SERVER + SERVICE_VEDIO_LIST,
		SERVER + SERVICE_VEDIO_LIST,
		SERVER + SERVICE_VEDIO_LIST,
		SERVER + SERVICE_VEDIO_LIST,
		SERVER + SERVICE_VEDIO_LIST,
		SERVER + SERVICE_VEDIO_LIST,
		SERVER + SERVICE_VEDIO_LIST,
		SERVER + SERVICE_VEDIO_LIST,
		SERVER + SERVICE_VEDIO_LIST,
		SERVER + SERVICE_VEDIO_LIST,
		SERVER + SERVICE_VEDIO_LIST,
		SERVER + SERVICE_VEDIO_LIST,
		SERVER + SERVICE_VEDIO_LIST
	};
	
	public static String getGetcrowdfundingprogress() {
		return SERVER +GETCROWDFUNDINGPROGRESS;
	}

	public static String getPubcrowdfundingcomment() {
		return SERVER +PUBCROWDFUNDINGCOMMENT;
	}
	public static String getGetcrowdfundingcomment() {
		return SERVER + getCrowdFundingComment;
	}
	public static String getLikecrowdfunding() {
		return SERVER + LIKECrowdFunding;
	}
	public static String getLikeincomment() {
		return SERVER +LIKEINCOMMENT;
	}
	public static String getComment() {
		return SERVER +COMMENT;
	}
	public static String getEventadd() {
		return SERVER + EVENTADD;
	}
	public static String getEventcollection() {
		return SERVER +EVENTCOLLECTION;
	}
	public static String getGeteventdetail() {
		return SERVER +GETEVENTDETAIL;
	}
	public static String getGetcrowdfunding() {
		return SERVER + GETCROWDFUNDING;
	}
	public static String getCreatecrowdfunding() {
		return SERVER + CREATECROWDFUNDING;
	}
	public static String getAddgroupactivity() {
		return SERVER + ADDGROUPACTIVITY;
	}
	public static String getCreategroup() {
		return SERVER + CREATEGROUP;
	}
	public static String getGrowthonlinetype() {
		return SERVER + GrowthOnlineType;
	}
	public static String getAudioColl() {
		return SERVER + AUDIO_COLL;
	}
	public static String getSubmitVisit() {
		return SERVER + SUBMIT_VISIT;
	}
	public static String getServiceFundingList() {
		return SERVER + SERVICE_Funding_LIST;
	}

	public static String getServiceTeacherList() {
		return SERVER + SERVICE_Teacher_LIST;
	}

	public static String getServiceVisitList() {
		return SERVER + SERVICE_Visit_LIST;
	}

	public static String getServiceAudioList() {
		return SERVER + SERVICE_AUDIO_LIST;
	}

	public static String getServiceVedioList() {
		return SERVER + SERVICE_VEDIO_LIST;
	}

	public static String getServiceCityList() {
		return SERVER + SERVICE_CITY_LIST;
	}

	public static String getServiceSearchQuan() {
		return SERVER + SERVICE_SEARCH_QUAN;
	}

	public static String getUrlShareCloud() {
		return SERVER + SERVICE_SHARECLOUD;
	}

	public static String getUrlCmtLike() {
		return SERVER + SERVICE_CMT_LIKE;
	}

	public static String getUrlTeacher() {
		return SERVER + SERVICE_TEACHER;
	}

	public static String getUrlTeacherList() {
		return SERVER + SERVICE_TEACHERLIST;
	}

	public static String getUrlZhuoNotice() {
		return SERVER + SERVICE_ZHUONOTICE;
	}

	public static String getUrlGetZhuoRMB() {
		return SERVER + SERVICE_GETZHUORMB;
	}

	public static String getUrlGetMyCloudChat() {
		return SERVER + SERVICE_GETMYCLOUDCHAT;
	}

	public static String getUrlGetAboutList() {
		return SERVER + SERVICE_GETABOUTLIST;
	}

	public static String getUrlGetAboutDetail() {
		return SERVER + SERVICE_GETABOUTDETAIL;
	}

	public static String getUrlGetLastRecord() {
		return SERVER + SERVICE_GETLASTRECORD;
	}

	public static String getUrlGetClodVoice() {
		return SERVER + SERVICE_GETCLOUDVOICE;
	}

	public static String getUrlGetGoodsList() {
		return SERVER + SERVICE_GETGOODSLIST;
	}

	public static String getUrlGetGoodsDetail() {
		return SERVER + SERVICE_GETGOODSDETAIL;
	}

	public static String getUrlGetGoodsRule() {
		return SERVER + SERVICE_GETGOODSRULE;
	}

	public static String getUrlApplyForExch() {
		return SERVER + SERVICE_APPLYFOREXCH;
	}

	public static String getUrlLogin() {
		return SERVER + SERVICE_LOGIN;
	}

	public static String getUrlChangPwd() {
		return SERVER + SERVICE_MODIFYPWD;
	}

	public static String getUrlMsgList() {
		return SERVER + SERVICE_MSGLIST;
	}

	public static String getUrlUserInfo() {
		return SERVER + SERVICE_USERINFO;
	}

	public static String getUrlPubinfo() {
		return SERVER + SERVICE_PUBINFO;
	}

	public static String getUrlCmt() {
		return SERVER + SERVICE_CMT;
	}

	public static String getUrlGood() {
		return SERVER + SERVICE_GOOD;
	}

	public static String getUrlMsgDetail() {
		return SERVER + SERVICE_MSGDETAIL;
	}

	public static String getUrlCmtList() {
		return SERVER + SERVICE_CMTLIST;
	}

	public static String getUrlCollect() {
		return SERVER + SERVICE_COLLECT;
	}

//	public static String getUrlFollow() {
//		return SERVER + SERVICE_FOLLOW;
//	}



	public static String getUrlFollowGroup() {
		return SERVER + SERVICE_FOLLOWGROUP;
	}

	public static String getUrlGroupMembers() {
		return SERVER + SERVICE_GROUPMEMBERS;
	}

	public static String getUrlGroupDetail() {
		return SERVER + SERVICE_GROUPDETAIL;
	}



	public static String getUrlGroupChat() {
		return SERVER + SERVICE_GROUPCHAT;
	}

	public static String getUrlChatMsgList() {
		return SERVER + SERVICE_CHATMSGLIST;
	}

	public static String getUrlChat() {
		return SERVER + SERVICE_CHAT;
	}

	public static String getUrlChatList() {
		return SERVER + SERVICE_CHATLIST;
	}


	public static String getUrlLastVisitUserList() {
		return SERVER + SERVICE_LASTVISITUSERLIST;
	}

	public static String getUrlCmtRecommandMsgList() {
		return SERVER + SERVICE_CMTRECOMMANDMSGLIST;
	}

	public static String getUrlCard() {
		return SERVER + SERVICE_CARD;
	}

	public static String getUrlRecommandMsg() {
		return SERVER + SERVICE_RECOMMANDMSG;
	}

	public static String getUrlSysMsgList() {
		return SERVER + SERVICE_SYSMSGLIST;
	}

	public static String getUrlUserList() {
		return SERVER + SERVICE_USERLIST;
	}

	public static String getUrlCardChangeList() {
		return SERVER + SERVICE_CARDCHANGELIST;
	}


	public static String getUrlPlanList() {
		return SERVER + SERVICE_PLANLIST;
	}

	public static String getUrlHotGroupList() {
		return SERVER + SERVICE_HOTGROUPLIST;
	}

	public static String getUrlReGroupList() {
		return SERVER + SERVICE_REGROUPLIST;
	}
	public static String getUrlCollectList() {
		return SERVER + SERVICE_COLLECTLIST;
	}

	public static String getUrlGetMsgCount() {
		return SERVER + SERVICE_GETMSGCOUNT;
	}

	public static String getUrlContact() {
		return SERVER + SERVICE_CONTACT;
	}

	public static String getUrlGetFollows() {
		return SERVER + SERVICE_GETFOLLOWS;
	}

	public static String getUrlLastChatList() {
		return SERVER + SERVICE_LASTCHATLIST;
	}


	public static String getUrlBirthdayUsers() {
		return SERVER + SERVICE_BIRTHDAYUSERS;
	}

	public static String getUrlMyResource() {
		return SERVER + SERVICE_MYRESOURCE;
	}


	public static String getUrlUserConfig() {
		return SERVER + SERVICE_USERCONFIG;
	}

	public static String getUrlZenghuiTitleList() {
		return SERVER + SERVICE_ZENGHUITITLELIST;
	}


	public static String getUrlFamily() {
		return SERVER + SERVICE_FAMILY;
	}

	public static String getUrlUserFollowsGroups() {
		return SERVER + SERVICE_USERFOLLOWSGROUPS;
	}

	public static String getUrlAndroidName() {
		return SERVER + SERVICE_ANDROIDNAME;
	}


	public static String getResourceGongxuDetail() {
		return SERVER + SERVICE_RESOURCE_GONGXU_DETAIL;
	}
	public static String getDisolveQuan() {
		return SERVER + DISOLVE_QUAN;
	}
	public static String getQuitQuan() {
		return SERVER + QUIT_QUAN;
	}

}