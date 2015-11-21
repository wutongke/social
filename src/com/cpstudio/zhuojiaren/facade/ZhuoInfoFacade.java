package com.cpstudio.zhuojiaren.facade;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.cpstudio.zhuojiaren.helper.DatabaseHelper;
import com.cpstudio.zhuojiaren.helper.ResHelper;
import com.cpstudio.zhuojiaren.model.CmtVO;
import com.cpstudio.zhuojiaren.model.PicVO;
import com.cpstudio.zhuojiaren.model.UserVO;
import com.cpstudio.zhuojiaren.model.ZhuoInfoVO;

public class ZhuoInfoFacade {
	private final DatabaseHelper dbHelper;
	private static final String SQL_TABLE = "ZHUOINFOLIST";
	private static final String[] COLUMNS = { "msgid", "userid", "type",
			"category", "title", "text", "position", "tagsids", "goodnum",
			"cmtnum", "collectnum", "forwardnum", "goodids", "picids", "cmtids",
			"addtime", "iscollect", "isgood", "iscmt", "originid", "params1",
			"params2", "params3", "params4", "params5" };
	private Context mContext;

	public ZhuoInfoFacade(Context context) {
		mContext = context;
		String userid = ResHelper.getInstance(context).getUserid();
		dbHelper = new DatabaseHelper(context, userid);
	}

	@Override
	public void finalize() throws Throwable {
		super.finalize();
	}

	public ZhuoInfoVO getById(String id) {
		Cursor cursor = dbHelper.query(SQL_TABLE, COLUMNS, "msgid = ?",
				new String[] { id }, null, null, null);
		if (cursor.getCount() != 1) {
			return null;
		}
		cursor.moveToFirst();
		ZhuoInfoVO item = getByCursor(cursor);
		cursor.close();
		return item;
	}

	public ZhuoInfoVO getSimpleById(String id) {
		Cursor cursor = dbHelper.query(SQL_TABLE, COLUMNS, "msgid = ?",
				new String[] { id }, null, null, null);
		if (cursor.getCount() != 1) {
			return null;
		}
		cursor.moveToFirst();
		ZhuoInfoVO item = getSimpleByCursor(cursor);
		cursor.close();
		return item;
	}

	public ArrayList<ZhuoInfoVO> getAll() {
		Cursor cursor = dbHelper.query(SQL_TABLE, COLUMNS, null, null, null,
				null, null);
		if (cursor.getCount() == 0) {
			return new ArrayList<ZhuoInfoVO>();
		}
		ArrayList<ZhuoInfoVO> li = new ArrayList<ZhuoInfoVO>(cursor.getCount());
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			li.add(getByCursor(cursor));
			cursor.moveToNext();
		}
		cursor.close();
		return li;
	}

	public int update(ZhuoInfoVO item) {
		return dbHelper.update(SQL_TABLE, getFullValues(item), "msgid = ?",
				new String[] { item.getMsgid() });
	}

	public long insert(ZhuoInfoVO item) {
		return dbHelper.insert(SQL_TABLE, null, getFullValues(item));
	}

	public long saveOrUpdate(ZhuoInfoVO item) {
		String id = item.getMsgid();
		if (getById(id) != null) {
			return update(item);
		} else {
			return insert(item);
		}
	}

	public void add(ZhuoInfoVO item) {
		String id = item.getMsgid();
		if (getById(id) == null) {
			insert(item);
		}
	}

	public int delete(String id) {
		return dbHelper.delete(SQL_TABLE, "msgid = ?", new String[] { id });
	}

	public int deleteAll() {
		return dbHelper.deleteAll(SQL_TABLE);
	}

	private ZhuoInfoVO getSimpleByCursor(Cursor cursor) {
		UserFacade userFacade = new UserFacade(mContext);
		PicFacade picFacade = new PicFacade(mContext);
		CmtFacade cmtFacade = new CmtFacade(mContext);
		ZhuoInfoVO item = new ZhuoInfoVO();
		item.setMsgid(cursor.getString(0));
		String userid = cursor.getString(1);
//		UserVO userVO = userFacade.getSimpleInfoById(userid);
//		item.setUser(userVO);
//		item.setType(cursor.getString(2));
//		item.setCategory(cursor.getString(3));
//		item.setTitle(cursor.getString(4));
//		item.setText(cursor.getString(5));
//		item.setPosition(cursor.getString(6));
//		String tagses = cursor.getString(7);
//		ArrayList<String> tags = new ArrayList<String>();
//		if (tagses != null && !tagses.equals("")) {
//			if (tagses.indexOf(";") != -1) {
//				String[] ids = tagses.split(";");
//				for (String id : ids) {
//					tags.add(id);
//				}
//			} else {
//				tags.add(tagses);
//			}
//		}
//		item.setTags(tags);
//		item.setGoodnum(cursor.getString(8));
//		item.setCmtnum(cursor.getString(9));
//		item.setCollectnum(cursor.getString(10));
//		item.setForwardnum(cursor.getString(11));
//		String goodids = cursor.getString(12);
//		ArrayList<UserVO> goods = new ArrayList<UserVO>();
//		if (goodids != null && !goodids.equals("")) {
//			if (goodids.indexOf(";") != -1) {
//				String[] ids = goodids.split(";");
//				for (String id : ids) {
//					UserVO item2 = userFacade.getSimpleInfoById(id);
//					goods.add(item2);
//				}
//			} else {
//				UserVO item2 = userFacade.getSimpleInfoById(goodids);
//				goods.add(item2);
//			}
//		}
//		item.setGood(goods);
//		String picids = cursor.getString(13);
//		ArrayList<PicVO> pics = new ArrayList<PicVO>();
//		if (picids != null && !picids.equals("")) {
//			if (picids.indexOf(";") != -1) {
//				String[] ids = picids.split(";");
//				for (String id : ids) {
//					PicVO item2 = picFacade.getById(id);
//					pics.add(item2);
//				}
//			} else {
//				PicVO item2 = picFacade.getById(picids);
//				pics.add(item2);
//			}
//		}
//		item.setPic(pics);
//
//		String cmtids = cursor.getString(14);
//		ArrayList<CmtVO> cmts = new ArrayList<CmtVO>();
//		if (cmtids != null && !cmtids.equals("")) {
//			if (cmtids.indexOf(";") != -1) {
//				String[] ids = cmtids.split(";");
//				for (String id : ids) {
//					CmtVO item2 = cmtFacade.getById(id);
//					cmts.add(item2);
//				}
//			} else {
//				CmtVO item2 = cmtFacade.getById(cmtids);
//				cmts.add(item2);
//			}
//		}
//		item.setCmt(cmts);
//		item.setAddtime(cursor.getString(15));
//		item.setIscollect(cursor.getString(16));
//		item.setIsgood(cursor.getString(17));
//		item.setIscmt(cursor.getString(18));
		return item;
	}

	private ZhuoInfoVO getByCursor(Cursor cursor) {
		ZhuoInfoVO item = getSimpleByCursor(cursor);
		String originid = cursor.getString(19);
		if (originid != null && !originid.equals("")) {
			item.setOrigin(getSimpleById(originid));
		} else {
			item.setOrigin(null);
		}
		return item;
	}

	private ContentValues getFullValues(ZhuoInfoVO item) {
		UserFacade userFacade = new UserFacade(mContext);
		PicFacade picFacade = new PicFacade(mContext);
		CmtFacade cmtFacade = new CmtFacade(mContext);
		ContentValues values = new ContentValues();
		values.put("msgid", item.getMsgid());
		UserVO user = item.getUser();
//		String userid = userFacade.add(user);
//		values.put("userid", userid);
		values.put("type", item.getType());
		values.put("category", item.getCategory());
		values.put("title", item.getTitle());
		values.put("text", item.getText());
		values.put("position", item.getPosition());
		List<String> tags = item.getTags();
		String tagsids = "";
		if (tags != null && tags.size() > 0) {
			for (String tag : tags) {
				tagsids += tag + ";";
			}
			tagsids = tagsids.substring(0, tagsids.length() - 1);
		}
		values.put("tagsids", tagsids);
		values.put("goodnum", item.getGoodnum());
		values.put("cmtnum", item.getCmtnum());
		values.put("collectnum", item.getCollectnum());
		values.put("forwardnum", item.getForwardnum());
		List<UserVO> goods = item.getGood();
		String goodids = "";
		if (goods != null && goods.size() > 0) {
			for (UserVO good : goods) {
//				String goodid = userFacade.add(good);
//				goodids += goodid + ";";
			}
			goodids = goodids.substring(0, goodids.length() - 1);
		}
		values.put("goodids", goodids);
		List<PicVO> pics = item.getPic();
		String picids = "";
		if (pics != null && pics.size() > 0) {
			for (PicVO pic : pics) {
				String id = pic.getId();
				if (id == null) {
					id = System.currentTimeMillis() + "";
					pic.setId(id);
				}
				picFacade.saveOrUpdate(pic);
				picids += id + ";";
			}
			picids = picids.substring(0, picids.length() - 1);
		}
		values.put("picids", picids);
		List<CmtVO> cmts = item.getCmt();
		String cmtids = "";
		if (cmts != null && cmts.size() > 0) {
			for (CmtVO cmt : cmts) {
				cmtFacade.saveOrUpdate(cmt);
				cmtids += cmt + ";";
			}
			cmtids = cmtids.substring(0, cmtids.length() - 1);
		}
		values.put("cmtids", cmtids);
		values.put("addtime", item.getAddtime());
		values.put("position", item.getPosition());
		values.put("iscollect", item.getIscollect());
		values.put("isgood", item.getIsgood());
		values.put("iscmt", item.getIscmt());
		ZhuoInfoVO origin = item.getOrigin();
		if (origin != null && origin.getMsgid() != null) {
			saveOrUpdate(origin);
			values.put("originid", origin.getMsgid());
		}
		return values;
	}
}
