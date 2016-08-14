package com.example.administrator.jinyeplayer.Base;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.jinyeplayer.R;

/**
 * Created by Administrator on 2016/6/24.
 */
public abstract class BaseActivity extends Activity {
    private ImageView left_icons1,right_icons1;
    private TextView title_actionbar;

    public boolean isShow=true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bindLayout();
        beforeInit();
        initViews();
        afterInit();
    }

    public abstract void bindLayout();

    public abstract void beforeInit();

    public abstract void initViews();

    public abstract void afterInit();
    public void isShowNotifyBar(boolean isShow) {
        if (!isShow) {
            //去掉通知栏
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }//去掉标题栏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
    }


    public  void  initActionbar(String title, int leftrid,boolean isShowRight,int rightRid){
        left_icons1= (ImageView) findViewById(R.id.left_actionbar);
        right_icons1= (ImageView) findViewById(R.id.right_actionbar);
        title_actionbar= (TextView) findViewById(R.id.title_actionbar);
        title_actionbar.setText(title);
        left_icons1.setImageResource(leftrid);
        if (isShowRight){
            right_icons1.setImageResource(rightRid);
        }else {
            right_icons1.setVisibility(View.GONE);
        }
    }


}
