package com.king.app.roles.base;

import android.app.Application;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;

import com.king.app.roles.conf.AppConfig;
import com.king.app.roles.model.entity.DaoMaster;
import com.king.app.roles.model.entity.DaoSession;
import com.king.app.roles.utils.DebugLog;

import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.query.QueryBuilder;

public class RApplication extends Application {

	private static RApplication instance;

	private DaoSession daoSession;
	private RHelper helper;

	public static int getSDKVersion() {
		return Build.VERSION.SDK_INT;
	}

	/**
	 * use number 21 to mark, make codes runs well under android L
	 * @return
	 */
	public static boolean isLollipop() {
		return Build.VERSION.SDK_INT >= 21;//Build.VERSION_CODES.L;
	}

	/**
	 * use number 23 to mark, make codes runs well under android L
	 * @return
	 */
	public static boolean isM() {
		return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
	}

	public static boolean DEBUG = false;

	public static RApplication getInstance() {
		return instance;
	}

	@Override
	public void onCreate() {
		instance = this;
		super.onCreate();
	}

	/**
	 * 程序初始化使用外置数据库
	 * 需要由外部调用，如果在onCreate里直接初始化会创建新的数据库
	 */
	public void createGreenDao() {
		helper = new RHelper(getApplicationContext(), AppConfig.DB_NAME);
		Database db = helper.getWritableDb();
		daoSession = new DaoMaster(db).newSession();

		QueryBuilder.LOG_SQL = true;
		QueryBuilder.LOG_VALUES = true;
	}

	public void reCreateGreenDao() {
		daoSession.clear();
		helper.close();
		createGreenDao();
	}

	public DaoSession getDaoSession() {
		return daoSession;
	}

	public static class RHelper extends DaoMaster.OpenHelper {

		public RHelper(Context context, String name) {
			super(context, name);
		}

		public RHelper(Context context, String name, SQLiteDatabase.CursorFactory factory) {
			super(context, name, factory);
		}

		@Override
		public void onUpgrade(Database db, int oldVersion, int newVersion) {
			DebugLog.e(" oldVersion=" + oldVersion + ", newVersion=" + newVersion);
		}
	}
}
