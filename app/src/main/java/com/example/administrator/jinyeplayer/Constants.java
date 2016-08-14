package com.example.administrator.jinyeplayer;

/**
 * Created by Administrator on 2016/6/29.
 */

public interface Constants {
    //音乐的状态
    int STATE_STOP = 0;//停止播放
    int STATE_PLAY = 1;//播放状态
    int STATE_PAUSE = 2;//暂停
    int STATE_BACK = 3;
    int STATE_NEXT = 4;
    //循环模式
    int LOOP_LIST = 0;//列表循环
    int LOOP_PANDOM = 1;//随机
    int LOOP_SINGLE = 2;//单体循环
    int NOTIFY_SHOW = 5;
    int NOTIFY_CANCEL = 6;

    String BUTTON_PRESS = "press_button";//播放页面按下按钮时的广播频道
    String PROGRESS_CHANGE = "progress_change";//播放页面进度条发生变化的广播频道
    String MUSIC_PLAY = "music_play";//列表页面点击ITEM时播放音乐的广播频道
    String LOOP_CHANGE = "loop_change";//播放页面改面循环模式的广播频道
    String HANDLER_MUSIC_STATE = "music_state";//服务handler专门发送音乐状态的广播
    String RECENT_FLUSH = "recentList_flush";//最近播放列表发生变化的广播
    String SHOW_NOTIFY = "show_notify";//
    String LIKE_FLUSH="likelist_flush";//喜好列表发生变化的广播


    int FLAG_NEXT = 0;
    int FLAG_BACK = 1;
    int FLAG_SINGLE = 2;
}
