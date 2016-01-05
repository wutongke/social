package com.cpstudio.zhuojiaren.facade;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.cpstudio.zhuojiaren.helper.DatabaseHelper;
import com.cpstudio.zhuojiaren.helper.ResHelper;
import com.cpstudio.zhuojiaren.model.QuanVO;

//注释部分都需要修改，为了
public class GroupFacade {
	private final DatabaseHelper dbHelper;
	private static final String SQL_TABLE = "GROUPLIST";
	private static final String[] COLUMNS = { "groupid", "gname", "gheader",
			"gintro", "gpub", "gtype", "addtime", "memberCount", "topicCount",
			"lastmsgtime", "alert", "userid", "name", "uheader", "city",
			"followpms", "accesspms", "role" };

	public GroupFacade(Context context) {
		String userid = ResHelper.getInstance(context).getUserid();
		dbHelper = new DatabaseHelper(context, userid);
	}

	@Override
	public void finalize() throws Throwable {
		super.finalize();
	}

	public QuanVO getSimpleInfoById(String id) {
		Cursor cursor = dbHelper.query(SQL_TABLE, COLUMNS, "groupid = ?",
				new String[] { id }, null, null, null);
		if (cursor.getCount() != 1) {
			return null;
		}
		cursor.moveToFirst();
		QuanVO quan = getSimpleInfoByCursor(cursor);
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
			li.add(getSimpleInfoByCursor(cursor));
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

	/**
	 * 批量插入,需先保证不冲突主键
	 * 
	 * @param quan
	 * @return
	 */
	public boolean insertAll(List<QuanVO> quanList) {
		List<ContentValues> listq = new ArrayList<ContentValues>();
		for (QuanVO item : quanList)
			listq.add(getFullValues(item));
		return dbHelper.insertAll(SQL_TABLE, null, listq);
	}

	public boolean saveOrUpdateAll(List<QuanVO> quanList) {
		boolean flag = true;
		for (QuanVO item : quanList) {
			if (saveOrUpdate(item) < 1)
				flag = false;
		}
		return flag;
	}

	public String add(QuanVO item) {
		String quanid = "";
		if (null != item && item.getGroupid() != null) {
			quanid = item.getGroupid();
			QuanVO quandb = getSimpleInfoById(quanid);
			if (quandb == null) {
				insert(item);
			}
		}
		return quanid;
	}

	public long saveOrUpdate(QuanVO item) {
		String id = item.getGroupid();
		if (getSimpleInfoById(id) != null) {
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

	/**
	 * 
	 * @param cursor
	 * @return
	 */
	private QuanVO getSimpleInfoByCursor(Cursor cursor) {
		QuanVO item = new QuanVO();
		item.setGroupid(cursor.getString(cursor
				.getColumnIndexOrThrow("groupid")));
		item.setGname(cursor.getString(cursor.getColumnIndexOrThrow("gname")));

		item.setGheader(cursor.getString(cursor
				.getColumnIndexOrThrow("gheader")));
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
