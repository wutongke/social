package com.cpstudio.zhuojiaren.helper;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import android.content.Context;
import android.util.Log;

import com.cpstudio.zhuojiaren.R;
import com.cpstudio.zhuojiaren.model.AboutUsVO;
import com.cpstudio.zhuojiaren.model.AdVO;
import com.cpstudio.zhuojiaren.model.BaseCodeData;
import com.cpstudio.zhuojiaren.model.BusinessInfoVO;
import com.cpstudio.zhuojiaren.model.CardMsgVO;
import com.cpstudio.zhuojiaren.model.ChangeBgAVO;
import com.cpstudio.zhuojiaren.model.CmtRcmdVO;
import com.cpstudio.zhuojiaren.model.CmtVO;
import com.cpstudio.zhuojiaren.model.Comment;
import com.cpstudio.zhuojiaren.model.CompanyNewVO;
import com.cpstudio.zhuojiaren.model.ContactVO;
import com.cpstudio.zhuojiaren.model.DownloadVO;
import com.cpstudio.zhuojiaren.model.Dynamic;
import com.cpstudio.zhuojiaren.model.EventVO;
import com.cpstudio.zhuojiaren.model.GXTypeCodeData;
import com.cpstudio.zhuojiaren.model.GeoVO;
import com.cpstudio.zhuojiaren.model.GoodsVO;
import com.cpstudio.zhuojiaren.model.GroupStatus;
import com.cpstudio.zhuojiaren.model.GroupsForIM;
import com.cpstudio.zhuojiaren.model.HangYeVO;
import com.cpstudio.zhuojiaren.model.ImMsgVO;
import com.cpstudio.zhuojiaren.model.ImQuanVO;
import com.cpstudio.zhuojiaren.model.MainHeadInfo;
import com.cpstudio.zhuojiaren.model.MessagePubVO;
import com.cpstudio.zhuojiaren.model.OrderVO;
import com.cpstudio.zhuojiaren.model.PagesCmtVO;
import com.cpstudio.zhuojiaren.model.PlanVO;
import com.cpstudio.zhuojiaren.model.Praise;
import com.cpstudio.zhuojiaren.model.ProductNewVO;
import com.cpstudio.zhuojiaren.model.Province;
import com.cpstudio.zhuojiaren.model.PushMsgVO;
import com.cpstudio.zhuojiaren.model.QuanTopicVO;
import com.cpstudio.zhuojiaren.model.QuanUserVO;
import com.cpstudio.zhuojiaren.model.QuanVO;
import com.cpstudio.zhuojiaren.model.RecentVisitVO;
import com.cpstudio.zhuojiaren.model.ResourceGXVO;
import com.cpstudio.zhuojiaren.model.ResultVO;
import com.cpstudio.zhuojiaren.model.RuleVO;
import com.cpstudio.zhuojiaren.model.SearchHotKeyWord;
import com.cpstudio.zhuojiaren.model.SysMsgVO;
import com.cpstudio.zhuojiaren.model.TeacherVO;
import com.cpstudio.zhuojiaren.model.TopicDetailVO;
import com.cpstudio.zhuojiaren.model.TotalUserVO;
import com.cpstudio.zhuojiaren.model.UserAndCollection;
import com.cpstudio.zhuojiaren.model.UserEvent;
import com.cpstudio.zhuojiaren.model.UserNewVO;
import com.cpstudio.zhuojiaren.model.UserVO;
import com.cpstudio.zhuojiaren.model.ZMCDCount;
import com.cpstudio.zhuojiaren.model.ZhuoInfoVO;
import com.cpstudio.zhuojiaren.model.ZhuoQuanVO;
import com.cpstudio.zhuojiaren.util.CommonUtil;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

public class JsonHandler {
	private String jsonData = null;
	private ResultVO resultVO = null;

	public JsonHandler(String data, Context context, boolean showMsg) {
		this.jsonData = processResult(data, context, showMsg);
	}

	public JsonHandler(String data, Context context) {
		this.jsonData = processResult(data, context, true);
	}

	public JsonHandler(String data) {
		this.jsonData = processResult(data, null, false);
	}

	public String processResult(String str, Context context, boolean showMsg) {
		if (null != str && !str.equals("")) {
			Log.i("processResult", str);
			resultVO = parseResult(str);
			String code = resultVO.getCode();
			String data = resultVO.getData();
			String msg = resultVO.getMsg();
			if (null != code) {
				// lz新版本后台code为0时表示成功
				if (code.equals("10000") || code.equals("0")) {
					if (null != data) {
						return data;
					}
				} else {
					if (showMsg && !"null".equals(msg) && null != msg) {
						CommonUtil.displayToast(context, msg);
					}
				}
			}
		} else {
			if (showMsg) {
				CommonUtil.displayToast(context, R.string.error0);
			}
		}
		return "";
	}

	public static boolean checkResult(String str) {
		if (null != str && !str.equals("")) {
			String code = parseResult(str).getCode();
			if (code != null && code.equals("0")) {
				return true;
			}
		}
		return false;
	}

	public static String getSingleResult(String str) {
		if (null != str && !str.equals("")) {
			ResultVO result = parseResult(str);
			String code = result.getCode();
			if (code.equals("0")) {
				return result.getData();
			}
		}
		return null;
	}

	public static boolean checkResult(String str, Context context) {
		if (null != str && !str.equals("")) {
			ResultVO result = parseResult(str);
			String code = result.getCode();
			if (null != code && code.equals("0")) {
				return true;
			} else {
				// 处理出错信息，目前只处理session，其他错处直接输出
				switch (Integer.parseInt(code)) {
				case ResultVO.SESSIONOUT:
					AppClientLef.getInstance(context).refreshSession(context);
					return false;
				}
				CommonUtil.displayToast(context, result.getMsg());
			}
		} else {
			// CommonUtil.displayToast(context, R.string.data_error);
		}
		return false;
	}

	public static ResultVO parseResult(String str) {
		ResultVO resultVO = new ResultVO();
		try {
			Log.i("Debug", str);
			JsonParser parser = new JsonParser();
			JsonElement ele = parser.parse(str);
			JsonObject object = ele.getAsJsonObject();
			String code = object.get("code").getAsString();
			String message = null;
			try {
				message = object.get("msg").getAsString();
			} catch (Exception e) {
				e.printStackTrace();
			}
			String data = null;
			try {
				data = object.get("data").getAsString();
			} catch (Exception e) {
				data = object.get("data").toString();
				// e.printStackTrace();
			}
			resultVO.setCode(code);
			resultVO.setMsg(message);
			resultVO.setData(data);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultVO;
	}

	public DownloadVO parseDownload() {
		DownloadVO item = null;
		try {
			Gson gson = new Gson();
			item = gson.fromJson(jsonData, DownloadVO.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return item;
	}

	public static PushMsgVO parsePushMsg(String jsonData) {
		PushMsgVO data = null;
		try {
			Gson gson = new Gson();
			data = gson.fromJson(jsonData, PushMsgVO.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return data;
	}

	public static GeoVO parseGeo(String jsonData) {
		GeoVO geoVO = null;
		try {
			Gson gson = new Gson();
			geoVO = gson.fromJson(jsonData, GeoVO.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return geoVO;
	}

	public QuanVO parseQuan() {
		QuanVO quanVO = null;
		try {
			Gson gson = new Gson();
			quanVO = gson.fromJson(jsonData, QuanVO.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return quanVO;
	}

	public GroupsForIM parseGroupsForIM() {
		GroupsForIM groupVO = null;
		try {
			Gson gson = new Gson();
			groupVO = gson.fromJson(jsonData, GroupsForIM.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return groupVO;
	}

	/**
	 * 解析活动详情
	 */
	public EventVO parseEvent() {
		EventVO eventVO = null;
		try {
			Gson gson = new Gson();
			eventVO = gson.fromJson(jsonData, EventVO.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return eventVO;
	}

	public UserVO parseUser() {
		UserVO userVO = null;
		try {
			Gson gson = new Gson();
			userVO = gson.fromJson(jsonData, UserVO.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return userVO;
	}

	public ZhuoInfoVO parseZhuoInfo() {
		ZhuoInfoVO zhuoInfoVO = null;
		try {
			Gson gson = new Gson();
			zhuoInfoVO = gson.fromJson(jsonData, ZhuoInfoVO.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return zhuoInfoVO;
	}

	/*
	 * lz获取供需详情
	 */
	public ResourceGXVO parseGongxuInfo() {
		ResourceGXVO gxInfoVO = null;
		try {
			Gson gson = new Gson();
			gxInfoVO = gson.fromJson(jsonData, ResourceGXVO.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return gxInfoVO;
	}

	public List<ResourceGXVO> parseGongxuList() {
		List<ResourceGXVO> list = new ArrayList<ResourceGXVO>();
		try {
			Type listType = new TypeToken<LinkedList<ResourceGXVO>>() {
			}.getType();
			Gson gson = new Gson();
			if (!jsonData.equals("") && !jsonData.equals("\"\"")) {
				LinkedList<ResourceGXVO> li = gson.fromJson(jsonData, listType);
				for (Iterator<ResourceGXVO> iterator = li.iterator(); iterator
						.hasNext();) {
					ResourceGXVO item = (ResourceGXVO) iterator.next();
					list.add(item);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	public AboutUsVO parseAboutUs() {
		AboutUsVO aboutUsVO = null;
		try {
			Gson gson = new Gson();
			aboutUsVO = gson.fromJson(jsonData, AboutUsVO.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return aboutUsVO;
	}

	public GoodsVO parseGoods() {
		GoodsVO goods = null;
		try {
			Gson gson = new Gson();
			goods = gson.fromJson(jsonData, GoodsVO.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return goods;
	}

	public PagesCmtVO parsePagesCmt() {
		PagesCmtVO data = null;
		try {
			Gson gson = new Gson();
			data = gson.fromJson(jsonData, PagesCmtVO.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return data;
	}

	public ContactVO parseContact() {
		ContactVO data = null;
		try {
			Gson gson = new Gson();
			data = gson.fromJson(jsonData, ContactVO.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return data;
	}

	public RuleVO parseRule() {
		RuleVO data = null;
		try {
			Gson gson = new Gson();
			data = gson.fromJson(jsonData, RuleVO.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return data;
	}

	public AdVO parseAd() {
		AdVO data = null;
		try {
			Gson gson = new Gson();
			data = gson.fromJson(jsonData, AdVO.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return data;
	}

	public TeacherVO parseTeacher() {
		TeacherVO data = null;
		try {
			Gson gson = new Gson();
			data = gson.fromJson(jsonData, TeacherVO.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return data;
	}

	public TotalUserVO parseTotalUser() {
		TotalUserVO data = null;
		try {
			Gson gson = new Gson();
			data = gson.fromJson(jsonData, TotalUserVO.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return data;
	}

	public ArrayList<AdVO> parseAdList() {
		ArrayList<AdVO> list = new ArrayList<AdVO>();
		try {
			Type listType = new TypeToken<LinkedList<AdVO>>() {
			}.getType();
			Gson gson = new Gson();
			if (!jsonData.equals("") && !jsonData.equals("\"\"")) {
				LinkedList<AdVO> li = gson.fromJson(jsonData, listType);
				for (Iterator<AdVO> iterator = li.iterator(); iterator
						.hasNext();) {
					AdVO item = (AdVO) iterator.next();
					list.add(item);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	public ArrayList<AboutUsVO> parseAboutUsList() {
		ArrayList<AboutUsVO> list = new ArrayList<AboutUsVO>();
		try {
			Type listType = new TypeToken<LinkedList<AboutUsVO>>() {
			}.getType();
			Gson gson = new Gson();
			if (!jsonData.equals("") && !jsonData.equals("\"\"")) {
				LinkedList<AboutUsVO> li = gson.fromJson(jsonData, listType);
				for (Iterator<AboutUsVO> iterator = li.iterator(); iterator
						.hasNext();) {
					AboutUsVO item = (AboutUsVO) iterator.next();
					list.add(item);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	public ArrayList<TeacherVO> parseTeacherList() {
		ArrayList<TeacherVO> list = new ArrayList<TeacherVO>();
		try {
			Type listType = new TypeToken<LinkedList<TeacherVO>>() {
			}.getType();
			Gson gson = new Gson();
			if (!jsonData.equals("") && !jsonData.equals("\"\"")) {
				LinkedList<TeacherVO> li = gson.fromJson(jsonData, listType);
				for (Iterator<TeacherVO> iterator = li.iterator(); iterator
						.hasNext();) {
					TeacherVO item = (TeacherVO) iterator.next();
					list.add(item);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	public ArrayList<ZhuoInfoVO> parseZhuoInfoList() {
		ArrayList<ZhuoInfoVO> list = new ArrayList<ZhuoInfoVO>();
		try {
			Type listType = new TypeToken<LinkedList<ZhuoInfoVO>>() {
			}.getType();
			Gson gson = new Gson();
			if (!jsonData.equals("") && !jsonData.equals("\"\"")) {
				LinkedList<ZhuoInfoVO> li = gson.fromJson(jsonData, listType);
				for (Iterator<ZhuoInfoVO> iterator = li.iterator(); iterator
						.hasNext();) {
					ZhuoInfoVO item = (ZhuoInfoVO) iterator.next();
					list.add(item);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	// lz解析活动列表
	public ArrayList<EventVO> parseEventInfoList() {
		ArrayList<EventVO> list = new ArrayList<EventVO>();
		try {
			Type listType = new TypeToken<LinkedList<EventVO>>() {
			}.getType();
			Gson gson = new Gson();
			if (!jsonData.equals("") && !jsonData.equals("\"\"")) {
				LinkedList<EventVO> li = gson.fromJson(jsonData, listType);
				for (Iterator<EventVO> iterator = li.iterator(); iterator
						.hasNext();) {
					EventVO item = (EventVO) iterator.next();
					list.add(item);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	public ArrayList<CompanyNewVO> parseCompanyInfoList() {
		ArrayList<CompanyNewVO> list = new ArrayList<CompanyNewVO>();
		try {
			Type listType = new TypeToken<LinkedList<CompanyNewVO>>() {
			}.getType();
			Gson gson = new Gson();
			if (!jsonData.equals("") && !jsonData.equals("\"\"")) {
				LinkedList<CompanyNewVO> li = gson.fromJson(jsonData, listType);
				for (Iterator<CompanyNewVO> iterator = li.iterator(); iterator
						.hasNext();) {
					CompanyNewVO item = (CompanyNewVO) iterator.next();
					list.add(item);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	public ArrayList<ProductNewVO> parseProductInfoList() {
		ArrayList<ProductNewVO> list = new ArrayList<ProductNewVO>();
		try {
			Type listType = new TypeToken<LinkedList<ProductNewVO>>() {
			}.getType();
			Gson gson = new Gson();
			if (!jsonData.equals("") && !jsonData.equals("\"\"")) {
				LinkedList<ProductNewVO> li = gson.fromJson(jsonData, listType);
				for (Iterator<ProductNewVO> iterator = li.iterator(); iterator
						.hasNext();) {
					ProductNewVO item = (ProductNewVO) iterator.next();
					list.add(item);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	public ArrayList<GoodsVO> parseGoodsList() {
		ArrayList<GoodsVO> list = new ArrayList<GoodsVO>();
		try {
			Type listType = new TypeToken<LinkedList<GoodsVO>>() {
			}.getType();
			Gson gson = new Gson();
			if (!jsonData.equals("") && !jsonData.equals("\"\"")) {
				LinkedList<GoodsVO> li = gson.fromJson(jsonData, listType);

				for (Iterator<GoodsVO> iterator = li.iterator(); iterator
						.hasNext();) {
					GoodsVO item = (GoodsVO) iterator.next();
					list.add(item);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	public ArrayList<UserVO> parseTUserList() {
		ArrayList<UserVO> list = new ArrayList<UserVO>();
		try {
			Type listType = new TypeToken<LinkedList<UserVO>>() {
			}.getType();
			Gson gson = new Gson();
			if (!jsonData.equals("") && !jsonData.equals("\"\"")) {
				LinkedList<UserVO> li = gson.fromJson(jsonData, listType);

				for (Iterator<UserVO> iterator = li.iterator(); iterator
						.hasNext();) {
					UserVO item = (UserVO) iterator.next();
					list.add(item);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	public ArrayList<RecentVisitVO> parseRecentVisitList() {
		ArrayList<RecentVisitVO> list = new ArrayList<RecentVisitVO>();
		try {
			Type listType = new TypeToken<LinkedList<RecentVisitVO>>() {
			}.getType();
			Gson gson = new Gson();
			if (!jsonData.equals("") && !jsonData.equals("\"\"")) {
				LinkedList<RecentVisitVO> li = gson
						.fromJson(jsonData, listType);

				for (Iterator<RecentVisitVO> iterator = li.iterator(); iterator
						.hasNext();) {
					RecentVisitVO item = (RecentVisitVO) iterator.next();
					list.add(item);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	public ArrayList<UserVO> parseUserList() {
		ArrayList<UserVO> list = new ArrayList<UserVO>();
		try {
			Type listType = new TypeToken<LinkedList<UserVO>>() {
			}.getType();
			Gson gson = new Gson();
			if (!jsonData.equals("") && !jsonData.equals("\"\"")) {
				LinkedList<UserVO> li = gson.fromJson(jsonData, listType);

				for (Iterator<UserVO> iterator = li.iterator(); iterator
						.hasNext();) {
					UserVO item = (UserVO) iterator.next();
					list.add(item);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	public ArrayList<UserNewVO> parseUserNewList() {
		ArrayList<UserNewVO> list = new ArrayList<UserNewVO>();
		try {
			Type listType = new TypeToken<LinkedList<UserNewVO>>() {
			}.getType();
			Gson gson = new Gson();
			if (!jsonData.equals("") && !jsonData.equals("\"\"")) {
				LinkedList<UserNewVO> li = gson.fromJson(jsonData, listType);

				for (Iterator<UserNewVO> iterator = li.iterator(); iterator
						.hasNext();) {
					UserNewVO item = (UserNewVO) iterator.next();
					list.add(item);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	public ArrayList<CmtVO> parseCmtList() {
		ArrayList<CmtVO> list = new ArrayList<CmtVO>();
		try {
			Type listType = new TypeToken<LinkedList<CmtVO>>() {
			}.getType();
			Gson gson = new Gson();
			if (!jsonData.equals("") && !jsonData.equals("\"\"")) {
				LinkedList<CmtVO> li = gson.fromJson(jsonData, listType);

				for (Iterator<CmtVO> iterator = li.iterator(); iterator
						.hasNext();) {
					CmtVO item = (CmtVO) iterator.next();
					list.add(item);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	public ArrayList<CardMsgVO> parseCardMsgList() {
		ArrayList<CardMsgVO> list = new ArrayList<CardMsgVO>();
		try {
			Type listType = new TypeToken<LinkedList<CardMsgVO>>() {
			}.getType();
			Gson gson = new Gson();
			if (!jsonData.equals("") && !jsonData.equals("\"\"")) {
				LinkedList<CardMsgVO> li = gson.fromJson(jsonData, listType);

				for (Iterator<CardMsgVO> iterator = li.iterator(); iterator
						.hasNext();) {
					CardMsgVO item = (CardMsgVO) iterator.next();
					list.add(item);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	public ArrayList<CmtRcmdVO> parseCmtRecomandList() {
		ArrayList<CmtRcmdVO> list = new ArrayList<CmtRcmdVO>();
		try {
			Type listType = new TypeToken<LinkedList<CmtRcmdVO>>() {
			}.getType();
			Gson gson = new Gson();
			if (!jsonData.equals("") && !jsonData.equals("\"\"")) {
				LinkedList<CmtRcmdVO> li = gson.fromJson(jsonData, listType);

				for (Iterator<CmtRcmdVO> iterator = li.iterator(); iterator
						.hasNext();) {
					CmtRcmdVO item = (CmtRcmdVO) iterator.next();
					list.add(item);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	public ArrayList<QuanUserVO> parseGroupMemberList() {
		ArrayList<QuanUserVO> list = new ArrayList<QuanUserVO>();
		try {
			Type listType = new TypeToken<LinkedList<QuanUserVO>>() {
			}.getType();
			Gson gson = new Gson();
			if (!jsonData.equals("") && !jsonData.equals("\"\"")) {
				LinkedList<QuanUserVO> li = gson.fromJson(jsonData, listType);

				for (Iterator<QuanUserVO> iterator = li.iterator(); iterator
						.hasNext();) {
					QuanUserVO item = (QuanUserVO) iterator.next();
					list.add(item);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	public ArrayList<HangYeVO> parseFieldList() {
		ArrayList<HangYeVO> list = new ArrayList<HangYeVO>();
		try {
			Type listType = new TypeToken<LinkedList<HangYeVO>>() {
			}.getType();
			Gson gson = new Gson();
			if (!jsonData.equals("") && !jsonData.equals("\"\"")) {
				LinkedList<HangYeVO> li = gson.fromJson(jsonData, listType);

				for (Iterator<HangYeVO> iterator = li.iterator(); iterator
						.hasNext();) {
					HangYeVO item = (HangYeVO) iterator.next();
					list.add(item);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	public ArrayList<PlanVO> parsePlanList() {
		ArrayList<PlanVO> list = new ArrayList<PlanVO>();
		try {
			Type listType = new TypeToken<LinkedList<PlanVO>>() {
			}.getType();
			Gson gson = new Gson();
			if (!jsonData.equals("") && !jsonData.equals("\"\"")) {
				LinkedList<PlanVO> li = gson.fromJson(jsonData, listType);

				for (Iterator<PlanVO> iterator = li.iterator(); iterator
						.hasNext();) {
					PlanVO item = (PlanVO) iterator.next();
					list.add(item);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	public ArrayList<ImMsgVO> parseImMsgList() {
		ArrayList<ImMsgVO> list = new ArrayList<ImMsgVO>();
		try {
			Type listType = new TypeToken<LinkedList<ImMsgVO>>() {
			}.getType();
			Gson gson = new Gson();
			if (!jsonData.equals("") && !jsonData.equals("\"\"")) {
				LinkedList<ImMsgVO> li = gson.fromJson(jsonData, listType);

				for (Iterator<ImMsgVO> iterator = li.iterator(); iterator
						.hasNext();) {
					ImMsgVO item = (ImMsgVO) iterator.next();
					list.add(item);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	public ArrayList<ImQuanVO> parseImQuanList() {
		ArrayList<ImQuanVO> list = new ArrayList<ImQuanVO>();
		try {
			Type listType = new TypeToken<LinkedList<ImQuanVO>>() {
			}.getType();
			Gson gson = new Gson();
			if (!jsonData.equals("") && !jsonData.equals("\"\"")) {
				LinkedList<ImQuanVO> li = gson.fromJson(jsonData, listType);

				for (Iterator<ImQuanVO> iterator = li.iterator(); iterator
						.hasNext();) {
					ImQuanVO item = (ImQuanVO) iterator.next();
					list.add(item);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	public ArrayList<SysMsgVO> parseSysMsgList() {
		ArrayList<SysMsgVO> list = new ArrayList<SysMsgVO>();
		try {
			Type listType = new TypeToken<LinkedList<SysMsgVO>>() {
			}.getType();
			Gson gson = new Gson();
			if (!jsonData.equals("") && !jsonData.equals("\"\"")) {
				LinkedList<SysMsgVO> li = gson.fromJson(jsonData, listType);

				for (Iterator<SysMsgVO> iterator = li.iterator(); iterator
						.hasNext();) {
					SysMsgVO item = (SysMsgVO) iterator.next();
					list.add(item);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	public ArrayList<QuanVO> parseQuanList() {
		ArrayList<QuanVO> list = new ArrayList<QuanVO>();
		try {
			Type listType = new TypeToken<LinkedList<QuanVO>>() {
			}.getType();
			Gson gson = new Gson();
			if (!jsonData.equals("") && !jsonData.equals("\"\"")) {
				LinkedList<QuanVO> li = gson.fromJson(jsonData, listType);

				for (Iterator<QuanVO> iterator = li.iterator(); iterator
						.hasNext();) {
					QuanVO item = (QuanVO) iterator.next();
					list.add(item);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	public ArrayList<ZhuoQuanVO> parseZhuoQuanList() {
		ArrayList<ZhuoQuanVO> list = new ArrayList<ZhuoQuanVO>();
		try {
			Type listType = new TypeToken<LinkedList<ZhuoQuanVO>>() {
			}.getType();
			Gson gson = new Gson();
			if (!jsonData.equals("") && !jsonData.equals("\"\"")) {
				LinkedList<ZhuoQuanVO> li = gson.fromJson(jsonData, listType);

				for (Iterator<ZhuoQuanVO> iterator = li.iterator(); iterator
						.hasNext();) {
					ZhuoQuanVO item = (ZhuoQuanVO) iterator.next();
					list.add(item);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	public String getSingleKeyValue(String key) {
		JsonParser parser = new JsonParser();
		JsonElement elem = parser.parse(jsonData);
		JsonObject obj = null;
		if (elem.isJsonObject()) {
			obj = elem.getAsJsonObject();
		} else if (elem.isJsonArray()) {
			JsonArray array = elem.getAsJsonArray();
			obj = array.get(0).getAsJsonObject();
		}
		String rs = obj.get(key).getAsString();
		return rs;
	}

	// lzlzl
	public MainHeadInfo parseMainInfo() {
		MainHeadInfo data = null;
		try {
			Gson gson = new Gson();

			data = gson.fromJson(jsonData, MainHeadInfo.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return data;
	}

	public ArrayList<QuanTopicVO> parseQuanTopicList() {
		ArrayList<QuanTopicVO> list = new ArrayList<QuanTopicVO>();
		try {
			Type listType = new TypeToken<LinkedList<QuanTopicVO>>() {
			}.getType();
			Gson gson = new Gson();
			if (!jsonData.equals("") && !jsonData.equals("\"\"")) {
				LinkedList<QuanTopicVO> li = gson.fromJson(jsonData, listType);

				for (Iterator<QuanTopicVO> iterator = li.iterator(); iterator
						.hasNext();) {
					QuanTopicVO item = (QuanTopicVO) iterator.next();
					list.add(item);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	public ArrayList<Dynamic> parseDynamicList() {
		ArrayList<Dynamic> list = new ArrayList<Dynamic>();
		try {
			Type listType = new TypeToken<LinkedList<Dynamic>>() {
			}.getType();
			Gson gson = new Gson();
			if (!jsonData.equals("") && !jsonData.equals("\"\"")) {
				LinkedList<Dynamic> li = gson.fromJson(jsonData, listType);

				for (Iterator<Dynamic> iterator = li.iterator(); iterator
						.hasNext();) {
					Dynamic item = (Dynamic) iterator.next();
					list.add(item);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	public OrderVO parseOrderVO() {
		OrderVO data = null;
		try {
			Gson gson = new Gson();

			data = gson.fromJson(jsonData, OrderVO.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return data;
	}

	public ArrayList<OrderVO> parseOrderList() {
		ArrayList<OrderVO> list = new ArrayList<OrderVO>();
		try {
			Type listType = new TypeToken<LinkedList<OrderVO>>() {
			}.getType();
			Gson gson = new Gson();
			if (!jsonData.equals("") && !jsonData.equals("\"\"")) {
				LinkedList<OrderVO> li = gson.fromJson(jsonData, listType);

				for (Iterator<OrderVO> iterator = li.iterator(); iterator
						.hasNext();) {
					OrderVO item = (OrderVO) iterator.next();
					list.add(item);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	public ArrayList<ChangeBgAVO> parseBg() {
		ArrayList<ChangeBgAVO> list = new ArrayList<ChangeBgAVO>();
		try {
			Type listType = new TypeToken<LinkedList<ChangeBgAVO>>() {
			}.getType();
			Gson gson = new Gson();
			if (!jsonData.equals("") && !jsonData.equals("\"\"")) {
				LinkedList<ChangeBgAVO> li = gson.fromJson(jsonData, listType);

				for (Iterator<ChangeBgAVO> iterator = li.iterator(); iterator
						.hasNext();) {
					ChangeBgAVO item = (ChangeBgAVO) iterator.next();
					list.add(item);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	
	public ArrayList<UserAndCollection> parseUserCollection() {
		ArrayList<UserAndCollection> list = new ArrayList<UserAndCollection>();
		try {
			Type listType = new TypeToken<LinkedList<UserAndCollection>>() {
			}.getType();
			Gson gson = new Gson();
			if (!jsonData.equals("") && !jsonData.equals("\"\"")) {
				LinkedList<UserAndCollection> li = gson.fromJson(jsonData,
						listType);

				for (Iterator<UserAndCollection> iterator = li.iterator(); iterator
						.hasNext();) {
					UserAndCollection item = (UserAndCollection) iterator
							.next();
					list.add(item);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	public ArrayList<GroupStatus> parseGroupStatusList() {
		ArrayList<GroupStatus> list = new ArrayList<GroupStatus>();
		try {
			Type listType = new TypeToken<LinkedList<GroupStatus>>() {
			}.getType();
			Gson gson = new Gson();
			if (!jsonData.equals("") && !jsonData.equals("\"\"")) {
				LinkedList<GroupStatus> li = gson.fromJson(jsonData, listType);

				for (Iterator<GroupStatus> iterator = li.iterator(); iterator
						.hasNext();) {
					GroupStatus item = (GroupStatus) iterator.next();
					list.add(item);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	public TopicDetailVO parseQuanTopicDetail() {
		TopicDetailVO detail = null;
		try {
			Gson gson = new Gson();
			detail = gson.fromJson(jsonData, TopicDetailVO.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return detail;
	}

	public Dynamic parseDynamicDetail() {
		Dynamic detail = null;
		try {
			Gson gson = new Gson();
			detail = gson.fromJson(jsonData, Dynamic.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return detail;
	}

	public UserEvent parseUserEvent() {
		UserEvent detail = null;
		try {
			Gson gson = new Gson();
			detail = gson.fromJson(jsonData, UserEvent.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return detail;
	}

	/**
	 * 解析评论后返回的评论列表
	 * 
	 * @return
	 */
	public List<Comment> parseQuanTopicCommentList() {
		List<Comment> list = new ArrayList<Comment>();
		try {
			Type listType = new TypeToken<LinkedList<Comment>>() {
			}.getType();
			Gson gson = new Gson();
			if (!jsonData.equals("") && !jsonData.equals("\"\"")) {
				LinkedList<Comment> li = gson.fromJson(jsonData, listType);

				for (Iterator<Comment> iterator = li.iterator(); iterator
						.hasNext();) {
					Comment item = (Comment) iterator.next();
					list.add(item);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	/**
	 * 解析点赞成功后返回的点赞列表
	 * 
	 * @return
	 */
	public List<Praise> parseQuanTopicPraiseList() {
		List<Praise> list = new ArrayList<Praise>();
		try {
			Type listType = new TypeToken<LinkedList<Praise>>() {
			}.getType();
			Gson gson = new Gson();
			if (!jsonData.equals("") && !jsonData.equals("\"\"")) {
				LinkedList<Praise> li = gson.fromJson(jsonData, listType);

				for (Iterator<Praise> iterator = li.iterator(); iterator
						.hasNext();) {
					Praise item = (Praise) iterator.next();
					list.add(item);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	/**
	 * 倬脉动态
	 * 
	 * @return
	 */
	public List<MessagePubVO> parsePubMessageList() {
		List<MessagePubVO> list = new ArrayList<MessagePubVO>();
		try {
			Type listType = new TypeToken<LinkedList<MessagePubVO>>() {
			}.getType();
			Gson gson = new Gson();
			if (!jsonData.equals("") && !jsonData.equals("\"\"")) {
				LinkedList<MessagePubVO> li = gson.fromJson(jsonData, listType);

				for (Iterator<MessagePubVO> iterator = li.iterator(); iterator
						.hasNext();) {
					MessagePubVO item = (MessagePubVO) iterator.next();
					list.add(item);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	/**
	 * 新的获取个人用户信息
	 * 
	 * @return
	 */
	public UserNewVO parseNewUser() {
		UserNewVO userVO = null;
		try {
			Gson gson = new Gson();
			userVO = gson.fromJson(jsonData, UserNewVO.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return userVO;
	}

	public ArrayList<SearchHotKeyWord> parseHotWords() {
		ArrayList<SearchHotKeyWord> list = new ArrayList<SearchHotKeyWord>();
		try {
			Type listType = new TypeToken<LinkedList<SearchHotKeyWord>>() {
			}.getType();
			Gson gson = new Gson();
			if (!jsonData.equals("") && !jsonData.equals("\"\"")) {
				LinkedList<SearchHotKeyWord> li = gson.fromJson(jsonData,
						listType);

				for (Iterator<SearchHotKeyWord> iterator = li.iterator(); iterator
						.hasNext();) {
					SearchHotKeyWord item = (SearchHotKeyWord) iterator.next();
					list.add(item);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	public BusinessInfoVO parseBusinessInfo() {
		BusinessInfoVO info = null;
		try {
			Gson gson = new Gson();
			info = gson.fromJson(jsonData, BusinessInfoVO.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return info;
	}

	public ZMCDCount parseZmCDCount() {
		ZMCDCount info = null;
		try {
			Gson gson = new Gson();
			info = gson.fromJson(jsonData, ZMCDCount.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return info;
	}

	public BaseCodeData parseBaseCodeData() {
		BaseCodeData baseData = null;
		try {
			Gson gson = new Gson();
			baseData = gson.fromJson(jsonData, BaseCodeData.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return baseData;
	}

	public MessagePubVO parseMessagePub() {
		MessagePubVO msg = null;
		try {
			Gson gson = new Gson();
			msg = gson.fromJson(jsonData, MessagePubVO.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return msg;
	}

	public static BaseCodeData parseBaseCodeData(String jsonData) {
		BaseCodeData baseData = null;
		try {
			Gson gson = new Gson();
			baseData = gson.fromJson(jsonData, BaseCodeData.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return baseData;
	}
	public static GXTypeCodeData parseGXTypeCodeData(String jsonData) {
		GXTypeCodeData gxCodeData = null;
		try {
			Gson gson = new Gson();
			gxCodeData = gson.fromJson(jsonData, GXTypeCodeData.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return gxCodeData;
	}

	// 注意看命名是否和json中的一致

	public static List<Province> parseCodedCitys(String jsonData) {
		List<Province> list = new ArrayList<Province>();
		try {
			Type listType = new TypeToken<LinkedList<Province>>() {
			}.getType();
			Gson gson = new Gson();
			if (!jsonData.equals("") && !jsonData.equals("\"\"")) {
				LinkedList<Province> li = gson.fromJson(jsonData, listType);

				for (Iterator<Province> iterator = li.iterator(); iterator
						.hasNext();) {
					Province item = (Province) iterator.next();
					list.add(item);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

}
