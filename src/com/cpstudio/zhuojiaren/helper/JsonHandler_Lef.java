package com.cpstudio.zhuojiaren.helper;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Iterator;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

import com.cpstudio.zhuojiaren.model.CommentVO;
import com.cpstudio.zhuojiaren.model.CrowdFundingVO;
import com.cpstudio.zhuojiaren.model.GoodsVO;
import com.cpstudio.zhuojiaren.model.GrouthVedio;
import com.cpstudio.zhuojiaren.model.GrouthVisit;
import com.cpstudio.zhuojiaren.model.IncomeVO;
import com.cpstudio.zhuojiaren.model.LoginRes;
import com.cpstudio.zhuojiaren.model.ProgressVO;
import com.cpstudio.zhuojiaren.model.QuanVO;
import com.cpstudio.zhuojiaren.model.RecordVO;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class JsonHandler_Lef {

	/**
	 * 登录时获取session
	 */
	public static LoginRes parseLoginRes(Context context,String data){
		if (data==null){
			Log.w("Debug", "登录没有返回session");
		}
		try {
			JSONObject obj = new JSONObject(data);
			String session = obj.optString("session");
			if(session ==null||session.isEmpty()){
				Log.w("Debug", "session为空");
			}
			String qiniuToken = obj.optString("qiniuToken");
			if(qiniuToken ==null||qiniuToken.isEmpty()){
				Log.w("Debug", "qiniuToken为空");
			}
			String rongyunToken = obj.optString("rongyunToken");
			if(rongyunToken ==null||rongyunToken.isEmpty()){
				Log.w("Debug", "rongyunToken为空");
			}
			String userId = obj.optString("userid");
			//保存信息
			LoginRes res = new LoginRes();
			res.setQiniuToken(qiniuToken);
			res.setRongyunToken(rongyunToken);
			res.setUserid(userId);
			res.setSession(session);
			//如果是session过期导致的重新登录，则在这里处理
			return res;
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.w("Debug", "登录信息解析失败");
			return null;
		}
	}
	/**
	 * 圈子列表
	 */
	public static ArrayList<QuanVO> parseQuanList(String jsonData) {
		ArrayList<QuanVO> list = new ArrayList<QuanVO>();
		try {
			Type listType = new TypeToken<ArrayList<QuanVO>>() {
			}.getType();
			Gson gson = new Gson();
			if (!jsonData.equals("") && !jsonData.equals("\"\"")) {
				ArrayList<QuanVO> li = gson.fromJson(jsonData, listType);

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
	/**
	 * 解析列表
	 * @param <T>
	 * @param <T>
	 */
	public static   ArrayList<GrouthVedio> parseGrouthVedioList(String jsonData) {
		ArrayList<GrouthVedio> list = new ArrayList<GrouthVedio>();
		try {
			Type listType = new TypeToken<ArrayList<GrouthVedio>>() {
			}.getType();
			Gson gson = new Gson();
			if (!jsonData.equals("") && !jsonData.equals("\"\"")) {
				ArrayList<GrouthVedio> li = gson.fromJson(jsonData, listType);

				for (Iterator<GrouthVedio> iterator = li.iterator(); iterator
						.hasNext();) {
					GrouthVedio item = (GrouthVedio) iterator.next();
					list.add(item);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	/**
	 * 解析列表
	 * @param <T>
	 * @param <T>
	 */
	public static   ArrayList<RecordVO> parseAudioList(String jsonData) {
		ArrayList<RecordVO> list = new ArrayList<RecordVO>();
		try {
			Type listType = new TypeToken<ArrayList<RecordVO>>() {
			}.getType();
			Gson gson = new Gson();
			if (!jsonData.equals("") && !jsonData.equals("\"\"")) {
				ArrayList<RecordVO> li = gson.fromJson(jsonData, listType);

				for (Iterator<RecordVO> iterator = li.iterator(); iterator
						.hasNext();) {
					RecordVO item = (RecordVO) iterator.next();
					list.add(item);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	/**
	 * 解析评论列表
	 * @param <T>
	 * @param <T>
	 */
	public static   ArrayList<CommentVO> parseCommentList(String jsonData) {
		ArrayList<CommentVO> list = new ArrayList<CommentVO>();
		try {
			Type listType = new TypeToken<ArrayList<CommentVO>>() {
			}.getType();
			Gson gson = new Gson();
			if (!jsonData.equals("") && !jsonData.equals("\"\"")) {
				ArrayList<CommentVO> li = gson.fromJson(jsonData, listType);

				for (Iterator<CommentVO> iterator = li.iterator(); iterator
						.hasNext();) {
					CommentVO item = (CommentVO) iterator.next();
					list.add(item);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	/**
	 * 解析进展列表
	 * @param <T>
	 * @param <T>
	 */
	public static   ArrayList<ProgressVO> parseProgressVOList(String jsonData) {
		ArrayList<ProgressVO> list = new ArrayList<ProgressVO>();
		try {
			Type listType = new TypeToken<ArrayList<ProgressVO>>() {
			}.getType();
			Gson gson = new Gson();
			if (!jsonData.equals("") && !jsonData.equals("\"\"")) {
				ArrayList<ProgressVO> li = gson.fromJson(jsonData, listType);

				for (Iterator<ProgressVO> iterator = li.iterator(); iterator
						.hasNext();) {
					ProgressVO item = (ProgressVO) iterator.next();
					list.add(item);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	/**
	 * 解析采访列表
	 * @param jsonData
	 * @return
	 */
	public static   ArrayList<GrouthVisit> parseVisitList(String jsonData) {
		ArrayList<GrouthVisit> list = new ArrayList<GrouthVisit>();
		try {
			Type listType = new TypeToken<ArrayList<GrouthVisit>>() {
			}.getType();
			Gson gson = new Gson();
			if (!jsonData.equals("") && !jsonData.equals("\"\"")) {
				ArrayList<GrouthVisit> li = gson.fromJson(jsonData, listType);

				for (Iterator<GrouthVisit> iterator = li.iterator(); iterator
						.hasNext();) {
					GrouthVisit item = (GrouthVisit) iterator.next();
					list.add(item);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	public static ArrayList<CrowdFundingVO> parseFundingList(String jsonData) {
		// TODO Auto-generated method stub
		ArrayList<CrowdFundingVO> list = new ArrayList<CrowdFundingVO>();
		try {
			Type listType = new TypeToken<ArrayList<CrowdFundingVO>>() {
			}.getType();
			Gson gson = new Gson();
			if (!jsonData.equals("") && !jsonData.equals("\"\"")) {
				ArrayList<CrowdFundingVO> li = gson.fromJson(jsonData, listType);

				for (Iterator<CrowdFundingVO> iterator = li.iterator(); iterator
						.hasNext();) {
					CrowdFundingVO item = (CrowdFundingVO) iterator.next();
					list.add(item);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	public static ArrayList<GoodsVO> parseGoodsVOList(String jsonData) {
		// TODO Auto-generated method stub
		ArrayList<GoodsVO> list = new ArrayList<GoodsVO>();
		try {
			Type listType = new TypeToken<ArrayList<GoodsVO>>() {
			}.getType();
			Gson gson = new Gson();
			if (!jsonData.equals("") && !jsonData.equals("\"\"")) {
				ArrayList<GoodsVO> li = gson.fromJson(jsonData, listType);

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
	public static ArrayList<IncomeVO> parseIncomeVOList(String jsonData) {
		// TODO Auto-generated method stub
		ArrayList<IncomeVO> list = new ArrayList<IncomeVO>();
		try {
			Type listType = new TypeToken<ArrayList<IncomeVO>>() {
			}.getType();
			Gson gson = new Gson();
			if (!jsonData.equals("") && !jsonData.equals("\"\"")) {
				ArrayList<IncomeVO> li = gson.fromJson(jsonData, listType);

				for (Iterator<IncomeVO> iterator = li.iterator(); iterator
						.hasNext();) {
					IncomeVO item = (IncomeVO) iterator.next();
					list.add(item);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	/***
	 * 解析列表
	 * @param <T>
	 * @param <T>
	 * @param jsonData
	 * @return
	 */
	public  <T> ArrayList<T> parseList(String jsonData) {
		// TODO Auto-generated method stub
		ArrayList<T> list = new ArrayList<T>();
		try {
			Type listType = new TypeToken<ArrayList<T>>() {
			}.getType();
			Gson gson = new Gson();
			if (!jsonData.equals("") && !jsonData.equals("\"\"")) {
				ArrayList<T> li = gson.fromJson(jsonData, listType);

				for (Iterator<T> iterator = li.iterator(); iterator
						.hasNext();) {
					T item = (T) iterator.next();
					list.add(item);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
}
