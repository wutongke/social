package com.cpstudio.zhuojiaren.facade;

import java.util.ArrayList;
import com.cpstudio.zhuojiaren.helper.DatabaseHelper;
import com.cpstudio.zhuojiaren.helper.ResHelper;
import com.cpstudio.zhuojiaren.model.ProductVO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

public class ProductFacade {
	private final DatabaseHelper dbHelper;
	private static final String SQL_TABLE = "PRODUCTLIST";
	private static final String[] COLUMNS = {
			"id" , "title" , "_desc" , "_value" , "addtime" , "username"
					, "uheader" , "userid" , "params1", "params2", "params3",
			"params4", "params5" };

	public ProductFacade(Context context) {
		String userid = ResHelper.getInstance(context).getUserid();
		dbHelper = new DatabaseHelper(context,userid);
	}

	@Override
	public void finalize() throws Throwable {
		super.finalize();
	}

	public ProductVO getById(String id) {
		Cursor cursor = dbHelper.query(SQL_TABLE, COLUMNS, "id = ?",
				new String[] { id },
				null, null, null);
		if (cursor.getCount() != 1) {
			return null;
		}
		cursor.moveToFirst();
		ProductVO item = getByCursor(cursor);
		cursor.close();
		return item;
	}

	public ArrayList<ProductVO> getAll() {
		Cursor cursor = dbHelper.query(SQL_TABLE, COLUMNS, null, null, null,
				null, null);
		if (cursor.getCount() == 0) {
			return new ArrayList<ProductVO>();
		}
		ArrayList<ProductVO> li = new ArrayList<ProductVO>(cursor.getCount());
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			li.add(getByCursor(cursor));
			cursor.moveToNext();
		}
		cursor.close();
		return li;
	}

	public int update(ProductVO item) {
		return dbHelper.update(SQL_TABLE, getFullValues(item), "id = ?",
				new String[] { item.getId() });
	}

	public long insert(ProductVO item) {
		return dbHelper.insert(SQL_TABLE, null, getFullValues(item));
	}

	public long saveOrUpdate(ProductVO item) {
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
	
	public int deleteByUserid(String id) {
		return dbHelper.delete(SQL_TABLE, "userid = ?", new String[] { id });
	}

	public int deleteAll() {
		return dbHelper.deleteAll(SQL_TABLE);
	}

	private ProductVO getByCursor(Cursor cursor) {
		ProductVO item = new ProductVO();
		item.setId(cursor.getString(0));
		item.setTitle(cursor.getString(1));
		item.set_desc(cursor.getString(2));
		item.set_value(cursor.getString(3));
		item.setAddtime(cursor.getString(4));
		item.setUsername(cursor.getString(5));
		item.setUheader(cursor.getString(6));
		item.setUserid(cursor.getString(7));
		return item;
	}

	private ContentValues getFullValues(ProductVO item) {
		ContentValues values = new ContentValues();
		values.put("id", item.getId());
		values.put("title", item.getTitle());
		values.put("_desc", item.get_desc());
		values.put("_value", item.get_value());
		values.put("addtime", item.getAddtime());
		values.put("username", item.getUsername());
		values.put("uheader", item.getUheader());
		values.put("userid", item.getUserid());
		return values;
	}
}
