package com.cpstudio.zhuojiaren.facade;

import java.util.ArrayList;
import com.cpstudio.zhuojiaren.helper.DatabaseHelper;
import com.cpstudio.zhuojiaren.helper.ResHelper;
import com.cpstudio.zhuojiaren.model.PicVO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

public class PicFacade {
	private final DatabaseHelper dbHelper;
	private static final String SQL_TABLE = "PICLIST";
	private static final String[] COLUMNS = {
			"id" , "thumburl" , "orgurl" , "desc", "params1", "params2",
			"params3", "params4", "params5" };

	public PicFacade(Context context) {
		String userid = ResHelper.getInstance(context).getUserid();
		dbHelper = new DatabaseHelper(context,userid);
	}

	@Override
	public void finalize() throws Throwable {
		super.finalize();
	}

	public PicVO getById(String id) {
		Cursor cursor = dbHelper.query(SQL_TABLE, COLUMNS, "id = ?",
				new String[] { id },
				null, null, null);
		if (cursor.getCount() != 1) {
			return null;
		}
		cursor.moveToFirst();
		PicVO item = getByCursor(cursor);
		cursor.close();
		return item;
	}

	public ArrayList<PicVO> getAll() {
		Cursor cursor = dbHelper.query(SQL_TABLE, COLUMNS, null, null, null,
				null, null);
		if (cursor.getCount() == 0) {
			return new ArrayList<PicVO>();
		}
		ArrayList<PicVO> li = new ArrayList<PicVO>(cursor.getCount());
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			li.add(getByCursor(cursor));
			cursor.moveToNext();
		}
		cursor.close();
		return li;
	}

	public int update(PicVO item) {
		return dbHelper.update(SQL_TABLE, getFullValues(item), "id = ?",
				new String[] { item.getId() });
	}

	public long insert(PicVO item) {
		return dbHelper.insert(SQL_TABLE, null, getFullValues(item));
	}
	
	public long saveOrUpdate(PicVO item) {
		String id = item.getId();
		if(getById(id) != null){
			return dbHelper.update(SQL_TABLE, getFullValues(item), "id = ?",
					new String[] { item.getId() });
		}else{
			return dbHelper.insert(SQL_TABLE, null, getFullValues(item));
		}
	}

	public int delete(String id) {
		return dbHelper.delete(SQL_TABLE, "id = ?", new String[] { id });
	}

	public int deleteAll() {
		return dbHelper.deleteAll(SQL_TABLE);
	}

	private PicVO getByCursor(Cursor cursor) {
		PicVO item = new PicVO();
		item.setId(cursor.getString(0));
		item.setUrl(cursor.getString(1));
		item.setOrgurl(cursor.getString(2));
		item.setDesc(cursor.getString(3));
		return item;
	}

	private ContentValues getFullValues(PicVO item) {
		ContentValues values = new ContentValues();
		values.put("id", item.getId());
		values.put("thumburl", item.getUrl());
		values.put("orgurl", item.getOrgurl());
		values.put("desc", item.getDesc());
		return values;
	}
}
