package com.example.administrator.jinyeplayer;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.example.administrator.jinyeplayer.adapter.MainViewPaggerAdapter;
import com.example.administrator.jinyeplayer.fragment.LikeListFragment;
import com.example.administrator.jinyeplayer.fragment.MainListFragment;
import com.example.administrator.jinyeplayer.fragment.RecentListFragment;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, ViewPager.OnPageChangeListener {
    private ViewPager vp_main;
    private Fragment[] frags = new Fragment[3];
    private MainViewPaggerAdapter adapter;
    private Button[] btns = new Button[3];
    private int[] btnIds = {R.id.btn1_main, R.id.btn2_main, R.id.btn3_main};

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        vp_main = (ViewPager) findViewById(R.id.vp_main);
        frags[0] = new MainListFragment();
        frags[1] = new RecentListFragment();
        frags[2] = new LikeListFragment();
        adapter = new MainViewPaggerAdapter(getSupportFragmentManager(), frags);
        vp_main.setAdapter(adapter);
        for (int i = 0; i < btns.length; i++) {
            btns[i] = (Button) findViewById(btnIds[i]);
            btns[i].setOnClickListener(this);
        }
        vp_main. setOnPageChangeListener(this);
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn1_main:
                setBtnEnable(0);
                break;
            case R.id.btn2_main:
                setBtnEnable(1);
                break;
            case R.id.btn3_main:
                setBtnEnable(2);
                break;
        }

    }

    private void setBtnEnable(int number) {
        vp_main.setCurrentItem(number);
        for (int i = 0; i < btns.length; i++) {
            if (i == number) {
                btns[i].setEnabled(false);
            } else {
                btns[i].setEnabled(true);
            }
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
         setBtnEnable(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
