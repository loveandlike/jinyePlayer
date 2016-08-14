package com.example.administrator.jinyeplayer.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/6/29.
 */
public class MusicInfo implements Serializable {
    private String title;//音乐名
    private int _id;//音乐id
    private String artist;//艺术家
    private int duration;//时长
    private String album;//专辑
    private int album_id;//专辑id
    private String data;//路径
    private boolean isLike;//是否喜好

    public MusicInfo(String title, String data, int _id, String artist, int duration, String album, int album_id) {
        this.title = title;
        this.data = data;
        this._id = _id;
        this.artist = artist;
        this.duration = duration;
        this.album = album;
        this.album_id = album_id;
        isLike=false;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int get_id() {
        return _id;
    }

    public boolean isLike() {
        return isLike;
    }

    public void setLike(boolean like) {
        isLike = like;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public int getAlbum_id() {
        return album_id;
    }

    public void setAlbum_id(int album_id) {
        this.album_id = album_id;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}

