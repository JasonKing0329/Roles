package com.king.app.roles.conf;

import android.os.Environment;

/**
 * 描述:
 * <p/>作者：景阳
 * <p/>创建时间: 2018/3/23 15:46
 */
public class AppConfig {
    public static final String DB_NAME = "roles.db";
    public static final String DB_JOURNAL = "roles.db-journal";

    public static final String SDCARD = Environment.getExternalStorageDirectory().getPath();

    public static final String DEF_CONTENT = SDCARD + "/roles";

    public static final String EXPORT_BASE = DEF_CONTENT + "/export";
    public static final String HISTORY_BASE = DEF_CONTENT + "/history";
    public static final String CONTENT_BASE = DEF_CONTENT + "/content";

    public static final String[] DIRS = new String[] {
            DEF_CONTENT, EXPORT_BASE, HISTORY_BASE, CONTENT_BASE
    };

    public static final String CONTENT_EXTRA = ".rd";

    public static final String IMG_SELECT_ROOT = SDCARD + "/fileencrypt/img/gdb";
}
