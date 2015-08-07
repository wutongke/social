package com.cpstudio.zhuojiaren.facade;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.cpstudio.zhuojiaren.helper.DatabaseHelper;
import com.cpstudio.zhuojiaren.helper.ResHelper;
import com.cpstudio.zhuojiaren.model.SysMsgVO;
import com.cpstudio.zhuojiaren.model.UserVO;

public class SysMsgFacade {
	private final DatabaseHelper dbHelper;
	private static final String SQL_TABLE = "SYSMSGLIST";
	private static final String[] COLUMNS = { "id", "senderid", "receiverid",
			"groupid", "addtime", "groupname", "type", "content", "isread",
			"userid", "params1", "params2", "params3", "params4", "params5" };
	private Context mContext;

	public SysMsgFacade(Context context) {
		String userid = ResHelper.getInstance(context).getUserid();
		dbHelper = new DatabaseHelper(context, userid);
		mContext = context;
	}

	@Override
	public void finalize() throws Throwable {
		super.finalize();
	}

	public SysMsgVO getById(String id) {
		Cursor cursor = dbHelper.query(SQL_TABLE, COLUMNS, "id = ?",
				new String[] { id }, null, null, null);
		if (cursor.getCount() != 1) {
			return null;
		}
		cursor.moveToFirst();
		SysMsgVO item = getByCursor(cursor);
		cursor.close();
		return item;
	}

	public ArrayList<SysMsgVO> getAll() {
		Cursor cursor = dbHelper.query(SQL_TABLE, COLUMNS, null, null, null,
				null, null);
		return getList(cursor);
	}

	public ArrayList<SysMsgVO> getAllByCondition(String where,
			String[] whereSections, String groupby, String orderby) {
		Cursor cursor = dbHelper.query(SQL_TABLE, COLUMNS, where,
				whereSections, groupby, null, orderby);
		return getList(cursor);
	}

	public ArrayList<SysMsgVO> getUnread() {
		Cursor cursor = dbHelper.query(SQL_TABLE, COLUMNS, "isread=?",
				new String[] { "0" }, null, null, null);
		return getList(cursor);
	}

	public void updateReadState() {
		ArrayList<SysMsgVO> msgs = getUnread();
		for (SysMsgVO msg : msgs) {
			msg.setIsread("1");
			update(msg);
		}
	}

	private ArrayList<SysMsgVO> getList(Cursor cursor) {
		if (cursor.getCount() == 0) {
			return new ArrayList<SysMsgVO>();
		}
		ArrayList<SysMsgVO> li = new ArrayList<SysMsgVO>(cursor.getCount());
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			li.add(getByCursor(cursor));
			cursor.moveToNext();
		}
		cursor.close();
		return li;
	}

	public int update(SysMsgVO item) {
		return dbHelper.update(SQL_TABLE, getFullValues(item), "id = ?",
				new String[] { item.getId() });
	}

	public long insert(SysMsgVO item) {
		return dbHelper.insert(SQL_TABLE, null, getFullValues(item));
	}

	public long saveOrUpdate(SysMsgVO item) {
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

	private SysMsgVO getByCursor(Cursor cursor) {
		UserFacade userFacade = new UserFacade(mContext);
		SysMsgVO item = new SysMsgVO();
		item.setId(cursor.getString(0));
		String senderid = cursor.getString(1);
//		UserVO sender = userFacade.getSimpleInfoById(senderid);
//		item.setSender(sender);
//		String receiverid = cursor.getString(2);
//		UserVO receiver = userFacade.getSimpleInfoById(receiverid);
//		item.setReceiver(receiver);
//		item.setGroupid(cursor.getString(3));
//		item.setAddtime(cursor.getString(4));
//		item.setGroupname(cursor.getString(5));
//		item.setType(cursor.getString(6));
//		item.setContent(cursor.getString(7));
//		item.setIsread(cursor.getString(8));
//		String userid = cursor.getString(9);
//		UserVO user = userFacade.getById(userid);
//		item.setUser(user);
		return item;
	}

	private ContentValues getFullValues(SysMsgVO item) {
		UserFacade userFacade = new UserFacade(mContext);
		ContentValues values = new ContentValues();
		values.put("id", item.getId());
		UserVO sender = item.getSender();
//		String senderid = userFacade.add(sender);
//		values.put("senderid", senderid);
//		UserVO receiver = item.getReceiver();
//		String receiverid = userFacade.add(receiver);
//		values.put("receiverid", receiverid);
//		values.put("groupid", item.getGroupid());
//		values.put("addtime", item.getAddtime());
//		values.put("groupname", item.getGroupname());
//		values.put("type", item.getType());
//		values.put("content", item.getContent());
//		values.put("isread", item.getIsread());
//		UserVO user = item.getUser();
//		String userid = userFacade.add(user);
//		values.put("userid", userid);
		return values;
	}
}
