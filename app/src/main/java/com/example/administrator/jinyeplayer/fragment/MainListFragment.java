package com.example.administrator.jinyeplayer.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.administrator.jinyeplayer.Constants;
import com.example.administrator.jinyeplayer.PlayActivity;
import com.example.administrator.jinyeplayer.R;
import com.example.administrator.jinyeplayer.adapter.MusicListAdapter;
import com.example.administrator.jinyeplayer.bean.MusicInfo;
import com.example.administrator.jinyeplayer.service.MusicService;
import com.example.administrator.jinyeplayer.utils.MusicUtil;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/7/6.
 */
public class MainListFragment extends Fragment implements AdapterView.OnItemClickListener {
    private ListView iv_main;
    private ArrayList<MusicInfo> musicList;
    private MusicListAdapter adapter;
    private int index = -1;
    private LocalBroadcastManager manager;
    private BroadcastReceiver receiver=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_musiclist,container,false);
        iv_main = (ListView)view.findViewById(R.id.iv_musiclist);
        adapter = new MusicListAdapter(getActivity(),false);
        iv_main.setAdapter(adapter);
        //开启子线程  用于读取音乐文件
        new Thread(new Runnable() {
            @Override
            public void run() {
                musicList = MusicUtil.getMusicList(getActivity());
                getActivity().runOnUiThread(new Runnable() {//在ui线程中运行的程序
                    @Override
                    public void run() {
                        //给adapter设置音乐列表，并且刷新页面
                        adapter.setMusiclist(musicList);
                        Intent intent = new Intent(getActivity(), MusicService.class);
                        getActivity().startService(intent);
                    }
                });
            }
        }).start();
        iv_main.setOnItemClickListener(this);
        IntentFilter filter=new IntentFilter(Constants.HANDLER_MUSIC_STATE);
        getActivity().registerReceiver(receiver,filter);

        return view;
    }

    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        //发送广播
        Intent intent = new Intent(Constants.MUSIC_PLAY);
        Intent intent1=new Intent(getActivity(),PlayActivity.class);
        startActivity(intent1);
        intent1.putExtra("l",i);
        if (index == i) {
            return;
        }
        index = i;//避免点同一首歌从头开始播放
        intent.putExtra("index", i);
        getActivity().sendBroadcast(intent);
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(receiver);
    }
}
