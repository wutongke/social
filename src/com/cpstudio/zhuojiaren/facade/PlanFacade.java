package com.cpstudio.zhuojiaren.facade;

import java.util.ArrayList;
import com.cpstudio.zhuojiaren.helper.DatabaseHelper;
import com.cpstudio.zhuojiaren.helper.ResHelper;
import com.cpstudio.zhuojiaren.model.PlanVO;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

public class PlanFacade {
	private final DatabaseHelper dbHelper;
	private static final String SQL_TABLE = "PLANLIST";
	private static final String[] COLUMNS = { "id" , "content" , "addtime",
			"params1", "params2", "params3", "params4", "params5" };

	public PlanFacade(Context context) {
		String userid = ResHelper.getInstance(context).getUserid();
		dbHelper = new DatabaseHelper(context,userid);
	}

	@Override
	public void finalize() throws Throwable {
		super.finalize();
	}

	public PlanVO getById(String id) {
		Cursor cursor = dbHelper.query(SQL_TABLE, COLUMNS, "id = ?",
				new String[] { id },
				null, null, null);
		if (cursor.getCount() != 1) {
			return null;
		}
		cursor.moveToFirst();
		PlanVO item = getByCursor(cursor);
		cursor.close();
		return item;
	}

	public ArrayList<PlanVO> getAll() {
		Cursor cursor = dbHelper.query(SQL_TABLE, COLUMNS, null, null, null,
				null, null);
		if (cursor.getCount() == 0) {
			return new ArrayList<PlanVO>();
		}
		ArrayList<PlanVO> li = new ArrayList<PlanVO>(cursor.getCount());
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			li.add(getByCursor(cursor));
			cursor.moveToNext();
		}
		cursor.close();
		return li;
	}

	public int update(PlanVO item) {
		return dbHelper.update(SQL_TABLE, getFullValues(item), "id = ?",
				new String[] { item.getId() });
	}

	public long insert(PlanVO item) {
		return dbHelper.insert(SQL_TABLE, null, getFullValues(item));
	}

	public long saveOrUpdate(PlanVO item) {
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

	private PlanVO getByCursor(Cursor cursor) {
		PlanVO item = new PlanVO();
		item.setId(cursor.getString(0));
		item.setContent(cursor.getString(1));
		item.setAddtime(cursor.getString(2));
		return item;
	}

	private ContentValues getFullValues(PlanVO item) {
		ContentValues values = new ContentValues();
		values.put("id", item.getId());
		values.put("content", item.getContent());
		values.put("addtime", item.getAddtime());
		return values;
	}
}
