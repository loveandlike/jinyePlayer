package com.example.administrator.jinyeplayer.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.widget.RemoteViews;

import com.example.administrator.jinyeplayer.Constants;
import com.example.administrator.jinyeplayer.MainActivity;
import com.example.administrator.jinyeplayer.R;
import com.example.administrator.jinyeplayer.bean.MusicInfo;
import com.example.administrator.jinyeplayer.utils.DBUtil;
import com.example.administrator.jinyeplayer.utils.MusicUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Administrator on 2016/6/29.
 */
public class MusicService extends Service {
    //音乐相关参数
    private ArrayList<MusicInfo> musicList;//音乐列表
    private MediaPlayer mediaPlayer;//媒体播放器
    private int index = -1;//音乐id
    private MusicInfo musicNow;//当前正在播放的音乐
    private int progressNow;//当前进度
    private int musicState = Constants.STATE_STOP;//音乐状态
    private int loopMode = Constants.LOOP_LIST;
    //通知栏相关参数
    private Notification notification;
    private int isShowNotify = Constants.NOTIFY_SHOW;
    private NotificationManager manager;
    //随机模式相关参数
    private int[] ranBox; //音乐随机角标数组
    private Random random = new Random();//产生随机数的对象
    private int ranIndex = -1;//随机角标数组角标变量
    private DBUtil dbUtil;

    //音乐准备完成监听
    private MediaPlayer.OnPreparedListener preparedListener = new MediaPlayer.OnPreparedListener() {
        @Override
        public void onPrepared(MediaPlayer mediaPlayer) {
            mediaPlayer.start();
            musicState = Constants.STATE_PLAY;
//            MusicUtil.writeInfo(musicNow);
            dbUtil.saveMusic(musicNow);
            Intent intent = new Intent(Constants.RECENT_FLUSH);
            sendBroadcast(intent);
            handler.sendEmptyMessage(0);
        }
    };
    //音乐完成的监听
    private MediaPlayer.OnCompletionListener onCompletionListener = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mediaPlayer) {
            changeMusic(Constants.FLAG_NEXT);
        }
    };
    //发送广播
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (mediaPlayer != null) {
                progressNow = mediaPlayer.getCurrentPosition();
            }
            initNotify();
            Intent intent = new Intent(Constants.HANDLER_MUSIC_STATE);
            intent.putExtra("index", index);
            intent.putExtra("progress", mediaPlayer.getCurrentPosition());
            intent.putExtra("musicState", musicState);
            if (musicState == Constants.STATE_PLAY) {
                handler.sendEmptyMessageDelayed(0, 300);
            }
            sendBroadcast(intent);
        }
    };
    //广播接收器，接收到的频道做操作
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();//接收到的频道辨别谁发来的广播
            switch (action) {
                case Constants.LOOP_CHANGE:
                    loopChange(intent);
                    break;
                case Constants.BUTTON_PRESS:
                    pressButton(intent);
                    break;
                case Constants.PROGRESS_CHANGE:
                    progressChange(intent);
                    break;
                case Constants.MUSIC_PLAY:
                    index = intent.getIntExtra("index", index);
                    playMusic();
                    break;
                case Constants.SHOW_NOTIFY:
                    isShowNotify = intent.getIntExtra("showNotify", isShowNotify);
                    break;
            }
        }
    };

    private void progressChange(Intent intent) {
        musicState = intent.getIntExtra("musicState", musicState);
        switch (musicState) {
            case Constants.STATE_PAUSE:
                pauseMusic();
                break;
            case Constants.STATE_PLAY:
                progressNow = intent.getIntExtra("progress", progressNow);
                resumeMusic();
                break;
        }

    }

    private void loopChange(Intent intent) {
        loopMode = intent.getIntExtra("loopMode", loopMode);
    }

    //按下按钮后的操作
    private void pressButton(Intent intent) {
        musicState = intent.getIntExtra("musicState", musicState);
        switch (musicState) {
            case Constants.STATE_PLAY:
                resumeMusic();
                break;
            case Constants.STATE_PAUSE:
                pauseMusic();
                break;
            case Constants.STATE_STOP:
                stopMusic();
                break;
            case Constants.STATE_BACK:
                changeMusic(Constants.FLAG_BACK);
                break;
            case Constants.STATE_NEXT:
                changeMusic(Constants.FLAG_NEXT);
                break;
        }
    }

    //切换歌曲
    public MusicService() {
        super();
    }

    private void changeMusic(int flag) {
        stopMusic();//先让歌曲停止
        switch (flag) {//根据操作 改变角标index
            case Constants.FLAG_BACK:
                index--;
                if (index < 0) {
                    index = musicList.size() - 1;
                }
                break;
            case Constants.FLAG_NEXT:    //下一曲
                nextMusic();
//                index++;
//                if (index > musicList.size()) {
//                    index = 0;
//                }
                break;
            case Constants.FLAG_SINGLE:
                break;
        }
        playMusic();//做完变换id,播放音乐
    }

    private void nextMusic() {
        stopMusic();//先让歌曲停止
        switch (loopMode) {
            case Constants.LOOP_LIST:
                index++;
                if (index > musicList.size()) {
                    index = 0;
                }
                break;
            case Constants.LOOP_SINGLE:
                break;
            case Constants.LOOP_PANDOM:
                //找到当前这首歌在随机列表中的角标
                if (ranIndex < 0) {
                    for (int i = 0; i < ranBox.length; i++) {
                        if (index == ranBox[i]) {
                            ranIndex = i;
                            break;
                        }
                    }
                }
                ranIndex++;
                index = ranBox[ranIndex];
                break;
        }
    }

    //停止播放
    private void stopMusic() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.reset();
        }
    }

    //音乐暂停的操作
    private void pauseMusic() {
        musicState = Constants.STATE_PAUSE;
        if (mediaPlayer != null) {
            mediaPlayer.pause();
            progressNow = mediaPlayer.getCurrentPosition();
        }
    }

    //开始
    private void resumeMusic() {
        stopMusic();//先让歌曲停止
//        musicState = Constants.STATE_PLAY;
        if (progressNow > 0) {
            mediaPlayer.start();
            mediaPlayer.seekTo(progressNow);
            handler.sendEmptyMessage(0);
        } else {
            playMusic();
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        musicList = MusicUtil.getMusicList(this);//初始化
        dbUtil = DBUtil.getsInstance(this);
        //注册广播各个频道
        IntentFilter filter = new IntentFilter();
        filter.addAction(Constants.LOOP_CHANGE);
        filter.addAction(Constants.BUTTON_PRESS);
        filter.addAction(Constants.PROGRESS_CHANGE);
        filter.addAction(Constants.MUSIC_PLAY);
        filter.addAction(Constants.RECENT_FLUSH);
        filter.addAction(Constants.SHOW_NOTIFY);
        registerReceiver(receiver, filter);
        manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        initRanBox();
    }

    //通知栏设置
    private void initNotify() {
        notification = new Notification();
        notification.flags = Notification.FLAG_AUTO_CANCEL;
        notification.icon = R.drawable.wallpaper_5250686;//设置图标
        notification.tickerText = "金爷正在演唱:" + musicNow.getTitle();
        //参数一包名   参数二是布局
        RemoteViews remoteviews = new RemoteViews(getPackageName(), R.layout.layout_notify);
        notification.contentView = remoteviews;//此处不能写反
        //参数一 id    参数二是文本
        remoteviews.setTextViewText(R.id.title_notify, musicNow.getTitle());//给通知歌曲设置文本
        remoteviews.setTextViewText(R.id.artist_notify, musicNow.getArtist());
//        remoteviews.setImageViewResource();//给图片栏图片设置资源id
        remoteviews.setProgressBar(R.id.pb_notify, musicNow.getDuration(), progressNow, false);
        // 给通知上的图片设置点击后，跳转到列表页面
        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pendingintent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
        remoteviews.setOnClickPendingIntent(R.id.img_notify, pendingintent);
//        Intent i2 = new Intent(Constants.MUSIC_PLAY);
//        PendingIntent p2 = PendingIntent.getBroadcast(this, 0, i2, PendingIntent.FLAG_ONE_SHOT);
        manager.notify(0x110, notification);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setOnPreparedListener(preparedListener);//监听
        mediaPlayer.setOnCompletionListener(onCompletionListener);//监听
        initRanBox();
        return super.onStartCommand(intent, flags, startId);

    }

    private void initRanBox() {
        ranBox = new int[musicList.size()];
        for (int i = 0; i < musicList.size(); i++) {
            ranBox[i] = random.nextInt(musicList.size());
            for (int j = 0; j < i; j++) {
                if (ranBox[i] == ranBox[j]) {
                    i--;
                    break;
                }
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);//解除注册
    }

    //播放音乐
    private void playMusic() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.reset();
        }
        musicNow = musicList.get(index);
        try {
            mediaPlayer.setDataSource(musicNow.getData());
            mediaPlayer.prepareAsync();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
