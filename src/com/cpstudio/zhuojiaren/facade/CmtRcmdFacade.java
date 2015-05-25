package com.cpstudio.zhuojiaren.facade;

import java.util.ArrayList;
import com.cpstudio.zhuojiaren.helper.DatabaseHelper;
import com.cpstudio.zhuojiaren.helper.ResHelper;
import com.cpstudio.zhuojiaren.model.CmtRcmdVO;
import com.cpstudio.zhuojiaren.model.UserVO;
import com.cpstudio.zhuojiaren.model.ZhuoInfoVO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

public class CmtRcmdFacade {
	private final DatabaseHelper dbHelper;
	private static final String SQL_TABLE = "CMTRCMDLIST";
	private static final String[] COLUMNS = { "id", "senderid", "receiverid",
			"content", "orginid", "isread", "addtime", "params1", "params2",
			"params3", "params4", "params5" };
	private Context mContext;

	public CmtRcmdFacade(Context context) {
		mContext = context;
		String userid = ResHelper.getInstance(context).getUserid();
		dbHelper = new DatabaseHelper(context, userid);
	}

	@Override
	public void finalize() throws Throwable {
		super.finalize();
	}

	public CmtRcmdVO getById(String id) {
		Cursor cursor = dbHelper.query(SQL_TABLE, COLUMNS, "id = ?",
				new String[] { id }, null, null, null);
		if (cursor.getCount() != 1) {
			return null;
		}
		cursor.moveToFirst();
		CmtRcmdVO item = getByCursor(cursor);
		cursor.close();
		return item;
	}

	public ArrayList<CmtRcmdVO> getAll() {
		Cursor cursor = dbHelper.query(SQL_TABLE, COLUMNS, null, null, null,
				null, null);
		return getList(cursor);
	}

	public ArrayList<CmtRcmdVO> getAllByCondition(String where,
			String[] whereSections, String groupby, String orderby) {
		Cursor cursor = dbHelper.query(SQL_TABLE, COLUMNS, where,
				whereSections, groupby, null, orderby);
		return getList(cursor);
	}

	public ArrayList<CmtRcmdVO> getUnread() {
		Cursor cursor = dbHelper.query(SQL_TABLE, COLUMNS, "isread=?",
				new String[] { "0" }, null, null, null);
		return getList(cursor);
	}

	public void updateReadState() {
		ArrayList<CmtRcmdVO> msgs = getUnread();
		for (CmtRcmdVO msg : msgs) {
			msg.setIsread("1");
			update(msg);
		}
	}

	private ArrayList<CmtRcmdVO> getList(Cursor cursor) {
		if (cursor.getCount() == 0) {
			return new ArrayList<CmtRcmdVO>();
		}
		ArrayList<CmtRcmdVO> li = new ArrayList<CmtRcmdVO>(cursor.getCount());
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			li.add(getByCursor(cursor));
			cursor.moveToNext();
		}
		cursor.close();
		return li;
	}

	public int update(CmtRcmdVO item) {
		return dbHelper.update(SQL_TABLE, getFullValues(item), "id = ?",
				new String[] { item.getId() });
	}

	public long insert(CmtRcmdVO item) {
		return dbHelper.insert(SQL_TABLE, null, getFullValues(item));
	}

	public long saveOrUpdate(CmtRcmdVO item) {
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

	private CmtRcmdVO getByCursor(Cursor cursor) {
		UserFacade userFacade = new UserFacade(mContext);
		ZhuoInfoFacade zhuoInfoFacade = new ZhuoInfoFacade(mContext);
		CmtRcmdVO item = new CmtRcmdVO();
		item.setId(cursor.getString(0));
		String senderid = cursor.getString(1);
		UserVO sender = userFacade.getSimpleInfoById(senderid);
		item.setSender(sender);
		String receiverid = cursor.getString(2);
		if (null != receiverid && !receiverid.equals("")) {
			UserVO receiver = userFacade.getSimpleInfoById(receiverid);
			item.setReceiver(receiver);
		}
		item.setContent(cursor.getString(3));
		String msgid = cursor.getString(4);
		ZhuoInfoVO zhuoInfo = zhuoInfoFacade.getById(msgid);
		item.setOrgin(zhuoInfo);
		item.setIsread(cursor.getString(5));
		item.setAddtime(cursor.getString(6));
		return item;
	}

	private ContentValues getFullValues(CmtRcmdVO item) {
		UserFacade userFacade = new UserFacade(mContext);
		ZhuoInfoFacade zhuoInfoFacade = new ZhuoInfoFacade(mContext);
		ContentValues values = new ContentValues();
		values.put("id", item.getId());
		UserVO sender = item.getSender();
		String senderid = userFacade.add(sender);
		values.put("senderid", senderid);
		UserVO receiver = item.getReceiver();
		String receiverid = userFacade.add(receiver);
		values.put("receiverid", receiverid);
		values.put("content", item.getContent());
		ZhuoInfoVO zhuoInfo = item.getOrgin();
		String orginid = zhuoInfo.getMsgid();
		zhuoInfoFacade.saveOrUpdate(zhuoInfo);
		values.put("orginid", orginid);
		values.put("isread", item.getIsread());
		values.put("addtime", item.getAddtime());
		return values;
	}
}
