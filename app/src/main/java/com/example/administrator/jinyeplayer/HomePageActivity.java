package com.example.administrator.jinyeplayer;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.example.administrator.jinyeplayer.Base.BaseActivity;
public class HomePageActivity extends BaseActivity  {
    TextView bdyy,dt,lk,gj,gd,xz;
    @Override
    public void bindLayout() {
        setContentView(R.layout.homepage_activity);
    }

    @Override
    public void beforeInit() {

    }

    @Override
    public void initViews() {
        dt= (TextView) findViewById(R.id.dt);
        xz= (TextView) findViewById(R.id.xz);
        gd= (TextView) findViewById(R.id.gd);
        gj= (TextView) findViewById(R.id.gj);
        lk= (TextView) findViewById(R.id.lk);
        bdyy = (TextView) findViewById(R.id.bdyy);
        initActionbar("金爷演唱会",R.drawable.wallpaper_5234860,true,R.drawable.wallpaper_5249815);
    }

    @Override
    public void afterInit() {
        bdyy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomePageActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    }


