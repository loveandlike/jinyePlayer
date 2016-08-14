package com.example.administrator.jinyeplayer.lianxi;

/**
 * Created by Administrator on 2016/8/5.
 */
public class UserInfo {
    private String name;
    private long pass;

    public UserInfo(long pass, String name) {
        this.pass = pass;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getPass() {
        return pass;
    }

    public void setPass(long pass) {
        this.pass = pass;
    }
}
