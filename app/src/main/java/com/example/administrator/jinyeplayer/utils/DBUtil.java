package com.example.administrator.jinyeplayer.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.administrator.jinyeplayer.bean.MusicInfo;

import java.util.ArrayList;


public class DBUtil {
    private SQLHelper helper;
    private SQLiteDatabase db;
    private static DBUtil sInstance;

    private DBUtil(Context context) {
        helper = new SQLHelper(context);
        db = helper.getWritableDatabase();
    }
           //懒汉模式,并发不安全，时间换空间
    public static DBUtil getsInstance(Context context) {
        if (sInstance == null) {
            sInstance = new DBUtil(context);
        }
        return sInstance;
    }

    public void saveMusic(MusicInfo info) {
        ContentValues values = new ContentValues();
        values.put("title", info.getTitle());
        values.put("duration", info.getDuration());
        values.put("_id", info.get_id());
        values.put("album", info.getAlbum());
        values.put("album_id", info.getAlbum_id());
        values.put("artist", info.getArtist());
        values.put("data", info.getData());
        db.insert(SQLHelper.TABLE_RECENT, null, values);//將數據插入数据库
    }

    public ArrayList<MusicInfo> getRecentList() {
        ArrayList<MusicInfo> list = new ArrayList<>();
        Cursor cursor = db.query(true, SQLHelper.TABLE_RECENT, null, null, null, null, null, null, null);
        while (cursor.moveToNext()) {
            String title = cursor.getString(cursor.getColumnIndex("title"));
            String artist = cursor.getString(cursor.getColumnIndex("artist"));
            String album = cursor.getString(cursor.getColumnIndex("album"));
            String data = cursor.getString(cursor.getColumnIndex("data"));
            int _id = cursor.getInt(cursor.getColumnIndex("_id"));
            int duration = cursor.getInt(cursor.getColumnIndex("duration"));
            int album_id = cursor.getInt(cursor.getColumnIndex("album_id"));
            list.add(new MusicInfo(title, data, _id, artist, duration, album, album_id));
        }
        return list;
    }
}
