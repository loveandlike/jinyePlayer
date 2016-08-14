package com.example.administrator.jinyeplayer.lianxi;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.Preference;

/**
 * Created by Administrator on 2016/8/5.
 */
public class SharePrenferencesHelper {
    private SharedPreferences sharedPreferences;
    private static SharePrenferencesHelper sInstance;

    public SharePrenferencesHelper(Context context) {
        sharedPreferences = context.getSharedPreferences("remember", Context.MODE_PRIVATE);
    }

    public static SharePrenferencesHelper getsInstance(Context context) {
        if (sInstance == null) {
            sInstance = new SharePrenferencesHelper(context);
        }
        return sInstance;
    }

    public void saveInfo(UserInfo info) {
       SharedPreferences.Editor editor= sharedPreferences.edit();
        editor.putString("user_name",info.getName());
        editor.putLong("user_pass",info.getPass());
        editor.commit();
    }

    public UserInfo getInfo() {
        String name=sharedPreferences.getString("user_name","");
        long pass=sharedPreferences.getLong("user_pass",0);
        return new UserInfo(pass,name);
    }



}
