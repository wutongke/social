package com.cpstudio.zhuojiaren.facade;

import java.util.ArrayList;
import com.cpstudio.zhuojiaren.helper.DatabaseHelper;
import com.cpstudio.zhuojiaren.helper.ResHelper;
import com.cpstudio.zhuojiaren.model.ZhuoInfoVO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

public class UserInfoFacade {
	private final DatabaseHelper dbHelper;
	private String tableName = null;
	private static final String[] COLUMNS = { "msgid", "userid" };
	private ZhuoInfoFacade mFacade;
	private String mUid = null;
	public static final String USERDAILY = "USERDAILY";

	public UserInfoFacade(Context context, String tableName, String uid) {
		this.tableName = tableName;
		this.mUid = uid;
		mFacade = new ZhuoInfoFacade(context);
		String userid = ResHelper.getInstance(context).getUserid();
		dbHelper = new DatabaseHelper(context, userid);
	}

	@Override
	public void finalize() throws Throwable {
		super.finalize();
	}

	private boolean checkExist(String id) {
		Cursor cursor = dbHelper.query(tableName, COLUMNS,
				"msgid = ? and userid = ?", new String[] { id, mUid }, null,
				null, null);
		if (cursor.getCount() != 1) {
			return false;
		}
		cursor.moveToFirst();
		cursor.close();
		return true;
	}

	public ArrayList<ZhuoInfoVO> getAll() {
		Cursor cursor = dbHelper.query(tableName, COLUMNS, "userid = ?",
				new String[] { mUid }, null, null, null);
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
		Cursor cursor = dbHelper.query(tableName, COLUMNS, "userid = ?",
				new String[] { mUid }, null, null, null, " " + (page - 1)
						* size + "," + size);
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
		return dbHelper.delete(tableName, "msgid = ? and userid = ?",
				new String[] { id, mUid });
	}

	public void deleteAll() {
		dbHelper.execSql("DELETE FROM " + tableName + " WHERE userid = '"
				+ mUid + "'");
	}

	private String getByCursor(Cursor cursor) {
		return cursor.getString(0);
	}

	private ContentValues getFullValues(ZhuoInfoVO item) {
		ContentValues values = new ContentValues();
		values.put("userid", mUid);
		values.put("msgid", item.getMsgid());
		return values;
	}
}
