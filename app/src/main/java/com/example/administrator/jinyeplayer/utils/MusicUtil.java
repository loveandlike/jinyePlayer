package com.example.administrator.jinyeplayer.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;

import com.example.administrator.jinyeplayer.bean.MusicInfo;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

/**
 * Created by Administrator on 2016/6/29.
 */
public class MusicUtil {
    private static final String LIST_FILE_PATH = "/data/data/com.example.administrator.jinyeplayer/list";
    private static ArrayList<MusicInfo> musicList;//音乐总列表
    private static ArrayList<MusicInfo> recentList = new ArrayList<>();//最近播放列表
    private static ArrayList<MusicInfo> likeList = new ArrayList<>();//喜爱列表

    public static ArrayList<MusicInfo> getMusicList(Context context) {
        if (musicList == null) {
            musicList = new ArrayList<>();
            ContentResolver Resolver = context.getContentResolver();//内容解析器
            //参数一 音频文件的uri   参数五 按照修改时间进行排序
            //通过银屏获取内部存储的内部标识符   拿到游标
            Cursor cursor = Resolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null, null, MediaStore.Audio.Media.DATE_MODIFIED);
            while (cursor.moveToNext()) {
                String title = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE));
                String artist = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
                String album = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM));
                String data = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
                int _id = cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media._ID));
                int duration = cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION));
                int album_id = cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID));
                if (duration > 2000) {
                    musicList.add(new MusicInfo(title, data, _id, artist, duration, album, album_id));
                }
            }
        }
        return musicList;
    }

    public static ArrayList<MusicInfo> getRecentList() {
        recentList.clear();
        File f = new File(LIST_FILE_PATH + "recentList");
        try {
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(f));
            while (true) {
                MusicInfo info = (MusicInfo) ois.readObject();
                if (info != null) {
                    recentList.add(info);
                } else {
                    break;
                }
            }
            ois.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return recentList;
    }

//    public static void writeInfo(MusicInfo info) {
//        for (MusicInfo musicinfo : recentList) {
//            if (musicinfo.get_id() == info.get_id()) {
//                recentList.remove(musicinfo);
//                break;
//            }
//        }
//        recentList.add(info);
//        File f = new File(LIST_FILE_PATH + "recentList");
//        try {
//            f.delete();
//            if (!f.exists()) {
//                f.createNewFile();
//            }
//            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(f));
//            for (MusicInfo musicinfo : recentList) {
//                oos.writeObject(musicinfo);
//            }
//            oos.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

    public static ArrayList<MusicInfo> getLikeList() {
        likeList.clear();
        File f = new File(LIST_FILE_PATH + "likeList");
        try {
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(f));
            while (true) {
                MusicInfo info = (MusicInfo) ois.readObject();
                if (info != null) {
                    likeList.add(info);
                } else {
                    break;
                }
            }
            ois.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return likeList;
    }

    public static void writeLikeInfo(MusicInfo info) {
        for (MusicInfo musicinfo : likeList) {
            if (musicinfo.get_id() == info.get_id()) {
                likeList.remove(musicinfo);
                break;
            }
        }
        likeList.add(info);
        File f = new File(LIST_FILE_PATH + "likeList");
        try {
            f.delete();
            if (!f.exists()) {
                f.createNewFile();
            }
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(f));
            for (MusicInfo musicinfo : likeList) {
                oos.writeObject(musicinfo);
            }
            oos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void removeLikeINfo(MusicInfo info) {
        for (MusicInfo musicinfo : likeList) {
            if (musicinfo.get_id() == info.get_id()) {
                likeList.remove(musicinfo);
                break;
            }
        }
        File f = new File(LIST_FILE_PATH + "likeList");
        try {
            f.delete();
            if (!f.exists()) {
                f.createNewFile();
            }
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(f));
            for (MusicInfo musicinfo : likeList) {
                oos.writeObject(musicinfo);
            }
            oos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
