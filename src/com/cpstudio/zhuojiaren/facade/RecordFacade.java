package com.cpstudio.zhuojiaren.facade;

import java.util.ArrayList;
import com.cpstudio.zhuojiaren.helper.DatabaseHelper;
import com.cpstudio.zhuojiaren.helper.ResHelper;
import com.cpstudio.zhuojiaren.model.RecordVO;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

public class RecordFacade {
	protected final DatabaseHelper dbHelper;
	protected static final String SQL_TABLE = "RECORDLIST";
	protected static final String[] COLUMNS = { "id", "path", "name", "size",
			"date", "length", "users", "state", "params1", "params2",
			"params3", "params4", "params5" };

	public RecordFacade(Context context) {
		String userid = ResHelper.getInstance(context).getUserid();
		dbHelper = new DatabaseHelper(context, userid);
	}

	@Override
	public void finalize() throws Throwable {
		super.finalize();
	}

	public RecordVO getById(String id) {
		Cursor cursor = dbHelper.query(SQL_TABLE, COLUMNS, "id = ?",
				new String[] { id }, null, null, null);
		if (cursor.getCount() != 1) {
			return null;
		}
		cursor.moveToFirst();
		RecordVO item = getByCursor(cursor);
		cursor.close();
		return item;
	}
	
	public RecordVO getByPath(String path) {
		Cursor cursor = dbHelper.query(SQL_TABLE, COLUMNS, "path = ?",
				new String[] { path }, null, null, null);
		if (cursor.getCount() != 1) {
			return null;
		}
		cursor.moveToFirst();
		RecordVO item = getByCursor(cursor);
		cursor.close();
		return item;
	}

	public ArrayList<RecordVO> getAll() {
		Cursor cursor = dbHelper.query(SQL_TABLE, COLUMNS, null, null, null,
				null, null);
		return getList(cursor);
	}

	public ArrayList<RecordVO> getAllNotDelete() {
		Cursor cursor = dbHelper.query(SQL_TABLE, COLUMNS, "state <> ?",
				new String[] { "0" }, null, null, null);
		return getList(cursor);
	}

	public ArrayList<RecordVO> getByCondition(String selection,
			String[] selectionArgs, String groupBy, String having,
			String orderBy) {
		Cursor cursor = dbHelper.query(SQL_TABLE, COLUMNS, selection,
				selectionArgs, groupBy, having, orderBy);
		return getList(cursor);
	}

	private ArrayList<RecordVO> getList(Cursor cursor) {
		if (cursor.getCount() == 0) {
			return new ArrayList<RecordVO>();
		}
		ArrayList<RecordVO> li = new ArrayList<RecordVO>(cursor.getCount());
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			li.add(getByCursor(cursor));
			cursor.moveToNext();
		}
		cursor.close();
		return li;
	}

	public int update(RecordVO item) {
		return dbHelper.update(SQL_TABLE, getFullValues(item), "id = ?",
				new String[] { item.getId() });
	}
	
	public int updateWithNewId(RecordVO msg, String id) {
		return dbHelper.update(SQL_TABLE, getFullValues(msg), "id = ?",
				new String[] { id });
	}

	public long insert(RecordVO item) {
		return dbHelper.insert(SQL_TABLE, null, getFullValues(item));
	}

	public int delete(String id) {
		return dbHelper.delete(SQL_TABLE, "id = ?", new String[] { id });
	}

	public int deleteAll() {
		return dbHelper.deleteAll(SQL_TABLE);
	}

	private RecordVO getByCursor(Cursor cursor) {
		RecordVO item = new RecordVO();
		item.setId(cursor.getString(0));
		item.setPath(cursor.getString(1));
		item.setName(cursor.getString(2));
		item.setSize(cursor.getString(3));
		item.setDate(cursor.getString(4));
		item.setLength(cursor.getString(5));
		item.setUsers(cursor.getString(6));
		item.setState(cursor.getString(7));
		return item;
	}

	private ContentValues getFullValues(RecordVO item) {
		ContentValues values = new ContentValues();
		values.put("id", item.getId());
		values.put("path", item.getPath());
		values.put("name", item.getName());
		values.put("size", item.getSize());
		values.put("date", item.getDate());
		values.put("length", item.getLength());
		values.put("users", item.getUsers());
		values.put("state", item.getState());
		return values;
	}

}
