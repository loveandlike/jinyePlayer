package com.example.administrator.jinyeplayer.utils;

import android.database.sqlite.SQLiteDatabase;

/**
 * Created by Administrator on 2016/7/6.
 */
public class SQLiteUtil {
    private static SQLiteUtil sInstance;

    private SQLiteUtil() {
//       db=SQLiteDatabase.openOrCreateDatabase()

    }
  //多线程的单例模式
    private static SQLiteUtil getsInstance() {
        if (sInstance == null) {
            synchronized (SQLiteUtil.class) {
            if (sInstance==null){
                sInstance=new SQLiteUtil();
            }
            }
        }
        return sInstance;
    }


}
