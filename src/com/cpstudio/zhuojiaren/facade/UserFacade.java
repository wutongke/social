package com.cpstudio.zhuojiaren.facade;

import java.util.ArrayList;
import java.util.List;
import com.cpstudio.zhuojiaren.helper.DatabaseHelper;
import com.cpstudio.zhuojiaren.helper.ResHelper;
import com.cpstudio.zhuojiaren.model.DreamVO;
import com.cpstudio.zhuojiaren.model.PicVO;
import com.cpstudio.zhuojiaren.model.ProductVO;
import com.cpstudio.zhuojiaren.model.QuanVO;
import com.cpstudio.zhuojiaren.model.UserNewVO;
import com.cpstudio.zhuojiaren.model.ZhuoInfoVO;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

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

	public long saveOrUpdate(UserNewVO item) {
		String userid = item.getUserid();
		if (getSimpleInfoById(userid) != null) {
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
		String productids = cursor.getString(47);
		String familyids = cursor.getString(48);
		String groupsids = cursor.getString(49);
		String growthids = cursor.getString(50);
		String dreamids = cursor.getString(51);
		String picsids = cursor.getString(52);
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
		// ProductFacade productFacade = new ProductFacade(mContext);
		// QuanFacade quanFacade = new QuanFacade(mContext);
		// ZhuoInfoFacade zhuoInfoFacade = new ZhuoInfoFacade(mContext);
		// DreamFacade dreamFacade = new DreamFacade(mContext);
		// PicFacade picFacade = new PicFacade(mContext);

		// values.put("userid", item.getUserid());
		// values.put("userpwd", item.getUserid());
		// values.put("username", item.getUsername());
		// values.put("uheader", item.getUheader());
		// values.put("sex", item.getSex());
		// values.put("hometown", item.getHometown());
		// values.put("travel_cities", item.getTravelCities());
		// values.put("birthday", item.getBirthday());
		// values.put("birthday_type", item.getBirthdayType());
		// values.put("constellation", item.getConstellation());
		// values.put("maxim", item.getMaxim());
		// values.put("hobby", item.getHobby());
		// values.put("company", item.getCompany());
		// values.put("email", item.getEmail());
		// values.put("learn_exp", item.getLearn_exp());
		// values.put("website", item.getWebsite());
		// values.put("join_zhuo_date", item.getJoinZhuoDate());
		// values.put("pinyin", item.getPinyin());
		// values.put("startletter", item.getStartletter());
		// values.put("level", item.getLevel());
		// values.put("productotal", item.getProductotal());
		// values.put("familytotal", item.getFamilytotal());
		// values.put("grouptotal", item.getGrouptotal());
		// values.put("isfollow", item.getIsfollow());
		// values.put("isemailopen", item.getIsemailopen());
		// values.put("isphoneopen", item.getIsphoneopen());
		// values.put("isworking", item.getIsworking());
		// values.put("isisentrepreneurship", item.getIsisentrepreneurship());
		// values.put("ismarry", item.getIsmarry());
		// values.put("offertotal", item.getOffertotal());
		// values.put("lastoffer", item.getLastoffer());
		// values.put("lastdemand", item.getLastdemand());
		// values.put("id", item.getId());
		// values.put("classissure", item.getClassissure());
		// values.put("isbirthdayopen", item.getIsbirthdayopen());
		// values.put("activenum", item.getActivenum());
		// values.put("fannum", item.getFannum());
		// values.put("follownum", item.getFollownum());
		// values.put("age", item.getAge());
		// values.put("isread", item.getIsread());
		// values.put("jifen", item.getJifen());
		// values.put("phone", item.getPhone());
		// values.put("mycustomer", item.getMycustomer());
		// values.put("post", item.getPost());
		// values.put("industry", item.getIndustry());
		// values.put("city", item.getCity());
		// values.put("isalert", item.getIsalert());
		// String productids = "";
		// List<ProductVO> productVOs = item.getProduct();
		// productFacade.deleteByUserid(item.getUserid());
		// if (null != productVOs && productVOs.size() > 0) {
		// for (int i = 0; i < productVOs.size(); i++) {
		// productVOs.get(i).setUserid(item.getUserid());
		// productVOs.get(i).setId(System.currentTimeMillis() + "");
		// productFacade.insert(productVOs.get(i));
		// productids += productVOs.get(i).getId() + ";";
		// }
		// productids = productids.substring(0, productids.length() - 1);
		// }
		// values.put("productids", productids);
		// String familyids = "";
		// List<UserNewVO> UserNewVOs = item.getFamily();
		// if (null != UserNewVOs && UserNewVOs.size() > 0) {
		// for (UserNewVO family : UserNewVOs) {
		// String familyid = add(family);
		// familyids += familyid + ";";
		// }
		// familyids = familyids.substring(0, familyids.length() - 1);
		// }
		// values.put("familyids", familyids);
		// String groupsids = "";
		// List<QuanVO> quanVOs = item.getGroups();
		// if (null != quanVOs && quanVOs.size() > 0) {
		// for (QuanVO quan : quanVOs) {
		// String quanid = quanFacade.add(quan);
		// groupsids += quanid + ";";
		// }
		// groupsids = groupsids.substring(0, groupsids.length() - 1);
		// }
		// values.put("groupsids", groupsids);
		// String growthids = "";
		// List<ZhuoInfoVO> zhuoInfoVOs = item.getGrowth();
		// if (null != zhuoInfoVOs && zhuoInfoVOs.size() > 0) {
		// for (int i = 0; i < zhuoInfoVOs.size(); i++) {
		// ZhuoInfoVO info = zhuoInfoVOs.get(i);
		// info.setUser(item);
		// zhuoInfoFacade.add(info);
		// growthids += zhuoInfoVOs.get(i).getMsgid() + ";";
		// }
		// growthids = growthids.substring(0, growthids.length() - 1);
		// }
		// values.put("growthids", growthids);
		// String dreamids = "";
		// List<DreamVO> dreamVOs = item.getDream();
		// if (null != dreamVOs && dreamVOs.size() > 0) {
		// for (int i = 0; i < dreamVOs.size(); i++) {
		// dreamFacade.saveOrUpdate(dreamVOs.get(i));
		// dreamids += dreamVOs.get(i).getId() + ";";
		// }
		// dreamids = dreamids.substring(0, dreamids.length() - 1);
		// }
		// values.put("dreamids", dreamids);
		// String picsids = "";
		// List<PicVO> picVOs = item.getPics();
		// if (null != picVOs && picVOs.size() > 0) {
		// for (int i = 0; i < picVOs.size(); i++) {
		// picFacade.saveOrUpdate(picVOs.get(i));
		// picsids += picVOs.get(i).getId() + ";";
		// }
		// picsids = picsids.substring(0, picsids.length() - 1);
		// }
		// values.put("picsids", picsids);
		return values;
	}
}
