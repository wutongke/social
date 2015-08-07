package com.cpstudio.zhuojiaren.facade;

import java.util.ArrayList;
import com.cpstudio.zhuojiaren.helper.DatabaseHelper;
import com.cpstudio.zhuojiaren.helper.ResHelper;
import com.cpstudio.zhuojiaren.model.UserVO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

public class RelateFacade {
	private final DatabaseHelper dbHelper;
	private String tableName = null;
	private static final String[] COLUMNS = { "userid" };
	private UserFacade mFacade;
	public static final String CONTRACTLIST = "CONTRACTLIST";
	public static final String FANLIST = "FANLIST";
	public static final String FOLLOWLIST = "FOLLOWLIST";
	
	public RelateFacade(Context context, String tableName) {
		this.tableName = tableName;
		mFacade = new UserFacade(context);
		String userid = ResHelper.getInstance(context).getUserid();
		dbHelper = new DatabaseHelper(context, userid);
	}

	@Override
	public void finalize() throws Throwable {
		super.finalize();
	}

	private boolean checkExist(String id) {
		Cursor cursor = dbHelper.query(tableName, COLUMNS, "userid = ?",
				new String[] { id }, null, null, null);
		if (cursor.getCount() != 1) {
			return false;
		}
		cursor.moveToFirst();
		cursor.close();
		return true;
	}

	public ArrayList<UserVO> getAll() {
		Cursor cursor = dbHelper.query(tableName, COLUMNS, null, null, null,
				null, null);
		if (cursor.getCount() == 0) {
			return new ArrayList<UserVO>();
		}
		ArrayList<UserVO> li = new ArrayList<UserVO>(cursor.getCount());
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
//			li.add(mFacade.getSimpleInfoById(getByCursor(cursor)));
//			cursor.moveToNext();
		}
		cursor.close();
		return li;
	}

	public boolean update(ArrayList<UserVO> items) {
		try {
			deleteAll();
			for (UserVO item : items) {
				if (!checkExist(item.getUserid())) {
					insert(item);
				} else {
//					mFacade.add(item);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public long insert(UserVO item) {
//		mFacade.add(item);
		return dbHelper.insert(tableName, null, getFullValues(item));
	}

	public int delete(String id) {
		return dbHelper.delete(tableName, "userid = ?", new String[] { id });
	}

	public int deleteAll() {
		return dbHelper.deleteAll(tableName);
	}

	private String getByCursor(Cursor cursor) {
		return cursor.getString(0);
	}

	private ContentValues getFullValues(UserVO item) {
		ContentValues values = new ContentValues();
		values.put("userid", item.getUserid());
		return values;
	}
}
