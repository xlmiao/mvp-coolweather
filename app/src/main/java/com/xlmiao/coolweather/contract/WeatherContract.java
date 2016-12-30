package com.xlmiao.coolweather.contract;

import com.xlmiao.coolweather.db.AQI;
import com.xlmiao.coolweather.db.Basic;
import com.xlmiao.coolweather.db.ForeCast;
import com.xlmiao.coolweather.db.HeWeather;
import com.xlmiao.coolweather.db.Now;
import com.xlmiao.coolweather.db.Suggestion;

import java.util.List;

/**
 * Created by XuLeMiao on 2016/12/25.
 * 城市详情页契约接口
 */

public interface WeatherContract {
    interface View extends BaseView<WeatherContract.Presenter>{
        String loadWeatherId();
        void showInfo();
        void setBingPicImg(String bingPicPath);
        void setRefreshing();
        void setWeatherId(String weatherId);
        /*
        void changeTitle(String titleName);
        void changeCallBackBtn();*/
    }

    interface Presenter {
        void start();
        void loadWeatherInfo();
        AQI getCurrentAQI();
        Basic getCurrentBasic();
        List<ForeCast> getCurrentForeCastList();
        Now getCurrentNow();
        Suggestion getCurrentSuggestion();
        void saveWeatherResponse(HeWeather.Weather weather);
        HeWeather.Weather getWeather();
        void loadBingPic();
        /*void loadWeatherPrivinceInfo();
        void loadWeatherCityInfo(Object data);
        void loadWeatherCountyInfo(Object data);
        void callBack();*/
    }
}
