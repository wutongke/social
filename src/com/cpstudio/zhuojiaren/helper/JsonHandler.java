package com.cpstudio.zhuojiaren.helper;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

import android.content.Context;

import com.cpstudio.zhuojiaren.R;
import com.cpstudio.zhuojiaren.model.AboutUsVO;
import com.cpstudio.zhuojiaren.model.AdVO;
import com.cpstudio.zhuojiaren.model.CardMsgVO;
import com.cpstudio.zhuojiaren.model.CmtRcmdVO;
import com.cpstudio.zhuojiaren.model.ContactVO;
import com.cpstudio.zhuojiaren.model.DownloadVO;
import com.cpstudio.zhuojiaren.model.EventVO;
import com.cpstudio.zhuojiaren.model.GeoVO;
import com.cpstudio.zhuojiaren.model.CmtVO;
import com.cpstudio.zhuojiaren.model.GoodsVO;
import com.cpstudio.zhuojiaren.model.ImQuanVO;
import com.cpstudio.zhuojiaren.model.PagesCmtVO;
import com.cpstudio.zhuojiaren.model.PushMsgVO;
import com.cpstudio.zhuojiaren.model.RecentVisitVO;
import com.cpstudio.zhuojiaren.model.ResultVO;
import com.cpstudio.zhuojiaren.model.RuleVO;
import com.cpstudio.zhuojiaren.model.TeacherVO;
import com.cpstudio.zhuojiaren.model.TotalUserVO;
import com.cpstudio.zhuojiaren.model.ZhuoInfoVO;
import com.cpstudio.zhuojiaren.model.QuanUserVO;
import com.cpstudio.zhuojiaren.model.QuanVO;
import com.cpstudio.zhuojiaren.model.HangYeVO;
import com.cpstudio.zhuojiaren.model.ImMsgVO;
import com.cpstudio.zhuojiaren.model.PlanVO;
import com.cpstudio.zhuojiaren.model.SysMsgVO;
import com.cpstudio.zhuojiaren.model.UserVO;
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
//			str = str.replaceAll(":null", ":\"\"");
			resultVO = parseResult(str);
			String code = resultVO.getCode();
			String data = resultVO.getData();
			String msg = resultVO.getMsg();
			if (null != code) {
				if (code.equals("10000")) {
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
			if (code.equals("10000")) {
				return true;
			}
		}
		return false;
	}

	public static String getSingleResult(String str) {
		if (null != str && !str.equals("")) {
			ResultVO result = parseResult(str);
			String code = result.getCode();
			if (code.equals("10000")) {
				return result.getData();
			}
		}
		return null;
	}

	public static boolean checkResult(String str, Context context) {
		if (null != str && !str.equals("")) {
			ResultVO result = parseResult(str);
			String code = result.getCode();
			if (code.equals("10000")) {
				return true;
			} else {
				CommonUtil.displayToast(context, result.getMsg());
			}
		} else {
			CommonUtil.displayToast(context, R.string.error17);
		}
		return false;
	}

	public static ResultVO parseResult(String str) {
		ResultVO resultVO = new ResultVO();
		try {
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

	//lz解析活动列表
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
				LinkedList<RecentVisitVO> li = gson.fromJson(jsonData, listType);

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
}
