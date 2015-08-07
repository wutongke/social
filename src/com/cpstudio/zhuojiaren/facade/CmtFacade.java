package com.cpstudio.zhuojiaren.facade;

import java.util.ArrayList;
import com.cpstudio.zhuojiaren.helper.DatabaseHelper;
import com.cpstudio.zhuojiaren.helper.ResHelper;
import com.cpstudio.zhuojiaren.model.CmtVO;
import com.cpstudio.zhuojiaren.model.UserVO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

public class CmtFacade {
	private final DatabaseHelper dbHelper;
	private static final String SQL_TABLE = "CMTLIST";
	private static final String[] COLUMNS = { "id", "addtime", "content",
			"userid", "parentid", "msgid", "likecnt", "params1", "params2",
			"params3", "params4", "params5" };
	private Context mContext;

	public CmtFacade(Context context) {
		mContext = context;
		String userid = ResHelper.getInstance(context).getUserid();
		dbHelper = new DatabaseHelper(context, userid);
	}

	@Override
	public void finalize() throws Throwable {
		super.finalize();
	}

	public CmtVO getById(String id) {
		Cursor cursor = dbHelper.query(SQL_TABLE, COLUMNS, "id = ?",
				new String[] { id }, null, null, null);
		if (cursor.getCount() != 1) {
			return null;
		}
		cursor.moveToFirst();
		CmtVO item = getByCursor(cursor);
		cursor.close();
		return item;
	}

	public ArrayList<CmtVO> getAll() {
		Cursor cursor = dbHelper.query(SQL_TABLE, COLUMNS, null, null, null,
				null, null);
		if (cursor.getCount() == 0) {
			return new ArrayList<CmtVO>();
		}
		ArrayList<CmtVO> li = new ArrayList<CmtVO>(cursor.getCount());
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			li.add(getByCursor(cursor));
			cursor.moveToNext();
		}
		cursor.close();
		return li;
	}

	public int update(CmtVO item) {
		return dbHelper.update(SQL_TABLE, getFullValues(item), "id = ?",
				new String[] { item.getId() });
	}

	public long insert(CmtVO item) {
		return dbHelper.insert(SQL_TABLE, null, getFullValues(item));
	}

	public long saveOrUpdate(CmtVO item) {
		String id = item.getMsgid();
		if (getById(id) != null) {
			return dbHelper.update(SQL_TABLE, getFullValues(item), "id = ?",
					new String[] { item.getMsgid() });
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

	private CmtVO getByCursor(Cursor cursor) {
		UserFacade userFacade = new UserFacade(mContext);
		CmtVO item = new CmtVO();
		item.setId(cursor.getString(0));
		item.setAddtime(cursor.getString(1));
		item.setContent(cursor.getString(2));
		String userid = cursor.getString(3);
//		UserVO user = userFacade.getSimpleInfoById(userid);
//		item.setUser(user);
//		item.setParentid(cursor.getString(4));
//		item.setMsgid(cursor.getString(5));
//		item.setLikecnt(cursor.getString(6));
		return item;
	}

	private ContentValues getFullValues(CmtVO item) {
		UserFacade userFacade = new UserFacade(mContext);
		ContentValues values = new ContentValues();
		values.put("id", item.getId());
		values.put("addtime", item.getAddtime());
		values.put("content", item.getContent());
		UserVO user = item.getUser();
//		String userid = userFacade.add(user);
//		values.put("userid", userid);
//		values.put("parentid", item.getParentid());
//		values.put("msgid", item.getMsgid());
//		values.put("likecnt", item.getLikecnt());
		return values;
	}
}
