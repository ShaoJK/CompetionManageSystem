package com.wzdx.competionmanagesystem.App;

import android.app.Application;

/**
 * Created by sjk on 2017/1/24.
 */

public class MyApplication extends Application {
    private static MyApplication instance;

    public static final String COOKIES = "cookies";

    public static MyApplication getInstance() {
        return instance;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }
}
