package com.example.administrator.jinyeplayer.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLHelper extends SQLiteOpenHelper {
    public static final String DB_NAME = "music_db";
    public static final String TABLE_RECENT = "recent_list";


    public SQLHelper(Context context) {
        super(context, DB_NAME, null, 1);
    }

    //當第一次被調用的時候，執行的方法
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create table if not exists "
                + TABLE_RECENT + " (title varchar(255) ,"
                + "data varchar(255) ,"
                + "_id int ,"
                + "artist varchar(255) ,"
                + "duration int ,"
                + "album varchar(255) ,"
                + "album_id int ) ");

    }

    //當數據庫更新時 才會被調用
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
