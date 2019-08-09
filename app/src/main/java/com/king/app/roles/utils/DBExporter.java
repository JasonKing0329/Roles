package com.king.app.roles.utils;

import com.king.app.roles.base.RApplication;
import com.king.app.roles.conf.AppConfig;

import java.io.IOException;

public class DBExporter {
	
	public static void execute() {

		String dbPath = RApplication.getInstance().getFilesDir().getParent() + "/databases";
	//	String dbPath = Environment.getExternalStorageDirectory() + "/tcslSystem";
		String targetPath = AppConfig.EXPORT_BASE;
		try {
			FileUtil.copyDirectiory(dbPath, targetPath);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static String getDatabaseFolder() {
		String dbPath = RApplication.getInstance().getFilesDir().getParent() + "/databases";
		return dbPath;
	}

	public static String getDatabasePath() {
		String dbPath = RApplication.getInstance().getFilesDir().getParent() + "/databases/" + AppConfig.DB_NAME;
		return dbPath;
	}

}
