package com.cpstudio.zhuojiaren.facade;

import java.util.ArrayList;
import com.cpstudio.zhuojiaren.helper.DatabaseHelper;
import com.cpstudio.zhuojiaren.helper.ResHelper;
import com.cpstudio.zhuojiaren.model.ZhuoInfoVO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

public class InfoFacade {
	private final DatabaseHelper dbHelper;
	private String tableName = null;
	private static final String[] COLUMNS = { "msgid" };
	private ZhuoInfoFacade mFacade;
	public static final String ACTIVELIST = "ACTIVELIST";
	public static final String COLLECTLIST = "COLLECTLIST";
	public static final String NEWSLIST = "NEWSLIST";

	public InfoFacade(Context context, String tableName) {
		this.tableName = tableName;
		mFacade = new ZhuoInfoFacade(context);
		String userid = ResHelper.getInstance(context).getUserid();
		dbHelper = new DatabaseHelper(context, userid);
	}

	@Override
	public void finalize() throws Throwable {
		super.finalize();
	}

	private boolean checkExist(String id) {
		Cursor cursor = dbHelper.query(tableName, COLUMNS, "msgid = ?",
				new String[] { id }, null, null, null);
		if (cursor.getCount() != 1) {
			return false;
		}
		cursor.moveToFirst();
		cursor.close();
		return true;
	}

	public ArrayList<ZhuoInfoVO> getAll() {
		Cursor cursor = dbHelper.query(tableName, COLUMNS, null, null, null,
				null, null);
		if (cursor.getCount() == 0) {
			return new ArrayList<ZhuoInfoVO>();
		}
		ArrayList<ZhuoInfoVO> li = new ArrayList<ZhuoInfoVO>(cursor.getCount());
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			li.add(mFacade.getById(getByCursor(cursor)));
			cursor.moveToNext();
		}
		cursor.close();
		return li;
	}

	public ArrayList<ZhuoInfoVO> getByPage(int page) {
		int size = 10;
		Cursor cursor = dbHelper.query(tableName, COLUMNS, null, null, null,
				null, null, " " + (page - 1) * size + "," + size);
		if (cursor.getCount() == 0) {
			return new ArrayList<ZhuoInfoVO>();
		}
		ArrayList<ZhuoInfoVO> li = new ArrayList<ZhuoInfoVO>(cursor.getCount());
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			li.add(mFacade.getById(getByCursor(cursor)));
			cursor.moveToNext();
		}
		cursor.close();
		return li;
	}

	public boolean update(ArrayList<ZhuoInfoVO> items) {
		try {
			deleteAll();
			for (ZhuoInfoVO item : items) {
				if (!checkExist(item.getMsgid())) {
					insert(item);
				} else {
					mFacade.add(item);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public long insert(ZhuoInfoVO item) {
		mFacade.add(item);
		return dbHelper.insert(tableName, null, getFullValues(item));
	}

	public int delete(String id) {
		return dbHelper.delete(tableName, "msgid = ?", new String[] { id });
	}

	public int deleteAll() {
		return dbHelper.deleteAll(tableName);
	}

	private String getByCursor(Cursor cursor) {
		return cursor.getString(0);
	}

	private ContentValues getFullValues(ZhuoInfoVO item) {
		ContentValues values = new ContentValues();
		values.put("msgid", item.getMsgid());
		return values;
	}
}
