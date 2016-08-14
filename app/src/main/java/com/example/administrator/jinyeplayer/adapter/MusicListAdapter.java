package com.example.administrator.jinyeplayer.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.administrator.jinyeplayer.MainActivity;
import com.example.administrator.jinyeplayer.R;
import com.example.administrator.jinyeplayer.bean.MusicInfo;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/6/29.
 */
public class MusicListAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<MusicInfo> musicList;
    private boolean reverse;

    public MusicListAdapter(Context context, boolean reverse) {
        this.reverse = reverse;
        this.context = context;
        musicList = new ArrayList<>();

    }

    public void setMusiclist(ArrayList<MusicInfo> musiclist) {
        this.musicList = musiclist;
        notifyDataSetChanged();//提示数据发生变动，请求刷新界面
    }

    @Override
    public int getCount() {
        return musicList.size();
    }

    @Override
    public Object getItem(int i) {
        return musicList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (reverse) {
            i = musicList.size() - 1 - i;
        }
        ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = LayoutInflater.from(context).inflate(R.layout.layout_list_item, viewGroup, false);
            holder.title = (TextView) view.findViewById(R.id.title_item_main);
            holder.artist = (TextView) view.findViewById(R.id.artist_item_main);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        holder.title.setText(musicList.get(i).getTitle());
        holder.artist.setText(musicList.get(i).getArtist());
        return view;
    }

    private class ViewHolder {
        TextView title, artist;
    }
}
