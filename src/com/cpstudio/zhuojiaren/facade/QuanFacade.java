package com.cpstudio.zhuojiaren.facade;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.cpstudio.zhuojiaren.helper.DatabaseHelper;
import com.cpstudio.zhuojiaren.helper.ResHelper;
import com.cpstudio.zhuojiaren.model.QuanVO;
/**
 * 圈子信息本地缓存管理类，主要用于圈子的详细信息。某些字段未用
 * @author lz
 *
 */
public class QuanFacade {
	private final DatabaseHelper dbHelper;
	private static final String SQL_TABLE = "QUANLIST";
	private static final String[] COLUMNS = { "groupid", "gname", "gheader",
			"gproperty", "gintro", "createtime", "membersnum", "membersmax",
			"lastbroadcast", "lastmsgtime", "alert", "founderid", "membertype",
			"managersids", "membersids", "params1", "params2", "params3",
			"params4", "params5" };

	public QuanFacade(Context context) {
		String userid = ResHelper.getInstance(context).getUserid();
		dbHelper = new DatabaseHelper(context, userid);
	}

	@Override
	public void finalize() throws Throwable {
		super.finalize();
	}

	public QuanVO getById(String id) {
		Cursor cursor = dbHelper.query(SQL_TABLE, COLUMNS, "groupid = ?",
				new String[] { id }, null, null, null);
		if (cursor.getCount() != 1) {
			return null;
		}
		cursor.moveToFirst();
		QuanVO quan = getByCursor(cursor);
		cursor.close();
		return quan;
	}

	public ArrayList<QuanVO> getAll() {
		Cursor cursor = dbHelper.query(SQL_TABLE, COLUMNS, null, null, null,
				null, null);
		if (cursor.getCount() == 0) {
			return new ArrayList<QuanVO>();
		}
		ArrayList<QuanVO> li = new ArrayList<QuanVO>(cursor.getCount());
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			li.add(getByCursor(cursor));
			cursor.moveToNext();
		}
		cursor.close();
		return li;
	}

	public int update(QuanVO quan) {
		return dbHelper.update(SQL_TABLE, getFullValues(quan), "groupid = ?",
				new String[] { quan.getGroupid() });
	}

	public long insert(QuanVO quan) {
		return dbHelper.insert(SQL_TABLE, null, getFullValues(quan));
	}

	public String add(QuanVO item) {
		String quanid = "";
		if (null != item && item.getGroupid() != null) {
			quanid = item.getGroupid();
			QuanVO quandb = getById(quanid);
			if (quandb == null) {
				insert(item);
			}
		}
		return quanid;
	}

	public long saveOrUpdate(QuanVO item) {
		String id = item.getGroupid();
		if (getById(id) != null) {
			return dbHelper.update(SQL_TABLE, getFullValues(item),
					"groupid = ?", new String[] { item.getGroupid() });
		} else {
			return dbHelper.insert(SQL_TABLE, null, getFullValues(item));
		}
	}

	public int delete(String id) {
		return dbHelper.delete(SQL_TABLE, "groupid = ?", new String[] { id });
	}

	public int deleteAll() {
		return dbHelper.deleteAll(SQL_TABLE);
	}

	private QuanVO getByCursor(Cursor cursor) {
		QuanVO item = new QuanVO();
		item.setGroupid(cursor.getString(0));
		item.setGname(cursor.getString(1));
		item.setGheader(cursor.getString(2));
		return item;
	}

	private ContentValues getFullValues(QuanVO item) {
		ContentValues values = new ContentValues();
		values.put("groupid", item.getGroupid());
		values.put("gname", item.getGname());
		values.put("gheader", item.getGheader());
		return values;
	}
}
