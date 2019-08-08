package com.king.app.roles.model;

import android.content.SharedPreferences;

import com.king.app.roles.base.RApplication;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Desc:
 *
 * @author：Jing Yang
 * @date: 2019/8/8 14:02
 */
public class ChapterModel {

    private static final String SP_CONTENT = "chapter_content";

    public String parseChapterFile(String file) {
        if (file == null) {
            return null;
        }
        if (file.startsWith("file://")) {
            String filePath = file.substring(7);
            return parseDiskFile(filePath);
        } else if (file.startsWith("xml://")) {
            String key = file.substring(6);
            return parseXmlFile(key);
        } else {
            return null;
        }
    }

    public boolean saveContent(String html, String file) {
        boolean result = false;
        if (file.startsWith("file://")) {
            String filePath = file.substring(7);
            result = writeFile(html, filePath);
        } else if (file.startsWith("xml://")) {
            String key = file.substring(6);
            result = setXmlContent(key, html);
        }
        return result;
    }

    private String parseDiskFile(String filePath) {
        String content = null;
        File file = new File(filePath);
        if (file.exists() && !file.isDirectory()) {
            content = readFileContent(file);
        }
        return content;
    }

    private String readFileContent(File file) {
        BufferedReader reader = null;
        StringBuffer sbf = new StringBuffer();
        try {
            reader = new BufferedReader(new FileReader(file));
            String tempStr;
            while ((tempStr = reader.readLine()) != null) {
                sbf.append(tempStr);
            }
            reader.close();
            return sbf.toString();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
        return sbf.toString();
    }

    private boolean writeFile(String text, String file) {
        try {
            File writeName = new File(file);
            if (!writeName.exists()) {
                File parent = writeName.getParentFile();
                parent.mkdirs();
                writeName.createNewFile();
            }
            FileWriter writer = new FileWriter(writeName);
            BufferedWriter out = new BufferedWriter(writer);
            out.write(text);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private String parseXmlFile(String key) {
        return getXmlContent(key);
    }

    public static String getContentPreference() {
        return RApplication.getInstance().getCacheDir().getParent() + "/shared_prefs/" + SP_CONTENT + ".xml";
    }

    protected String getXmlContent(String key) {
        SharedPreferences sp = RApplication.getInstance().getSharedPreferences(SP_CONTENT, 0);
        return sp.getString(key, "");
    }

    protected boolean setXmlContent(String key, String value) {
        SharedPreferences sp = RApplication.getInstance().getSharedPreferences(SP_CONTENT, 0);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(key, value);
        editor.commit();
        return true;
    }
}
