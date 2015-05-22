package com.cpstudio.zhuojiaren.facade;

import java.util.ArrayList;
import java.util.List;

import com.cpstudio.zhuojiaren.helper.DatabaseHelper;
import com.cpstudio.zhuojiaren.helper.ResHelper;
import com.cpstudio.zhuojiaren.model.QuanVO;
import com.cpstudio.zhuojiaren.model.UserVO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

public class QuanFacade {
	private final DatabaseHelper dbHelper;
	private static final String SQL_TABLE = "QUANLIST";
	private static final String[] COLUMNS = { "groupid", "gname", "gheader",
			"gproperty", "gintro", "createtime", "membersnum", "membersmax",
			"lastbroadcast", "lastmsgtime", "alert", "founderid", "membertype",
			"managersids", "membersids", "params1", "params2", "params3",
			"params4", "params5" };
	private Context mContext;

	public QuanFacade(Context context) {
		mContext = context;
		String userid = ResHelper.getInstance(context).getUserid();
		dbHelper = new DatabaseHelper(context, userid);
	}

	@Override
	public void finalize() throws Throwable {
		super.finalize();
	}

	public QuanVO getById(String id) {
		Cursor cursor = dbHelper.query(SQL_TABLE, COLUMNS, "groupid = ?",
				new String[] { id }, null, null, null);
		if (cursor.getCount() != 1) {
			return null;
		}
		cursor.moveToFirst();
		QuanVO quan = getByCursor(cursor);
		cursor.close();
		return quan;
	}

	public ArrayList<QuanVO> getAll() {
		Cursor cursor = dbHelper.query(SQL_TABLE, COLUMNS, null, null, null,
				null, null);
		if (cursor.getCount() == 0) {
			return new ArrayList<QuanVO>();
		}
		ArrayList<QuanVO> li = new ArrayList<QuanVO>(cursor.getCount());
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			li.add(getByCursor(cursor));
			cursor.moveToNext();
		}
		cursor.close();
		return li;
	}

	public int update(QuanVO quan) {
		return dbHelper.update(SQL_TABLE, getFullValues(quan), "groupid = ?",
				new String[] { quan.getGroupid() });
	}

	public long insert(QuanVO quan) {
		return dbHelper.insert(SQL_TABLE, null, getFullValues(quan));
	}

	public String add(QuanVO item) {
		String quanid = "";
		if (null != item && item.getGroupid() != null) {
			quanid = item.getGroupid();
			QuanVO quandb = getById(quanid);
			if (quandb == null) {
				insert(item);
			}
		}
		return quanid;
	}

	public long saveOrUpdate(QuanVO item) {
		String id = item.getGroupid();
		if (getById(id) != null) {
			return dbHelper.update(SQL_TABLE, getFullValues(item),
					"groupid = ?", new String[] { item.getGroupid() });
		} else {
			return dbHelper.insert(SQL_TABLE, null, getFullValues(item));
		}
	}

	public int delete(String id) {
		return dbHelper.delete(SQL_TABLE, "groupid = ?", new String[] { id });
	}

	public int deleteAll() {
		return dbHelper.deleteAll(SQL_TABLE);
	}

	private QuanVO getByCursor(Cursor cursor) {
		UserFacade userFacade = new UserFacade(mContext);
		QuanVO item = new QuanVO();
		item.setGroupid(cursor.getString(0));
		item.setGname(cursor.getString(1));
		item.setGheader(cursor.getString(2));
		item.setGproperty(cursor.getString(3));
		item.setGintro(cursor.getString(4));
		item.setCreatetime(cursor.getString(5));
		item.setMembersnum(cursor.getString(6));
		item.setMembersmax(cursor.getString(7));
		item.setLastbroadcast(cursor.getString(8));
		item.setLastmsgtime(cursor.getString(9));
		item.setAlert(cursor.getString(10));
		String founderid = cursor.getString(11);
		item.setFounder(userFacade.getSimpleInfoById(founderid));
		item.setMembertype(cursor.getString(12));
		String managersids = cursor.getString(13);
		ArrayList<UserVO> managers = new ArrayList<UserVO>();
		if (managersids != null && !managersids.equals("")) {
			if (managersids.indexOf(";") != -1) {
				String[] ids = managersids.split(";");
				for (String id : ids) {
					UserVO item2 = userFacade.getSimpleInfoById(id);
					managers.add(item2);
				}
			} else {
				UserVO item2 = userFacade.getSimpleInfoById(managersids);
				managers.add(item2);
			}
		}
		item.setManagers(managers);
		String membersids = cursor.getString(14);
		ArrayList<UserVO> members = new ArrayList<UserVO>();
		if (membersids != null && !membersids.equals("")) {
			if (membersids.indexOf(";") != -1) {
				String[] ids = membersids.split(";");
				for (String id : ids) {
					UserVO item2 = userFacade.getSimpleInfoById(id);
					members.add(item2);
				}
			} else {
				UserVO item2 = userFacade.getSimpleInfoById(membersids);
				members.add(item2);
			}
		}
		item.setMembers(members);
		return item;
	}

	private ContentValues getFullValues(QuanVO item) {
		UserFacade userFacade = new UserFacade(mContext);
		ContentValues values = new ContentValues();
		values.put("groupid", item.getGroupid());
		values.put("gname", item.getGname());
		values.put("gheader", item.getGheader());
		values.put("gproperty", item.getGproperty());
		values.put("gintro", item.getGintro());
		values.put("createtime", item.getCreatetime());
		values.put("membersnum", item.getMembersnum());
		values.put("membersmax", item.getMembersmax());
		values.put("lastbroadcast", item.getLastbroadcast());
		values.put("lastmsgtime", item.getLastmsgtime());
		values.put("alert", item.getAlert());
		UserVO founder = item.getFounder();
		String founderid = userFacade.add(founder);
		values.put("founderid", founderid);
		values.put("membertype", item.getMembertype());
		List<UserVO> managers = item.getManagers();
		String managersids = "";
		if (managers != null && managers.size() > 0) {
			for (UserVO manager : managers) {
				String managerid = userFacade.add(manager);
				managersids += managerid + ";";
			}
			managersids = managersids.substring(0, managersids.length() - 1);
		}
		values.put("managersids", managersids);
		List<UserVO> members = item.getManagers();
		String membersids = "";
		if (members != null && members.size() > 0) {
			for (UserVO member : members) {
				String memberid = userFacade.add(member);
				membersids += memberid + ";";
			}
			membersids = membersids.substring(0, membersids.length() - 1);
		}
		values.put("membersids", membersids);
		return values;
	}
}
