package com.cpstudio.zhuojiaren.facade;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.cpstudio.zhuojiaren.helper.DatabaseHelper;
import com.cpstudio.zhuojiaren.helper.ResHelper;
import com.cpstudio.zhuojiaren.model.QuanVO;
import com.cpstudio.zhuojiaren.model.ZhuoQuanVO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

public class ZhuoQuanFacade {
	private final DatabaseHelper dbHelper;
	private String tableName = null;
	private static final String[] COLUMNS = { "type", "typeid", "groupid" };
	private QuanFacade mFacade;
	public static final String ZHUOQUANLIST = "ZHUOQUANLIST";

	public ZhuoQuanFacade(Context context) {
		this.tableName = ZHUOQUANLIST;
		mFacade = new QuanFacade(context);
		String userid = ResHelper.getInstance(context).getUserid();
		dbHelper = new DatabaseHelper(context, userid);
	}

	@Override
	public void finalize() throws Throwable {
		super.finalize();
	}

	private boolean checkExist(String groupid, String typeid) {
		Cursor cursor = dbHelper.query(tableName, COLUMNS,
				"groupid = ? and typeid = ?", new String[] { groupid, typeid },
				null, null, null);
		if (cursor.getCount() != 1) {
			return false;
		}
		cursor.moveToFirst();
		cursor.close();
		return true;
	}

	public ArrayList<ZhuoQuanVO> getAll() {
		Cursor cursor = dbHelper.query(tableName, COLUMNS, null, null, null,
				null, null);
		return getItem(cursor);
	}

	public ArrayList<ZhuoQuanVO> getByPage(int page) {
		int size = 10;
		Cursor cursor = dbHelper.query(tableName, COLUMNS, null, null, null,
				null, null, " " + (page - 1) * size + "," + size);
		return getItem(cursor);
	}

	private ArrayList<ZhuoQuanVO> getItem(Cursor cursor) {
		if (cursor.getCount() == 0) {
			return new ArrayList<ZhuoQuanVO>();
		}
		Map<String, String> typeMap = new HashMap<String, String>();
		Map<String, ArrayList<QuanVO>> map = new HashMap<String, ArrayList<QuanVO>>();
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			ZhuoQuanVO item = getByCursor(cursor);
			String typeid = item.getTypeid();
			String type = item.getType();
			ArrayList<QuanVO> quans = map.get(typeid);
			if (quans == null) {
				quans = new ArrayList<QuanVO>();
				typeMap.put(typeid, type);
			}
			quans.addAll(item.getGroups());
			item.setGroups(quans);
			map.put(typeid, quans);
			cursor.moveToNext();
		}
		cursor.close();
		ArrayList<ZhuoQuanVO> li = new ArrayList<ZhuoQuanVO>();
		for (String item : typeMap.keySet()) {
			ZhuoQuanVO zhuoQuan = new ZhuoQuanVO();
			zhuoQuan.setTypeid(item);
			zhuoQuan.setType(typeMap.get(item));
			zhuoQuan.setGroups(map.get(item));
			li.add(zhuoQuan);
		}
		return li;
	}

	public boolean update(ArrayList<ZhuoQuanVO> items) {
		try {
			deleteAll();
			for (ZhuoQuanVO item : items) {
				ArrayList<QuanVO> quans = (ArrayList<QuanVO>) item.getGroups();
				String type = item.getType();
				String typeid = item.getTypeid();
				for (QuanVO quan : quans) {
					if (!checkExist(quan.getGroupid(), typeid)) {
						insert(type, typeid, quan);
					} else {
						mFacade.add(quan);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public long insert(String type, String typeid, QuanVO quan) {
		mFacade.add(quan);
		return dbHelper.insert(tableName, null,
				getFullValues(type, typeid, quan));
	}

	public int deleteAll() {
		return dbHelper.deleteAll(tableName);
	}

	private ZhuoQuanVO getByCursor(Cursor cursor) {
		ZhuoQuanVO item = new ZhuoQuanVO();
		item.setType(cursor.getString(0));
		item.setTypeid(cursor.getString(1));
		ArrayList<QuanVO> groups = new ArrayList<QuanVO>();
		groups.add(mFacade.getById(cursor.getString(2)));
		item.setGroups(groups);
		return item;
	}

	private ContentValues getFullValues(String type, String typeid, QuanVO quan) {
		ContentValues values = new ContentValues();
		values.put("type", type);
		values.put("typeid", typeid);
		values.put("groupid", quan.getGroupid());
		return values;
	}
}
