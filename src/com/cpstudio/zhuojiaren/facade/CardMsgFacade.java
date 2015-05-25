package com.cpstudio.zhuojiaren.facade;

import java.util.ArrayList;
import com.cpstudio.zhuojiaren.helper.DatabaseHelper;
import com.cpstudio.zhuojiaren.helper.ResHelper;
import com.cpstudio.zhuojiaren.model.CardMsgVO;
import com.cpstudio.zhuojiaren.model.UserVO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

public class CardMsgFacade {
	private final DatabaseHelper dbHelper;
	private static final String SQL_TABLE = "CARDMSGLIST";
	private static final String[] COLUMNS = { "id", "senderid", "receiverid",
			"leavemsg", "isopen", "state", "isread", "addtime", "params1",
			"params2", "params3", "params4", "params5" };
	private Context mContext;

	public CardMsgFacade(Context context) {
		mContext = context;
		String userid = ResHelper.getInstance(context).getUserid();
		dbHelper = new DatabaseHelper(context, userid);
	}

	@Override
	public void finalize() throws Throwable {
		super.finalize();
	}

	public CardMsgVO getById(String id) {
		Cursor cursor = dbHelper.query(SQL_TABLE, COLUMNS, "id = ?",
				new String[] { id }, null, null, null);
		if (cursor.getCount() != 1) {
			return null;
		}
		cursor.moveToFirst();
		CardMsgVO item = getByCursor(cursor);
		cursor.close();
		return item;
	}

	public ArrayList<CardMsgVO> getAll() {
		Cursor cursor = dbHelper.query(SQL_TABLE, COLUMNS, null, null, null,
				null, null);
		return getList(cursor);
	}

	public ArrayList<CardMsgVO> getAllByCondition(String where,
			String[] whereSections, String groupby, String orderby) {
		Cursor cursor = dbHelper.query(SQL_TABLE, COLUMNS, where,
				whereSections, groupby, null, orderby);
		return getList(cursor);
	}

	public ArrayList<CardMsgVO> getUnread() {
		Cursor cursor = dbHelper.query(SQL_TABLE, COLUMNS, "isread=?",
				new String[] { "0" }, null, null, null);
		return getList(cursor);
	}

	public void updateReadState() {
		ArrayList<CardMsgVO> msgs = getUnread();
		for (CardMsgVO msg : msgs) {
			msg.setIsread("1");
			update(msg);
		}
	}

	private ArrayList<CardMsgVO> getList(Cursor cursor) {
		if (cursor.getCount() == 0) {
			return new ArrayList<CardMsgVO>();
		}
		ArrayList<CardMsgVO> li = new ArrayList<CardMsgVO>(cursor.getCount());
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			li.add(getByCursor(cursor));
			cursor.moveToNext();
		}
		cursor.close();
		return li;
	}

	public int update(CardMsgVO item) {
		return dbHelper.update(SQL_TABLE, getFullValues(item), "id = ?",
				new String[] { item.getId() });
	}

	public long insert(CardMsgVO item) {
		return dbHelper.insert(SQL_TABLE, null, getFullValues(item));
	}

	public long saveOrUpdate(CardMsgVO item) {
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

	private CardMsgVO getByCursor(Cursor cursor) {
		UserFacade userFacade = new UserFacade(mContext);
		CardMsgVO item = new CardMsgVO();
		item.setId(cursor.getString(0));
		String senderid = cursor.getString(1);
		UserVO sender = userFacade.getSimpleInfoById(senderid);
		item.setSender(sender);
		String receiverid = cursor.getString(2);
		UserVO receiver = userFacade.getSimpleInfoById(receiverid);
		item.setReceiver(receiver);
		item.setLeavemsg(cursor.getString(3));
		item.setIsopen(cursor.getString(4));
		item.setState(cursor.getString(5));
		item.setIsread(cursor.getString(6));
		item.setAddtime(cursor.getString(7));
		return item;
	}

	private ContentValues getFullValues(CardMsgVO item) {
		UserFacade userFacade = new UserFacade(mContext);
		ContentValues values = new ContentValues();
		values.put("id", item.getId());
		UserVO sender = item.getSender();
		String senderid = userFacade.add(sender);
		values.put("senderid", senderid);
		UserVO receiver = item.getReceiver();
		String receiverid = userFacade.add(receiver);
		values.put("receiverid", receiverid);
		values.put("leavemsg", item.getLeavemsg());
		values.put("isopen", item.getIsopen());
		values.put("state", item.getState());
		values.put("isread", item.getIsread());
		values.put("addtime", item.getAddtime());
		return values;
	}
}
