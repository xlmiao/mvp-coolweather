package com.xlmiao.coolweather.views;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.xlmiao.coolweather.common.ActivityLifeCycleEvent;
import com.xlmiao.coolweather.common.AppManager;

import rx.subjects.PublishSubject;

/**
 * Created by XuLeMiao on 2016/12/23.
 *  封装 Activity 可添加
 */

public abstract class BaseActivity extends AppCompatActivity {

    public final PublishSubject<ActivityLifeCycleEvent>  lifecycleEventPublishSubject = PublishSubject.create();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        /**
         * 这里为了体验MVP模式没有使用
         */
      //  AppManager.getAppManager().addActivity(this); //添加 全局activity
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        lifecycleEventPublishSubject.onNext(ActivityLifeCycleEvent.CREATE);
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        initVariables(savedInstanceState);
        initView();
        loaderData();
    }

    protected abstract int getLayoutId();

    @Override
    protected void onResume() {
        lifecycleEventPublishSubject.onNext(ActivityLifeCycleEvent.RESUME);
        super.onResume();
    }

    @Override
    protected void onPause() {
        lifecycleEventPublishSubject.onNext(ActivityLifeCycleEvent.PAUSE);
        super.onPause();
    }

    @Override
    protected void onStop() {
        lifecycleEventPublishSubject.onNext(ActivityLifeCycleEvent.STOP);
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        lifecycleEventPublishSubject.onNext(ActivityLifeCycleEvent.DESTROY);
        //AppManager.getAppManager().finishActivity();
        super.onDestroy();
    }

    /**
     * 做初始化方面的工作,比如接收上一个界面的Intent
     * @param savedInstanceState
     */
    public abstract void initVariables(Bundle savedInstanceState);

    /**
     * 初始化控件
     */
    public abstract void initView();

    /**
     * 加载数据
     */
    public abstract void loaderData();

}
