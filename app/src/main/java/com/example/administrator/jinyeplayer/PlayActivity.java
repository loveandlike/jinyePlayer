package com.example.administrator.jinyeplayer;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.administrator.jinyeplayer.bean.MusicInfo;
import com.example.administrator.jinyeplayer.utils.MusicUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Administrator on 2016/6/30.
 */
public class PlayActivity extends Activity {
    private ArrayList<MusicInfo> musicList;
    private int index = -1;
    private Button notify_button, loop_play, djsc;
    private int LoopMode = Constants.LOOP_LIST;
    private TextView title_play, artist_play, total_play, current_play;
    private int progressNow;
    private SeekBar sb_play;
    private ImageView start_play, back_play, next_play;
    private int musicState = Constants.STATE_STOP;
    private int isShowNotify = Constants.NOTIFY_SHOW;
    Random r = new Random();
    char[] letter = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
    Handler handler2 = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            title_play.setTextColor(Color.parseColor(rancoler()));
            handler2.sendEmptyMessageDelayed(0, 2000);
        }
    };
    private SimpleDateFormat format = new SimpleDateFormat("mm:ss");
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            progressNow = intent.getIntExtra("progress", progressNow);
            sb_play.setProgress(progressNow);//设置进度条
            current_play.setText(format.format(progressNow));//设置随进度的播放时间
            isShowNotify = intent.getIntExtra("showNotify", isShowNotify);
            notify_button.setText(isShowNotify != Constants.NOTIFY_SHOW ? "开启通知" : "关闭通知");

            LoopMode = intent.getIntExtra("loopMode", LoopMode);
            switch (LoopMode) {
                case Constants.LOOP_LIST:
                    loop_play.setText("列表循环");
                    break;
                case Constants.LOOP_PANDOM:
                    loop_play.setText("全体随机");
                    break;
                case Constants.LOOP_SINGLE:
                    loop_play.setText("单体循环");
                    break;
            }

            musicState = intent.getIntExtra("musicState", musicState);
            if (musicState == Constants.STATE_PLAY) {
                musicState = Constants.STATE_PLAY;
                start_play.setImageResource(R.drawable.btn_pause);
            } else {
                musicState = Constants.STATE_PAUSE;
                start_play.setImageResource(R.drawable.btn_start);
            }
            int tempIndex = intent.getIntExtra("index", index);
            boolean b = musicList.get(tempIndex).isLike();
            djsc.setText(b ? "已收藏" : "收藏该歌曲");
            if (tempIndex != index) {
                index = tempIndex;
                MusicInfo musicInfo = musicList.get(index);
                title_play.setText("金爷唱的:" + musicInfo.getTitle());
                artist_play.setText("---" + musicInfo.getArtist() + "说：真好听");
                total_play.setText(format.format(musicInfo.getDuration()));
                sb_play.setMax(musicInfo.getDuration());
                handler2.sendEmptyMessage(0);//颜色的
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);
        musicList = MusicUtil.getMusicList(this);
        index = getIntent().getIntExtra("index", index);
        initViews();
        IntentFilter filter = new IntentFilter(Constants.HANDLER_MUSIC_STATE);
        registerReceiver(receiver, filter);
        initDates();
    }

    private void initDates() {
        if (index >= 0) {
            MusicInfo musicInfo = musicList.get(index);
            title_play.setText("金爷唱的:" + musicInfo.getTitle());
            artist_play.setText("---" + musicInfo.getArtist() + "说：真好听");
            total_play.setText(format.format(musicInfo.getDuration()));
            sb_play.setMax(musicInfo.getDuration());
        }
        IntentFilter filter = new IntentFilter(Constants.HANDLER_MUSIC_STATE);
        registerReceiver(receiver, filter);
    }

    private void initViews() {
        title_play = (TextView) findViewById(R.id.title_play);
        artist_play = (TextView) findViewById(R.id.aritist_play);
        total_play = (TextView) findViewById(R.id.total_play);
        current_play = (TextView) findViewById(R.id.current_play);
        sb_play = (SeekBar) findViewById(R.id.sb_play);
        start_play = (ImageView) findViewById(R.id.start_stop_play);
        back_play = (ImageView) findViewById(R.id.back_play);
        next_play = (ImageView) findViewById(R.id.next_play);
        notify_button = (Button) findViewById(R.id.notify_button);
        loop_play = (Button) findViewById(R.id.loop_play);
        djsc = (Button) findViewById(R.id.djsc);
        notify_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isShowNotify == Constants.NOTIFY_SHOW) {
                    isShowNotify = Constants.NOTIFY_CANCEL;
                } else {
                    isShowNotify = Constants.NOTIFY_SHOW;
                }
                Intent intent = new Intent(Constants.SHOW_NOTIFY);
                intent.putExtra("showNotify", isShowNotify);
                sendBroadcast(intent);
            }
        });
        loop_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (LoopMode) {
                    case Constants.LOOP_LIST:
                        LoopMode = Constants.LOOP_PANDOM;
                        loop_play.setText("全体随机");
                        break;
                    case Constants.LOOP_PANDOM:
                        LoopMode = Constants.LOOP_SINGLE;
                        loop_play.setText("单体循环");
                        break;
                    case Constants.LOOP_SINGLE:
                        LoopMode = Constants.LOOP_LIST;
                        loop_play.setText("列表循环");
                        break;
                }
                Intent intent = new Intent(Constants.LOOP_CHANGE);
                intent.putExtra("loopMode", LoopMode);
                sendBroadcast(intent);
            }
        });
        djsc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean b = musicList.get(index).isLike();
                djsc.setText(b ? "收藏该歌曲" : "已收藏");
                if (b) {
                    MusicUtil.writeLikeInfo(musicList.get(index)); //将当前这首歌放入喜爱列表
                } else {
                    MusicUtil.removeLikeINfo(musicList.get(index));//
                }
                musicList.get(index).setLike(!b);
                //将当前这首歌放入喜爱列表
                MusicUtil.writeLikeInfo(musicList.get(index));
                Intent intent = new Intent(Constants.LIKE_FLUSH);
                sendBroadcast(intent);
            }
        });
    }

    public void playClick(View view) {
        Intent intent = new Intent(Constants.BUTTON_PRESS);
        switch (view.getId()) {
            case R.id.start_stop_play:
                if (musicState == Constants.STATE_PLAY) {
                    musicState = Constants.STATE_PAUSE;
                    start_play.setImageResource(R.drawable.btn_start);
                } else {
                    musicState = Constants.STATE_PLAY;
                    start_play.setImageResource(R.drawable.btn_pause);
                }
                break;
            case R.id.back_play:
                musicState = Constants.STATE_BACK;
                break;
            case R.id.next_play:
                musicState = Constants.STATE_NEXT;
                break;
        }
        intent.putExtra("musicState", musicState);
        intent.putExtra("index", index);
        sendBroadcast(intent);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }

    //随机颜色的方法
    private String rancoler() {
        StringBuffer sb = new StringBuffer("#");
        for (int i = 0; i < 8; i++) {
            sb.append(letter[r.nextInt(letter.length)]);
        }
        return sb.toString();
    }
}
