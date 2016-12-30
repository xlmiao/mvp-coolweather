package com.xlmiao.coolweather.views;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentTransaction;
import com.xlmiao.coolweather.R;

/**
 * 应用启动界面 展示全国城市选项列表
 * 本应用主要用于体验 mvp模式结合retrofit2 rxjava okHttp3 。
 */

public class WeatherMainActivity extends BaseActivity {
    @Override
    protected int getLayoutId() {
        return R.layout.activity_weather_main;
    }

    @Override
    public void initVariables(Bundle savedInstanceState) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        if (prefs.getString("weather", null) != null) {
            Intent intent = new Intent(this, WeatherActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void initView() {
        replaceFragment(new ChooseAreaFragment());
    }

    private void replaceFragment(ChooseAreaFragment chooseAreaFragment) {
        FragmentTransaction fragmentTransaction =  getSupportFragmentManager ().beginTransaction();
        if(chooseAreaFragment.isAdded()){
            fragmentTransaction.show(chooseAreaFragment).commit();
        }else{
            fragmentTransaction.add(R.id.activity_weather_main, chooseAreaFragment).commit();
        }
    }

    @Override
    public void loaderData() {

    }
}
