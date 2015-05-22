package com.cpstudio.zhuojiaren.facade;

import java.util.ArrayList;
import java.util.List;

import com.cpstudio.zhuojiaren.helper.DatabaseHelper;
import com.cpstudio.zhuojiaren.helper.ResHelper;
import com.cpstudio.zhuojiaren.model.DreamVO;
import com.cpstudio.zhuojiaren.model.PicVO;
import com.cpstudio.zhuojiaren.model.ProductVO;
import com.cpstudio.zhuojiaren.model.QuanVO;
import com.cpstudio.zhuojiaren.model.UserVO;
import com.cpstudio.zhuojiaren.model.ZhuoInfoVO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

public class UserFacade {
	private final DatabaseHelper dbHelper;
	private static final String SQL_TABLE = "USERLIST";
	private static final String[] COLUMNS = { "userid", "userpwd", "username",
			"uheader", "sex", "hometown", "travel_cities", "birthday",
			"birthday_type", "constellation", "maxim", "hobby", "company",
			"email", "learn_exp", "website", "join_zhuo_date", "pinyin",
			"startletter", "level", "productotal", "familytotal", "grouptotal",
			"isfollow", "isemailopen", "isphoneopen", "isworking",
			"isisentrepreneurship", "ismarry", "offertotal", "lastoffer",
			"lastdemand", "id", "classissure", "isbirthdayopen", "activenum",
			"fannum", "follownum", "age", "isread", "jifen", "phone",
			"mycustomer", "post", "industry", "city", "isalert", "productids",
			"familyids", "groupsids", "growthids", "dreamids", "picsids",
			"params1", "params2", "params3", "params4", "params5" };
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

	public UserVO getMySimpleInfo() {
		Cursor cursor = dbHelper.query(SQL_TABLE, COLUMNS, "userid = ?",
				new String[] { myid }, null, null, null);
		if (cursor.getCount() != 1) {
			return null;
		}
		cursor.moveToFirst();
		UserVO item = getSimpleInfoByCursor(cursor);
		cursor.close();
		return item;
	}

	public UserVO getMyInfo() {
		Cursor cursor = dbHelper.query(SQL_TABLE, COLUMNS, "userid = ?",
				new String[] { myid }, null, null, null);
		if (cursor.getCount() != 1) {
			return null;
		}
		cursor.moveToFirst();
		UserVO item = getByCursor(cursor);
		cursor.close();
		return item;
	}

	public UserVO getById(String id) {
		Cursor cursor = dbHelper.query(SQL_TABLE, COLUMNS, "userid = ?",
				new String[] { id }, null, null, null);
		if (cursor.getCount() != 1) {
			return null;
		}
		cursor.moveToFirst();
		UserVO item = getByCursor(cursor);
		cursor.close();
		return item;
	}

	public UserVO getSimpleInfoById(String id) {
		Cursor cursor = dbHelper.query(SQL_TABLE, COLUMNS, "userid = ?",
				new String[] { id }, null, null, null);
		if (cursor.getCount() != 1) {
			return null;
		}
		cursor.moveToFirst();
		UserVO item = getSimpleInfoByCursor(cursor);
		cursor.close();
		return item;
	}

	public ArrayList<UserVO> getAll() {
		Cursor cursor = dbHelper.query(SQL_TABLE, COLUMNS, null, null, null,
				null, null);
		if (cursor.getCount() == 0) {
			return new ArrayList<UserVO>();
		}
		ArrayList<UserVO> li = new ArrayList<UserVO>(cursor.getCount());
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			li.add(getByCursor(cursor));
			cursor.moveToNext();
		}
		cursor.close();
		return li;
	}

	public ArrayList<UserVO> getAllSimple() {
		Cursor cursor = dbHelper.query(SQL_TABLE, COLUMNS, null, null, null,
				null, null);
		if (cursor.getCount() == 0) {
			return new ArrayList<UserVO>();
		}
		ArrayList<UserVO> li = new ArrayList<UserVO>(cursor.getCount());
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			li.add(getSimpleInfoByCursor(cursor));
			cursor.moveToNext();
		}
		cursor.close();
		return li;
	}

	public int update(UserVO item) {
		return dbHelper.update(SQL_TABLE, getFullValues(item), "userid = ?",
				new String[] { item.getUserid() });
	}

	public long insert(UserVO item) {
		return dbHelper.insert(SQL_TABLE, null, getFullValues(item));
	}
	
	public long insertSimpleInfo(UserVO item) {
		return dbHelper.insert(SQL_TABLE, null, getSimpleValues(item));
	}

	public String add(UserVO item) {
		String userid = "";
		if (null != item && item.getUserid() != null) {
			userid = item.getUserid();
			UserVO userdb = getSimpleInfoById(userid);
			if (userdb == null) {
				insertSimpleInfo(item);
			}
		}
		return userid;
	}

	public long saveOrUpdate(UserVO item) {
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

	private UserVO getSimpleInfoByCursor(Cursor cursor) {
		UserVO item = new UserVO();
		item.setUserid(cursor.getString(0));
		item.setUserpwd(cursor.getString(1));
		item.setUsername(cursor.getString(2));
		item.setUheader(cursor.getString(3));
		item.setSex(cursor.getString(4));
		item.setHometown(cursor.getString(5));
		item.setTravelCities(cursor.getString(6));
		item.setBirthday(cursor.getString(7));
		item.setBirthdayType(cursor.getString(8));
		item.setConstellation(cursor.getString(9));
		item.setMaxim(cursor.getString(10));
		item.setHobby(cursor.getString(11));
		item.setCompany(cursor.getString(12));
		item.setEmail(cursor.getString(13));
		item.setLearn_exp(cursor.getString(14));
		item.setWebsite(cursor.getString(15));
		item.setJoinZhuoDate(cursor.getString(16));
		item.setPinyin(cursor.getString(17));
		item.setStartletter(cursor.getString(18));
		item.setLevel(cursor.getString(19));
		item.setProductotal(cursor.getString(20));
		item.setFamilytotal(cursor.getString(21));
		item.setGrouptotal(cursor.getString(22));
		item.setIsfollow(cursor.getString(23));
		item.setIsemailopen(cursor.getString(24));
		item.setIsphoneopen(cursor.getString(25));
		item.setIsworking(cursor.getString(26));
		item.setIsisentrepreneurship(cursor.getString(27));
		item.setIsmarry(cursor.getString(28));
		item.setOffertotal(cursor.getString(29));
		item.setLastoffer(cursor.getString(30));
		item.setLastdemand(cursor.getString(31));
		item.setId(cursor.getString(32));
		item.setClassissure(cursor.getString(33));
		item.setIsbirthdayopen(cursor.getString(34));
		item.setActivenum(cursor.getString(35));
		item.setFannum(cursor.getString(36));
		item.setFollownum(cursor.getString(37));
		item.setAge(cursor.getString(38));
		item.setIsread(cursor.getString(39));
		item.setJifen(cursor.getString(40));
		item.setPhone(cursor.getString(41));
		item.setMycustomer(cursor.getString(42));
		item.setPost(cursor.getString(43));
		item.setIndustry(cursor.getString(44));
		item.setCity(cursor.getString(45));
		item.setIsalert(cursor.getString(46));
		return item;
	}

	private UserVO getByCursor(Cursor cursor) {
		ProductFacade productFacade = new ProductFacade(mContext);
		QuanFacade quanFacade = new QuanFacade(mContext);
		ZhuoInfoFacade zhuoInfoFacade = new ZhuoInfoFacade(mContext);
		DreamFacade dreamFacade = new DreamFacade(mContext);
		PicFacade picFacade = new PicFacade(mContext);
		UserVO item = getSimpleInfoByCursor(cursor);
		String productids = cursor.getString(47);
		String familyids = cursor.getString(48);
		String groupsids = cursor.getString(49);
		String growthids = cursor.getString(50);
		String dreamids = cursor.getString(51);
		String picsids = cursor.getString(52);
		ArrayList<ProductVO> productVOs = new ArrayList<ProductVO>();
		if (productids != null && !productids.equals("")) {
			if (productids.indexOf(";") != -1) {
				String[] ids = productids.split(";");
				for (String id : ids) {
					ProductVO item2 = productFacade.getById(id);
					productVOs.add(item2);
				}
			} else {
				ProductVO item2 = productFacade.getById(productids);
				productVOs.add(item2);
			}
		}
		item.setProduct(productVOs);
		ArrayList<UserVO> familys = new ArrayList<UserVO>();
		if (familyids != null && !familyids.equals("")) {
			if (familyids.indexOf(";") != -1) {
				String[] ids = familyids.split(";");
				for (String id : ids) {
					UserVO item2 = getSimpleInfoById(id);
					familys.add(item2);
				}
			} else {
				UserVO item2 = getSimpleInfoById(familyids);
				familys.add(item2);
			}
		}
		item.setFamily(familys);
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
		item.setGroups(quans);
		ArrayList<ZhuoInfoVO> zhuoInfos = new ArrayList<ZhuoInfoVO>();
		if (growthids != null && !growthids.equals("")) {
			if (growthids.indexOf(";") != -1) {
				String[] ids = growthids.split(";");
				for (String id : ids) {
					ZhuoInfoVO item2 = zhuoInfoFacade.getById(id);
					zhuoInfos.add(item2);
				}
			} else {
				ZhuoInfoVO item2 = zhuoInfoFacade.getById(growthids);
				zhuoInfos.add(item2);
			}
		}
		item.setGrowth(zhuoInfos);
		ArrayList<DreamVO> dreams = new ArrayList<DreamVO>();
		if (dreamids != null && !dreamids.equals("")) {
			if (dreamids.indexOf(";") != -1) {
				String[] ids = dreamids.split(";");
				for (String id : ids) {
					DreamVO item2 = dreamFacade.getById(id);
					dreams.add(item2);
				}
			} else {
				DreamVO item2 = dreamFacade.getById(dreamids);
				dreams.add(item2);
			}
		}
		item.setDream(dreams);
		ArrayList<PicVO> pics = new ArrayList<PicVO>();
		if (picsids != null && !picsids.equals("")) {
			if (picsids.indexOf(";") != -1) {
				String[] ids = picsids.split(";");
				for (String id : ids) {
					PicVO item2 = picFacade.getById(id);
					pics.add(item2);
				}
			} else {
				PicVO item2 = picFacade.getById(picsids);
				pics.add(item2);
			}
		}
		item.setPics(pics);
		return item;
	}

	private ContentValues getSimpleValues(UserVO item) {
		ContentValues values = new ContentValues();
		values.put("userid", item.getUserid());
		values.put("userpwd", item.getUserid());
		values.put("username", item.getUsername());
		values.put("uheader", item.getUheader());
		values.put("sex", item.getSex());
		values.put("hometown", item.getHometown());
		values.put("travel_cities", item.getTravelCities());
		values.put("birthday", item.getBirthday());
		values.put("birthday_type", item.getBirthdayType());
		values.put("constellation", item.getConstellation());
		values.put("maxim", item.getMaxim());
		values.put("hobby", item.getHobby());
		values.put("company", item.getCompany());
		values.put("email", item.getEmail());
		values.put("learn_exp", item.getLearn_exp());
		values.put("website", item.getWebsite());
		values.put("join_zhuo_date", item.getJoinZhuoDate());
		values.put("pinyin", item.getPinyin());
		values.put("startletter", item.getStartletter());
		values.put("level", item.getLevel());
		values.put("productotal", item.getProductotal());
		values.put("familytotal", item.getFamilytotal());
		values.put("grouptotal", item.getGrouptotal());
		values.put("isfollow", item.getIsfollow());
		values.put("isemailopen", item.getIsemailopen());
		values.put("isphoneopen", item.getIsphoneopen());
		values.put("isworking", item.getIsworking());
		values.put("isisentrepreneurship", item.getIsisentrepreneurship());
		values.put("ismarry", item.getIsmarry());
		values.put("offertotal", item.getOffertotal());
		values.put("lastoffer", item.getLastoffer());
		values.put("lastdemand", item.getLastdemand());
		values.put("id", item.getId());
		values.put("classissure", item.getClassissure());
		values.put("isbirthdayopen", item.getIsbirthdayopen());
		values.put("activenum", item.getActivenum());
		values.put("fannum", item.getFannum());
		values.put("follownum", item.getFollownum());
		values.put("age", item.getAge());
		values.put("isread", item.getIsread());
		values.put("jifen", item.getJifen());
		values.put("phone", item.getPhone());
		values.put("mycustomer", item.getMycustomer());
		values.put("post", item.getPost());
		values.put("industry", item.getIndustry());
		values.put("city", item.getCity());
		values.put("isalert", item.getIsalert());
		values.put("productids", "");
		values.put("familyids", "");
		values.put("groupsids", "");
		values.put("growthids", "");
		values.put("dreamids", "");
		values.put("picsids", "");
		return values;
	}
	
	private ContentValues getFullValues(UserVO item) {
		ProductFacade productFacade = new ProductFacade(mContext);
		QuanFacade quanFacade = new QuanFacade(mContext);
		ZhuoInfoFacade zhuoInfoFacade = new ZhuoInfoFacade(mContext);
		DreamFacade dreamFacade = new DreamFacade(mContext);
		PicFacade picFacade = new PicFacade(mContext);
		ContentValues values = new ContentValues();
		values.put("userid", item.getUserid());
		values.put("userpwd", item.getUserid());
		values.put("username", item.getUsername());
		values.put("uheader", item.getUheader());
		values.put("sex", item.getSex());
		values.put("hometown", item.getHometown());
		values.put("travel_cities", item.getTravelCities());
		values.put("birthday", item.getBirthday());
		values.put("birthday_type", item.getBirthdayType());
		values.put("constellation", item.getConstellation());
		values.put("maxim", item.getMaxim());
		values.put("hobby", item.getHobby());
		values.put("company", item.getCompany());
		values.put("email", item.getEmail());
		values.put("learn_exp", item.getLearn_exp());
		values.put("website", item.getWebsite());
		values.put("join_zhuo_date", item.getJoinZhuoDate());
		values.put("pinyin", item.getPinyin());
		values.put("startletter", item.getStartletter());
		values.put("level", item.getLevel());
		values.put("productotal", item.getProductotal());
		values.put("familytotal", item.getFamilytotal());
		values.put("grouptotal", item.getGrouptotal());
		values.put("isfollow", item.getIsfollow());
		values.put("isemailopen", item.getIsemailopen());
		values.put("isphoneopen", item.getIsphoneopen());
		values.put("isworking", item.getIsworking());
		values.put("isisentrepreneurship", item.getIsisentrepreneurship());
		values.put("ismarry", item.getIsmarry());
		values.put("offertotal", item.getOffertotal());
		values.put("lastoffer", item.getLastoffer());
		values.put("lastdemand", item.getLastdemand());
		values.put("id", item.getId());
		values.put("classissure", item.getClassissure());
		values.put("isbirthdayopen", item.getIsbirthdayopen());
		values.put("activenum", item.getActivenum());
		values.put("fannum", item.getFannum());
		values.put("follownum", item.getFollownum());
		values.put("age", item.getAge());
		values.put("isread", item.getIsread());
		values.put("jifen", item.getJifen());
		values.put("phone", item.getPhone());
		values.put("mycustomer", item.getMycustomer());
		values.put("post", item.getPost());
		values.put("industry", item.getIndustry());
		values.put("city", item.getCity());
		values.put("isalert", item.getIsalert());
		String productids = "";
		List<ProductVO> productVOs = item.getProduct();
		productFacade.deleteByUserid(item.getUserid());
		if (null != productVOs && productVOs.size() > 0) {
			for (int i = 0; i < productVOs.size(); i++) {
				productVOs.get(i).setUserid(item.getUserid());
				productVOs.get(i).setId(System.currentTimeMillis() + "");
				productFacade.insert(productVOs.get(i));
				productids += productVOs.get(i).getId() + ";";
			}
			productids = productids.substring(0, productids.length() - 1);
		}
		values.put("productids", productids);
		String familyids = "";
		List<UserVO> userVOs = item.getFamily();
		if (null != userVOs && userVOs.size() > 0) {
			for (UserVO family : userVOs) {
				String familyid = add(family);
				familyids += familyid + ";";
			}
			familyids = familyids.substring(0, familyids.length() - 1);
		}
		values.put("familyids", familyids);
		String groupsids = "";
		List<QuanVO> quanVOs = item.getGroups();
		if (null != quanVOs && quanVOs.size() > 0) {
			for (QuanVO quan : quanVOs) {
				String quanid = quanFacade.add(quan);
				groupsids += quanid + ";";
			}
			groupsids = groupsids.substring(0, groupsids.length() - 1);
		}
		values.put("groupsids", groupsids);
		String growthids = "";
		List<ZhuoInfoVO> zhuoInfoVOs = item.getGrowth();
		if (null != zhuoInfoVOs && zhuoInfoVOs.size() > 0) {
			for (int i = 0; i < zhuoInfoVOs.size(); i++) {
				ZhuoInfoVO info = zhuoInfoVOs.get(i);
				info.setUser(item);
				zhuoInfoFacade.add(info);
				growthids += zhuoInfoVOs.get(i).getMsgid() + ";";
			}
			growthids = growthids.substring(0, growthids.length() - 1);
		}
		values.put("growthids", growthids);
		String dreamids = "";
		List<DreamVO> dreamVOs = item.getDream();
		if (null != dreamVOs && dreamVOs.size() > 0) {
			for (int i = 0; i < dreamVOs.size(); i++) {
				dreamFacade.saveOrUpdate(dreamVOs.get(i));
				dreamids += dreamVOs.get(i).getId() + ";";
			}
			dreamids = dreamids.substring(0, dreamids.length() - 1);
		}
		values.put("dreamids", dreamids);
		String picsids = "";
		List<PicVO> picVOs = item.getPics();
		if (null != picVOs && picVOs.size() > 0) {
			for (int i = 0; i < picVOs.size(); i++) {
				picFacade.saveOrUpdate(picVOs.get(i));
				picsids += picVOs.get(i).getId() + ";";
			}
			picsids = picsids.substring(0, picsids.length() - 1);
		}
		values.put("picsids", picsids);
		return values;
	}
}
