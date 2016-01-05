package com.cpstudio.zhuojiaren.facade;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.cpstudio.zhuojiaren.helper.DatabaseHelper;
import com.cpstudio.zhuojiaren.helper.ResHelper;
import com.cpstudio.zhuojiaren.model.QuanVO;
import com.cpstudio.zhuojiaren.model.UserNewVO;

public class UserFacade {
	private final DatabaseHelper dbHelper;
	private static final String SQL_TABLE = "NEWUSERLIST";
	private static final String[] COLUMNS = { "signature", "faith", "dream",
			"position", "company", "birthday", "isPhoneOpen", "constellation",
			"hometown", "hobby", "registerTime", "userType", "city",
			"isBirthdayOpen", "birthdayLunar", "married", "weixin",
			"isEmailOpen", "zodiac", "email", "isWeixinOpen", "name",
			"uheader", "gender", "travelCity", "lastLoginTime", "qq",
			"isQqOpen", "industry", "phone", "userid", "isFree", "role",
			"qrcode", "friendNum", "statusNum" };
	private String myid = "";
	private Context mContext = null;

	public UserFacade(Context context) {
		mContext = context;
		myid = ResHelper.getInstance(context).getUserid();
		dbHelper = new DatabaseHelper(context, myid);
	}

	@Override
	public void finalize() throws Throwable {
		super.finalize();
	}

	public UserNewVO getMySimpleInfo() {
		Cursor cursor = dbHelper.query(SQL_TABLE, COLUMNS, "userid = ?",
				new String[] { myid }, null, null, null);
		if (cursor.getCount() != 1) {
			return null;
		}
		cursor.moveToFirst();
		UserNewVO item = getSimpleInfoByCursor(cursor);
		cursor.close();
		return item;
	}

	public UserNewVO getMyInfo() {
		Cursor cursor = dbHelper.query(SQL_TABLE, COLUMNS, "userid = ?",
				new String[] { myid }, null, null, null);
		if (cursor.getCount() != 1) {
			return null;
		}
		cursor.moveToFirst();
		UserNewVO item = getByCursor(cursor);
		cursor.close();
		return item;
	}

	public UserNewVO getById(String id) {
		Cursor cursor = dbHelper.query(SQL_TABLE, COLUMNS, "userid = ?",
				new String[] { id }, null, null, null);
		if (cursor.getCount() != 1) {
			return null;
		}
		cursor.moveToFirst();
		UserNewVO item = getByCursor(cursor);
		cursor.close();
		return item;
	}

	public UserNewVO getSimpleInfoById(String id) {
		Cursor cursor = dbHelper.query(SQL_TABLE, COLUMNS, "userid = ?",
				new String[] { id }, null, null, null);
		if (cursor.getCount() != 1) {
			return null;
		}
		cursor.moveToFirst();
		UserNewVO item = getSimpleInfoByCursor(cursor);
		cursor.close();
		return item;
	}

	public ArrayList<UserNewVO> getAll() {
		Cursor cursor = dbHelper.query(SQL_TABLE, COLUMNS, null, null, null,
				null, null);
		if (cursor.getCount() == 0) {
			return new ArrayList<UserNewVO>();
		}
		ArrayList<UserNewVO> li = new ArrayList<UserNewVO>(cursor.getCount());
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			li.add(getByCursor(cursor));
			cursor.moveToNext();
		}
		cursor.close();
		return li;
	}

	public ArrayList<UserNewVO> getAllSimple() {
		Cursor cursor = dbHelper.query(SQL_TABLE, COLUMNS, null, null, null,
				null, null);
		if (cursor.getCount() == 0) {
			return new ArrayList<UserNewVO>();
		}
		ArrayList<UserNewVO> li = new ArrayList<UserNewVO>(cursor.getCount());
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			li.add(getSimpleInfoByCursor(cursor));
			cursor.moveToNext();
		}
		cursor.close();
		return li;
	}

	public int update(UserNewVO item) {
		return dbHelper.update(SQL_TABLE, getFullValues(item), "userid = ?",
				new String[] { item.getUserid() });
	}

	public long insert(UserNewVO item) {
		return dbHelper.insert(SQL_TABLE, null, getFullValues(item));
	}

	public long insertSimpleInfo(UserNewVO item) {
		return dbHelper.insert(SQL_TABLE, null, getSimpleValues(item));
	}

	public String add(UserNewVO item) {
		String userid = "";
		if (null != item && item.getUserid() != null) {
			userid = item.getUserid();
			UserNewVO userdb = getSimpleInfoById(userid);
			if (userdb == null) {
				insertSimpleInfo(item);
			}
		}
		return userid;
	}

	//更新用户信息的地方
	public long saveOrUpdate(UserNewVO item) {
		String userid = item.getUserid();
		if (userid != null && getSimpleInfoById(userid) != null) {
			return update(item);
		} else {
			return insert(item);
		}
	}

	public int delete(String id) {
		return dbHelper.delete(SQL_TABLE, "userid = ?", new String[] { id });
	}

	public int deleteAll() {
		return dbHelper.deleteAll(SQL_TABLE);
	}

	private UserNewVO getSimpleInfoByCursor(Cursor cursor) {
		UserNewVO item = new UserNewVO();
		// signature dream position

		item.setUserid(cursor.getString(cursor.getColumnIndexOrThrow("userid")));
		item.setName(cursor.getString(cursor.getColumnIndexOrThrow("name")));
		item.setUheader(cursor.getString(cursor
				.getColumnIndexOrThrow("uheader")));
		item.setCompany(cursor.getString(cursor
				.getColumnIndexOrThrow("company")));
		item.setSignature(cursor.getString(cursor
				.getColumnIndexOrThrow("signature")));
		item.setFriendNum(cursor.getInt(cursor
				.getColumnIndexOrThrow("friendNum")));
		item.setStatusNum(cursor.getInt(cursor
				.getColumnIndexOrThrow("statusNum")));
		item.setGender(cursor.getInt(cursor.getColumnIndexOrThrow("gender")));
		return item;
	}

	private UserNewVO getByCursor(Cursor cursor) {
		QuanFacade quanFacade = new QuanFacade(mContext);
		UserNewVO item = getSimpleInfoByCursor(cursor);
		String familyids = cursor.getString(48);
		String groupsids = cursor.getString(49);
		ArrayList<UserNewVO> familys = new ArrayList<UserNewVO>();
		if (familyids != null && !familyids.equals("")) {
			if (familyids.indexOf(";") != -1) {
				String[] ids = familyids.split(";");
				for (String id : ids) {
					UserNewVO item2 = getSimpleInfoById(id);
					familys.add(item2);
				}
			} else {
				UserNewVO item2 = getSimpleInfoById(familyids);
				familys.add(item2);
			}
		}
		ArrayList<QuanVO> quans = new ArrayList<QuanVO>();
		if (groupsids != null && !groupsids.equals("")) {
			if (groupsids.indexOf(";") != -1) {
				String[] ids = groupsids.split(";");
				for (String id : ids) {
					QuanVO item2 = quanFacade.getById(id);
					quans.add(item2);
				}
			} else {
				QuanVO item2 = quanFacade.getById(groupsids);
				quans.add(item2);
			}
		}
		// item.setPics(pics);
		return item;
	}

	private ContentValues getSimpleValues(UserNewVO item) {
		ContentValues values = new ContentValues();

		values.put("userid", item.getUserid());
		values.put("name", item.getName());
		values.put("uheader", item.getUheader());
		values.put("company", item.getCompany());
		values.put("signature", item.getSignature());
		values.put("friendNum", item.getFriendNum());
		values.put("statusNum", item.getStatusNum());
		values.put("gender", item.getGender());
		return values;
	}

	private ContentValues getFullValues(UserNewVO item) {
		ContentValues values = new ContentValues();

		values.put("userid", item.getUserid());
		values.put("name", item.getName());
		values.put("uheader", item.getUheader());
		values.put("company", item.getCompany());
		values.put("signature", item.getSignature());
		values.put("friendNum", item.getFriendNum());
		values.put("statusNum", item.getStatusNum());
		values.put("gender", item.getGender());
		return values;
	}
}
