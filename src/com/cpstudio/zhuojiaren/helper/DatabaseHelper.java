package com.cpstudio.zhuojiaren.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {
	private static final String DATABASE_NAME = ".db";
	private static final int DATABASE_VERSION = 8;
	private static final String SQL_TABLE_IMMSGLIST = "CREATE TABLE IMMSGLIST(id TEXT PRIMARY KEY, "
			+ "senderid TEXT NOT NULL, "
			+ "receiverid TEXT NOT NULL, "
			+ "type TEXT NOT NULL, "
			+ "content TEXT, "
			+ "file TEXT, "
			+ "isread TEXT, "
			+ "groupid TEXT, "
			+ "addtime TEXT, "
			+ "savepath TEXT, "
			+ "secs TEXT, "
			+ "params1 TEXT, "
			+ "params2 TEXT, "
			+ "params3 TEXT, "
			+ "params4 TEXT, "
			+ "params5 TEXT)";
	private static final String SQL_TABLE_IMQUANMSGLIST = "CREATE TABLE IMQUANMSGLIST (id TEXT PRIMARY KEY, "
			+ "senderid TEXT NOT NULL, "
			+ "type TEXT NOT NULL, "
			+ "content TEXT, "
			+ "file TEXT, "
			+ "isread TEXT, "
			+ "groupid TEXT, "
			+ "addtime TEXT, "
			+ "savepath TEXT, "
			+ "secs TEXT, "
			+ "params1 TEXT, "
			+ "params2 TEXT, "
			+ "params3 TEXT, " + "params4 TEXT, " + "params5 TEXT)";
	private static final String SQL_TABLE_USERLIST = "CREATE TABLE USERLIST (userid TEXT PRIMARY KEY, "
			+ "userpwd TEXT, "
			+ "username TEXT, "
			+ "uheader TEXT, "
			+ "sex TEXT, "
			+ "hometown TEXT, "
			+ "travel_cities TEXT, "
			+ "birthday TEXT, "
			+ "birthday_type TEXT, "
			+ "constellation TEXT, "
			+ "maxim TEXT, "
			+ "hobby TEXT, "
			+ "company TEXT, "
			+ "email TEXT, "
			+ "learn_exp TEXT, "
			+ "website TEXT, "
			+ "join_zhuo_date TEXT, "
			+ "pinyin TEXT, "
			+ "startletter TEXT, "
			+ "level TEXT, "
			+ "productotal TEXT, "
			+ "familytotal TEXT, "
			+ "grouptotal TEXT, "
			+ "isfollow TEXT, "
			+ "isemailopen TEXT, "
			+ "isphoneopen TEXT, "
			+ "isworking TEXT, "
			+ "isisentrepreneurship TEXT, "
			+ "ismarry TEXT, "
			+ "offertotal TEXT, "
			+ "lastoffer TEXT, "
			+ "lastdemand TEXT, "
			+ "id TEXT, "
			+ "classissure TEXT, "
			+ "isbirthdayopen TEXT, "
			+ "activenum TEXT, "
			+ "fannum TEXT, "
			+ "follownum TEXT, "
			+ "age TEXT, "
			+ "isread TEXT, "
			+ "jifen TEXT, "
			+ "phone TEXT, "
			+ "mycustomer TEXT, "
			+ "post TEXT, "
			+ "industry TEXT, "
			+ "city TEXT,"
			+ "isalert TEXT,"
			+ "productids TEXT, "
			+ "familyids TEXT, "
			+ "groupsids TEXT, "
			+ "growthids TEXT, "
			+ "dreamids TEXT, "
			+ "picsids TEXT, "
			+ "params1 TEXT, "
			+ "params2 TEXT, "
			+ "params3 TEXT, "
			+ "params4 TEXT, "
			+ "params5 TEXT)";
	private static final String SQL_TABLE_CONTRACTLIST = "CREATE TABLE CONTRACTLIST (userid TEXT PRIMARY KEY)";
	private static final String SQL_TABLE_FANLIST = "CREATE TABLE  FANLIST (userid TEXT PRIMARY KEY)";
	private static final String SQL_TABLE_FOLLOWLIST = "CREATE TABLE  FOLLOWLIST (userid TEXT PRIMARY KEY)";
	private static final String SQL_TABLE_QUANLIST = "CREATE TABLE QUANLIST (groupid TEXT PRIMARY KEY, "
			+ "gname TEXT, "
			+ "gheader TEXT, "
			+ "gproperty TEXT, "
			+ "gintro TEXT, "
			+ "createtime TEXT, "
			+ "membersnum TEXT, "
			+ "membersmax TEXT, "
			+ "lastbroadcast TEXT, "
			+ "lastmsgtime TEXT, "
			+ "alert TEXT, "
			+ "founderid TEXT, "
			+ "membertype TEXT, "
			+ "managersids TEXT, "
			+ "membersids TEXT, "
			+ "params1 TEXT, "
			+ "params2 TEXT, "
			+ "params3 TEXT, "
			+ "params4 TEXT, " + "params5 TEXT)";

	private static final String SQL_TABLE_ZHUOQUANLIST = "CREATE TABLE ZHUOQUANLIST ("
			+ "type TEXT, " + "typeid TEXT, " + "groupid TEXT)";

	private static final String SQL_TABLE_PLANLIST = "CREATE TABLE PLANLIST (id TEXT PRIMARY KEY, "
			+ "content TEXT, "
			+ "addtime TEXT, "
			+ "params1 TEXT, "
			+ "params2 TEXT, "
			+ "params3 TEXT, "
			+ "params4 TEXT, "
			+ "params5 TEXT)";
	private static final String SQL_TABLE_SYSMSGLIST = "CREATE TABLE SYSMSGLIST (id TEXT PRIMARY KEY, "
			+ "senderid TEXT, "
			+ "receiverid TEXT, "
			+ "groupid TEXT, "
			+ "addtime TEXT, "
			+ "groupname TEXT, "
			+ "type TEXT, "
			+ "content TEXT, "
			+ "isread TEXT, "
			+ "userid TEXT, "
			+ "params1 TEXT, "
			+ "params2 TEXT, "
			+ "params3 TEXT, "
			+ "params4 TEXT, " + "params5 TEXT)";
	private static final String SQL_TABLE_CARDMSGLIST = "CREATE TABLE CARDMSGLIST (id TEXT PRIMARY KEY, "
			+ "senderid TEXT, "
			+ "receiverid TEXT, "
			+ "leavemsg TEXT, "
			+ "isopen TEXT, "
			+ "state TEXT, "
			+ "isread TEXT, "
			+ "addtime TEXT, "
			+ "params1 TEXT, "
			+ "params2 TEXT, "
			+ "params3 TEXT, " + "params4 TEXT, " + "params5 TEXT)";
	private static final String SQL_TABLE_CMTRCMDLIST = "CREATE TABLE CMTRCMDLIST (id TEXT PRIMARY KEY, "
			+ "senderid TEXT, "
			+ "receiverid TEXT, "
			+ "content TEXT, "
			+ "orginid TEXT, "
			+ "isread TEXT, "
			+ "addtime TEXT, "
			+ "params1 TEXT, "
			+ "params2 TEXT, "
			+ "params3 TEXT, "
			+ "params4 TEXT, " + "params5 TEXT)";

	private static final String SQL_TABLE_CMTLIST = "CREATE TABLE CMTLIST (id TEXT PRIMARY KEY, "
			+ "addtime TEXT, "
			+ "content TEXT, "
			+ "userid TEXT, "
			+ "parentid TEXT, "
			+ "msgid TEXT, "
			+ "likecnt TEXT, "
			+ "params1 TEXT, "
			+ "params2 TEXT, "
			+ "params3 TEXT, "
			+ "params4 TEXT, " + "params5 TEXT)";
	private static final String SQL_TABLE_PICLIST = "CREATE TABLE PICLIST (id TEXT PRIMARY KEY, "
			+ "thumburl TEXT, "
			+ "orgurl TEXT, "
			+ "desc TEXT, "
			+ "params1 TEXT, "
			+ "params2 TEXT, "
			+ "params3 TEXT, "
			+ "params4 TEXT, " + "params5 TEXT)";
	private static final String SQL_TABLE_DREAMLIST = "CREATE TABLE DREAMLIST (id TEXT PRIMARY KEY, "
			+ "userid TEXT, "
			+ "dream TEXT, "
			+ "addtime TEXT, "
			+ "params1 TEXT, "
			+ "params2 TEXT, "
			+ "params3 TEXT, "
			+ "params4 TEXT, " + "params5 TEXT)";
	private static final String SQL_TABLE_PRODUCTLIST = "CREATE TABLE PRODUCTLIST (id TEXT PRIMARY KEY, "
			+ "title TEXT, "
			+ "_desc TEXT, "
			+ "_value TEXT, "
			+ "addtime TEXT, "
			+ "username TEXT, "
			+ "uheader TEXT, "
			+ "userid TEXT, "
			+ "params1 TEXT, "
			+ "params2 TEXT, "
			+ "params3 TEXT, " + "params4 TEXT, " + "params5 TEXT)";

	private static final String SQL_TABLE_QUANUSERLIST = "CREATE TABLE QUANUSERLIST (id TEXT PRIMARY KEY, "
			+ "userid TEXT, "
			+ "glevel TEXT, "
			+ "groupid TEXT, "
			+ "params1 TEXT, "
			+ "params2 TEXT, "
			+ "params3 TEXT, "
			+ "params4 TEXT, " + "params5 TEXT)";
	private static final String SQL_TABLE_ZENGHUILIST = "CREATE TABLE ZENGHUILIST (id TEXT PRIMARY KEY, "
			+ "title TEXT, "
			+ "detail TEXT, "
			+ "addtime TEXT, "
			+ "username TEXT, "
			+ "params1 TEXT, "
			+ "params2 TEXT, "
			+ "params3 TEXT, " + "params4 TEXT, " + "params5 TEXT)";
	private static final String SQL_TABLE_COLLECTLIST = "CREATE TABLE COLLECTLIST (msgid TEXT PRIMARY KEY)";
	private static final String SQL_TABLE_ACTIVELIST = "CREATE TABLE ACTIVELIST (msgid TEXT PRIMARY KEY)";
	private static final String SQL_TABLE_NEWSLIST = "CREATE TABLE NEWSLIST (msgid TEXT PRIMARY KEY)";
	private static final String SQL_TABLE_USERDAILY = "CREATE TABLE USERDAILY (msgid TEXT,userid TEXT)";
	private static final String SQL_TABLE_ZHUOINFOLIST = "CREATE TABLE ZHUOINFOLIST (msgid TEXT PRIMARY KEY, "
			+ "userid TEXT, "
			+ "type TEXT, "
			+ "category TEXT, "
			+ "title TEXT, "
			+ "text TEXT, "
			+ "position TEXT, "
			+ "tagsids TEXT, "
			+ "goodnum TEXT, "
			+ "cmtnum TEXT, "
			+ "collectnum TEXT, "
			+ "forwardnum TEXT, "
			+ "goodids TEXT, "
			+ "picids TEXT, "
			+ "cmtids TEXT, "
			+ "addtime TEXT, "
			+ "iscollect TEXT, "
			+ "isgood TEXT, "
			+ "iscmt TEXT, "
			+ "originid TEXT, "
			+ "params1 TEXT, "
			+ "params2 TEXT, "
			+ "params3 TEXT, " + "params4 TEXT, " + "params5 TEXT)";

	private static final String SQL_TABLE_RECORDLIST = "CREATE TABLE RECORDLIST (id TEXT PRIMARY KEY, "
			+ "path TEXT NOT NULL, "
			+ "name TEXT, "
			+ "size TEXT, "
			+ "date TEXT, "
			+ "length TEXT, "
			+ "users TEXT, "
			+ "state TEXT, "
			+ "params1 TEXT, "
			+ "params2 TEXT, "
			+ "params3 TEXT, "
			+ "params4 TEXT, " + "params5 TEXT)";

	private static final String SQL_TABLE_RECORDCHATLIST = "CREATE TABLE RECORDCHATLIST(id TEXT PRIMARY KEY, "
			+ "senderid TEXT NOT NULL, "
			+ "receiverid TEXT NOT NULL, "
			+ "type TEXT NOT NULL, "
			+ "content TEXT, "
			+ "file TEXT, "
			+ "isread TEXT, "
			+ "groupid TEXT, "
			+ "addtime TEXT, "
			+ "savepath TEXT, "
			+ "secs TEXT, "
			+ "params1 TEXT, "
			+ "params2 TEXT, "
			+ "params3 TEXT, "
			+ "params4 TEXT, "
			+ "params5 TEXT)";

	private static final String TAG = null;
	private static SQLiteDatabase dbSqlite = null;

	public DatabaseHelper(Context context, String userid) {
		super(context, userid + DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		createAll(db);
	}

	public void createTable(SQLiteDatabase db, String sql) {
		db.execSQL(sql);
	}

	public void createAll(SQLiteDatabase db) {
		db.execSQL(SQL_TABLE_USERLIST);
		db.execSQL(SQL_TABLE_CONTRACTLIST);
		db.execSQL(SQL_TABLE_FOLLOWLIST);
		db.execSQL(SQL_TABLE_FANLIST);
		db.execSQL(SQL_TABLE_IMMSGLIST);
		db.execSQL(SQL_TABLE_IMQUANMSGLIST);
		db.execSQL(SQL_TABLE_CARDMSGLIST);
		db.execSQL(SQL_TABLE_CMTLIST);
		db.execSQL(SQL_TABLE_PICLIST);
		db.execSQL(SQL_TABLE_PLANLIST);
		db.execSQL(SQL_TABLE_DREAMLIST);
		db.execSQL(SQL_TABLE_PRODUCTLIST);
		db.execSQL(SQL_TABLE_QUANUSERLIST);
		db.execSQL(SQL_TABLE_QUANLIST);
		db.execSQL(SQL_TABLE_SYSMSGLIST);
		db.execSQL(SQL_TABLE_ZENGHUILIST);
		db.execSQL(SQL_TABLE_COLLECTLIST);
		db.execSQL(SQL_TABLE_ACTIVELIST);
		db.execSQL(SQL_TABLE_NEWSLIST);
		db.execSQL(SQL_TABLE_ZHUOINFOLIST);
		db.execSQL(SQL_TABLE_CMTRCMDLIST);
		db.execSQL(SQL_TABLE_ZHUOQUANLIST);
		db.execSQL(SQL_TABLE_USERDAILY);
		db.execSQL(SQL_TABLE_RECORDLIST);
		db.execSQL(SQL_TABLE_RECORDCHATLIST);
	}

	public void dropTable(SQLiteDatabase db, String tableName) {
		db.execSQL("drop table if exists " + tableName.trim());
	}

	public void dropAll(SQLiteDatabase db) {
		db.execSQL("drop table if exists USERLIST");
		db.execSQL("drop table if exists CONTRACTLIST");
		db.execSQL("drop table if exists FOLLOWLIST");
		db.execSQL("drop table if exists FANLIST");
		db.execSQL("drop table if exists IMMSGLIST");
		db.execSQL("drop table if exists IMQUANMSGLIST");
		db.execSQL("drop table if exists CARDMSGLIST");
		db.execSQL("drop table if exists PICLIST");
		db.execSQL("drop table if exists PLANLIST");
		db.execSQL("drop table if exists DREAMLIST");
		db.execSQL("drop table if exists PRODUCTLIST");
		db.execSQL("drop table if exists QUANUSERLIST");
		db.execSQL("drop table if exists QUANLIST");
		db.execSQL("drop table if exists SYSMSGLIST");
		db.execSQL("drop table if exists ZENGHUILIST");
		db.execSQL("drop table if exists COLLECTLIST");
		db.execSQL("drop table if exists ACTIVELIST");
		db.execSQL("drop table if exists NEWSLIST");
		db.execSQL("drop table if exists ZHUOINFOLIST");
		db.execSQL("drop table if exists CMTRCMDLIST");
		db.execSQL("drop table if exists ZHUOQUANLIST");
		db.execSQL("drop table if exists USERDAILY");
		db.execSQL("drop table if exists RECORDLIST");
		db.execSQL("drop table if exists RECORDCHATLIST");
	}

	@Override
	public void onOpen(SQLiteDatabase db) {
		super.onOpen(db);
		if (!db.isReadOnly())
			dbSqlite = db;
	}

	@Override
	public synchronized void close() {
		if (dbSqlite != null) {
			dbSqlite.close();
			dbSqlite = null;
		}
		super.close();
	}

	public boolean tabbleIsExist(SQLiteDatabase db, String tableName) {
		boolean result = false;
		if (tableName == null) {
			return false;
		}
		Cursor cursor = null;
		try {
			String sql = "select count(*) as c from " + DATABASE_NAME
					+ " where type ='table' and name ='" + tableName.trim()
					+ "' ";
			cursor = db.rawQuery(sql, null);
			if (cursor.moveToNext()) {
				int count = cursor.getInt(0);
				if (count > 0) {
					result = true;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int currentVersion) {
		Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
				+ currentVersion + ".");
		if (oldVersion > currentVersion) {
			dropAll(db);
		} else if (oldVersion == currentVersion) {
			return;
		}
		db.beginTransaction();
		try {
			switch (oldVersion) {
			case 1:
				if (!tabbleIsExist(db, "CONTRACTLIST")) {
					createTable(db, SQL_TABLE_CONTRACTLIST);
				}
				if (!tabbleIsExist(db, "FOLLOWLIST")) {
					createTable(db, SQL_TABLE_FOLLOWLIST);
				}
				if (!tabbleIsExist(db, "FANLIST")) {
					createTable(db, SQL_TABLE_FANLIST);
				}
			case 2:
				if (!tabbleIsExist(db, "COLLECTLIST")) {
					createTable(db, SQL_TABLE_COLLECTLIST);
				}
				if (!tabbleIsExist(db, "ACTIVELIST")) {
					createTable(db, SQL_TABLE_ACTIVELIST);
				}
				if (!tabbleIsExist(db, "NEWSLIST")) {
					createTable(db, SQL_TABLE_NEWSLIST);
				}
				if (!tabbleIsExist(db, "ZHUOQUANLIST")) {
					createTable(db, SQL_TABLE_ZHUOQUANLIST);
				}
				if (!tabbleIsExist(db, "USERDAILY")) {
					createTable(db, SQL_TABLE_USERDAILY);
				}
			case 3:
				if (!tabbleIsExist(db, "RECORDLIST")) {
					createTable(db, SQL_TABLE_RECORDLIST);
				}
			case 4:
				if (!tabbleIsExist(db, "RECORDCHATLIST")) {
					createTable(db, SQL_TABLE_RECORDCHATLIST);
				}
			case 5:
				db.execSQL("ALTER TABLE ZHUOINFOLIST ADD COLUMN originid TEXT");
			case 6:
				db.execSQL("ALTER TABLE CMTLIST ADD COLUMN likecnt TEXT");
			case 7:
				db.execSQL("ALTER TABLE ZHUOINFOLIST ADD COLUMN forwardnum TEXT");
			}
			db.setTransactionSuccessful();
		} catch (Throwable ex) {
			Log.e(TAG, ex.getMessage(), ex);
		} finally {
			db.endTransaction();
		}
	}

	public void execSql(String sql) {
		if (dbSqlite == null) {
			getWritableDatabase();
		}
		dbSqlite.execSQL(sql);
	}

	public long insert(String table, String nullColumnHack, ContentValues values) {
		if (dbSqlite == null) {
			getWritableDatabase();
		}
		return dbSqlite.insert(table, nullColumnHack, values);
	}

	public int update(String table, ContentValues values, String whereClause,
			String[] whereArgs) {
		if (dbSqlite == null) {
			getWritableDatabase();
		}
		return dbSqlite.update(table, values, whereClause, whereArgs);
	}

	public int delete(String table, String whereClause, String[] whereArgs) {
		if (dbSqlite == null) {
			getWritableDatabase();
		}
		return dbSqlite.delete(table, whereClause, whereArgs);
	}

	public int deleteAll(String table) {
		if (dbSqlite == null) {
			getWritableDatabase();
		}
		return dbSqlite.delete(table, null, null);
	}

	public Cursor query(String table, String[] columns, String selection,
			String[] selectionArgs, String groupBy, String having,
			String orderBy) {
		if (dbSqlite == null) {
			getWritableDatabase();
		}
		return dbSqlite.query(table, columns, selection, selectionArgs,
				groupBy, having, orderBy);
	}

	public Cursor query(String table, String[] columns, String selection,
			String[] selectionArgs, String groupBy, String having,
			String orderBy, String limit) {
		if (dbSqlite == null) {
			getWritableDatabase();
		}
		return dbSqlite.query(table, columns, selection, selectionArgs,
				groupBy, having, orderBy, limit);
	}

}
