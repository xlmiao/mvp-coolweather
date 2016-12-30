package com.xlmiao.coolweather.common;

import android.app.Application;
import android.content.Context;

import org.litepal.LitePal;

/**
 * Created by XuLeMiao on 2016/12/23.
 */

public class MyApplication extends Application {
    private static Context appContext;

    @Override
    public void onCreate() {
        super.onCreate();
        appContext = getApplicationContext();
        LitePal.initialize(this);
    }

    public static Context getAppContext(){
        return appContext;
    }
}
