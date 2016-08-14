package com.example.administrator.jinyeplayer.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.example.administrator.jinyeplayer.Constants;
import com.example.administrator.jinyeplayer.PlayActivity;
import com.example.administrator.jinyeplayer.R;
import com.example.administrator.jinyeplayer.adapter.MusicListAdapter;
import com.example.administrator.jinyeplayer.bean.MusicInfo;
import com.example.administrator.jinyeplayer.utils.DBUtil;
import com.example.administrator.jinyeplayer.utils.MusicUtil;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/7/6.
 */
public class RecentListFragment extends Fragment implements AdapterView.OnItemClickListener {
    private ListView Lv_musiclist;
    private ArrayList<MusicInfo> recentList = new ArrayList<>();
    private MusicListAdapter adapter;
    private int index = -1;
    private DBUtil dbUtil;
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
//            recentList = MusicUtil.getRecentList();
            recentList=dbUtil.getRecentList();
            adapter.setMusiclist(recentList);
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_musiclist, container, false);
        Lv_musiclist = (ListView) view.findViewById(R.id.iv_musiclist);
        adapter = new MusicListAdapter(getActivity(), true);
        dbUtil = DBUtil.getsInstance(getActivity());
        Lv_musiclist.setAdapter(adapter);
        Lv_musiclist.setOnItemClickListener(this);
        IntentFilter filter = new IntentFilter(Constants.RECENT_FLUSH);
        getActivity().registerReceiver(receiver, filter);
//        recentList = MusicUtil.getRecentList();
        recentList=dbUtil.getRecentList();
        adapter.setMusiclist(recentList);
        return view;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Intent intent = new Intent(Constants.MUSIC_PLAY);
        Intent intent1 = new Intent(getActivity(), PlayActivity.class);
        startActivity(intent1);
        intent1.putExtra("l", i);
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
