package com.xlmiao.coolweather.presenter;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;
import com.xlmiao.coolweather.common.ActivityLifeCycleEvent;
import com.xlmiao.coolweather.common.HttpUtils;
import com.xlmiao.coolweather.common.IHttpService;
import com.xlmiao.coolweather.common.SubscriberCallBack;
import com.xlmiao.coolweather.contract.WeatherContract;
import com.xlmiao.coolweather.db.AQI;
import com.xlmiao.coolweather.db.Basic;
import com.xlmiao.coolweather.db.ForeCast;
import com.xlmiao.coolweather.db.HeWeather;
import com.xlmiao.coolweather.db.HeWeather.Weather;
import com.xlmiao.coolweather.db.Now;
import com.xlmiao.coolweather.db.Suggestion;
import com.xlmiao.coolweather.views.WeatherActivity;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;

/**
 * Created by XuLeMiao on 2016/12/25.
 * 该presenter 用于处理具体城市数据获取和逻辑处理
 */

public class WeatherPresenter extends BasePresenter implements WeatherContract.Presenter {
    private WeatherActivity activity;

    public WeatherPresenter(WeatherContract.View view) {
        this.activity = (WeatherActivity)view;
        view.setPresenter(this);
        attachView(activity);
    }
    @Override
    public void start() {
        loadWeatherInfo();
    }

    @Override
    public void loadWeatherInfo() {
        loadBingPic();
        Weather weather = getWeather();
        if(null != weather && null == activity.loadWeatherId()){
            this.currentAQI = weather.getAqi();
            this.currentBasic = weather.getBasic();
            this.currentNow = weather.getNow();
            this.currentForeCastList = weather.getForeCastsList();
            this.currentSuggestion = weather.getSuggestion();
            activity.showInfo();
            activity.setRefreshing();
        }else{
            Observable ob = iHttpService.getWeather(activity.loadWeatherId(), iHttpService.APP_KEY);
            HttpUtils.getHttpUtils().toSubscribe(ob, ActivityLifeCycleEvent.DESTROY, activity.lifecycleEventPublishSubject, new SubscriberCallBack<HeWeather>(activity) {

                @Override
                public void onCancelProgress() {
                    activity.setRefreshing();
                }

                @Override
                public void onSuccess(HeWeather weather) {
                    preWratherData(weather);
                    activity.showInfo();
                }

                @Override
                public void onFailure(String msg) {
                }

            });
        }
    }

    @Override
    public AQI getCurrentAQI() {
        return null != currentAQI ? currentAQI : new AQI();
    }

    @Override
    public Basic getCurrentBasic() {
        return null != currentBasic ? currentBasic : new Basic();
    }

    @Override
    public List<ForeCast> getCurrentForeCastList() {
        return null != currentForeCastList ? currentForeCastList : new ArrayList<ForeCast>();
    }

    @Override
    public Now getCurrentNow() {
        return null != currentNow?currentNow : new Now();
    }

    @Override
    public Suggestion getCurrentSuggestion() {
        return null != currentSuggestion ? currentSuggestion : new Suggestion();
    }

    @Override
    public void saveWeatherResponse(Weather weather) {
        String  weatherJsonString = new Gson().toJson(weather);
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(activity).edit();
        editor.putString("weather", weatherJsonString);
        editor.apply();
    }

    @Override
    public Weather getWeather() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(activity);
        String weatherString = prefs.getString("weather", null);
        if (weatherString != null) {
            // 有缓存时直接解析天气数据
            return new Gson().fromJson(weatherString, Weather.class);
        } else {
            return null;
        }
    }

    @Override
    public void loadBingPic() {
        SharedPreferences pref =  PreferenceManager.getDefaultSharedPreferences(activity);
        String bingPicPath = pref.getString("bing_pic", null);
        if (bingPicPath == null) {
            activity.setBingPicImg(bingPicPath);
            activity.setRefreshing();
        } else {

            /**
             * 因其数据未 无"" 包裹字符串 response 无法转换成bean json异常
             */
            Observable ob = iHttpService.getbingPic();
            HttpUtils.getHttpUtils().toSubscribe(ob, ActivityLifeCycleEvent.DESTROY, activity.lifecycleEventPublishSubject, new SubscriberCallBack<Object>(activity) {
                @Override
                public void onCancelProgress() {
                    activity.setRefreshing();
                }

                @Override
                public void onSuccess(Object bingPicPath) {
                    pref.edit().putString("bing_pic", bingPicPath.toString()).apply();
                    activity.setBingPicImg(bingPicPath.toString());
                }

                @Override
                public void onFailure(String msg) {
                    /**
                     * 数据本地获取 的假数据
                     */
                    String bingPicPath = IHttpService.BING_PIC_PATH;
                    pref.edit().putString("bing_pic", bingPicPath).apply();
                    activity.setBingPicImg(bingPicPath);
                }

            });
        }
    }


    /**
     * 提供当前界面使用
     */
    private AQI currentAQI;
    private Basic currentBasic;
    private List<ForeCast> currentForeCastList;
    private Now currentNow;
    private Suggestion currentSuggestion;
    private void preWratherData(HeWeather weather) {
        if(null != weather.getWeather()){
            if(weather.getWeather().size() > 0){
                if("ok".equals(weather.getWeather().get(0).getStatus())){
                    Weather currentWeather = weather.getWeather().get(0);
                    saveWeatherResponse(currentWeather);
                    this.currentAQI = currentWeather.getAqi();
                    this.currentBasic = currentWeather.getBasic();
                    this.currentNow = currentWeather.getNow();
                    this.currentForeCastList = currentWeather.getForeCastsList();
                    this.currentSuggestion = currentWeather.getSuggestion();
                }
            }
        }
    }
}
