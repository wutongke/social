package com.cpstudio.zhuojiaren.helper;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;

import com.cpstudio.zhuojiaren.R;

public class ZhuoCommHelper {

	private final static String SERVER_PREFIX = "http://";
	private final static String SERVER_IP = "115.28.167.196";// "115.29.145.63";//
															// "zjr.ci77.com";//

	private final static String SERVER_PORT = ":9001";
	private final static String SERVER_APP = "/zhuo-api";
	public final static String SERVER = SERVER_PREFIX + SERVER_IP + SERVER_PORT
			+ SERVER_APP;

	// lz
	private final static String SERVICE_HOT_WORDS = "/hot_words";
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
	private final static String SERVICE_FOLLOW = "/follow";
	private final static String SERVICE_UPDATEUSERDETAIL = "/updateuserdetail";
	private final static String SERVICE_CREATEGROUP = "/creategroup";
	private final static String SERVICE_FOLLOWGROUP = "/followgroup";
	private final static String SERVICE_GROUPMEMBERS = "/groupmembers";
	private final static String SERVICE_GROUPDETAIL = "/groupdetail";
	private final static String SERVICE_GROUPALERT = "/groupalert";
	private final static String SERVICE_GROUPRELEASE = "/grouprelease";
	private final static String SERVICE_ADVICE = "/advice";
	private final static String SERVICE_GROUPCHAT = "/groupchat";
	private final static String SERVICE_CHATMSGLIST = "/chatmsglist";
	private final static String SERVICE_CHAT = "/chat";
	private final static String SERVICE_CHATLIST = "/chatlist";
	private final static String SERVICE_BLACK = "/black";
	private final static String SERVICE_PROSECUTE = "/prosecute";
	private final static String SERVICE_LASTVISITUSERLIST = "/lastvisituserlist";
	private final static String SERVICE_CARD = "/card";
	private final static String SERVICE_CMTRECOMMANDMSGLIST = "/cmtrecommandmsglist";
	private final static String SERVICE_RECOMMANDMSG = "/recommandmsg";
	private final static String SERVICE_RECOMMANDGROUP = "/recommandgroup";
	private final static String SERVICE_RECOMMANDUSER = "/recommanduser";
	private final static String SERVICE_SYSMSGLIST = "/sysmsglist";
	private final static String SERVICE_USERLIST = "/userlist";
	private final static String SERVICE_SENDCARD = "/sendcard";
	private final static String SERVICE_CARDCHANGELIST = "/cardchangelist";
	private final static String SERVICE_PLAN = "/plan";
	private final static String SERVICE_PLANLIST = "/planlist";
	private final static String SERVICE_HOTGROUPLIST = "/hotgrouplist";
	private final static String SERVICE_REGROUPLIST = "/grouprecommend.do";
	private final static String SERVICE_MYGROUPLIST = "/getmygroup.do";
	private final static String SERVICE_COLLECTLIST = "/collectlist";
	private final static String SERVICE_ALLZHUO = "/allzhuo";
	private final static String SERVICE_ADDDREAM = "/adddream";
	private final static String SERVICE_ADDPRODUCT = "/addproduct";
	private final static String SERVICE_GETMSGCOUNT = "/getmsgcount";
	private final static String SERVICE_DELHEADERIMG = "/delheaderimg";
	private final static String SERVICE_GROUPREMOVEUSER = "/groupremoveuser";
	private final static String SERVICE_CONTACT = "/contact";
	private final static String SERVICE_GETFOLLOWS = "/getfollows";
	private final static String SERVICE_LASTCHATLIST = "/lastchatlist";
	private final static String SERVICE_ACCEPTUSER = "/acceptuser";
	private final static String SERVICE_FORWARDINFO = "/forwardinfo";
	private final static String SERVICE_BIRTHDAYUSERS = "/birthdayusers";
	private final static String SERVICE_MYRESOURCE = "/myresource";
	private final static String SERVICE_DELRESOURCE = "/delresource";
	private final static String SERVICE_USERCONFIG = "/userconfig";
	private final static String SERVICE_UPDATECONFIG = "/updateconfig";
	private final static String SERVICE_ZENGHUITITLELIST = "/zenghuititlelist";
	private final static String SERVICE_MSGREAD = "/msgread";
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
	private final static String SERVICE_AUDIO_LIST = "/getAudio.do";
	private final static String SERVICE_Visit_LIST = "/getInterviewList.do";
	private final static String SUBMIT_VISIT= "/getGrowthOnlineType.do";

	private final static String SERVICE_Teacher_LIST = "/getTutor.do";
	private final static String SERVICE_Funding_LIST = "/crowdfunding/list.do";
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
	private final static String GETCROWDFUNDINGPROGRESS = "crowdfunding/progressList.do";
	public static String getGetcrowdfundingprogress() {
		return SERVER +GETCROWDFUNDINGPROGRESS;
	}

	private final static String PUBCROWDFUNDINGCOMMENT = "/crowdfunding/createProgress.do";
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

	public static String getUrlFollow() {
		return SERVER + SERVICE_FOLLOW;
	}

	public static String getUrlUpdateUserDetail() {
		return SERVER + SERVICE_UPDATEUSERDETAIL;
	}

	public static String getUrlCreateGroup() {
		return SERVER + SERVICE_CREATEGROUP;
	}

	public static String getUrlFollowGroup() {
		return SERVER + SERVICE_FOLLOWGROUP;
	}

	public static String getUrlGroupMembers() {
		return SERVER + SERVICE_GROUPMEMBERS;
	}

	public static String getUrlGroupDetail() {
		return SERVER + SERVICE_GROUPDETAIL;
	}

	public static String getUrlGroupAlert() {
		return SERVER + SERVICE_GROUPALERT;
	}

	public static String getUrlGroupRelease() {
		return SERVER + SERVICE_GROUPRELEASE;
	}

	public static String getUrlAdvice() {
		return SERVER + SERVICE_ADVICE;
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

	public static String getUrlBlack() {
		return SERVER + SERVICE_BLACK;
	}

	public static String getUrlProsecute() {
		return SERVER + SERVICE_PROSECUTE;
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

	public static String getUrlRecommandGroup() {
		return SERVER + SERVICE_RECOMMANDGROUP;
	}

	public static String getUrlRecommandUser() {
		return SERVER + SERVICE_RECOMMANDUSER;
	}

	public static String getUrlSysMsgList() {
		return SERVER + SERVICE_SYSMSGLIST;
	}

	public static String getUrlUserList() {
		return SERVER + SERVICE_USERLIST;
	}

	public static String getUrlSendCard() {
		return SERVER + SERVICE_SENDCARD;
	}

	public static String getUrlCardChangeList() {
		return SERVER + SERVICE_CARDCHANGELIST;
	}

	public static String getUrlPlan() {
		return SERVER + SERVICE_PLAN;
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
	public static String getUrlMyGroupList() {
		return SERVER + SERVICE_MYGROUPLIST;
	}
	public static String getUrlCollectList() {
		return SERVER + SERVICE_COLLECTLIST;
	}

	public static String getUrlAllZhuo() {
		return SERVER + SERVICE_ALLZHUO;
	}

	public static String getUrlAddDream() {
		return SERVER + SERVICE_ADDDREAM;
	}

	public static String getUrlAddProduct() {
		return SERVER + SERVICE_ADDPRODUCT;
	}

	public static String getUrlGetMsgCount() {
		return SERVER + SERVICE_GETMSGCOUNT;
	}

	public static String getUrlDelHeaderImg() {
		return SERVER + SERVICE_DELHEADERIMG;
	}

	public static String getUrlGroupRemoveUser() {
		return SERVER + SERVICE_GROUPREMOVEUSER;
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

	public static String getUrlAcceptUser() {
		return SERVER + SERVICE_ACCEPTUSER;
	}

	public static String getUrlForwardInfo() {
		return SERVER + SERVICE_FORWARDINFO;
	}

	public static String getUrlBirthdayUsers() {
		return SERVER + SERVICE_BIRTHDAYUSERS;
	}

	public static String getUrlMyResource() {
		return SERVER + SERVICE_MYRESOURCE;
	}

	public static String getUrlDelResource() {
		return SERVER + SERVICE_DELRESOURCE;
	}

	public static String getUrlUserConfig() {
		return SERVER + SERVICE_USERCONFIG;
	}

	public static String getUrlUpdateConfig() {
		return SERVER + SERVICE_UPDATECONFIG;
	}

	public static String getUrlZenghuiTitleList() {
		return SERVER + SERVICE_ZENGHUITITLELIST;
	}

	public static String getUrlMsgRead() {
		return SERVER + SERVICE_MSGREAD;
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

	// lz
	public static String getHotWords() {
		return SERVER + SERVICE_HOT_WORDS;
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

	public static String getYearFromTime(String time) {
		try {
			time = time.trim();
			if (time.indexOf("-") != -1) {
				String year = time.substring(0, time.indexOf("-")).trim();
				if (year.length() == 4 || year.length() == 2) {
					return year;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	public static String getMonthFromTime(String time) {
		try {
			time = time.trim();
			if (time.indexOf("-") != -1) {
				time = time.substring(time.indexOf("-") + 1).trim();
				if (time.indexOf("-") != -1) {
					String month = time.substring(0, time.indexOf("-")).trim();
					if (month.length() <= 2) {
						return month;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	public static String getDateFromTime(String time) {
		try {
			time = time.trim();
			if (time.indexOf("-") != -1) {
				time = time.substring(time.indexOf("-") + 1).trim();
				if (time.indexOf("-") != -1) {
					time = time.substring(time.indexOf("-") + 1).trim();
					String date = time;
					if (time.indexOf(" ") != -1) {
						date = time.substring(0, time.indexOf(" ")).trim();
					}
					if (date.length() <= 2) {
						return date;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	public static Map<String, Object> gentResInfo(String type, String category,
			String title, String content, Context context) {
		Map<String, Object> map = new HashMap<String, Object>();
		if (type != null && !type.equals("")) {
			if (type.equals(context.getString(R.string.type_xu_type))) {
				category = ZhuoCommHelper.transferMsgCategoryToString(category,
						context);
				map.put("ico", R.drawable.ico_xu);
				map.put("category", "¡¡¡¾" + category + "¡¿");
				map.put("title", title + "//");
				map.put("content", content);
			} else if (type.equals(context.getString(R.string.type_gong_type))) {
				category = ZhuoCommHelper.transferMsgCategoryToString(category,
						context);
				map.put("ico", R.drawable.ico_gong);
				map.put("category", "¡¡¡¾" + category + "¡¿");
				map.put("title", title + "//");
				map.put("content", content);
			} else if (type.equals(context.getString(R.string.type_board_type))) {
				map.put("ico", R.drawable.ico_quan);
				map.put("category", " ¡¾" + category + "¡¿");
				map.put("title", title + "//");
				map.put("content", content);
			} else if (type.equals(context.getString(R.string.type_daily_type))) {
				map.put("ico", 0);
				map.put("category", "");
				map.put("title", title);
				map.put("content", content);
			} else {
				map.put("ico", 0);
				map.put("category", "");
				map.put("title", title);
				map.put("content", content);
			}
		}
		return map;
	}

	public static String transferMsgTypeToString(String type, Context context) {
		String str = "";
		if (type.equals(context.getString(R.string.type_xu_type))) {
			str = context.getString(R.string.type_xu);
		} else if (type.equals(context.getString(R.string.type_gong_type))) {
			str = context.getString(R.string.type_gong);
		}
		return str;
	}

	public static String transferMsgStringToType(String str, Context context) {
		String type = "";
		if (str.equals(context.getString(R.string.type_xu))) {
			type = context.getString(R.string.type_xu_type);
		} else if (str.equals(context.getString(R.string.type_gong))) {
			type = context.getString(R.string.type_gong_type);
		}
		return type;
	}

	public static String transferMsgCategoryToString(String category,
			Context context) {
		String str = "";
		try {
			String[] reses = context.getResources().getStringArray(
					R.array.array_res_type);
			str = reses[Integer.valueOf(category)];
		} catch (Exception e) {
			e.printStackTrace();
		}
		return str;
	}

	public static String transferMsgStringToCategory(String category,
			Context context) {
		String type = "";
		try {
			String[] reses = context.getResources().getStringArray(
					R.array.array_res_type);
			for (int i = 0; i < reses.length; i++) {
				if (category.equals(reses[i])) {
					type = String.valueOf(i);
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return type;
	}

	public static String transferBoardCategoryToString(String category,
			Context context) {
		String str = "";
		if (category.equals(context.getString(R.string.type_board0_type))) {
			str = context.getString(R.string.type_board0);
		} else if (category
				.equals(context.getString(R.string.type_board1_type))) {
			str = context.getString(R.string.type_board1);
		}
		return str;
	}

	public static String transferBoardStringToCategory(String category,
			Context context) {
		String type = "";
		if (category.equals(context.getString(R.string.type_board0))) {
			type = context.getString(R.string.type_board0_type);
		} else if (category.equals(context.getString(R.string.type_board1))) {
			type = context.getString(R.string.type_board1_type);
		}
		return type;
	}

	public static String dayToSign(Context context, int month, int day) {
		int divider = 22;
		String[] res = new String[2];
		String[] sign = context.getResources().getStringArray(
				R.array.array_sign_type);
		switch (month) {
		case 1:
			res[0] = sign[9];
			res[1] = sign[10];
			divider = 19;
			break;
		case 2:
			res[0] = sign[10];
			res[1] = sign[11];
			divider = 18;
			break;
		case 3:
			res[0] = sign[11];
			res[1] = sign[0];
			divider = 20;
			break;
		case 4:
			res[0] = sign[0];
			res[1] = sign[1];
			divider = 19;
			break;
		case 5:
			res[0] = sign[1];
			res[1] = sign[2];
			divider = 20;
			break;
		case 6:
			res[0] = sign[2];
			res[1] = sign[3];
			divider = 21;
			break;
		case 7:
			res[0] = sign[3];
			res[1] = sign[4];
			divider = 22;
			break;
		case 8:
			res[0] = sign[4];
			res[1] = sign[5];
			divider = 22;
			break;
		case 9:
			res[0] = sign[5];
			res[1] = sign[6];
			divider = 22;
			break;
		case 10:
			res[0] = sign[6];
			res[1] = sign[7];
			divider = 23;
			break;
		case 11:
			res[0] = sign[7];
			res[1] = sign[8];
			divider = 22;
			break;
		case 12:
			res[0] = sign[8];
			res[1] = sign[9];
			divider = 21;
			break;
		default:
			break;
		}
		if (day > divider) {
			return res[1];
		} else {
			return res[0];
		}
	}

	public static String getFirst(String orgin, String str) {
		if (orgin.indexOf(str) != -1) {
			orgin = orgin.substring(0, orgin.indexOf(str));
		}
		return orgin;
	}

	public static String subLast(String orgin) {
		if (orgin.length() > 0) {
			orgin = orgin.substring(0, orgin.length() - 1);
		}
		return orgin;
	}

	public static String concatStringWithTag(String orgin, String concat,
			String tag) {
		if (orgin != null && !orgin.equals("")) {
			if (concat != null && !concat.equals("")) {
				return orgin + tag + concat;
			} else {
				return orgin;
			}
		} else {
			if (concat != null && !concat.equals("")) {
				return concat;
			}
		}
		return "";
	}

	public static String[] getBirthday(String birthday) {
		String[] birth = new String[] { "1900", "1", "1" };
		if (birthday != null && birthday.indexOf("-") != -1) {
			birthday = birthday.trim();
			birth[0] = birthday.substring(0, birthday.indexOf("-"));
			birth[1] = birthday.substring(birthday.indexOf("-") + 1,
					birthday.lastIndexOf("-"));
			if (birthday.indexOf(" ") != -1) {
				birth[2] = birthday.substring(birthday.lastIndexOf("-") + 1,
						birthday.indexOf(" "));
			} else {
				birth[2] = birthday.substring(birthday.lastIndexOf("-") + 1);
			}
		}
		return birth;
	}

	public static String getZenghuiName(Context context, String type) {
		String[] zenghuitype = context.getResources().getStringArray(
				R.array.array_zenghui_type);
		if (type.equals("zxzh")) {
			return zenghuitype[0];
		} else if (type.equals("ldzx")) {
			return zenghuitype[1];
		} else if (type.equals("qywh")) {
			return zenghuitype[2];
		} else if (type.equals("scyx")) {
			return zenghuitype[3];
		} else if (type.equals("xcsj")) {
			return zenghuitype[4];
		} else if (type.equals("jxkp")) {
			return zenghuitype[5];
		} else if (type.equals("zzjz")) {
			return zenghuitype[6];
		} else if (type.equals("rczp")) {
			return zenghuitype[7];
		} else if (type.equals("cwgl")) {
			return zenghuitype[8];
		} else if (type.equals("xlcz")) {
			return zenghuitype[9];
		} else if (type.equals("fyrw")) {
			return zenghuitype[10];
		} else if (type.equals("khfw")) {
			return zenghuitype[11];
		} else if (type.equals("ppcz")) {
			return zenghuitype[12];
		} else if (type.equals("yctx")) {
			return zenghuitype[13];
		} else if (type.equals("zlgh")) {
			return zenghuitype[14];
		} else {
			return "";
		}
	}

	
}