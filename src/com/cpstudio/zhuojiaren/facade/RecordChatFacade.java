package com.cpstudio.zhuojiaren.facade;

import java.util.ArrayList;
import com.cpstudio.zhuojiaren.helper.DatabaseHelper;
import com.cpstudio.zhuojiaren.helper.ResHelper;
import com.cpstudio.zhuojiaren.model.ImMsgVO;
import com.cpstudio.zhuojiaren.model.UserVO;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

public class RecordChatFacade {
	protected final DatabaseHelper dbHelper;
	protected static final String SQL_TABLE = "RECORDCHATLIST";
	protected static final String[] COLUMNS = { "id", "senderid", "receiverid",
			"type", "content", "file", "isread", "groupid", "addtime",
			"savepath", "secs", "params1", "params2", "params3", "params4",
			"params5" };
	private Context mContext;

	public RecordChatFacade(Context context) {
		mContext = context;
		String userid = ResHelper.getInstance(context).getUserid();
		dbHelper = new DatabaseHelper(context, userid);
	}

	@Override
	public void finalize() throws Throwable {
		super.finalize();
	}

	public ImMsgVO getById(String id) {
		Cursor cursor = dbHelper.query(SQL_TABLE, COLUMNS, "id = ?",
				new String[] { id }, null, null, null);
		if (cursor.getCount() != 1) {
			return null;
		}
		cursor.moveToFirst();
		ImMsgVO msg = getByCursor(cursor);
		cursor.close();
		return msg;
	}

	public ArrayList<ImMsgVO> getAll() {
		Cursor cursor = dbHelper.query(SQL_TABLE, COLUMNS, null, null, null,
				null, null);
		return getList(cursor);
	}

	public ArrayList<ImMsgVO> getAllByCondition(String where,
			String[] whereSections, String groupby, String orderby,
			String username) {
		Cursor cursor = dbHelper.query(SQL_TABLE, COLUMNS, where,
				whereSections, groupby, null, orderby);
		return getList(cursor, username);
	}

	public ArrayList<ImMsgVO> getAllByCondition(String where,
			String[] whereSections, String groupby, String orderby) {
		Cursor cursor = dbHelper.query(SQL_TABLE, COLUMNS, where,
				whereSections, groupby, null, orderby);
		return getList(cursor);
	}

	public ArrayList<ImMsgVO> getUnread() {
		Cursor cursor = dbHelper.query(SQL_TABLE, COLUMNS, "isread=?",
				new String[] { "0" }, null, null, null);
		return getList(cursor);
	}

	public ArrayList<ImMsgVO> getUnreadById(String id) {
		Cursor cursor = dbHelper.query(SQL_TABLE, COLUMNS,
				"isread=? and (senderid=? or receiverid=?)", new String[] {
						"0", id, id }, null, null, null);
		return getList(cursor);
	}

	public void updateReadState(String id) {
		ArrayList<ImMsgVO> msgs = getUnreadById(id);
		for (ImMsgVO msg : msgs) {
			msg.setIsread("1");
			update(msg);
		}
	}

	public void updateSavePath(String id, String path) {
		ImMsgVO msg = getById(id);
		msg.setSavepath(path);
		update(msg);
	}

	public String getSenderid(String id) {
		ImMsgVO msg = getById(id);
		return msg.getSender().getUserid();
	}

	private ArrayList<ImMsgVO> getList(Cursor cursor, String username) {
		if (cursor.getCount() == 0) {
			return new ArrayList<ImMsgVO>();
		}
		ArrayList<ImMsgVO> li = new ArrayList<ImMsgVO>();
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			ImMsgVO msg = getByCursor(cursor, username);
			if (null != msg) {
				li.add(msg);
			}
			cursor.moveToNext();
		}
		cursor.close();
		return li;
	}

	private ArrayList<ImMsgVO> getList(Cursor cursor) {
		if (cursor.getCount() == 0) {
			return new ArrayList<ImMsgVO>();
		}
		ArrayList<ImMsgVO> li = new ArrayList<ImMsgVO>(cursor.getCount());
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			li.add(getByCursor(cursor));
			cursor.moveToNext();
		}
		cursor.close();
		return li;
	}

	public int update(ImMsgVO msg) {
		return dbHelper.update(SQL_TABLE, getFullValues(msg), "id = ?",
				new String[] { msg.getId() });
	}

	public long insert(ImMsgVO msg) {
		return dbHelper.insert(SQL_TABLE, null, getFullValues(msg));
	}

	public long saveOrUpdate(ImMsgVO item) {
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

	public int deleteByUserId(String id) {
		return dbHelper.delete(SQL_TABLE, "senderid = ? or receiverid = ? ",
				new String[] { id, id });
	}

	public int deleteAll() {
		return dbHelper.deleteAll(SQL_TABLE);
	}

	private ImMsgVO getByCursor(Cursor cursor, String username) {
		ImMsgVO msg = getByCursor(cursor);
		if (username != null && !username.equals("")) {
			if (!msg.getSender().getUsername().contains(username)
					&& !msg.getReceiver().getUsername().contains(username)) {
				return null;
			}
		}
		return msg;
	}

	private ImMsgVO getByCursor(Cursor cursor) {
		UserFacade userFacade = new UserFacade(mContext);
		ImMsgVO item = new ImMsgVO();
		item.setId(cursor.getString(0));
		String senderid = cursor.getString(1);
//		UserVO sender = userFacade.getSimpleInfoById(senderid);
//		item.setSender(sender);
//		String receiverid = cursor.getString(2);
//		UserVO receiver = userFacade.getSimpleInfoById(receiverid);
//		item.setReceiver(receiver);
		item.setType(cursor.getString(3));
		item.setContent(cursor.getString(4));
		item.setFile(cursor.getString(5));
		item.setIsread(cursor.getString(6));
		item.setGroupid(cursor.getString(7));
		item.setAddtime(cursor.getString(8));
		item.setSavepath(cursor.getString(9));
		item.setSecs(cursor.getString(10));
		return item;
	}

	private ContentValues getFullValues(ImMsgVO item) {
		UserFacade userFacade = new UserFacade(mContext);
		ContentValues values = new ContentValues();
		values.put("id", item.getId());
		UserVO sender = item.getSender();
//		String senderid = userFacade.add(sender);
//		values.put("senderid", senderid);
//		UserVO receiver = item.getReceiver();
//		String receiverid = userFacade.add(receiver);
//		values.put("receiverid", receiverid);
		values.put("type", item.getType());
		values.put("content", item.getContent());
		values.put("file", item.getFile());
		values.put("isread", item.getIsread());
		values.put("groupid", item.getGroupid());
		values.put("addtime", item.getAddtime());
		values.put("savepath", item.getSavepath());
		values.put("secs", item.getSecs());
		return values;
	}
}
