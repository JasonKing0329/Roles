package com.king.app.roles.utils;

import android.database.sqlite.SQLiteDatabase;

import com.king.app.roles.base.RApplication;
import com.king.app.roles.conf.AppConfig;
import com.king.app.roles.model.ChapterModel;
import com.king.app.roles.model.SettingProperty;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * 描述:
 * <p/>作者：景阳
 * <p/>创建时间: 2018/1/29 14:29
 */
public class FileUtil {

    /**
     * 从assets目录复制的方法
     * @param dbFile
     */
    public static void copyDbFromAssets(String dbFile) {

        SQLiteDatabase db = null;
        //先检查是否存在，不存在才复制
        String dbPath = RApplication.getInstance().getFilesDir().getParent() + "/databases";
        try {
            db = SQLiteDatabase.openDatabase(dbPath + "/" + dbFile
                    , null, SQLiteDatabase.OPEN_READONLY);
        } catch (Exception e) {
            db = null;
        }
        if (db == null) {
            try {
                InputStream assetsIn = RApplication.getInstance().getAssets().open(dbFile);
                File file = new File(dbPath);
                if (!file.exists()) {
                    file.mkdir();
                }
                OutputStream fileOut = new FileOutputStream(dbPath + "/" + dbFile);
                byte[] buffer = new byte[1024];
                int length;
                while ((length = assetsIn.read(buffer))>0){
                    fileOut.write(buffer, 0, length);
                }

                fileOut.flush();
                fileOut.close();
                assetsIn.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (db != null) {
            db.close();
        }
    }

    public static boolean deleteFile(String path) {
        File file = new File(path);
        if (file.exists()) {
            file.delete();
            return true;
        }
        return false;
    }

    public static void copyFile(File sourceFile, File targetFile)
            throws IOException {

        // 新建文件输入流并对它进行缓冲
        FileInputStream input = new FileInputStream(sourceFile);
        BufferedInputStream inbuff = new BufferedInputStream(input);

        // 新建文件输出流并对它进行缓冲
        FileOutputStream out = new FileOutputStream(targetFile);
        BufferedOutputStream outbuff = new BufferedOutputStream(out);

        // 缓冲数组
        byte[] b = new byte[1024 * 5];
        int len = 0;
        while ((len = inbuff.read(b)) != -1) {
            outbuff.write(b, 0, len);
        }

        // 刷新此缓冲的输出流
        outbuff.flush();

        // 关闭流
        inbuff.close();
        outbuff.close();
        out.close();
        input.close();

    }

    public static void copyDirectiory(String sourceDir, String targetDir)
            throws IOException {
        DebugLog.e("copy from [" + sourceDir + "] to [" + targetDir + "]");
        // 新建目标目录
        File target = new File(targetDir);
        if (!target.exists()) {
            target.mkdirs();
        }

        // 获取源文件夹当下的文件或目录
        File[] file = (new File(sourceDir)).listFiles();
        if (file == null) {
            return;
        }

        for (int i = 0; i < file.length; i++) {
            if (file[i].isFile()) {
                // 源文件
                File sourceFile = file[i];
                // 目标文件
                File targetFile = new File(
                        new File(targetDir).getAbsolutePath() + File.separator
                                + file[i].getName());

                FileUtil.copyFile(sourceFile, targetFile);

            }

            if (file[i].isDirectory()) {
                // 准备复制的源文件夹
                String dir1 = sourceDir + file[i].getName();
                // 准备复制的目标文件夹
                String dir2 = targetDir + "/" + file[i].getName();

                copyDirectiory(dir1, dir2);
            }
        }

    }
}
