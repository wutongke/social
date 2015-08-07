package com.cpstudio.zhuojiaren.facade;

import java.util.ArrayList;

import com.cpstudio.zhuojiaren.helper.DatabaseHelper;
import com.cpstudio.zhuojiaren.helper.ResHelper;
import com.cpstudio.zhuojiaren.model.ImQuanVO;
import com.cpstudio.zhuojiaren.model.QuanVO;
import com.cpstudio.zhuojiaren.model.UserVO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

public class ImQuanFacade {
	protected final DatabaseHelper dbHelper;
	protected static final String SQL_TABLE = "IMQUANMSGLIST";
	protected static final String[] COLUMNS = { "id", "senderid", "type",
			"content", "file", "isread", "groupid", "addtime", "savepath",
			"secs", "params1", "params2", "params3", "params4", "params5" };
	private Context mContext;

	public ImQuanFacade(Context context) {
		mContext = context;
		String userid = ResHelper.getInstance(context).getUserid();
		dbHelper = new DatabaseHelper(context, userid);
	}

	@Override
	public void finalize() throws Throwable {
		super.finalize();
	}

	public ImQuanVO getById(String id) {
		Cursor cursor = dbHelper.query(SQL_TABLE, COLUMNS, "id = ?",
				new String[] { id }, null, null, null);
		if (cursor.getCount() != 1) {
			return null;
		}
		cursor.moveToFirst();
		ImQuanVO msg = getByCursor(cursor);
		cursor.close();
		return msg;
	}

	public ArrayList<ImQuanVO> getAll() {
		Cursor cursor = dbHelper.query(SQL_TABLE, COLUMNS, null, null, null,
				null, null);
		return getList(cursor);
	}

	public ArrayList<ImQuanVO> getAllByCondition(String where,
			String[] whereSections, String groupby, String orderby) {
		Cursor cursor = null;
		if (where != null && !where.trim().equals("")) {
			where += " AND isread <> ? ";
			String[] sections = new String[whereSections.length + 1];
			for (int i = 0; i < whereSections.length; i++) {
				sections[i] = whereSections[i];
			}
			sections[whereSections.length] = "3";
			cursor = dbHelper.query(SQL_TABLE, COLUMNS, where, sections,
					groupby, null, orderby);
		} else {
			cursor = dbHelper.query(SQL_TABLE, COLUMNS, "isread <> ?",
					new String[] { "3" }, groupby, null, orderby);
		}
		return getList(cursor);
	}

	public ArrayList<ImQuanVO> getUnread() {
		Cursor cursor = dbHelper.query(SQL_TABLE, COLUMNS, "isread=?",
				new String[] { "0" }, null, null, null);
		return getList(cursor);
	}

	public ArrayList<ImQuanVO> getUnreadById(String id) {
		Cursor cursor = dbHelper.query(SQL_TABLE, COLUMNS,
				"isread=? and groupid=?", new String[] { "0", id }, null, null,
				null);
		return getList(cursor);
	}

	public void updateReadState(String id) {
		ArrayList<ImQuanVO> msgs = getUnreadById(id);
		for (ImQuanVO msg : msgs) {
			msg.setIsread("1");
			update(msg);
		}
	}

	public ArrayList<ImQuanVO> getSending() {
		Cursor cursor = dbHelper.query(SQL_TABLE, COLUMNS, "isread=?",
				new String[] { "4" }, null, null, null);
		return getList(cursor);
	}

	public void updateSendState() {
		ArrayList<ImQuanVO> msgs = getSending();
		for (ImQuanVO msg : msgs) {
			msg.setIsread("2");
			update(msg);
		}
	}

	public void updateSavePath(String id, String path) {
		ImQuanVO msg = getById(id);
		msg.setSavepath(path);
		update(msg);
	}

	public String getGroupid(String id) {
		ImQuanVO msg = getById(id);
		return msg.getGroup().getGroupid();
	}

	public ArrayList<ImQuanVO> getList(Cursor cursor) {
		if (cursor.getCount() == 0) {
			return new ArrayList<ImQuanVO>();
		}
		ArrayList<ImQuanVO> li = new ArrayList<ImQuanVO>(cursor.getCount());
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			ImQuanVO msg = getByCursor(cursor);
			if (null != msg) {
				li.add(msg);
			}
			cursor.moveToNext();
		}
		cursor.close();
		return li;
	}

	public int update(ImQuanVO msg) {
		return dbHelper.update(SQL_TABLE, getFullValues(msg), "id = ?",
				new String[] { msg.getId() });
	}

	public int updateWithNewId(ImQuanVO msg, String id) {
		return dbHelper.update(SQL_TABLE, getFullValues(msg), "id = ?",
				new String[] { id });
	}

	public long insert(ImQuanVO msg) {
		return dbHelper.insert(SQL_TABLE, null, getFullValues(msg));
	}

	public long saveOrUpdate(ImQuanVO item) {
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

	private ImQuanVO getByCursor(Cursor cursor) {
		UserFacade userFacade = new UserFacade(mContext);
		QuanFacade quanFacade = new QuanFacade(mContext);
		ImQuanVO item = new ImQuanVO();
		item.setId(cursor.getString(0));
		String senderid = cursor.getString(1);
		if (senderid == null || senderid.equals("")) {
			return null;
		}
//		UserVO sender = userFacade.getSimpleInfoById(senderid);
//		item.setSender(sender);
//		item.setType(cursor.getString(2));
//		item.setContent(cursor.getString(3));
//		item.setFile(cursor.getString(4));
//		item.setIsread(cursor.getString(5));
//		String groupid = cursor.getString(6);
//		QuanVO group = quanFacade.getById(groupid);
//		item.setGroup(group);
//		item.setAddtime(cursor.getString(7));
//		item.setSavepath(cursor.getString(8));
//		item.setSecs(cursor.getString(9));
		return item;
	}

	private ContentValues getFullValues(ImQuanVO item) {
		UserFacade userFacade = new UserFacade(mContext);
		QuanFacade quanFacade = new QuanFacade(mContext);
		ContentValues values = new ContentValues();
		values.put("id", item.getId());
		UserVO sender = item.getSender();
//		String senderid = userFacade.add(sender);
//		values.put("senderid", senderid);
		values.put("type", item.getType());
		values.put("content", item.getContent());
		values.put("file", item.getFile());
		values.put("isread", item.getIsread());
		String groupid = item.getGroup().getGroupid();
		QuanVO group = quanFacade.getById(groupid);
		if (group == null) {
			group = item.getGroup();
			quanFacade.insert(group);
		}
		values.put("groupid", groupid);
		values.put("addtime", item.getAddtime());
		values.put("savepath", item.getSavepath());
		values.put("secs", item.getSecs());
		return values;
	}
}
