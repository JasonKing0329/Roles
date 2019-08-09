package com.king.app.roles.model;

import android.content.SharedPreferences;

import com.king.app.roles.base.RApplication;

/**
 * 描述:
 * <p/>作者：景阳
 * <p/>创建时间: 2018/1/29 14:40
 */
public class SettingProperty {

    public static final String SETTING_FILE = "Roles";

    public static String getSharedPreference() {
        return RApplication.getInstance().getCacheDir().getParent() + "/shared_prefs/" + SETTING_FILE + ".xml";
    }

    protected static final String getString(String key) {
        SharedPreferences sp = RApplication.getInstance().getSharedPreferences(SETTING_FILE, 0);
        return sp.getString(key, "");
    }

    protected static final void setString(String key, String value) {
        SharedPreferences sp = RApplication.getInstance().getSharedPreferences(SETTING_FILE, 0);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(key, value);
        editor.commit();
    }

    protected static final float getFloat(String key) {
        SharedPreferences sp = RApplication.getInstance().getSharedPreferences(SETTING_FILE, 0);
        return sp.getFloat(key, 0);
    }

    protected static final void setFloat(String key, float value) {
        SharedPreferences sp = RApplication.getInstance().getSharedPreferences(SETTING_FILE, 0);
        SharedPreferences.Editor editor = sp.edit();
        editor.putFloat(key, value);
        editor.commit();
    }

    protected static final int getInt(String key) {
        SharedPreferences sp = RApplication.getInstance().getSharedPreferences(SETTING_FILE, 0);
        return sp.getInt(key, -1);
    }

    protected static final int getInt(String key, int defaultValue) {
        SharedPreferences sp = RApplication.getInstance().getSharedPreferences(SETTING_FILE, 0);
        return sp.getInt(key, defaultValue);
    }

    protected static final void setInt(String key, int value) {
        SharedPreferences sp = RApplication.getInstance().getSharedPreferences(SETTING_FILE, 0);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt(key, value);
        editor.commit();
    }

    protected static final long getLong(String key) {
        SharedPreferences sp = RApplication.getInstance().getSharedPreferences(SETTING_FILE, 0);
        return sp.getLong(key, 0);
    }

    protected static final void setLong(String key, long value) {
        SharedPreferences sp = RApplication.getInstance().getSharedPreferences(SETTING_FILE, 0);
        SharedPreferences.Editor editor = sp.edit();
        editor.putLong(key, value);
        editor.commit();
    }

    protected static final boolean getBoolean(String key) {
        SharedPreferences sp = RApplication.getInstance().getSharedPreferences(SETTING_FILE, 0);
        return sp.getBoolean(key, false);
    }

    protected static final boolean getBoolean(String key, boolean defaultValue) {
        SharedPreferences sp = RApplication.getInstance().getSharedPreferences(SETTING_FILE, 0);
        return sp.getBoolean(key, defaultValue);
    }

    protected static final void setBoolean(String key, boolean value) {
        SharedPreferences sp = RApplication.getInstance().getSharedPreferences(SETTING_FILE, 0);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    public static void setEnableFingerPrint(boolean enable) {
        setBoolean("enable_finger_print", enable);
    }

    public static boolean isEnableFingerPrint() {
        return getBoolean("enable_finger_print");
    }

}
