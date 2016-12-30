package com.xlmiao.coolweather.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;

import com.xlmiao.coolweather.common.HttpApi;
import com.xlmiao.coolweather.common.HttpUtils;

import rx.Observable;
import rx.Subscriber;

/**
 * Created by XuLeMiao on 2016/12/26.
 * AutoUpdateSerice 自动刷新与 mpv rxjava retrofit 无关就不实现了 且功能过于简单无实现意义
 */

public class AutoUpdateSerice extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {

        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        UpdateBingpic();
        UpdateWeather();
        AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);
        int anHour = 8 * 60 * 60 * 1000; // 这是8小时的毫秒数
        long triggerAtTime = SystemClock.elapsedRealtime() + anHour;
        Intent i = new Intent(this, AutoUpdateSerice.class);
        PendingIntent pi = PendingIntent.getService(this, 0, i, 0);
        manager.cancel(pi);
        manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtTime, pi);
        return super.onStartCommand(intent, flags, startId);
    }

    /**
     * 城市数据自动刷新 功能相同未写
     */
    private void UpdateWeather(){

    }

    /**
     * 功能相同未写
     */
    private void UpdateBingpic(){
        Observable ob = HttpApi.getDefaultApi().getbingPic();
        HttpUtils.getHttpUtils().toSubscribe(ob, new Subscriber<String>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(String response) {
                SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(AutoUpdateSerice.this).edit();
                editor.putString("bing_pic", response);
                editor.apply();
            }
        });

    }

}
