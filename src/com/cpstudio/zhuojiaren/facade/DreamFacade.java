package com.cpstudio.zhuojiaren.facade;

import java.util.ArrayList;
import com.cpstudio.zhuojiaren.helper.DatabaseHelper;
import com.cpstudio.zhuojiaren.helper.ResHelper;
import com.cpstudio.zhuojiaren.model.DreamVO;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

public class DreamFacade {
	private final DatabaseHelper dbHelper;
	private static final String SQL_TABLE = "DREAMLIST";
	private static final String[] COLUMNS = { "id", "userid", "dream",
			"addtime", "params1", "params2", "params3", "params4", "params5" };

	public DreamFacade(Context context) {
		String userid = ResHelper.getInstance(context).getUserid();
		dbHelper = new DatabaseHelper(context, userid);
	}

	@Override
	public void finalize() throws Throwable {
		super.finalize();
	}

	public DreamVO getById(String id) {
		Cursor cursor = dbHelper.query(SQL_TABLE, COLUMNS, "id = ?",
				new String[] { id }, null, null, null);
		if (cursor.getCount() != 1) {
			return null;
		}
		cursor.moveToFirst();
		DreamVO item = getByCursor(cursor);
		cursor.close();
		return item;
	}

	public ArrayList<DreamVO> getAll() {
		Cursor cursor = dbHelper.query(SQL_TABLE, COLUMNS, null, null, null,
				null, null);
		if (cursor.getCount() == 0) {
			return new ArrayList<DreamVO>();
		}
		ArrayList<DreamVO> li = new ArrayList<DreamVO>(cursor.getCount());
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			li.add(getByCursor(cursor));
			cursor.moveToNext();
		}
		cursor.close();
		return li;
	}

	public int update(DreamVO item) {
		return dbHelper.update(SQL_TABLE, getFullValues(item), "id = ?",
				new String[] { item.getId() });
	}

	public long insert(DreamVO item) {
		return dbHelper.insert(SQL_TABLE, null, getFullValues(item));
	}

	public long saveOrUpdate(DreamVO item) {
		String id = item.getId();
		if (getById(id) != null) {
			return dbHelper.update(SQL_TABLE, getFullValues(item), "id = ?",
					new String[] { item.getId() });
		} else {
			return dbHelper.insert(SQL_TABLE, null, getFullValues(item));
		}
	}

	public int delete(String id) {
		return dbHelper.delete(SQL_TABLE, "id = ?", new String[] { id });
	}

	public int deleteAll() {
		return dbHelper.deleteAll(SQL_TABLE);
	}

	private DreamVO getByCursor(Cursor cursor) {
		DreamVO item = new DreamVO();
		item.setId(cursor.getString(0));
		item.setUserid(cursor.getString(1));
		item.setDream(cursor.getString(2));
		item.setAddtime(cursor.getString(3));
		return item;
	}

	private ContentValues getFullValues(DreamVO item) {
		ContentValues values = new ContentValues();
		values.put("id", item.getId());
		values.put("userid", item.getUserid());
		values.put("dream", item.getDream());
		values.put("addtime", item.getAddtime());
		return values;
	}
}
